package com.vmax.workflow

import com.vmax.core_intelligence.ScreenAnalyzer
import com.vmax.core_intelligence.TextClassifier
import com.vmax.core_intelligence.OcrResult
import com.vmax.runtime.MetricsCollector
import com.vmax.runtime.ExecutionRecorder
import com.vmax.action.ActionExecutor
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
        ocrBlocks: List<OcrResult.BoundingBox>
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
            ScreenAnalyzer.SuggestedAction.CHECK_AVAILABILITY -> {
                handleClassSelection(analysis)
            }
            ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_DETAILS -> {
                handlePassengerDetails(analysis)
            }
            ScreenAnalyzer.SuggestedAction.REVIEW_AND_PROCEED -> {
                handleReviewAndProceed(analysis)
            }
            else -> null
        }
    }

    fun handleStateAction(
        state: WorkflowState,
        uiElements: List<ScreenAnalyzer.UIElement>
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

    // --- Private Handler Methods ---

    private fun handleTrainSelection(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val details = passengerDetails ?: return null
        val uiElements = analysis.evidence?.uiElements ?: return null

        val target = findClickableElement(uiElements) { element ->
            element.text.equals(details.train, ignoreCase = true) ||
            element.text.contains(details.train, ignoreCase = true)
        } ?: findClickableElement(uiElements) { element ->
            val keywords = listOf("SELECT", "VIEW", "BOOK", "SEARCH", "FIND TRAINS", "CHECK")
            keywords.any { element.text.contains(it, ignoreCase = true) }
        }

        return target?.let {
            lastSuggestedAction = ScreenAnalyzer.SuggestedAction.SELECT_TRAIN
            WorkflowAction.Click(
                targetId = it.id,
                coordinates = getCoordinates(it)
            )
        }
    }

    private fun handleClassSelection(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val details = passengerDetails ?: return null
        val uiElements = analysis.evidence?.uiElements ?: return null

        val target = findClickableElement(uiElements) { element ->
            element.text.contains(details.trainClass, ignoreCase = true)
        }

        return target?.let {
            lastSuggestedAction = ScreenAnalyzer.SuggestedAction.CHECK_AVAILABILITY
            WorkflowAction.Click(
                targetId = it.id,
                coordinates = getCoordinates(it)
            )
        }
    }

    private fun handlePassengerDetails(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val details = passengerDetails ?: return null
        val uiElements = analysis.evidence?.uiElements ?: return null

        val nameTarget = findEditableElement(uiElements) { element ->
            element.hint?.contains("Name", ignoreCase = true) == true ||
            element.text.contains("Name", ignoreCase = true) ||
            element.contentDescription?.contains("Name", ignoreCase = true) == true
        }

        return nameTarget?.let {
            lastSuggestedAction = ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_DETAILS
            currentState = WorkflowState.PASSENGER_NAME_TYPED
            WorkflowAction.SetText(
                targetId = it.id,
                text = details.name
            )
        }
    }

    private fun handlePassengerAge(
        uiElements: List<ScreenAnalyzer.UIElement>
    ): WorkflowAction? {
        val details = passengerDetails ?: return null

        val ageTarget = findEditableElement(uiElements) { element ->
            element.hint?.contains("Age", ignoreCase = true) == true ||
            element.text.contains("Age", ignoreCase = true) ||
            element.contentDescription?.contains("Age", ignoreCase = true) == true
        }

        return ageTarget?.let {
            currentState = WorkflowState.PASSENGER_AGE_TYPED
            WorkflowAction.SetText(
                targetId = it.id,
                text = details.age
            )
        }
    }

    private fun handlePassengerGender(
        uiElements: List<ScreenAnalyzer.UIElement>
    ): WorkflowAction? {
        val genderTarget = findClickableElement(uiElements) { element ->
            element.hint?.contains("Gender", ignoreCase = true) == true ||
            element.text.contains("Gender", ignoreCase = true) ||
            element.contentDescription?.contains("Gender", ignoreCase = true) == true
        }

        return genderTarget?.let {
            currentState = WorkflowState.GENDER_DROPDOWN_OPENED
            WorkflowAction.Click(
                targetId = it.id,
                coordinates = getCoordinates(it)
            )
        }
    }

    private fun handlePassengerMeal(
        uiElements: List<ScreenAnalyzer.UIElement>
    ): WorkflowAction? {
        val mealTarget = findClickableElement(uiElements) { element ->
            element.hint?.contains("Meal", ignoreCase = true) == true ||
            element.text.contains("Meal", ignoreCase = true) ||
            element.contentDescription?.contains("Meal", ignoreCase = true) == true
        }

        return mealTarget?.let {
            currentState = WorkflowState.MEAL_DROPDOWN_OPENED
            WorkflowAction.Click(
                targetId = it.id,
                coordinates = getCoordinates(it)
            )
        }
    }

    private fun selectDropdownOption(
        uiElements: List<ScreenAnalyzer.UIElement>,
        targetText: String
    ): WorkflowAction? {
        val target = findClickableElement(uiElements) { element ->
            element.text.contains(targetText, ignoreCase = true)
        }

        return target?.let {
            currentState = when (currentState) {
                WorkflowState.GENDER_DROPDOWN_OPENED -> WorkflowState.PASSENGER_GENDER_SELECTED
                WorkflowState.MEAL_DROPDOWN_OPENED -> WorkflowState.PASSENGER_MEAL_SELECTED
                else -> currentState
            }
            WorkflowAction.Click(
                targetId = it.id,
                coordinates = getCoordinates(it)
            )
        }
    }

    private fun handleReviewAndProceed(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {
        val uiElements = analysis.evidence?.uiElements ?: return null

        val target = findClickableElement(uiElements) { element ->
            element.text.contains("REVIEW", ignoreCase = true)
        }

        return target?.let {
            lastSuggestedAction = ScreenAnalyzer.SuggestedAction.REVIEW_AND_PROCEED
            currentState = WorkflowState.STOPPED
            WorkflowAction.Click(
                targetId = it.id,
                coordinates = getCoordinates(it)
            )
        }
    }

    // --- Helper Functions ---

    private fun findClickableElement(
        elements: List<ScreenAnalyzer.UIElement>,
        predicate: (ScreenAnalyzer.UIElement) -> Boolean
    ): ScreenAnalyzer.UIElement? {
        return elements.firstOrNull { it.isClickable && predicate(it) }
    }

    private fun findEditableElement(
        elements: List<ScreenAnalyzer.UIElement>,
        predicate: (ScreenAnalyzer.UIElement) -> Boolean
    ): ScreenAnalyzer.UIElement? {
        return elements.firstOrNull { it.isEditable && predicate(it) }
    }

    private fun getCoordinates(
        element: ScreenAnalyzer.UIElement
    ): Pair<Int, Int>? {
        return element.bounds?.let {
            Pair((it.left + it.right) / 2, (it.top + it.bottom) / 2)
        }
    }
}

// --- Workflow Action Types ---

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
