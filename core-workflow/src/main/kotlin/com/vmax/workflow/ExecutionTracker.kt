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
 * - Track workflow sessions.
 * - Track workflow state transitions.
 * - Track dispatched actions.
 * - Track successful actions.
 * - Track failed actions.
 * - Track session errors.
 * - Maintain in-memory execution timelines.
 *
 * This class:
 * - Has no Android dependency.
 * - Does not execute actions.
 * - Does not retry actions.
 * - Does not perform recovery.
 * - Does not persist data.
 * - Does not own metrics.
 * - Does not own ExecutionRecorder.
 */
class ExecutionTracker(
    private val logger: Logger
) {

    // ========================================================================
    // STORAGE
    // ========================================================================

    private val sessions:
        ConcurrentHashMap<String, MutableList<ExecutionEvent>> =
        ConcurrentHashMap()

    private val sessionLock: Any =
        Any()

    @Volatile
    private var activeSessionId: String? =
        null

    // ========================================================================
    // SESSION START
    // ========================================================================

    fun startSession(
        sessionId: String
    ): ExecutionEvent.SessionStarted {

        val id = sessionId.trim()

        require(id.isNotEmpty()) {
            "sessionId must not be blank"
        }

        synchronized(sessionLock) {

            val activeId = activeSessionId

            if (activeId != null) {

                if (activeId == id) {

                    logger.warn(
                        "ExecutionTracker",
                        "Session already active: $id"
                    )

                    val existing =
                        sessions[id]

                    if (existing != null) {

                        for (event in existing) {

                            if (
                                event is
                                ExecutionEvent.SessionStarted
                            ) {
                                return event
                            }
                        }
                    }

                    val fallback =
                        ExecutionEvent.SessionStarted(id)

                    if (existing != null) {
                        existing.add(fallback)
                    }

                    return fallback
                }

                logger.warn(
                    "ExecutionTracker",
                    "Session start ignored. Active session: $activeId"
                )

                return ExecutionEvent.SessionStarted(id)
            }

            val event =
                ExecutionEvent.SessionStarted(id)

            val timeline =
                mutableListOf<ExecutionEvent>()

            timeline.add(event)

            sessions[id] =
                timeline

            activeSessionId =
                id

            logger.info(
                "ExecutionTracker",
                "Session started: $id"
            )

            return event
        }
    }

    // ========================================================================
    // SESSION STOP
    // ========================================================================

    fun stopSession(
        sessionId: String
    ): ExecutionEvent.SessionStopped {

        val id = sessionId.trim()

        require(id.isNotEmpty()) {
            "sessionId must not be blank"
        }

        synchronized(sessionLock) {

            val timeline =
                sessions[id]
                    ?: throw IllegalStateException(
                        "Session not found: $id"
                    )

            val event =
                ExecutionEvent.SessionStopped(id)

            timeline.add(event)

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
    // STATE TRANSITION
    // ========================================================================

    fun recordStateTransition(
        sessionId: String,
        fromState: String,
        toState: String
    ): ExecutionEvent.WorkflowStateChanged {

        val id = sessionId.trim()

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

            val timeline =
                sessions[id]
                    ?: throw IllegalStateException(
                        "Session not found: $id"
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

        val id = sessionId.trim()

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

            val timeline =
                sessions[id]
                    ?: throw IllegalStateException(
                        "Session not found: $id"
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

        val id = sessionId.trim()

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

            val timeline =
                sessions[id]
                    ?: throw IllegalStateException(
                        "Session not found: $id"
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

        val id = sessionId.trim()

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

            val timeline =
                sessions[id]
                    ?: throw IllegalStateException(
                        "Session not found: $id"
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

        val id = sessionId.trim()

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

            val timeline =
                sessions[id]
                    ?: throw IllegalStateException(
                        "Session not found: $id"
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
    // READ
    // ========================================================================

    fun getSessionTimeline(
        sessionId: String
    ): List<ExecutionEvent> {

        val id = sessionId.trim()

        if (id.isEmpty()) {
            return emptyList()
        }

        synchronized(sessionLock) {

            val timeline =
                sessions[id]

            return if (timeline == null) {
                emptyList()
            } else {
                timeline.toList()
            }
        }
    }

    fun getAllSessionIds(): Set<String> {

        synchronized(sessionLock) {
            return sessions.keys.toSet()
        }
    }

    fun getActiveSessionId(): String? {
        return activeSessionId
    }

    fun hasSession(
        sessionId: String
    ): Boolean {

        val id = sessionId.trim()

        if (id.isEmpty()) {
            return false
        }

        synchronized(sessionLock) {
            return sessions.containsKey(id)
        }
    }

    fun isSessionActive(
        sessionId: String
    ): Boolean {

        val id = sessionId.trim()

        if (id.isEmpty()) {
            return false
        }

        return activeSessionId == id
    }

    // ========================================================================
    // CLEAR ONE
    // ========================================================================

    fun clearSession(
        sessionId: String
    ) {

        val id = sessionId.trim()

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

    // ========================================================================
    // CLEAR ALL
    // ========================================================================

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

        @JvmStatic
        fun nextActionId(): String {

            return "action-" +
                System.currentTimeMillis() +
                "-" +
                actionCounter.incrementAndGet()
        }
    }
}
