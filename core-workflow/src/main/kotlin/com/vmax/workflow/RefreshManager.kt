package com.vmax.workflow

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 13 — RefreshManager
 *
 * Manages refresh operations for train matching and availability.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface RefreshManager {

    data class RefreshResult(
        val attemptCount: Int,
        val maxAttempts: Int,
        val isSuccessful: Boolean,
        val message: String? = null
    )

    fun startRefresh(): Result<RefreshResult, RefreshError>

    fun stopRefresh(): Result<Unit, RefreshError>

    fun isRefreshing(): Boolean

    fun getCurrentAttempt(): Int

    fun getMaxAttempts(): Int

    fun setMaxAttempts(maxAttempts: Int)

    fun getRemainingAttempts(): Int

    fun reset()

    fun shouldContinueRefreshing(): Boolean
}

data class RefreshError(
    val code: String,
    val message: String,
    val attemptCount: Int = 0
)
