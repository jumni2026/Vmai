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
         * Kept here as workflow-level metadata only.
         * Actual Android package filtering belongs to the platform layer.
         */
        private const val IRCTC_PACKAGE =
            "cris.org.in.prs.ima"

        @Volatile
        private var instance: WorkflowController? = null

        /**
         * Returns the globally configured controller.
         *
         * The application wiring layer must initialize this
         * controller before MainViewModel attempts to use it.
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
         *
         * Dependency construction remains outside this class.
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

    // -------------------------------------------------------------------------
    // Passenger contract
    // -------------------------------------------------------------------------

    /**
     * Passenger details required by the workflow engine.
     *
     * This is intentionally platform-independent.
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
    // Canonical state
    // -------------------------------------------------------------------------

    private val _state =
        MutableStateFlow(WorkflowState.IDLE)

    /**
     * Read-only state exposed to UI/application layers.
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
    // Session data
    // -------------------------------------------------------------------------

    private var currentSessionId: String = ""

    private var passengerDetails: PassengerDetails? = null

    private var lastSuggestedAction:
        ScreenAnalyzer.SuggestedAction? = null

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Starts a workflow from the application-level booking contract.
     *
     * This is the API used by MainViewModel.
     */
    fun start(
        bookingRequest: BookingRequest,
        passengerProfile: PassengerProfile
    ): Boolean {

        /*
         * PassengerProfile is intentionally accepted as part of the
         * application contract. The workflow currently executes the
         * first passenger from BookingRequest.
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
         * A new session can only start from IDLE.
         *
         * This prevents accidental overlapping workflow sessions.
         */
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

        /*
         * Configuration is accepted first.
         */
        currentState =
            WorkflowState.CONFIGURED

        /*
         * Then the execution engine is allowed to operate.
         */
        currentState =
            WorkflowState.RUNNING

        return true
    }

    /**
     * Stops the current workflow.
     */
    fun stopWorkflow() {

        if (
            currentState == WorkflowState.IDLE ||
            currentState == WorkflowState.STOPPED
        ) {
            return
        }

        currentState =
            WorkflowState.STOPPED

        lastSuggestedAction = null
    }

    /**
     * Resets the controller after a finished/stopped/error session.
     *
     * This does NOT automatically restart anything.
     */
    fun reset(): Boolean {

        if (isActive()) {
            return false
        }

        currentSessionId = ""
        passengerDetails = null
        lastSuggestedAction = null

        currentState =
            WorkflowState.IDLE

        return true
    }

    // -------------------------------------------------------------------------
    // State API
    // -------------------------------------------------------------------------

    /**
     * Returns current canonical workflow state.
     */
    fun getCurrentState(): WorkflowState =
        currentState

    /**
     * Returns the current session ID.
     */
    fun getSessionId(): String =
        currentSessionId

    /**
     * Returns true when the controller is allowed to process
     * workflow/screen events.
     */
    fun isActive(): Boolean {

        return when (currentState) {

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
     * Updates state through the canonical state contract.
     */
    fun updateState(
        newState: WorkflowState
    ) {

        /*
         * USER_BOUNDARY always clears the pending suggested action.
         * This prevents automatic continuation.
         */
        if (newState == WorkflowState.USER_BOUNDARY) {
            lastSuggestedAction = null
        }

        /*
         * STOPPED and ERROR also clear pending actions.
         */
        if (
            newState == WorkflowState.STOPPED ||
            newState == WorkflowState.ERROR
        ) {
            lastSuggestedAction = null
        }

        currentState =
            newState
    }

    // -------------------------------------------------------------------------
    // Screen analysis
    // -------------------------------------------------------------------------

    /**
     * Converts screen analysis into a platform-independent action.
     *
     * Returns null when:
     * - workflow is inactive,
     * - session is invalid,
     * - security boundary is detected,
     * - no safe action is available,
     * - the same suggested action is already pending.
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

        if (
            classifier.isSensitiveScreen(
                ocrResult
            )
        ) {

            enterUserBoundary(
                reason = "Sensitive screen detected"
            )

            return null
        }

        /*
         * Explicit boundary states always block further automation.
         */
        if (
            currentState == WorkflowState.USER_BOUNDARY ||
            currentState == WorkflowState.STOPPED ||
            currentState == WorkflowState.ERROR
        ) {
            return null
        }

        val suggestedAction =
            analysis.suggestedAction

        /*
         * NONE is never executed.
         */
        if (
            suggestedAction ==
            ScreenAnalyzer.SuggestedAction.NONE
        ) {
            return null
        }

        /*
         * Prevent repeatedly generating the same action
         * while the screen has not changed.
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
                    handleTrainSelection(
                        analysis
                    )

                ScreenAnalyzer.SuggestedAction.SELECT_CLASS ->
                    handleClassSelection(
                        analysis
                    )

                ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_NAME ->
                    handlePassengerName(
                        analysis
                    )

                ScreenAnalyzer.SuggestedAction.FILL_PASSENGER_AGE ->
                    handlePassengerAgeFromAnalysis(
                        analysis
                    )

                ScreenAnalyzer.SuggestedAction.ADD_PASSENGER ->
                    handleAddPassenger(
                        analysis
                    )

                ScreenAnalyzer.SuggestedAction.REVIEW_JOURNEY ->
                    handleReviewAndProceed(
                        analysis
                    )

                else ->
                    null
            }

        /*
         * Only remember an action if an actual WorkflowAction
         * was generated.
         *
         * This is important:
         * failed target discovery must NOT permanently suppress
         * the next attempt.
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
         * Do not allow a caller to execute a state-driven action
         * after the controller has already entered a hard boundary.
         */
        if (
            currentState == WorkflowState.USER_BOUNDARY ||
            currentState == WorkflowState.STOPPED ||
            currentState == WorkflowState.ERROR
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

                if (meal.isBlank()) {
                    currentState =
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

                currentState =
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
     * Automation stops here and no further WorkflowAction is generated.
     */
    private fun enterUserBoundary(
        reason: String
    ) {

        if (
            currentState ==
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

        currentState =
            WorkflowState.USER_BOUNDARY
    }

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------

    private fun isValidPassengerDetails(
        details: PassengerDetails
    ): Boolean {

        if (details.from.isBlank()) {
            return false
        }

        if (details.to.isBlank()) {
            return false
        }

        if (details.date.isBlank()) {
            return false
        }

        if (details.train.isBlank()) {
            return false
        }

        if (details.trainClass.isBlank()) {
            return false
        }

        if (details.name.isBlank()) {
            return false
        }

        if (details.age.isBlank()) {
            return false
        }

        if (details.gender.isBlank()) {
            return false
        }

        return true
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
         * exact/contains train number match.
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
         * common train-selection actions.
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
            passengerDetails
                ?: return null

        val target =
            findEditableByLabel(
                uiElements,
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

        currentState =
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
         * the user has not configured a meal preference.
         */
        if (details.meal.isBlank()) {

            currentState =
                WorkflowState.PASSENGER_MEAL_SELECTED

            return null
        }

        val target =
            findClickableByLabel(
                uiElements,
                "Meal"
            )
            ?: return null

        currentState =
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
         * The controller may produce the boundary action,
         * but it must NOT continue beyond it automatically.
         */
        currentState =
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
     * Finds an editable UI element using hint/text/contentDescription.
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
     * Finds a clickable UI element using hint/text/contentDescription.
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
     * The platform action executor decides how they are used.
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
 * The controller never executes these actions directly.
 * The execution layer is responsible for actually performing them.
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
