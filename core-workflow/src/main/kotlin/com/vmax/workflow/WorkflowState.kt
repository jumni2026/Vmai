package com.vmax.workflow

/**
 * VMAX Enterprise v2.6.1
 *
 * File: WorkflowState.kt
 *
 * Canonical workflow state contract.
 *
 * IMPORTANT:
 * This is the SINGLE source of truth for workflow states.
 *
 * Used by:
 * - WorkflowController
 * - MainViewModel
 * - MainActivity
 * - Workflow execution/orchestration layer
 *
 * Platform independent:
 * - No Android imports
 * - No Compose imports
 * - No AccessibilityService imports
 *
 * This enum only describes workflow state.
 * It does not execute any action.
 */
enum class WorkflowState {

    /**
     * Initial state.
     *
     * No workflow is configured or running.
     */
    IDLE,

    /**
     * Workflow configuration has been accepted.
     *
     * The workflow is ready to be handed to the execution layer.
     */
    CONFIGURED,

    /**
     * Workflow is actively executing.
     */
    RUNNING,

    /**
     * Workflow is armed and waiting for a valid screen/action.
     *
     * This state is useful for the accessibility/screen-driven
     * execution pipeline.
     */
    ARMED,

    /**
     * Workflow has reached a security-sensitive or
     * user-controlled boundary.
     *
     * Automation MUST NOT continue automatically.
     *
     * Examples:
     * - CAPTCHA
     * - OTP
     * - Payment authentication
     * - Other sensitive screens
     */
    USER_BOUNDARY,

    /**
     * Gender dropdown has been opened.
     *
     * Workflow is waiting for the gender option.
     */
    GENDER_DROPDOWN_OPENED,

    /**
     * Meal dropdown has been opened.
     *
     * Workflow is waiting for the meal option.
     */
    MEAL_DROPDOWN_OPENED,

    /**
     * Passenger name has been successfully entered.
     */
    PASSENGER_NAME_TYPED,

    /**
     * Passenger age has been successfully entered.
     */
    PASSENGER_AGE_TYPED,

    /**
     * Passenger gender has been successfully selected.
     */
    PASSENGER_GENDER_SELECTED,

    /**
     * Passenger meal preference has been successfully selected.
     */
    PASSENGER_MEAL_SELECTED,

    /**
     * Workflow has been explicitly stopped.
     */
    STOPPED,

    /**
     * Workflow terminated because of an error.
     *
     * Detailed error information should be handled by
     * the controller/error-reporting layer, not this enum.
     */
    ERROR
}
