package com.vmax.app

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.vmax.common.Result
import com.vmax.model.BookingRequest
import com.vmax.model.PassengerProfile
import com.vmax.runtime.SessionManager
import com.vmax.runtime.SessionError
import java.util.UUID

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidSessionManager.kt
 *
 * Android implementation of the SessionManager interface.
 * Stores session data in SharedPreferences.
 *
 * Responsibilities:
 * - Create and manage session IDs.
 * - Persist BookingRequest and PassengerProfile.
 * - Provide session history retrieval.
 */
class AndroidSessionManager(
    private val context: Context
) : SessionManager {

    companion object {
        private const val PREFS_NAME = "vmax_session_prefs"
        private const val KEY_SESSION_ID = "session_id"
        private const val KEY_BOOKING_REQUEST = "booking_request"
        private const val KEY_PASSENGER_PROFILE = "passenger_profile"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun createSession(): Result<SessionManager.SessionData, SessionError> {
        val sessionId = UUID.randomUUID().toString()
        prefs.edit().putString(KEY_SESSION_ID, sessionId).apply()

        val sessionData = SessionManager.SessionData(
            sessionId = sessionId
        )

        return Result.Success(sessionData)
    }

    override fun getCurrentSession(): Result<SessionManager.SessionData, SessionError> {
        val sessionId = prefs.getString(KEY_SESSION_ID, null)
        return if (sessionId != null) {
            val bookingRequest = getBookingRequest()
            val passengerProfile = getPassengerProfile()
            Result.Success(
                SessionManager.SessionData(
                    sessionId = sessionId,
                    bookingRequest = bookingRequest,
                    passengerProfile = passengerProfile
                )
            )
        } else {
            Result.Error(
                SessionError(
                    code = "SESSION_NOT_FOUND",
                    message = "No active session found."
                )
            )
        }
    }

    override fun saveBookingRequest(request: BookingRequest): Result<Unit, SessionError> {
        val json = gson.toJson(request)
        prefs.edit().putString(KEY_BOOKING_REQUEST, json).apply()
        return Result.Success(Unit)
    }

    override fun getBookingRequest(): BookingRequest? {
        val json = prefs.getString(KEY_BOOKING_REQUEST, null)
        return if (json != null) {
            gson.fromJson(json, BookingRequest::class.java)
        } else {
            null
        }
    }

    override fun savePassengerProfile(profile: PassengerProfile): Result<Unit, SessionError> {
        val json = gson.toJson(profile)
        prefs.edit().putString(KEY_PASSENGER_PROFILE, json).apply()
        return Result.Success(Unit)
    }

    override fun getPassengerProfile(): PassengerProfile? {
        val json = prefs.getString(KEY_PASSENGER_PROFILE, null)
        return if (json != null) {
            gson.fromJson(json, PassengerProfile::class.java)
        } else {
            null
        }
    }

    override fun clearSession(): Result<Unit, SessionError> {
        prefs.edit().clear().apply()
        return Result.Success(Unit)
    }

    override fun sessionExists(): Boolean {
        return prefs.contains(KEY_SESSION_ID)
    }
}
