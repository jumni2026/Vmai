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
 * File:
 * WorkflowController.kt
 *
 * Canonical, platform-independent workflow controller.
 *
 * Responsibilities:
 * - Maintain workflow lifecycle.
 * - Maintain canonical WorkflowState.
 * - Maintain workflow session identity.
 * - Convert screen analysis into WorkflowAction.
 * - Enforce security/user boundaries.
 * - Coordinate MetricsCollector.
 * - Coordinate ExecutionRecorder.
 *
 * Architecture rules:
 * - No Android imports.
 * - No Compose imports.
 * - No AccessibilityService imports.
 * - No duplicate WorkflowState enum.
 * - Uses WorkflowState.kt as the single source of truth.
 *
 * JVM rule:
 * - Internal state property is named workflowState.
 * - Public getCurrentState() is retained for Java/UI compatibility.
 * - This avoids Kotlin's generated getCurrentState() JVM clash.
 */
class WorkflowController(
    private val orchestrator: ActionOrchestrator,
    private val analyzer: ScreenAnalyzer,
    private val classifier: TextClassifier,
    private val metrics: MetricsCollector,
    private val recorder: ExecutionRecorder
) {

    companion object {

        /**
         * IRCTC Android package identifier.
         *
         * Actual package filtering belongs to the
         * Android AccessibilityService layer.
         */
        private const val IRCTC_PACKAGE =
            "cris.org.in.prs.ima"

        @Volatile
        private var instance: WorkflowController? = null

        /**
         * Returns the globally configured controller.
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
         * Initializes the canonical controller instance.
         */
        @JvmStatic
        fun initialize(
            controller: WorkflowController
        ) {
            synchronized(this) {
                if (instance != null) {
                    throw IllegalStateException(
                        "WorkflowController is already initialized."
                    )
                }

                instance = controller
            }
        }

        /**
         * Replaces the current controller instance.
         *
         * Intended for controlled application/test wiring.
         */
        @JvmStatic
        fun replaceInstance(
            controller: WorkflowController
        ) {
            synchronized(this) {
                instance = controller
            }
        }

        /**
         * Clears the singleton reference.
         */
        @JvmStatic
        fun clearInstance() {
            synchronized(this) {
                instance = null
            }
        }
    }

    // -------------------------------------------------------------------------
    // Passenger contract
    // -------------------------------------------------------------------------

    /**
     * Passenger details required by the workflow engine.
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
     * Read-only state exposed to application/UI layers.
     */
    val state: StateFlow<WorkflowState> =
        _state.asStateFlow()

    /**
     * Internal workflow state.
     *
     * IMPORTANT:
     * Do NOT rename this property to currentState.
     *
     * Kotlin would generate getCurrentState(), which would clash
     * with the explicit getCurrentState() method below.
     */
    private var workflowState: WorkflowState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    // -------------------------------------------------------------------------
    // Session data
    // -------------------------------------------------------------------------

    private var currentSessionId: String = ""

    private var passengerDetails: PassengerDetails? = null

    /**
     * Last action that was actually converted into a WorkflowAction.
     *
     * A failed target search must NOT update this value.
     */
    private var lastSuggestedAction:
        ScreenAnalyzer.SuggestedAction? = null

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Starts a workflow from the application-level booking contract.
     */
    fun start(
        bookingRequest: BookingRequest,
        passengerProfile: PassengerProfile
    ): Boolean {

        /*
         * PassengerProfile is part of the application contract.
         *
         * The workflow currently uses the first passenger from
         * BookingRequest.
         */
        if (passengerProfile.passengers.isEmpty()) {
            return false
        }

        val passenger =
            bookingRequest.passengers.firstOrNull()
                ?: return false

        val details =
            PassengerDetails(
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

        val sessionId =
            UUID.randomUUID().toString()

        return startWorkflow(
            details = details,
            sessionId = sessionId
        )
    }

    /**
     * Stops the currently active workflow.
     */
    fun stop() {
        stopWorkflow()
    }

    /**
     * Starts a new workflow session.
     */
    fun startWorkflow(
        details: PassengerDetails,
        sessionId: String
    ): Boolean {

        /*
         * Only IDLE can start a new session.
         */
        if (workflowState != WorkflowState.IDLE) {
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

        /*
         * Configuration accepted.
         */
        workflowState =
            WorkflowState.CONFIGURED

        /*
         * Execution begins.
         */
        workflowState =
            WorkflowState.RUNNING

        return true
    }

    /**
     * Stops the current workflow.
     */
    fun stopWorkflow() {

        if (
            workflowState == WorkflowState.IDLE ||
            workflowState == WorkflowState.STOPPED
        ) {
            return
        }

        workflowState =
            WorkflowState.STOPPED

        lastSuggestedAction = null
    }

    /**
     * Resets the controller after a completed/stopped/error/boundary
     * session.
     */
    fun reset(): Boolean {

        if (isActive()) {
            return false
        }

        currentSessionId = ""
        passengerDetails = null
        lastSuggestedAction = null

        workflowState =
            WorkflowState.IDLE

        return true
    }

    // -------------------------------------------------------------------------
    // State API
    // -------------------------------------------------------------------------

    /**
     * Returns the current canonical workflow state.
     *
     * Explicit method retained for Java/UI compatibility.
     */
    fun getCurrentState(): WorkflowState =
        workflowState

    /**
     * Returns the current session ID.
     */
    fun getSessionId(): String =
        currentSessionId

    /**
     * Returns true when the controller is allowed to process
     * workflow/screen events.
     *
     * IMPORTANT:
     * ARMED is an active workflow state and therefore MUST be
     * included here.
     */
    fun isActive(): Boolean {

        return when (workflowState) {

            WorkflowState.CONFIGURED,
            WorkflowState.RUNNING,
            WorkflowState.ARMED,
            WorkflowState.GENDER_DROPDOWN_OPENED,
            WorkflowState.MEAL_DROPDOWN_OPENED,
            WorkflowState.PASSENGER_NAME_TYPED,
            WorkflowState.PASSENGER_AGE_TYPED,
            WorkflowState.PASSENGER_GENDER_SELECTED,
            WorkflowState.PASSENGER_MEAL_SELECTED -> true

            WorkflowState.IDLE,
            WorkflowState.USER_BOUNDARY,
            WorkflowState.STOPPED,
            WorkflowState.ERROR -> false
        }
    }

    /**
     * Updates state through the canonical WorkflowState contract.
     */
    fun updateState(
        newState: WorkflowState
    ) {

        /*
         * Hard boundary states clear pending actions.
         */
        if (
            newState == WorkflowState.USER_BOUNDARY ||
            newState == WorkflowState.STOPPED ||
            newState == WorkflowState.ERROR
        ) {
            lastSuggestedAction = null
        }

        workflowState =
            newState
    }

    // -------------------------------------------------------------------------
    // Screen analysis
    // -------------------------------------------------------------------------

    /**
     * Converts screen analysis into a platform-independent action.
     */
    fun handleScreenAnalysis(
        analysis: ScreenAnalyzer.AnalysisResult,
        ocrText: String,
        ocrBlocks: List<OcrResult.TextBlock>
    ): WorkflowAction? {

        if (!isActive()) {
            return null
        }

        val sessionId =
            currentSessionId

        if (sessionId.isBlank()) {
            return null
        }

        val ocrResult =
            OcrResult(
                sessionId,
                System.currentTimeMillis(),
                ocrText,
                ocrBlocks
            )

        // ---------------------------------------------------------------------
        // Security boundary
        // ---------------------------------------------------------------------

        if (classifier.isSensitiveScreen(ocrResult)) {

            enterUserBoundary(
                reason = "Sensitive screen detected"
            )

            return null
        }

        /*
         * Explicit hard-boundary states always block automation.
         */
        if (
            workflowState == WorkflowState.USER_BOUNDARY ||
            workflowState == WorkflowState.STOPPED ||
            workflowState == WorkflowState.ERROR
        ) {
            return null
        }

        val suggestedAction =
            analysis.suggestedAction

        /*
         * NONE is never executable.
         */
        if (
            suggestedAction ==
            ScreenAnalyzer.SuggestedAction.NONE
        ) {
            return null
        }

        /*
         * Prevent repeated generation of the same action.
         */
        if (
            suggestedAction ==
            lastSuggestedAction
        ) {
            return null
        }

        val action =
            when (suggestedAction) {

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

        /*
         * Only remember the suggestion when a real action was created.
         */
        if (action != null) {
            lastSuggestedAction =
                suggestedAction
        }

        return action
    }

    // -------------------------------------------------------------------------
    // State-driven actions
    // -------------------------------------------------------------------------

    /**
     * Produces the next action for the supplied workflow state.
     */
    fun handleStateAction(
        state: WorkflowState,
        uiElements: List<UIElement>
    ): WorkflowAction? {

        /*
         * Hard boundaries always win.
         */
        if (
            workflowState == WorkflowState.USER_BOUNDARY ||
            workflowState == WorkflowState.STOPPED ||
            workflowState == WorkflowState.ERROR
        ) {
            return null
        }

        return when (state) {

            WorkflowState.GENDER_DROPDOWN_OPENED -> {

                selectDropdownOption(
                    uiElements = uiElements,
                    targetText =
                        passengerDetails
                            ?.gender
                            .orEmpty()
                )
            }

            WorkflowState.MEAL_DROPDOWN_OPENED -> {

                val meal =
                    passengerDetails
                        ?.meal
                        .orEmpty()

                /*
                 * Meal is optional.
                 */
                if (meal.isBlank()) {

                    workflowState =
                        WorkflowState.PASSENGER_MEAL_SELECTED

                    null

                } else {

                    selectDropdownOption(
                        uiElements = uiElements,
                        targetText = meal
                    )
                }
            }

            WorkflowState.PASSENGER_NAME_TYPED -> {

                handlePassengerAge(
                    uiElements
                )
            }

            WorkflowState.PASSENGER_AGE_TYPED -> {

                handlePassengerGender(
                    uiElements
                )
            }

            WorkflowState.PASSENGER_GENDER_SELECTED -> {

                handlePassengerMeal(
                    uiElements
                )
            }

            WorkflowState.PASSENGER_MEAL_SELECTED -> {

                lastSuggestedAction = null

                workflowState =
                    WorkflowState.RUNNING

                null
            }

            else -> {
                null
            }
        }
    }

    // -------------------------------------------------------------------------
    // Security boundary
    // -------------------------------------------------------------------------

    /**
     * Enters the explicit user-controlled boundary.
     *
     * Automation cannot continue automatically from this state.
     */
    private fun enterUserBoundary(
        reason: String
    ) {

        if (
            workflowState ==
            WorkflowState.USER_BOUNDARY
        ) {
            return
        }

        val sessionId =
            currentSessionId

        if (sessionId.isNotBlank()) {

            recorder.recordEvent(
                ExecutionEvent.SessionError(
                    sessionId,
                    "SECURITY_BOUNDARY",
                    reason
                )
            )

            metrics.stopMetrics(
                sessionId,
                "USER_BOUNDARY"
            )
        }

        lastSuggestedAction = null

        workflowState =
            WorkflowState.USER_BOUNDARY
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
            passengerDetails
                ?: return null

        val elements =
            analysis.evidence
                ?.uiElements
                ?: return null

        var target: UIElement? = null

        /*
         * First preference:
         * exact or contains train-number match.
         */
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

        /*
         * Fallback:
         * common train-selection controls.
         */
        if (target == null) {

            val keywords =
                arrayOf(
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
                    keywords.any { keyword ->
                        text.contains(
                            keyword,
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

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates =
                getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // CLASS
    // -------------------------------------------------------------------------

    private fun handleClassSelection(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val details =
            passengerDetails
                ?: return null

        val elements =
            analysis.evidence
                ?.uiElements
                ?: return null

        val target =
            elements.firstOrNull { element ->

                element.isClickable &&
                    element.text.contains(
                        details.trainClass,
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
            coordinates =
                getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // PASSENGER NAME
    // -------------------------------------------------------------------------

    private fun handlePassengerName(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val details =
            passengerDetails
                ?: return null

        val elements =
            analysis.evidence
                ?.uiElements
                ?: return null

        val target =
            findEditableByLabel(
                elements,
                "Name"
            )
                ?: return null

        workflowState =
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
            passengerDetails
                ?: return null

        val elements =
            analysis.evidence
                ?.uiElements
                ?: return null

        val target =
            findEditableByLabel(
                elements,
                "Age"
            )
                ?: return null

        workflowState =
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
            passengerDetails
                ?: return null

        val target =
            findEditableByLabel(
                uiElements,
                "Age"
            )
                ?: return null

        workflowState =
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

    // -------------------------------------------------------------------------
    // GENDER
    // -------------------------------------------------------------------------

    private fun handlePassengerGender(
        uiElements: List<UIElement>
    ): WorkflowAction? {

        val target =
            findClickableByLabel(
                uiElements,
                "Gender"
            )
                ?: return null

        workflowState =
            WorkflowState.GENDER_DROPDOWN_OPENED

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates =
                getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // MEAL
    // -------------------------------------------------------------------------

    private fun handlePassengerMeal(
        uiElements: List<UIElement>
    ): WorkflowAction? {

        val details =
            passengerDetails
                ?: return null

        /*
         * Meal is optional.
         *
         * Never click an arbitrary Meal control when
         * no preference has been configured.
         */
        if (details.meal.isBlank()) {

            workflowState =
                WorkflowState.PASSENGER_MEAL_SELECTED

            return null
        }

        val target =
            findClickableByLabel(
                uiElements,
                "Meal"
            )
                ?: return null

        workflowState =
            WorkflowState.MEAL_DROPDOWN_OPENED

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates =
                getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // DROPDOWN
    // -------------------------------------------------------------------------

    private fun selectDropdownOption(
        uiElements: List<UIElement>,
        targetText: String
    ): WorkflowAction? {

        if (targetText.isBlank()) {
            return null
        }

        val target =
            uiElements.firstOrNull { element ->

                element.isClickable &&
                    element.text.contains(
                        targetText,
                        ignoreCase = true
                    )
            }
                ?: return null

        workflowState =
            when (workflowState) {

                WorkflowState.GENDER_DROPDOWN_OPENED ->
                    WorkflowState.PASSENGER_GENDER_SELECTED

                WorkflowState.MEAL_DROPDOWN_OPENED ->
                    WorkflowState.PASSENGER_MEAL_SELECTED

                else ->
                    workflowState
            }

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates =
                getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // ADD PASSENGER
    // -------------------------------------------------------------------------

    private fun handleAddPassenger(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val elements =
            analysis.evidence
                ?.uiElements
                ?: return null

        val target =
            elements.firstOrNull { element ->

                element.isClickable &&
                    element.text.contains(
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
            coordinates =
                getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // REVIEW / USER BOUNDARY
    // -------------------------------------------------------------------------

    private fun handleReviewAndProceed(
        analysis: ScreenAnalyzer.AnalysisResult
    ): WorkflowAction? {

        val elements =
            analysis.evidence
                ?.uiElements
                ?: return null

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

        /*
         * Review / Proceed-to-Pay is a hard user boundary.
         *
         * The controller may return this boundary action,
         * but it will not generate another automatic action
         * afterwards.
         */
        workflowState =
            WorkflowState.USER_BOUNDARY

        lastSuggestedAction = null

        val targetId =
            target.id.takeIf {
                it.isNotEmpty()
            }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates =
                getCoordinates(target)
        )
    }

    // -------------------------------------------------------------------------
    // UI helpers
    // -------------------------------------------------------------------------

    /**
     * Finds an editable element by hint, text, or content description.
     */
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

    /**
     * Finds a clickable element by hint, text, or content description.
     */
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

    /**
     * Calculates the center point of a UI element.
     *
     * Coordinates are only an execution hint.
     * The platform execution layer decides how they are used.
     */
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
 *
 * WorkflowController never executes these actions directly.
 * The execution layer performs the actual operation.
 */
sealed class WorkflowAction {

    /**
     * Click action.
     *
     * targetId is preferred when available.
     * Coordinates are an optional fallback.
     */
    data class Click(
        val targetId: String? = null,
        val coordinates: Pair<Int, Int>? = null
    ) : WorkflowAction()

    /**
     * Set text into an editable UI element.
     */
    data class SetText(
        val targetId: String? = null,
        val text: String
    ) : WorkflowAction()
}
