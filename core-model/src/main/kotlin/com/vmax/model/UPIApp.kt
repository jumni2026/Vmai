package com.vmax.model

/**
 * VMAX Enterprise v2.6.1
 *
 * File — UPIApp.kt
 *
 * Contract for UPI App selection as per Booking UI.
 * Platform-independent — no Android dependencies.
 * No business logic.
 */
enum class UPIApp {
    PHONEPE,
    PAYTM,
    CRED_UPI,
    BHIM_UPI,
    OTHER
}
