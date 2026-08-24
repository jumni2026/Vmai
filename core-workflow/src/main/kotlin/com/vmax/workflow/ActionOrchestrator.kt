package com.vmax.workflow

import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

class ActionOrchestrator(
    private val actionExecutor: ActionExecutor,
    private val executionTracker: ExecutionTracker
) {

    fun dispatchAndTrack(
        request: ActionExecutor.ActionRequest
    ): Result<Unit, ActionError> {

        return try {
            when (val result = actionExecutor.executeAction(request)) {
                is Result.Success -> {
                    executionTracker.recordActionSucceeded(
                        sessionId = "N/A",
                        actionType = request.type,
                        resultMessage = result.data.message
                    )
                    Result.Success(Unit)
                }
                is Result.Failure -> {
                    executionTracker.recordActionFailed(
                        sessionId = "N/A",
                        actionType = request.type,
                        errorCode = "EXECUTION_FAILED",
                        errorMessage = result.error.message
                    )
                    Result.Failure(result.error)
                }
            }
        } catch (t: Throwable) {
            Result.Failure(
                ActionError(
                    code = "ORCHESTRATION_ERROR",
                    message = t.message ?: "Unknown error"
                )
            )
        }
    }

    fun click(targetId: String): Result<Unit, ActionError> {
        return dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.CLICK,
                targetId = targetId
            )
        )
    }

    fun tap(targetId: String): Result<Unit, ActionError> {
        return dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.TAP,
                targetId = targetId
            )
        )
    }

    fun setText(targetId: String, text: String): Result<Unit, ActionError> {
        if (text.isEmpty()) {
            return Result.Failure(
                ActionError(code = "INVALID_REQUEST", message = "Text must not be empty")
            )
        }
        return dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SET_TEXT,
                targetId = targetId,
                text = text
            )
        )
    }

    fun clearText(targetId: String): Result<Unit, ActionError> {
        return dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.CLEAR_TEXT,
                targetId = targetId
            )
        )
    }

    fun scroll(direction: String, amount: Int): Result<Unit, ActionError> {
        return dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SCROLL,
                targetClass = direction,
                durationMs = amount.toLong()
            )
        )
    }

    fun wait(durationMs: Long): Result<Unit, ActionError> {
        if (durationMs < 0L) {
            return Result.Failure(
                ActionError(code = "INVALID_REQUEST", message = "Wait duration must not be negative")
            )
        }
        return dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.WAIT,
                durationMs = durationMs
            )
        )
    }
}
