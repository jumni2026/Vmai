package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

/**
 * VMAX Enterprise v2.6
 *
 * Stage 2 — Runtime Evidence
 *
 * Purpose:
 * - Prove that Android starts and binds the AccessibilityService.
 * - Prove that AccessibilityEvents reach the service.
 *
 * No business automation is performed here.
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_SERVICE"
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        Log.d(
            TAG,
            "Accessibility Service Started"
        )
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val eventType = when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "WINDOW_CHANGED"
            AccessibilityEvent.TYPE_VIEW_CLICKED -> "VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> "TEXT_CHANGED"
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> "SCROLLED"
            else -> "OTHER"
        }

        Log.d(
            TAG,
            "Accessibility Event Received: $eventType"
        )
    }

    override fun onInterrupt() {
        Log.d(
            TAG,
            "Accessibility Service Interrupted"
        )
    }
}
