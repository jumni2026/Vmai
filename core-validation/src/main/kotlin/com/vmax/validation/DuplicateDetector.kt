package com.vmax.validation

import com.vmax.model.Passenger

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 8 — DuplicateDetector
 *
 * Detects duplicate passenger data across the list.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
object DuplicateDetector {

    data class DuplicateWarning(
        val field: String,
        val value: String,
        val passengerIndices: List<Int>
    )

    fun detectDuplicateNames(passengers: List<Passenger>): List<DuplicateWarning> {
        val nameMap = mutableMapOf<String, MutableList<Int>>()
        passengers.forEachIndexed { index, passenger ->
            nameMap.getOrPut(passenger.name) { mutableListOf() }.add(index)
        }
        return nameMap
            .filter { it.value.size > 1 }
            .map { DuplicateWarning("name", it.key, it.value) }
    }

    fun detectDuplicateMobile(passengers: List<Passenger>): List<DuplicateWarning> {
        val mobileMap = mutableMapOf<String, MutableList<Int>>()
        passengers.forEachIndexed { index, passenger ->
            passenger.mobile?.let { mobile ->
                mobileMap.getOrPut(mobile) { mutableListOf() }.add(index)
            }
        }
        return mobileMap
            .filter { it.value.size > 1 }
            .map { DuplicateWarning("mobile", it.key, it.value) }
    }

    fun detectAllDuplicates(passengers: List<Passenger>): List<DuplicateWarning> {
        return detectDuplicateNames(passengers) + detectDuplicateMobile(passengers)
    }

    fun hasDuplicates(passengers: List<Passenger>): Boolean {
        return detectAllDuplicates(passengers).isNotEmpty()
    }
}
