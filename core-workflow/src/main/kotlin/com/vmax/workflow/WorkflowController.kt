package com.vmax.workflow

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 11 — WorkflowController
 *
 * Controls the overall workflow execution for VMAX Enterprise.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface WorkflowController {

    enum class WorkflowState {
        IDLE,
        CONFIGURED,
        TRAIN_SELECTED,
        PASSENGER_FILLED,
        REVIEW_READY,
        PAYMENT_WAITING,
        PAYMENT_PROCESSING,
        COMPLETED,
        FAILED
    }

    data class WorkflowContext(
        val state: WorkflowState,
        val currentStep: String,
        val progress: Float,
        val message: String? = null,
        val error: Throwable? = null
    )

    fun startWorkflow(config: Map<String, Any>): Result<Unit, WorkflowError>

    fun pauseWorkflow(): Result<Unit, WorkflowError>

    fun resumeWorkflow(): Result<Unit, WorkflowError>

    fun stopWorkflow(): Result<Unit, WorkflowError>

    fun getCurrentState(): WorkflowState

    fun getContext(): WorkflowContext

    fun isRunning(): Boolean

    fun isPaused(): Boolean

    fun isCompleted(): Boolean

    fun isFailed(): Boolean

    fun getError(): Throwable?

    fun clearError()
}

data class WorkflowError(
    val code: String,
    val message: String,
    val cause: Throwable? = null
)
