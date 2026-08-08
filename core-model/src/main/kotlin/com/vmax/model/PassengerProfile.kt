package com.vmax.model

import java.time.LocalDateTime

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 5 — PassengerProfile
 *
 * Passenger profile with versioning support.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
data class PassengerProfile(
    val profileId: String,
    val passengers: List<Passenger>,
    val createdTime: LocalDateTime,
    val updatedTime: LocalDateTime,
    val version: Int = 1
)
