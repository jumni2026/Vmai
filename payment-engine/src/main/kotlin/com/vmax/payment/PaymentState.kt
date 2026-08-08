package com.vmax.payment

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 22 — PaymentState
 *
 * Payment state management contract for the payment engine.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
data class PaymentState(
    val currentStatus: PaymentStatus = PaymentStatus.IDLE,
    val selectedUpiApp: String? = null,
    val transactionId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {

    enum class PaymentStatus {
        IDLE,
        SELECTING_APP,
        APP_SELECTED,
        LAUNCHING,
        PROCESSING,
        SUCCESS,
        FAILED
    }
}
