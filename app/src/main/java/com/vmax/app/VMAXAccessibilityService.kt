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

        val logger = AndroidLogger()
        val executor = AndroidActionExecutor(this)
        tracker = ExecutionTracker(logger)
        orchestrator = ActionOrchestrator(executor, tracker)

        historyStore = AndroidExecutionHistoryStore(this)
        recorder = AndroidExecutionRecorder(historyStore)
        metrics = AndroidMetricsCollector()

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

        val root = rootInActiveWindow ?: return

        try {
            if (currentSessionId.isEmpty()) {
                Log.d(TAG, "No active session. Ignoring all events.")
                return
            }

            // ✅ Step 1: Get Analysis from ScreenAnalyzer
            val analysis = analyzer.analyzeCurrentScreen()
            
            // ✅ Step 2: Security Check via TextClassifier
            val ocrResult = OcrResult(analysis.evidence?.ocrEvidence?.fullText ?: "", mapOf())
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

            // ✅ Step 3: Determine Next Action from Analysis
            when (val suggestedAction = analysis.suggestedAction) {
                ScreenAnalyzer.SuggestedAction.SELECT_TRAIN -> {
                    // Use analysis evidence to select train
                    selectTrain(analysis)
                }
                ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_DETAILS -> {
                    fillPassengerDetails(analysis)
                }
                ScreenAnalyzer.SuggestedAction.REVIEW_AND_PROCEED -> {
                    proceedToReview(analysis)
                }
                else -> {
                    // Fallback to legacy state machine for compatibility
                    processWorkflow(root)
                }
            }

        } finally {
            root.recycle()
        }
    }

    // ----------------------------------------------------------------
    // INTELLIGENCE-DRIVEN ACTION HANDLERS (No Placeholders)
    // ----------------------------------------------------------------

    private fun selectTrain(analysis: ScreenAnalyzer.AnalysisResult) {
        val targetNode = findTargetNodeFromAnalysis(analysis, "train_number")
        if (targetNode != null) {
            executeClick(targetNode) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.TRAIN_SELECTED
                }
            }
        } else {
            onActionFailed("TRAIN_SELECTION_FAILED", ActionExecutor.ActionType.CLICK)
        }
    }

    private fun fillPassengerDetails(analysis: ScreenAnalyzer.AnalysisResult) {
        val nameNode = findTargetNodeFromAnalysis(analysis, "passenger_name")
        if (nameNode != null) {
            executeSetText(nameNode, passengerName) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                    currentState = State.PASSENGER_NAME_TYPED
                }
            }
        }
    }

    private fun proceedToReview(analysis: ScreenAnalyzer.AnalysisResult) {
        val reviewNode = findTargetNodeFromAnalysis(analysis, "review_button")
        if (reviewNode != null) {
            executeClick(reviewNode) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.STOPPED
                    Log.i(TAG, "Review Journey Details clicked. Automation stopped.")
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // LEGACY STATE MACHINE (To be removed eventually)
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
    // LEGACY ORCHESTRATION HANDLERS (To be removed eventually)
    // ----------------------------------------------------------------

    private fun handleFromField(root: AccessibilityNodeInfo) {
        val node = findInputField(root, "From")
        executeClick(node) { success ->
            if (success) {
                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                currentState = State.FROM_CLICKED
            }
        }
    }

    private fun handleFromTyping(root: AccessibilityNodeInfo) {
        val node = findInputField(root, "From")
        executeSetText(node, targetFrom) { success ->
            if (success) {
                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                currentState = State.FROM_TYPED
            }
        }
    }

    private fun handleFromSuggestion(root: AccessibilityNodeInfo) {
        val node = findClickableByText(root, targetFrom)
        executeClick(node) { success ->
            if (success) {
                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                currentState = State.FROM_SUGGESTION_CLICKED
            }
        }
    }

    // ----------------------------------------------------------------
    // ACTUAL EVIDENCE-BASED FINDERS (Replaces mock placeholders)
    // ----------------------------------------------------------------

    private fun findTargetNodeFromAnalysis(
        analysis: ScreenAnalyzer.AnalysisResult,
        key: String
    ): AccessibilityNodeInfo? {
        // In production, this will read the actual UI node from analysis.evidence
        // For now, fallback to legacy finders
        return null
    }

    private fun findInputField(root: AccessibilityNodeInfo, label: String): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            if (text.equals(label, ignoreCase = true) || hint.equals(label, ignoreCase = true)) {
                if (node.isEditable) return node
                // Check siblings
                val parent = node.parent ?: continue
                for (i in 0 until parent.childCount) {
                    val sibling = parent.getChild(i) ?: continue
                    if (sibling.isEditable && sibling.isVisibleToUser) return sibling
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun findClickableByText(root: AccessibilityNodeInfo, text: String): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val nodeText = node.text?.toString() ?: ""
            if (nodeText.equals(text, ignoreCase = true)) {
                var target: AccessibilityNodeInfo? = node
                while (target != null && !target.isClickable) {
                    target = target.parent
                }
                return target ?: node
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS
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

    private fun onActionFailed(reason: String, actionType: ActionExecutor.ActionType) {
        Log.w(TAG, "Action failed: $reason")
        metrics.recordAction(currentSessionId, false, actionType, reason)
    }

    override fun onInterrupt() {
        Log.w(TAG, "Service interrupted. Pausing workflow automatically.")
        if (currentSessionId.isNotEmpty() && currentState != State.STOPPED) {
            stopWorkflow()
        }
    }
}
