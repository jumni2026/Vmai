package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.common.Logger
import java.util.concurrent.ConcurrentHashMap

/**
 * VMAX Enterprise v2.6.1
 *
 * File: ExecutionTracker.kt
 *
 * Responsibility:
 * - Track workflow session lifecycle.
 * - Track workflow state transitions.
 * - Track dispatched/succeeded/failed actions.
 * - Track session errors.
 * - Maintain session execution timelines.
 * - Remain platform independent.
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
    // SESSION STORAGE
    // ========================================================================

    /**
     * All execution sessions.
     *
     * ConcurrentHashMap protects the session map itself.
     * Individual timeline mutations are additionally protected by
     * sessionLock so that add/read/clear operations remain consistent.
     */
    private val sessions =
        ConcurrentHashMap<String, MutableList<ExecutionEvent>>()

    /**
     * Lock protecting session timeline mutations and active session state.
     */
    private val sessionLock =
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
     * If another session is active, the new session is rejected
     * by returning a SessionStarted object without registering it.
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

            val existingSession =
                activeSessionId

            // Same session already active.
            if (
                existingSession ==
                normalizedSessionId
            ) {

                logger.warn(
                    "ExecutionTracker",
                    "Session already active: $normalizedSessionId"
                )

                return sessions[
                    normalizedSessionId
                ]
                    ?.firstOrNull {
                        it is ExecutionEvent.SessionStarted
                    }
                    as? ExecutionEvent.SessionStarted
                    ?: ExecutionEvent.SessionStarted(
                        normalizedSessionId
                    )
            }

            // Another session is already active.
            if (
                existingSession != null
            ) {

                logger.warn(
                    "ExecutionTracker",
                    "Session start ignored. " +
                        "Active session: $existingSession"
                )

                return ExecutionEvent.SessionStarted(
                    normalizedSessionId
                )
            }

            val event =
                ExecutionEvent.SessionStarted(
                    normalizedSessionId
                )

            sessions[
                normalizedSessionId
            ] =
                mutableListOf(event)

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
     * Stops an execution session.
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

            val events =
                getSessionEvents(
                    normalizedSessionId
                )

            val event =
                ExecutionEvent.SessionStopped(
                    normalizedSessionId
                )

            events.add(event)

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

            getSessionEvents(
                normalizedSessionId
            ).add(event)
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
     * Records that an action was dispatched.
     *
     * IMPORTANT:
     * Uses the current ActionExecutor.ActionType contract.
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

            getSessionEvents(
                normalizedSessionId
            ).add(event)
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

    /**
     * Records successful action completion.
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

            getSessionEvents(
                normalizedSessionId
            ).add(event)
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

            getSessionEvents(
                normalizedSessionId
            ).add(event)
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

            getSessionEvents(
                normalizedSessionId
            ).add(event)
        }

        logger.error(
            "ExecutionTracker",
            "Session error: " +
                "$errorCode | $errorMessage"
        )

        return event
    }

    // ========================================================================
    // READ APIs
    // ========================================================================

    /**
     * Returns a snapshot of a session timeline.
     *
     * A copy is returned so callers cannot mutate the internal timeline.
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

            return sessions[
                normalizedSessionId
            ]
                ?.toList()
                ?: emptyList()
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

        return synchronized(sessionLock) {
            activeSessionId
        }
    }

    /**
     * Returns true when the supplied session is active.
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

        return synchronized(sessionLock) {

            activeSessionId ==
                normalizedSessionId
        }
    }

    /**
     * Returns true when a session exists in history.
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

        return synchronized(sessionLock) {

            sessions.containsKey(
                normalizedSessionId
            )
        }
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
            "Session cleared: $normalizedSessionId"
        )
    }

    /**
     * Removes all sessions from memory.
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
    // INTERNAL
    // ========================================================================

    /**
     * Returns the mutable internal timeline.
     *
     * MUST only be called while sessionLock is held.
     */
    private fun getSessionEvents(
        sessionId: String
    ): MutableList<ExecutionEvent> {

        return sessions[
            sessionId
        ]
            ?: throw IllegalStateException(
                "Session not found: $sessionId"
            )
    }
}
