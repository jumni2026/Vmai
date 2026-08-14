package com.vmax.action

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionEvent.kt
 *
 * Canonical contract for every event within an execution session.
 *
 * Architecture:
 * - Platform-independent
 * - No Android dependencies
 * - No persistence logic
 * - No business logic
 *
 * This class is owned by the core-action module and must NOT be
 * duplicated in runtime or app modules.
 */
sealed class ExecutionEvent {

    /**
     * Session lifecycle events.
     */
    data class SessionStarted(
        val sessionId: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    data class SessionStopped(
        val sessionId: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    data class SessionError(
        val sessionId: String,
        val errorCode: String,
        val errorMessage: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    /**
     * Workflow state transition event.
     */
    data class WorkflowStateChanged(
        val sessionId: String,
        val fromState: String,
        val toState: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    /**
     * Action execution events.
     */
    data class ActionDispatched(
        val sessionId: String,
        val actionType: ActionExecutor.ActionType,
        val targetId: String?,
        val targetText: String?,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    data class ActionSucceeded(
        val sessionId: String,
        val actionType: ActionExecutor.ActionType,
        val resultMessage: String?,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    data class ActionFailed(
        val sessionId: String,
        val actionType: ActionExecutor.ActionType,
        val errorCode: String,
        val errorMessage: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()
}
