package com.vmax.security

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 18 — SecureLogger
 *
 * Secure logging that sanitizes sensitive data before output.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface SecureLogger {

    data class LogEntry(
        val tag: String,
        val message: String,
        val timestamp: Long = System.currentTimeMillis(),
        val level: String = "INFO",
        val sensitiveFields: List<String> = emptyList()
    )

    data class LogSanitizationResult(
        val originalMessage: String,
        val sanitizedMessage: String,
        val redactedFields: List<String>
    )

    fun log(entry: LogEntry): Result<Unit, SecureLogError>

    fun sanitizeMessage(
        message: String,
        sensitiveFields: List<String>
    ): LogSanitizationResult

    fun isSensitiveData(value: String): Boolean

    fun redactSensitiveData(value: String): String

    fun getRedactedFields(): List<String>

    fun addRedactionPattern(pattern: String)

    fun clearRedactionPatterns()

    fun getLogHistory(): List<LogEntry>

    fun clearHistory()

    fun setLoggingEnabled(enabled: Boolean)

    fun isLoggingEnabled(): Boolean
}

data class SecureLogError(
    val code: String,
    val message: String,
    val tag: String? = null,
    val sensitiveField: String? = null
)
