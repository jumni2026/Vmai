package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.common.Logger

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionTracker.kt
 *
 * Central recording system for tracking every step of an execution session.
 * Platform-independent — no Android dependencies.
 */
class ExecutionTracker(
    private val logger: Logger
) {

    private val sessions = mutableMapOf<String, MutableList<ExecutionEvent>>()

    fun startSession(sessionId: String): ExecutionEvent.SessionStarted {
        val event = ExecutionEvent.SessionStarted(sessionId)
        sessions[sessionId] = mutableListOf(event)
        logger.info("ExecutionTracker", "Session started: $sessionId")
        return event
    }

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

    fun recordActionDispatched(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        targetId: String?,
        targetText: String?
    ): ExecutionEvent.ActionDispatched {
        val event = ExecutionEvent.ActionDispatched(
            sessionId,
            actionType,
            targetId,
            targetText
        )
        getSessionEvents(sessionId).add(event)
        logger.debug("ExecutionTracker", "Action dispatched: $actionType")
        return event
    }

    fun recordActionSucceeded(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        resultMessage: String?
    ): ExecutionEvent.ActionSucceeded {
        val event = ExecutionEvent.ActionSucceeded(
            sessionId,
            actionType,
            resultMessage
        )
        getSessionEvents(sessionId).add(event)
        logger.info("ExecutionTracker", "Action succeeded: $actionType")
        return event
    }

    fun recordActionFailed(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        errorCode: String,
        errorMessage: String
    ): ExecutionEvent.ActionFailed {
        val event = ExecutionEvent.ActionFailed(
            sessionId,
            actionType,
            errorCode,
            errorMessage
        )
        getSessionEvents(sessionId).add(event)
        logger.error(
            "ExecutionTracker",
            "Action failed: $actionType -> $errorCode: $errorMessage"
        )
        return event
    }

    fun stopSession(sessionId: String): ExecutionEvent.SessionStopped {
        val event = ExecutionEvent.SessionStopped(sessionId)
        getSessionEvents(sessionId).add(event)
        logger.info("ExecutionTracker", "Session stopped: $sessionId")
        return event
    }

    fun recordSessionError(
        sessionId: String,
        errorCode: String,
        errorMessage: String
    ): ExecutionEvent.SessionError {
        val event = ExecutionEvent.SessionError(
            sessionId,
            errorCode,
            errorMessage
        )
        getSessionEvents(sessionId).add(event)
        logger.error(
            "ExecutionTracker",
            "Session error: $errorCode -> $errorMessage"
        )
        return event
    }

    fun getSessionTimeline(sessionId: String): List<ExecutionEvent> {
        return sessions[sessionId] ?: emptyList()
    }

    fun getAllSessionIds(): Set<String> = sessions.keys

    fun clearAllSessions() {
        sessions.clear()
        logger.warn("ExecutionTracker", "All sessions cleared")
    }

    private fun getSessionEvents(sessionId: String): MutableList<ExecutionEvent> {
        return sessions[sessionId]
            ?: throw IllegalStateException("Session $sessionId not found")
    }
}
