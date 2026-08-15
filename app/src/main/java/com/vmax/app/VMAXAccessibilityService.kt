package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.workflow.ExecutionTracker
import com.vmax.workflow.ActionOrchestrator
import com.vmax.common.Result
import com.vmax.runtime.MetricsCollector
import com.vmax.runtime.ExecutionRecorder
import com.vmax.app.AndroidLogger
import com.vmax.app.AndroidExecutionHistoryStore
import com.vmax.app.AndroidExecutionRecorder
import com.vmax.app.AndroidMetricsCollector
import com.vmax.core_intelligence.ScreenAnalyzer
import com.vmax.core_intelligence.UIEvidenceCollector
import com.vmax.core_intelligence.TextClassifier
import com.vmax.core_intelligence.OcrResult

class VMAXAccessibilityService : AccessibilityService() {
    companion object {
        private const val TAG = "VMAX_EXECUTION_SERVICE"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"
    }

    // --- Data ---
    private var targetFrom = ""; private var targetTo = ""; private var targetDate = ""
    private var targetTrain = ""; private var targetClass = ""
    private var passengerName = ""; private var passengerAge = ""; private var passengerGender = ""; private var passengerMeal = ""
    private var currentSessionId = ""; private var isServiceReady = false

    private enum class State { IDLE, ARMED, USER_BOUNDARY, STOPPED, GENDER_DROPDOWN_OPENED, MEAL_DROPDOWN_OPENED, PASSENGER_NAME_TYPED, PASSENGER_AGE_TYPED, PASSENGER_GENDER_SELECTED, PASSENGER_MEAL_SELECTED }
    private var currentState = State.IDLE

    private lateinit var orchestrator: ActionOrchestrator
    private lateinit var tracker: ExecutionTracker
    private lateinit var analyzer: ScreenAnalyzer
    private lateinit var classifier: TextClassifier
    private lateinit var evidenceCollector: UIEvidenceCollector
    private lateinit var historyStore: AndroidExecutionHistoryStore
    private lateinit var recorder: ExecutionRecorder
    private lateinit var metrics: MetricsCollector

    private var lastSuggestedAction: ScreenAnalyzer.SuggestedAction? = null

    // ✅ FIX: Non-null `id` with default, fallback to coordinates if null
    private data class ClickTarget(
        val id: String?,
        val text: String?,
        val className: String?,
        val coordinates: Pair<Int, Int>?
    )

    // --- Public Contract ---
    fun startWorkflow(from: String, to: String, date: String, train: String, trainClass: String, name: String, age: String, gender: String, meal: String) {
        if (!isServiceReady) { Log.e(TAG, "Service not ready"); return }
        if (currentState != State.IDLE) { Log.w(TAG, "Workflow already active"); return }
        if (from.isBlank() || to.isBlank() || train.isBlank() || trainClass.isBlank() || name.isBlank() || age.isBlank() || gender.isBlank()) {
            Log.e(TAG, "Invalid config"); return
        }
        targetFrom = from; targetTo = to; targetDate = date; targetTrain = train; targetClass = trainClass
        passengerName = name; passengerAge = age; passengerGender = gender; passengerMeal = meal
        currentSessionId = "SESSION_${System.currentTimeMillis()}"
        tracker.startSession(currentSessionId)
        metrics.startMetrics(currentSessionId)
        recorder.recordEvent(ExecutionEvent.SessionStarted(currentSessionId))
        currentState = State.ARMED
        Log.i(TAG, "Workflow armed")
    }

