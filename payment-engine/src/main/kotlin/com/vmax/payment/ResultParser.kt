package com.vmax.payment

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 28 — ResultParser
 *
 * Parses payment result data from UPI response.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface ResultParser {

    fun parseResult(data: String): Result<PaymentResult, PaymentError>
}
