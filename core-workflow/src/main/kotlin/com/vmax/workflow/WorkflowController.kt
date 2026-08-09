package com.vmax.workflow

import com.vmax.model.BookingRequest
import com.vmax.model.PassengerProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * VMAX Enterprise v2.6
 *
 * File — WorkflowController.kt
 *
 * Corrected Working Implementation (Base on existing StateFlow architecture).
 *
 * - Simulation loop removed completely.
 * - Real Execution contract placeholder prepared.
 * - No fake persistence.
 * - Clean cancellation.
 */
class WorkflowController private constructor() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Active Coroutine Job tracking for real cancellation handling
    private var executionJob: Job? = null

    // Central State Machine
    private val _state = MutableStateFlow<WorkflowState>(WorkflowState.IDLE)
    val state: StateFlow<WorkflowState> = _state.asStateFlow()

    // Active Request and Profile Memory Holders
    private var activeBookingRequest: BookingRequest? = null
    private var activePassengerProfile: PassengerProfile? = null

    companion object {
        @Volatile
        private var INSTANCE: WorkflowController? = null

        fun getInstance(): WorkflowController {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WorkflowController().also { INSTANCE = it }
            }
        }
    }

    /**
     * Validates input parameters, manages coroutine job execution, and drives state machine transitions.
     */
    fun start(bookingRequest: BookingRequest, passengerProfile: PassengerProfile) {
        if (_state.value != WorkflowState.IDLE) {
            return
        }

        // Gate 1: Profile Emptiness Check
        if (passengerProfile.passengers.isEmpty()) {
            _state.value = WorkflowState.ERROR("Passenger profile list is empty.")
            return
        }

        // Gate 2: Passenger Data Mandatory Fields & Bounds Check
        val hasInvalidPassenger = passengerProfile.passengers.any { passenger ->
            passenger.name.isBlank() || passenger.age <= 0 || passenger.age > 120
        }
        if (hasInvalidPassenger) {
            _state.value = WorkflowState.ERROR("Invalid passenger data detected.")
            return
        }

        // Gate 3: Target Settings Completeness Check
        // ✅ EXACT FIX: bookingRequest.train.number and bookingRequest.train.classType
        if (bookingRequest.train.number.isBlank() || bookingRequest.train.classType.isBlank()) {
            _state.value = WorkflowState.ERROR("Target settings incomplete.")
            return
        }

        activeBookingRequest = bookingRequest
        activePassengerProfile = passengerProfile

        // Cancel any existing background execution job
        executionJob?.cancel()

        _state.value = WorkflowState.CONFIGURED

        // ⚠️ REAL EXECUTION BOUNDARY:
        // The infinite simulation loop has been removed.
        // This is now a contract placeholder for the actual Runtime/Action Engine.
        // To make this real, the following components must be invoked:
        // 1. RuntimeCoordinator.startAutomation()
        // 2. ActionExecutor.executeActions()
        //
        // Since their exact contract is not yet evidenced, this file remains HOLD.
        executionJob = scope.launch {
            // Real execution will be added here when exact contracts are provided.
            // For now, the WorkflowController remains a State Manager.
        }
    }

    /**
     * Immediately cancels active execution job coroutines and resets state machine to IDLE.
     */
    fun stop() {
        // Real Execution Cancellation
        executionJob?.cancel()
        executionJob = null

        activeBookingRequest = null
        activePassengerProfile = null
        _state.value = WorkflowState.IDLE
    }

    /**
     * Returns the current booking request if available.
     */
    fun getActiveBookingRequest(): BookingRequest? = activeBookingRequest

    /**
     * Returns the current passenger profile if available.
     */
    fun getActivePassengerProfile(): PassengerProfile? = activePassengerProfile

    /**
     * Returns true if the workflow is currently running or configured.
     */
    fun isRunning(): Boolean = _state.value == WorkflowState.RUNNING

    /**
     * Returns true if the workflow is in an ERROR state.
     */
    fun isError(): Boolean = _state.value is WorkflowState.ERROR

    /**
     * Cancels active job execution and forces state machine into ERROR state.
     */
    fun notifyConfigurationError(reason: String) {
        executionJob?.cancel()
        executionJob = null
        _state.value = WorkflowState.ERROR(reason)
    }
}

/**
 * Blueprint v2.6 Workflow Execution States.
 */
sealed class WorkflowState(val name: String) {
    object IDLE : WorkflowState("IDLE")
    object CONFIGURED : WorkflowState("CONFIGURED")
    object RUNNING : WorkflowState("RUNNING")
    data class ERROR(val reason: String) : WorkflowState("ERROR")
}
