package com.vmax.runtime

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 19 — RuntimeCoordinator
 *
 * Coordinates runtime execution and orchestration.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 *
 * Minimal contract — implementation details are left to
 * platform-specific modules.
 */
interface RuntimeCoordinator {

    enum class RuntimeState {
        INIT,
        CONFIGURED,
        RUNNING,
        PAUSED,
        STOPPED,
        COMPLETED,
        ERROR
    }

    data class RuntimeConfig(
        val maxRetries: Int = 3,
        val timeoutMs: Long = 30000L,
        val enableLogging: Boolean = true
    )

    data class RuntimeStatus(
        val state: RuntimeState,
        val message: String? = null
    )

    fun initialize(config: RuntimeConfig): Result<Unit, RuntimeError>

    fun start(): Result<Unit, RuntimeError>

    fun pause(): Result<Unit, RuntimeError>

    fun resume(): Result<Unit, RuntimeError>

    fun stop(): Result<Unit, RuntimeError>

    fun getStatus(): RuntimeStatus

    fun getConfig(): RuntimeConfig

    fun isRunning(): Boolean

    fun isPaused(): Boolean

    fun isCompleted(): Boolean

    fun getCurrentState(): RuntimeState
}

data class RuntimeError(
    val code: String,
    val message: String,
    val state: RuntimeCoordinator.RuntimeState? = null
)
