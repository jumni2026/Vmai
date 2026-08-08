package com.vmax.runtime

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 21 — PaymentSessionData
 *
 * Payment-specific session data contract.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 *
 * Minimal data model — no methods, only immutable data.
 */
data class PaymentSessionData(
    val upiApp: String? = null,
    val transactionId: String? = null,
    val paymentStatus: PaymentStatus = PaymentStatus.IDLE,
    val timestamp: Long = System.currentTimeMillis()
) {

    enum class PaymentStatus {
        IDLE,
        SELECTED,
        INITIATED,
        SUCCESS,
        FAILED
    }
}
