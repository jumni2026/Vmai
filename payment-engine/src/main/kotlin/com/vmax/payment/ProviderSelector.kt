package com.vmax.payment

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 29 — ProviderSelector
 *
 * Selects UPI payment provider from available options.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 *
 * Exclusive responsibility: Select UPI App.
 * Available providers list is provided as input.
 */
interface ProviderSelector {

    fun selectProvider(upiApps: List<String>): Result<String, PaymentError>
}
