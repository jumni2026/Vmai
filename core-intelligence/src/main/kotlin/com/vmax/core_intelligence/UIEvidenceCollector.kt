package com.vmax.workflow

/**
 * Platform-agnostic UI Element representation
 * Used for communication between WorkflowController and Accessibility Service
 * 
 * This matches the structure of UIEvidenceCollector.ScreenEvidence.UIElement
 * but without Android dependencies
 */
data class UIElement(
    val id: String = "",
    val type: String = "",
    val text: String = "",
    val contentDescription: String? = null,
    val bounds: BoundingBox? = null,
    val isClickable: Boolean = false,
    val isEditable: Boolean = false,
    val hint: String? = null
)

data class BoundingBox(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)
