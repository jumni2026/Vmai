package com.vmax.workflow

import com.vmax.model.BookingRequest
import com.vmax.model.PassengerProfile
import com.vmax.runtime.RuntimeCoordinator
import com.vmax.runtime.RuntimeError
import com.vmax.common.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

/**
 * VMAX Enterprise v2.6.1
 *
 * File — WorkflowController.kt
 *
 * FINAL PLATFORM-INDEPENDENT VERSION
 * - RuntimeCoordinator injected (No Android dependencies)
 * - ExecutionTracker injected for recording
 * - Proper CONFIGURED → RUNNING transition
 * - Session ID generation
 * - Thread-safe state management
 * - Clean lifecycle handling
 */
class WorkflowController private constructor(
    private val runtimeCoordinator: RuntimeCoordinator,
    private val executionTracker: ExecutionTracker
) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var executionJob: Job? = null
    private var currentSessionId: String? = null

    private val _state = MutableStateFlow<WorkflowState>(WorkflowState.IDLE)
    val state: StateFlow<WorkflowState> = _state.asStateFlow()

    private var activeBookingRequest: BookingRequest? = null
    private var activePassengerProfile: PassengerProfile? = null

    private val mutex = Mutex() // Thread safety for mutable properties

    companion object {
        @Volatile
        private var INSTANCE: WorkflowController? = null

        fun getInstance(
            runtimeCoordinator: RuntimeCoordinator,
            executionTracker: ExecutionTracker
        ): WorkflowController {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WorkflowController(runtimeCoordinator, executionTracker).also { INSTANCE = it }
            }
        }
    }

    /**
     * Starts the workflow with validation and triggers the runtime engine.
     */
    suspend fun start(bookingRequest: BookingRequest, passengerProfile: PassengerProfile) {
        mutex.withLock {
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
            if (bookingRequest.train.number.isBlank() || bookingRequest.train.classType.isBlank()) {
                _state.value = WorkflowState.ERROR("Target settings incomplete.")
                return
            }

            activeBookingRequest = bookingRequest
            activePassengerProfile = passengerProfile

            executionJob?.cancel()
            _state.value = WorkflowState.CONFIGURED

            // Record State Transition
            executionTracker.recordStateTransition(
                sessionId = currentSessionId ?: UUID.randomUUID().toString(),
                fromState = "IDLE",
                toState = "CONFIGURED"
            )

            // ✅ ACTUAL EXECUTION TRIGGER
            executionJob = scope.launch {
                val result = runtimeCoordinator.start()
                when (result) {
                    is Result.Success -> {
                        _state.value = WorkflowState.RUNNING
                        executionTracker.recordStateTransition(
                            sessionId = currentSessionId!!,
                            fromState = "CONFIGURED",
                            toState = "RUNNING"
                        )
                    }
                    is Result.Error -> {
                        _state.value = WorkflowState.ERROR(result.error.message)
                        executionTracker.recordSessionError(
                            sessionId = currentSessionId!!,
                            errorCode = result.error.code,
                            errorMessage = result.error.message
                        )
                    }
                }
            }
        }
    }

    /**
     * Stops the workflow and cancels runtime execution.
     */
    suspend fun stop() {
        mutex.withLock {
            executionJob?.cancel()
            executionJob = null

            // Stop the runtime coordinator as well
            runtimeCoordinator.stop()
            
            currentSessionId = null
            activeBookingRequest = null
            activePassengerProfile = null
            _state.value = WorkflowState.IDLE
        }
    }

    fun getActiveBookingRequest(): BookingRequest? = activeBookingRequest

    fun getActivePassengerProfile(): PassengerProfile? = activePassengerProfile

    fun isRunning(): Boolean = _state.value == WorkflowState.RUNNING

    fun isError(): Boolean = _state.value is WorkflowState.ERROR

    fun notifyConfigurationError(reason: String) {
        mutex.withLock {
            executionJob?.cancel()
            executionJob = null
            _state.value = WorkflowState.ERROR(reason)
        }
    }

    fun generateNewSessionId(): String {
        return UUID.randomUUID().toString()
    }
}

/**
 * Blueprint v2.6.1 Workflow Execution States.
 */
sealed class WorkflowState(val name: String) {
    object IDLE : WorkflowState("IDLE")
    object CONFIGURED : WorkflowState("CONFIGURED")
    object RUNNING : WorkflowState("RUNNING")
    data class ERROR(val reason: String) : WorkflowState("ERROR")
}
