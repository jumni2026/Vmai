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

        // 📌 Extended Security Evidence
        private val SECURITY_EVIDENCE = listOf(
            "CAPTCHA", "OTP", "Enter OTP", "Verification Code",
            "Security Code", "Captcha required"
        )
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
    private var isServiceReady: Boolean = false  // ✅ Lateinit safety guard

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
        // ✅ P0: Lateinit Safety + Configuration Validation
        if (!isServiceReady) {
            Log.e(TAG, "Service not ready. Cannot start workflow.")
            return
        }

        if (currentState != State.IDLE) {
            Log.w(TAG, "Workflow already active or armed. Ignoring start request.")
            return
        }

        // ✅ P0: Configuration Validation
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

        // Always start session before any logic
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
        executor = AndroidActionExecutor(this)
        tracker = ExecutionTracker(AndroidLogger())
        orchestrator = ActionOrchestrator(executor, tracker)

        historyStore = AndroidExecutionHistoryStore(this)
        recorder = AndroidExecutionRecorder(historyStore)
        metrics = AndroidMetricsCollector()

        isServiceReady = true  // ✅ Lateinit guard
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
            // ✅ Session Guard
            if (currentSessionId.isEmpty()) {
                Log.d(TAG, "No active session. Ignoring all events.")
                return
            }

            // ✅ P0: CAPTCHA/OTP USER BOUNDARY (Extended detection)
            if (isSecurityBoundaryPresent(root)) {
                if (currentState != State.USER_BOUNDARY) {
                    recorder.recordEvent(
                        ExecutionEvent.SessionError(
                            sessionId = currentSessionId,
                            errorCode = "CAPTCHA_OTP_DETECTED",
                            errorMessage = "CAPTCHA or OTP screen detected. Automation paused."
                        )
                    )
                    metrics.stopMetrics(currentSessionId, "USER_BOUNDARY")
                    currentState = State.USER_BOUNDARY
                    Log.w(TAG, "Security boundary detected. Locking to USER_BOUNDARY.")
                }
                return
            }

            // Terminal states
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
            State.PASSENGER_SUBMITTED -> handleOptionsReview(root)  // ✅ Fix: Dead state fixed
            else -> { /* Awaiting next valid workflow event */ }
        }
    }

    // ----------------------------------------------------------------
    // ORCHESTRATION HANDLERS (Event-Driven)
    // ----------------------------------------------------------------

    // 📌 P0/P1 Enhancement: Semantic Evidence Finders
    private fun findInputFieldByLabel(root: AccessibilityNodeInfo, labelText: String): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""
            val resId = node.viewIdResourceName ?: ""

            // Priority 1: Resource ID
            if (resId.contains(labelText.lowercase(), ignoreCase = true) && node.isEditable) return node

            // Priority 2: Text/Hint/Desc match
            val labelMatch = text.equals(labelText, ignoreCase = true) || 
                             hint.equals(labelText, ignoreCase = true) ||
                             desc.equals(labelText, ignoreCase = true)
            if (labelMatch && node.isEditable) return node

            // Priority 3: Label is a sibling/child of an editable node (UI structure)
            if (labelMatch && !node.isEditable) {
                // Search for an editable sibling or child
                val parent = node.parent ?: continue
                for (i in 0 until parent.childCount) {
                    val sibling = parent.getChild(i) ?: continue
                    if (sibling.isEditable && sibling.isVisibleToUser) {
                        return sibling
                    }
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun findClickableByText(root: AccessibilityNodeInfo, targetText: String): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""

            if (text.equals(targetText, ignoreCase = true) && node.isVisibleToUser) {
                // Find the nearest clickable ancestor or self
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

    private fun findClickableControlByLabel(root: AccessibilityNodeInfo, labelText: String): AccessibilityNodeInfo? {
        // ✅ P0: Used for buttons/controls instead of input fields
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""
            val resId = node.viewIdResourceName ?: ""

            if (node.isVisibleToUser && node.isClickable) {
                if (text.equals(labelText, ignoreCase = true) ||
                    hint.equals(labelText, ignoreCase = true) ||
                    desc.equals(labelText, ignoreCase = true) ||
                    resId.contains(labelText.lowercase(), ignoreCase = true)) {
                    return node
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun findAvailableClass(root: AccessibilityNodeInfo, targetClass: String): AccessibilityNodeInfo? {
        // ✅ P0: Proper Class + Availability Verification
        val trainNode = findClickableByText(root, targetTrain) ?: return null
        val trainRow = trainNode.parent ?: return null

        // Search for the target class within the same train row
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(trainRow)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""

            if (text.contains(targetClass, ignoreCase = true) && node.isVisibleToUser) {
                // Check availability in the same node or sibling
                var availabilityNode: AccessibilityNodeInfo? = node
                var available = false
                while (availabilityNode != null && !available) {
                    val availText = availabilityNode.text?.toString() ?: ""
                    if (availText.contains("AVL", ignoreCase = true) ||
                        availText.contains("AVAILABLE", ignoreCase = true)) {
                        available = true
                        break
                    }
                    availabilityNode = availabilityNode.parent
                }
                if (available) {
                    var clickable: AccessibilityNodeInfo? = node
                    while (clickable != null && !clickable.isClickable) {
                        clickable = clickable.parent
                    }
                    return clickable ?: node
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun findDateInCalendar(root: AccessibilityNodeInfo, targetDate: String): AccessibilityNodeInfo? {
        // ✅ P0: Exact Date Semantic Matching
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""

            // Match exact date string (e.g., "14 Aug" or "14/08/2026")
            if (node.isVisibleToUser && node.isClickable) {
                if (text.equals(targetDate, ignoreCase = true)) {
                    return node
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun handleFromField(root: AccessibilityNodeInfo) {
        findInputFieldByLabel(root, EVIDENCE_FROM)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.FROM_CLICKED
                } else {
                    metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                    onActionFailed("FROM_CLICK", ActionExecutor.ActionType.CLICK)
                }
            }
        } ?: onActionFailed("FROM_FIELD_NOT_FOUND", ActionExecutor.ActionType.CLICK)
    }

    private fun handleFromTyping(root: AccessibilityNodeInfo) {
        if (currentState == State.FROM_CLICKED) {
            findInputFieldByLabel(root, EVIDENCE_FROM)?.let { node ->
                executeSetText(node, targetFrom) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                        currentState = State.FROM_TYPED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.SET_TEXT)
                        onActionFailed("FROM_SET_TEXT", ActionExecutor.ActionType.SET_TEXT)
                    }
                }
            }
        }
    }

    private fun handleFromSuggestion(root: AccessibilityNodeInfo) {
        if (currentState == State.FROM_TYPED) {
            findClickableByText(root, targetFrom)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.FROM_SUGGESTION_CLICKED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("FROM_SUGGESTION", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handleToField(root: AccessibilityNodeInfo) {
        if (currentState == State.FROM_SUGGESTION_CLICKED) {
            findInputFieldByLabel(root, EVIDENCE_TO)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.TO_CLICKED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("TO_CLICK", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handleToTyping(root: AccessibilityNodeInfo) {
        if (currentState == State.TO_CLICKED) {
            findInputFieldByLabel(root, EVIDENCE_TO)?.let { node ->
                executeSetText(node, targetTo) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                        currentState = State.TO_TYPED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.SET_TEXT)
                        onActionFailed("TO_SET_TEXT", ActionExecutor.ActionType.SET_TEXT)
                    }
                }
            }
        }
    }

    private fun handleToSuggestion(root: AccessibilityNodeInfo) {
        if (currentState == State.TO_TYPED) {
            findClickableByText(root, targetTo)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.TO_SUGGESTION_CLICKED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("TO_SUGGESTION", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handleDateField(root: AccessibilityNodeInfo) {
        if (currentState == State.TO_SUGGESTION_CLICKED) {
            findInputFieldByLabel(root, EVIDENCE_DATE)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.DATE_CLICKED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("DATE_CLICK", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handleDateSelection(root: AccessibilityNodeInfo) {
        if (currentState == State.DATE_CLICKED) {
            findDateInCalendar(root, targetDate)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.DATE_SELECTED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("DATE_SELECTION", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handleSearch(root: AccessibilityNodeInfo) {
        if (currentState == State.DATE_SELECTED) {
            findInputFieldByLabel(root, EVIDENCE_SEARCH)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.SEARCH_CLICKED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("SEARCH_CLICK", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handleTrainSelection(root: AccessibilityNodeInfo) {
        if (currentState == State.SEARCH_CLICKED) {
            findClickableByText(root, targetTrain)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.TRAIN_SELECTED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("TRAIN_SELECTION", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handleClassSelection(root: AccessibilityNodeInfo) {
        if (currentState == State.TRAIN_SELECTED) {
            findAvailableClass(root, targetClass)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.CLASS_SELECTED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("CLASS_SELECTION", ActionExecutor.ActionType.CLICK)
                    }
                }
            } ?: onActionFailed("CLASS_NOT_AVAILABLE", ActionExecutor.ActionType.CLICK)
        }
    }

    private fun handlePassengerScreen(root: AccessibilityNodeInfo) {
        if (currentState == State.CLASS_SELECTED) {
            findClickableControlByLabel(root, EVIDENCE_ADD_NEW)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.PASSENGER_ADD_CLICKED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("ADD_NEW_CLICK", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handlePassengerName(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_ADD_CLICKED) {
            findInputFieldByLabel(root, "Passenger Name")?.let { nameNode ->
                executeSetText(nameNode, passengerName) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                        currentState = State.PASSENGER_NAME_TYPED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.SET_TEXT)
                        onActionFailed("PASSENGER_NAME", ActionExecutor.ActionType.SET_TEXT)
                    }
                }
            }
        }
    }

    private fun handlePassengerAge(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_NAME_TYPED) {
            findInputFieldByLabel(root, "Age")?.let { ageNode ->
                executeSetText(ageNode, passengerAge) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                        currentState = State.PASSENGER_AGE_TYPED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.SET_TEXT)
                        onActionFailed("PASSENGER_AGE", ActionExecutor.ActionType.SET_TEXT)
                    }
                }
            }
        }
    }

    private fun handlePassengerGender(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_AGE_TYPED) {
            findClickableByText(root, passengerGender)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.PASSENGER_GENDER_CLICKED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("PASSENGER_GENDER", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handlePassengerMeal(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_GENDER_CLICKED) {
            findInputFieldByLabel(root, "Meal Preference")?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.PASSENGER_MEAL_CLICKED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("MEAL_PREFERENCE", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handlePassengerSubmit(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_MEAL_CLICKED) {
            // ✅ P0: Find Submit button using ClickableControl (not InputField)
            findClickableControlByLabel(root, EVIDENCE_ADD_PASSENGER)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.PASSENGER_SUBMITTED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("ADD_PASSENGER_SUBMIT", ActionExecutor.ActionType.CLICK)
                    }
                }
            }
        }
    }

    private fun handleOptionsReview(root: AccessibilityNodeInfo) {
        if (currentState == State.PASSENGER_SUBMITTED) {
            // ✅ P0: Find Review Button using ClickableControl (not InputField)
            findClickableControlByLabel(root, EVIDENCE_REVIEW)?.let { node ->
                executeClick(node) { success ->
                    if (success) {
                        metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                        currentState = State.OPTIONS_REVIEW_CLICKED
                        Log.i(TAG, "Review Journey Details clicked. Automation stopped.")
                        currentState = State.STOPPED
                    } else {
                        metrics.recordAction(currentSessionId, false, ActionExecutor.ActionType.CLICK)
                        onActionFailed("REVIEW_CLICK", ActionExecutor.ActionType.CLICK)
                    }
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
    // HELPER: Action Failure Handling (Action-Aware)
    // ----------------------------------------------------------------

    private fun onActionFailed(reason: String, actionType: ActionExecutor.ActionType) {
        // ✅ P0: Record action failure with correct ActionType
        Log.w(TAG, "Action failed: $reason")
        metrics.recordAction(currentSessionId, false, actionType, reason)
        // Optionally: record a failed step in recorder
    }

    // ----------------------------------------------------------------
    // SECURITY & HELPER FUNCTIONS
    // ----------------------------------------------------------------

    private fun isSecurityBoundaryPresent(root: AccessibilityNodeInfo): Boolean {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""

            if (node.isVisibleToUser) {
                if (SECURITY_EVIDENCE.any { 
                    text.contains(it, ignoreCase = true) ||
                    hint.contains(it, ignoreCase = true) ||
                    desc.contains(it, ignoreCase = true)
                }) {
                    return true
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return false
    }

    override fun onInterrupt() {
        Log.w(TAG, "Service interrupted. Pausing workflow automatically.")
        if (currentSessionId.isNotEmpty() && currentState != State.STOPPED) {
            stopWorkflow()
        }
    }
}
