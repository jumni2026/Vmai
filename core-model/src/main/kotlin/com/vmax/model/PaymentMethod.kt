package com.vmax.model

/**
 * VMAX Enterprise v2.6.1
 *
 * File — PaymentMethod.kt
 *
 * Contract for Payment Method selection as per Booking UI.
 * Platform-independent — no Android dependencies.
 * No business logic.
 */
data class PaymentMethod(
    val useEWallet: Boolean = false,
    val useNetbanking: Boolean = false,
    val useUPIId: Boolean = false,
    val useUPIApp: Boolean = false,
    val selectedUPIApp: UPIApp? = null,
    val manualPayment: Boolean = false,
    val autofillOTP: Boolean = false
)
