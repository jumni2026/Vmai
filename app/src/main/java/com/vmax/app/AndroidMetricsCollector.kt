package com.vmax.app

import com.vmax.action.ActionExecutor
import com.vmax.runtime.MetricsCollector
import java.util.concurrent.ConcurrentHashMap

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidMetricsCollector.kt
 *
 * Android implementation of the MetricsCollector interface.
 * Stores session metrics in a thread-safe in-memory map.
 *
 * Responsibilities:
 * - Track session start and end times.
 * - Count total, successful, and failed actions per session.
 * - Aggregate error codes to identify failure patterns.
 * - Provide an overview of all session metrics.
 */
class AndroidMetricsCollector : MetricsCollector {

    // Thread-safe map to store metrics for each session
    private val sessionMetricsMap = ConcurrentHashMap<String, MetricsCollector.SessionMetrics>()

    override fun startMetrics(sessionId: String) {
        val now = System.currentTimeMillis()
        val metrics = MetricsCollector.SessionMetrics(
            sessionId = sessionId,
            startTime = now,
            status = "RUNNING"
        )
        sessionMetricsMap[sessionId] = metrics
    }

    override fun stopMetrics(sessionId: String, status: String) {
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
        sessionMetricsMap.computeIfPresent(sessionId) { _, oldMetrics ->
            val newTotal = oldMetrics.totalActions + 1
            val newSuccess = if (isSuccess) oldMetrics.successActions + 1 else oldMetrics.successActions
            val newFailed = if (!isSuccess) oldMetrics.failedActions + 1 else oldMetrics.failedActions

            // Update error distribution if action failed
            val newErrorDist = oldMetrics.errorDistribution.toMutableMap()
            if (!isSuccess && errorCode != null) {
                newErrorDist[errorCode] = newErrorDist.getOrDefault(errorCode, 0) + 1
            }

            oldMetrics.copy(
                totalActions = newTotal,
                successActions = newSuccess,
                failedActions = newFailed,
                errorDistribution = newErrorDist
            )
        }
    }

    override fun getSessionMetrics(sessionId: String): MetricsCollector.SessionMetrics? {
        return sessionMetricsMap[sessionId]
    }

    override fun getAllSessionsMetrics(): List<MetricsCollector.SessionMetrics> {
        return sessionMetricsMap.values.toList()
    }

    override fun clearMetrics() {
        sessionMetricsMap.clear()
    }
}
