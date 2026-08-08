package com.vmax.payment

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 27 — PaymentLauncher
 *
 * Launches UPI payment apps for payment processing.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 *
 * Exclusive responsibility: Launch UPI App only.
 * App availability and selection are handled by ProviderSelector.
 */
interface PaymentLauncher {

    fun launchUpiApp(upiApp: String): Result<Unit, PaymentError>
}
