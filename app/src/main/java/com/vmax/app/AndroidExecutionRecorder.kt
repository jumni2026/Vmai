package com.vmax.app

import com.vmax.runtime.ExecutionEvent
import com.vmax.runtime.ExecutionRecorder
import kotlinx.coroutines.runBlocking

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidExecutionRecorder.kt
 *
 * Android implementation of the ExecutionRecorder interface.
 * Uses AndroidExecutionHistoryStore for persistent storage.
 *
 * Responsibilities:
 * - Record a single ExecutionEvent to storage (append to session).
 * - Retrieve all events for a given session ID.
 * - List all available session IDs.
 * - Clear all history.
 */
class AndroidExecutionRecorder(
    private val historyStore: AndroidExecutionHistoryStore
) : ExecutionRecorder {

    override fun recordEvent(event: ExecutionEvent) {
        // Extract sessionId from the sealed class event
        val sessionId = when (event) {
            is ExecutionEvent.SessionStarted -> event.sessionId
            is ExecutionEvent.SessionStopped -> event.sessionId
            is ExecutionEvent.SessionError -> event.sessionId
            is ExecutionEvent.WorkflowStateChanged -> event.sessionId
            is ExecutionEvent.ActionDispatched -> event.sessionId
            is ExecutionEvent.ActionSucceeded -> event.sessionId
            is ExecutionEvent.ActionFailed -> event.sessionId
        }

        // Append event to the session's event list
        runBlocking {
            val existingEvents = historyStore.getSessionEvents(sessionId)
            val updatedEvents = existingEvents + event
            historyStore.saveSessionEvents(sessionId, updatedEvents)
        }
    }

    override fun getSessionEvents(sessionId: String): List<ExecutionEvent> {
        return runBlocking {
            historyStore.getSessionEvents(sessionId)
        }
    }

    override fun getAllSessionIds(): List<String> {
        return historyStore.getAllSessionIds()
    }

    override fun clearAllHistory() {
        runBlocking {
            historyStore.clearAllHistory()
        }
    }
}
