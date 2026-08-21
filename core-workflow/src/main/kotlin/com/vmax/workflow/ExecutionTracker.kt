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
 * - Track action dispatch/success/failure.
 * - Track session errors.
 * - Maintain session execution timelines.
 * - Remain platform independent.
 *
 * IMPORTANT:
 * - No Android dependencies.
 * - No action execution.
 * - No retry logic.
 * - No workflow recovery logic.
 * - No MetricsCollector dependency.
 * - No ExecutionRecorder dependency.
 * - Uses the canonical ExecutionEvent from com.vmax.action.
 */
class ExecutionTracker(
    private val logger: Logger
) {

    // ========================================================================
    // SESSION STORAGE
    // ========================================================================

    private val sessions:
        ConcurrentHashMap<String, MutableList<ExecutionEvent>> =
        ConcurrentHashMap()

    /**
     * Single lock for session state and timeline mutation.
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

    fun startSession(
        sessionId: String
    ): ExecutionEvent.SessionStarted {

        val id =
            sessionId.trim()

        require(id.isNotEmpty()) {
            "sessionId must not be blank"
        }

        synchronized(sessionLock) {

            val active =
                activeSessionId

            /*
             * Same session is already active.
             */
            if (active == id) {

                logger.warn(
                    "ExecutionTracker",
                    "Session already active: $id"
                )

                return sessions[id]
                    ?.firstOrNull {
                        it is ExecutionEvent.SessionStarted
                    }
                    as? ExecutionEvent.SessionStarted
                    ?: ExecutionEvent.SessionStarted(id)
            }

            /*
             * Another session is already active.
             */
            if (active != null) {

                logger.warn(
                    "ExecutionTracker",
                    "Session start ignored. " +
                        "Active session: $active"
                )

                return ExecutionEvent.SessionStarted(id)
            }

            val event =
                ExecutionEvent.SessionStarted(id)

            sessions[id] =
                mutableListOf(event)

            activeSessionId =
                id

            logger.info(
                "ExecutionTracker",
                "Session started: $id"
            )

            return event
        }
    }

    fun stopSession(
        sessionId: String
    ): ExecutionEvent.SessionStopped {

        val id =
            sessionId.trim()

        require(id.isNotEmpty()) {
            "sessionId must not be blank"
        }

        synchronized(sessionLock) {

            val event =
                ExecutionEvent.SessionStopped(id)

            getSessionEvents(id)
                .add(event)

            if (activeSessionId == id) {
                activeSessionId = null
            }

            logger.info(
                "ExecutionTracker",
                "Session stopped: $id"
            )

            return event
        }
    }

    // ========================================================================
    // WORKFLOW STATE
    // ========================================================================

    fun recordStateTransition(
        sessionId: String,
        fromState: String,
        toState: String
    ): ExecutionEvent.WorkflowStateChanged {

        val id =
            sessionId.trim()

        require(id.isNotEmpty()) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.WorkflowStateChanged(
                id,
                fromState,
                toState
            )

        synchronized(sessionLock) {
            getSessionEvents(id)
                .add(event)
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

        val id =
            sessionId.trim()

        require(id.isNotEmpty()) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.ActionDispatched(
                id,
                actionType,
                targetId,
                targetText
            )

        synchronized(sessionLock) {
            getSessionEvents(id)
                .add(event)
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

        val id =
            sessionId.trim()

        require(id.isNotEmpty()) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.ActionSucceeded(
                id,
                actionType,
                resultMessage
            )

        synchronized(sessionLock) {
            getSessionEvents(id)
                .add(event)
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

        val id =
            sessionId.trim()

        require(id.isNotEmpty()) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.ActionFailed(
                id,
                actionType,
                errorCode,
                errorMessage
            )

        synchronized(sessionLock) {
            getSessionEvents(id)
                .add(event)
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

        val id =
            sessionId.trim()

        require(id.isNotEmpty()) {
            "sessionId must not be blank"
        }

        val event =
            ExecutionEvent.SessionError(
                id,
                errorCode,
                errorMessage
            )

        synchronized(sessionLock) {
            getSessionEvents(id)
                .add(event)
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

    fun getSessionTimeline(
        sessionId: String
    ): List<ExecutionEvent> {

        val id =
            sessionId.trim()

        if (id.isEmpty()) {
            return emptyList()
        }

        synchronized(sessionLock) {

            val events =
                sessions[id]

            return events?.toList()
                ?: emptyList()
        }
    }

    fun getAllSessionIds(): Set<String> {

        synchronized(sessionLock) {
            return sessions.keys.toSet()
        }
    }

    fun getActiveSessionId(): String? {

        synchronized(sessionLock) {
            return activeSessionId
        }
    }

    fun isSessionActive(
        sessionId: String
    ): Boolean {

        val id =
            sessionId.trim()

        if (id.isEmpty()) {
            return false
        }

        synchronized(sessionLock) {
            return activeSessionId == id
        }
    }

    fun hasSession(
        sessionId: String
    ): Boolean {

        val id =
            sessionId.trim()

        if (id.isEmpty()) {
            return false
        }

        synchronized(sessionLock) {
            return sessions.containsKey(id)
        }
    }

    // ========================================================================
    // MAINTENANCE
    // ========================================================================

    fun clearSession(
        sessionId: String
    ) {

        val id =
            sessionId.trim()

        if (id.isEmpty()) {
            return
        }

        synchronized(sessionLock) {

            sessions.remove(id)

            if (activeSessionId == id) {
                activeSessionId = null
            }
        }

        logger.info(
            "ExecutionTracker",
            "Session cleared: $id"
        )
    }

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
     * Returns the internal mutable timeline.
     *
     * This method must only be called while sessionLock is held.
     */
    private fun getSessionEvents(
        sessionId: String
    ): MutableList<ExecutionEvent> {

        return sessions[sessionId]
            ?: throw IllegalStateException(
                "Session not found: $sessionId"
            )
    }
}
