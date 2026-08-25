package com.vmax.app

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vmax.action.ActionExecutor
import com.vmax.action.ExecutionEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * VMAX Enterprise v2.6.1
 *
 * File: AndroidExecutionHistoryStore.kt
 *
 * Android persistent storage for VMAX execution history.
 *
 * Design:
 * - Stores execution events in app-private storage.
 * - Uses explicit event type information.
 * - Does NOT rely on Gson polymorphic deserialization.
 * - Supports all ExecutionEvent subclasses used by VMAX.
 * - Uses atomic temporary-file replacement when saving.
 *
 * Storage:
 *   filesDir/execution_history/<sessionId>.json
 */
class AndroidExecutionHistoryStore(
    private val context: Context
) {

    private val gson = Gson()

    /**
     * Directory:
     *
     * /data/data/<package>/files/execution_history/
     */
    private val historyDir: File by lazy {
        File(context.filesDir, HISTORY_DIRECTORY).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    /**
     * Saves the complete event timeline for a session.
     *
     * Event type is explicitly written as "className".
     * This is required because ExecutionEvent is polymorphic.
     */
    suspend fun saveSessionEvents(
        sessionId: String,
        events: List<ExecutionEvent>
    ) {
        if (sessionId.isBlank()) {
            return
        }

        withContext(Dispatchers.IO) {

            val safeSessionId = sanitizeSessionId(sessionId)

            val targetFile = File(
                historyDir,
                "$safeSessionId.json"
            )

            val temporaryFile = File(
                historyDir,
                "$safeSessionId.tmp"
            )

            val jsonArray = JsonArray()

            events.forEach { event ->
                jsonArray.add(serializeEvent(event))
            }

            temporaryFile.writeText(
                gson.toJson(jsonArray),
                Charsets.UTF_8
            )

            /*
             * Atomic-ish replacement:
             *
             * Write completely first, then replace
             * the previous history file.
             */
            if (targetFile.exists()) {
                targetFile.delete()
            }

            if (!temporaryFile.renameTo(targetFile)) {

                /*
                 * Fallback for filesystems where renameTo()
                 * may fail.
                 */
                targetFile.writeText(
                    gson.toJson(jsonArray),
                    Charsets.UTF_8
                )

                temporaryFile.delete()
            }
        }
    }

    /**
     * Retrieves all events belonging to one session.
     *
     * Returns emptyList() when:
     * - session does not exist
     * - file is empty
     * - JSON is invalid
     */
    suspend fun getSessionEvents(
        sessionId: String
    ): List<ExecutionEvent> {

        if (sessionId.isBlank()) {
            return emptyList()
        }

        return withContext(Dispatchers.IO) {

            val safeSessionId = sanitizeSessionId(sessionId)

            val file = File(
                historyDir,
                "$safeSessionId.json"
            )

            if (!file.exists()) {
                return@withContext emptyList()
            }

            val json = try {
                file.readText(Charsets.UTF_8)
            } catch (_: Exception) {
                return@withContext emptyList()
            }

            if (json.isBlank()) {
                return@withContext emptyList()
            }

            try {

                val root = JsonParser.parseString(json)

                if (!root.isJsonArray) {
                    return@withContext emptyList()
                }

                parseExecutionEvents(
                    root.asJsonArray
                )

            } catch (_: Exception) {

                /*
                 * Corrupt history must never crash
                 * the application.
                 */
                emptyList()
            }
        }
    }

    /**
     * Deletes history belonging to one session.
     */
    suspend fun deleteSession(
        sessionId: String
    ) {

        if (sessionId.isBlank()) {
            return
        }

        withContext(Dispatchers.IO) {

            val safeSessionId = sanitizeSessionId(sessionId)

            val file = File(
                historyDir,
                "$safeSessionId.json"
            )

            if (file.exists()) {
                file.delete()
            }

            val temporaryFile = File(
                historyDir,
                "$safeSessionId.tmp"
            )

            if (temporaryFile.exists()) {
                temporaryFile.delete()
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
                file.isFile &&
                    file.extension.equals(
                        "json",
                        ignoreCase = true
                    )
            }
            ?.map { file ->
                file.nameWithoutExtension
            }
            ?.sorted()
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

    // ---------------------------------------------------------------------
    // SERIALIZATION
    // ---------------------------------------------------------------------

    /**
     * Converts ExecutionEvent into an explicit JSON object.
     *
     * The "className" field is deliberately stored because Gson
     * cannot automatically reconstruct sealed/polymorphic event types.
     */
    private fun serializeEvent(
        event: ExecutionEvent
    ): JsonObject {

        val json = JsonObject()

        when (event) {

            is ExecutionEvent.SessionStarted -> {

                json.addProperty(
                    "className",
                    "SessionStarted"
                )

                json.addProperty(
                    "sessionId",
                    event.sessionId
                )

                json.addProperty(
                    "timestamp",
                    event.timestamp
                )
            }

            is ExecutionEvent.SessionStopped -> {

                json.addProperty(
                    "className",
                    "SessionStopped"
                )

                json.addProperty(
                    "sessionId",
                    event.sessionId
                )

                json.addProperty(
                    "timestamp",
                    event.timestamp
                )
            }

            is ExecutionEvent.SessionError -> {

                json.addProperty(
                    "className",
                    "SessionError"
                )

                json.addProperty(
                    "sessionId",
                    event.sessionId
                )

                json.addProperty(
                    "errorCode",
                    event.errorCode
                )

                json.addProperty(
                    "errorMessage",
                    event.errorMessage
                )

                json.addProperty(
                    "timestamp",
                    event.timestamp
                )
            }

            is ExecutionEvent.WorkflowStateChanged -> {

                json.addProperty(
                    "className",
                    "WorkflowStateChanged"
                )

                json.addProperty(
                    "sessionId",
                    event.sessionId
                )

                json.addProperty(
                    "fromState",
                    event.fromState
                )

                json.addProperty(
                    "toState",
                    event.toState
                )

                json.addProperty(
                    "timestamp",
                    event.timestamp
                )
            }

            is ExecutionEvent.ActionDispatched -> {

                json.addProperty(
                    "className",
                    "ActionDispatched"
                )

                json.addProperty(
                    "sessionId",
                    event.sessionId
                )

                json.addProperty(
                    "actionType",
                    event.actionType.name
                )

                event.targetId?.let {
                    json.addProperty(
                        "targetId",
                        it
                    )
                }

                event.targetText?.let {
                    json.addProperty(
                        "targetText",
                        it
                    )
                }

                json.addProperty(
                    "timestamp",
                    event.timestamp
                )
            }

            is ExecutionEvent.ActionSucceeded -> {

                json.addProperty(
                    "className",
                    "ActionSucceeded"
                )

                json.addProperty(
                    "sessionId",
                    event.sessionId
                )

                json.addProperty(
                    "actionType",
                    event.actionType.name
                )

                event.resultMessage?.let {
                    json.addProperty(
                        "resultMessage",
                        it
                    )
                }

                json.addProperty(
                    "timestamp",
                    event.timestamp
                )
            }

            is ExecutionEvent.ActionFailed -> {

                json.addProperty(
                    "className",
                    "ActionFailed"
                )

                json.addProperty(
                    "sessionId",
                    event.sessionId
                )

                json.addProperty(
                    "actionType",
                    event.actionType.name
                )

                json.addProperty(
                    "errorCode",
                    event.errorCode
                )

                json.addProperty(
                    "errorMessage",
                    event.errorMessage
                )

                json.addProperty(
                    "timestamp",
                    event.timestamp
                )
            }
        }

        return json
    }

    // ---------------------------------------------------------------------
    // DESERIALIZATION
    // ---------------------------------------------------------------------

    /**
     * Reconstructs ExecutionEvent objects from JSON.
     */
    private fun parseExecutionEvents(
        array: JsonArray
    ): List<ExecutionEvent> {

        val events = mutableListOf<ExecutionEvent>()

        array.forEach { element ->

            if (!element.isJsonObject) {
                return@forEach
            }

            val obj = element.asJsonObject

            val className =
                obj.stringOrNull("className")
                    ?: return@forEach

            val sessionId =
                obj.stringOrNull("sessionId")
                    ?: return@forEach

            val timestamp =
                obj.longOrDefault(
                    "timestamp",
                    System.currentTimeMillis()
                )

            try {

                when (className) {

                    "SessionStarted" -> {

                        events +=
                            ExecutionEvent.SessionStarted(
                                sessionId,
                                timestamp
                            )
                    }

                    "SessionStopped" -> {

                        events +=
                            ExecutionEvent.SessionStopped(
                                sessionId,
                                timestamp
                            )
                    }

                    "SessionError" -> {

                        events +=
                            ExecutionEvent.SessionError(
                                sessionId,
                                obj.stringOrEmpty("errorCode"),
                                obj.stringOrEmpty("errorMessage"),
                                timestamp
                            )
                    }

                    "WorkflowStateChanged" -> {

                        events +=
                            ExecutionEvent.WorkflowStateChanged(
                                sessionId,
                                obj.stringOrEmpty("fromState"),
                                obj.stringOrEmpty("toState"),
                                timestamp
                            )
                    }

                    "ActionDispatched" -> {

                        events +=
                            ExecutionEvent.ActionDispatched(
                                sessionId,
                                parseActionType(
                                    obj.stringOrNull("actionType")
                                ),
                                obj.stringOrNull("targetId"),
                                obj.stringOrNull("targetText"),
                                timestamp
                            )
                    }

                    "ActionSucceeded" -> {

                        events +=
                            ExecutionEvent.ActionSucceeded(
                                sessionId,
                                parseActionType(
                                    obj.stringOrNull("actionType")
                                ),
                                obj.stringOrNull("resultMessage"),
                                timestamp
                            )
                    }

                    "ActionFailed" -> {

                        events +=
                            ExecutionEvent.ActionFailed(
                                sessionId,
                                parseActionType(
                                    obj.stringOrNull("actionType")
                                ),
                                obj.stringOrEmpty("errorCode"),
                                obj.stringOrEmpty("errorMessage"),
                                timestamp
                            )
                    }
                }

            } catch (_: Exception) {

                /*
                 * A malformed individual event must not
                 * destroy the complete session history.
                 */
            }
        }

        return events
    }

    // ---------------------------------------------------------------------
    // HELPERS
    // ---------------------------------------------------------------------

    /**
     * Safely converts stored action type into ActionType.
     *
     * This replaces the problematic generic enumValueOf<T>() approach.
     */
    private fun parseActionType(
        value: String?
    ): ActionExecutor.ActionType {

        if (value.isNullOrBlank()) {
            return ActionExecutor.ActionType.CLICK
        }

        return ActionExecutor.ActionType.entries
            .firstOrNull {
                it.name.equals(
                    value,
                    ignoreCase = true
                )
            }
            ?: ActionExecutor.ActionType.CLICK
    }

    private fun JsonObject.stringOrNull(
        key: String
    ): String? {

        val element = get(key)

        if (
            element == null ||
            element.isJsonNull
        ) {
            return null
        }

        return try {
            element.asString
        } catch (_: Exception) {
            null
        }
    }

    private fun JsonObject.stringOrEmpty(
        key: String
    ): String {
        return stringOrNull(key) ?: ""
    }

    private fun JsonObject.longOrDefault(
        key: String,
        default: Long
    ): Long {

        val element = get(key)

        if (
            element == null ||
            element.isJsonNull
        ) {
            return default
        }

        return try {
            element.asLong
        } catch (_: Exception) {
            default
        }
    }

    /**
     * Prevents a session ID from creating an unsafe file path.
     */
    private fun sanitizeSessionId(
        sessionId: String
    ): String {

        val sanitized = sessionId
            .trim()
            .replace(
                Regex("[^A-Za-z0-9._-]"),
                "_"
            )

        return sanitized.ifBlank {
            "unknown_session"
        }
    }

    companion object {

        private const val HISTORY_DIRECTORY =
            "execution_history"
    }
}
