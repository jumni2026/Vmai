package com.vmax.runtime

import com.vmax.action.ExecutionEvent
import com.vmax.action.MetricsCollector

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionHistoryRepository.kt
 *
 * Platform-independent repository contract for retrieving
 * execution history and metrics.
 *
 * Combines data from:
 * - ExecutionHistoryStore
 * - MetricsCollector
 *
 * Responsibilities:
 * - Fetch complete session history by session ID.
 * - List all available session IDs.
 * - Provide session summaries containing events and metrics.
 *
 * Implementation is provided by platform-specific modules.
 */
interface ExecutionHistoryRepository {

    /**
     * Represents a complete execution history for one session.
     */
    data class SessionHistory(
        val sessionId: String,
        val events: List<ExecutionEvent>,
        val metrics: MetricsCollector.SessionMetrics?
    )

    /**
     * Fetches the complete history for a session.
     *
     * Returns null when the session does not exist.
     */
    suspend fun getSessionHistory(
        sessionId: String
    ): SessionHistory?

    /**
     * Returns all recorded session IDs.
     */
    fun getAllSessionIds(): List<String>

    /**
     * Fetches complete histories for all recorded sessions.
     */
    suspend fun getAllSessionHistories(): List<SessionHistory>

    /**
     * Deletes history for a specific session.
     */
    suspend fun deleteSession(
        sessionId: String
    )

    /**
     * Clears all stored execution history and metrics.
     */
    suspend fun clearAll()
}
