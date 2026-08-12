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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * VMAX Enterprise v2.6.1
 *
 * File — WorkflowController.kt
 *
 * Platform-independent workflow state controller.
 *
 * Responsibilities:
 * - Validate workflow configuration.
 * - Store the active booking request/profile.
 * - Manage workflow lifecycle state.
 * - Provide thread-safe state transitions.
 * - Support cancellation/reset.
 *
 * Architecture rule:
 * - No Android dependency.
 * - No runtime-module dependency.
 * - No ActionExecutor dependency.
 * - No ExecutionTracker dependency.
 * - No execution implementation/business automation logic.
 *
 * Actual runtime execution is owned by the runtime layer.
 */
class WorkflowController private constructor() {

    private val scope =
        CoroutineScope(
            Dispatchers.Default + SupervisorJob()
        )

    private var executionJob: Job? = null

    private val _state =
        MutableStateFlow<WorkflowState>(
            WorkflowState.IDLE
        )

    val state: StateFlow<WorkflowState> =
        _state.asStateFlow()

    private var activeBookingRequest: BookingRequest? = null
    private var activePassengerProfile: PassengerProfile? = null

    private val mutex = Mutex()

    companion object {

        @Volatile
        private var INSTANCE: WorkflowController? = null

        fun getInstance(): WorkflowController {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: WorkflowController().also {
                        INSTANCE = it
                    }
            }
        }
    }

    /**
     * Validates and configures a new workflow.
     *
     * This method intentionally does not start platform execution.
     * Runtime execution is triggered by the runtime layer.
     */
    suspend fun start(
        bookingRequest: BookingRequest,
        passengerProfile: PassengerProfile
    ) {
        mutex.withLock {

            if (_state.value != WorkflowState.IDLE) {
                return
            }

            // --------------------------------------------------------
            // VALIDATION GATE 1
            // --------------------------------------------------------

            if (passengerProfile.passengers.isEmpty()) {
                _state.value =
                    WorkflowState.ERROR(
                        "Passenger profile list is empty."
                    )
                return
            }

            // --------------------------------------------------------
            // VALIDATION GATE 2
            // --------------------------------------------------------

            val hasInvalidPassenger =
                passengerProfile.passengers.any { passenger ->

                    passenger.name.isBlank() ||
                        passenger.age <= 0 ||
                        passenger.age > 120
                }

            if (hasInvalidPassenger) {
                _state.value =
                    WorkflowState.ERROR(
                        "Invalid passenger data detected."
                    )
                return
            }

            // --------------------------------------------------------
            // VALIDATION GATE 3
            // --------------------------------------------------------

            if (
                bookingRequest.train.number.isBlank() ||
                bookingRequest.train.classType.isBlank()
            ) {
                _state.value =
                    WorkflowState.ERROR(
                        "Target settings incomplete."
                    )
                return
            }

            // --------------------------------------------------------
            // STORE ACTIVE WORKFLOW DATA
            // --------------------------------------------------------

            activeBookingRequest = bookingRequest
            activePassengerProfile = passengerProfile

            // Cancel any previous local job.
            executionJob?.cancel()
            executionJob = null

            // --------------------------------------------------------
            // CONFIGURED
            // --------------------------------------------------------

            _state.value =
                WorkflowState.CONFIGURED
        }
    }

    /**
     * Marks the workflow as running.
     *
     * Runtime execution itself is intentionally outside this class.
     */
    suspend fun markRunning(): Boolean {
        return mutex.withLock {

            if (_state.value != WorkflowState.CONFIGURED) {
                return@withLock false
            }

            _state.value =
                WorkflowState.RUNNING

            true
        }
    }

    /**
     * Marks the workflow as completed.
     */
    suspend fun markCompleted(): Boolean {
        return mutex.withLock {

            if (_state.value != WorkflowState.RUNNING) {
                return@withLock false
            }

            _state.value =
                WorkflowState.COMPLETED

            true
        }
    }

    /**
     * Stops the current workflow and clears active data.
     */
    suspend fun stop() {
        mutex.withLock {

            executionJob?.cancel()
            executionJob = null

            activeBookingRequest = null
            activePassengerProfile = null

            _state.value =
                WorkflowState.IDLE
        }
    }

    /**
     * Moves the workflow into an error state.
     */
    suspend fun notifyConfigurationError(
        reason: String
    ) {
        mutex.withLock {

            executionJob?.cancel()
            executionJob = null

            _state.value =
                WorkflowState.ERROR(reason)
        }
    }

    /**
     * Returns the active booking request.
     */
    fun getActiveBookingRequest(): BookingRequest? =
        activeBookingRequest

    /**
     * Returns the active passenger profile.
     */
    fun getActivePassengerProfile(): PassengerProfile? =
        activePassengerProfile

    /**
     * Returns true when workflow execution is running.
     */
    fun isRunning(): Boolean =
        _state.value == WorkflowState.RUNNING

    /**
     * Returns true when workflow is configured.
     */
    fun isConfigured(): Boolean =
        _state.value == WorkflowState.CONFIGURED

    /**
     * Returns true when workflow completed successfully.
     */
    fun isCompleted(): Boolean =
        _state.value == WorkflowState.COMPLETED

    /**
     * Returns true when workflow is in an error state.
     */
    fun isError(): Boolean =
        _state.value is WorkflowState.ERROR

    /**
     * Returns the current workflow state.
     */
    fun getCurrentState(): WorkflowState =
        _state.value
}

/**
 * VMAX Enterprise v2.6.1
 *
 * Workflow lifecycle states.
 */
sealed class WorkflowState(
    val name: String
) {

    object IDLE :
        WorkflowState("IDLE")

    object CONFIGURED :
        WorkflowState("CONFIGURED")

    object RUNNING :
        WorkflowState("RUNNING")

    object COMPLETED :
        WorkflowState("COMPLETED")

    data class ERROR(
        val reason: String
    ) : WorkflowState("ERROR")
}
