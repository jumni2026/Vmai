package com.vmax.payment

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 26 — PaymentManager
 *
 * Core Orchestrator for payment operations.
 *
 * Coordinates four specialized payment components:
 * 1. ProviderSelector - Select UPI payment provider
 * 2. PaymentLauncher - Launch UPI payment app
 * 3. ResultParser - Parse payment result data
 * 4. TransactionVerifier - Verify transaction status
 *
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface PaymentManager {

    /**
     * ProviderSelector coordination.
     * Selects appropriate UPI provider based on available apps.
     */
    fun selectProvider(upiApps: List<String>): Result<String, PaymentError>

    /**
     * PaymentLauncher coordination.
     * Launches the selected UPI payment app.
     */
    fun launchPayment(upiApp: String): Result<Unit, PaymentError>

    /**
     * ResultParser coordination.
     * Parses payment result data from UPI response.
     */
    fun parseResult(data: String): Result<PaymentResult, PaymentError>

    /**
     * TransactionVerifier coordination.
     * Verifies transaction status using transaction ID.
     */
    fun verifyTransaction(transactionId: String): Result<PaymentResult, PaymentError>
}
