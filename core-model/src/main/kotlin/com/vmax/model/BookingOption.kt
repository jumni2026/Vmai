package com.vmax.model

data class BookingOption(
    val autoUpgradation: Boolean = false,
    val confirmBerths: Boolean = false,
    val travelInsurance: Boolean = false,
    val coachPreferred: Boolean = false,
    val coachId: String? = null,
    val mobileNumber: String? = null
)
