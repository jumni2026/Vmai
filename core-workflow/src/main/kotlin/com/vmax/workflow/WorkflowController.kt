package com.vmax.workflow

import com.vmax.action.ExecutionEvent
import com.vmax.action.ExecutionRecorder
import com.vmax.action.MetricsCollector
import com.vmax.core_intelligence.OcrResult
import com.vmax.core_intelligence.ScreenAnalyzer
import com.vmax.core_intelligence.TextClassifier
import com.vmax.core_intelligence.UIEvidenceCollector.ScreenEvidence.UIElement
import com.vmax.model.BookingRequest
import com.vmax.model.PassengerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * VMAX Enterprise v2.6.1
 *
 * File: WorkflowController.kt
 *
 * Platform-independent workflow controller.
 *
 * Responsibilities:
 * - Maintain workflow lifecycle state.
 * - Convert screen analysis into WorkflowAction.
 * - Enforce user/security boundaries.
 * - Maintain workflow session identity.
 * - Coordinate MetricsCollector and ExecutionRecorder.
 *
 * Architecture:
 * - No Android imports.
 * - No AccessibilityService imports.
 * - No Compose imports.
 * - Uses canonical WorkflowState from WorkflowState.kt.
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

        @Volatile
        private var instance: WorkflowController? = null

        /**
         * Returns the globally configured WorkflowController.
         *
         * The actual controller must be initialized by the application
         * composition/wiring layer before use.
         */
        @JvmStatic
        fun getInstance(): WorkflowController {
            return instance
                ?: throw IllegalStateException(
                    "WorkflowController has not been initialized. " +
                        "Initialize it from the application wiring layer first."
                )
        }

        /**
         * Registers the canonical WorkflowController instance.
         *
         * This keeps construction/dependency wiring outside this class.
         */
        @JvmStatic
        fun initialize(controller: WorkflowController) {
            synchronized(this) {
                instance = controller
            }
        }

        /**
         * Clears the singleton reference.
         *
         * Intended for application/test teardown.
         */
        @JvmStatic
        fun clearInstance() {
            synchronized(this) {
                instance = null
            }
        }
    }

    /**
     * Passenger data required by the workflow engine.
     */
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

    // -------------------------------------------------------------------------
    // Canonical workflow state
    // -------------------------------------------------------------------------

    private val _state =
        MutableStateFlow(WorkflowState.IDLE)

    /**
     * Public read-only workflow state.
     */
    val state: StateFlow<WorkflowState> =
        _state.asStateFlow()

    /**
     * Internal state accessor.
     */
    private var currentState: WorkflowState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    // -------------------------------------------------------------------------
    // Runtime session data
    // -------------------------------------------------------------------------

    private var currentSessionId: String = ""

    private var passengerDetails: PassengerDetails? = null

    private var lastSuggestedAction:
        ScreenAnalyzer.SuggestedAction? = null

    // -------------------------------------------------------------------------
    // Public lifecycle API
    // -------------------------------------------------------------------------

    /**
     * Starts the workflow using the platform-independent booking contract.
     *
     * This API is kept for MainViewModel/application orchestration.
     */
    fun start(
        bookingRequest: BookingRequest,
        passengerProfile: PassengerProfile
    ): Boolean {

        val passenger = bookingRequest.passengers.firstOrNull()
            ?: return false

        val details = PassengerDetails(
            from = bookingRequest.fromStation.code,
            to = bookingRequest.toStation.code,
            date = bookingRequest.date,
            train = bookingRequest.train.number,
            trainClass = bookingRequest.train.classType,
            name = passenger.name,
            age = passenger.age.toString(),
            gender = passenger.gender,
            meal = ""
        )

        val sessionId = UUID.randomUUID().toString()

        return startWorkflow(
            details = details,
            sessionId = sessionId
        )
    }

    /**
     * Stops the currently running workflow.
     */
    fun stop() {
        stopWorkflow()
    }

    /**
     * Starts a workflow session.
     */
    fun startWorkflow(
        details: PassengerDetails,
        sessionId: String
    ): Boolean {

        if (currentState != WorkflowState.IDLE) {
            return false
        }

        if (sessionId.isBlank()) {
            return false
        }

        if (!isValidPassengerDetails(details)) {
            return false
        }

        passengerDetails = details
        currentSessionId = sessionId
        lastSuggestedAction = null

        currentState = WorkflowState.CONFIGURED
        currentState = WorkflowState.RUNNING

        return true
    }

    /**
     * Stops the current workflow.
     */
    fun stopWorkflow() {

        if (currentState == WorkflowState.IDLE ||
            currentState == WorkflowState.STOPPED
        ) {
            return
        }

        currentState = WorkflowState.STOPPED
        lastSuggestedAction = null
    }

    /**
     * Returns the current workflow state.
     */
    fun getCurrentState(): WorkflowState =
        currentState

    /**
     * Returns the active session ID.
     */
    fun getSessionId(): String =
        currentSessionId

    /**
     * Returns whether the workflow is currently active.
     */
    fun isActive(): Boolean {
        return currentState == WorkflowState.CONFIGURED ||
            currentState == WorkflowState.RUNNING ||
            currentState == WorkflowState.GENDER_DROPDOWN_OPENED ||
            currentState == WorkflowState.MEAL_DROPDOWN_OPENED ||
            currentState == WorkflowState.PASSENGER_NAME_TYPED ||
            currentState == WorkflowState.PASSENGER_AGE_TYPED ||
            currentState == WorkflowState.PASSENGER_GENDER_SELECTED ||
            currentState == WorkflowState.PASSENGER_MEAL_SELECTED
    }

    /**
     * Updates the workflow state.
     *
     * State transitions remain centralized here.
     */
    fun updateState(
        newState: WorkflowState
    ) {
        currentState = newState
    }

    // -------------------------------------------------------------------------
    // Screen analysis
    // -------------------------------------------------------------------------

    /**
     * Converts screen analysis into a platform-independent WorkflowAction.
     */
    fun handleScreenAnalysis(
        analysis: ScreenAnalyzer.AnalysisResult,
        ocrText: String,
        ocrBlocks: List<OcrResult.TextBlock>
    ): WorkflowAction? {

        if (!isActive()) {
            return null
        }

        val sessionId = currentSessionId

        if (sessionId.isBlank()) {
            return null
        }

        val ocrResult = OcrResult(
            sessionId,
            System.currentTimeMillis(),
            ocrText,
            ocrBlocks
        )

        // -------------------------------------------------------------
        // Security / user boundary
        // -------------------------------------------------------------

        if (classifier.isSensitiveScreen(ocrResult)) {

            if (currentState != WorkflowState.USER_BOUNDARY) {

                recorder.recordEvent(
                    ExecutionEvent.SessionError(
                        sessionId,
                        "SECURITY_BOUNDARY",
                        "Sensitive screen detected"
                    )
                )

                metrics.stopMetrics(
                    sessionId,
                    "USER_BOUNDARY"
                )

                currentState =
                    WorkflowState.USER_BOUNDARY

                lastSuggestedAction = null
            }

            return null
        }

        if (currentState == WorkflowState.USER_BOUNDARY ||
            currentState == WorkflowState.STOPPED ||
            currentState == WorkflowState.ERROR
        ) {
            return null
        }

        val suggestedAction =
            analysis.suggestedAction

        if (suggestedAction ==
            lastSuggestedAction &&
            suggestedAction !=
            ScreenAnalyzer.SuggestedAction.NONE
        ) {
            return null
        }

        return when (suggestedAction) {

            ScreenAnalyzer.SuggestedAction.SELECT_TRAIN ->
                handleTrainSelection(analysis)

            ScreenAnalyzer.SuggestedAction.SELECT_CLASS ->
                handleClassSelection(analysis)

            ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_NAME ->
                handlePassengerName(analysis)

            ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_AGE ->
                handlePassengerAgeFromAnalysis(analysis)

            ScreenAnalyzer.SuggestedAction.ADD_PASSENGER ->
                handleAddPassenger(analysis)

            ScreenAnalyzer.SuggestedAction.REVIEW_JOURNEY ->
                handleReviewAndProceed(analysis)

            else ->
                null
        }
    }

    // -------------------------------------------------------------------------
    // State-driven actions
    // -------------------------------------------------------------------------

    /**
     * Produces the next action for the current passenger-entry state.
     */
    fun handleStateAction(
        state: WorkflowState,
        uiElements: List<UIElement>
    ): WorkflowAction? {

        return when (state) {

            WorkflowState.GENDER_DROPDOWN_OPENED -> {
                selectDropdownOption(
                    uiElements,
                    passengerDetails?.gender.orEmpty()
                )
            }

            WorkflowState.MEAL_DROPDOWN_OPENED -> {

                val meal =
                    passengerDetails?.meal.orEmpty()

                if (meal.isBlank()) {
                    null
                } else {
                    selectDropdownOption(
                        uiElements,
                        meal
                    )
                }
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
                currentState = WorkflowState.RUNNING

                null
            }

            else -> {
                null
            }
        }
    }

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------

    private fun isValidPassengerDetails(
        details: PassengerDetails
    ): Boolean {

        return details.from.isNotBlank() &&
            details.to.isNotBlank() &&
            details.date.isNotBlank() &&
            details.train.isNotBlank() &&
            details.trainClass.isNotBlank() &&
            details.name.isNotBlank() &&
            details.age.isNotBlank() &&
            details.gender.isNotBlank()
    }

    // -------------------------------------------------------------------------
    // TRAIN
    // -------------------------------------------------------------------------

    private fun handleTrainSelection(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val details =
            passengerDetails ?: return null

        val elements =
            analysis.evidence?.uiElements ?: return null

        var target: UIElement? = null

        for (element in elements) {

            if (!element.isClickable) {
                continue
            }

            val text =
                element.text

            if (
                text.equals(
                    details.train,
                    ignoreCase = true
                ) ||
                text.contains(
                    details.train,
                    ignoreCase = true
                )
            ) {
                target = element
                break
            }
        }

        if (target == null) {

            val keywords = arrayOf(
                "SELECT",
                "VIEW",
                "BOOK",
                "SEARCH",
                "FIND TRAINS",
                "CHECK"
            )

            for (element in elements) {

                if (!element.isClickable) {
                    continue
                }

                val text =
                    element.text

                if (
                    keywords.any {
                        text.contains(
                            it,
                            ignoreCase = true
                        )
                    }
                ) {
                    target = element
                    break
                }
            }
        }

        if (target == null) {
            return null
        }

        lastSuggestedAction =
            ScreenAnalyzer.SuggestedAction.SELECT_TRAIN

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates = getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // CLASS
    // -------------------------------------------------------------------------

    private fun handleClassSelection(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val details =
            passengerDetails ?: return null

        val elements =
            analysis.evidence?.uiElements ?: return null

        val target =
            elements.firstOrNull {
                it.isClickable &&
                    it.text.contains(
                        details.trainClass,
                        ignoreCase = true
                    )
            }
            ?: return null

        lastSuggestedAction =
            ScreenAnalyzer.SuggestedAction.SELECT_CLASS

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates = getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // PASSENGER NAME
    // -------------------------------------------------------------------------

    private fun handlePassengerName(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val details =
            passengerDetails ?: return null

        val elements =
            analysis.evidence?.uiElements ?: return null

        val target =
            findEditableByLabel(
                elements,
                "Name"
            )
            ?: return null

        lastSuggestedAction =
            ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_NAME

        currentState =
            WorkflowState.PASSENGER_NAME_TYPED

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.SetText(
            targetId = targetId,
            text = details.name
        )
    }

    // -------------------------------------------------------------------------
    // PASSENGER AGE
    // -------------------------------------------------------------------------

    private fun handlePassengerAgeFromAnalysis(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val details =
            passengerDetails ?: return null

        val elements =
            analysis.evidence?.uiElements ?: return null

        val target =
            findEditableByLabel(
                elements,
                "Age"
            )
            ?: return null

        currentState =
            WorkflowState.PASSENGER_AGE_TYPED

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.SetText(
            targetId = targetId,
            text = details.age
        )
    }

    private fun handlePassengerAge(
        uiElements: List<UIElement>
    ): WorkflowAction? {

        val details =
            passengerDetails ?: return null

        val ageTarget =
            findEditableByLabel(
                uiElements,
                "Age"
            )
            ?: return null

        currentState =
            WorkflowState.PASSENGER_AGE_TYPED

        val targetId =
            ageTarget.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.SetText(
            targetId = targetId,
            text = details.age
        )
    }

    // -------------------------------------------------------------------------
    // GENDER
    // -------------------------------------------------------------------------

    private fun handlePassengerGender(
        uiElements: List<UIElement>
    ): WorkflowAction? {

        val genderTarget =
            findClickableByLabel(
                uiElements,
                "Gender"
            )
            ?: return null

        currentState =
            WorkflowState.GENDER_DROPDOWN_OPENED

        val targetId =
            genderTarget.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates = getCoordinates(genderTarget)
        )
    }

    // -------------------------------------------------------------------------
    // MEAL
    // -------------------------------------------------------------------------

    private fun handlePassengerMeal(
        uiElements: List<UIElement>
    ): WorkflowAction? {

        val details =
            passengerDetails ?: return null

        /*
         * Meal is optional.
         *
         * If no meal preference is configured, do not click an
         * arbitrary meal control.
         */
        if (details.meal.isBlank()) {
            currentState =
                WorkflowState.PASSENGER_MEAL_SELECTED

            return null
        }

        val mealTarget =
            findClickableByLabel(
                uiElements,
                "Meal"
            )
            ?: return null

        currentState =
            WorkflowState.MEAL_DROPDOWN_OPENED

        val targetId =
            mealTarget.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates = getCoordinates(mealTarget)
        )
    }

    // -------------------------------------------------------------------------
    // DROPDOWN OPTION
    // -------------------------------------------------------------------------

    private fun selectDropdownOption(
        uiElements: List<UIElement>,
        targetText: String
    ): WorkflowAction? {

        if (targetText.isBlank()) {
            return null
        }

        val target =
            uiElements.firstOrNull {
                it.isClickable &&
                    it.text.contains(
                        targetText,
                        ignoreCase = true
                    )
            }
            ?: return null

        currentState =
            when (currentState) {

                WorkflowState.GENDER_DROPDOWN_OPENED ->
                    WorkflowState.PASSENGER_GENDER_SELECTED

                WorkflowState.MEAL_DROPDOWN_OPENED ->
                    WorkflowState.PASSENGER_MEAL_SELECTED

                else ->
                    currentState
            }

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates = getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // ADD PASSENGER
    // -------------------------------------------------------------------------

    private fun handleAddPassenger(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val elements =
            analysis.evidence?.uiElements ?: return null

        val target =
            elements.firstOrNull {
                it.isClickable &&
                    it.text.contains(
                        "Add New",
                        ignoreCase = true
                    )
            }
            ?: return null

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates = getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // REVIEW / USER BOUNDARY
    // -------------------------------------------------------------------------

    private fun handleReviewAndProceed(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val elements =
            analysis.evidence?.uiElements ?: return null

        val target =
            elements.firstOrNull { element ->

                if (!element.isClickable) {
                    return@firstOrNull false
                }

                val text =
                    element.text

                text.contains(
                    "REVIEW",
                    ignoreCase = true
                ) ||
                    text.contains(
                        "Proceed to Pay",
                        ignoreCase = true
                    ) ||
                    text.contains(
                        "PROCEED TO PAY",
                        ignoreCase = true
                    )
            }
            ?: return null

        lastSuggestedAction =
            ScreenAnalyzer.SuggestedAction.REVIEW_JOURNEY

        /*
         * Reaching Review/Proceed-to-Pay is an explicit user-controlled
         * boundary. The controller must not continue beyond it.
         */
        currentState =
            WorkflowState.USER_BOUNDARY

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates = getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // UI helpers
    // -------------------------------------------------------------------------

    private fun findEditableByLabel(
        elements: List<UIElement>,
        label: String
    ): UIElement? {

        return elements.firstOrNull { element ->

            if (!element.isEditable) {
                return@firstOrNull false
            }

            val hint =
                element.hint.orEmpty()

            val text =
                element.text

            val description =
                element.contentDescription.orEmpty()

            hint.contains(
                label,
                ignoreCase = true
            ) ||
                text.contains(
                    label,
                    ignoreCase = true
                ) ||
                description.contains(
                    label,
                    ignoreCase = true
                )
        }
    }

    private fun findClickableByLabel(
        elements: List<UIElement>,
        label: String
    ): UIElement? {

        return elements.firstOrNull { element ->

            if (!element.isClickable) {
                return@firstOrNull false
            }

            val hint =
                element.hint.orEmpty()

            val text =
                element.text

            val description =
                element.contentDescription.orEmpty()

            hint.contains(
                label,
                ignoreCase = true
            ) ||
                text.contains(
                    label,
                    ignoreCase = true
                ) ||
                description.contains(
                    label,
                    ignoreCase = true
                )
        }
    }

    private fun getCoordinates(
        element: UIElement
    ): Pair<Int, Int>? {

        val bounds =
            element.bounds
                ?: return null

        return Pair(
            (bounds.left + bounds.right) / 2,
            (bounds.top + bounds.bottom) / 2
        )
    }
}

/**
 * Platform-independent action generated by WorkflowController.
 */
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
