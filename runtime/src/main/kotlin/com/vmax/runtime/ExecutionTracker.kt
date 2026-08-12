package com.vmax.runtime

import com.vmax.action.ActionExecutor
import com.vmax.common.Logger
import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionTracker.kt
 *
 * Central recording system for tracking every step of an execution session.
 * It records events, manages session state, and provides a timeline for debugging.
 *
 * Architecture:
 *
 * WorkflowController / RuntimeCoordinator / ActionExecutor
 *              ↓
 *          ExecutionTracker
 *              ↓
 *      In-Memory Event List (Persistent storage to be added later)
 */
class ExecutionTracker(
    private val logger: Logger
) {

    private val sessions = mutableMapOf<String, MutableList<ExecutionEvent>>()

    /**
     * Starts a new execution session and records the first event.
     */
    fun startSession(sessionId: String): ExecutionEvent.SessionStarted {
        val event = ExecutionEvent.SessionStarted(sessionId)
        sessions[sessionId] = mutableListOf(event)
        logger.info("ExecutionTracker", "Session started: $sessionId")
        return event
    }

    /**
     * Records a workflow state transition.
     */
    fun recordStateTransition(
        sessionId: String,
        fromState: String,
        toState: String
    ): ExecutionEvent.WorkflowStateChanged {
        val event = ExecutionEvent.WorkflowStateChanged(sessionId, fromState, toState)
        getSessionEvents(sessionId).add(event)
        logger.debug("ExecutionTracker", "State changed: $fromState -> $toState")
        return event
    }

    /**
     * Records an action being dispatched to the executor.
     */
    fun recordActionDispatched(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        targetId: String?,
        targetText: String?
    ): ExecutionEvent.ActionDispatched {
        val event = ExecutionEvent.ActionDispatched(sessionId, actionType, targetId, targetText)
        getSessionEvents(sessionId).add(event)
        logger.debug("ExecutionTracker", "Action dispatched: $actionType")
        return event
    }

    /**
     * Records a successful action execution.
     */
    fun recordActionSucceeded(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        resultMessage: String?
    ): ExecutionEvent.ActionSucceeded {
        val event = ExecutionEvent.ActionSucceeded(sessionId, actionType, resultMessage)
        getSessionEvents(sessionId).add(event)
        logger.info("ExecutionTracker", "Action succeeded: $actionType")
        return event
    }

    /**
     * Records a failed action execution.
     */
    fun recordActionFailed(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        errorCode: String,
        errorMessage: String
    ): ExecutionEvent.ActionFailed {
        val event = ExecutionEvent.ActionFailed(sessionId, actionType, errorCode, errorMessage)
        getSessionEvents(sessionId).add(event)
        logger.error("ExecutionTracker", "Action failed: $actionType -> $errorCode: $errorMessage")
        return event
    }

    /**
     * Stops the session and records the final event.
     */
    fun stopSession(sessionId: String): ExecutionEvent.SessionStopped {
        val event = ExecutionEvent.SessionStopped(sessionId)
        getSessionEvents(sessionId).add(event)
        logger.info("ExecutionTracker", "Session stopped: $sessionId")
        return event
    }

    /**
     * Records a session-level error (e.g., CAPTCHA/OTP, Network failure).
     */
    fun recordSessionError(
        sessionId: String,
        errorCode: String,
        errorMessage: String
    ): ExecutionEvent.SessionError {
        val event = ExecutionEvent.SessionError(sessionId, errorCode, errorMessage)
        getSessionEvents(sessionId).add(event)
        logger.error("ExecutionTracker", "Session error: $errorCode -> $errorMessage")
        return event
    }

    /**
     * Returns the complete event timeline for a given session.
     */
    fun getSessionTimeline(sessionId: String): List<ExecutionEvent> {
        return sessions[sessionId] ?: emptyList()
    }

    /**
     * Returns all active session IDs.
     */
    fun getAllSessionIds(): Set<String> = sessions.keys

    /**
     * Clears all recorded sessions (useful for testing or reset).
     */
    fun clearAllSessions() {
        sessions.clear()
        logger.warn("ExecutionTracker", "All sessions cleared")
    }

    // ----------------------------------------------------------------
    // PRIVATE HELPERS
    // ----------------------------------------------------------------
    private fun getSessionEvents(sessionId: String): MutableList<ExecutionEvent> {
        return sessions[sessionId] ?: throw IllegalStateException("Session $sessionId not found")
    }
}
