package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.common.Result

class ActionOrchestrator(
    private val executor: ActionExecutor
) {

    fun execute(actionType: ActionExecutor.ActionType, targetId: String? = null, text: String? = null): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = actionType,
            targetId = targetId,
            targetText = text,
            text = text
        )
        return executor.executeAction(request)
    }
}

data class ActionError(
    val code: String,
    val message: String
)
