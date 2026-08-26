package com.vmax.workflow

import com.vmax.model.BookingRequest
import com.vmax.model.PassengerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * VMAX v2.6.1
 *
 * Central workflow lifecycle controller.
 *
 * Responsibilities:
 * - Maintain workflow state.
 * - Maintain current session.
 * - Validate passenger workflow data.
 * - Provide safe state transitions.
 * - Provide safe singleton access.
 *
 * NOTE:
 * Dependencies are intentionally nullable for compatibility with
 * the current modular architecture.
 */
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
            return instance ?: synchronized(this) {
                instance ?: WorkflowController().also { controller ->
                    instance = controller
                }
            }
        }

        @JvmStatic
        fun getInstanceOrNull(): WorkflowController? {
            return instance
        }

        @JvmStatic
        fun initialize(controller: WorkflowController) {
            synchronized(this) {
                if (instance == null) {
                    instance = controller
                }
            }
        }

        @JvmStatic
        fun replaceInstance(controller: WorkflowController) {
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

    // ---------------------------------------------------------------------
    // Passenger data (UPGRADED with new fields)
    // ---------------------------------------------------------------------

    data class PassengerDetails(
        val from: String,
        val to: String,
        val date: String,
        val trainNumber: String,      
        val classType: String,        
        val quota: String,            
        val berthPreference: String,  
        val name: String,
        val age: String,
        val gender: String,
        val mealPreference: String = "" 
    )

    // ---------------------------------------------------------------------
    // Workflow state
    // ---------------------------------------------------------------------

    private val _state = MutableStateFlow(WorkflowState.IDLE)

    val state: StateFlow<WorkflowState> = _state.asStateFlow()

    private var workflowState: WorkflowState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    // ---------------------------------------------------------------------
    // Session
    // ---------------------------------------------------------------------

    @Volatile
    private var currentSessionId: String = ""

    @Volatile
    private var passengerDetails: PassengerDetails? = null

    private val lifecycleLock = Any()

    // ---------------------------------------------------------------------
    // Workflow start
    // ---------------------------------------------------------------------

    fun start(
        bookingRequest: BookingRequest,
        passengerProfile: PassengerProfile
    ): Boolean {

        synchronized(lifecycleLock) {

            if (workflowState != WorkflowState.IDLE) {
                return false
            }

            if (passengerProfile.passengers.isEmpty()) {
                return false
            }

            val passenger = bookingRequest.passengers.firstOrNull() ?: return false

            val details = try {
                PassengerDetails(
                    from = bookingRequest.fromStation.code.trim(),
                    to = bookingRequest.toStation.code.trim(),
                    date = bookingRequest.date.trim(),
                    trainNumber = bookingRequest.train.number.trim(),
                    classType = bookingRequest.train.classType.trim(),
                    
                    // FIXED: Removed 'dynamic' cast. Using safe, standard Kotlin property access.
                    quota = bookingRequest.quota?.trim() ?: "GN", 
                    berthPreference = bookingRequest.berth?.trim() ?: "No Preference",
                    
                    name = passenger.name.trim(),
                    age = passenger.age.toString().trim(),
                    gender = passenger.gender.trim(),
                    
                    // FIXED: Removed 'dynamic' cast. Using safe property access.
                    mealPreference = passenger.mealPreference?.trim() ?: ""
                )
            } catch (_: Exception) {
                return false
            }

            return startWorkflowLocked(
                details = details,
                sessionId = UUID.randomUUID().toString()
            )
        }
    }

    fun stop() {
        stopWorkflow()
    }

    fun startWorkflow(
        details: PassengerDetails,
        sessionId: String
    ): Boolean {
        synchronized(lifecycleLock) {
            return startWorkflowLocked(
                details = details,
                sessionId = sessionId
            )
        }
    }

    // ---------------------------------------------------------------------
    // Internal workflow start
    // ---------------------------------------------------------------------

    private fun startWorkflowLocked(
        details: PassengerDetails,
        sessionId: String
    ): Boolean {

        if (workflowState != WorkflowState.IDLE) {
            return false
        }

        val normalizedSessionId = sessionId.trim()
        if (normalizedSessionId.isBlank()) {
            return false
        }

        val normalizedDetails = normalizePassengerDetails(details)

        if (!isValidPassengerDetails(normalizedDetails)) {
            return false
        }

        passengerDetails = normalizedDetails
        currentSessionId = normalizedSessionId

        workflowState = WorkflowState.CONFIGURED
        workflowState = WorkflowState.RUNNING

        return true
    }

    // ---------------------------------------------------------------------
    // Stop & Reset
    // ---------------------------------------------------------------------

    fun stopWorkflow() {
        synchronized(lifecycleLock) {
            if (workflowState == WorkflowState.IDLE || workflowState == WorkflowState.STOPPED) {
                return
            }
            workflowState = WorkflowState.STOPPED
            _state.value = WorkflowState.STOPPED
        }
    }

    fun reset(): Boolean {
        synchronized(lifecycleLock) {
            if (isActive()) {
                return false
            }

            currentSessionId = ""
            passengerDetails = null
            workflowState = WorkflowState.IDLE
            _state.value = WorkflowState.IDLE

            return true
        }
    }

    // ---------------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------------

    fun getCurrentState(): WorkflowState {
        return workflowState
    }

    fun getSessionId(): String {
        return currentSessionId
    }

    fun getPassengerDetails(): PassengerDetails? {
        return passengerDetails
    }

    // ---------------------------------------------------------------------
    // Active state (UPGRADED with new states)
    // ---------------------------------------------------------------------

    fun isActive(): Boolean {
        return when (workflowState) {
            WorkflowState.CONFIGURED,
            WorkflowState.RUNNING,
            WorkflowState.ARMED,
            WorkflowState.SELECTING_TRAIN,
            WorkflowState.SELECTING_CLASS,
            WorkflowState.SELECTING_QUOTA,
            WorkflowState.SELECTING_BERTH,
            WorkflowState.PASSENGER_NAME_TYPED,
            WorkflowState.PASSENGER_AGE_TYPED,
            WorkflowState.GENDER_DROPDOWN_OPENED,
            WorkflowState.PASSENGER_GENDER_SELECTED,
            WorkflowState.SELECTING_MEAL,
            WorkflowState.MEAL_DROPDOWN_OPENED,
            WorkflowState.PASSENGER_MEAL_SELECTED,
            WorkflowState.WAITING_FOR_PAYMENT -> true

            WorkflowState.IDLE,
            WorkflowState.USER_BOUNDARY,
            WorkflowState.STOPPED,
            WorkflowState.ERROR -> false
        }
    }

    // ---------------------------------------------------------------------
    // State Transition Logic (NEW & UPGRADED)
    // ---------------------------------------------------------------------

    /**
     * Safely transitions to a new state after validating the transition path.
     * @return true if transition was successful, false if invalid.
     */
    fun transitionTo(newState: WorkflowState): Boolean {
        synchronized(lifecycleLock) {
            if (!canTransitionTo(workflowState, newState)) {
                return false
            }
            workflowState = newState
            _state.value = newState
            return true
        }
    }

    /**
     * Validates if a state transition is allowed.
     * Prevents jumping out of terminal states (STOPPED, ERROR).
     */
    private fun canTransitionTo(current: WorkflowState, next: WorkflowState): Boolean {
        if (next == WorkflowState.IDLE) return true

        if (current == WorkflowState.STOPPED || current == WorkflowState.ERROR) {
            return false
        }

        return when (next) {
            WorkflowState.IDLE, WorkflowState.STOPPED, WorkflowState.ERROR -> true
            WorkflowState.USER_BOUNDARY, WorkflowState.WAITING_FOR_PAYMENT -> true
            else -> current == WorkflowState.RUNNING || current == WorkflowState.ARMED || current == WorkflowState.CONFIGURED
        }
    }

    @Deprecated("Use transitionTo() for safer state updates", ReplaceWith("transitionTo(newState)"))
    fun updateState(newState: WorkflowState) {
        transitionTo(newState)
    }

    // ---------------------------------------------------------------------
    // Normalization (UPGRADED)
    // ---------------------------------------------------------------------

    private fun normalizePassengerDetails(details: PassengerDetails): PassengerDetails {
        return details.copy(
            from = details.from.trim(),
            to = details.to.trim(),
            date = details.date.trim(),
            trainNumber = details.trainNumber.trim(),
            classType = details.classType.trim(),
            quota = details.quota.trim(),
            berthPreference = details.berthPreference.trim(),
            name = details.name.trim(),
            age = details.age.trim(),
            gender = details.gender.trim(),
            mealPreference = details.mealPreference.trim()
        )
    }

    // ---------------------------------------------------------------------
    // Validation (UPGRADED)
    // ---------------------------------------------------------------------

    private fun isValidPassengerDetails(details: PassengerDetails): Boolean {
        return details.from.isNotBlank() &&
                details.to.isNotBlank() &&
                details.date.isNotBlank() &&
                details.trainNumber.isNotBlank() &&
                details.classType.isNotBlank() &&
                details.quota.isNotBlank() &&
                details.berthPreference.isNotBlank() &&
                details.name.isNotBlank() &&
                details.age.isNotBlank() &&
                details.gender.isNotBlank()
    }
}

/**
 * Generic workflow actions.
 */
sealed class WorkflowAction {

    data class Click(
        val targetId: String? = null,
        val coordinates: Pair<Int, Int>? = null
    ) : WorkflowAction()

    data class SetText(
        val targetId: String,
        val text: String
    ) : WorkflowAction()
}
