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
    MALE, FEMALE, TRANSGENDER
}

enum class BerthPreference {
    NO_PREFERENCE, LOWER, MIDDLE, UPPER, SIDE_LOWER, SIDE_UPPER
}

enum class MealPreference {
    NO_MEAL, VEG, NON_VEG
}

enum class Concession {
    NONE, SENIOR_CITIZEN, DIVYANG, STUDENT, OTHER
}

enum class TravelClass {
    SL, // Sleeper
    CC, // Chair Car
    EC, // Executive Chair Car
    _3A, // AC 3 Tier
    _3E, // AC 3 Economy
    _2A, // AC 2 Tier
    _1A, // AC First Class
    _2S  // Second Seating
}

enum class Quota {
    GENERAL,
    TATKAL,
    PREMIUM_TATKAL,
    LADIES,
    SENIOR_CITIZEN,
    DIVYANG,
    LOWER_BERTH_SR_CITIZEN,
    FOREIGN_TOURIST
}

// -----------------------------------------------------------------------------
// Data Models
// -----------------------------------------------------------------------------

/**
 * Represents a single passenger with their personal details 
 * and optional journey-specific preferences.
 * 
 * Note: Journey-specific fields (trainNumber, travelClass, quota) are nullable.
 * This allows the Passenger to be used both as a generic "Master List" contact 
 * AND as a specific "Saved Booking Template" for a particular train/route.
 */
data class Passenger(
    val passengerId: String, // Unique ID for this passenger within the profile
    val name: String,
    val age: Int,
    val gender: Gender,
    
    // --- Newly Added Journey/Booking Context Fields ---
    val trainNumber: String? = null,
    val travelClass: TravelClass? = null,
    val quota: Quota? = null,
    
    // --- Passenger Specific Preferences ---
    val berthPreference: BerthPreference = BerthPreference.NO_PREFERENCE,
    val mealPreference: MealPreference = MealPreference.NO_MEAL,
    val concession: Concession = Concession.NONE,
    
    // --- Identity & Metadata ---
    val isPrimary: Boolean = false, // e.g., The main booker whose IRCTC ID is used
    val idProofType: String? = null, // e.g., "AADHAAR", "VOTER_ID", "PAN"
    val idProofNumber: String? = null
)

/**
 * Represents a collection of passengers (a profile) along with 
 * global default preferences that apply if a specific passenger 
 * does not have an override defined.
 */
data class PassengerProfile(
    val profileId: String,
    val profileName: String = "Default Profile", // e.g., "Family", "Office Colleagues"
    val passengers: List<Passenger>,
    
    val createdTime: LocalDateTime,
    val updatedTime: LocalDateTime,
    val version: Int = 1,

    // Global defaults for this profile (used as fallback)
    val defaultBerthPreference: BerthPreference = BerthPreference.NO_PREFERENCE,
    val defaultMealPreference: MealPreference = MealPreference.NO_MEAL,
    val defaultConcession: Concession = Concession.NONE,
    val requestBedRoll: Boolean = false,
    
    // Metadata
    val isActive: Boolean = true
)
