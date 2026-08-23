package com.vmax.workflow

import com.vmax.model.BookingRequest
import com.vmax.model.PassengerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class WorkflowController(
    @Suppress("UNUSED_PARAMETER")
    private val analyzer: Any? = null,
    @Suppress("UNUSED_PARAMETER")
    private val classifier: Any? = null,
    @Suppress("UNUSED_PARAMETER")
    private val metrics: Any? = null,
    @Suppress("UNUSED_PARAMETER")
    private val recorder: Any? = null
) {

    companion object {
        @Volatile
        private var instance: WorkflowController? = null

        @JvmStatic
        fun getInstance(): WorkflowController {
            return instance ?: throw IllegalStateException("WorkflowController has not been initialized.")
        }

        @JvmStatic
        fun getInstanceOrNull(): WorkflowController? {
            return instance
        }

        @JvmStatic
        fun initialize(controller: WorkflowController) {
            requireNotNull(controller)
            synchronized(this) {
                if (instance == null) instance = controller
            }
        }

        @JvmStatic
        fun replaceInstance(controller: WorkflowController) {
            requireNotNull(controller)
            synchronized(this) {
                instance = controller
            }
        }

        @JvmStatic
        fun clearInstance() {
            synchronized(this) {
                instance = null
            }
        }
    }

    data class PassengerDetails(
        val from: String,
        val to: String,
        val date: String,
        val train: String,
        val trainClass: String,
        val name: String,
        val age: String,
        val gender: String,
        val meal: String = ""
    )

    private val _state = MutableStateFlow(WorkflowState.IDLE)
    val state: StateFlow<WorkflowState> = _state.asStateFlow()

    private var workflowState: WorkflowState
        get() = _state.value
        set(value) { _state.value = value }

    @Volatile
    private var currentSessionId: String = ""

    @Volatile
    private var passengerDetails: PassengerDetails? = null

    private val lifecycleLock = Any()

    fun start(bookingRequest: BookingRequest, passengerProfile: PassengerProfile): Boolean {
        synchronized(lifecycleLock) {
            if (workflowState != WorkflowState.IDLE) return false
            if (passengerProfile.passengers.isEmpty()) return false

            val passenger = bookingRequest.passengers.firstOrNull() ?: return false

            val details = try {
                PassengerDetails(
                    from = bookingRequest.fromStation.code.trim(),
                    to = bookingRequest.toStation.code.trim(),
                    date = bookingRequest.date.trim(),
                    train = bookingRequest.train.number.trim(),
                    trainClass = bookingRequest.train.classType.trim(),
                    name = passenger.name.trim(),
                    age = passenger.age.toString().trim(),
                    gender = passenger.gender.trim(),
                    meal = ""
                )
            } catch (_: Exception) {
                return false
            }

            return startWorkflowLocked(details, UUID.randomUUID().toString())
        }
    }

    fun stop() = stopWorkflow()

    fun startWorkflow(details: PassengerDetails, sessionId: String): Boolean {
        synchronized(lifecycleLock) {
            return startWorkflowLocked(details, sessionId)
        }
    }

    private fun startWorkflowLocked(details: PassengerDetails, sessionId: String): Boolean {
        if (workflowState != WorkflowState.IDLE) return false

        val normalizedSessionId = sessionId.trim()
        if (normalizedSessionId.isBlank()) return false

        val normalizedDetails = normalizePassengerDetails(details)
        if (!isValidPassengerDetails(normalizedDetails)) return false

        passengerDetails = normalizedDetails
        currentSessionId = normalizedSessionId

        workflowState = WorkflowState.CONFIGURED

        workflowState = WorkflowState.RUNNING
        return true
    }

    fun stopWorkflow() {
        synchronized(lifecycleLock) {
            if (workflowState == WorkflowState.IDLE || workflowState == WorkflowState.STOPPED) return
            workflowState = WorkflowState.STOPPED
        }
    }

    fun reset(): Boolean {
        synchronized(lifecycleLock) {
            if (isActive()) return false
            currentSessionId = ""
            passengerDetails = null
            workflowState = WorkflowState.IDLE
            return true
        }
    }

    fun getCurrentState(): WorkflowState = workflowState
    fun getSessionId(): String = currentSessionId

    fun isActive(): Boolean {
        return when (workflowState) {
            WorkflowState.CONFIGURED, WorkflowState.RUNNING, WorkflowState.ARMED,
            WorkflowState.GENDER_DROPDOWN_OPENED, WorkflowState.MEAL_DROPDOWN_OPENED,
            WorkflowState.PASSENGER_NAME_TYPED, WorkflowState.PASSENGER_AGE_TYPED,
            WorkflowState.PASSENGER_GENDER_SELECTED, WorkflowState.PASSENGER_MEAL_SELECTED -> true
            WorkflowState.IDLE, WorkflowState.USER_BOUNDARY, WorkflowState.STOPPED, WorkflowState.ERROR -> false
        }
    }

    fun updateState(newState: WorkflowState) {
        synchronized(lifecycleLock) {
            workflowState = newState
        }
    }

    private fun normalizePassengerDetails(details: PassengerDetails): PassengerDetails {
        return details.copy(
            from = details.from.trim(), to = details.to.trim(), date = details.date.trim(),
            train = details.train.trim(), trainClass = details.trainClass.trim(),
            name = details.name.trim(), age = details.age.trim(), gender = details.gender.trim(),
            meal = details.meal.trim()
        )
    }

    private fun isValidPassengerDetails(details: PassengerDetails): Boolean {
        return details.from.isNotBlank() && details.to.isNotBlank() && details.date.isNotBlank() &&
            details.train.isNotBlank() && details.trainClass.isNotBlank() &&
            details.name.isNotBlank() && details.age.isNotBlank() && details.gender.isNotBlank()
    }
}

sealed class WorkflowAction {
    data class Click(val targetId: String? = null, val coordinates: Pair<Int, Int>? = null) : WorkflowAction()
    data class SetText(val targetId: String, val text: String) : WorkflowAction()
}
