package com.vmax.workflow

import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6.1
 *
 * Bridge between WorkflowController and ActionExecutor.
 * Platform-agnostic - No Android dependencies.
 */
class ActionOrchestrator(
    private val actionExecutor: ActionExecutor,
    private val executionTracker: ExecutionTracker
) {

    fun click(
        targetId: String? = null,
        sessionId: String,
        coordinates: Pair<Int, Int>? = null
    ): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.CLICK,
            targetId = targetId,
            coordinates = coordinates
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun tap(
        targetId: String? = null,
        sessionId: String,
        coordinates: Pair<Int, Int>? = null
    ): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.TAP,
            targetId = targetId,
            coordinates = coordinates
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun setText(
        targetId: String? = null,
        text: String,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.SET_TEXT,
            targetId = targetId,
            text = text
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun clearText(
        targetId: String? = null,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.CLEAR_TEXT,
            targetId = targetId
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun scroll(
        direction: String,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.SCROLL,
            targetText = direction
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun wait(
        durationMs: Long,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.WAIT,
            durationMs = durationMs
        )
        return dispatchAndTrack(request, sessionId)
    }

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
