package com.vmax.workflow

import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

class ActionOrchestrator(
    private val actionExecutor: ActionExecutor
) {

    fun dispatchAndTrack(
        request: ActionExecutor.ActionRequest
    ): Result<Unit, ActionError> {

        val result = actionExecutor.executeAction(request)
        
        // सीधे रिज़ल्ट को map करते हैं (सफलता या असफलता)
        return when (result) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(
                ActionError(
                    code = "EXECUTION_FAILED",
                    message = result.error.message ?: "Action failed"
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
            return Result.Error(
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
            return Result.Error(
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
