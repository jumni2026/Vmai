package com.vmax.payment

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 25 — PaymentCoordinator
 *
 * Coordinates payment flow initiation and status retrieval.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 *
 * Minimal contract — only payment flow coordination.
 * Retry, cancellation, and state management are left to
 * implementation or higher-level orchestration.
 */
interface PaymentCoordinator {

    fun initiatePayment(request: PaymentRequest): Result<PaymentResult, PaymentError>

    fun getPaymentStatus(transactionId: String): Result<PaymentResult, PaymentError>
}

data class PaymentError(
    val code: String,
    val message: String,
    val transactionId: String? = null
)
