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
    // SESSION STORAGE
    // ========================================================================

    /**
     * Complete execution timeline for every session.
     *
     * All mutations are protected by sessionLock.
     */
    private val sessions:
        ConcurrentHashMap<String, MutableList<ExecutionEvent>> =
        ConcurrentHashMap()

    /**
     * Single lock protecting session timeline mutations.
     */
    private val sessionLock: Any =
        Any()

    /**
     * Currently active session.
     */
    @Volatile
    private var activeSessionId: String? =
        null

    // ========================================================================
    // SESSION LIFECYCLE
    // ========================================================================

    /**
     * Starts a new execution session.
     *
     * If the same session is already active, the existing
     * SessionStarted event is returned.
     *
     * If another session is active, the new session is ignored
     * and a SessionStarted event is returned without registering it.
     */
    fun startSession(
        sessionId: String
    ): ExecutionEvent.SessionStarted {

        val normalizedSessionId =
            normalizeSessionId(sessionId)

        synchronized(sessionLock) {

            val existingSessionId =
                activeSessionId

            if (existingSessionId != null) {

                if (
                    existingSessionId ==
                    normalizedSessionId
                ) {

                    logger.warn(
                        "ExecutionTracker",
                        "Session already active: $normalizedSessionId"
                    )

                    val existingEvents:
                        MutableList<ExecutionEvent> =
                        sessions[normalizedSessionId]
                            ?: mutableListOf()

                    for (event in existingEvents) {
                        if (
                            event is
                            ExecutionEvent.SessionStarted
                        ) {
                            return event
                        }
                    }

                    val fallbackEvent =
                        ExecutionEvent.SessionStarted(
                            normalizedSessionId
                        )

                    existingEvents.add(
                        fallbackEvent
                    )

                    sessions[normalizedSessionId] =
                        existingEvents

                    return fallbackEvent
                }

                logger.warn(
                    "ExecutionTracker",
                    "Session start ignored. " +
                        "Active session: $existingSessionId"
                )

                return ExecutionEvent.SessionStarted(
                    normalizedSessionId
                )
            }

            val event =
                ExecutionEvent.SessionStarted(
                    normalizedSessionId
                )

            val timeline:
                MutableList<ExecutionEvent> =
                mutableListOf()

            timeline.add(event)

            sessions[normalizedSessionId] =
                timeline

            activeSessionId =
                normalizedSessionId

            logger.info(
                "ExecutionTracker",
                "Session started: $normalizedSessionId"
            )

            return event
        }
    }

    /**
     * Stops an active session.
     *
     * The session timeline is retained for history.
     */
    fun stopSession(
        sessionId: String
    ): ExecutionEvent.SessionStopped {

        val normalizedSessionId =
            normalizeSessionId(sessionId)

        synchronized(sessionLock) {

            val timeline:
                MutableList<ExecutionEvent> =
                getOrCreateSessionEvents(
                    normalizedSessionId
                )

            val event =
                ExecutionEvent.SessionStopped(
                    normalizedSessionId
                )

            timeline.add(event)

            if (
                activeSessionId ==
                normalizedSessionId
            ) {
                activeSessionId = null
            }

            logger.info(
                "ExecutionTracker",
                "Session stopped: $normalizedSessionId"
            )

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

        val normalizedSessionId =
            normalizeSessionId(sessionId)

        val event =
            ExecutionEvent.WorkflowStateChanged(
                normalizedSessionId,
                fromState,
                toState
            )

        synchronized(sessionLock) {

            val timeline:
                MutableList<ExecutionEvent> =
                getOrCreateSessionEvents(
                    normalizedSessionId
                )

            timeline.add(event)
        }

        logger.debug(
            "ExecutionTracker",
            "State changed: $fromState -> $toState"
        )

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

        val normalizedSessionId =
            normalizeSessionId(sessionId)

        val event =
            ExecutionEvent.ActionDispatched(
                normalizedSessionId,
                actionType,
                targetId,
                targetText
            )

        synchronized(sessionLock) {

            val timeline:
                MutableList<ExecutionEvent> =
                getOrCreateSessionEvents(
                    normalizedSessionId
                )

            timeline.add(event)
        }

        logger.debug(
            "ExecutionTracker",
            "Action dispatched: $actionType | " +
                "targetId=${targetId ?: "none"} | " +
                "targetText=${targetText ?: "none"}"
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

        val normalizedSessionId =
            normalizeSessionId(sessionId)

        val event =
            ExecutionEvent.ActionSucceeded(
                normalizedSessionId,
                actionType,
                resultMessage
            )

        synchronized(sessionLock) {

            val timeline:
                MutableList<ExecutionEvent> =
                getOrCreateSessionEvents(
                    normalizedSessionId
                )

            timeline.add(event)
        }

        logger.info(
            "ExecutionTracker",
            "Action succeeded: $actionType | " +
                "message=${resultMessage ?: "none"}"
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

        val normalizedSessionId =
            normalizeSessionId(sessionId)

        val event =
            ExecutionEvent.ActionFailed(
                normalizedSessionId,
                actionType,
                errorCode,
                errorMessage
            )

        synchronized(sessionLock) {

            val timeline:
                MutableList<ExecutionEvent> =
                getOrCreateSessionEvents(
                    normalizedSessionId
                )

            timeline.add(event)
        }

        logger.error(
            "ExecutionTracker",
            "Action failed: $actionType | " +
                "$errorCode | $errorMessage"
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

        val normalizedSessionId =
            normalizeSessionId(sessionId)

        val event =
            ExecutionEvent.SessionError(
                normalizedSessionId,
                errorCode,
                errorMessage
            )

        synchronized(sessionLock) {

            val timeline:
                MutableList<ExecutionEvent> =
                getOrCreateSessionEvents(
                    normalizedSessionId
                )

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
    fun getSessionTimeline(
        sessionId: String
    ): List<ExecutionEvent> {

        val normalizedSessionId =
            normalizeSessionId(sessionId)

        synchronized(sessionLock) {

            val timeline:
                MutableList<ExecutionEvent>? =
                sessions[normalizedSessionId]

            return if (timeline == null) {
                emptyList()
            } else {
                timeline.toList()
            }
        }
    }

    /**
     * Returns all known session IDs.
     */
    fun getAllSessionIds(): Set<String> {

        synchronized(sessionLock) {
            return sessions.keys.toSet()
        }
    }

    /**
     * Returns the currently active session ID.
     */
    fun getActiveSessionId(): String? {
        return activeSessionId
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

        val normalizedSessionId =
            normalizeSessionId(sessionId)

        synchronized(sessionLock) {

            sessions.remove(
                normalizedSessionId
            )

            if (
                activeSessionId ==
                normalizedSessionId
            ) {
                activeSessionId = null
            }
        }

        logger.info(
            "ExecutionTracker",
            "Session cleared: $normalizedSessionId"
        )
    }

    /**
     * Removes all session timelines.
     */
    fun clearAllSessions() {

        synchronized(sessionLock) {

            sessions.clear()

            activeSessionId =
                null
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
     * Normalizes and validates a session ID.
     */
    private fun normalizeSessionId(
        sessionId: String
    ): String {

        val normalized =
            sessionId.trim()

        require(
            normalized.isNotEmpty()
        ) {
            "sessionId must not be blank"
        }

        return normalized
    }

    /**
     * Returns an existing timeline or creates one.
     *
     * IMPORTANT:
     * This method must only be called while sessionLock
     * is already held.
     */
    private fun getOrCreateSessionEvents(
        sessionId: String
    ): MutableList<ExecutionEvent> {

        val existing:
            MutableList<ExecutionEvent>? =
            sessions[sessionId]

        if (existing != null) {
            return existing
        }

        val created:
            MutableList<ExecutionEvent> =
            mutableListOf()

        sessions[sessionId] =
            created

        return created
    }

    // ========================================================================
    // ACTION ID
    // ========================================================================

    companion object {

        private val actionCounter:
            AtomicLong =
            AtomicLong(0L)

        /**
         * Generates a unique action ID.
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
