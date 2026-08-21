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

    private val sessions =
        ConcurrentHashMap<String, MutableList<ExecutionEvent>>()

    private val sessionLock = Any()

    @Volatile
    private var activeSessionId: String? = null

    // ========================================================================
    // SESSION LIFECYCLE
    // ========================================================================

    fun startSession(
        sessionId: String
    ): ExecutionEvent.SessionStarted {

        val normalizedSessionId = sessionId.trim()

        require(normalizedSessionId.isNotEmpty()) {
            "sessionId must not be blank"
        }

        synchronized(sessionLock) {

            val existingSession = activeSessionId

            if (existingSession != null) {
                if (existingSession == normalizedSessionId) {
                    logger.warn(
                        "ExecutionTracker",
                        "Session already active: $normalizedSessionId"
                    )

                    return getSessionEvents(normalizedSessionId)
                        .firstOrNull {
                            it is ExecutionEvent.SessionStarted
                        } as? ExecutionEvent.SessionStarted
                        ?: ExecutionEvent.SessionStarted(
                            normalizedSessionId
                        )
                }

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

            sessions[normalizedSessionId] =
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

    fun stopSession(
        sessionId: String
    ): ExecutionEvent.SessionStopped {

        val normalizedSessionId =
            sessionId.trim()

        require(normalizedSessionId.isNotEmpty()) {
            "sessionId must not be blank"
        }

        synchronized(sessionLock) {

            val event =
                ExecutionEvent.SessionStopped(
                    normalizedSessionId
                )

            getSessionEvents(normalizedSessionId)
                .add(event)

            if (activeSessionId == normalizedSessionId) {
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
    // STATE TRANSITION
    // ========================================================================

    fun recordStateTransition(
        sessionId: String,
        fromState: String,
        toState: String
    ): ExecutionEvent.WorkflowStateChanged {

        val normalizedSessionId =
            sessionId.trim()

        val event =
            ExecutionEvent.WorkflowStateChanged(
                normalizedSessionId,
                fromState,
                toState
            )

        synchronized(sessionLock) {
            getSessionEvents(normalizedSessionId)
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

        val normalizedSessionId =
            sessionId.trim()

        val event =
            ExecutionEvent.ActionDispatched(
                normalizedSessionId,
                actionType,
                targetId,
                targetText
            )

        synchronized(sessionLock) {
            getSessionEvents(normalizedSessionId)
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

        val normalizedSessionId =
            sessionId.trim()

        val event =
            ExecutionEvent.ActionSucceeded(
                normalizedSessionId,
                actionType,
                resultMessage
            )

        synchronized(sessionLock) {
            getSessionEvents(normalizedSessionId)
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

        val normalizedSessionId =
            sessionId.trim()

        val event =
            ExecutionEvent.ActionFailed(
                normalizedSessionId,
                actionType,
                errorCode,
                errorMessage
            )

        synchronized(sessionLock) {
            getSessionEvents(normalizedSessionId)
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

        val normalizedSessionId =
            sessionId.trim()

        val event =
            ExecutionEvent.SessionError(
                normalizedSessionId,
                errorCode,
                errorMessage
            )

        synchronized(sessionLock) {
            getSessionEvents(normalizedSessionId)
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

        return synchronized(sessionLock) {
            sessions[sessionId]
                ?.toList()
                ?: emptyList()
        }
    }

    fun getAllSessionIds(): Set<String> {
        return synchronized(sessionLock) {
            sessions.keys.toSet()
        }
    }

    fun getActiveSessionId(): String? {
        return activeSessionId
    }

    // ========================================================================
    // MAINTENANCE
    // ========================================================================

    fun clearSession(
        sessionId: String
    ) {

        synchronized(sessionLock) {

            sessions.remove(sessionId)

            if (activeSessionId == sessionId) {
                activeSessionId = null
            }
        }

        logger.info(
            "ExecutionTracker",
            "Session cleared: $sessionId"
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

    private fun getSessionEvents(
        sessionId: String
    ): MutableList<ExecutionEvent> {

        return sessions[sessionId]
            ?: throw IllegalStateException(
                "Session not found: $sessionId"
            )
    }

    companion object {

        private val actionCounter =
            AtomicLong(0L)

        @Suppress("unused")
        fun nextActionId(): String {
            return "action-" +
                System.currentTimeMillis() +
                "-" +
                actionCounter.incrementAndGet()
        }
    }
}
