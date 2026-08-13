package com.vmax.app

import com.vmax.runtime.ExecutionEvent
import com.vmax.runtime.ExecutionHistoryRepository
import com.vmax.runtime.MetricsCollector
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidExecutionHistoryRepository.kt
 *
 * Android implementation of the ExecutionHistoryRepository interface.
 * Combines data from AndroidExecutionHistoryStore and AndroidMetricsCollector.
 *
 * Responsibilities:
 * - Fetch combined session data (events + metrics) for a single session.
 * - Fetch combined data for all available sessions.
 * - Delegate deletion and clearing to underlying storage/metrics.
 */
class AndroidExecutionHistoryRepository(
    private val historyStore: AndroidExecutionHistoryStore,
    private val metricsCollector: AndroidMetricsCollector
) : ExecutionHistoryRepository {

    override suspend fun getSessionHistory(sessionId: String): ExecutionHistoryRepository.SessionHistory? {
        return withContext(Dispatchers.IO) {
            val events = historyStore.getSessionEvents(sessionId)
            if (events.isEmpty()) {
                return@withContext null
            }
            val metrics = metricsCollector.getSessionMetrics(sessionId)
            ExecutionHistoryRepository.SessionHistory(
                sessionId = sessionId,
                events = events,
                metrics = metrics
            )
        }
    }

    override fun getAllSessionIds(): List<String> {
        return historyStore.getAllSessionIds()
    }

    override suspend fun getAllSessionHistories(): List<ExecutionHistoryRepository.SessionHistory> {
        val allIds = getAllSessionIds()
        if (allIds.isEmpty()) return emptyList()

        return coroutineScope {
            allIds.map { sessionId ->
                async { getSessionHistory(sessionId) }
            }.awaitAll().filterNotNull()
        }
    }

    override suspend fun deleteSession(sessionId: String) {
        historyStore.deleteSession(sessionId)
        // Note: Metrics are currently kept for historical aggregation,
        // but can be cleared here if the contract requires it.
    }

    override suspend fun clearAll() {
        historyStore.clearAllHistory()
        metricsCollector.clearMetrics()
    }
}
