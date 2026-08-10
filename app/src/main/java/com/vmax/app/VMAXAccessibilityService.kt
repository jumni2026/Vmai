package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/**
 * VMAX Enterprise v2.6.1
 *
 * File — VMAXAccessibilityService.kt
 *
 * Stage 3 — On-Screen Diagnostic Overlay
 *
 * Purpose:
 * - Display real-time Accessibility Events directly on the phone screen.
 * - Show captured Accessibility Node Trees when IRCTC text changes.
 * - Verify that the Accessibility Service is actually seeing the IRCTC UI.
 *
 * No business logic.
 * No station database.
 * No automation.
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_OVERLAY"
        private const val MAX_LINES = 50
        private const val OVERLAY_LAYOUT = "VMAX_OVERLAY_LAYOUT"
    }

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var logTextView: TextView? = null
    private var isOverlayVisible = false

    // ------------------------------------------------------------------------
    // SERVICE CONNECTION
    // ------------------------------------------------------------------------

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "VMAX Overlay Service CONNECTED")
        showOverlay("VMAX Overlay Active\nWaiting for IRCTC input...")
    }

    // ------------------------------------------------------------------------
    // ACCESSIBILITY EVENTS
    // ------------------------------------------------------------------------

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val eventTypeName = getEventTypeName(event.eventType)
        val packageName = event.packageName?.toString() ?: "?"

        // ✅ Corrected package name for current IRCTC Rail Connect app
        if (packageName == "cris.org.in.prs.ima") {
            appendLog("[EVENT] $eventTypeName in $packageName")
            val root = rootInActiveWindow
            if (root != null) {
                appendLog("--- CAPTURING TREE ---")
                val treeBuilder = StringBuilder()
                captureTreeToString(root, 0, treeBuilder)
                appendLog(treeBuilder.toString())
                root.recycle()
            }
        }
    }

    // ------------------------------------------------------------------------
    // CAPTURE TREE TO STRING (instead of Logcat)
    // ------------------------------------------------------------------------

    private fun captureTreeToString(node: AccessibilityNodeInfo, depth: Int, sb: StringBuilder) {
        val indent = "  ".repeat(depth)
        val text = node.text?.toString() ?: ""
        val hint = node.hintText?.toString() ?: ""
        val className = node.className?.toString() ?: ""
        val clickable = node.isClickable
        val editable = node.isEditable
        val visible = node.isVisibleToUser

        if (text.isNotBlank() || hint.isNotBlank() || clickable || editable) {
            sb.append("$indent• $className\n")
            if (text.isNotBlank()) sb.append("$indent  Text: '$text'\n")
            if (hint.isNotBlank()) sb.append("$indent  Hint: '$hint'\n")
            if (clickable) sb.append("$indent  Clickable: YES\n")
            if (editable) sb.append("$indent  Editable: YES\n")
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            captureTreeToString(child, depth + 1, sb)
            child.recycle()
        }
    }

    // ------------------------------------------------------------------------
    // OVERLAY LOGGING
    // ------------------------------------------------------------------------

    private fun showOverlay(initialText: String) {
        if (isOverlayVisible) return
        try {
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            )
            params.gravity = Gravity.TOP or Gravity.START

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setBackgroundColor(0xAA000000.toInt())
                setPadding(16, 16, 16, 16)
            }

            val scrollView = ScrollView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            }

            logTextView = TextView(this).apply {
                text = initialText
                setTextColor(0xFF00FF00.toInt())
                textSize = 12f
                setTextIsSelectable(true)
            }

            scrollView.addView(logTextView)
            layout.addView(scrollView)

            val closeBtn = TextView(this).apply {
                text = "X (Close)"
                setTextColor(0xFFFF0000.toInt())
                textSize = 14f
                setOnClickListener {
                    hideOverlay()
                }
            }
            layout.addView(closeBtn)

            overlayView = layout
            windowManager?.addView(layout, params)
            isOverlayVisible = true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create overlay: ${e.message}")
        }
    }

    private fun appendLog(line: String) {
        logTextView?.post {
            val currentText = logTextView?.text?.toString() ?: ""
            val newText = if (currentText.lines().size >= MAX_LINES) {
                currentText.lines().drop(1).joinToString("\n") + "\n$line"
            } else {
                "$currentText\n$line"
            }
            logTextView?.text = newText
        }
    }

    private fun hideOverlay() {
        try {
            if (overlayView != null) {
                windowManager?.removeView(overlayView)
                overlayView = null
                isOverlayVisible = false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to hide overlay: ${e.message}")
        }
    }

    // ------------------------------------------------------------------------
    // INTERRUPT / DESTROY
    // ------------------------------------------------------------------------

    override fun onInterrupt() {
        Log.d(TAG, "Service INTERRUPTED")
        hideOverlay()
    }

    override fun onDestroy() {
        Log.d(TAG, "Service DESTROYED")
        hideOverlay()
        super.onDestroy()
    }

    // ------------------------------------------------------------------------
    // HELPER: EVENT TYPE
    // ------------------------------------------------------------------------

    private fun getEventTypeName(eventType: Int): String {
        return when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> "VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> "VIEW_FOCUSED"
            AccessibilityEvent.TYPE_VIEW_SELECTED -> "VIEW_SELECTED"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> "VIEW_TEXT_CHANGED"
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> "VIEW_SCROLLED"
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "WINDOW_STATE_CHANGED"
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> "WINDOW_CONTENT_CHANGED"
            else -> "OTHER($eventType)"
        }
    }
}
