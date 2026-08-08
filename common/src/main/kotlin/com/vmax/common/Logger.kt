package com.vmax.common

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 2 — Logger
 *
 * Generic logging contract for VMAX Enterprise.
 * Platform-independent — no Android dependencies.
 * No external logging libraries.
 * No business logic.
 *
 * Implementations can be provided by platform-specific modules
 * (e.g., Android Logger using Logcat, JVM Logger using Console).
 */
interface Logger {

    fun log(level: LogLevel, tag: String, message: String)

    fun debug(tag: String, message: String) = log(LogLevel.DEBUG, tag, message)

    fun info(tag: String, message: String) = log(LogLevel.INFO, tag, message)

    fun warn(tag: String, message: String) = log(LogLevel.WARN, tag, message)

    fun error(tag: String, message: String) = log(LogLevel.ERROR, tag, message)
}

enum class LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR
}
