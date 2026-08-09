package com.vmax.model

/**
 * VMAX Enterprise v2.6.1
 *
 * File — MealPreference.kt
 *
 * Contract for Meal Preference selection as per Booking UI.
 * Platform-independent — no Android dependencies.
 * No business logic.
 */
enum class MealPreference {
    VEG,
    NON_VEG,
    JAIN_MEAL,
    VEG_DIABETIC,
    NON_VEG_DIABETIC,
    NO_MEAL
}
