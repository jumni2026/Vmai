package com.vmax.workflow

import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.common.Result
import com.vmax.runtime.ExecutionTracker

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ActionOrchestrator.kt
 *
 * Bridge between WorkflowController and ActionExecutor.
 *
 * Responsibilities:
 * - Receives high-level action commands.
 * - Creates platform-independent ActionRequest objects.
 * - Dispatches requests to ActionExecutor.
 * - Records dispatch/success/failure in ExecutionTracker.
 *
 * Architecture:
 *
 * WorkflowController
 *        ↓
 * ActionOrchestrator
 *        ↓
 * ActionExecutor
 *        ↓
 * AndroidActionExecutor
 *
 * Rules:
 * - No Android dependencies.
 * - No IRCTC-specific logic.
 * - No UI-node discovery.
 * - No business logic.
 * - SCROLL targetText represents direction.
 */
class ActionOrchestrator(
    private val actionExecutor: ActionExecutor,
    private val executionTracker: ExecutionTracker
) {

    /**
     * Executes a CLICK action.
     */
    fun click(
        targetId: String,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.CLICK,
            targetId = targetId
        )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    /**
     * Executes a TAP action.
     */
    fun tap(
        targetId: String,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.TAP,
            targetId = targetId
        )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    /**
     * Executes a SET_TEXT action.
     */
    fun setText(
        targetId: String,
        text: String,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.SET_TEXT,
            targetId = targetId,
            text = text
        )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    /**
     * Executes a CLEAR_TEXT action.
     */
    fun clearText(
        targetId: String,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.CLEAR_TEXT,
            targetId = targetId
        )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    /**
     * Executes a SCROLL action.
     *
     * Contract:
     * targetText = direction
     *
     * The direction is NOT treated as a UI-node text selector.
     */
    fun scroll(
        direction: String,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.SCROLL,
            targetText = direction
        )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    /**
     * Executes a WAIT action.
     */
    fun wait(
        durationMs: Long,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.WAIT,
            durationMs = durationMs
        )

        return dispatchAndTrack(
            request = request,
            sessionId = sessionId
        )
    }

    /**
     * Central dispatch and tracking path.
     *
     * 1. Records action dispatch.
     * 2. Executes the ActionRequest.
     * 3. Records success or failure.
     * 4. Returns the original execution result unchanged.
     */
    private fun dispatchAndTrack(
        request: ActionExecutor.ActionRequest,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        executionTracker.recordActionDispatched(
            sessionId = sessionId,
            actionType = request.type,
            targetId = request.targetId,
            targetText = request.targetText
        )

        val result = actionExecutor.executeAction(request)

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

        return result
    }
}
