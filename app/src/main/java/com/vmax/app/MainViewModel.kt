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
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class MainViewModel : ViewModel() {

    /*
     * Keep existing singleton contract.
     * Do not change WorkflowController architecture here.
     */
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
    val validationError: StateFlow<String?> =
        _validationError.asStateFlow()

    val workflowState: StateFlow<WorkflowState> =
        workflowController.state

    // ---- Update Methods ----

    fun updateTrainNumber(value: String) {
        val cleaned = value.trim()

        if (cleaned.all { it.isDigit() }) {
            _trainNumber.value = cleaned
        }
    }

    fun updateTrainName(value: String) {
        /*
         * Train name is currently read-only / auto-populated.
         * Keep this method for existing UI/API compatibility.
         */
    }

    fun updateClassType(value: String) {
        _classType.value = value.trim()
    }

    fun updateQuota(value: Quota?) {
        _quota.value = value
    }

    fun updateFromStation(value: String) {
        _fromStation.value = value.trim().uppercase()
    }

    fun updateToStation(value: String) {
        _toStation.value = value.trim().uppercase()
    }

    fun updateJourneyDate(value: String) {
        _journeyDate.value = value.trim()
    }

    fun updatePassengerName(value: String) {
        _passengerName.value = value.trim()
    }

    fun updatePassengerAge(value: String) {
        val cleaned = value.trim()

        if (cleaned.all { it.isDigit() }) {
            _passengerAge.value = cleaned
        }
    }

    fun updatePassengerGender(value: String) {
        _passengerGender.value = value.trim().uppercase()
    }

    fun updatePassengerMobile(value: String) {
        val cleaned = value.trim()

        if (cleaned.all { it.isDigit() }) {
            _passengerMobile.value = cleaned
        }
    }

    // ---- Validation ----

    private fun validateInputs(): Boolean {

        _validationError.value = null

        val trainNumberValue = trainNumber.value.trim()
        val classTypeValue = classType.value.trim()
        val fromStationValue = fromStation.value.trim()
        val toStationValue = toStation.value.trim()
        val journeyDateValue = journeyDate.value.trim()
        val passengerNameValue = passengerName.value.trim()
        val passengerAgeValue = passengerAge.value.trim()
        val passengerMobileValue = passengerMobile.value.trim()

        if (trainNumberValue.isBlank()) {
            _validationError.value =
                "Train Number is required."
            return false
        }

        if (!trainNumberValue.matches(Regex("^\\d{4,5}$"))) {
            _validationError.value =
                "Train Number must be 4 or 5 digits."
            return false
        }

        if (classTypeValue.isBlank()) {
            _validationError.value =
                "Class Type is required."
            return false
        }

        if (quota.value == null) {
            _validationError.value =
                "Quota is required."
            return false
        }

        if (fromStationValue.isBlank()) {
            _validationError.value =
                "From Station is required."
            return false
        }

        if (toStationValue.isBlank()) {
            _validationError.value =
                "To Station is required."
            return false
        }

        if (journeyDateValue.isBlank()) {
            _validationError.value =
                "Journey Date is required."
            return false
        }

        /*
         * Validate the date instead of accepting arbitrary text.
         */
        try {
            LocalDate.parse(journeyDateValue)
        } catch (_: Exception) {
            _validationError.value =
                "Journey Date must be in YYYY-MM-DD format."
            return false
        }

        if (passengerNameValue.isBlank()) {
            _validationError.value =
                "Passenger Name is required."
            return false
        }

        val ageInt = passengerAgeValue.toIntOrNull()

        if (ageInt == null || ageInt !in 1..120) {
            _validationError.value =
                "Valid Age (1-120) is required."
            return false
        }

        if (
            passengerMobileValue.isNotBlank() &&
            !passengerMobileValue.matches(
                Regex("^[6-9]\\d{9}$")
            )
        ) {
            _validationError.value =
                "Mobile must be exactly 10 digits starting with 6-9."
            return false
        }

        return true
    }

    // ---- Workflow ----

    fun startWorkflow() {

        /*
         * Prevent accidental duplicate start.
         */
        if (isWorkflowActive()) {
            return
        }

        if (!validateInputs()) {
            return
        }

        try {

            val train = Train(
                number = trainNumber.value.trim(),
                name = trainName.value.trim(),
                classType = classType.value.trim(),
                quota = quota.value?.name ?: "GENERAL"
            )

            val fromStationModel = Station(
                fromStation.value.trim(),
                fromStation.value.trim()
            )

            val toStationModel = Station(
                toStation.value.trim(),
                toStation.value.trim()
            )

            val passenger = Passenger(
                name = passengerName.value.trim(),
                age = passengerAge.value.trim().toInt(),
                gender = passengerGender.value.trim().uppercase(),
                mobile = passengerMobile.value
                    .trim()
                    .takeIf { it.isNotBlank() }
            )

            val profileId = UUID.randomUUID().toString()

            val bookingRequest = BookingRequest(
                train = train,
                fromStation = fromStationModel,
                toStation = toStationModel,
                date = journeyDate.value.trim(),
                passengers = listOf(passenger),
                quota = quota.value?.name ?: "GENERAL"
            )

            val now = LocalDateTime.now()

            val passengerProfile = PassengerProfile(
                profileId = profileId,
                passengers = listOf(passenger),
                createdTime = now,
                updatedTime = now
            )

            viewModelScope.launch {

                try {

                    workflowController.start(
                        bookingRequest,
                        passengerProfile
                    )

                } catch (error: Exception) {

                    _validationError.value =
                        error.message
                            ?.takeIf { it.isNotBlank() }
                            ?: "Unable to start workflow."
                }
            }

        } catch (error: Exception) {

            _validationError.value =
                error.message
                    ?.takeIf { it.isNotBlank() }
                    ?: "Invalid workflow configuration."
        }
    }

    fun stopWorkflow() {

        if (!isWorkflowActive()) {
            return
        }

        viewModelScope.launch {

            try {

                workflowController.stop()

            } catch (error: Exception) {

                _validationError.value =
                    error.message
                        ?.takeIf { it.isNotBlank() }
                        ?: "Unable to stop workflow."
            }
        }
    }

    // ---- Workflow State ----

    fun isWorkflowActive(): Boolean =
        workflowState.value == WorkflowState.RUNNING ||
        workflowState.value == WorkflowState.CONFIGURED
}
