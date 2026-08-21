package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.common.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class ExecutionTracker(
    private val logger: Logger
) {

    // ========================================================================
    // SESSION STORAGE - THESE MUST BE AT TOP
    // ========================================================================

    private val sessionEvents = ConcurrentHashMap<String, MutableList<ExecutionEvent>>()
    
    @Volatile
    private var activeSession: String? = null

    // ========================================================================
    // SESSION LIFECYCLE
    // ========================================================================

    fun startSession(sessionId: String): ExecutionEvent.SessionStarted {
        val normalized = normalizeSessionId(sessionId)
        
        synchronized(sessionEvents) {
            val existingActive = activeSession
            
            if (existingActive != null) {
                if (existingActive == normalized) {
                    logger.warn("ExecutionTracker", "Session already active: $normalized")
                    val events = getOrCreateSessionEvents(normalized)
                    for (event in events) {
                        if (event is ExecutionEvent.SessionStarted) {
                            return event
                        }
                    }
                    val fallback = ExecutionEvent.SessionStarted(normalized)
                    events.add(fallback)
                    return fallback
                }
                
                logger.warn("ExecutionTracker", "Session start ignored. Active session: $existingActive")
                return ExecutionEvent.SessionStarted(normalized)
            }
            
            val event = ExecutionEvent.SessionStarted(normalized)
            val timeline = getOrCreateSessionEvents(normalized)
            timeline.add(event)
            activeSession = normalized
            
            logger.info("ExecutionTracker", "Session started: $normalized")
            return event
        }
    }

    fun stopSession(sessionId: String): ExecutionEvent.SessionStopped {
        val normalized = normalizeSessionId(sessionId)
        
        synchronized(sessionEvents) {
            val timeline = getOrCreateSessionEvents(normalized)
            val event = ExecutionEvent.SessionStopped(normalized)
            timeline.add(event)
            
            if (activeSession == normalized) {
                activeSession = null
            }
            
            logger.info("ExecutionTracker", "Session stopped: $normalized")
            return event
        }
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
        
        logger.debug(
            "ExecutionTracker",
            "Action dispatched: $actionType | targetId=${targetId ?: "none"} | targetText=${targetText ?: "none"}"
        )
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
        
        logger.info(
            "ExecutionTracker",
            "Action succeeded: $actionType | message=${resultMessage ?: "none"}"
        )
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
        
        logger.error(
            "ExecutionTracker",
            "Action failed: $actionType | $errorCode | $errorMessage"
        )
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
        
        logger.error(
            "ExecutionTracker",
            "Session error: $errorCode | $errorMessage"
        )
        return event
    }

    // ========================================================================
    // READ APIs
    // ========================================================================

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

    fun getActiveSessionId(): String? = activeSession

    fun getEvents(sessionId: String? = null): List<ExecutionEvent> {
        val target = sessionId ?: activeSession ?: return emptyList()
        return getSessionTimeline(target)
    }

    fun getAllSessions(): Set<String> = getAllSessionIds()

    fun getEventCount(sessionId: String? = null): Int {
        val target = sessionId ?: activeSession ?: return 0
        return getSessionTimeline(target).size
    }

    // ========================================================================
    // MAINTENANCE
    // ========================================================================

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

    fun clear(sessionId: String? = null) {
        val target = sessionId ?: activeSession
        if (target != null) {
            clearSession(target)
        }
    }

    fun clearAllSessions() {
        synchronized(sessionEvents) {
            sessionEvents.clear()
            activeSession = null
        }
        logger.warn("ExecutionTracker", "All execution sessions cleared")
    }

    fun clearAll() = clearAllSessions()

    // ========================================================================
    // INTERNAL HELPERS
    // ========================================================================

    private fun normalizeSessionId(sessionId: String): String {
        val normalized = sessionId.trim()
        require(normalized.isNotEmpty()) { "sessionId must not be blank" }
        return normalized
    }

    private fun getOrCreateSessionEvents(sessionId: String): MutableList<ExecutionEvent> {
        return sessionEvents.getOrPut(sessionId) { mutableListOf() }
    }

    // ========================================================================
    // COMPANION
    // ========================================================================

    companion object {
        private val actionCounter = AtomicLong(0L)

        @JvmStatic
        fun nextActionId(): String {
            return "action-${System.currentTimeMillis()}-${actionCounter.incrementAndGet()}"
        }
    }
}
