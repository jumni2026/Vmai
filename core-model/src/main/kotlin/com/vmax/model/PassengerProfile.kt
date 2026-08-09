package com.vmax.model

import java.time.LocalDateTime

/**
 * VMAX Enterprise v2.6.1
 *
 * File — PassengerProfile.kt
 *
 * Passenger profile with versioning and optional
 * booking preferences.
 *
 * Platform-independent — no Android dependencies.
 * No business logic.
 */
data class PassengerProfile(
    val profileId: String,
    val passengers: List<Passenger>,
    val createdTime: LocalDateTime,
    val updatedTime: LocalDateTime,
    val version: Int = 1,

    val berthPreference: BerthPreference = BerthPreference.NO_PREFERENCE,
    val mealPreference: MealPreference = MealPreference.NO_MEAL,
    val concession: Concession = Concession.NONE,
    val bedRoll: Boolean = false
)
