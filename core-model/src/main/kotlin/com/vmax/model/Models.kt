package com.vmax.model

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 4 — Models
 *
 * Core data models for VMAX Enterprise.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */

data class Train(
    val number: String,
    val name: String,
    val classType: String,
    val quota: String? = null
)

data class Station(
    val code: String,
    val name: String
)

data class Passenger(
    val name: String,
    val age: Int,
    val gender: String,
    val mobile: String? = null
)

data class BookingRequest(
    val train: Train,
    val fromStation: Station,
    val toStation: Station,
    val date: String,
    val passengers: List<Passenger>,
    val quota: String
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}

// ============================================
// नई जोड़ी गई Enums (PassengerProfile के लिए)
// ============================================

enum class BerthPreference {
    NO_PREFERENCE,
    LOWER,
    MIDDLE,
    UPPER,
    SIDE_LOWER,
    SIDE_UPPER
}

enum class MealPreference {
    NO_MEAL,
    VEG,
    NON_VEG,
    VEG_JAIN
}

enum class Concession {
    NONE,
    SENIOR_CITIZEN,
    DISABLED
}
