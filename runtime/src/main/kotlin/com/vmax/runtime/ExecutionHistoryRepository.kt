package com.vmax.runtime

import com.vmax.action.ActionExecutor

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionHistoryRepository.kt
 *
 * Platform-independent repository contract for retrieving execution history and metrics.
 * Combines data from ExecutionHistoryStore and MetricsCollector.
 *
 * Responsibilities:
 * - Fetch a complete session history (events + metrics) by session ID.
 * - List all available session IDs.
 * - Provide summary statistics for all sessions.
 *
 * Implementation details are left to platform-specific modules
 * (e.g., AndroidExecutionHistoryRepository in the app module).
 */
interface ExecutionHistoryRepository {

    /**
     * Represents a complete session history, including all events and aggregated metrics.
     */
    data class SessionHistory(
        val sessionId: String,
        val events: List<ExecutionEvent>,
        val metrics: MetricsCollector.SessionMetrics?
    )

    /**
     * Fetches the full history for a given session ID.
     * Returns null if the session does not exist.
     */
    suspend fun getSessionHistory(sessionId: String): SessionHistory?

    /**
     * Returns a list of all recorded session IDs.
     */
    fun getAllSessionIds(): List<String>

    /**
     * Returns a list of all session histories (events + metrics) for all recorded sessions.
     * Useful for generating reports.
     */
    suspend fun getAllSessionHistories(): List<SessionHistory>

    /**
     * Deletes the history for a specific session.
     */
    suspend fun deleteSession(sessionId: String)

    /**
     * Clears all stored history and metrics.
     */
    suspend fun clearAll()
}
