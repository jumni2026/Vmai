package com.vmax.action

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 16 — GestureExecutor
 *
 * Executes gesture-based interactions like swipe and scroll.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface GestureExecutor {

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    enum class GestureType {
        SWIPE,
        SCROLL,
        FLING,
        DRAG
    }

    data class GestureRequest(
        val type: GestureType,
        val direction: Direction,
        val distance: Int = 100,
        val durationMs: Long = 300L,
        val startX: Int? = null,
        val startY: Int? = null,
        val endX: Int? = null,
        val endY: Int? = null
    )

    data class GestureResult(
        val success: Boolean,
        val gestureType: GestureType,
        val direction: Direction,
        val distance: Int,
        val message: String? = null
    )

    fun executeSwipe(
        direction: Direction,
        distance: Int = 100,
        durationMs: Long = 300L
    ): Result<GestureResult, GestureError>

    fun executeScroll(
        direction: Direction,
        distance: Int = 100
    ): Result<GestureResult, GestureError>

    fun executeFling(
        direction: Direction,
        distance: Int = 200,
        durationMs: Long = 100L
    ): Result<GestureResult, GestureError>

    fun executeDrag(
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        durationMs: Long = 500L
    ): Result<GestureResult, GestureError>

    fun executeGesture(request: GestureRequest): Result<GestureResult, GestureError>

    fun isGestureSupported(type: GestureType): Boolean

    fun getDefaultDurationMs(): Long

    fun getDefaultDistance(): Int
}

data class GestureError(
    val code: String,
    val message: String,
    val gestureType: GestureExecutor.GestureType? = null,
    val direction: GestureExecutor.Direction? = null
)
