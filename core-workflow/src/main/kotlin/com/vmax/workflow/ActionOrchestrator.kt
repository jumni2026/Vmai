package com.vmax.workflow

import com.vmax.action.ExecutionEvent
import com.vmax.action.ExecutionRecorder
import com.vmax.action.MetricsCollector
import com.vmax.common.Logger
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * VMAX Enterprise v2.6.1
 *
 * File:
 * ExecutionTracker.kt
 *
 * Responsibility:
 * - Track workflow session lifecycle.
 * - Track dispatched/succeeded/failed actions.
 * - Maintain the execution state used by the workflow layer.
 * - Forward execution events to MetricsCollector and ExecutionRecorder.
 *
 * IMPORTANT:
 * - No Android dependencies.
 * - No action execution.
 * - No retry logic.
 * - No parallel execution.
 * - No SLF4J.
 * - No duplicate ExecutionEvent.
 * - No duplicate MetricsCollector.
 * - No duplicate ExecutionRecorder.
 * - Does not independently decide workflow recovery.
 */
class ExecutionTracker(
    private val metricsCollector: MetricsCollector,
    private val executionRecorder: ExecutionRecorder,
    private val logger: Logger
) {

    // ========================================================================
    // STATE
    // ========================================================================

    private val stateMutex = Mutex()

    @Volatile
    private var currentState: WorkflowState =
        WorkflowState.IDLE

    @Volatile
    private var currentSessionId: String? =
        null

    /**
     * Current canonical workflow state.
     */
    val currentWorkflowState: WorkflowState
        get() = currentState

    /**
     * Current active session ID, if any.
     */
    val sessionId: String?
        get() = currentSessionId

    // ========================================================================
    // SESSION LIFECYCLE
    // ========================================================================

    /**
     * Starts a workflow execution session.
     *
     * This method only records lifecycle information.
     * It does not execute any UI action.
     */
    fun startSession(
        sessionId: String
    ) {

        val normalizedSessionId =
            sessionId.trim()

        if (normalizedSessionId.isBlank()) {
            logger.w(
                "Session start ignored: blank sessionId."
            )
            return
        }

        synchronized(this) {

            if (
                currentSessionId != null &&
                currentState != WorkflowState.IDLE
            ) {
                logger.w(
                    "Session start ignored. " +
                        "Active session already exists: " +
                        currentSessionId
                )
                return
            }

            currentSessionId =
                normalizedSessionId

            currentState =
                WorkflowState.RUNNING
        }

        val event =
            ExecutionEvent.SessionStarted(
                normalizedSessionId
            )

        recordEventSafely(
            event
        )

        updateMetricsStateSafely(
            WorkflowState.RUNNING
        )

        logger.i(
            "Session started: $normalizedSessionId"
        )
    }

    /**
     * Stops the currently active session.
     *
     * The supplied final state is preserved instead of
     * guessing success/failure from a reason string.
     */
    fun stopSession(
        finalState: WorkflowState = WorkflowState.STOPPED,
        reason: String? = null
    ) {

        val sessionId: String

        synchronized(this) {

            val activeSession =
                currentSessionId

            if (
                activeSession == null ||
                currentState == WorkflowState.IDLE
            ) {
                logger.w(
                    "Session stop ignored. No active session."
                )
                return
            }

            sessionId =
                activeSession
        }

        val event =
            ExecutionEvent.SessionStopped(
                sessionId = sessionId,
                finalState = finalState,
                reason = reason
            )

        recordEventSafely(
            event
        )

        updateMetricsStateSafely(
            finalState
        )

        synchronized(this) {

            currentSessionId =
                null

            currentState =
                finalState
        }

        logger.i(
            "Session stopped: $sessionId | " +
                "Final state: $finalState | " +
                "Reason: ${reason ?: "none"}"
        )
    }

    // ========================================================================
    // ACTION LIFECYCLE
    // ========================================================================

    /**
     * Records that an action has been dispatched.
     *
     * This matches the current ActionOrchestrator contract.
     */
    fun recordActionDispatched(
        sessionId: String,
        actionType: Any,
        targetId: String? = null,
        targetText: String? = null
    ) {

        val normalizedSessionId =
            sessionId.trim()

        if (
            normalizedSessionId.isBlank()
        ) {
            logger.w(
                "Action dispatch ignored: blank sessionId."
            )
            return
        }

        if (
            !isCurrentSession(
                normalizedSessionId
            )
        ) {
            logger.w(
                "Action dispatch ignored for inactive session: " +
                    normalizedSessionId
            )
            return
        }

        val actionId =
            createActionId()

        val event =
            ExecutionEvent.ActionDispatched(
                sessionId = normalizedSessionId,
                actionId = actionId,
                actionType = actionType.toString()
            )

        recordEventSafely(
            event
        )

        logger.d(
            "Action dispatched: $actionId | " +
                "type=${actionType} | " +
                "targetId=${targetId ?: "none"} | " +
                "targetText=${targetText ?: "none"}"
        )
    }

    /**
     * Records successful action completion.
     */
    fun recordActionSucceeded(
        sessionId: String,
        actionType: Any,
        resultMessage: String? = null
    ) {

        val normalizedSessionId =
            sessionId.trim()

        if (
            normalizedSessionId.isBlank()
        ) {
            logger.w(
                "Action success ignored: blank sessionId."
            )
            return
        }

        if (
            !isCurrentSession(
                normalizedSessionId
            )
        ) {
            logger.w(
                "Action success ignored for inactive session: " +
                    normalizedSessionId
            )
            return
        }

        val actionId =
            createActionId()

        val event =
            ExecutionEvent.ActionSucceeded(
                sessionId = normalizedSessionId,
                actionId = actionId,
                durationMs = 0L
            )

        recordEventSafely(
            event
        )

        logger.d(
            "Action succeeded: $actionId | " +
                "type=$actionType | " +
                "message=${resultMessage ?: "none"}"
        )
    }

    /**
     * Records failed action execution.
     *
     * Tracker does NOT decide whether the workflow should
     * stop, recover, retry, or enter ERROR state.
     */
    fun recordActionFailed(
        sessionId: String,
        actionType: Any,
        errorCode: String,
        errorMessage: String?
    ) {

        val normalizedSessionId =
            sessionId.trim()

        if (
            normalizedSessionId.isBlank()
        ) {
            logger.w(
                "Action failure ignored: blank sessionId."
            )
            return
        }

        if (
            !isCurrentSession(
                normalizedSessionId
            )
        ) {
            logger.w(
                "Action failure ignored for inactive session: " +
                    normalizedSessionId
            )
            return
        }

        val actionId =
            createActionId()

        val reason =
            if (
                errorCode.isBlank()
            ) {
                "ACTION_FAILED"
            } else {
                errorCode
            }

        val event =
            ExecutionEvent.ActionFailed(
                sessionId = normalizedSessionId,
                actionId = actionId,
                durationMs = 0L,
                failureReason = reason,
                exceptionMessage = errorMessage
            )

        recordEventSafely(
            event
        )

        logger.e(
            "Action failed: $actionId | " +
                "type=$actionType | " +
                "code=$errorCode | " +
                "message=${errorMessage ?: "none"}"
        )
    }

    // ========================================================================
    // WORKFLOW STATE
    // ========================================================================

    /**
     * Updates canonical workflow state.
     *
     * Tracker records state; it does not decide state transitions.
     */
    fun updateWorkflowState(
        newState: WorkflowState
    ) {

        synchronized(this) {

            if (
                currentState == newState
            ) {
                return
            }

            val oldState =
                currentState

            currentState =
                newState

            updateMetricsStateSafely(
                newState
            )

            logger.i(
                "Workflow state: " +
                    "$oldState -> $newState"
            )
        }
    }

    // ========================================================================
    // SESSION CHECK
    // ========================================================================

    private fun isCurrentSession(
        sessionId: String
    ): Boolean {

        return synchronized(this) {

            currentSessionId ==
                sessionId &&
                currentState !=
                WorkflowState.IDLE
        }
    }

    // ========================================================================
    // EVENT RECORDING
    // ========================================================================

    private fun recordEventSafely(
        event: ExecutionEvent
    ) {

        try {

            /*
             * Existing ExecutionRecorder contract is responsible
             * for persistence/history handling.
             *
             * The tracker itself remains failure-safe.
             */
            executionRecorder.recordEvent(
                event
            )

        } catch (error: Throwable) {

            logger.e(
                "Execution history recording failed: " +
                    "${error.message}"
            )
        }

        try {

            metricsCollector.recordEvent(
                event
            )

        } catch (error: Throwable) {

            logger.e(
                "Metrics recording failed: " +
                    "${error.message}"
            )
        }
    }

    // ========================================================================
    // METRICS STATE
    // ========================================================================

    private fun updateMetricsStateSafely(
        state: WorkflowState
    ) {

        try {

            metricsCollector.updateState(
                state
            )

        } catch (error: Throwable) {

            logger.e(
                "Metrics state update failed: " +
                    "${error.message}"
            )
        }
    }

    // ========================================================================
    // ACTION ID
    // ========================================================================

    private fun createActionId(): String {
        return "action-" +
            System.currentTimeMillis() +
            "-" +
            actionCounter.incrementAndGet()
    }

    companion object {

        private val actionCounter =
            java.util.concurrent.atomic.AtomicLong(0L)
    }
}
