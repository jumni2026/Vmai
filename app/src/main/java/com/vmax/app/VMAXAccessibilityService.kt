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
    private lateinit var evidenceCollector: UIEvidenceCollector  // ✅ Now a property

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
        val executor = AndroidActionExecutor(this)
        tracker = ExecutionTracker(logger)
        orchestrator = ActionOrchestrator(executor, tracker)

        historyStore = AndroidExecutionHistoryStore(this)
        recorder = AndroidExecutionRecorder(historyStore)
        metrics = AndroidMetricsCollector()

        // ✅ Correct initialization of evidenceCollector (property)
        evidenceCollector = UIEvidenceCollector(logger)
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

        // ✅ Step 0: Update UI Evidence FIRST
        evidenceCollector.updateUiElements(extractUiElements(root))

        try {
            if (currentSessionId.isEmpty()) {
                Log.d(TAG, "No active session. Ignoring all events.")
                return
            }

            // ✅ Step 1: State check – only process if ARMED
            if (currentState != State.ARMED) {
                Log.d(TAG, "Workflow not armed. Ignoring event.")
                return
            }

            // Step 2: Get Analysis from ScreenAnalyzer
            val analysis = analyzer.analyzeCurrentScreen()

            // Step 3: Security Check via TextClassifier
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

            // Step 4: Action Discovery
            when (val suggestedAction = analysis.suggestedAction) {
                ScreenAnalyzer.SuggestedAction.SELECT_TRAIN -> {
                    // Find a clickable element that corresponds to the train selection
                    val trainNode = findTargetNodeFromAnalysis(analysis, "train_number")
                    trainNode?.let { node ->
                        executeClick(node) { success ->
                            if (success) {
                                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                            }
                        }
                    } ?: onActionFailed("TRAIN_NODE_NOT_FOUND", ActionExecutor.ActionType.CLICK)
                }

                ScreenAnalyzer.SuggestedAction.CHECK_AVAILABILITY -> {
                    // Placeholder: will be implemented when ScreenAnalyzer is updated
                    Log.i(TAG, "CHECK_AVAILABILITY action detected – to be implemented.")
                }

                ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_DETAILS -> {
                    // Fill all passenger fields (Name, Age, Gender, Meal)
                    fillPassengerDetails(analysis)
                }

                ScreenAnalyzer.SuggestedAction.REVIEW_AND_PROCEED -> {
                    val reviewNode = findTargetNodeFromAnalysis(analysis, "review_button")
                    reviewNode?.let { node ->
                        executeClick(node) { success ->
                            if (success) {
                                metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.CLICK)
                                currentState = State.STOPPED
                                Log.i(TAG, "Review Journey Details clicked. Automation stopped.")
                            }
                        }
                    } ?: onActionFailed("REVIEW_NODE_NOT_FOUND", ActionExecutor.ActionType.CLICK)
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
    // HELPER: Extract UI Elements from root
    // ----------------------------------------------------------------

    private fun extractUiElements(root: AccessibilityNodeInfo): List<UIEvidenceCollector.ScreenEvidence.UIElement> {
        val elements = mutableListOf<UIEvidenceCollector.ScreenEvidence.UIElement>()
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            // Try to find a valid viewId by walking up the hierarchy if needed
            val id = findViewIdForNode(node) ?: ""
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

    // Find a viewId by walking up the parent chain to find a clickable ancestor with an ID
    private fun findViewIdForNode(node: AccessibilityNodeInfo): String? {
        var current: AccessibilityNodeInfo? = node
        while (current != null) {
            val id = current.viewIdResourceName
            if (!id.isNullOrEmpty()) {
                return id
            }
            // If current is clickable and has no ID, try its parent
            if (current.isClickable && current.parent != null) {
                val parentId = current.parent?.viewIdResourceName
                if (!parentId.isNullOrEmpty()) {
                    return parentId
                }
            }
            current = current.parent
        }
        return null
    }

    // ----------------------------------------------------------------
    // HELPER: Find target node from analysis by key
    // ----------------------------------------------------------------

    private fun findTargetNodeFromAnalysis(
        analysis: ScreenAnalyzer.AnalysisResult,
        key: String
    ): AccessibilityNodeInfo? {
        // Look for a UIElement that matches the target key (e.g., "train_number", "review_button")
        val targetElement = findTargetUIElement(analysis, key)
        if (targetElement == null) {
            Log.w(TAG, "No UI element found for key: $key")
            return null
        }

        val targetId = targetElement.id
        if (targetId.isEmpty()) {
            Log.w(TAG, "UI element for key $key has no valid viewId")
            return null
        }

        // Search the current accessibility tree for the exact node
        val root = rootInActiveWindow ?: return null
        return findNodeById(root, targetId)
    }

    private fun findTargetUIElement(
        analysis: ScreenAnalyzer.AnalysisResult,
        key: String
    ): UIEvidenceCollector.ScreenEvidence.UIElement? {
        val uiElements = analysis.evidence?.uiElements ?: return null

        // Heuristic: for "train_number", look for a clickable element containing the train number
        // or a button with text "Select" or "Book".
        return when (key) {
            "train_number" -> {
                uiElements.firstOrNull { element ->
                    element.isClickable &&
                            (element.text.contains(targetTrain, ignoreCase = true) ||
                             element.text.contains("SELECT", ignoreCase = true) ||
                             element.text.contains("BOOK", ignoreCase = true))
                }
            }
            "review_button" -> {
                uiElements.firstOrNull { element ->
                    element.isClickable && element.text.contains("REVIEW", ignoreCase = true)
                }
            }
            else -> {
                // Generic fallback: any clickable element that contains the key as text
                uiElements.firstOrNull { element ->
                    element.isClickable && element.text.contains(key, ignoreCase = true)
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // HELPER: Find node by viewIdResourceName
    // ----------------------------------------------------------------

    private fun findNodeById(root: AccessibilityNodeInfo?, targetId: String): AccessibilityNodeInfo? {
        if (root == null) return null
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            if (node.viewIdResourceName == targetId) {
                return node
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    // ----------------------------------------------------------------
    // HELPER: Fill all passenger fields
    // ----------------------------------------------------------------

    private fun fillPassengerDetails(analysis: ScreenAnalyzer.AnalysisResult) {
        // Find editable fields for Name, Age, Gender, Meal
        val nameField = findEditableField(analysis, "Name")
        val ageField = findEditableField(analysis, "Age")
        val genderField = findClickableField(analysis, "Gender")
        val mealField = findEditableField(analysis, "Meal")

        // Execute in sequence (simplified: only Name filled for now)
        nameField?.let { node ->
            executeSetText(node, passengerName) { success ->
                if (success) {
                    metrics.recordAction(currentSessionId, true, ActionExecutor.ActionType.SET_TEXT)
                }
            }
        } ?: onActionFailed("NAME_FIELD_NOT_FOUND", ActionExecutor.ActionType.SET_TEXT)

        // Similarly, we can add Age, Gender, Meal in a more advanced version
        // For now, we just show the pattern.
    }

    private fun findEditableField(analysis: ScreenAnalyzer.AnalysisResult, label: String): AccessibilityNodeInfo? {
        val uiElement = analysis.evidence?.uiElements?.firstOrNull { element ->
            element.isEditable && element.text.contains(label, ignoreCase = true)
        }
        if (uiElement == null) return null
        val root = rootInActiveWindow ?: return null
        return findNodeById(root, uiElement.id)
    }

    private fun findClickableField(analysis: ScreenAnalyzer.AnalysisResult, label: String): AccessibilityNodeInfo? {
        val uiElement = analysis.evidence?.uiElements?.firstOrNull { element ->
            element.isClickable && element.text.contains(label, ignoreCase = true)
        }
        if (uiElement == null) return null
        val root = rootInActiveWindow ?: return null
        return findNodeById(root, uiElement.id)
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS (Strict Contract Compliance)
    // ----------------------------------------------------------------

    private fun executeClick(node: AccessibilityNodeInfo?, onDispatched: (Boolean) -> Unit) {
        if (node == null) {
            Log.w(TAG, "Click failed: node is null")
            onDispatched(false)
            return
        }
        val targetId = node.viewIdResourceName ?: ""
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

    private fun executeSetText(node: AccessibilityNodeInfo?, text: String, onDispatched: (Boolean) -> Unit) {
        if (node == null) {
            Log.w(TAG, "SetText failed: node is null")
            onDispatched(false)
            return
        }
        val targetId = node.viewIdResourceName ?: ""
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
