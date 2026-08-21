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
 * - Enforce user/security boundaries.
 * - Coordinate metrics/history.
 * - Produce platform-independent WorkflowAction intents.
 *
 * IMPORTANT:
 * This class NEVER directly executes Android UI operations.
 *
 * Runtime layer is responsible for execution.
 *
 * HARD USER / SECURITY BOUNDARIES:
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

        /**
         * Strict accessor.
         *
         * Use only when initialization is guaranteed.
         */
        @JvmStatic
        fun getInstance(): WorkflowController {

            return instance
                ?: throw IllegalStateException(
                    "WorkflowController has not been initialized."
                )
        }

        /**
         * Safe accessor.
         *
         * Returns null instead of crashing when controller
         * has not yet been initialized.
         */
        @JvmStatic
        fun getInstanceOrNull(): WorkflowController? {
            return instance
        }

        /**
         * Initializes controller exactly once.
         *
         * This method intentionally rejects accidental
         * double initialization.
         */
        @JvmStatic
        fun initialize(
            controller: WorkflowController
        ) {

            requireNotNull(controller) {
                "WorkflowController cannot be null."
            }

            synchronized(this) {

                if (instance != null) {
                    return
                }

                instance = controller
            }
        }

        /**
         * Explicit replacement.
         *
         * Intended for controlled application/test setup.
         */
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

        /**
         * Clears singleton reference.
         */
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

    /**
     * Canonical workflow state.
     */
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
     * Last action that was ACTUALLY produced.
     *
     * It is not updated merely because an analyzer suggested
     * something.
     */
    @Volatile
    private var lastSuggestedAction:
        ScreenAnalyzer.SuggestedAction? = null

    // ========================================================================
    // LIFECYCLE
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

        if (workflowState != WorkflowState.IDLE) {
            return false
        }

        /*
         * Validate passenger profile first.
         */
        if (passengerProfile.passengers.isEmpty()) {
            return false
        }

        /*
         * Use the first requested passenger.
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

            } catch (_: Throwable) {

                /*
                 * Invalid model contract must never crash
                 * the workflow layer.
                 */
                return false
            }

        return startWorkflow(
            details = details,
            sessionId = UUID.randomUUID().toString()
        )
    }

    /**
     * Public stop API.
     */
    fun stop() {
        stopWorkflow()
    }

    /**
     * Starts explicit workflow session.
     */
    fun startWorkflow(
        details: PassengerDetails,
        sessionId: String
    ): Boolean {

        /*
         * Never start a second workflow over an existing one.
         */
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

        val normalizedDetails =
            details.copy(

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

        passengerDetails =
            normalizedDetails

        currentSessionId =
            normalizedSessionId

        lastSuggestedAction =
            null

        /*
         * Configuration phase.
         */
        workflowState =
            WorkflowState.CONFIGURED

        /*
         * Observational systems must never block startup.
         */
        startMetricsSafely(
            normalizedSessionId
        )

        recordSessionStartedSafely(
            normalizedSessionId
        )

        /*
         * Only after bookkeeping succeeds/fails safely,
         * enter RUNNING.
         */
        workflowState =
            WorkflowState.RUNNING

        return true
    }

    /**
     * Stops current workflow safely.
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

        lastSuggestedAction =
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

    /**
     * Resets inactive workflow.
     *
     * Active workflow cannot be silently reset.
     */
    fun reset(): Boolean {

        if (isActive()) {
            return false
        }

        currentSessionId =
            ""

        passengerDetails =
            null

        lastSuggestedAction =
            null

        workflowState =
            WorkflowState.IDLE

        return true
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

    /**
     * Updates canonical state.
     */
    fun updateState(
        newState: WorkflowState
    ) {

        if (
            newState == WorkflowState.USER_BOUNDARY ||
            newState == WorkflowState.STOPPED ||
            newState == WorkflowState.ERROR
        ) {
            lastSuggestedAction =
                null
        }

        workflowState =
            newState
    }

    // ========================================================================
    // SCREEN ANALYSIS
    // ========================================================================

    /**
     * Consumes screen analysis and produces an intent.
     *
     * No Android operation is performed here.
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

            } catch (_: Throwable) {

                /*
                 * Malformed OCR data must not crash workflow.
                 */
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

            } catch (_: Throwable) {

                /*
                 * If security classification fails,
                 * safest behavior is to stop.
                 */
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

        /*
         * Never continue after a boundary.
         */
        if (isHardBoundary()) {
            return null
        }

        val suggestedAction =
            try {

                analysis.suggestedAction

            } catch (_: Throwable) {

                return null
            }

        if (
            suggestedAction ==
            ScreenAnalyzer.SuggestedAction.NONE
        ) {
            return null
        }

        /*
         * Prevent duplicate action generation.
         */
        if (
            suggestedAction ==
            lastSuggestedAction
        ) {
            return null
        }

        val action =
            try {

                createSafeAction(
                    analysis = analysis,
                    suggestedAction = suggestedAction
                )

            } catch (_: Throwable) {

                /*
                 * Never allow analyzer/action conversion
                 * to crash the service.
                 */
                null
            }

        /*
         * IMPORTANT:
         *
         * Only remember an action when an actual action
         * object was produced.
         */
        if (action != null) {

            lastSuggestedAction =
                suggestedAction
        }

        return action
    }

    // ========================================================================
    // SAFE ACTION CONVERSION
    // ========================================================================

    /**
     * Converts intelligence into platform-independent intent.
     *
     * Actual execution is outside this class.
     *
     * Unsupported analyzer suggestions intentionally return null.
     */
    private fun createSafeAction(
        analysis: ScreenAnalyzer.AnalysisResult,
        suggestedAction:
            ScreenAnalyzer.SuggestedAction
    ): WorkflowAction? {

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

                    lastSuggestedAction =
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

                lastSuggestedAction =
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
                    text.contains(normalizedTarget)
            }
                ?: return null

        val action =
            buildClickAction(target)
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

            } catch (_: Throwable) {

                /*
                 * History failure must never bypass
                 * a security boundary.
                 */
            }

            stopMetricsSafely(
                sessionId,
                "USER_BOUNDARY"
            )
        }

        lastSuggestedAction =
            null

        workflowState =
            WorkflowState.USER_BOUNDARY
    }

    // ========================================================================
    // VALIDATION
    // ========================================================================

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

        } catch (_: Throwable) {

            /*
             * Metrics are observational.
             * They must never crash workflow startup.
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

        } catch (_: Throwable) {

            /*
             * Metrics failure must never break state safety.
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

        } catch (_: Throwable) {

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

        } catch (_: Throwable) {

            /*
             * History failure must not prevent safe shutdown.
             */
        }
    }
}

/**
 * Platform-independent workflow action.
 *
 * This class only represents an intent.
 * It does not execute anything.
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
     * Concrete target ID is mandatory.
     */
    data class SetText(
        val targetId: String,
        val text: String
    ) : WorkflowAction()
}
