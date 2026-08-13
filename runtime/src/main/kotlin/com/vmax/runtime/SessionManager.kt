package com.vmax.runtime

import com.vmax.common.Result
import com.vmax.model.BookingRequest
import com.vmax.model.PassengerProfile

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
