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
    private var workflowController: WorkflowController? = null
    private val _workflowState = MutableStateFlow(WorkflowState.IDLE)
    val workflowState: StateFlow<WorkflowState> = _workflowState.asStateFlow()

    private val _trainNumber = MutableStateFlow("")
    val trainNumber: StateFlow<String> = _trainNumber.asStateFlow()
    private val _trainName = MutableStateFlow("")
    val trainName: StateFlow<String> = _trainName.asStateFlow()
    private val _classType = MutableStateFlow("")
    val classType: StateFlow<String> = _classType.asStateFlow()

    // Quota अब String है (Enum नहीं) - यही गड़बड़ी थी!
    private val _quota = MutableStateFlow<String?>(null)
    val quota: StateFlow<String?> = _quota.asStateFlow()

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

    fun updateTrainNumber(value: String) { val cleaned = value.trim(); if (cleaned.isEmpty() || cleaned.all { it.isDigit() }) _trainNumber.value = cleaned }
    fun updateTrainName(value: String) { _trainName.value = value.trim() }
    fun updateClassType(value: String) { _classType.value = value.trim() }
    fun updateQuota(value: String?) { _quota.value = value } // String accept कर रहा है
    fun updateFromStation(value: String) { _fromStation.value = value.trim().uppercase() }
    fun updateToStation(value: String) { _toStation.value = value.trim().uppercase() }
    fun updateJourneyDate(value: String) { _journeyDate.value = value.trim() }
    fun updatePassengerName(value: String) { _passengerName.value = value.trim() }
    fun updatePassengerAge(value: String) { val cleaned = value.trim(); if (cleaned.isEmpty() || cleaned.all { it.isDigit() }) _passengerAge.value = cleaned }
    fun updatePassengerGender(value: String) { _passengerGender.value = value.trim().uppercase() }
    fun updatePassengerMobile(value: String) { val cleaned = value.trim(); if (cleaned.isEmpty() || cleaned.all { it.isDigit() }) _passengerMobile.value = cleaned }

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

        if (trainNumberValue.isBlank()) return validationError("Train Number is required.")
        if (!trainNumberValue.matches(Regex("^\\d{4,5}$"))) return validationError("Train Number must be 4 or 5 digits.")
        if (classTypeValue.isBlank()) return validationError("Class Type is required.")
        if (quota.value.isNullOrBlank()) return validationError("Quota is required.")
        if (fromStationValue.isBlank()) return validationError("From Station is required.")
        if (toStationValue.isBlank()) return validationError("To Station is required.")
        if (fromStationValue.length < 2 || toStationValue.length < 2) return validationError("Station codes are invalid.")
        if (journeyDateValue.isBlank()) return validationError("Journey Date is required.")
        try { LocalDate.parse(journeyDateValue) } catch (_: Exception) { return validationError("Journey Date must be in YYYY-MM-DD format.") }
        if (passengerNameValue.isBlank()) return validationError("Passenger Name is required.")
        val age = passengerAgeValue.toIntOrNull()
        if (age == null || age !in 1..120) return validationError("Valid Age (1-120) is required.")
        if (passengerMobileValue.isNotBlank() && !passengerMobileValue.matches(Regex("^[6-9]\\d{9}$"))) return validationError("Mobile must be exactly 10 digits starting with 6-9.")
        return true
    }

    private fun validationError(message: String): Boolean { _validationError.value = message; return false }

    private fun getWorkflowController(): WorkflowController? {
        workflowController?.let { return it }
        return try {
            val controller = WorkflowController.getInstance()
            workflowController = controller
            viewModelScope.launch { try { controller.state.collect { state -> _workflowState.value = state } } catch (error: Throwable) { _workflowState.value = WorkflowState.ERROR; _validationError.value = error.message?.takeIf { it.isNotBlank() } ?: "Workflow state observer failed." } }
            controller
        } catch (error: Throwable) {
            _workflowState.value = WorkflowState.ERROR
            _validationError.value = error.message?.takeIf { it.isNotBlank() } ?: "Workflow controller initialization failed."
            null
        }
    }

    fun startWorkflow() {
        if (isWorkflowActive()) return
        if (!validateInputs()) return
        val controller = getWorkflowController() ?: return
        try {
            val passenger = Passenger(name = passengerName.value.trim(), age = passengerAge.value.trim().toInt(), gender = passengerGender.value.trim().uppercase(), mobile = passengerMobile.value.trim().takeIf { it.isNotBlank() })
            val train = Train(number = trainNumber.value.trim(), name = trainName.value.trim(), classType = classType.value.trim(), quota = quota.value) // String
            val fromStationModel = Station(fromStation.value.trim(), fromStation.value.trim())
            val toStationModel = Station(toStation.value.trim(), toStation.value.trim())
            val bookingRequest = BookingRequest(train = train, fromStation = fromStationModel, toStation = toStationModel, date = journeyDate.value.trim(), passengers = listOf(passenger), quota = quota.value ?: "GENERAL") // String
            val now = LocalDateTime.now()
            val passengerProfile = PassengerProfile(profileId = UUID.randomUUID().toString(), passengers = listOf(passenger), createdTime = now, updatedTime = now)
            viewModelScope.launch { try { controller.start(bookingRequest, passengerProfile) } catch (error: Throwable) { _workflowState.value = WorkflowState.ERROR; _validationError.value = error.message?.takeIf { it.isNotBlank() } ?: "Unable to start workflow." } }
        } catch (error: Throwable) { _workflowState.value = WorkflowState.ERROR; _validationError.value = error.message?.takeIf { it.isNotBlank() } ?: "Invalid workflow configuration." }
    }

    fun stopWorkflow() {
        if (!isWorkflowActive()) return
        val controller = workflowController ?: return
        viewModelScope.launch { try { controller.stop() } catch (error: Throwable) { _workflowState.value = WorkflowState.ERROR; _validationError.value = error.message?.takeIf { it.isNotBlank() } ?: "Unable to stop workflow." } }
    }

    fun isWorkflowActive(): Boolean = _workflowState.value == WorkflowState.RUNNING || _workflowState.value == WorkflowState.CONFIGURED

    override fun onCleared() { workflowController = null; super.onCleared() }
}
