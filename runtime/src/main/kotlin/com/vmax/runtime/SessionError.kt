package com.vmax.runtime

/**
 * VMAX Enterprise v2.6.1
 *
 * File — SessionError.kt
 *
 * Platform-independent session error contract.
 */
data class SessionError(
    val code: String,
    val message: String,
    val sessionId: String? = null
)
