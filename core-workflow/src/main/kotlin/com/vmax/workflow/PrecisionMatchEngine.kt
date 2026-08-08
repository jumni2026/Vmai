package com.vmax.workflow

import com.vmax.common.Result
import com.vmax.model.Train

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 12 — PrecisionMatchEngine
 *
 * Precision matching engine for train and class selection.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface PrecisionMatchEngine {

    data class MatchResult(
        val trainNumber: String,
        val trainName: String?,
        val classType: String,
        val isExactTrainMatch: Boolean,
        val isExactClassMatch: Boolean,
        val isTrainNameVerified: Boolean,
        val available: Boolean = false
    )

    fun matchTrain(
        trainNumber: String,
        trainName: String?,
        classType: String
    ): Result<MatchResult, MatchError>

    fun verifyTrainName(
        trainNumber: String,
        actualName: String
    ): Boolean

    fun checkAvailability(
        trainNumber: String,
        classType: String,
        date: String
    ): Result<Boolean, MatchError>

    fun isTrainMatchSuccessful(result: MatchResult): Boolean

    fun isClassMatchSuccessful(result: MatchResult): Boolean

    fun needsRefresh(result: MatchResult): Boolean
}

data class MatchError(
    val code: String,
    val message: String,
    val trainNumber: String? = null,
    val classType: String? = null
)
