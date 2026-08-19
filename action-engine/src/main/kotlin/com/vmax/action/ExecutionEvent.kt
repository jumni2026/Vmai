package com.vmax.action

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionEvent.kt
 *
 * Defines the immutable event contract for a single execution session.
 *
 * Responsibilities:
 * - Represent session lifecycle events.
 * - Represent workflow state transitions.
 * - Represent action execution events.
 * - Carry diagnostic information required for execution history.
 *
 * Design:
 * - Platform-independent.
 * - No Android dependencies.
 * - No business logic.
 * - Immutable event data.
 * - Safe to persist or forward to platform-specific recorders.
 */
sealed class ExecutionEvent {

    /**
     * --------------------------------------------------------
     * Session Lifecycle Events
     * --------------------------------------------------------
     */

    /**
     * Indicates that a new execution session has started.
     */
    data class SessionStarted(
        val sessionId: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    /**
     * Indicates that an execution session has stopped normally
     * or by an explicit stop request.
     */
    data class SessionStopped(
        val sessionId: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    /**
     * Indicates that the execution session encountered an error.
     */
    data class SessionError(
        val sessionId: String,
        val errorCode: String,
        val errorMessage: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    /**
     * --------------------------------------------------------
     * Workflow State Events
     * --------------------------------------------------------
     */

    /**
     * Records a workflow state transition.
     */
    data class WorkflowStateChanged(
        val sessionId: String,
        val fromState: String,
        val toState: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    /**
     * --------------------------------------------------------
     * Action Execution Events
     * --------------------------------------------------------
     */

    /**
     * Indicates that an action was dispatched for execution.
     *
     * targetId and targetText are optional because not every
     * action requires a UI target.
     */
    data class ActionDispatched(
        val sessionId: String,
        val actionType: ActionExecutor.ActionType,
        val targetId: String?,
        val targetText: String?,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    /**
     * Indicates that an action completed successfully.
     */
    data class ActionSucceeded(
        val sessionId: String,
        val actionType: ActionExecutor.ActionType,
        val resultMessage: String?,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    /**
     * Indicates that an action failed.
     */
    data class ActionFailed(
        val sessionId: String,
        val actionType: ActionExecutor.ActionType,
        val errorCode: String,
        val errorMessage: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()
}
