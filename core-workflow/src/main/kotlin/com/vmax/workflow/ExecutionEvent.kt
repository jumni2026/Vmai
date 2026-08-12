package com.vmax.runtime

import com.vmax.action.ActionExecutor
import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionEvent.kt
 *
 * Defines the contract for every step within a single execution session.
 * Platform-independent — no Android dependencies.
 * No business logic.
 */
sealed class ExecutionEvent {

    /**
     * Session Lifecycle Events
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
     * Workflow State Events
     */
    data class WorkflowStateChanged(
        val sessionId: String,
        val fromState: String,
        val toState: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ExecutionEvent()

    /**
     * Action Execution Events
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
