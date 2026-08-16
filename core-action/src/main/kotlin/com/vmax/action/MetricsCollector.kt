package com.vmax.action

/**
 * VMAX Enterprise v2.6.1
 *
 * File — MetricsCollector.kt
 *
 * Platform-independent contract for collecting and aggregating
 * execution metrics.
 *
 * Ownership:
 * - Contract belongs to core-action.
 * - Implementations may live in runtime/app platform layers.
 *
 * Responsibilities:
 * - Track session start and end times.
 * - Count total, successful, and failed actions per session.
 * - Aggregate error codes to identify failure patterns.
 * - Provide an overview of all session metrics.
 */
interface MetricsCollector {

    /**
     * Represents the aggregated metrics for a single execution session.
     */
    data class SessionMetrics(
        val sessionId: String,
        val startTime: Long,
        var endTime: Long? = null,
        var totalActions: Int = 0,
        var successActions: Int = 0,
        var failedActions: Int = 0,
        val errorDistribution: MutableMap<String, Int> = mutableMapOf(),
        var status: String = "RUNNING"
    )

    /**
     * Starts recording metrics for a new session.
     */
    fun startMetrics(sessionId: String)

    /**
     * Stops recording metrics for a session and updates its final status.
     */
    fun stopMetrics(
        sessionId: String,
        status: String
    )

    /**
     * Records the result of an executed action.
     */
    fun recordAction(
        sessionId: String,
        isSuccess: Boolean,
        actionType: ActionExecutor.ActionType,
        errorCode: String? = null
    )

    /**
     * Retrieves the aggregated metrics for a specific session.
     *
     * Returns null if the session does not exist.
     */
    fun getSessionMetrics(
        sessionId: String
    ): SessionMetrics?

    /**
     * Retrieves the aggregated metrics for all recorded sessions.
     */
    fun getAllSessionsMetrics(): List<SessionMetrics>

    /**
     * Clears all recorded metrics data.
     */
    fun clearMetrics()
}
