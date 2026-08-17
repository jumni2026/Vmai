package com.vmax.app

import com.vmax.action.ActionExecutor
import com.vmax.action.MetricsCollector
import java.util.concurrent.ConcurrentHashMap

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidMetricsCollector.kt
 *
 * Android implementation of the platform-independent
 * MetricsCollector contract.
 *
 * Responsibilities:
 * - Track session start and end times.
 * - Count total, successful, and failed actions.
 * - Aggregate error codes.
 * - Provide session metrics.
 *
 * Architecture:
 * - Contract: core-action
 * - Implementation: app
 */
class AndroidMetricsCollector : MetricsCollector {

    private val sessionMetricsMap =
        ConcurrentHashMap<String, MetricsCollector.SessionMetrics>()

    override fun startMetrics(sessionId: String) {
        if (sessionId.isBlank()) return

        val metrics = MetricsCollector.SessionMetrics(
            sessionId = sessionId,
            startTime = System.currentTimeMillis(),
            status = "RUNNING"
        )

        sessionMetricsMap[sessionId] = metrics
    }

    override fun stopMetrics(
        sessionId: String,
        status: String
    ) {
        if (sessionId.isBlank()) return

        sessionMetricsMap.computeIfPresent(sessionId) { _, oldMetrics ->
            oldMetrics.copy(
                endTime = System.currentTimeMillis(),
                status = status
            )
        }
    }

    override fun recordAction(
        sessionId: String,
        isSuccess: Boolean,
        actionType: ActionExecutor.ActionType,
        errorCode: String?
    ) {
        if (sessionId.isBlank()) return

        sessionMetricsMap.computeIfPresent(sessionId) { _, oldMetrics ->

            val newTotalActions =
                oldMetrics.totalActions + 1

            val newSuccessActions =
                if (isSuccess) {
                    oldMetrics.successActions + 1
                } else {
                    oldMetrics.successActions
                }

            val newFailedActions =
                if (!isSuccess) {
                    oldMetrics.failedActions + 1
                } else {
                    oldMetrics.failedActions
                }

            val newErrorDistribution =
                oldMetrics.errorDistribution.toMutableMap()

            if (!isSuccess && !errorCode.isNullOrBlank()) {
                newErrorDistribution[errorCode] =
                    (newErrorDistribution[errorCode] ?: 0) + 1
            }

            oldMetrics.copy(
                totalActions = newTotalActions,
                successActions = newSuccessActions,
                failedActions = newFailedActions,
                errorDistribution = newErrorDistribution
            )
        }
    }

    override fun getSessionMetrics(
        sessionId: String
    ): MetricsCollector.SessionMetrics? {
        if (sessionId.isBlank()) return null

        return sessionMetricsMap[sessionId]
    }

    override fun getAllSessionsMetrics():
        List<MetricsCollector.SessionMetrics> {
        return sessionMetricsMap.values.toList()
    }

    override fun clearMetrics() {
        sessionMetricsMap.clear()
    }
}
