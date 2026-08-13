package com.vmax.runtime

/**
 * VMAX Enterprise v2.6.1
 *
 * File — ExecutionHistoryStore.kt
 *
 * Platform-independent contract for storing and retrieving execution event timelines.
 * This interface abstracts the persistence mechanism for execution history.
 *
 * Responsibilities:
 * - Save a list of ExecutionEvent for a specific session ID.
 * - Retrieve the full event timeline for a session ID.
 * - Delete the history of a specific session.
 * - List all available session IDs.
 * - Clear all stored history.
 *
 * Implementation details are left to platform-specific modules
 * (e.g., AndroidExecutionHistoryStore in the app module).
 */
interface ExecutionHistoryStore {

    /**
     * Persists a list of ExecutionEvent for a given session ID.
     */
    suspend fun saveSessionEvents(sessionId: String, events: List<ExecutionEvent>)

    /**
     * Retrieves the list of ExecutionEvent for a given session ID.
     * Returns an empty list if the session does not exist.
     */
    suspend fun getSessionEvents(sessionId: String): List<ExecutionEvent>

    /**
     * Deletes the history for a specific session ID.
     */
    suspend fun deleteSession(sessionId: String)

    /**
     * Returns all session IDs currently stored in the history store.
     */
    fun getAllSessionIds(): List<String>

    /**
     * Clears all stored execution history.
     */
    suspend fun clearAllHistory()
}
