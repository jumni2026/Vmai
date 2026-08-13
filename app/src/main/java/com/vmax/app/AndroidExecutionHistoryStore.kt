package com.vmax.app

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vmax.runtime.ExecutionEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidExecutionHistoryStore.kt
 *
 * Android implementation of persistent execution history storage.
 * Stores session timelines as JSON files in the app's private storage.
 *
 * Responsibilities:
 * - Save and retrieve execution events for a specific session ID.
 * - Delete specific sessions or clear all history.
 * - List all available session IDs.
 */
class AndroidExecutionHistoryStore(private val context: Context) {

    private val gson = Gson()
    private val historyDir: File by lazy {
        File(context.filesDir, "execution_history").apply { mkdirs() }
    }

    /**
     * Saves a list of ExecutionEvent for a given session ID.
     */
    suspend fun saveSessionEvents(sessionId: String, events: List<ExecutionEvent>) {
        withContext(Dispatchers.IO) {
            val file = File(historyDir, "$sessionId.json")
            val json = gson.toJson(events)
            file.writeText(json)
        }
    }

    /**
     * Retrieves the list of ExecutionEvent for a given session ID.
     * Returns empty list if session not found.
     */
    suspend fun getSessionEvents(sessionId: String): List<ExecutionEvent> {
        return withContext(Dispatchers.IO) {
            val file = File(historyDir, "$sessionId.json")
            if (!file.exists()) {
                return@withContext emptyList()
            }
            val json = file.readText()
            val type = object : TypeToken<List<ExecutionEvent>>() {}.type
            gson.fromJson<List<ExecutionEvent>>(json, type) ?: emptyList()
        }
    }

    /**
     * Deletes the history for a specific session.
     */
    suspend fun deleteSession(sessionId: String) {
        withContext(Dispatchers.IO) {
            val file = File(historyDir, "$sessionId.json")
            if (file.exists()) {
                file.delete()
            }
        }
    }

    /**
     * Returns all session IDs currently stored in history.
     */
    fun getAllSessionIds(): List<String> {
        return historyDir.listFiles()?.filter { it.isFile && it.extension == "json" }
            ?.map { it.nameWithoutExtension } ?: emptyList()
    }

    /**
     * Clears all stored history.
     */
    suspend fun clearAllHistory() {
        withContext(Dispatchers.IO) {
            historyDir.listFiles()?.forEach { it.delete() }
        }
    }
}
