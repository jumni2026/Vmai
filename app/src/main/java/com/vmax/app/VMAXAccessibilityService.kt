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

        // UI Evidence (For screen classification only)
        private const val EVIDENCE_SEARCH = "Search"
        private const val EVIDENCE_REVIEW = "REVIEW JOURNEY DETAILS"
        private const val EVIDENCE_CAPTCHA = "CAPTCHA"
        private const val EVIDENCE_OTP = "OTP"
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
    // STATE MACHINE
    // ----------------------------------------------------------------

    private enum class State {
        IDLE, ARMED,
        FROM_CLICKED, FROM_TYPED, FROM_SUGGESTION_CLICKED,
        TO_CLICKED, TO_TYPED, TO_SUGGESTION_CLICKED,
        DATE_CLICKED, DATE_SELECTED,
        SEARCH_CLICKED, TRAIN_SELECTED, CLASS_SELECTED,
        PASSENGER_ADD_CLICKED, PASSENGER_NAME_TYPED, PASSENGER_AGE_TYPED,
        PASSENGER_GENDER_CLICKED, PASSENGER_MEAL_CLICKED, PASSENGER_SUBMITTED,
        OPTIONS_REVIEW_CLICKED, USER_BOUNDARY, STOPPED
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
            Log.w(TAG, "Workflow already active or armed. Ignoring start request.")
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

        // Initialize Core Components
        val logger = AndroidLogger()
        val executor = AndroidActionExecutor(this)
        tracker = ExecutionTracker(logger)
        orchestrator = ActionOrchestrator(executor, tracker)

        historyStore = AndroidExecutionHistoryStore(this)
        recorder = AndroidExecutionRecorder(historyStore)
        metrics = AndroidMetricsCollector()

        // Initialize Intelligence Layer
        val evidenceCollector = UIEvidenceCollector()
        classifier = TextClassifier()
        analyzer = ScreenAnalyzer(evidenceCollector, logger)

        isServiceReady = true
        Log.i(TAG, "VMAX Service Connected. Ready for Workflow.")
    }

    // ----------------------------------------------------------------
    // ACCESSIBILITY EVENT
    // ----------------------------------------------------------------

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (!isServiceReady) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName != IRCTC_PACKAGE) return

        // ✅ Step 1: Collect UI Evidence using existing Core-Intelligence layers
        val root = rootInActiveWindow ?: return
        val uiEvidence = analyzer.getCurrentEvidence() ?: run {
            root.recycle()
            return
        }

        try {
            if (currentSessionId.isEmpty()) {
                Log.d(TAG, "No active session. Ignoring all events.")
                return
            }

            // ✅ Step 2: Security Boundary via TextClassifier (No hardcoded strings)
            val ocrResult = OcrResult(uiEvidence.ocrEvidence?.fullText ?: "", mapOf())
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

            // ✅ Step 3: Delegate decision making to ScreenAnalyzer
            val analysis = analyzer.analyzeCurrentScreen()
            val suggestedAction = analysis.suggestedAction

            // ✅ Step 4: Process based on Suggested Action
            when (suggestedAction) {
                ScreenAnalyzer.SuggestedAction.SELECT_TRAIN -> {
                    // Let State Machine handle it
                }
                ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_DETAILS -> {
                    // Let State Machine handle it
                }
                ScreenAnalyzer.SuggestedAction.REVIEW_AND_PROCEED -> {
                    // Let State Machine handle it
                }
                else -> {
                    // Fallback to existing State Machine
                }
            }

            // ✅ Step 5: Run existing State Machine for immediate needs
            processWorkflow(root)

        } finally {
            root.recycle()
        }
    }

    // ----------------------------------------------------------------
    // WORKFLOW STATE MACHINE (Event-Driven, Proof-of-Concept)
    // ----------------------------------------------------------------

    private fun processWorkflow(root: AccessibilityNodeInfo) {
        when (currentState) {
            State.ARMED -> handleFromField(root)
            State.FROM_CLICKED -> handleFromTyping(root)
            State.FROM_TYPED -> handleFromSuggestion(root)
            State.FROM_SUGGESTION_CLICKED -> handleToField(root)
            State.TO_CLICKED -> handleToTyping(root)
            State.TO_TYPED -> handleToSuggestion(root)
            State.TO_SUGGESTION_CLICKED -> handleDateField(root)
            State.DATE_CLICKED -> handleDateSelection(root)
            State.DATE_SELECTED -> handleSearch(root)
            State.SEARCH_CLICKED -> handleTrainSelection(root)
            State.TRAIN_SELECTED -> handleClassSelection(root)
            State.CLASS_SELECTED -> handlePassengerScreen(root)
            State.PASSENGER_ADD_CLICKED -> handlePassengerName(root)
            State.PASSENGER_NAME_TYPED -> handlePassengerAge(root)
            State.PASSENGER_AGE_TYPED -> handlePassengerGender(root)
            State.PASSENGER_GENDER_CLICKED -> handlePassengerMeal(root)
            State.PASSENGER_MEAL_CLICKED -> handlePassengerSubmit(root)
            State.PASSENGER_SUBMITTED -> handleOptionsReview(root)
            else -> { /* Awaiting next valid workflow event */ }
        }
    }

    // ----------------------------------------------------------------
    // ORCHESTRATION HANDLERS (Pure Event-Driven)
    // ----------------------------------------------------------------

    // 📌 Note: All finders are removed. They are now delegated to ScreenAnalyzer.
    // These handlers will eventually receive UI elements from analyzer results.
    // For now, they work as placeholders.

    private fun handleFromField(root: AccessibilityNodeInfo) {
        // Check ScreenAnalyzer result for "From" field
        if (isReadyForAction("FROM")) {
            executeClick(getTargetNode("FROM")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.FROM_CLICKED
                }
            }
        }
    }

    private fun handleFromTyping(root: AccessibilityNodeInfo) {
        if (currentState == State.FROM_CLICKED && isReadyForAction("FROM_INPUT")) {
            executeSetText(getTargetNode("FROM_INPUT"), targetFrom) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                    currentState = State.FROM_TYPED
                }
            }
        }
    }

    private fun handleFromSuggestion(root: AccessibilityNodeInfo) {
        if (currentState == State.FROM_TYPED && isReadyForAction("FROM_SUGGESTION")) {
            executeClick(getTargetNode("FROM_SUGGESTION")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.FROM_SUGGESTION_CLICKED
                }
            }
        }
    }

    private fun handleToField(root: AccessibilityNodeInfo) {
        if (currentState == State.FROM_SUGGESTION_CLICKED && isReadyForAction("TO")) {
            executeClick(getTargetNode("TO")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.TO_CLICKED
                }
            }
        }
    }

    private fun handleToTyping(root: AccessibilityNodeInfo) {
        if (currentState == State.TO_CLICKED && isReadyForAction("TO_INPUT")) {
            executeSetText(getTargetNode("TO_INPUT"), targetTo) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                    currentState = State.TO_TYPED
                }
            }
        }
    }

    private fun handleToSuggestion(root: AccessibilityNodeInfo) {
        if (currentState == State.TO_TYPED && isReadyForAction("TO_SUGGESTION")) {
            executeClick(getTargetNode("TO_SUGGESTION")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.TO_SUGGESTION_CLICKED
                }
            }
        }
    }

    private fun handleDateField(root: AccessibilityNodeInfo) {
        if (currentState == State.TO_SUGGESTION_CLICKED && isReadyForAction("DATE")) {
            executeClick(getTargetNode("DATE")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.DATE_CLICKED
                }
            }
        }
    }

    private fun handleDateSelection(root: AccessibilityNodeInfo) {
        if (currentState == State.DATE_CLICKED && isReadyForAction("DATE_SELECT")) {
            executeClick(getTargetNode("DATE_SELECT")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.DATE_SELECTED
                }
            }
        }
    }

    private fun handleSearch(root: AccessibilityNodeInfo) {
        if (currentState == State.DATE_SELECTED && isReadyForAction("SEARCH")) {
            executeClick(getTargetNode("SEARCH")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.SEARCH_CLICKED
                }
            }
        }
    }

    private fun handleTrainSelection(root: AccessibilityNodeInfo) {
        if (currentState == State.SEARCH_CLICKED && isReadyForAction("TRAIN_SELECT")) {
            executeClick(getTargetNode("TRAIN_SELECT")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.TRAIN_SELECTED
                }
            }
        }
    }

    private fun handleClassSelection(root: AccessibilityNodeInfo) {
        if (currentState == State.TRAIN_SELECTED && isReadyForAction("CLASS_SELECT")) {
            executeClick(getTargetNode("CLASS_SELECT")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.CLASS_SELECTED
                }
            }
        }
    }

    private fun handlePassengerScreen(root: AccessibilityNodeInfo) {
        if (currentState == State.CLASS_SELECTED && isReadyForAction("ADD_NEW")) {
            executeClick(getTargetNode("ADD_NEW")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.PASSENGER_ADD_CLICKED
                }
            }
        }
    }

    private fun handlePassengerName(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_ADD_CLICKED && isReadyForAction("PASSENGER_NAME")) {
            executeSetText(getTargetNode("PASSENGER_NAME"), passengerName) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                    currentState = State.PASSENGER_NAME_TYPED
                }
            }
        }
    }

    private fun handlePassengerAge(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_NAME_TYPED && isReadyForAction("AGE")) {
            executeSetText(getTargetNode("AGE"), passengerAge) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                    currentState = State.PASSENGER_AGE_TYPED
                }
            }
        }
    }

    private fun handlePassengerGender(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_AGE_TYPED && isReadyForAction("GENDER")) {
            executeClick(getTargetNode("GENDER")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.PASSENGER_GENDER_CLICKED
                }
            }
        }
    }

    private fun handlePassengerMeal(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_GENDER_CLICKED && isReadyForAction("MEAL")) {
            executeClick(getTargetNode("MEAL")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.PASSENGER_MEAL_CLICKED
                }
            }
        }
    }

    private fun handlePassengerSubmit(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_MEAL_CLICKED && isReadyForAction("ADD_PASSENGER")) {
            executeClick(getTargetNode("ADD_PASSENGER")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.PASSENGER_SUBMITTED
                }
            }
        }
    }

    private fun handleOptionsReview(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_SUBMITTED && isReadyForAction("REVIEW")) {
            executeClick(getTargetNode("REVIEW")) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.OPTIONS_REVIEW_CLICKED
                    Log.i(TAG, "Review Journey Details clicked. Automation stopped.")
                    currentState = State.STOPPED
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS (Pass full node details)
    // ----------------------------------------------------------------

    private fun executeClick(node: AccessibilityNodeInfo?, onDispatched: (Boolean) -> Unit) {
        if (node == null) { onDispatched(false); return }
        val targetId = node.viewIdResourceName ?: ""
        val targetText = node.text?.toString() ?: ""
        val targetClass = node.className?.toString() ?: ""

        if (targetId.isEmpty() && targetText.isEmpty() && targetClass.isEmpty()) {
            Log.w(TAG, "Click failed: No usable identifier found on node")
            onDispatched(false)
            return
        }

        val result = orchestrator.click(
            targetId = targetId,
            targetText = targetText,
            targetClass = targetClass,
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

    private fun executeSetText(node: AccessibilityNodeInfo?, text: String, onDispatched: (Boolean) -> Unit) {
        if (node == null) { onDispatched(false); return }
        val targetId = node.viewIdResourceName ?: ""
        val targetText = node.text?.toString() ?: ""
        val targetClass = node.className?.toString() ?: ""

        if (targetId.isEmpty() && targetText.isEmpty() && targetClass.isEmpty()) {
            Log.w(TAG, "SetText failed: No usable identifier found on node")
            onDispatched(false)
            return
        }

        val result = orchestrator.setText(
            targetId = targetId,
            text = text,
            targetText = targetText,
            targetClass = targetClass,
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

    // ----------------------------------------------------------------
    // HELPER: Smart Mock for ScreenAnalyzer Integration
    // ----------------------------------------------------------------

    private fun isReadyForAction(actionKey: String): Boolean {
        // This is a placeholder. In production, it checks ScreenAnalyzer result.
        return true
    }

    private fun getTargetNode(actionKey: String): AccessibilityNodeInfo? {
        // This is a placeholder. In production, it retrieves node from ScreenAnalyzer.
        return null
    }

    override fun onInterrupt() {
        Log.w(TAG, "Service interrupted. Pausing workflow automatically.")
        if (currentSessionId.isNotEmpty() && currentState != State.STOPPED) {
            stopWorkflow()
        }
    }
}
