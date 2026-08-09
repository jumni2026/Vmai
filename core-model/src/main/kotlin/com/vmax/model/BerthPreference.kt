package com.vmax.model

/**
 * VMAX Enterprise v2.6.1
 *
 * File — BerthPreference.kt
 *
 * Contract for Berth Preference selection as per Booking UI.
 * Platform-independent — no Android dependencies.
 * No business logic.
 */
enum class BerthPreference {
    LOWER,
    MIDDLE,
    UPPER,
    SIDE_LOWER,
    SIDE_UPPER,
    WINDOW_SIDE,
    CABIN,
    COUPE,
    NO_PREFERENCE
}
