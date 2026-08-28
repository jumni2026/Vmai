package com.vmax.model

import java.time.LocalDateTime

/**
 * VMAX Enterprise v2.6.1
 *
 * File — PassengerProfile.kt
 *
 * Passenger profile with versioning, journey context, and booking preferences.
 *
 * Platform-independent — no Android dependencies.
 * No business logic.
 */

// -----------------------------------------------------------------------------
// Enums for Type Safety (No magic strings)
// -----------------------------------------------------------------------------

enum class Gender {
    MALE,
    FEMALE,
    TRANSGENDER
}

enum class TravelClass {
    SL,       // Sleeper
    CC,       // Chair Car
    EC,       // Executive Chair Car
    _3A,      // AC 3 Tier
    _3E,      // AC 3 Economy
    _2A,      // AC 2 Tier
    _1A,      // AC First Class
    _2S       // Second Seating
}

// -----------------------------------------------------------------------------
// Data Models
// -----------------------------------------------------------------------------

/**
 * Represents a single passenger with their personal details
 * and optional journey-specific preferences.
 */
data class Passenger(
    val passengerId: String,
    val name: String,
    val age: Int,
    val gender: Gender,

    // Journey/Booking Context
    val trainNumber: String? = null,
    val travelClass: TravelClass? = null,
    val quota: Quota? = null,

    // Passenger Specific Preferences
    val berthPreference: BerthPreference =
        BerthPreference.NO_PREFERENCE,

    val mealPreference: MealPreference =
        MealPreference.NO_MEAL,

    val concession: Concession =
        Concession.NONE,

    // Identity & Metadata
    val isPrimary: Boolean = false,
    val idProofType: String? = null,
    val idProofNumber: String? = null,

    // Contact
    val mobile: String? = null
)

/**
 * Represents a collection of passengers (a profile) along with
 * global default preferences.
 */
data class PassengerProfile(
    val profileId: String,
    val profileName: String = "Default Profile",
    val passengers: List<Passenger>,

    val createdTime: LocalDateTime,
    val updatedTime: LocalDateTime,
    val version: Int = 1,

    // Global defaults
    val defaultBerthPreference: BerthPreference =
        BerthPreference.NO_PREFERENCE,

    val defaultMealPreference: MealPreference =
        MealPreference.NO_MEAL,

    val defaultConcession: Concession =
        Concession.NONE,

    val requestBedRoll: Boolean = false,

    // Metadata
    val isActive: Boolean = true
)
