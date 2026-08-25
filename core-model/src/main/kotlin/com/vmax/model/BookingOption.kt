package com.vmax.model

/**
 * VMAX Enterprise v2.6.1
 *
 * File — BookingOption.kt
 *
 * Contract for Booking Options configuration as per Booking UI.
 * Platform-independent — no Android dependencies.
 * No business logic.
 */
data class BookingOption(
    val autoUpgradation: Boolean = false,
    val confirmBerths: Boolean = false,
    val travelInsurance: Boolean = false,
    val coachPreferred: Boolean = false,
    val coachId: String? = null,
    val mobileNumber: String? = null
)
