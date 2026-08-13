package com.vmax.runtime

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionRecorder.kt
 *
 * Contract for recording and retrieving execution histories.
 * Platform-independent interface for the runtime layer.
 *
 * Responsibilities:
 * - Record a single ExecutionEvent.
 * - Retrieve the event timeline for a specific session ID.
 * - List all available session IDs.
 * - Clear all history.
 *
 * Implementation details are left to platform-specific modules
 * (e.g., AndroidExecutionRecorder in the app module).
 */
interface ExecutionRecorder {

    /**
     * Records a single execution event to persistent storage.
     */
    fun recordEvent(event: ExecutionEvent)

    /**
     * Retrieves the complete event timeline for a given session ID.
     * Returns an empty list if the session does not exist.
     */
    fun getSessionEvents(sessionId: String): List<ExecutionEvent>

    /**
     * Returns a list of all session IDs currently stored in the recorder.
     */
    fun getAllSessionIds(): List<String>

    /**
     * Clears all recorded execution history.
     */
    fun clearAllHistory()
}
