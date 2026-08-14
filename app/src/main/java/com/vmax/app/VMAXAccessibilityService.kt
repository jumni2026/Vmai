package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

// Core contracts
import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.workflow.ExecutionTracker
import com.vmax.workflow.ActionOrchestrator
import com.vmax.common.Result
import com.vmax.runtime.MetricsCollector
import com.vmax.runtime.ExecutionRecorder

// Android-specific implementations
import com.vmax.app.AndroidLogger
import com.vmax.app.AndroidExecutionHistoryStore
import com.vmax.app.AndroidExecutionRecorder
import com.vmax.app.AndroidMetricsCollector

// Core Intelligence
import com.vmax.core_intelligence.ScreenAnalyzer
import com.vmax.core_intelligence.UIEvidenceCollector
import com.vmax.core_intelligence.TextClassifier
import com.vmax.core_intelligence.OcrResult

class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_EXECUTION_SERVICE"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"
    }

    // ----------------------------------------------------------------
    // CONFIGURATION DATA
    // ----------------------------------------------------------------

    private var targetFrom: String = ""
    private var targetTo: String = ""
    private var targetDate: String = ""
    private var targetTrain: String = ""
    private var targetClass: String = ""
    private var passengerName: String = ""
    private var passengerAge: String = ""
    private var passengerGender: String = ""
    private var passengerMeal: String = ""

    private var currentSessionId: String = ""
    private var isServiceReady: Boolean = false

    // ----------------------------------------------------------------
    // STATE MACHINE (Pure State Holder)
    // ----------------------------------------------------------------

    private enum class State {
        IDLE, ARMED, USER_BOUNDARY, STOPPED
    }

    private var currentState = State.IDLE

    private lateinit var orchestrator: ActionOrchestrator
    private lateinit var tracker: ExecutionTracker
    private lateinit var analyzer: ScreenAnalyzer
    private lateinit var classifier: TextClassifier

    // ----------------------------------------------------------------
    // HISTORY SYSTEM COMPONENTS
    // ----------------------------------------------------------------
    private lateinit var historyStore: AndroidExecutionHistoryStore
    private lateinit var recorder: ExecutionRecorder
    private lateinit var metrics: MetricsCollector

    // ----------------------------------------------------------------
    // PUBLIC WORKFLOW CONTRACT
    // ----------------------------------------------------------------

    fun startWorkflow(
        from: String, to: String, date: String,
        train: String, trainClass: String,
        name: String, age: String, gender: String, meal: String
    ) {
        if (!isServiceReady) {
            Log.e(TAG, "Service not ready. Cannot start workflow.")
            return
        }

        if (currentState != State.IDLE) {
            Log.w(TAG, "Workflow already active. Ignoring start request.")
            return
        }

        if (from.isBlank() || to.isBlank() || train.isBlank() || trainClass.isBlank() ||
            name.isBlank() || age.isBlank() || gender.isBlank()) {
            Log.e(TAG, "Invalid configuration passed to startWorkflow.")
            return
        }

        targetFrom = from
        targetTo = to
        targetDate = date
        targetTrain = train
        targetClass = trainClass
        passengerName = name
        passengerAge = age
        passengerGender = gender
        passengerMeal = meal

        currentSessionId = "SESSION_${System.currentTimeMillis()}"

        tracker.startSession(currentSessionId)
        metrics.startMetrics(currentSessionId)
        recorder.recordEvent(ExecutionEvent.SessionStarted(currentSessionId))

        currentState = State.ARMED
        Log.i(TAG, "Workflow armed with session: $currentSessionId")
    }

    fun stopWorkflow() {
        if (currentState == State.STOPPED || currentState == State.IDLE) {
            Log.w(TAG, "Workflow already stopped or idle. Ignoring stop request.")
            return
        }

        if (currentSessionId.isNotEmpty()) {
            tracker.stopSession(currentSessionId)
            recorder.recordEvent(ExecutionEvent.SessionStopped(currentSessionId))
            metrics.stopMetrics(currentSessionId, "STOPPED")
        }

        currentState = State.STOPPED
        Log.i(TAG, "Workflow stopped.")
    }

    // ----------------------------------------------------------------
    // SERVICE LIFECYCLE
    // ----------------------------------------------------------------

    override fun onServiceConnected() {
        super.onServiceConnected()

        val logger = AndroidLogger()
        // ✅ CORRECT CONSTRUCTOR: AndroidActionExecutor(accessibilityService) accepts only one parameter
        val executor = AndroidActionExecutor(this)
        tracker = ExecutionTracker(logger)
        orchestrator = ActionOrchestrator(executor, tracker)

        historyStore = AndroidExecutionHistoryStore(this)
        recorder = AndroidExecutionRecorder(historyStore)
        metrics = AndroidMetricsCollector()

        // ✅ CORRECT: UIEvidenceCollector expects a Logger
        val evidenceCollector = UIEvidenceCollector(logger)
        classifier = TextClassifier()
        analyzer = ScreenAnalyzer(evidenceCollector, logger)

        isServiceReady = true
        Log.i(TAG, "VMAX Service Connected. Ready for Workflow.")
    }

    // ----------------------------------------------------------------
    // ACCESSIBILITY EVENT (Pure Intelligence-Driven)
    // ----------------------------------------------------------------

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (!isServiceReady) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName != IRCTC_PACKAGE) return

        val root = rootInActiveWindow ?: return

        try {
            if (currentSessionId.isEmpty()) {
                Log.d(TAG, "No active session. Ignoring all events.")
                return
            }

            // Step 1: Get Analysis from ScreenAnalyzer
            val analysis = analyzer.analyzeCurrentScreen()
            
            // Step 2: Security Check via TextClassifier
            // ✅ CORRECT: OcrResult uses the exact constructor from OcrResult.kt
            val ocrResult = OcrResult(
                screenId = currentSessionId,
                timestamp = System.currentTimeMillis(),
                fullText = analysis.evidence?.ocrEvidence?.fullText ?: "",
                textBlocks = analysis.evidence?.ocrEvidence?.rawBlocks ?: emptyList(),
                language = "unknown"
            )

            if (classifier.isSensitiveScreen(ocrResult)) {
                if (currentState != State.USER_BOUNDARY) {
                    recorder.recordEvent(
                        ExecutionEvent.SessionError(
                            sessionId = currentSessionId,
                            errorCode = "SECURITY_BOUNDARY",
                            errorMessage = "Sensitive screen detected. Automation paused."
                        )
                    )
                    metrics.stopMetrics(currentSessionId, "USER_BOUNDARY")
                    currentState = State.USER_BOUNDARY
                    Log.w(TAG, "Sensitive screen detected. Locking to USER_BOUNDARY.")
                }
                return
            }

            if (currentState == State.USER_BOUNDARY || currentState == State.STOPPED) {
                return
            }

            // Step 3: Action Discovery (No Legacy Handlers)
            when (val suggestedAction = analysis.suggestedAction) {
                ScreenAnalyzer.SuggestedAction.SELECT_TRAIN -> {
                    // The extractedData "train_number" is an OCR string, NOT a viewIdResourceName.
                    // The actual UI targetId must be resolved by ScreenAnalyzer's evidence.
                    val trainEvidence = analysis.evidence?.uiElements?.find { 
                        it.type == "button" || it.text.contains("TRAIN") 
                    }
                    trainEvidence?.let { evidence ->
                        executeClick(evidence.id) { success ->
                            if (success) {
                                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                                currentState = State.ARMED
                            }
                        }
                    }
                }
                ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_DETAILS -> {
                    val nameField = analysis.evidence?.uiElements?.find { it.text.contains("Name", ignoreCase = true) }
                    nameField?.let { evidence ->
                        executeSetText(evidence.id, passengerName) { success ->
                            if (success) {
                                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                            }
                        }
                    }
                }
                ScreenAnalyzer.SuggestedAction.REVIEW_AND_PROCEED -> {
                    val reviewBtn = analysis.evidence?.uiElements?.find { it.text.contains("REVIEW", ignoreCase = true) }
                    reviewBtn?.let { evidence ->
                        executeClick(evidence.id) { success ->
                            if (success) {
                                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                                currentState = State.STOPPED
                                Log.i(TAG, "Review Journey Details clicked. Automation stopped.")
                            }
                        }
                    }
                }
                else -> {
                    // No actionable intelligence, wait for next event
                }
            }

        } finally {
            root.recycle()
        }
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS (Strict Contract Compliance)
    // ----------------------------------------------------------------

    private fun executeClick(targetId: String, onDispatched: (Boolean) -> Unit) {
        if (targetId.isEmpty()) {
            Log.w(TAG, "Click failed: targetId is empty")
            onDispatched(false)
            return
        }

        val result = orchestrator.click(
            targetId = targetId,
            sessionId = currentSessionId
        )

        when (result) {
            is Result.Success -> onDispatched(true)
            is Result.Error -> {
                Log.e(TAG, "Click failed: ${result.error.message}")
                onDispatched(false)
            }
        }
    }

    private fun executeSetText(targetId: String, text: String, onDispatched: (Boolean) -> Unit) {
        if (targetId.isEmpty()) {
            Log.w(TAG, "SetText failed: targetId is empty")
            onDispatched(false)
            return
        }

        val result = orchestrator.setText(
            targetId = targetId,
            text = text,
            sessionId = currentSessionId
        )

        when (result) {
            is Result.Success -> onDispatched(true)
            is Result.Error -> {
                Log.e(TAG, "SetText failed: ${result.error.message}")
                onDispatched(false)
            }
        }
    }

    // ----------------------------------------------------------------
    // HELPER: Action-Aware Failure
    // ----------------------------------------------------------------

    private fun onActionFailed(reason: String, actionType: ActionExecutor.ActionType) {
        Log.w(TAG, "Action failed: $reason")
        metrics.recordAction(currentSessionId, false, actionType, reason)
    }

    override fun onInterrupt() {
        Log.w(TAG, "Service interrupted.")
        if (currentSessionId.isNotEmpty() && currentState != State.STOPPED) {
            stopWorkflow()
        }
    }
}
