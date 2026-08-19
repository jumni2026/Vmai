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
 * - Own canonical WorkflowState.
 * - Own workflow session identity.
 * - Validate workflow configuration.
 * - Process screen intelligence results.
 * - Enforce explicit user/security boundaries.
 * - Coordinate metrics and execution history.
 * - Produce platform-independent WorkflowAction objects.
 *
 * IMPORTANT:
 * This class never directly executes Android/UI operations.
 *
 * Runtime/execution layers are responsible for actual platform
 * interaction.
 *
 * This controller also stops at explicit user/security boundaries.
 */
class WorkflowController(
    @Suppress("UNUSED_PARAMETER")
    private val analyzer: ScreenAnalyzer,
    private val classifier: TextClassifier,
    private val metrics: MetricsCollector,
    private val recorder: ExecutionRecorder
) {

    // =========================================================================
    // SINGLETON
    // =========================================================================

    companion object {

        @Volatile
        private var instance: WorkflowController? = null

        @JvmStatic
        fun getInstance(): WorkflowController {
            return instance
                ?: throw IllegalStateException(
                    "WorkflowController has not been initialized."
                )
        }

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

        @JvmStatic
        fun replaceInstance(
            controller: WorkflowController
        ) {
            synchronized(this) {
                instance = controller
            }
        }

        @JvmStatic
        fun clearInstance() {
            synchronized(this) {
                instance = null
            }
        }
    }

    // =========================================================================
    // PASSENGER CONTRACT
    // =========================================================================

    data class PassengerDetails(
        val from: String,
        val to: String,
        val date: String,
        val train: String,
        val trainClass: String,
        val name: String,
        val age: String,
        val gender: String,
        val meal: String = ""
    )

    // =========================================================================
    // CANONICAL STATE
    // =========================================================================

    private val _state =
        MutableStateFlow(
            WorkflowState.IDLE
        )

    val state: StateFlow<WorkflowState> =
        _state.asStateFlow()

    /**
     * Do not rename to currentState.
     *
     * getCurrentState() is explicitly retained for Java/UI
     * compatibility and avoiding JVM getter collision.
     */
    private var workflowState: WorkflowState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    // =========================================================================
    // SESSION
    // =========================================================================

    private var currentSessionId: String = ""

    private var passengerDetails: PassengerDetails? = null

    /**
     * Tracks only an action that was actually produced.
     *
     * Failed target resolution must never update this value.
     */
    private var lastSuggestedAction:
        ScreenAnalyzer.SuggestedAction? = null

    // =========================================================================
    // LIFECYCLE
    // =========================================================================

    /**
     * Starts workflow from application-level contract.
     */
    fun start(
        bookingRequest: BookingRequest,
        passengerProfile: PassengerProfile
    ): Boolean {

        if (workflowState != WorkflowState.IDLE) {
            return false
        }

        if (passengerProfile.passengers.isEmpty()) {
            return false
        }

        val passenger =
            bookingRequest.passengers.firstOrNull()
                ?: return false

        val details =
            PassengerDetails(
                from =
                    bookingRequest
                        .fromStation
                        .code
                        .trim(),

                to =
                    bookingRequest
                        .toStation
                        .code
                        .trim(),

                date =
                    bookingRequest
                        .date
                        .trim(),

                train =
                    bookingRequest
                        .train
                        .number
                        .trim(),

                trainClass =
                    bookingRequest
                        .train
                        .classType
                        .trim(),

                name =
                    passenger
                        .name
                        .trim(),

                age =
                    passenger
                        .age
                        .toString()
                        .trim(),

                gender =
                    passenger
                        .gender
                        .trim(),

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
     * Public stop API.
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

        if (workflowState != WorkflowState.IDLE) {
            return false
        }

        val normalizedSessionId =
            sessionId.trim()

        if (normalizedSessionId.isBlank()) {
            return false
        }

        if (!isValidPassengerDetails(details)) {
            return false
        }

        passengerDetails =
            details.copy(
                from = details.from.trim(),
                to = details.to.trim(),
                date = details.date.trim(),
                train = details.train.trim(),
                trainClass = details.trainClass.trim(),
                name = details.name.trim(),
                age = details.age.trim(),
                gender = details.gender.trim(),
                meal = details.meal.trim()
            )

        currentSessionId =
            normalizedSessionId

        lastSuggestedAction = null

        workflowState =
            WorkflowState.CONFIGURED

        startMetricsSafely(
            normalizedSessionId
        )

        recordSessionStartedSafely(
            normalizedSessionId
        )

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

        val sessionId =
            currentSessionId

        lastSuggestedAction = null

        if (sessionId.isNotBlank()) {

            recordSessionStoppedSafely(
                sessionId
            )

            stopMetricsSafely(
                sessionId,
                "STOPPED"
            )
        }

        workflowState =
            WorkflowState.STOPPED
    }

    /**
     * Resets a completed, stopped, error, or user-boundary session.
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

    // =========================================================================
    // STATE API
    // =========================================================================

    fun getCurrentState(): WorkflowState =
        workflowState

    fun getSessionId(): String =
        currentSessionId

    /**
     * Returns true only for states where workflow processing is allowed.
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
     * Updates canonical workflow state.
     */
    fun updateState(
        newState: WorkflowState
    ) {

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

    // =========================================================================
    // SCREEN ANALYSIS
    // =========================================================================

    /**
     * Converts screen intelligence into a platform-independent action.
     *
     * No Android UI operation is performed here.
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

        /*
         * IMPORTANT:
         *
         * OcrResult now uses:
         * - screenId
         * - fullText
         * - textBlocks
         *
         * The old constructor names:
         * - sessionId
         * - timestamp
         * - text
         * - blocks
         *
         * must not be used.
         */
        val ocrResult =
            OcrResult(
                screenId = sessionId,
                fullText = ocrText,
                textBlocks = ocrBlocks
            )

        // ---------------------------------------------------------------------
        // SECURITY BOUNDARY
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

        if (isHardBoundary()) {
            return null
        }

        val suggestedAction =
            analysis.suggestedAction

        if (
            suggestedAction ==
            ScreenAnalyzer.SuggestedAction.NONE
        ) {
            return null
        }

        /*
         * Prevent identical action generation.
         */
        if (
            suggestedAction ==
            lastSuggestedAction
        ) {
            return null
        }

        /*
         * Only safe, non-transactional action conversion
         * is permitted here.
         */
        val action =
            createSafeAction(
                analysis = analysis,
                suggestedAction = suggestedAction
            )

        /*
         * Only remember a suggestion after an action
         * has actually been produced.
         */
        if (action != null) {

            lastSuggestedAction =
                suggestedAction
        }

        return action
    }

    // =========================================================================
    // SAFE ACTION CONVERSION
    // =========================================================================

    /**
     * Converts intelligence into a platform-independent action.
     *
     * Transactional/booking/payment execution is deliberately not
     * performed by this controller.
     */
    private fun createSafeAction(
        analysis: ScreenAnalyzer.AnalysisResult,
        suggestedAction:
            ScreenAnalyzer.SuggestedAction
    ): WorkflowAction? {

        /*
         * Keep the controller conservative.
         *
         * The actual platform execution layer must decide whether
         * and how an action may be executed.
         */
        return when (suggestedAction) {

            ScreenAnalyzer.SuggestedAction.NONE ->
                null

            else ->
                null
        }
    }

    // =========================================================================
    // STATE-DRIVEN PROCESSING
    // =========================================================================

    fun handleStateAction(
        state: WorkflowState,
        uiElements: List<UIElement>
    ): WorkflowAction? {

        if (isHardBoundary()) {
            return null
        }

        if (uiElements.isEmpty()) {
            return null
        }

        /*
         * Do not process a state that is different from the
         * controller's canonical state unless it is a harmless
         * externally-observed snapshot.
         */
        if (
            state != workflowState &&
            state !in setOf(
                WorkflowState.GENDER_DROPDOWN_OPENED,
                WorkflowState.MEAL_DROPDOWN_OPENED,
                WorkflowState.PASSENGER_MEAL_SELECTED
            )
        ) {
            return null
        }

        return when (state) {

            WorkflowState.GENDER_DROPDOWN_OPENED -> {

                val gender =
                    passengerDetails
                        ?.gender
                        .orEmpty()

                if (gender.isBlank()) {
                    null
                } else {
                    selectDropdownOption(
                        uiElements = uiElements,
                        targetText = gender
                    )
                }
            }

            WorkflowState.MEAL_DROPDOWN_OPENED -> {

                val meal =
                    passengerDetails
                        ?.meal
                        .orEmpty()

                if (meal.isBlank()) {

                    workflowState =
                        WorkflowState.PASSENGER_MEAL_SELECTED

                    lastSuggestedAction = null

                    null

                } else {

                    selectDropdownOption(
                        uiElements = uiElements,
                        targetText = meal
                    )
                }
            }

            WorkflowState.PASSENGER_MEAL_SELECTED -> {

                lastSuggestedAction = null

                workflowState =
                    WorkflowState.RUNNING

                null
            }

            else ->
                null
        }
    }

    // =========================================================================
    // DROPDOWN
    // =========================================================================

    private fun selectDropdownOption(
        uiElements: List<UIElement>,
        targetText: String
    ): WorkflowAction? {

        if (targetText.isBlank()) {
            return null
        }

        val normalizedTarget =
            normalize(targetText)

        if (normalizedTarget.isBlank()) {
            return null
        }

        val target =
            uiElements.firstOrNull { element ->

                if (!element.isClickable) {
                    return@firstOrNull false
                }

                val text =
                    normalize(
                        element.text
                    )

                text == normalizedTarget ||
                    text.contains(
                        normalizedTarget
                    )
            }
                ?: return null

        val action =
            buildClickAction(
                target
            )
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

        return action
    }

    // =========================================================================
    // SECURITY BOUNDARY
    // =========================================================================

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

            try {

                recorder.recordEvent(
                    ExecutionEvent.SessionError(
                        sessionId,
                        "SECURITY_BOUNDARY",
                        reason
                    )
                )

            } catch (_: Exception) {
                /*
                 * History failure must never bypass
                 * the security boundary.
                 */
            }

            stopMetricsSafely(
                sessionId,
                "USER_BOUNDARY"
            )
        }

        lastSuggestedAction = null

        workflowState =
            WorkflowState.USER_BOUNDARY
    }

    // =========================================================================
    // VALIDATION
    // =========================================================================

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

    // =========================================================================
    // BOUNDARY
    // =========================================================================

    private fun isHardBoundary(): Boolean {

        return when (workflowState) {

            WorkflowState.USER_BOUNDARY,
            WorkflowState.STOPPED,
            WorkflowState.ERROR ->
                true

            else ->
                false
        }
    }

    // =========================================================================
    // UI HELPERS
    // =========================================================================

    private fun findEditableByLabel(
        elements: List<UIElement>,
        label: String
    ): UIElement? {

        if (label.isBlank()) {
            return null
        }

        val normalizedLabel =
            normalize(label)

        return elements.firstOrNull { element ->

            if (!element.isEditable) {
                return@firstOrNull false
            }

            val hint =
                normalize(
                    element.hint.orEmpty()
                )

            val text =
                normalize(
                    element.text
                )

            val description =
                normalize(
                    element.contentDescription.orEmpty()
                )

            hint.contains(normalizedLabel) ||
                text.contains(normalizedLabel) ||
                description.contains(normalizedLabel)
        }
    }

    private fun findClickableByLabel(
        elements: List<UIElement>,
        label: String
    ): UIElement? {

        if (label.isBlank()) {
            return null
        }

        val normalizedLabel =
            normalize(label)

        return elements.firstOrNull { element ->

            if (!element.isClickable) {
                return@firstOrNull false
            }

            val hint =
                normalize(
                    element.hint.orEmpty()
                )

            val text =
                normalize(
                    element.text
                )

            val description =
                normalize(
                    element.contentDescription.orEmpty()
                )

            hint.contains(normalizedLabel) ||
                text.contains(normalizedLabel) ||
                description.contains(normalizedLabel)
        }
    }

    private fun buildClickAction(
        element: UIElement
    ): WorkflowAction.Click? {

        val targetId =
            element.id
                .trim()
                .takeIf {
                    it.isNotEmpty()
                }

        val coordinates =
            getCoordinates(element)

        if (
            targetId == null &&
            coordinates == null
        ) {
            return null
        }

        return WorkflowAction.Click(
            targetId = targetId,
            coordinates = coordinates
        )
    }

    private fun buildSetTextAction(
        element: UIElement,
        text: String
    ): WorkflowAction.SetText? {

        if (text.isBlank()) {
            return null
        }

        val targetId =
            element.id
                .trim()
                .takeIf {
                    it.isNotEmpty()
                }
                ?: return null

        return WorkflowAction.SetText(
            targetId = targetId,
            text = text
        )
    }

    private fun getCoordinates(
        element: UIElement
    ): Pair<Int, Int>? {

        val bounds =
            element.bounds
                ?: return null

        val width =
            bounds.right - bounds.left

        val height =
            bounds.bottom - bounds.top

        if (
            width <= 0 ||
            height <= 0
        ) {
            return null
        }

        val x =
            bounds.left +
                width / 2

        val y =
            bounds.top +
                height / 2

        if (
            x < 0 ||
            y < 0
        ) {
            return null
        }

        return Pair(
            x,
            y
        )
    }

    private fun normalize(
        value: String
    ): String {

        return value
            .trim()
            .replace(
                Regex("\\s+"),
                " "
            )
            .lowercase()
    }

    // =========================================================================
    // METRICS
    // =========================================================================

    private fun startMetricsSafely(
        sessionId: String
    ) {

        try {

            metrics.startMetrics(
                sessionId
            )

        } catch (_: Exception) {
            /*
             * Metrics are observational.
             * They must never block workflow startup.
             */
        }
    }

    private fun stopMetricsSafely(
        sessionId: String,
        reason: String
    ) {

        try {

            metrics.stopMetrics(
                sessionId,
                reason
            )

        } catch (_: Exception) {
            /*
             * Metrics failure must never break
             * state safety.
             */
        }
    }

    // =========================================================================
    // EXECUTION HISTORY
    // =========================================================================

    private fun recordSessionStartedSafely(
        sessionId: String
    ) {

        try {

            recorder.recordEvent(
                ExecutionEvent.SessionStarted(
                    sessionId
                )
            )

        } catch (_: Exception) {
            /*
             * History failure must not prevent workflow startup.
             */
        }
    }

    private fun recordSessionStoppedSafely(
        sessionId: String
    ) {

        try {

            recorder.recordEvent(
                ExecutionEvent.SessionStopped(
                    sessionId
                )
            )

        } catch (_: Exception) {
            /*
             * History failure must not prevent safe shutdown.
             */
        }
    }
}

/**
 * Platform-independent workflow action.
 *
 * This class never performs execution itself.
 */
sealed class WorkflowAction {

    /**
     * UI click intent.
     */
    data class Click(
        val targetId: String? = null,
        val coordinates: Pair<Int, Int>? = null
    ) : WorkflowAction()

    /**
     * Text-setting intent.
     *
     * targetId is mandatory because text insertion without
     * a concrete target is unsafe.
     */
    data class SetText(
        val targetId: String,
        val text: String
    ) : WorkflowAction()
}
