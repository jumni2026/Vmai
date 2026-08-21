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
 * - Track workflow state transitions.
 * - Track action dispatch/success/failure.
 * - Track session errors.
 * - Maintain an in-memory execution timeline.
 * - Remain completely platform independent.
 *
 * IMPORTANT:
 * - No Android dependencies.
 * - No action execution.
 * - No retry logic.
 * - No workflow recovery decisions.
 * - No duplicate ExecutionEvent.
 * - No MetricsCollector dependency.
 * - No ExecutionRecorder dependency.
 */
class ExecutionTracker(
    private val logger: Logger
) {

    // ========================================================================
    // INTERNAL STORAGE
    // ========================================================================

    /**
     * Each session owns one timeline.
     *
     * ConcurrentHashMap is used for session lookup.
     * Individual timeline mutations are protected by timelineMonitor.
     */
    private val timelineStore =
        ConcurrentHashMap<String, MutableList<ExecutionEvent>>()

    /**
     * Single monitor protecting timeline mutations and snapshots.
     *
     * Deliberately named differently from previous implementations so that
     * no old sessionLock contract remains in this file.
     */
    private val timelineMonitor = Any()

    /**
     * Currently active session.
     */
    @Volatile
    private var currentSessionId: String? = null

    // ========================================================================
    // SESSION LIFECYCLE
    // ========================================================================

    /**
     * Starts a new execution session.
     *
     * If the same session is already active, its existing SessionStarted
     * event is returned.
     *
     * If another session is active, the new session is not started.
     */
    fun startSession(
        sessionId: String
    ): ExecutionEvent.SessionStarted {

        val id = normalizeSessionId(sessionId)

        synchronized(timelineMonitor) {

            val activeId = currentSessionId

            // ------------------------------------------------------------
            // Same session already active
            // ------------------------------------------------------------

            if (activeId == id) {

                val existingStart =
                    timelineStore[id]
                        ?.firstOrNull { event ->
                            event is ExecutionEvent.SessionStarted
                        } as? ExecutionEvent.SessionStarted

                if (existingStart != null) {
                    logger.warn(
                        "ExecutionTracker",
                        "Session already active: $id"
                    )

                    return existingStart
                }

                /*
                 * Defensive recovery:
                 * active session exists but its start event is missing.
                 */
                val recoveredStart =
                    ExecutionEvent.SessionStarted(id)

                timelineStore
                    .getOrPut(id) {
                        mutableListOf()
                    }
                    .add(recoveredStart)

                logger.warn(
                    "ExecutionTracker",
                    "Recovered missing SessionStarted event: $id"
                )

                return recoveredStart
            }

            // ------------------------------------------------------------
            // Another session already active
            // ------------------------------------------------------------

            if (activeId != null) {

                logger.warn(
                    "ExecutionTracker",
                    "Session start ignored. " +
                        "Active session: $activeId"
                )

                /*
                 * Contract compatibility:
                 * caller still receives a SessionStarted object,
                 * but it is NOT inserted into the active session timeline.
                 */
                return ExecutionEvent.SessionStarted(id)
            }

            // ------------------------------------------------------------
            // Create new session
            // ------------------------------------------------------------

            val startEvent =
                ExecutionEvent.SessionStarted(id)

            timelineStore[id] =
                mutableListOf(startEvent)

            currentSessionId =
                id

            logger.info(
                "ExecutionTracker",
                "Session started: $id"
            )

            return startEvent
        }
    }

    /**
     * Stops a session.
     *
     * The session remains in the timeline after stopping so that its
     * execution history can still be inspected.
     */
    fun stopSession(
        sessionId: String
    ): ExecutionEvent.SessionStopped {

        val id = normalizeSessionId(sessionId)

        synchronized(timelineMonitor) {

            val stopEvent =
                ExecutionEvent.SessionStopped(id)

            timelineFor(id)
                .add(stopEvent)

            if (currentSessionId == id) {
                currentSessionId = null
            }

            logger.info(
                "ExecutionTracker",
                "Session stopped: $id"
            )

            return stopEvent
        }
    }

    // ========================================================================
    // WORKFLOW STATE
    // ========================================================================

    /**
     * Records a workflow state transition.
     */
    fun recordStateTransition(
        sessionId: String,
        fromState: String,
        toState: String
    ): ExecutionEvent.WorkflowStateChanged {

        val id = normalizeSessionId(sessionId)

        val transitionEvent =
            ExecutionEvent.WorkflowStateChanged(
                id,
                fromState,
                toState
            )

        synchronized(timelineMonitor) {

            timelineFor(id)
                .add(transitionEvent)
        }

        logger.debug(
            "ExecutionTracker",
            "State changed: $fromState -> $toState"
        )

        return transitionEvent
    }

    // ========================================================================
    // ACTION DISPATCH
    // ========================================================================

    /**
     * Records action dispatch.
     */
    fun recordActionDispatched(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        targetId: String?,
        targetText: String?
    ): ExecutionEvent.ActionDispatched {

        val id = normalizeSessionId(sessionId)

        val dispatchEvent =
            ExecutionEvent.ActionDispatched(
                id,
                actionType,
                targetId,
                targetText
            )

        synchronized(timelineMonitor) {

            timelineFor(id)
                .add(dispatchEvent)
        }

        logger.debug(
            "ExecutionTracker",
            "Action dispatched: $actionType | " +
                "targetId=${targetId ?: "none"} | " +
                "targetText=${targetText ?: "none"}"
        )

        return dispatchEvent
    }

    // ========================================================================
    // ACTION SUCCESS
    // ========================================================================

    /**
     * Records successful action execution.
     */
    fun recordActionSucceeded(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        resultMessage: String?
    ): ExecutionEvent.ActionSucceeded {

        val id = normalizeSessionId(sessionId)

        val successEvent =
            ExecutionEvent.ActionSucceeded(
                id,
                actionType,
                resultMessage
            )

        synchronized(timelineMonitor) {

            timelineFor(id)
                .add(successEvent)
        }

        logger.info(
            "ExecutionTracker",
            "Action succeeded: $actionType | " +
                "message=${resultMessage ?: "none"}"
        )

        return successEvent
    }

    // ========================================================================
    // ACTION FAILURE
    // ========================================================================

    /**
     * Records failed action execution.
     */
    fun recordActionFailed(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        errorCode: String,
        errorMessage: String
    ): ExecutionEvent.ActionFailed {

        val id = normalizeSessionId(sessionId)

        val failureEvent =
            ExecutionEvent.ActionFailed(
                id,
                actionType,
                errorCode,
                errorMessage
            )

        synchronized(timelineMonitor) {

            timelineFor(id)
                .add(failureEvent)
        }

        logger.error(
            "ExecutionTracker",
            "Action failed: $actionType | " +
                "$errorCode | $errorMessage"
        )

        return failureEvent
    }

    // ========================================================================
    // SESSION ERROR
    // ========================================================================

    /**
     * Records a session-level error.
     */
    fun recordSessionError(
        sessionId: String,
        errorCode: String,
        errorMessage: String
    ): ExecutionEvent.SessionError {

        val id = normalizeSessionId(sessionId)

        val errorEvent =
            ExecutionEvent.SessionError(
                id,
                errorCode,
                errorMessage
            )

        synchronized(timelineMonitor) {

            timelineFor(id)
                .add(errorEvent)
        }

        logger.error(
            "ExecutionTracker",
            "Session error: $errorCode | $errorMessage"
        )

        return errorEvent
    }

    // ========================================================================
    // READ APIs
    // ========================================================================

    /**
     * Returns an immutable snapshot of the session timeline.
     *
     * The caller cannot modify the internal list.
     */
    fun getSessionTimeline(
        sessionId: String
    ): List<ExecutionEvent> {

        val id = normalizeSessionId(sessionId)

        synchronized(timelineMonitor) {

            return timelineStore[id]
                ?.toList()
                ?: emptyList()
        }
    }

    /**
     * Returns all known session IDs.
     */
    fun getAllSessionIds(): Set<String> {

        synchronized(timelineMonitor) {

            return timelineStore.keys.toSet()
        }
    }

    /**
     * Returns the currently active session ID.
     */
    fun getActiveSessionId(): String? {

        return currentSessionId
    }

    /**
     * Alias for callers that use the shorter name.
     */
    fun getCurrentSessionId(): String? {

        return currentSessionId
    }

    // ========================================================================
    // SESSION EXISTENCE
    // ========================================================================

    /**
     * Returns true if a session exists in the timeline store.
     */
    fun hasSession(
        sessionId: String
    ): Boolean {

        val id = normalizeSessionId(sessionId)

        synchronized(timelineMonitor) {

            return timelineStore.containsKey(id)
        }
    }

    /**
     * Returns true if the supplied session is currently active.
     */
    fun isSessionActive(
        sessionId: String
    ): Boolean {

        val id = normalizeSessionId(sessionId)

        return currentSessionId == id
    }

    // ========================================================================
    // MAINTENANCE
    // ========================================================================

    /**
     * Removes one session from memory.
     */
    fun clearSession(
        sessionId: String
    ) {

        val id = normalizeSessionId(sessionId)

        synchronized(timelineMonitor) {

            timelineStore.remove(id)

            if (currentSessionId == id) {
                currentSessionId = null
            }
        }

        logger.info(
            "ExecutionTracker",
            "Session cleared: $id"
        )
    }

    /**
     * Removes all sessions from memory.
     */
    fun clearAllSessions() {

        synchronized(timelineMonitor) {

            timelineStore.clear()
            currentSessionId = null
        }

        logger.warn(
            "ExecutionTracker",
            "All execution sessions cleared"
        )
    }

    // ========================================================================
    // INTERNAL HELPERS
    // ========================================================================

    /**
     * Normalizes and validates session IDs in one place.
     */
    private fun normalizeSessionId(
        sessionId: String
    ): String {

        val normalized =
            sessionId.trim()

        require(normalized.isNotEmpty()) {
            "sessionId must not be blank"
        }

        return normalized
    }

    /**
     * Returns the mutable timeline for an existing session.
     *
     * IMPORTANT:
     * This function does NOT use any old getSessionEvents/sessionLock
     * implementation.
     *
     * It must only be called while timelineMonitor is held.
     */
    private fun timelineFor(
        sessionId: String
    ): MutableList<ExecutionEvent> {

        return timelineStore[sessionId]
            ?: throw IllegalStateException(
                "Session not found: $sessionId"
            )
    }

    // ========================================================================
    // ACTION ID
    // ========================================================================

    companion object {

        private val actionCounter =
            AtomicLong(0L)

        /**
         * Generates a process-local unique action ID.
         */
        @JvmStatic
        fun nextActionId(): String {

            return "action-" +
                System.currentTimeMillis() +
                "-" +
                actionCounter.incrementAndGet()
        }
    }
}
