package com.vmax.model

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ChildData.kt
 *
 * Contract for Child Details entry in the Booking UI.
 * Platform-independent — no Android dependencies.
 * No business logic.
 */
data class ChildData(
    val name: String,
    val ageCategory: ChildAgeCategory,
    val gender: String
)

/**
 * Child age categories as seen in the Booking UI.
 */
enum class ChildAgeCategory {
    BELOW_ONE_YEAR,
    ONE_YEAR,
    TWO_YEARS,
    THREE_YEARS,
    FOUR_YEARS
}
