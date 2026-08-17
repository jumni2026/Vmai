package com.vmax.workflow

/**
 * VMAX Enterprise
 *
 * Top-level workflow state contract used by the Android UI.
 *
 * IMPORTANT:
 * This is a sealed class, not an enum.
 *
 * Reason:
 * ERROR state carries a reason/message.
 */
sealed class WorkflowState {

    /**
     * Initial state.
     */
    data object IDLE : WorkflowState()

    /**
     * Configuration has been accepted.
     * Engine is ready/waiting for execution.
     */
    data object CONFIGURED : WorkflowState()

    /**
     * Workflow engine is actively running.
     */
    data object RUNNING : WorkflowState()

    /**
     * Workflow reached a user/security boundary.
     */
    data object USER_BOUNDARY : WorkflowState()

    /**
     * Workflow was explicitly stopped.
     */
    data object STOPPED : WorkflowState()

    /**
     * Workflow failed.
     *
     * @param reason human-readable error reason
     */
    data class ERROR(
        val reason: String
    ) : WorkflowState()
}
