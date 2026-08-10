package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * VMAX Enterprise v2.6.1
 *
 * File — VMAXAccessibilityService.kt
 *
 * Stage 3 — Live Evidence Collector (Diagnostic Mode)
 *
 * Purpose:
 * - Log live Android Accessibility Events.
 * - Log the entire Accessibility Node Tree (UI hierarchy) when a significant event occurs.
 * - Specifically targeted to understand IRCTC's Live Autocomplete behavior.
 *
 * Architecture Note:
 * - This is strictly a Diagnostic tool.
 * - No business logic or automation decisions are made here.
 * - All data will be used to design the exact ActionExecutor/ActionRequest contracts.
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_DIAGNOSTIC"
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility Service Connected (Diagnostic Mode)")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Step 1: Log the basic event details
        val eventType = when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "WINDOW_CHANGED"
            AccessibilityEvent.TYPE_VIEW_CLICKED -> "VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> "TEXT_CHANGED"
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> "SCROLLED"
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> "CONTENT_CHANGED"
            else -> "OTHER (${event.eventType})"
        }

        Log.d(TAG, "Event: $eventType | Package: ${event.packageName} | Class: ${event.className}")

        // Step 2: If Text Changed in IRCTC, log the full UI Tree to see the Autocomplete
        if (event.packageName == "com.irctc.connect") {
            Log.d(TAG, "🔍 IRCTC Text Change Detected! Capturing Full UI Tree...")
            val root = rootInActiveWindow
            if (root != null) {
                logNodeTree(root, 0)
                root.recycle()
            } else {
                Log.d(TAG, "Root node is null.")
            }
        }
    }

    /**
     * Recursively logs the Accessibility Node Tree to understand the UI structure.
     * This is critical for figuring out how IRCTC's Autocomplete list is structured.
     */
    private fun logNodeTree(node: AccessibilityNodeInfo, depth: Int) {
        val indent = " ".repeat(depth * 2)
        val text = node.text?.toString() ?: ""
        val hint = node.hintText?.toString() ?: ""
        val className = node.className?.toString() ?: ""
        val isVisible = node.isVisibleToUser
        val isEditable = node.isEditable
        val isClickable = node.isClickable
        
        // Only log nodes that have content or are interactive
        if (text.isNotBlank() || hint.isNotBlank() || isClickable || isEditable) {
            Log.d(TAG, "$indent🔹 Class: $className | Text: '$text' | Hint: '$hint' | Clickable: $isClickable | Editable: $isEditable | Visible: $isVisible")
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                logNodeTree(child, depth + 1)
                child.recycle()
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility Service Interrupted")
    }
}
