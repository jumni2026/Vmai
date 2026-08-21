package com.vmax.workflow

import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6.1
 *
 * File: ActionOrchestrator.kt
 *
 * Responsibility:
 * - Bridge Workflow layer with ActionExecutor.
 * - Build platform-independent ActionRequest objects.
 * - Dispatch actions to ActionExecutor.
 * - Record dispatch/success/failure through ExecutionTracker.
 *
 * IMPORTANT:
 * - No Android dependencies.
 * - No direct UI manipulation.
 * - Execution is delegated to ActionExecutor.
 * - Tracking failures must not change the actual action result.
 */
class ActionOrchestrator(
    private val actionExecutor: ActionExecutor,
    private val executionTracker: ExecutionTracker
) {

    // ========================================================================
    // CLICK
    // ========================================================================

    fun click(
        targetId: String? = null,
        sessionId: String,
        coordinates: Pair<Int, Int>? = null
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val request =
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.CLICK,
                targetId = targetId,
                coordinates = coordinates
            )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    // ========================================================================
    // TAP
    // ========================================================================

    fun tap(
        targetId: String? = null,
        sessionId: String,
        coordinates: Pair<Int, Int>? = null
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val request =
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.TAP,
                targetId = targetId,
                coordinates = coordinates
            )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    // ========================================================================
    // SET TEXT
    // ========================================================================

    fun setText(
        targetId: String? = null,
        text: String,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val safeText =
            text.trim()

        val request =
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SET_TEXT,
                targetId = targetId,
                text = safeText
            )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    // ========================================================================
    // CLEAR TEXT
    // ========================================================================

    fun clearText(
        targetId: String? = null,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val request =
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.CLEAR_TEXT,
                targetId = targetId
            )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    // ========================================================================
    // SCROLL
    // ========================================================================

    fun scroll(
        direction: String,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val safeDirection =
            direction.trim()

        val request =
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SCROLL,
                targetText = safeDirection
            )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    // ========================================================================
    // WAIT
    // ========================================================================

    fun wait(
        durationMs: Long,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val safeDuration =
            durationMs.coerceAtLeast(0L)

        val request =
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.WAIT,
                durationMs = safeDuration
            )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    // ========================================================================
    // DISPATCH + TRACK
    // ========================================================================

    /**
     * Executes one action and records its lifecycle.
     *
     * Tracking is observational:
     * a tracking failure must never replace or corrupt
     * the actual ActionExecutor result.
     */
    private fun dispatchAndTrack(
        request: ActionExecutor.ActionRequest,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val safeSessionId =
            sessionId.trim()

        /*
         * Dispatch is recorded before execution.
         *
         * Execution must still proceed even if tracking fails.
         */
        recordDispatchSafely(
            sessionId = safeSessionId,
            request = request
        )

        /*
         * ActionExecutor remains the single authority responsible
         * for actual action execution and its Result contract.
         */
        val result =
            actionExecutor.executeAction(
                request
            )

        /*
         * Record the outcome without allowing an
         * ExecutionTracker failure to modify the result.
         */
        recordResultSafely(
            sessionId = safeSessionId,
            request = request,
            result = result
        )

        return result
    }

    // ========================================================================
    // SAFE DISPATCH TRACKING
    // ========================================================================

    private fun recordDispatchSafely(
        sessionId: String,
        request: ActionExecutor.ActionRequest
    ) {

        if (sessionId.isBlank()) {
            return
        }

        try {

            executionTracker.recordActionDispatched(
                sessionId = sessionId,
                actionType = request.type,
                targetId = request.targetId,
                targetText = request.targetText
            )

        } catch (_: Throwable) {

            /*
             * ExecutionTracker is observational.
             *
             * A history/metrics failure must never prevent
             * ActionExecutor from receiving the request.
             */
        }
    }

    // ========================================================================
    // SAFE RESULT TRACKING
    // ========================================================================

    private fun recordResultSafely(
        sessionId: String,
        request: ActionExecutor.ActionRequest,
        result: Result<ActionExecutor.ActionResult, ActionError>
    ) {

        if (sessionId.isBlank()) {
            return
        }

        try {

            when (result) {

                is Result.Success -> {

                    executionTracker.recordActionSucceeded(
                        sessionId = sessionId,
                        actionType = request.type,
                        resultMessage = result.data.message
                    )
                }

                is Result.Error -> {

                    executionTracker.recordActionFailed(
                        sessionId = sessionId,
                        actionType = request.type,
                        errorCode = result.error.code,
                        errorMessage = result.error.message
                    )
                }
            }

        } catch (_: Throwable) {

            /*
             * Tracking failure must never replace
             * the original ActionExecutor result.
             */
        }
    }
}
