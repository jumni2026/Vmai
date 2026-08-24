package com.vmax.model

import java.time.LocalDateTime

data class Train(
    val number: String,
    val name: String,
    val classType: String,
    val quota: Quota? = null
)

data class Station(val code: String, val name: String)

data class Passenger(
    val name: String,
    val age: Int,
    val gender: String,
    val mobile: String? = null
)

data class BookingRequest(
    val train: Train,
    val fromStation: Station,
    val toStation: Station,
    val date: String,
    val passengers: List<Passenger>,
    val quota: Quota
)

data class PassengerProfile(
    val profileId: String,
    val passengers: List<Passenger>,
    val createdTime: LocalDateTime,
    val updatedTime: LocalDateTime,
    val version: Int = 1,
    val berthPreference: BerthPreference = BerthPreference.NO_PREFERENCE,
    val mealPreference: MealPreference = MealPreference.NO_MEAL,
    val concession: Concession = Concession.NONE,
    val bedRoll: Boolean = false
)

enum class Gender { MALE, FEMALE, OTHER }

// Quota Enum (यही सबसे महत्वपूर्ण है)
enum class Quota { GENERAL, TATKAL, LADIES, SENIOR_CITIZEN, DEFENCE, YOUTH, FOREIGN_TOURIST }

enum class BerthPreference { NO_PREFERENCE, LOWER, MIDDLE, UPPER, SIDE_LOWER, SIDE_UPPER }
enum class MealPreference { NO_MEAL, VEG, NON_VEG, VEG_JAIN }
enum class Concession { NONE, SENIOR_CITIZEN, DISABLED }
