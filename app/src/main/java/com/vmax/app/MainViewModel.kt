package com.vmax.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmax.model.*
import com.vmax.workflow.WorkflowController
import com.vmax.workflow.WorkflowState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class MainViewModel : ViewModel() {

    private val workflowController = WorkflowController.getInstance()

    // ---- UI States ----

    private val _trainNumber = MutableStateFlow("")
    val trainNumber: StateFlow<String> = _trainNumber.asStateFlow()

    private val _trainName = MutableStateFlow("")
    val trainName: StateFlow<String> = _trainName.asStateFlow()

    private val _classType = MutableStateFlow("")
    val classType: StateFlow<String> = _classType.asStateFlow()

    private val _quota = MutableStateFlow<Quota?>(null)
    val quota: StateFlow<Quota?> = _quota.asStateFlow()

    private val _fromStation = MutableStateFlow("")
    val fromStation: StateFlow<String> = _fromStation.asStateFlow()

    private val _toStation = MutableStateFlow("")
    val toStation: StateFlow<String> = _toStation.asStateFlow()

    private val _journeyDate = MutableStateFlow("")
    val journeyDate: StateFlow<String> = _journeyDate.asStateFlow()

    private val _passengerName = MutableStateFlow("")
    val passengerName: StateFlow<String> = _passengerName.asStateFlow()

    private val _passengerAge = MutableStateFlow("")
    val passengerAge: StateFlow<String> = _passengerAge.asStateFlow()

    private val _passengerGender = MutableStateFlow("MALE")
    val passengerGender: StateFlow<String> = _passengerGender.asStateFlow()

    private val _passengerMobile = MutableStateFlow("")
    val passengerMobile: StateFlow<String> = _passengerMobile.asStateFlow()

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError.asStateFlow()

    val workflowState: StateFlow<WorkflowState> = workflowController.state

    // ---- Update Methods ----

    fun updateTrainNumber(value: String) {
        if (value.all { it.isDigit() }) {
            _trainNumber.value = value
        }
    }

    fun updateTrainName(value: String) {
        // Read-only, no-op
    }

    fun updateClassType(value: String) {
        _classType.value = value
    }

    fun updateQuota(value: Quota?) {
        _quota.value = value
    }

    fun updateFromStation(value: String) {
        _fromStation.value = value
    }

    fun updateToStation(value: String) {
        _toStation.value = value
    }

    fun updateJourneyDate(value: String) {
        _journeyDate.value = value
    }

    fun updatePassengerName(value: String) {
        _passengerName.value = value
    }

    fun updatePassengerAge(value: String) {
        _passengerAge.value = value
    }

    fun updatePassengerGender(value: String) {
        _passengerGender.value = value
    }

    fun updatePassengerMobile(value: String) {
        _passengerMobile.value = value
    }

    // ---- Workflow ----

    fun startWorkflow() {
        _validationError.value = null

        if (trainNumber.value.isBlank()) {
            _validationError.value = "Train Number is required."
            return
        }

        if (!trainNumber.value.matches(Regex("^\\d{4,5}$"))) {
            _validationError.value =
                "Train Number must be 4 or 5 digits."
            return
        }

        if (classType.value.isBlank()) {
            _validationError.value =
                "Class Type is required."
            return
        }

        if (quota.value == null) {
            _validationError.value =
                "Quota is required."
            return
        }

        if (fromStation.value.isBlank()) {
            _validationError.value =
                "From Station is required."
            return
        }

        if (toStation.value.isBlank()) {
            _validationError.value =
                "To Station is required."
            return
        }

        if (journeyDate.value.isBlank()) {
            _validationError.value =
                "Journey Date is required."
            return
        }

        if (passengerName.value.isBlank()) {
            _validationError.value =
                "Passenger Name is required."
            return
        }

        val ageInt = passengerAge.value.toIntOrNull()

        if (ageInt == null || ageInt !in 1..120) {
            _validationError.value =
                "Valid Age (1-120) is required."
            return
        }

        if (
            passengerMobile.value.isNotBlank() &&
            !passengerMobile.value.matches(
                Regex("^[6-9]\\d{9}$")
            )
        ) {
            _validationError.value =
                "Mobile must be exactly 10 digits starting with 6-9."
            return
        }

        val train = Train(
            number = trainNumber.value,
            name = trainName.value,
            classType = classType.value,
            quota = quota.value?.name ?: "GENERAL"
        )

        val fromStation = Station(
            fromStation.value,
            fromStation.value
        )

        val toStation = Station(
            toStation.value,
            toStation.value
        )

        val passenger = Passenger(
            name = passengerName.value,
            age = ageInt,
            gender = passengerGender.value,
            mobile = passengerMobile.value
                .takeIf { it.isNotBlank() }
        )

        val profileId = UUID.randomUUID().toString()

        val bookingRequest = BookingRequest(
            train = train,
            fromStation = fromStation,
            toStation = toStation,
            date = journeyDate.value,
            passengers = listOf(passenger),
            quota = quota.value?.name ?: "GENERAL"
        )

        val passengerProfile = PassengerProfile(
            profileId = profileId,
            passengers = listOf(passenger),
            createdTime = LocalDateTime.now(),
            updatedTime = LocalDateTime.now()
        )

        viewModelScope.launch {
            workflowController.start(
                bookingRequest,
                passengerProfile
            )
        }
    }

    fun stopWorkflow() {
        viewModelScope.launch {
            workflowController.stop()
        }
    }

    // IMPORTANT:
    // WorkflowState is an enum.
    // Therefore use ==, NOT "is".

    fun isWorkflowActive(): Boolean =
        workflowState.value == WorkflowState.RUNNING ||
        workflowState.value == WorkflowState.CONFIGURED
}
