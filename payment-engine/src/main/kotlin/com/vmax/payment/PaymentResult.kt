package com.vmax.payment

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 24 — PaymentResult
 *
 * Payment result contract for the payment engine.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
data class PaymentResult(
    val transactionId: String,
    val status: PaymentStatus,
    val message: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {

    enum class PaymentStatus {
        SUCCESS,
        FAILED,
        PENDING,
        CANCELLED,
        TIMEOUT
    }
}
