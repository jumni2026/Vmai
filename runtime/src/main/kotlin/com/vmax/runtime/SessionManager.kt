package com.vmax.runtime

import com.vmax.common.Result
import com.vmax.model.BookingRequest
import com.vmax.model.PassengerProfile

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 20 — SessionManager
 *
 * Manages session data persistence across the workflow.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 *
 * Minimal contract — session state is tracked by WorkflowController.
 * SessionManager only handles data storage and retrieval.
 */
interface SessionManager {

    data class SessionData(
        val sessionId: String,
        val bookingRequest: BookingRequest? = null,
        val passengerProfile: PassengerProfile? = null,
        val timestamp: Long = System.currentTimeMillis()
    )

    fun createSession(): Result<SessionData, SessionError>

    fun getCurrentSession(): Result<SessionData, SessionError>

    fun saveBookingRequest(request: BookingRequest): Result<Unit, SessionError>

    fun getBookingRequest(): BookingRequest?

    fun savePassengerProfile(profile: PassengerProfile): Result<Unit, SessionError>

    fun getPassengerProfile(): PassengerProfile?

    fun clearSession(): Result<Unit, SessionError>

    fun sessionExists(): Boolean
}

data class SessionError(
    val code: String,
    val message: String,
    val sessionId: String? = null
)
