package com.vmax.intelligence

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 10 — UIEvidenceCollector
 *
 * Collects and manages UI evidence before every action.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface UIEvidenceCollector {

    data class UIEvidence(
        val screenName: String,
        val visibleText: String?,
        val nodeId: String?,
        val className: String?,
        val bounds: String?,
        val timestamp: Long = System.currentTimeMillis(),
        val action: String? = null
    )

    fun collectCurrentEvidence(): UIEvidence

    fun collectEvidenceForAction(action: String): UIEvidence

    fun getLastEvidence(): UIEvidence?

    fun clearEvidence()

    fun hasEvidence(): Boolean

    fun saveEvidence(evidence: UIEvidence)

    fun getEvidenceHistory(): List<UIEvidence>
}
