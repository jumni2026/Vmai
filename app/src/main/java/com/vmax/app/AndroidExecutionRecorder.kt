package com.vmax.app

import com.vmax.action.ExecutionEvent
import com.vmax.action.ExecutionRecorder
import kotlinx.coroutines.runBlocking

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidExecutionRecorder.kt
 *
 * Android implementation of the platform-independent
 * ExecutionRecorder contract.
 *
 * Storage:
 * AndroidExecutionHistoryStore
 *
 * Responsibilities:
 * - Record execution events.
 * - Retrieve session events.
 * - Retrieve all session IDs.
 * - Clear execution history.
 */
class AndroidExecutionRecorder(
    private val historyStore: AndroidExecutionHistoryStore
) : ExecutionRecorder {

    override fun recordEvent(event: ExecutionEvent) {

        val sessionId = when (event) {
            is ExecutionEvent.SessionStarted -> event.sessionId
            is ExecutionEvent.SessionStopped -> event.sessionId
            is ExecutionEvent.SessionError -> event.sessionId
            is ExecutionEvent.WorkflowStateChanged -> event.sessionId
            is ExecutionEvent.ActionDispatched -> event.sessionId
            is ExecutionEvent.ActionSucceeded -> event.sessionId
            is ExecutionEvent.ActionFailed -> event.sessionId
        }

        runBlocking {
            val existingEvents =
                historyStore.getSessionEvents(sessionId)

            val updatedEvents =
                existingEvents + event

            historyStore.saveSessionEvents(
                sessionId,
                updatedEvents
            )
        }
    }

    override fun getSessionEvents(
        sessionId: String
    ): List<ExecutionEvent> {
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
