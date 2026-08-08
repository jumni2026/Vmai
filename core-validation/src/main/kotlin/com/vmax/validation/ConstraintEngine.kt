package com.vmax.validation

import com.vmax.model.Passenger

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 7 — ConstraintEngine
 *
 * Dynamic constraint detection and validation engine.
 * Reads field constraints dynamically from accessibility nodes.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
object ConstraintEngine {

    data class FieldConstraint(
        val maxLength: Int? = null,
        val minLength: Int? = null,
        val pattern: String? = null,
        val required: Boolean = true
    )

    private val constraintCache = mutableMapOf<String, FieldConstraint>()

    fun registerConstraint(fieldId: String, constraint: FieldConstraint) {
        constraintCache[fieldId] = constraint
    }

    fun getConstraint(fieldId: String): FieldConstraint? {
        return constraintCache[fieldId]
    }

    fun clearCache() {
        constraintCache.clear()
    }

    fun validateWithConstraint(
        fieldId: String,
        value: String,
        passenger: Passenger? = null
    ): Boolean {
        val constraint = getConstraint(fieldId) ?: return true

        if (constraint.required && value.isBlank()) return false

        constraint.maxLength?.let { if (value.length > it) return false }
        constraint.minLength?.let { if (value.length < it) return false }
        constraint.pattern?.let { if (!Regex(it).matches(value)) return false }

        return true
    }
}
