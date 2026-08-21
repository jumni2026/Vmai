package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.common.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * VMAX Enterprise v2.6.1
 *
 * File: ExecutionTracker.kt
 *
 * Responsibility:
 * - Track workflow session lifecycle.
 * - Track action dispatch/success/failure.
 * - Maintain session execution timeline.
 * - Remain platform independent.
 *
 * IMPORTANT:
 * - No Android dependencies.
 * - No action execution.
 * - No retry logic.
 * - No parallel execution logic.
 * - No SLF4J.
 * - No duplicate ExecutionEvent.
 * - No duplicate recorder/metrics contracts.
 * - Does not decide workflow recovery.
 */
class ExecutionTracker(
    private val logger: Logger
) {

    // ========================================================================
    // SESSION STORAGE - THESE MUST BE AT TOP
    // ========================================================================

    /**
     * Complete execution timeline for every session.
     * All mutations are protected by synchronized blocks.
     */
    private val sessionEvents = ConcurrentHashMap<String, MutableList<ExecutionEvent>>()
    
    /**
     * Currently active session ID.
     */
    @Volatile
    private var activeSession: String? = null

    // ========================================================================
    // SESSION LIFECYCLE
    // ========================================================================

    /**
     * Starts a new execution session.
     *
     * If the same session is already active, returns existing SessionStarted event.
     * If another session is active, the new session is ignored.
     */
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

    /**
     * Stops an active session.
     * The session timeline is retained for history.
     */
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

    // ========================================================================
    // WORKFLOW STATE TRANSITION
    // ========================================================================

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

    // ========================================================================
    // ACTION DISPATCH
    // ========================================================================

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

    // ========================================================================
    // ACTION SUCCESS
    // ========================================================================

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

    // ========================================================================
    // ACTION FAILURE
    // ========================================================================

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

    // ========================================================================
    // SESSION ERROR
    // ========================================================================

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

    /**
     * Returns a snapshot of the session timeline.
     */
    fun getSessionTimeline(sessionId: String): List<ExecutionEvent> {
        val normalized = normalizeSessionId(sessionId)
        
        synchronized(sessionEvents) {
            return sessionEvents[normalized]?.toList() ?: emptyList()
        }
    }

    /**
     * Returns all known session IDs.
     */
    fun getAllSessionIds(): Set<String> {
        synchronized(sessionEvents) {
            return sessionEvents.keys.toSet()
        }
    }

    /**
     * Returns the currently active session ID.
     */
    fun getActiveSessionId(): String? = activeSession

    /**
     * Returns events for a session (simplified API).
     */
    fun getEvents(sessionId: String? = null): List<ExecutionEvent> {
        val target = sessionId ?: activeSession ?: return emptyList()
        return getSessionTimeline(target)
    }

    /**
     * Returns all session IDs (alias for getAllSessionIds).
     */
    fun getAllSessions(): Set<String> = getAllSessionIds()

    /**
     * Returns event count for a session.
     */
    fun getEventCount(sessionId: String? = null): Int {
        val target = sessionId ?: activeSession ?: return 0
        return getSessionTimeline(target).size
    }

    // ========================================================================
    // MAINTENANCE
    // ========================================================================

    /**
     * Removes one session from memory.
     */
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

    /**
     * Removes a session (alias for clearSession).
     * If no sessionId provided, clears the active session.
     */
    fun clear(sessionId: String? = null) {
        val target = sessionId ?: activeSession
        if (target != null) {
            clearSession(target)
        }
    }

    /**
     * Removes all session timelines.
     */
    fun clearAllSessions() {
        synchronized(sessionEvents) {
            sessionEvents.clear()
            activeSession = null
        }
        logger.warn("ExecutionTracker", "All execution sessions cleared")
    }

    /**
     * Clears all (alias for clearAllSessions).
     */
    fun clearAll() = clearAllSessions()

    // ========================================================================
    // INTERNAL HELPERS - THESE MUST BE DEFINED
    // ========================================================================

    /**
     * Normalizes and validates a session ID.
     * 
     * @param sessionId Raw session ID
     * @return Trimmed session ID
     * @throws IllegalArgumentException if sessionId is blank
     */
    private fun normalizeSessionId(sessionId: String): String {
        val normalized = sessionId.trim()
        require(normalized.isNotEmpty()) { "sessionId must not be blank" }
        return normalized
    }

    /**
     * Returns an existing timeline or creates one.
     *
     * IMPORTANT:
     * This method must only be called while sessionEvents
     * is already synchronized.
     * 
     * @param sessionId Normalized session ID
     * @return Mutable list of ExecutionEvent for the session
     */
    private fun getOrCreateSessionEvents(sessionId: String): MutableList<ExecutionEvent> {
        return sessionEvents.getOrPut(sessionId) { mutableListOf() }
    }

    // ========================================================================
    // COMPANION: ACTION ID GENERATOR
    // ========================================================================

    companion object {
        private val actionCounter = AtomicLong(0L)

        /**
         * Generates a unique action ID.
         * Format: action-{timestamp}-{counter}
         */
        @JvmStatic
        fun nextActionId(): String {
            return "action-${System.currentTimeMillis()}-${actionCounter.incrementAndGet()}"
        }
    }
}
