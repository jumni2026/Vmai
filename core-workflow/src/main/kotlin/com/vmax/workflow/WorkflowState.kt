package com.vmax.workflow

/**
 * VMAX Enterprise v2.6.1
 *
 * File: WorkflowState.kt
 *
 * Canonical workflow state contract.
 *
 * This file contains the single shared state model used by:
 * - WorkflowController
 * - MainViewModel
 * - MainActivity
 *
 * Platform-independent:
 * - No Android imports
 * - No Compose imports
 * - No AccessibilityService imports
 *
 * The state machine describes the lifecycle of the VMAX workflow
 * without performing any action itself.
 */
enum class WorkflowState {

    /**
     * Workflow is not configured or not running.
     */
    IDLE,

    /**
     * Workflow configuration has been accepted
     * and is waiting for the execution engine.
     */
    CONFIGURED,

    /**
     * Workflow execution is currently active.
     */
    RUNNING,

    /**
     * Workflow has reached a security/user-controlled boundary.
     *
     * Automation must not continue automatically from this state.
     */
    USER_BOUNDARY,

    /**
     * Workflow has been explicitly stopped.
     */
    STOPPED,

    /**
     * Workflow failed because of an execution or configuration error.
     *
     * The actual error message is carried by WorkflowController's
     * error handling mechanism rather than being stored in this enum.
     */
    ERROR,

    /**
     * Gender dropdown is currently open and the workflow
     * is waiting for the gender option to be selected.
     */
    GENDER_DROPDOWN_OPENED,

    /**
     * Meal dropdown is currently open and the workflow
     * is waiting for the meal option to be selected.
     */
    MEAL_DROPDOWN_OPENED,

    /**
     * Passenger name has been entered successfully.
     */
    PASSENGER_NAME_TYPED,

    /**
     * Passenger age has been entered successfully.
     */
    PASSENGER_AGE_TYPED,

    /**
     * Passenger gender has been selected successfully.
     */
    PASSENGER_GENDER_SELECTED,

    /**
     * Passenger meal preference has been selected successfully.
     */
    PASSENGER_MEAL_SELECTED
}
