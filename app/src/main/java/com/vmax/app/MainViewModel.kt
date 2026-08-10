package com.vmax.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmax.model.*
import com.vmax.workflow.WorkflowController
import com.vmax.workflow.WorkflowController.WorkflowState   // ✅ Added import
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

/**
 * VMAX Enterprise v2.6.1
 *
 * File — MainViewModel
 *
 * Responsibility:
 * - UI state management
 * - Validation of user inputs
 * - Domain Model Construction
 * - WorkflowController interaction
 */
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

    private val _berthPreference = MutableStateFlow(BerthPreference.NO_PREFERENCE)
    val berthPreference: StateFlow<BerthPreference> = _berthPreference.asStateFlow()

    private val _mealPreference = MutableStateFlow(MealPreference.NO_MEAL)
    val mealPreference: StateFlow<MealPreference> = _mealPreference.asStateFlow()

    private val _concession = MutableStateFlow(Concession.NONE)
    val concession: StateFlow<Concession> = _concession.asStateFlow()

    private val _bedRoll = MutableStateFlow(false)
    val bedRoll: StateFlow<Boolean> = _bedRoll.asStateFlow()

    private val _children = MutableStateFlow<List<ChildData>>(emptyList())
    val children: StateFlow<List<ChildData>> = _children.asStateFlow()

    private val _bookingOption = MutableStateFlow(BookingOption())
    val bookingOption: StateFlow<BookingOption> = _bookingOption.asStateFlow()

    private val _paymentMethod = MutableStateFlow(PaymentMethod())
    val paymentMethod: StateFlow<PaymentMethod> = _paymentMethod.asStateFlow()

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError.asStateFlow()

    // ---- Workflow state ----
    val workflowState: StateFlow<WorkflowState> = workflowController.state

    // ---- Public update methods ----
    fun updateTrainNumber(value: String) {
        if (value.all { it.isDigit() }) {
            _trainNumber.value = value
        }
    }

    fun updateTrainName(value: String) { /* Read-only, no-op */ }
    fun updateClassType(value: String) { _classType.value = value }
    fun updateQuota(value: Quota?) { _quota.value = value }
    fun updateFromStation(value: String) { _fromStation.value = value }
    fun updateToStation(value: String) { _toStation.value = value }
    fun updateJourneyDate(value: String) { _journeyDate.value = value }
    fun updatePassengerName(value: String) { _passengerName.value = value }
    fun updatePassengerAge(value: String) { _passengerAge.value = value }
    fun updatePassengerGender(value: String) { _passengerGender.value = value }
    fun updatePassengerMobile(value: String) { _passengerMobile.value = value }
    fun updateBerthPreference(value: BerthPreference) { _berthPreference.value = value }
    fun updateMealPreference(value: MealPreference) { _mealPreference.value = value }
    fun updateConcession(value: Concession) { _concession.value = value }
    fun updateBedRoll(value: Boolean) { _bedRoll.value = value }

    fun addChild(child: ChildData) {
        _children.value = _children.value + child
    }

    fun removeChild(index: Int) {
        val newList = _children.value.toMutableList()
        if (index in newList.indices) {
            newList.removeAt(index)
            _children.value = newList
        }
    }

    fun updateBookingOption(option: BookingOption) { _bookingOption.value = option }
    fun updatePaymentMethod(method: PaymentMethod) { _paymentMethod.value = method }

    // ---- Validation and Workflow Control ----
    fun startWorkflow() {
        _validationError.value = null

        if (trainNumber.value.isBlank()) {
            _validationError.value = "Train Number is required."
            return
        }
        if (!trainNumber.value.matches(Regex("^\\d{4,5}$"))) {
            _validationError.value = "Train Number must be 4 or 5 digits."
            return
        }
        if (classType.value.isBlank()) {
            _validationError.value = "Class Type is required."
            return
        }
        if (quota.value == null) {
            _validationError.value = "Quota is required."
            return
        }
        if (fromStation.value.isBlank()) {
            _validationError.value = "From Station is required."
            return
        }
        if (toStation.value.isBlank()) {
            _validationError.value = "To Station is required."
            return
        }
        if (journeyDate.value.isBlank()) {
            _validationError.value = "Journey Date is required."
            return
        }
        if (passengerName.value.isBlank()) {
            _validationError.value = "Passenger Name is required."
            return
        }
        val ageInt = passengerAge.value.toIntOrNull()
        if (ageInt == null || ageInt !in 1..120) {
            _validationError.value = "Valid Age (1-120) is required."
            return
        }
        if (passengerMobile.value.isNotBlank() && !passengerMobile.value.matches(Regex("^[6-9]\\d{9}$"))) {
            _validationError.value = "Mobile must be exactly 10 digits starting with 6-9."
            return
        }

        val train = Train(
            number = trainNumber.value,
            name = trainName.value,
            classType = classType.value,
            quota = quota.value?.name ?: "GENERAL"
        )
        val fromStation = Station(fromStation.value, fromStation.value)
        val toStation = Station(toStation.value, toStation.value)

        val passenger = Passenger(
            name = passengerName.value,
            age = ageInt,
            gender = passengerGender.value,
            mobile = passengerMobile.value.takeIf { it.isNotBlank() }
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
            updatedTime = LocalDateTime.now(),
            berthPreference = berthPreference.value,
            mealPreference = mealPreference.value,
            concession = concession.value,
            bedRoll = bedRoll.value
        )

        workflowController.start(bookingRequest, passengerProfile)
    }

    fun stopWorkflow() {
        workflowController.stop()
    }

    fun isWorkflowActive(): Boolean =
        workflowState.value is WorkflowState.RUNNING || workflowState.value is WorkflowState.CONFIGURED
}
