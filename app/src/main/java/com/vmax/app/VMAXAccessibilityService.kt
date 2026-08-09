package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * VMAX Enterprise v2.6
 *
 * Stage 2 — Runtime Entry
 * File 34 — VMAXAccessibilityService
 *
 * Accessibility service for VMAX Enterprise automation.
 * Stage 2: Receives Android Accessibility Events.
 * No business logic.
 */
class VMAXAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Stage 2 — Evidence Capture Phase
        if (event == null) return

        // Log the basic event for evidence
        val eventType = when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "WINDOW_CHANGED"
            AccessibilityEvent.TYPE_VIEW_CLICKED -> "VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> "TEXT_CHANGED"
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> "SCROLLED"
            else -> "OTHER"
        }

        android.util.Log.d("VMAX_SERVICE", "Accessibility Event Received: $eventType")
    }

    override fun onInterrupt() {
        // Stage 1 — Skeleton
    }
}
