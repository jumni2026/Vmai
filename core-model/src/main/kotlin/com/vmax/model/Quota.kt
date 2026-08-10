package com.vmax.model

/**
 * VMAX Enterprise v2.6.1
 *
 * File — Quota.kt
 *
 * Contract for Quota selection as per IRCTC Booking UI.
 * Platform-independent — no Android dependencies.
 * No business logic.
 */
enum class Quota {
    GENERAL,
    TATKAL,
    PREMIUM_TATKAL,
    PHYSICALLY_HANDICAPPED,
    DUTY_PASS,
    LADIES
}
