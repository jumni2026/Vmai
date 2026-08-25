package com.vmax.model

data class Train(
    val number: String,
    val name: String,
    val classType: String,
    val quota: String? = null
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
    val quota: String
)

enum class Gender { MALE, FEMALE, OTHER }
