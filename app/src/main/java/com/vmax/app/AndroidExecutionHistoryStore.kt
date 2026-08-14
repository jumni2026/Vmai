package com.vmax.app

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vmax.action.ExecutionEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidExecutionHistoryStore.kt
 *
 * Android implementation of persistent execution history storage.
 *
 * Responsibilities:
 * - Persist execution event timelines in the app's private storage.
 * - Retrieve events for a specific session.
 * - Delete a specific session history.
 * - List stored session IDs.
 * - Clear all stored history.
 *
 * ExecutionEvent is owned by the core-action module.
 */
class AndroidExecutionHistoryStore(
    private val context: Context
) {

    private val gson = Gson()

    private val historyDir: File by lazy {
        File(context.filesDir, "execution_history").apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    /**
     * Persists the complete event timeline for a session.
     */
    suspend fun saveSessionEvents(
        sessionId: String,
        events: List<ExecutionEvent>
    ) {
        withContext(Dispatchers.IO) {
            val file = File(historyDir, "$sessionId.json")
            file.writeText(gson.toJson(events))
        }
    }

    /**
     * Retrieves the event timeline for a session.
     *
     * Returns an empty list when no history file exists.
     */
    suspend fun getSessionEvents(
        sessionId: String
    ): List<ExecutionEvent> {
        return withContext(Dispatchers.IO) {
            val file = File(historyDir, "$sessionId.json")

            if (!file.exists()) {
                return@withContext emptyList()
            }

            val json = file.readText()

            if (json.isBlank()) {
                return@withContext emptyList()
            }

            val type = object : TypeToken<List<ExecutionEvent>>() {}.type

            gson.fromJson<List<ExecutionEvent>>(json, type)
                ?: emptyList()
        }
    }

    /**
     * Deletes the history for a specific session.
     */
    suspend fun deleteSession(
        sessionId: String
    ) {
        withContext(Dispatchers.IO) {
            val file = File(historyDir, "$sessionId.json")

            if (file.exists()) {
                file.delete()
            }
        }
    }

    /**
     * Returns all stored session IDs.
     */
    fun getAllSessionIds(): List<String> {
        return historyDir
            .listFiles()
            ?.asSequence()
            ?.filter { file ->
                file.isFile && file.extension == "json"
            }
            ?.map { file ->
                file.nameWithoutExtension
            }
            ?.toList()
            ?: emptyList()
    }

    /**
     * Deletes all stored execution history.
     */
    suspend fun clearAllHistory() {
        withContext(Dispatchers.IO) {
            historyDir
                .listFiles()
                ?.forEach { file ->
                    if (file.isFile) {
                        file.delete()
                    }
                }
        }
    }
}
