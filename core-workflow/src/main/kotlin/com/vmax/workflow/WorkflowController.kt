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
 * Responsibility:
 * - Own canonical WorkflowState.
 * - Own workflow session identity.
 * - Validate workflow configuration.
 * - Consume screen-intelligence results.
 * - Enforce security/user boundaries.
 * - Coordinate metrics/history.
 * - Produce platform-independent WorkflowAction intents.
 *
 * IMPORTANT:
 * This class NEVER executes Android UI operations.
 *
 * Runtime layer is responsible for execution.
 *
 * SECURITY BOUNDARIES:
 * - Sensitive screens
 * - CAPTCHA
 * - OTP
 * - Payment
 * - Financial confirmation
 * - Completion
 * - Critical errors
 */
class WorkflowController(
    @Suppress("UNUSED_PARAMETER")
    private val analyzer: ScreenAnalyzer,
    private val classifier: TextClassifier,
    private val metrics: MetricsCollector,
    private val recorder: ExecutionRecorder
) {

    // ========================================================================
    // SINGLETON
    // ========================================================================

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
        fun getInstanceOrNull(): WorkflowController? {
            return instance
        }

        @JvmStatic
        fun initialize(
            controller: WorkflowController
        ) {
            requireNotNull(controller) {
                "WorkflowController cannot be null."
            }

            synchronized(this) {
                if (instance == null) {
                    instance = controller
                }
            }
        }

        @JvmStatic
        fun replaceInstance(
            controller: WorkflowController
        ) {
            requireNotNull(controller) {
                "WorkflowController cannot be null."
            }

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

    // ========================================================================
    // PASSENGER CONTRACT
    // ========================================================================

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

    // ========================================================================
    // STATE
    // ========================================================================

    private val _state =
        MutableStateFlow(
            WorkflowState.IDLE
        )

    val state: StateFlow<WorkflowState> =
        _state.asStateFlow()

    private var workflowState: WorkflowState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    // ========================================================================
    // SESSION
    // ========================================================================

    @Volatile
    private var currentSessionId: String = ""

    @Volatile
    private var passengerDetails: PassengerDetails? = null

    /**
     * Last successfully produced analyzer suggestion.
     *
     * This is deliberately NOT updated when an action could
     * not be produced.
     */
    @Volatile
    private var lastProducedSuggestion:
        ScreenAnalyzer.SuggestedAction? = null

    // ========================================================================
    // LIFECYCLE LOCK
    // ========================================================================

    private val lifecycleLock =
        Any()

    // ========================================================================
    // START
    // ========================================================================

    /**
     * Starts workflow from model contracts.
     *
     * No Android UI operation occurs here.
     */
    fun start(
        bookingRequest: BookingRequest,
        passengerProfile: PassengerProfile
    ): Boolean {

        synchronized(lifecycleLock) {

            if (workflowState != WorkflowState.IDLE) {
                return false
            }

            /*
             * Validate profile contract.
             */
            if (passengerProfile.passengers.isEmpty()) {
                return false
            }

            /*
             * Use the first passenger from the request.
             *
             * The complete multi-passenger orchestration belongs
             * outside this controller.
             */
            val passenger =
                bookingRequest.passengers.firstOrNull()
                    ?: return false

            val details =
                try {

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

                } catch (_: Exception) {

                    return false
                }

            return startWorkflowLocked(
                details = details,
                sessionId = UUID.randomUUID().toString()
            )
        }
    }

    // ========================================================================
    // PUBLIC STOP
    // ========================================================================

    fun stop() {
        stopWorkflow()
    }

    // ========================================================================
    // EXPLICIT START
    // ========================================================================

    fun startWorkflow(
        details: PassengerDetails,
        sessionId: String
    ): Boolean {

        synchronized(lifecycleLock) {
            return startWorkflowLocked(
                details = details,
                sessionId = sessionId
            )
        }
    }

    private fun startWorkflowLocked(
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

        val normalizedDetails =
            normalizePassengerDetails(
                details
            )

        if (!isValidPassengerDetails(normalizedDetails)) {
            return false
        }

        passengerDetails =
            normalizedDetails

        currentSessionId =
            normalizedSessionId

        lastProducedSuggestion =
            null

        /*
         * Enter configuration state first.
         */
        workflowState =
            WorkflowState.CONFIGURED

        /*
         * Metrics/history are observational.
         * Their failures must not crash startup.
         */
        startMetricsSafely(
            normalizedSessionId
        )

        recordSessionStartedSafely(
            normalizedSessionId
        )

        /*
         * Session is now active.
         */
        workflowState =
            WorkflowState.RUNNING

        return true
    }

    // ========================================================================
    // STOP
    // ========================================================================

    fun stopWorkflow() {

        synchronized(lifecycleLock) {

            if (
                workflowState == WorkflowState.IDLE ||
                workflowState == WorkflowState.STOPPED
            ) {
                return
            }

            val sessionId =
                currentSessionId

            lastProducedSuggestion =
                null

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
    }

    // ========================================================================
    // RESET
    // ========================================================================

    /**
     * Resets only an inactive workflow.
     *
     * Active workflow cannot be silently destroyed.
     */
    fun reset(): Boolean {

        synchronized(lifecycleLock) {

            if (isActive()) {
                return false
            }

            currentSessionId =
                ""

            passengerDetails =
                null

            lastProducedSuggestion =
                null

            workflowState =
                WorkflowState.IDLE

            return true
        }
    }

    // ========================================================================
    // STATE API
    // ========================================================================

    fun getCurrentState(): WorkflowState {
        return workflowState
    }

    fun getSessionId(): String {
        return currentSessionId
    }

    /**
     * Returns true only while workflow may continue.
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
            WorkflowState.PASSENGER_MEAL_SELECTED ->
                true

            WorkflowState.IDLE,
            WorkflowState.USER_BOUNDARY,
            WorkflowState.STOPPED,
            WorkflowState.ERROR ->
                false
        }
    }

    // ========================================================================
    // STATE UPDATE
    // ========================================================================

    /**
     * Updates canonical state.
     *
     * Terminal/security states invalidate any pending
     * analyzer suggestion.
     */
    fun updateState(
        newState: WorkflowState
    ) {

        synchronized(lifecycleLock) {

            if (
                newState == WorkflowState.USER_BOUNDARY ||
                newState == WorkflowState.STOPPED ||
                newState == WorkflowState.ERROR
            ) {
                lastProducedSuggestion =
                    null
            }

            workflowState =
                newState
        }
    }

    // ========================================================================
    // SCREEN ANALYSIS
    // ========================================================================

    /**
     * Consumes screen analysis and produces a platform-independent intent.
     *
     * This method NEVER performs Android UI operations.
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

        val safeOcrText =
            ocrText.trim()

        val safeOcrBlocks =
            ocrBlocks.toList()

        val ocrResult =
            try {

                OcrResult(
                    screenId = sessionId,
                    timestamp = System.currentTimeMillis(),
                    fullText = safeOcrText,
                    textBlocks = safeOcrBlocks
                )

            } catch (_: Exception) {

                return null
            }

        // ====================================================================
        // SECURITY CLASSIFICATION
        // ====================================================================

        val sensitive =
            try {

                classifier.isSensitiveScreen(
                    ocrResult
                )

            } catch (_: Exception) {

                enterUserBoundary(
                    "Screen classification failed"
                )

                return null
            }

        if (sensitive) {

            enterUserBoundary(
                "Sensitive screen detected"
            )

            return null
        }

        if (isHardBoundary()) {
            return null
        }

        // ====================================================================
        // ANALYZER RESULT
        // ====================================================================

        val suggestedAction =
            try {

                analysis.suggestedAction

            } catch (_: Exception) {

                return null
            }

        if (
            suggestedAction ==
            ScreenAnalyzer.SuggestedAction.NONE
        ) {
            return null
        }

        /*
         * Suppress the same suggestion only after a previous
         * action was actually produced.
         */
        if (
            suggestedAction ==
            lastProducedSuggestion
        ) {
            return null
        }

        val action =
            try {

                createSafeAction(
                    analysis = analysis,
                    suggestedAction = suggestedAction
                )

            } catch (_: Exception) {

                null
            }

        /*
         * IMPORTANT:
         *
         * A suggestion is remembered only when it resulted
         * in a real WorkflowAction.
         */
        if (action != null) {

            lastProducedSuggestion =
                suggestedAction
        }

        return action
    }

    // ========================================================================
    // SAFE ACTION CONVERSION
    // ========================================================================

    /**
     * Converts analyzer output into a platform-independent intent.
     *
     * IMPORTANT:
     * No Android operation belongs here.
     *
     * The exact SuggestedAction -> WorkflowAction mapping
     * must be added only after ScreenAnalyzer's exact contract
     * is verified.
     *
     * Returning null for unsupported suggestions is intentional.
     */
    private fun createSafeAction(
        analysis: ScreenAnalyzer.AnalysisResult,
        suggestedAction: ScreenAnalyzer.SuggestedAction
    ): WorkflowAction? {

        /*
         * Keep the parameter contract explicit so this method
         * can later consume exact analyzer evidence without
         * inventing fields that may not exist.
         */
        @Suppress("UNUSED_VARIABLE")
        val analyzedResult =
            analysis

        return when (suggestedAction) {

            ScreenAnalyzer.SuggestedAction.NONE ->
                null

            else ->
                null
        }
    }

    // ========================================================================
    // STATE-DRIVEN PROCESSING
    // ========================================================================

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

        val elements =
            uiElements.toList()

        /*
         * Normally caller state must match canonical state.
         *
         * Dropdown states are allowed because the visible UI
         * can remain in a transient selection state.
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
                        .trim()

                if (gender.isBlank()) {
                    null
                } else {

                    selectDropdownOption(
                        uiElements = elements,
                        targetText = gender
                    )
                }
            }

            WorkflowState.MEAL_DROPDOWN_OPENED -> {

                val meal =
                    passengerDetails
                        ?.meal
                        .orEmpty()
                        .trim()

                if (meal.isBlank()) {

                    workflowState =
                        WorkflowState.PASSENGER_MEAL_SELECTED

                    lastProducedSuggestion =
                        null

                    null

                } else {

                    selectDropdownOption(
                        uiElements = elements,
                        targetText = meal
                    )
                }
            }

            WorkflowState.PASSENGER_MEAL_SELECTED -> {

                lastProducedSuggestion =
                    null

                workflowState =
                    WorkflowState.RUNNING

                null
            }

            else ->
                null
        }
    }

    // ========================================================================
    // DROPDOWN
    // ========================================================================

    private fun selectDropdownOption(
        uiElements: List<UIElement>,
        targetText: String
    ): WorkflowAction? {

        val normalizedTarget =
            normalize(
                targetText
            )

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
                    text.contains(normalizedTarget)
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

    // ========================================================================
    // SECURITY BOUNDARY
    // ========================================================================

    private fun enterUserBoundary(
        reason: String
    ) {

        synchronized(lifecycleLock) {

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
                            sessionId = sessionId,
                            errorCode = "SECURITY_BOUNDARY",
                            errorMessage = reason
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

            lastProducedSuggestion =
                null

            workflowState =
                WorkflowState.USER_BOUNDARY
        }
    }

    // ========================================================================
    // VALIDATION
    // ========================================================================

    private fun normalizePassengerDetails(
        details: PassengerDetails
    ): PassengerDetails {

        return details.copy(

            from =
                details.from.trim(),

            to =
                details.to.trim(),

            date =
                details.date.trim(),

            train =
                details.train.trim(),

            trainClass =
                details.trainClass.trim(),

            name =
                details.name.trim(),

            age =
                details.age.trim(),

            gender =
                details.gender.trim(),

            meal =
                details.meal.trim()
        )
    }

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

    // ========================================================================
    // HARD BOUNDARY
    // ========================================================================

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

    // ========================================================================
    // UI HELPERS
    // ========================================================================

    @Suppress("UNUSED")
    private fun findEditableByLabel(
        elements: List<UIElement>,
        label: String
    ): UIElement? {

        val normalizedLabel =
            normalize(label)

        if (normalizedLabel.isBlank()) {
            return null
        }

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

    @Suppress("UNUSED")
    private fun findClickableByLabel(
        elements: List<UIElement>,
        label: String
    ): UIElement? {

        val normalizedLabel =
            normalize(label)

        if (normalizedLabel.isBlank()) {
            return null
        }

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

    // ========================================================================
    // ACTION BUILDERS
    // ========================================================================

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

    @Suppress("UNUSED")
    private fun buildSetTextAction(
        element: UIElement,
        text: String
    ): WorkflowAction.SetText? {

        val safeText =
            text.trim()

        if (safeText.isBlank()) {
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
            text = safeText
        )
    }

    // ========================================================================
    // COORDINATES
    // ========================================================================

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

    // ========================================================================
    // NORMALIZATION
    // ========================================================================

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

    // ========================================================================
    // METRICS
    // ========================================================================

    private fun startMetricsSafely(
        sessionId: String
    ) {

        if (sessionId.isBlank()) {
            return
        }

        try {

            metrics.startMetrics(
                sessionId
            )

        } catch (_: Exception) {
            /*
             * Metrics are observational.
             */
        }
    }

    private fun stopMetricsSafely(
        sessionId: String,
        reason: String
    ) {

        if (sessionId.isBlank()) {
            return
        }

        try {

            metrics.stopMetrics(
                sessionId,
                reason
            )

        } catch (_: Exception) {
            /*
             * Metrics must never break state safety.
             */
        }
    }

    // ========================================================================
    // EXECUTION HISTORY
    // ========================================================================

    private fun recordSessionStartedSafely(
        sessionId: String
    ) {

        if (sessionId.isBlank()) {
            return
        }

        try {

            recorder.recordEvent(
                ExecutionEvent.SessionStarted(
                    sessionId
                )
            )

        } catch (_: Exception) {
            /*
             * History failure must not prevent startup.
             */
        }
    }

    private fun recordSessionStoppedSafely(
        sessionId: String
    ) {

        if (sessionId.isBlank()) {
            return
        }

        try {

            recorder.recordEvent(
                ExecutionEvent.SessionStopped(
                    sessionId
                )
            )

        } catch (_: Exception) {
            /*
             * History failure must not prevent shutdown.
             */
        }
    }
}

/**
 * Platform-independent workflow action.
 *
 * This class only represents an intent.
 * It does not execute Android operations.
 */
sealed class WorkflowAction {

    /**
     * UI click intent.
     *
     * Runtime decides how/if this intent is executed.
     */
    data class Click(
        val targetId: String? = null,
        val coordinates: Pair<Int, Int>? = null
    ) : WorkflowAction()

    /**
     * Text-setting intent.
     *
     * Concrete target ID is mandatory.
     */
    data class SetText(
        val targetId: String,
        val text: String
    ) : WorkflowAction()
}