    fun stopWorkflow() {
        if (currentState == State.STOPPED || currentState == State.IDLE) return
        if (currentSessionId.isNotEmpty()) {
            tracker.stopSession(currentSessionId)
            recorder.recordEvent(ExecutionEvent.SessionStopped(currentSessionId))
            metrics.stopMetrics(currentSessionId, "STOPPED")
        }
        currentState = State.STOPPED
    }

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
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || !isServiceReady) return
        if (event.packageName?.toString() != IRCTC_PACKAGE) return
        val root = rootInActiveWindow ?: return
        evidenceCollector.updateUiElements(extractUiElements(root))

        try {
            if (currentSessionId.isEmpty()) return

            when (currentState) {
                State.GENDER_DROPDOWN_OPENED -> { selectGenderOption(root); return }
                State.MEAL_DROPDOWN_OPENED -> { selectMealOption(root); return }
                State.PASSENGER_NAME_TYPED -> { handlePassengerAge(root); return }
                State.PASSENGER_AGE_TYPED -> { handlePassengerGender(root); return }
                State.PASSENGER_GENDER_SELECTED -> { handlePassengerMeal(root); return }
                State.PASSENGER_MEAL_SELECTED -> { lastSuggestedAction = null; currentState = State.ARMED; return }
                else -> {}
            }

            if (currentState != State.ARMED) return

            val analysis = analyzer.analyzeCurrentScreen()
            val ocrResult = OcrResult(
                currentSessionId, System.currentTimeMillis(),
                analysis.evidence?.ocrEvidence?.fullText ?: "",
                analysis.evidence?.ocrEvidence?.rawBlocks ?: emptyList()
            )
            if (classifier.isSensitiveScreen(ocrResult)) {
                if (currentState != State.USER_BOUNDARY) {
                    recorder.recordEvent(ExecutionEvent.SessionError(currentSessionId, "SECURITY_BOUNDARY", "Sensitive screen"))
                    metrics.stopMetrics(currentSessionId, "USER_BOUNDARY")
                    currentState = State.USER_BOUNDARY
                }
                return
            }
            if (currentState == State.USER_BOUNDARY || currentState == State.STOPPED) return

            val suggestedAction = analysis.suggestedAction
            if (suggestedAction == lastSuggestedAction && suggestedAction != ScreenAnalyzer.SuggestedAction.NONE) return

            when (suggestedAction) {
                ScreenAnalyzer.SuggestedAction.SELECT_TRAIN -> {
                    val target = findTargetForTrain(analysis)
                    if (target != null) {
                        executeClick(target) { success ->
                            metrics.recordAction(currentSessionId, success, ActionExecutor.ActionType.CLICK, if (!success) "TRAIN_CLICK_FAILED" else null)
                            if (success) { lastSuggestedAction = suggestedAction }
                        }
                    } else onActionFailed("TRAIN_NOT_FOUND", ActionExecutor.ActionType.CLICK)
                }
                ScreenAnalyzer.SuggestedAction.CHECK_AVAILABILITY -> {
                    val target = findTargetForClass(analysis)
                    if (target != null) {
                        executeClick(target) { success ->
                            metrics.recordAction(currentSessionId, success, ActionExecutor.ActionType.CLICK, if (!success) "CLASS_CLICK_FAILED" else null)
                            if (success) { lastSuggestedAction = suggestedAction }
                        }
                    } else onActionFailed("CLASS_NOT_FOUND", ActionExecutor.ActionType.CLICK)
                }
                ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_DETAILS -> fillPassengerDetails(analysis)
                ScreenAnalyzer.SuggestedAction.REVIEW_AND_PROCEED -> {
                    val target = findTargetForReview(analysis)
                    if (target != null) {
                        executeClick(target) { success ->
                            metrics.recordAction(currentSessionId, success, ActionExecutor.ActionType.CLICK, if (!success) "REVIEW_CLICK_FAILED" else null)
                            if (success) {
                                lastSuggestedAction = suggestedAction
                                currentState = State.STOPPED
                            }
                        }
                    } else onActionFailed("REVIEW_NOT_FOUND", ActionExecutor.ActionType.CLICK)
                }
                else -> {}
            }
        } finally {
            root.recycle()
        }
    }

    // --- UI Helpers with Safe Traversal ---
    private fun extractUiElements(root: AccessibilityNodeInfo): List<UIEvidenceCollector.ScreenEvidence.UIElement> {
        val elements = mutableListOf<UIEvidenceCollector.ScreenEvidence.UIElement>()
        val queue = ArrayDeque<AccessibilityNodeInfo>().apply { add(root) }
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val id = findClickableViewIdForNode(root, node) ?: ""
            val bounds = android.graphics.Rect().also { node.getBoundsInScreen(it) }
            elements.add(UIEvidenceCollector.ScreenEvidence.UIElement(
                id = id,
                type = node.className?.toString() ?: "",
                text = node.text?.toString() ?: "",
                contentDescription = node.contentDescription?.toString(),
                bounds = OcrResult.BoundingBox(bounds.left, bounds.top, bounds.right, bounds.bottom),
                isClickable = node.isClickable,
                isEditable = node.isEditable,
                hint = node.hintText?.toString()
            ))
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
            if (node !== root) {
                node.recycle()
            }
        }
        return elements
    }

    private fun findClickableViewIdForNode(root: AccessibilityNodeInfo, node: AccessibilityNodeInfo): String? {
        val queue = ArrayDeque<AccessibilityNodeInfo>().apply { add(node) }
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current.isClickable) {
                val id = current.viewIdResourceName
                if (!id.isNullOrEmpty()) return id
            }
            val parent = current.parent
            if (parent != null && parent !== root) {
                queue.addLast(parent)
            }
            if (current !== node) {
                current.recycle()
            }
        }
        return null
    }

    // --- Target finders ---
    private fun findTargetForTrain(analysis: ScreenAnalyzer.AnalysisResult): ClickTarget? {
        val ui = analysis.evidence?.uiElements ?: return null
        ui.firstOrNull { it.isClickable && it.text.equals(targetTrain, true) }?.let {
            return ClickTarget(it.id, it.text, it.type, getBoundsFromUIElement(it))
        }
        ui.firstOrNull { it.isClickable && it.text.contains(targetTrain, true) }?.let {
            return ClickTarget(it.id, it.text, it.type, getBoundsFromUIElement(it))
        }
        val keywords = listOf("SELECT", "VIEW", "BOOK", "SEARCH", "FIND TRAINS", "CHECK")
        ui.firstOrNull { it.isClickable && keywords.any { it.text.contains(it, true) } }?.let {
            return ClickTarget(it.id, it.text, it.type, getBoundsFromUIElement(it))
        }
        return null
    }

    private fun findTargetForClass(analysis: ScreenAnalyzer.AnalysisResult): ClickTarget? {
        val ui = analysis.evidence?.uiElements ?: return null
        return ui.firstOrNull { it.isClickable && it.text.contains(targetClass, true) }?.let {
            ClickTarget(it.id, it.text, it.type, getBoundsFromUIElement(it))
        }
    }

    private fun findTargetForReview(analysis: ScreenAnalyzer.AnalysisResult): ClickTarget? {
        val ui = analysis.evidence?.uiElements ?: return null
        return ui.firstOrNull { it.isClickable && it.text.contains("REVIEW", true) }?.let {
            ClickTarget(it.id, it.text, it.type, getBoundsFromUIElement(it))
        }
    }

    private fun getBoundsFromUIElement(element: UIEvidenceCollector.ScreenEvidence.UIElement): Pair<Int, Int>? {
        return element.bounds?.let { Pair((it.left + it.right) / 2, (it.top + it.bottom) / 2) }
    }

    // --- Passenger Flow ---
    private fun fillPassengerDetails(analysis: ScreenAnalyzer.AnalysisResult) {
        if (currentState != State.ARMED) return
        val nameTarget = findPassengerFieldTarget(analysis, "Name", false)
        if (nameTarget != null) {
            executeSetText(nameTarget, passengerName) { success ->
                metrics.recordAction(currentSessionId, success, ActionExecutor.ActionType.SET_TEXT, if (!success) "NAME_SET_FAILED" else null)
                if (success) {
                    lastSuggestedAction = ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_DETAILS
                    currentState = State.PASSENGER_NAME_TYPED
                }
            }
        } else onActionFailed("NAME_NOT_FOUND", ActionExecutor.ActionType.SET_TEXT)
    }

    private fun handlePassengerAge(root: AccessibilityNodeInfo) {
        if (currentState != State.PASSENGER_NAME_TYPED) return
        val ageTarget = findPassengerFieldTargetFromRoot(root, "Age", false)
        if (ageTarget != null) {
            executeSetText(ageTarget, passengerAge) { success ->
                metrics.recordAction(currentSessionId, success, ActionExecutor.ActionType.SET_TEXT, if (!success) "AGE_SET_FAILED" else null)
                if (success) currentState = State.PASSENGER_
