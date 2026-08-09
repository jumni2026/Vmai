package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

/**
 * VMAX Enterprise v2.6
 *
 * Stage 2 — Runtime Diagnostic
 *
 * Purpose:
 * - Verify that the AccessibilityService instance is created.
 * - Verify that onServiceConnected() executes.
 * - Generate one controlled synthetic event for diagnostic purposes.
 *
 * NOTE:
 * The synthetic event does NOT prove Android is delivering
 * real AccessibilityEvents. It only verifies our callback path.
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

        // Diagnostic-only synthetic event.
        // This must NOT be treated as proof of real Android event delivery.
        val testEvent = AccessibilityEvent.obtain(
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        )

        testEvent.packageName = packageName
        testEvent.className = VMAXAccessibilityService::class.java.name

        Log.d(
            TAG,
            "Synthetic Diagnostic Event Generated"
        )

        onAccessibilityEvent(testEvent)

        testEvent.recycle()
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
