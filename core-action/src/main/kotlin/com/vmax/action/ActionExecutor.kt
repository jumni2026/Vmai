package com.vmax.action

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 14 — ActionExecutor
 *
 * Executes UI actions based on analysis and decisions.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface ActionExecutor {

    enum class ActionType {
        TAP,
        CLICK,
        LONG_CLICK,
        DOUBLE_TAP,
        SWIPE,
        SCROLL,
        SET_TEXT,
        CLEAR_TEXT,
        WAIT
    }

    data class ActionRequest(
        val type: ActionType,
        val targetId: String? = null,
        val targetText: String? = null,
        val targetClass: String? = null,
        val text: String? = null,
        val coordinates: Pair<Int, Int>? = null,
        val durationMs: Long = 0L,
        val waitAfterMs: Long = 500L
    )

    data class ActionResult(
        val success: Boolean,
        val actionType: ActionType,
        val message: String? = null,
        val timestamp: Long = System.currentTimeMillis()
    )

    fun executeAction(request: ActionRequest): Result<ActionResult, ActionError>

    fun executeTap(targetId: String): Result<ActionResult, ActionError>

    fun executeClick(targetId: String): Result<ActionResult, ActionError>

    fun executeSetText(targetId: String, text: String): Result<ActionResult, ActionError>

    fun executeClearText(targetId: String): Result<ActionResult, ActionError>

    fun executeScroll(direction: String, amount: Int): Result<ActionResult, ActionError>

    fun executeWait(durationMs: Long): Result<ActionResult, ActionError>

    fun isActionAvailable(actionType: ActionType): Boolean

    fun getLastActionResult(): ActionResult?
}

data class ActionError(
    val code: String,
    val message: String,
    val actionType: ActionExecutor.ActionType? = null,
    val targetId: String? = null
)
