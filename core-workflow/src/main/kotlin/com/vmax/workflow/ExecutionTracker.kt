package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import com.vmax.common.Logger
import java.util.concurrent.ConcurrentHashMap

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionTracker.kt
 *
 * Central recording system for tracking every step of an execution session.
 *
 * Responsibilities:
 * - Maintain execution session timelines.
 * - Record workflow state transitions.
 * - Record action dispatch/success/failure.
 * - Record session lifecycle events.
 * - Record security/session errors.
 * - Expose immutable timeline snapshots.
 *
 * Architecture:
 * - Uses canonical com.vmax.action.ExecutionEvent.
 * - Uses canonical ActionExecutor.ActionType.
 * - Does NOT execute actions.
 * - Does NOT contain retry logic.
 * - Does NOT contain Android dependencies.
 * - Does NOT own WorkflowState.
 * - Does NOT make workflow decisions.
 *
 * Thread-safety:
 * - Session storage uses ConcurrentHashMap.
 * - Each session timeline uses a synchronized list.
 */
class ExecutionTracker(
    private val logger: Logger
) {

    // ========================================================================
    // SESSION STORAGE
    // ========================================================================

    /**
     * Session timelines.
     *
     * ConcurrentHashMap protects session creation/removal.
     * Each individual timeline is synchronized because events may be
     * recorded from different execution paths.
     */
    private val sessions =
        ConcurrentHashMap<String, MutableList<ExecutionEvent>>()

    // ========================================================================
    // SESSION LIFECYCLE
    // ========================================================================

    /**
     * Starts a new execution session.
     *
     * If the same sessionId already exists, its previous timeline is
     * intentionally replaced. A sessionId is expected to represent
     * one execution session only.
     */
    fun startSession(
        sessionId: String
    ): ExecutionEvent.SessionStarted {

        val safeSessionId =
            sessionId.trim()

        require(safeSessionId.isNotBlank()) {
            "Session ID cannot be blank."
        }

        val event =
            ExecutionEvent.SessionStarted(
                safeSessionId
            )

        val timeline =
            java.util.Collections.synchronizedList(
                mutableListOf<ExecutionEvent>()
            )

        timeline.add(event)

        sessions[safeSessionId] =
            timeline

        logger.info(
            "ExecutionTracker",
            "Session started: $safeSessionId"
        )

        return event
    }

    /**
     * Stops an existing execution session.
     */
    fun stopSession(
        sessionId: String
    ): ExecutionEvent.SessionStopped {

        val safeSessionId =
            sessionId.trim()

        require(safeSessionId.isNotBlank()) {
            "Session ID cannot be blank."
        }

        val timeline =
            getSessionEvents(safeSessionId)

        val event =
            ExecutionEvent.SessionStopped(
                safeSessionId
            )

        synchronized(timeline) {
            timeline.add(event)
        }

        logger.info(
            "ExecutionTracker",
            "Session stopped: $safeSessionId"
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

        val safeSessionId =
            sessionId.trim()

        if (safeSessionId.isBlank()) {
            throw IllegalArgumentException(
                "Session ID cannot be blank."
            )
        }

        val safeErrorCode =
            errorCode.trim()

        val safeErrorMessage =
            errorMessage.trim()

        val event =
            ExecutionEvent.SessionError(
                safeSessionId,
                safeErrorCode,
                safeErrorMessage
            )

        val timeline =
            getSessionEvents(safeSessionId)

        synchronized(timeline) {
            timeline.add(event)
        }

        logger.error(
            "ExecutionTracker",
            "Session error: $safeErrorCode -> $safeErrorMessage"
        )

        return event
    }

    // ========================================================================
    // STATE TRANSITIONS
    // ========================================================================

    /**
     * Records a workflow state transition.
     *
     * State ownership remains with WorkflowController.
     * Tracker only records the transition.
     */
    fun recordStateTransition(
        sessionId: String,
        fromState: String,
        toState: String
    ): ExecutionEvent.WorkflowStateChanged {

        val safeSessionId =
            sessionId.trim()

        val safeFromState =
            fromState.trim()

        val safeToState =
            toState.trim()

        val event =
            ExecutionEvent.WorkflowStateChanged(
                safeSessionId,
                safeFromState,
                safeToState
            )

        val timeline =
            getSessionEvents(safeSessionId)

        synchronized(timeline) {
            timeline.add(event)
        }

        logger.debug(
            "ExecutionTracker",
            "State changed: $safeFromState -> $safeToState"
        )

        return event
    }

    // ========================================================================
    // ACTION LIFECYCLE
    // ========================================================================

    /**
     * Records that an action was dispatched.
     *
     * This method does NOT execute the action.
     */
    fun recordActionDispatched(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        targetId: String?,
        targetText: String?
    ): ExecutionEvent.ActionDispatched {

        val safeSessionId =
            sessionId.trim()

        val event =
            ExecutionEvent.ActionDispatched(
                safeSessionId,
                actionType,
                targetId?.trim(),
                targetText?.trim()
            )

        val timeline =
            getSessionEvents(safeSessionId)

        synchronized(timeline) {
            timeline.add(event)
        }

        logger.debug(
            "ExecutionTracker",
            "Action dispatched: $actionType"
        )

        return event
    }

    /**
     * Records successful action execution.
     */
    fun recordActionSucceeded(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        resultMessage: String?
    ): ExecutionEvent.ActionSucceeded {

        val safeSessionId =
            sessionId.trim()

        val event =
            ExecutionEvent.ActionSucceeded(
                safeSessionId,
                actionType,
                resultMessage?.trim()
            )

        val timeline =
            getSessionEvents(safeSessionId)

        synchronized(timeline) {
            timeline.add(event)
        }

        logger.info(
            "ExecutionTracker",
            "Action succeeded: $actionType"
        )

        return event
    }

    /**
     * Records failed action execution.
     *
     * Failure is recorded only.
     * Workflow recovery/failure decisions remain outside this class.
     */
    fun recordActionFailed(
        sessionId: String,
        actionType: ActionExecutor.ActionType,
        errorCode: String,
        errorMessage: String
    ): ExecutionEvent.ActionFailed {

        val safeSessionId =
            sessionId.trim()

        val safeErrorCode =
            errorCode.trim()

        val safeErrorMessage =
            errorMessage.trim()

        val event =
            ExecutionEvent.ActionFailed(
                safeSessionId,
                actionType,
                safeErrorCode,
                safeErrorMessage
            )

        val timeline =
            getSessionEvents(safeSessionId)

        synchronized(timeline) {
            timeline.add(event)
        }

        logger.error(
            "ExecutionTracker",
            "Action failed: $actionType -> " +
                "$safeErrorCode: $safeErrorMessage"
        )

        return event
    }

    // ========================================================================
    // SESSION QUERIES
    // ========================================================================

    /**
     * Returns a snapshot of the session timeline.
     *
     * Caller receives a new immutable list and cannot mutate the
     * internal session storage.
     */
    fun getSessionTimeline(
        sessionId: String
    ): List<ExecutionEvent> {

        val safeSessionId =
            sessionId.trim()

        val timeline =
            sessions[safeSessionId]
                ?: return emptyList()

        synchronized(timeline) {
            return timeline.toList()
        }
    }

    /**
     * Returns all known session IDs as a snapshot.
     */
    fun getAllSessionIds(): Set<String> {
        return sessions.keys.toSet()
    }

    /**
     * Returns whether a session exists.
     */
    fun hasSession(
        sessionId: String
    ): Boolean {

        return sessions.containsKey(
            sessionId.trim()
        )
    }

    /**
     * Returns the number of tracked sessions.
     */
    fun getSessionCount(): Int {
        return sessions.size
    }

    /**
     * Returns the number of events in a session.
     */
    fun getEventCount(
        sessionId: String
    ): Int {

        val safeSessionId =
            sessionId.trim()

        val timeline =
            sessions[safeSessionId]
                ?: return 0

        synchronized(timeline) {
            return timeline.size
        }
    }

    // ========================================================================
    // STORAGE MANAGEMENT
    // ========================================================================

    /**
     * Removes one session from in-memory tracking.
     *
     * This does not affect persisted execution history.
     */
    fun clearSession(
        sessionId: String
    ) {

        val safeSessionId =
            sessionId.trim()

        if (safeSessionId.isBlank()) {
            return
        }

        sessions.remove(
            safeSessionId
        )

        logger.debug(
            "ExecutionTracker",
            "Session cleared: $safeSessionId"
        )
    }

    /**
     * Clears all in-memory sessions.
     *
     * This does not modify external persistence.
     */
    fun clearAllSessions() {

        sessions.clear()

        logger.warn(
            "ExecutionTracker",
            "All sessions cleared"
        )
    }

    // ========================================================================
    // INTERNAL
    // ========================================================================

    /**
     * Returns the internal timeline for an existing session.
     *
     * Recording methods intentionally fail fast when a session does not
     * exist. This prevents silent creation of orphaned action events.
     */
    private fun getSessionEvents(
        sessionId: String
    ): MutableList<ExecutionEvent> {

        val safeSessionId =
            sessionId.trim()

        return sessions[safeSessionId]
            ?: throw IllegalStateException(
                "Session $safeSessionId not found."
            )
    }
}
