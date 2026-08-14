package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

// Core contracts
import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent          // ✅ सही: core-action से
import com.vmax.workflow.ExecutionTracker     // ✅ सही: core-workflow से
import com.vmax.workflow.ActionOrchestrator
import com.vmax.common.Logger
import com.vmax.common.Result
import com.vmax.runtime.MetricsCollector
import com.vmax.runtime.ExecutionRecorder

// Android-specific implementations
import com.vmax.app.AndroidLogger
import com.vmax.app.AndroidExecutionHistoryStore
import com.vmax.app.AndroidExecutionRecorder
import com.vmax.app.AndroidMetricsCollector

class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_EXECUTION_SERVICE"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        // UI Evidence
        private const val EVIDENCE_FROM = "From"
        private const val EVIDENCE_TO = "To"
        private const val EVIDENCE_DATE = "Date"
        private const val EVIDENCE_SEARCH = "Search"
        private const val EVIDENCE_ADD_NEW = "Add New"
        private const val EVIDENCE_ADD_PASSENGER = "Add Passenger"
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

    // ----------------------------------------------------------------
    // STATE MACHINE
    // ----------------------------------------------------------------

    private enum class State {
        IDLE,
        FROM_CLICKED, FROM_TYPED, FROM_SUGGESTION_CLICKED,
        TO_CLICKED, TO_TYPED, TO_SUGGESTION_CLICKED,
        DATE_CLICKED, DATE_SELECTED,
        SEARCH_CLICKED, TRAIN_SELECTED, CLASS_SELECTED,
        PASSENGER_ADD_CLICKED, PASSENGER_NAME_TYPED, PASSENGER_AGE_TYPED,
        PASSENGER_GENDER_CLICKED, PASSENGER_MEAL_CLICKED, PASSENGER_SUBMITTED,
        OPTIONS_REVIEW_CLICKED,
        USER_BOUNDARY, STOPPED
    }

    private var currentState = State.IDLE

    private lateinit var executor: AndroidActionExecutor
    private lateinit var orchestrator: ActionOrchestrator
    private lateinit var tracker: ExecutionTracker

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

        // Session start records
        metrics.startMetrics(currentSessionId)
        recorder.recordEvent(ExecutionEvent.SessionStarted(currentSessionId))

        currentState = State.IDLE
        Log.i(TAG, "Workflow started: $currentSessionId")
    }

    fun stopWorkflow() {
        recorder.recordEvent(ExecutionEvent.SessionStopped(currentSessionId))
        metrics.stopMetrics(currentSessionId, "STOPPED")

        currentState = State.STOPPED
        Log.i(TAG, "Workflow stopped.")
    }

    // ----------------------------------------------------------------
    // SERVICE LIFECYCLE
    // ----------------------------------------------------------------

    override fun onServiceConnected() {
        super.onServiceConnected()

        executor = AndroidActionExecutor(this)
        tracker = ExecutionTracker(AndroidLogger())
        orchestrator = ActionOrchestrator(executor, tracker)

        historyStore = AndroidExecutionHistoryStore(this)
        recorder = AndroidExecutionRecorder(historyStore)
        metrics = AndroidMetricsCollector()

        Log.i(TAG, "VMAX Service Connected with Real Executor, ActionOrchestrator, and History System")
    }

    // ----------------------------------------------------------------
    // ACCESSIBILITY EVENT
    // ----------------------------------------------------------------

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName != IRCTC_PACKAGE) return

        val root = rootInActiveWindow ?: return

        try {
            // CAPTCHA / OTP USER BOUNDARY
            if (isCaptchaOrOtpPresent(root)) {
                currentState = State.USER_BOUNDARY

                recorder.recordEvent(
                    ExecutionEvent.SessionError(
                        sessionId = currentSessionId,
                        errorCode = "CAPTCHA_OTP_DETECTED",
                        errorMessage = "CAPTCHA or OTP screen detected. Automation paused."
                    )
                )
                metrics.stopMetrics(currentSessionId, "USER_BOUNDARY")

                Log.w(TAG, "CAPTCHA/OTP detected. Locking to USER_BOUNDARY.")
                return
            }

            if (currentState == State.USER_BOUNDARY || currentState == State.STOPPED) {
                return
            }

            processWorkflow(root)

        } finally {
            root.recycle()
        }
    }

    // ----------------------------------------------------------------
    // WORKFLOW STATE MACHINE
    // ----------------------------------------------------------------

    private fun processWorkflow(root: AccessibilityNodeInfo) {
        when (currentState) {
            State.IDLE -> handleFromField(root)
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
            State.PASSENGER_ADD_CLICKED -> handlePassengerDetails(root)
            State.PASSENGER_AGE_TYPED -> handlePassengerGender(root)
            State.PASSENGER_GENDER_CLICKED -> handlePassengerMeal(root)
            State.PASSENGER_MEAL_CLICKED -> handleAddPassengerSubmit(root)
            State.PASSENGER_SUBMITTED -> handleOptionsReview(root)
            else -> { /* Awaiting next valid workflow event */ }
        }
    }

    // ----------------------------------------------------------------
    // ORCHESTRATION HANDLERS (Metrics Tracking Only)
    // ----------------------------------------------------------------

    private fun handleFromField(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(root, EVIDENCE_FROM)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.FROM_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handleFromTyping(root: AccessibilityNodeInfo) {
        if (currentState == State.FROM_CLICKED) {
            findEditableNodeByEvidence(root, EVIDENCE_FROM)?.let { node ->
                executeSetText(node, targetFrom) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                        currentState = State.FROM_TYPED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.SET_TEXT)
                    }
                }
            }
        }
    }

    private fun handleFromSuggestion(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, targetFrom, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.FROM_SUGGESTION_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handleToField(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(root, EVIDENCE_TO)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.TO_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handleToTyping(root: AccessibilityNodeInfo) {
        if (currentState == State.TO_CLICKED) {
            findEditableNodeByEvidence(root, EVIDENCE_TO)?.let { node ->
                executeSetText(node, targetTo) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                        currentState = State.TO_TYPED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.SET_TEXT)
                    }
                }
            }
        }
    }

    private fun handleToSuggestion(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, targetTo, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.TO_SUGGESTION_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handleDateField(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_DATE, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.DATE_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handleDateSelection(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, targetDate, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.DATE_SELECTED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handleSearch(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_SEARCH, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.SEARCH_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handleTrainSelection(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, targetTrain, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.TRAIN_SELECTED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handleClassSelection(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, targetClass, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.CLASS_SELECTED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handlePassengerScreen(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_ADD_NEW, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.PASSENGER_ADD_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handlePassengerDetails(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(root, "Passenger Name")?.let { nameNode ->
            executeSetText(nameNode, passengerName) { nameSuccess ->
                if (!nameSuccess) return@executeSetText
                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                currentState = State.PASSENGER_NAME_TYPED

                findEditableNodeByEvidence(root, "Age")?.let { ageNode ->
                    executeSetText(ageNode, passengerAge) { ageSuccess ->
                        if (ageSuccess) {
                            metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                            currentState = State.PASSENGER_AGE_TYPED
                        } else {
                            metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.SET_TEXT)
                        }
                    }
                }
            }
        }
    }

    private fun handlePassengerGender(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, passengerGender, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.PASSENGER_GENDER_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handlePassengerMeal(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, "Meal Preference", isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.PASSENGER_MEAL_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    private fun handleAddPassengerSubmit(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, passengerMeal, isClickable = true)?.let { mealNode ->
            executeClick(mealNode) {
                findNodeByExactText(root, EVIDENCE_ADD_PASSENGER, isClickable = true)?.let { addNode ->
                    executeClick(addNode) { success ->
                        if (success) {
                            metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                            currentState = State.PASSENGER_SUBMITTED
                        } else {
                            metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        }
                    }
                }
            }
        }
    }

    private fun handleOptionsReview(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, EVIDENCE_REVIEW, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.OPTIONS_REVIEW_CLICKED
                    Log.i(TAG, "Review Journey Details clicked. Automation stopped.")
                    currentState = State.STOPPED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS (Uses Orchestrator)
    // ----------------------------------------------------------------

    private fun executeClick(node: AccessibilityNodeInfo?, onDispatched: (Boolean) -> Unit) {
        if (node == null) { onDispatched(false); return }
        val targetId = node.viewIdResourceName ?: ""
        if (targetId.isEmpty()) {
            Log.w(TAG, "Click failed: Node has no viewIdResourceName")
            onDispatched(false)
            return
        }
        val result = orchestrator.click(targetId, currentSessionId)
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
        if (targetId.isEmpty()) {
            Log.w(TAG, "SetText failed: Node has no viewIdResourceName")
            onDispatched(false)
            return
        }
        val result = orchestrator.setText(targetId, text, currentSessionId)
        when (result) {
            is Result.Success -> onDispatched(true)
            is Result.Error -> {
                Log.e(TAG, "SetText failed: ${result.error.message}")
                onDispatched(false)
            }
        }
    }

    // ----------------------------------------------------------------
    // EVIDENCE FINDERS
    // ----------------------------------------------------------------

    private fun findNodeByEvidence(root: AccessibilityNodeInfo, evidence: String, isClickable: Boolean = false): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""

            if (node.isVisibleToUser && (!isClickable || node.isClickable)) {
                if (text.equals(evidence, ignoreCase = true) || hint.equals(evidence, ignoreCase = true) || desc.equals(evidence, ignoreCase = true)) {
                    return node
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun findEditableNodeByEvidence(root: AccessibilityNodeInfo, evidence: String): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""

            if (node.isVisibleToUser && node.isEditable) {
                if (text.equals(evidence, ignoreCase = true) || hint.equals(evidence, ignoreCase = true) || desc.equals(evidence, ignoreCase = true)) {
                    return node
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun findNodeByExactText(root: AccessibilityNodeInfo, targetText: String, isClickable: Boolean = false): AccessibilityNodeInfo? {
        if (targetText.isEmpty()) return null
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            if (node.isVisibleToUser && (!isClickable || node.isClickable)) {
                if (text.equals(targetText, ignoreCase = true)) {
                    return node
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun isCaptchaOrOtpPresent(root: AccessibilityNodeInfo): Boolean {
        return findNodeByExactText(root, EVIDENCE_CAPTCHA) != null ||
                findNodeByExactText(root, EVIDENCE_OTP) != null
    }

    override fun onInterrupt() {
        Log.w(TAG, "Service interrupted")
    }
}v
