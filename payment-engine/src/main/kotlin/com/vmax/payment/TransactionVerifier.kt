package com.vmax.payment

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 30 — TransactionVerifier
 *
 * Interface for transaction verification.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 *
 * Exclusive responsibility: Verify transaction status and validity.
 * Uses existing PaymentResult and PaymentError contracts.
 */
interface TransactionVerifier {

    fun verifyTransaction(transactionId: String): Result<PaymentResult, PaymentError>
}
