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
    // STATE MACHINE (Extended for dropdowns)
    // ----------------------------------------------------------------

    private enum class State {
        IDLE, ARMED, USER_BOUNDARY, STOPPED,
        GENDER_DROPDOWN_OPENED, MEAL_DROPDOWN_OPENED
    }

    private var currentState = State.IDLE

    private lateinit var orchestrator: ActionOrchestrator
    private lateinit var tracker: ExecutionTracker
    private lateinit var analyzer: ScreenAnalyzer
    private lateinit var classifier: TextClassifier
    private lateinit var evidenceCollector: UIEvidenceCollector

    // ----------------------------------------------------------------
    // HISTORY SYSTEM COMPONENTS
    // ----------------------------------------------------------------
    private lateinit var historyStore: AndroidExecutionHistoryStore
    private lateinit var recorder: ExecutionRecorder
    private lateinit var metrics: MetricsCollector

    // ----------------------------------------------------------------
    // DUPLICATE ACTION PREVENTION
    // ----------------------------------------------------------------
    private var lastSuggestedAction: ScreenAnalyzer.SuggestedAction? = null

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
        val executor = AndroidActionExecutor(this)
        tracker = ExecutionTracker(logger)
        orchestrator = ActionOrchestrator(executor, tracker)

        historyStore = AndroidExecutionHistoryStore(this)
        recorder = AndroidExecutionRecorder(historyStore)
        metrics = AndroidMetricsCollector()

        evidenceCollector = UIEvidenceCollector(logger)
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

        // Step 1: Always update UI Evidence first
        evidenceCollector.updateUiElements(extractUiElements(root))

        try {
            if (currentSessionId.isEmpty()) {
                Log.d(TAG, "No active session. Ignoring all events.")
                return
            }

            // Step 2: Process dropdown selections first
            when (currentState) {
                State.GENDER_DROPDOWN_OPENED -> {
                    selectGenderOption(root)
                    return
                }
                State.MEAL_DROPDOWN_OPENED -> {
                    selectMealOption(root)
                    return
                }
                else -> {}
            }

            // Step 3: Only process if ARMED
            if (currentState != State.ARMED) {
                Log.d(TAG, "Workflow not armed. Ignoring event.")
                return
            }

            // Step 4: Get Analysis from ScreenAnalyzer
            val analysis = analyzer.analyzeCurrentScreen()

            // Step 5: Security Check via TextClassifier
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

            // Step 6: Duplicate Action Prevention
            val suggestedAction = analysis.suggestedAction
            if (suggestedAction == lastSuggestedAction && suggestedAction != ScreenAnalyzer.SuggestedAction.NONE) {
                Log.d(TAG, "Duplicate action '$suggestedAction' ignored.")
                return
            }
            lastSuggestedAction = suggestedAction

            // Step 7: Action Discovery
            when (suggestedAction) {
                ScreenAnalyzer.SuggestedAction.SELECT_TRAIN -> {
                    val targetId = findTargetIdForTrain(analysis)
                    if (targetId != null) {
                        executeClick(targetId) { success ->
                            if (success) {
                                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                            }
                        }
                    } else {
                        onActionFailed("TRAIN_NODE_ID_NOT_FOUND", ActionExecutor.ActionType.CLICK)
                    }
                }

                ScreenAnalyzer.SuggestedAction.CHECK_AVAILABILITY -> {
                    val classId = findTargetIdForClass(analysis)
                    if (classId != null) {
                        executeClick(classId) { success ->
                            if (success) {
                                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                                Log.i(TAG, "Availability check clicked.")
                            }
                        }
                    } else {
                        onActionFailed("CLASS_NODE_ID_NOT_FOUND", ActionExecutor.ActionType.CLICK)
                    }
                }

                ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_DETAILS -> {
                    fillPassengerDetails(analysis)
                }

                ScreenAnalyzer.SuggestedAction.REVIEW_AND_PROCEED -> {
                    val targetId = findTargetIdForReview(analysis)
                    if (targetId != null) {
                        executeClick(targetId) { success ->
                            if (success) {
                                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                                currentState = State.STOPPED
                                Log.i(TAG, "Review Journey Details clicked. Automation stopped.")
                            }
                        }
                    } else {
                        onActionFailed("REVIEW_NODE_ID_NOT_FOUND", ActionExecutor.ActionType.CLICK)
                    }
                }

                else -> { /* No actionable intelligence, wait for next event */ }
            }

        } finally {
            root.recycle()
        }
    }

    // ----------------------------------------------------------------
    // HELPER: Extract UI Elements
    // ----------------------------------------------------------------

    private fun extractUiElements(root: AccessibilityNodeInfo): List<UIEvidenceCollector.ScreenEvidence.UIElement> {
        val elements = mutableListOf<UIEvidenceCollector.ScreenEvidence.UIElement>()
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val id = findClickableViewIdForNode(node) ?: ""
            val type = node.className?.toString() ?: ""
            val text = node.text?.toString() ?: ""
            val desc = node.contentDescription?.toString()
            val bounds = android.graphics.Rect().also { node.getBoundsInScreen(it) }
            val boundingBox = OcrResult.BoundingBox(bounds.left, bounds.top, bounds.right, bounds.bottom)

            val uiElement = UIEvidenceCollector.ScreenEvidence.UIElement(
                id = id,
                type = type,
                text = text,
                contentDescription = desc,
                bounds = boundingBox,
                isClickable = node.isClickable,
                isEditable = node.isEditable,
                hint = node.hintText?.toString()
            )
            elements.add(uiElement)
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return elements
    }

    // Walk up parent chain to find the nearest clickable node with a viewId
    private fun findClickableViewIdForNode(node: AccessibilityNodeInfo): String? {
        var current: AccessibilityNodeInfo? = node
        while (current != null) {
            val id = current.viewIdResourceName
            if (!id.isNullOrEmpty() && current.isClickable) {
                return id
            }
            current = current.parent
        }
        return null
    }

    // ----------------------------------------------------------------
    // HELPER: Find Target IDs (Prioritized)
    // ----------------------------------------------------------------

    private fun findTargetIdForTrain(analysis: ScreenAnalyzer.AnalysisResult): String? {
        val uiElements = analysis.evidence?.uiElements ?: return null

        // Priority 1: Exact match with targetTrain
        uiElements.firstOrNull { element ->
            element.isClickable && element.text.equals(targetTrain, ignoreCase = true)
        }?.id?.let { return it }

        // Priority 2: Contains targetTrain
        uiElements.firstOrNull { element ->
            element.isClickable && element.text.contains(targetTrain, ignoreCase = true)
        }?.id?.let { return it }

        // Priority 3: Buttons with "SELECT" or "VIEW" (more specific)
        uiElements.firstOrNull { element ->
            element.isClickable && (
                element.text.contains("SELECT", ignoreCase = true) ||
                element.text.contains("VIEW", ignoreCase = true)
            )
        }?.id?.let { return it }

        // Priority 4: Broad fallback (BOOK, TRAIN)
        return uiElements.firstOrNull { element ->
            element.isClickable && (
                element.text.contains("BOOK", ignoreCase = true) ||
                element.text.contains("TRAIN", ignoreCase = true)
            )
        }?.id
    }

    private fun findTargetIdForClass(analysis: ScreenAnalyzer.AnalysisResult): String? {
        val uiElements = analysis.evidence?.uiElements ?: return null
        // Look for a clickable element that matches targetClass
        return uiElements.firstOrNull { element ->
            element.isClickable && element.text.contains(targetClass, ignoreCase = true)
        }?.id
    }

    private fun findTargetIdForReview(analysis: ScreenAnalyzer.AnalysisResult): String? {
        val uiElements = analysis.evidence?.uiElements ?: return null
        return uiElements.firstOrNull { element ->
            element.isClickable && element.text.contains("REVIEW", ignoreCase = true)
        }?.id
    }

    // ----------------------------------------------------------------
    // HELPER: Fill Passenger Details with Dropdown Support
    // ----------------------------------------------------------------

    private fun fillPassengerDetails(analysis: ScreenAnalyzer.AnalysisResult) {
        // For Name and Age: editable fields
        val nameId = findPassengerFieldId(analysis, "Name", isClickable = false)
        val ageId = findPassengerFieldId(analysis, "Age", isClickable = false)
        // For Gender and Meal: clickable dropdowns
        val genderId = findPassengerFieldId(analysis, "Gender", isClickable = true)
        val mealId = findPassengerFieldId(analysis, "Meal", isClickable = true)

        // Fill Name
        if (nameId != null) {
            executeSetText(nameId, passengerName) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                }
            }
        }

        // Fill Age
        if (ageId != null) {
            executeSetText(ageId, passengerAge) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                }
            }
        }

        // Gender Dropdown: Click to open
        if (genderId != null && currentState == State.ARMED) {
            executeClick(genderId) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.GENDER_DROPDOWN_OPENED
                    Log.i(TAG, "Gender dropdown opened. Waiting for option selection.")
                }
            }
        }

        // Meal Dropdown: Click to open (only if not already in dropdown state)
        if (mealId != null && currentState == State.ARMED) {
            // We'll open meal dropdown after gender is selected, but for simplicity,
            // we just open it now and assume the next event will handle selection.
            // This is a placeholder; actual option selection will be handled in separate states.
            executeClick(mealId) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.MEAL_DROPDOWN_OPENED
                    Log.i(TAG, "Meal dropdown opened. Waiting for option selection.")
                }
            }
        }
    }

    private fun selectGenderOption(root: AccessibilityNodeInfo) {
        // Find the option corresponding to passengerGender
        val genderOption = findClickableByText(root, passengerGender)
        if (genderOption != null) {
            executeClick(genderOption) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.ARMED
                    Log.i(TAG, "Gender option selected.")
                }
            }
        } else {
            Log.w(TAG, "Gender option not found. Returning to ARMED.")
            currentState = State.ARMED
        }
    }

    private fun selectMealOption(root: AccessibilityNodeInfo) {
        val mealOption = findClickableByText(root, passengerMeal)
        if (mealOption != null) {
            executeClick(mealOption) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                    currentState = State.ARMED
                    Log.i(TAG, "Meal option selected.")
                }
            }
        } else {
            Log.w(TAG, "Meal option not found. Returning to ARMED.")
            currentState = State.ARMED
        }
    }

    private fun findClickableByText(root: AccessibilityNodeInfo, text: String): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val nodeText = node.text?.toString() ?: ""
            if (node.isClickable && node.isVisibleToUser && nodeText.equals(text, ignoreCase = true)) {
                return node
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun findPassengerFieldId(
        analysis: ScreenAnalyzer.AnalysisResult,
        label: String,
        isClickable: Boolean
    ): String? {
        return analysis.evidence?.uiElements?.firstOrNull { element ->
            val textMatch = element.text.contains(label, ignoreCase = true)
            val hintMatch = element.hint?.contains(label, ignoreCase = true) == true
            val descMatch = element.contentDescription?.contains(label, ignoreCase = true) == true
            val match = textMatch || hintMatch || descMatch

            if (isClickable) {
                element.isClickable && match
            } else {
                element.isEditable && match
            }
        }?.id
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS (ID-based)
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
