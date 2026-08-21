package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.common.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * VMAX Enterprise v2.6.1
 *
 * File:
 * ExecutionTracker.kt
 *
 * Module:
 * core-workflow
 *
 * Responsibility:
 * - Track workflow session lifecycle.
 * - Track workflow state transitions.
 * - Track dispatched actions.
 * - Track successful actions.
 * - Track failed actions.
 * - Track session errors.
 * - Maintain in-memory execution timelines.
 *
 * IMPORTANT:
 * - No Android dependencies.
 * - No action execution.
 * - No retry logic.
 * - No recovery logic.
 * - No parallel execution logic.
 * - No persistence responsibility.
 * - No MetricsCollector dependency.
 * - No ExecutionRecorder dependency.
 * - No duplicate ExecutionEvent.
 * - No workflow decision making.
 */
class ExecutionTracker(
    private val logger: Logger
) {

    // ========================================================================
    // SESSION STORAGE
    // ========================================================================

    /**
     * Session ID -> ordered execution events.
     *
     * ConcurrentHashMap protects the session map.
     * sessionLock protects mutation/read of individual timelines.
     */
    private val sessions:
        ConcurrentHashMap<String, MutableList<ExecutionEvent>> =
        ConcurrentHashMap()

    /**
     * Single lock for timeline operations.
     */
    private val sessionLock =
        Any()

    /**
     * Currently active workflow session.
     */
    @Volatile
    private var activeSessionId:
        String? = null

    // ========================================================================
    // SESSION LIFECYCLE
    // ========================================================================

    /**
     * Starts a workflow session.
     *
     * If the same session is already active, the existing
     * SessionStarted event is returned.
     *
     * If another session is active, the new session is ignored
     * and a SessionStarted event is returned without replacing
     * the active session.
     */
    fun startSession(
        sessionId: String
    ): ExecutionEvent.SessionStarted {

        val normalizedSessionId =
            sessionId.trim()

        require(
            normalizedSessionId.isNotEmpty()
        ) {
            "sessionId must not be blank"
        }

        synchronized(sessionLock) {

            val currentActiveSession =
                activeSessionId

            // ---------------------------------------------------------------
            // Same session already active
            // ---------------------------------------------------------------

            if (
                currentActiveSession ==
                normalizedSessionId
            ) {

                logger.warn(
                    "ExecutionTracker",
                    "Session already active: " +
                        normalizedSessionId
                )

                val existingTimeline =
                    sessions[normalizedSessionId]

                if (existingTimeline != null) {

                    for (event in existingTimeline) {

                        if (
                            event is
                            ExecutionEvent.SessionStarted
                        ) {
                            return event
                        }
                    }
                }

                /*
                 * Defensive fallback.
                 *
                 * This should normally never happen because a session
                 * is created together with its SessionStarted event.
                 */
                return ExecutionEvent.SessionStarted(
                    normalizedSessionId
                )
            }

            // ---------------------------------------------------------------
            // Another session is active
            // ---------------------------------------------------------------

            if (
                currentActiveSession != null
            ) {

                logger.warn(
                    "ExecutionTracker",
                    "Session start ignored. " +
                        "Active session: " +
                        currentActiveSession
                )

                return ExecutionEvent.SessionStarted(
                    normalizedSessionId
                )
            }

            // ---------------------------------------------------------------
            // Create new session
            // ---------------------------------------------------------------

            val event =
                ExecutionEvent.SessionStarted(
                    normalizedSessionId
                )

            val timeline =
                mutableListOf<ExecutionEvent>()

            timeline.add(event)

            sessions[normalizedSessionId] =
                timeline

            activeSessionId =
                normalizedSessionId

            logger.info(
                "ExecutionTracker",
                "Session started: " +
                    normalizedSessionId
            )

            return event
        }
    }

    /**
     * Stops a workflow session.
     */
    fun stopSession(
        sessionId: String
    ): ExecutionEvent.SessionStopped {

        val normalizedSessionId =
            sessionId.trim()

        require(
            normalizedSessionId.isNotEmpty()
        ) {
            "sessionId must not be blank"
        }

        synchronized(sessionLock) {

            val timeline =
                sessions[normalizedSessionId]
                    ?: throw IllegalStateException(
                        "Session not found: " +
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
                "Session stopped: " +
                    normalizedSessionId
            )

            return event
        }
    }

    // ========================================================================
    // WORKFLOW STATE TRANSITION
    // ========================================================================

    /**
     * Records a workflow state transition.
     */
    fun recordStateTransition(
        sessionId: String,
        fromState: String,
        toState: String
    ): ExecutionEvent.WorkflowStateChanged {

        val normalizedSessionId =
            sessionId.trim()

        require(
            normalizedSessionId.isNotEmpty()
        ) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.WorkflowStateChanged(
                normalizedSessionId,
                fromState,
                toState
            )

        synchronized(sessionLock) {

            val timeline =
                sessions[normalizedSessionId]
                    ?: throw IllegalStateException(
                        "Session not found: " +
                            normalizedSessionId
                    )

            timeline.add(event)
        }

        logger.debug(
            "ExecutionTracker",
            "State changed: " +
                "$fromState -> $toState"
        )

        return event
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

        val normalizedSessionId =
            sessionId.trim()

        require(
            normalizedSessionId.isNotEmpty()
        ) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.ActionDispatched(
                normalizedSessionId,
                actionType,
                targetId,
                targetText
            )

        synchronized(sessionLock) {

            val timeline =
                sessions[normalizedSessionId]
                    ?: throw IllegalStateException(
                        "Session not found: " +
                            normalizedSessionId
                    )

            timeline.add(event)
        }

        logger.debug(
            "ExecutionTracker",
            "Action dispatched: " +
                "$actionType | " +
                "targetId=${targetId ?: "none"} | " +
                "targetText=${targetText ?: "none"}"
        )

        return event
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

        val normalizedSessionId =
            sessionId.trim()

        require(
            normalizedSessionId.isNotEmpty()
        ) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.ActionSucceeded(
                normalizedSessionId,
                actionType,
                resultMessage
            )

        synchronized(sessionLock) {

            val timeline =
                sessions[normalizedSessionId]
                    ?: throw IllegalStateException(
                        "Session not found: " +
                            normalizedSessionId
                    )

            timeline.add(event)
        }

        logger.info(
            "ExecutionTracker",
            "Action succeeded: " +
                "$actionType | " +
                "message=${resultMessage ?: "none"}"
        )

        return event
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

        val normalizedSessionId =
            sessionId.trim()

        require(
            normalizedSessionId.isNotEmpty()
        ) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.ActionFailed(
                normalizedSessionId,
                actionType,
                errorCode,
                errorMessage
            )

        synchronized(sessionLock) {

            val timeline =
                sessions[normalizedSessionId]
                    ?: throw IllegalStateException(
                        "Session not found: " +
                            normalizedSessionId
                    )

            timeline.add(event)
        }

        logger.error(
            "ExecutionTracker",
            "Action failed: " +
                "$actionType | " +
                "$errorCode | " +
                errorMessage
        )

        return event
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

        val normalizedSessionId =
            sessionId.trim()

        require(
            normalizedSessionId.isNotEmpty()
        ) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.SessionError(
                normalizedSessionId,
                errorCode,
                errorMessage
            )

        synchronized(sessionLock) {

            val timeline =
                sessions[normalizedSessionId]
                    ?: throw IllegalStateException(
                        "Session not found: " +
                            normalizedSessionId
                    )

            timeline.add(event)
        }

        logger.error(
            "ExecutionTracker",
            "Session error: " +
                "$errorCode | " +
                errorMessage
        )

        return event
    }

    // ========================================================================
    // READ APIs
    // ========================================================================

    /**
     * Returns an immutable snapshot of one session timeline.
     */
    fun getSessionTimeline(
        sessionId: String
    ): List<ExecutionEvent> {

        val normalizedSessionId =
            sessionId.trim()

        if (
            normalizedSessionId.isEmpty()
        ) {
            return emptyList()
        }

        synchronized(sessionLock) {

            val timeline =
                sessions[normalizedSessionId]

            return if (
                timeline == null
            ) {
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
    // SESSION EXISTENCE
    // ========================================================================

    /**
     * Returns true when the specified session exists.
     */
    fun hasSession(
        sessionId: String
    ): Boolean {

        val normalizedSessionId =
            sessionId.trim()

        if (
            normalizedSessionId.isEmpty()
        ) {
            return false
        }

        synchronized(sessionLock) {

            return sessions.containsKey(
                normalizedSessionId
            )
        }
    }

    /**
     * Returns true when the specified session is active.
     */
    fun isSessionActive(
        sessionId: String
    ): Boolean {

        val normalizedSessionId =
            sessionId.trim()

        if (
            normalizedSessionId.isEmpty()
        ) {
            return false
        }

        return activeSessionId ==
            normalizedSessionId
    }

    // ========================================================================
    // MAINTENANCE
    // ========================================================================

    /**
     * Removes one session and its timeline.
     */
    fun clearSession(
        sessionId: String
    ) {

        val normalizedSessionId =
            sessionId.trim()

        if (
            normalizedSessionId.isEmpty()
        ) {
            return
        }

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
            "Session cleared: " +
                normalizedSessionId
        )
    }

    /**
     * Removes all sessions.
     */
    fun clearAllSessions() {

        synchronized(sessionLock) {

            sessions.clear()

            activeSessionId = null
        }

        logger.warn(
            "ExecutionTracker",
            "All execution sessions cleared"
        )
    }

    // ========================================================================
    // ACTION ID
    // ========================================================================

    companion object {

        private val actionCounter =
            AtomicLong(0L)

        /**
         * Generates a unique action ID.
         *
         * Kept as a companion API so existing callers
         * can continue using ExecutionTracker.nextActionId().
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
