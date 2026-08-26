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

    // ==========================================
    // 1. Core Workflow Lifecycle
    // ==========================================

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

    // ==========================================
    // 2. Train & Journey Selection (NEW STATES)
    // ==========================================

    /**
     * Workflow is actively searching or selecting a train.
     */
    SELECTING_TRAIN,

    /**
     * Workflow is selecting the travel class (e.g., SL, 3A, 2A).
     */
    SELECTING_CLASS,

    /**
     * Workflow is selecting the booking quota (e.g., GNWL, TQWL, LD).
     */
    SELECTING_QUOTA,

    /**
     * Workflow is selecting berth preferences (e.g., Lower, Upper, Window).
     */
    SELECTING_BERTH,

    // ==========================================
    // 3. Passenger Details & Preferences
    // ==========================================

    /**
     * Passenger name has been successfully entered.
     */
    PASSENGER_NAME_TYPED,

    /**
     * Passenger age has been successfully entered.
     */
    PASSENGER_AGE_TYPED,

    /**
     * Gender dropdown has been opened.
     *
     * Workflow is waiting for the gender option.
     */
    GENDER_DROPDOWN_OPENED,

    /**
     * Passenger gender has been successfully selected.
     */
    PASSENGER_GENDER_SELECTED,

    /**
     * Workflow is actively selecting meal preferences for passengers.
     */
    SELECTING_MEAL,

    /**
     * Meal dropdown has been opened.
     *
     * Workflow is waiting for the meal option.
     */
    MEAL_DROPDOWN_OPENED,

    /**
     * Passenger meal preference has been successfully selected.
     */
    PASSENGER_MEAL_SELECTED,

    // ==========================================
    // 4. Payment & Security Boundaries
    // ==========================================

    /**
     * Workflow has reached the payment screen and is waiting for payment completion.
     * 
     * This usually transitions to USER_BOUNDARY if manual intervention (like CAPTCHA/OTP) is required.
     */
    WAITING_FOR_PAYMENT,

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

    // ==========================================
    // 5. Terminal States
    // ==========================================

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
