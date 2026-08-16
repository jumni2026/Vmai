package com.vmax.action

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionRecorder.kt
 *
 * Platform-independent contract for recording and retrieving
 * execution histories.
 *
 * Ownership:
 * - Contract belongs to core-action.
 * - Platform-specific implementations may live in runtime/app.
 */
interface ExecutionRecorder {

    /**
     * Records a single execution event.
     */
    fun recordEvent(event: ExecutionEvent)

    /**
     * Retrieves all events for a session.
     *
     * Returns an empty list when the session does not exist.
     */
    fun getSessionEvents(sessionId: String): List<ExecutionEvent>

    /**
     * Returns all stored session IDs.
     */
    fun getAllSessionIds(): List<String>

    /**
     * Clears all recorded execution history.
     */
    fun clearAllHistory()
}
