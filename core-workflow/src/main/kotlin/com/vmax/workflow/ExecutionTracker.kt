package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.common.Logger

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionTracker.kt
 *
 * Central recording system for tracking every step
 * of an execution session.
 *
 * Responsibilities:
 * - Session lifecycle tracking
 * - Workflow state-transition tracking
 * - Action dispatch tracking
 * - Action success/failure tracking
 * - Session error tracking
 * - Session timeline retrieval
 *
 * Platform-independent:
 * - No Android dependencies
 * - No Compose dependencies
 * - No coroutine dependency
 *
 * IMPORTANT:
 * This class records execution history only.
 * It does NOT execute actions.
 * It does NOT perform retries.
 * It does NOT change workflow architecture.
 */
class ExecutionTracker(
    private val logger: Logger
) {

    /**
     * All execution events grouped by session ID.
     *
     * Access to this map is protected by sessionLock.
     */
    private val sessions =
        mutableMapOf<String, MutableList<ExecutionEvent>>()

    /**
     * Protects session creation, modification and reads.
     */
    private val sessionLock = Any()

    // ============================================================
    // Session Lifecycle
    // ============================================================

    /**
     * Starts a new execution session.
     *
     * If the session ID already exists, its previous timeline
     * is replaced with a fresh session timeline.
     */
    fun startSession(
        sessionId: String
    ): ExecutionEvent.SessionStarted {

        require(sessionId.isNotBlank()) {
            "sessionId must not be blank"
        }

        val event = ExecutionEvent.SessionStarted(sessionId)

        synchronized(sessionLock) {
            sessions[sessionId] = mutableListOf(event)
        }

        logger.info(
            "ExecutionTracker",
            "Session started: $sessionId"
        )

        return event
    }

    /**
     * Stops an existing execution session.
     */
    fun stopSession(
        sessionId: String
    ): ExecutionEvent.SessionStopped {

        val event = ExecutionEvent.SessionStopped(sessionId)

        synchronized(sessionLock) {
            getSessionEventsLocked(sessionId).add(event)
        }

        logger.info(
            "ExecutionTracker",
            "Session stopped: $sessionId"
        )

        return event
    }

    /**
     * Records a session-level error.
     */
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

        synchronized(sessionLock) {
            getSessionEventsLocked(sessionId).add(event)
        }

        logger.error(
            "ExecutionTracker",
            "Session error: $errorCode -> $errorMessage"
        )

        return event
    }

    // ============================================================
    // Workflow State Tracking
    // ============================================================

    /**
     * Records a workflow state transition.
     */
    fun recordStateTransition(
        sessionId: String,
        fromState: String,
        toState: String
    ): ExecutionEvent.WorkflowStateChanged {

        val event = ExecutionEvent.WorkflowStateChanged(
            sessionId,
            fromState,
            toState
        )

        synchronized(sessionLock) {
            getSessionEventsLocked(sessionId).add(event)
        }

        logger.debug(
            "ExecutionTracker",
            "State changed: $fromState -> $toState"
        )

        return event
    }

    // ============================================================
    // Action Lifecycle
    // ============================================================

    /**
     * Records that an action has been dispatched.
     */
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

        synchronized(sessionLock) {
            getSessionEventsLocked(sessionId).add(event)
        }

        logger.debug(
            "ExecutionTracker",
            "Action dispatched: $actionType"
        )

        return event
    }

    /**
     * Records successful completion of an action.
     */
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

        synchronized(sessionLock) {
            getSessionEventsLocked(sessionId).add(event)
        }

        logger.info(
            "ExecutionTracker",
            "Action succeeded: $actionType"
        )

        return event
    }

    /**
     * Records failed execution of an action.
     *
     * Tracker only records the failure.
     * Recovery/failure decisions belong to the orchestration layer.
     */
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

        synchronized(sessionLock) {
            getSessionEventsLocked(sessionId).add(event)
        }

        logger.error(
            "ExecutionTracker",
            "Action failed: $actionType -> $errorCode: $errorMessage"
        )

        return event
    }

    // ============================================================
    // Session Queries
    // ============================================================

    /**
     * Returns an immutable snapshot of the session timeline.
     *
     * The returned list cannot modify the internal session history.
     */
    fun getSessionTimeline(
        sessionId: String
    ): List<ExecutionEvent> {

        synchronized(sessionLock) {
            return sessions[sessionId]
                ?.toList()
                ?: emptyList()
        }
    }

    /**
     * Returns a snapshot of all known session IDs.
     */
    fun getAllSessionIds(): Set<String> {

        synchronized(sessionLock) {
            return sessions.keys.toSet()
        }
    }

    /**
     * Returns the number of currently stored sessions.
     */
    fun getSessionCount(): Int {

        synchronized(sessionLock) {
            return sessions.size
        }
    }

    /**
     * Checks whether a session exists.
     */
    fun hasSession(
        sessionId: String
    ): Boolean {

        synchronized(sessionLock) {
            return sessions.containsKey(sessionId)
        }
    }

    /**
     * Clears all recorded session history.
     */
    fun clearAllSessions() {

        synchronized(sessionLock) {
            sessions.clear()
        }

        logger.warn(
            "ExecutionTracker",
            "All sessions cleared"
        )
    }

    // ============================================================
    // Internal Helpers
    // ============================================================

    /**
     * Must only be called while sessionLock is already held.
     */
    private fun getSessionEventsLocked(
        sessionId: String
    ): MutableList<ExecutionEvent> {

        return sessions[sessionId]
            ?: throw IllegalStateException(
                "Session $sessionId not found"
            )
    }
}
