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

/**
 * Defines the preference for coach selection.
 */
enum class CoachPreference {
    NONE,
    LOWER,
    MIDDLE,
    UPPER,
    SIDE_LOWER,
    SIDE_UPPER,
    ANY
}

/**
 * Data model for booking options.
 */
data class BookingOption(
    val autoUpgradation: Boolean = false,
    val confirmBerths: Boolean = false,
    val travelInsurance: Boolean = false,
    val coachPreferred: Boolean = false,
    val coachId: String? = null,
    val mobileNumber: String? = null,
    val coachPreference: CoachPreference = CoachPreference.NONE
)
