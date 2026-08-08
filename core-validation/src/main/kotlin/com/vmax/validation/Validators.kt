package com.vmax.validation

import com.vmax.model.Passenger

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 6 — Validators
 *
 * Core validation functions for passenger data.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic beyond validation rules.
 */
object Validators {

    fun validateName(name: String): Boolean {
        val pattern = Regex("^[A-Za-z .'\\-]+$")
        return name.isNotBlank() && pattern.matches(name)
    }

    fun validateAge(age: Int): Boolean {
        return age in 1..120
    }

    fun validateMobile(mobile: String): Boolean {
        if (mobile.length != 10) return false
        val firstDigit = mobile.first().digitToIntOrNull() ?: return false
        return firstDigit in 6..9 && mobile.all { it.isDigit() }
    }

    fun validatePassenger(passenger: Passenger): Boolean {
        return validateName(passenger.name) &&
                validateAge(passenger.age) &&
                (passenger.mobile == null || validateMobile(passenger.mobile))
    }

    fun validatePassengers(passengers: List<Passenger>): Boolean {
        return passengers.all { validatePassenger(it) }
    }
}
