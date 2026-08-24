package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.common.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class ExecutionTracker(
    private val logger: Logger
) {

    private val sessionEvents =
        ConcurrentHashMap<String, MutableList<ExecutionEvent>>()

    @Volatile
    private var activeSession: String? = null

    fun startSession(sessionId: String): ExecutionEvent.SessionStarted {
        val normalized = normalizeSessionId(sessionId)
        val event = ExecutionEvent.SessionStarted(normalized)
        val timeline = getOrCreateSessionEvents(normalized)
        synchronized(sessionEvents) {
            timeline.add(event)
            activeSession = normalized
        }
        logger.info("ExecutionTracker", "Session started: $normalized")
        return event
    }

    fun stopSession(sessionId: String): ExecutionEvent.SessionStopped {
        val normalized = normalizeSessionId(sessionId)
        val event = ExecutionEvent.SessionStopped(normalized)
        synchronized(sessionEvents) {
            val timeline = getOrCreateSessionEvents(normalized)
            timeline.add(event)
            if (activeSession == normalized) {
                activeSession = null
            }
        }
        logger.info("ExecutionTracker", "Session stopped: $normalized")
        return event
    }

    fun recordStateTransition(
        sessionId: String,
        fromState: String,
        toState: String
    ): ExecutionEvent.WorkflowStateChanged {
        val normalized = normalizeSessionId(sessionId)
        val event = ExecutionEvent.WorkflowStateChanged(normalized, fromState, toState)
        synchronized(sessionEvents) {
            val timeline = getOrCreateSessionEvents(normalized)
            timeline.add(event)
        }
        logger.debug("ExecutionTracker", "State changed: $fromState -> $toState")
        return event
    }

    fun recordActionDispatched(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        targetId: String?,
        targetText: String?
    ): ExecutionEvent.ActionDispatched {
        val normalized = normalizeSessionId(sessionId)
        val event = ExecutionEvent.ActionDispatched(normalized, actionType, targetId, targetText)
        synchronized(sessionEvents) {
            val timeline = getOrCreateSessionEvents(normalized)
            timeline.add(event)
        }
        logger.debug("ExecutionTracker", "Action dispatched: $actionType")
        return event
    }

    fun recordActionSucceeded(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        resultMessage: String?
    ): ExecutionEvent.ActionSucceeded {
        val normalized = normalizeSessionId(sessionId)
        val event = ExecutionEvent.ActionSucceeded(normalized, actionType, resultMessage)
        synchronized(sessionEvents) {
            val timeline = getOrCreateSessionEvents(normalized)
            timeline.add(event)
        }
        logger.info("ExecutionTracker", "Action succeeded: $actionType")
        return event
    }

    fun recordActionFailed(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        errorCode: String,
        errorMessage: String
    ): ExecutionEvent.ActionFailed {
        val normalized = normalizeSessionId(sessionId)
        val event = ExecutionEvent.ActionFailed(normalized, actionType, errorCode, errorMessage)
        synchronized(sessionEvents) {
            val timeline = getOrCreateSessionEvents(normalized)
            timeline.add(event)
        }
        logger.error("ExecutionTracker", "Action failed: $errorCode | $errorMessage")
        return event
    }

    fun recordSessionError(
        sessionId: String,
        errorCode: String,
        errorMessage: String
    ): ExecutionEvent.SessionError {
        val normalized = normalizeSessionId(sessionId)
        val event = ExecutionEvent.SessionError(normalized, errorCode, errorMessage)
        synchronized(sessionEvents) {
            val timeline = getOrCreateSessionEvents(normalized)
            timeline.add(event)
        }
        logger.error("ExecutionTracker", "Session error: $errorCode | $errorMessage")
        return event
    }

    fun getSessionTimeline(sessionId: String): List<ExecutionEvent> {
        val normalized = normalizeSessionId(sessionId)
        synchronized(sessionEvents) {
            return sessionEvents[normalized]?.toList() ?: emptyList()
        }
    }

    fun getAllSessionIds(): Set<String> {
        synchronized(sessionEvents) {
            return sessionEvents.keys.toSet()
        }
    }

    fun getActiveSessionId(): String? {
        return activeSession
    }

    fun clearSession(sessionId: String) {
        val normalized = normalizeSessionId(sessionId)
        synchronized(sessionEvents) {
            sessionEvents.remove(normalized)
            if (activeSession == normalized) {
                activeSession = null
            }
        }
        logger.info("ExecutionTracker", "Session cleared: $normalized")
    }

    fun clearAllSessions() {
        synchronized(sessionEvents) {
            sessionEvents.clear()
            activeSession = null
        }
        logger.warn("ExecutionTracker", "All execution sessions cleared")
    }

    private fun normalizeSessionId(sessionId: String): String {
        val normalized = sessionId.trim()
        require(normalized.isNotEmpty()) { "sessionId must not be blank" }
        return normalized
    }

    private fun getOrCreateSessionEvents(sessionId: String): MutableList<ExecutionEvent> {
        return sessionEvents.getOrPut(sessionId) { mutableListOf() }
    }

    companion object {
        private val actionCounter = AtomicLong(0L)

        @JvmStatic
        fun nextActionId(): String {
            return "action-" + System.currentTimeMillis() + "-" + actionCounter.incrementAndGet()
        }
    }
}
