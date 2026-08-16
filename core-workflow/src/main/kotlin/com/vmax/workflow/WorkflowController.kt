package com.vmax.workflow

import com.vmax.core_intelligence.ScreenAnalyzer
import com.vmax.core_intelligence.TextClassifier
import com.vmax.core_intelligence.OcrResult
import com.vmax.core_intelligence.UIEvidenceCollector
import com.vmax.common.MetricsCollector
import com.vmax.common.ExecutionRecorder
import com.vmax.action.ExecutionEvent

/**
 * IRCTC-specific workflow controller.
 * Platform-agnostic - No Android dependencies.
 */
class WorkflowController(
    private val orchestrator: ActionOrchestrator,
    private val analyzer: ScreenAnalyzer,
    private val classifier: TextClassifier,
    private val metrics: MetricsCollector,
    private val recorder: ExecutionRecorder
) {
    companion object {
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"
    }

    data class PassengerDetails(
        val from: String,
        val to: String,
        val date: String,
        val train: String,
        val trainClass: String,
        val name: String,
        val age: String,
        val gender: String,
        val meal: String
    )

    enum class WorkflowState {
        IDLE, ARMED, USER_BOUNDARY, STOPPED,
        GENDER_DROPDOWN_OPENED, MEAL_DROPDOWN_OPENED,
        PASSENGER_NAME_TYPED, PASSENGER_AGE_TYPED,
        PASSENGER_GENDER_SELECTED, PASSENGER_MEAL_SELECTED
    }

    private var currentState = WorkflowState.IDLE
    private var currentSessionId = ""
    private var passengerDetails: PassengerDetails? = null
    private var lastSuggestedAction: ScreenAnalyzer.SuggestedAction? = null

    fun startWorkflow(
        details: PassengerDetails,
        sessionId: String
    ): Boolean {
        if (currentState != WorkflowState.IDLE) {
            return false
        }

        if (details.from.isBlank() || details.to.isBlank() || details.train.isBlank() ||
            details.trainClass.isBlank() || details.name.isBlank() || 
            details.age.isBlank() || details.gender.isBlank()
        ) {
            return false
        }

        passengerDetails = details
        currentSessionId = sessionId
        currentState = WorkflowState.ARMED
        return true
    }

    fun stopWorkflow() {
        if (currentState == WorkflowState.STOPPED || currentState == WorkflowState.IDLE) return
        currentState = WorkflowState.STOPPED
    }

    fun handleScreenAnalysis(
        analysis: ScreenAnalyzer.AnalysisResult,
        ocrText: String,
        ocrBlocks: List<OcrResult.TextBlock>
    ): WorkflowAction? {
        if (currentState != WorkflowState.ARMED) {
            return null
        }

        // Check for sensitive screens
        val ocrResult = OcrResult(
            currentSessionId,
            System.currentTimeMillis(),
            ocrText,
            ocrBlocks
        )

        if (classifier.isSensitiveScreen(ocrResult)) {
            if (currentState != WorkflowState.USER_BOUNDARY) {
                recorder.recordEvent(
                    ExecutionEvent.SessionError(
                        currentSessionId,
                        "SECURITY_BOUNDARY",
                        "Sensitive screen detected"
                    )
                )
                metrics.stopMetrics(currentSessionId, "USER_BOUNDARY")
                currentState = WorkflowState.USER_BOUNDARY
            }
            return null
        }

        if (currentState == WorkflowState.USER_BOUNDARY || 
            currentState == WorkflowState.STOPPED) {
            return null
        }

        val suggestedAction = analysis.suggestedAction
        if (suggestedAction == lastSuggestedAction &&
            suggestedAction != ScreenAnalyzer.SuggestedAction.NONE
        ) {
            return null
        }

        return when (suggestedAction) {
            ScreenAnalyzer.SuggestedAction.SELECT_TRAIN -> {
                handleTrainSelection(analysis)
            }
            ScreenAnalyzer.SuggestedAction.SELECT_CLASS -> {
                handleClassSelection(analysis)
            }
            ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_NAME -> {
                handlePassengerName(analysis)
            }
            ScreenAnalyzer.SuggestedAction.REVIEW_JOURNEY -> {
                handleReviewAndProceed(analysis)
            }
            ScreenAnalyzer.SuggestedAction.ADD_PASSENGER -> {
                handleAddPassenger(analysis)
            }
            ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_AGE -> {
                handlePassengerAgeFromAnalysis(analysis)
            }
            else -> null
        }
    }

    fun handleStateAction(
        state: WorkflowState,
        uiElements: List<UIElement>
    ): WorkflowAction? {
        return when (state) {
            WorkflowState.GENDER_DROPDOWN_OPENED -> {
                selectDropdownOption(uiElements, passengerDetails?.gender ?: "")
            }
            WorkflowState.MEAL_DROPDOWN_OPENED -> {
                selectDropdownOption(uiElements, passengerDetails?.meal ?: "")
            }
            WorkflowState.PASSENGER_NAME_TYPED -> {
                handlePassengerAge(uiElements)
            }
            WorkflowState.PASSENGER_AGE_TYPED -> {
                handlePassengerGender(uiElements)
            }
            WorkflowState.PASSENGER_GENDER_SELECTED -> {
                handlePassengerMeal(uiElements)
            }
            WorkflowState.PASSENGER_MEAL_SELECTED -> {
                lastSuggestedAction = null
                currentState = WorkflowState.ARMED
                null
            }
            else -> null
        }
    }

    fun updateState(newState: WorkflowState) {
        currentState = newState
    }

    fun getCurrentState(): WorkflowState = currentState
    fun getSessionId(): String = currentSessionId

    // ==================== PRIVATE HANDLER METHODS ====================

    private fun handleTrainSelection(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val details = passengerDetails ?: return null
        val uiElements = analysis.evidence?.uiElements ?: return null
        
        val elements = convertUiElements(uiElements)

        var i = 0
        var target: UIElement? = null
        while (i < elements.size) {
            val element = elements.get(i)
            if (element.isClickable) {
                val text = element.text
                if (text.equals(details.train, ignoreCase = true) ||
                    text.contains(details.train, ignoreCase = true)) {
                    target = element
                    break
                }
            }
            i = i + 1
        }

        if (target == null) {
            val keywords = arrayOf("SELECT", "VIEW", "BOOK", "SEARCH", "FIND TRAINS", "CHECK")
            i = 0
            while (i < elements.size) {
                val element = elements.get(i)
                if (element.isClickable) {
                    val text = element.text
                    var j = 0
                    while (j < keywords.size) {
                        if (text.contains(keywords.get(j), ignoreCase = true)) {
                            target = element
                            break
                        }
                        j = j + 1
                    }
                    if (target != null) break
                }
                i = i + 1
            }
        }

        if (target != null) {
            lastSuggestedAction = ScreenAnalyzer.SuggestedAction.SELECT_TRAIN
            var targetId: String? = null
            if (target.id.isNotEmpty()) {
                targetId = target.id
            }
            return WorkflowAction.Click(
                targetId = targetId,
                coordinates = getCoordinates(target)
            )
        }
        return null
    }

    private fun handleClassSelection(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val details = passengerDetails ?: return null
        val uiElements = analysis.evidence?.uiElements ?: return null
        
        val elements = convertUiElements(uiElements)

        var i = 0
        var target: UIElement? = null
        while (i < elements.size) {
            val element = elements.get(i)
            if (element.isClickable && element.text.contains(details.trainClass, ignoreCase = true)) {
                target = element
                break
            }
            i = i + 1
        }

        if (target != null) {
            lastSuggestedAction = ScreenAnalyzer.SuggestedAction.SELECT_CLASS
            var targetId: String? = null
            if (target.id.isNotEmpty()) {
                targetId = target.id
            }
            return WorkflowAction.Click(
                targetId = targetId,
                coordinates = getCoordinates(target)
            )
        }
        return null
    }

    private fun handlePassengerName(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val details = passengerDetails ?: return null
        val uiElements = analysis.evidence?.uiElements ?: return null
        
        val elements = convertUiElements(uiElements)

        var i = 0
        var nameTarget: UIElement? = null
        while (i < elements.size) {
            val element = elements.get(i)
            if (element.isEditable) {
                val hint = element.hint
                val text = element.text
                val desc = element.contentDescription
                if (hint != null && hint.contains("Name", ignoreCase = true)) {
                    nameTarget = element
                    break
                }
                if (text.contains("Name", ignoreCase = true)) {
                    nameTarget = element
                    break
                }
                if (desc != null && desc.contains("Name", ignoreCase = true)) {
                    nameTarget = element
                    break
                }
            }
            i = i + 1
        }

        if (nameTarget != null) {
            lastSuggestedAction = ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_NAME
            currentState = WorkflowState.PASSENGER_NAME_TYPED
            var targetId: String? = null
            if (nameTarget.id.isNotEmpty()) {
                targetId = nameTarget.id
            }
            return WorkflowAction.SetText(
                targetId = targetId,
                text = details.name
            )
        }
        return null
    }

    private fun handlePassengerAgeFromAnalysis(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val details = passengerDetails ?: return null
        val uiElements = analysis.evidence?.uiElements ?: return null
        
        val elements = convertUiElements(uiElements)

        var i = 0
        var ageTarget: UIElement? = null
        while (i < elements.size) {
            val element = elements.get(i)
            if (element.isEditable) {
                val hint = element.hint
                val text = element.text
                val desc = element.contentDescription
                if (hint != null && hint.contains("Age", ignoreCase = true)) {
                    ageTarget = element
                    break
                }
                if (text.contains("Age", ignoreCase = true)) {
                    ageTarget = element
                    break
                }
                if (desc != null && desc.contains("Age", ignoreCase = true)) {
                    ageTarget = element
                    break
                }
            }
            i = i + 1
        }

        if (ageTarget != null) {
            currentState = WorkflowState.PASSENGER_AGE_TYPED
            var targetId: String? = null
            if (ageTarget.id.isNotEmpty()) {
                targetId = ageTarget.id
            }
            return WorkflowAction.SetText(
                targetId = targetId,
                text = details.age
            )
        }
        return null
    }

    private fun handlePassengerAge(
        uiElements: List<UIElement>
    ): WorkflowAction? {
        val details = passengerDetails ?: return null

        var i = 0
        var ageTarget: UIElement? = null
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isEditable) {
                val hint = element.hint
                val text = element.text
                val desc = element.contentDescription
                if (hint != null && hint.contains("Age", ignoreCase = true)) {
                    ageTarget = element
                    break
                }
                if (text.contains("Age", ignoreCase = true)) {
                    ageTarget = element
                    break
                }
                if (desc != null && desc.contains("Age", ignoreCase = true)) {
                    ageTarget = element
                    break
                }
            }
            i = i + 1
        }

        if (ageTarget != null) {
            currentState = WorkflowState.PASSENGER_AGE_TYPED
            var targetId: String? = null
            if (ageTarget.id.isNotEmpty()) {
                targetId = ageTarget.id
            }
            return WorkflowAction.SetText(
                targetId = targetId,
                text = details.age
            )
        }
        return null
    }

    private fun handlePassengerGender(
        uiElements: List<UIElement>
    ): WorkflowAction? {
        var i = 0
        var genderTarget: UIElement? = null
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val hint = element.hint
                val text = element.text
                val desc = element.contentDescription
                if (hint != null && hint.contains("Gender", ignoreCase = true)) {
                    genderTarget = element
                    break
                }
                if (text.contains("Gender", ignoreCase = true)) {
                    genderTarget = element
                    break
                }
                if (desc != null && desc.contains("Gender", ignoreCase = true)) {
                    genderTarget = element
                    break
                }
            }
            i = i + 1
        }

        if (genderTarget != null) {
            currentState = WorkflowState.GENDER_DROPDOWN_OPENED
            var targetId: String? = null
            if (genderTarget.id.isNotEmpty()) {
                targetId = genderTarget.id
            }
            return WorkflowAction.Click(
                targetId = targetId,
                coordinates = getCoordinates(genderTarget)
            )
        }
        return null
    }

    private fun handlePassengerMeal(
        uiElements: List<UIElement>
    ): WorkflowAction? {
        var i = 0
        var mealTarget: UIElement? = null
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val hint = element.hint
                val text = element.text
                val desc = element.contentDescription
                if (hint != null && hint.contains("Meal", ignoreCase = true)) {
                    mealTarget = element
                    break
                }
                if (text.contains("Meal", ignoreCase = true)) {
                    mealTarget = element
                    break
                }
                if (desc != null && desc.contains("Meal", ignoreCase = true)) {
                    mealTarget = element
                    break
                }
            }
            i = i + 1
        }

        if (mealTarget != null) {
            currentState = WorkflowState.MEAL_DROPDOWN_OPENED
            var targetId: String? = null
            if (mealTarget.id.isNotEmpty()) {
                targetId = mealTarget.id
            }
            return WorkflowAction.Click(
                targetId = targetId,
                coordinates = getCoordinates(mealTarget)
            )
        }
        return null
    }

    private fun selectDropdownOption(
        uiElements: List<UIElement>,
        targetText: String
    ): WorkflowAction? {
        var i = 0
        var target: UIElement? = null
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable && element.text.contains(targetText, ignoreCase = true)) {
                target = element
                break
            }
            i = i + 1
        }

        if (target != null) {
            currentState = when (currentState) {
                WorkflowState.GENDER_DROPDOWN_OPENED -> WorkflowState.PASSENGER_GENDER_SELECTED
                WorkflowState.MEAL_DROPDOWN_OPENED -> WorkflowState.PASSENGER_MEAL_SELECTED
                else -> currentState
            }
            var targetId: String? = null
            if (target.id.isNotEmpty()) {
                targetId = target.id
            }
            return WorkflowAction.Click(
                targetId = targetId,
                coordinates = getCoordinates(target)
            )
        }
        return null
    }

    private fun handleAddPassenger(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val uiElements = analysis.evidence?.uiElements ?: return null
        val elements = convertUiElements(uiElements)

        var i = 0
        var addNewButton: UIElement? = null
        while (i < elements.size) {
            val element = elements.get(i)
            if (element.isClickable && element.text.contains("Add New", ignoreCase = true)) {
                addNewButton = element
                break
            }
            i = i + 1
        }

        if (addNewButton != null) {
            var targetId: String? = null
            if (addNewButton.id.isNotEmpty()) {
                targetId = addNewButton.id
            }
            return WorkflowAction.Click(
                targetId = targetId,
                coordinates = getCoordinates(addNewButton)
            )
        }
        return null
    }

    private fun handleReviewAndProceed(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val uiElements = analysis.evidence?.uiElements ?: return null
        val elements = convertUiElements(uiElements)

        var i = 0
        var target: UIElement? = null
        while (i < elements.size) {
            val element = elements.get(i)
            if (element.isClickable) {
                val text = element.text
                if (text.contains("REVIEW", ignoreCase = true) ||
                    text.contains("Proceed to Pay", ignoreCase = true) ||
                    text.contains("PROCEED TO PAY", ignoreCase = true)) {
                    target = element
                    break
                }
            }
            i = i + 1
        }

        if (target != null) {
            lastSuggestedAction = ScreenAnalyzer.SuggestedAction.REVIEW_JOURNEY
            currentState = WorkflowState.STOPPED
            var targetId: String? = null
            if (target.id.isNotEmpty()) {
                targetId = target.id
            }
            return WorkflowAction.Click(
                targetId = targetId,
                coordinates = getCoordinates(target)
            )
        }
        return null
    }

    // ==================== HELPER FUNCTIONS ====================

    private fun getCoordinates(
        element: UIElement
    ): Pair<Int, Int>? {
        val bounds = element.bounds
        if (bounds != null) {
            val left = bounds.left
            val right = bounds.right
            val top = bounds.top
            val bottom = bounds.bottom
            return Pair((left + right) / 2, (top + bottom) / 2)
        }
        return null
    }

    private fun convertUiElements(
        original: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): List<UIElement> {
        val result = mutableListOf<UIElement>()
        var i = 0
        while (i < original.size) {
            val element = original.get(i)
            var bounds: BoundingBox? = null
            val originalBounds = element.bounds
            if (originalBounds != null) {
                bounds = BoundingBox(
                    left = originalBounds.left,
                    top = originalBounds.top,
                    right = originalBounds.right,
                    bottom = originalBounds.bottom
                )
            }
            result.add(
                UIElement(
                    id = element.id,
                    type = element.type,
                    text = element.text,
                    contentDescription = element.contentDescription,
                    bounds = bounds,
                    isClickable = element.isClickable,
                    isEditable = element.isEditable,
                    hint = element.hint
                )
            )
            i = i + 1
        }
        return result
    }
}

// ==================== WORKFLOW ACTION TYPES ====================

sealed class WorkflowAction {
    data class Click(
        val targetId: String? = null,
        val coordinates: Pair<Int, Int>? = null
    ) : WorkflowAction()

    data class SetText(
        val targetId: String? = null,
        val text: String
    ) : WorkflowAction()
}
