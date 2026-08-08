package com.vmax.payment

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 23 — PaymentRequest
 *
 * Payment request contract for the payment engine.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 *
 * Amount is stored as Long in paisa (1/100 of rupee)
 * to avoid floating-point precision issues.
 */
data class PaymentRequest(
    val amountPaisa: Long,
    val upiApp: String,
    val merchantName: String,
    val merchantId: String,
    val description: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
