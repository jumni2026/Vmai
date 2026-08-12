package com.vmax.workflow

import com.vmax.action.ActionExecutor
import com.vmax.common.Result
import com.vmax.runtime.ExecutionTracker

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ActionOrchestrator.kt
 *
 * The Missing Bridge between WorkflowController and AndroidActionExecutor.
 * - Receives high-level commands (e.g., "CLICK", "SET_TEXT").
 * - Converts them into platform-independent ActionRequests.
 * - Dispatches them to the ActionExecutor.
 * - Records Success/Failure in ExecutionTracker.
 */
class ActionOrchestrator(
    private val actionExecutor: ActionExecutor,
    private val executionTracker: ExecutionTracker
) {

    fun click(targetId: String, sessionId: String): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.CLICK,
            targetId = targetId
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun tap(targetId: String, sessionId: String): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.TAP,
            targetId = targetId
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun setText(targetId: String, text: String, sessionId: String): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.SET_TEXT,
            targetId = targetId,
            text = text
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun clearText(targetId: String, sessionId: String): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.CLEAR_TEXT,
            targetId = targetId
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun scroll(direction: String, sessionId: String): Result<ActionExecutor.ActionResult, ActionError> {
        // ✅ ARCHITECTURAL RULE PRESERVED: targetText = direction
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.SCROLL,
            targetText = direction
        )
        return dispatchAndTrack(request, sessionId)
    }

    fun wait(durationMs: Long, sessionId: String): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.WAIT,
            durationMs = durationMs
        )
        return dispatchAndTrack(request, sessionId)
    }

    // ----------------------------------------------------------------
    // CORE DISPATCH + TRACKING LOGIC
    // ----------------------------------------------------------------
    private fun dispatchAndTrack(
        request: ActionExecutor.ActionRequest,
        sessionId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        
        // 1. Record Action Dispatched
        executionTracker.recordActionDispatched(
            sessionId = sessionId,
            actionType = request.type,
            targetId = request.targetId,
            targetText = request.targetText
        )

        // 2. Execute Action
        val result = actionExecutor.executeAction(request)

        // 3. Record Success / Failure
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
