package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
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
 * Stage 3 — Diagnostic Mode
 *
 * PURPOSE:
 * - Verify that VMAX AccessibilityService is actually connected.
 * - Verify that VMAX can observe the IRCTC Accessibility UI.
 * - Capture Accessibility Events.
 * - Capture the current Accessibility Node Tree.
 * - Display diagnostic information directly on screen.
 *
 * DIAGNOSTIC ONLY:
 * - No station database.
 * - No autocomplete implementation.
 * - No automation decision.
 * - No ActionExecutor calls.
 * - No business logic.
 *
 * Target IRCTC package:
 *     cris.org.in.prs.ima
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {

        private const val TAG = "VMAX_DIAGNOSTIC"

        private const val IRCTC_PACKAGE =
            "cris.org.in.prs.ima"

        private const val MAX_LOG_LINES = 120

        private const val OVERLAY_WIDTH_MATCH_PARENT = true
    }

    private var windowManager: WindowManager? = null
    private var overlayView: LinearLayout? = null
    private var logTextView: TextView? = null

    // ------------------------------------------------------------------------
    // SERVICE CONNECTED
    // ------------------------------------------------------------------------

    override fun onServiceConnected() {
        super.onServiceConnected()

        Log.d(
            TAG,
            "VMAX Accessibility Service CONNECTED"
        )

        showDiagnosticOverlay()

        appendDiagnostic(
            "VMAX DIAGNOSTIC\n" +
                "SERVICE: CONNECTED\n" +
                "IRCTC: WAITING\n" +
                "Package: $IRCTC_PACKAGE"
        )
    }

    // ------------------------------------------------------------------------
    // ACCESSIBILITY EVENT
    // ------------------------------------------------------------------------

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {

        if (event == null) {
            return
        }

        val packageName =
            event.packageName?.toString() ?: "UNKNOWN"

        val eventName =
            getEventTypeName(event.eventType)

        /*
         * Always log the event to Logcat.
         * This proves whether the AccessibilityService
         * is receiving events at all.
         */
        Log.d(
            TAG,
            "EVENT=$eventName PACKAGE=$packageName"
        )

        /*
         * Only inspect the IRCTC UI for IRCTC events.
         */
        if (packageName != IRCTC_PACKAGE) {
            return
        }

        appendDiagnostic(
            "[IRCTC EVENT]\n" +
                "$eventName\n" +
                "Package: $packageName"
        )

        captureCurrentRoot()
    }

    // ------------------------------------------------------------------------
    // ROOT NODE CAPTURE
    // ------------------------------------------------------------------------

    private fun captureCurrentRoot() {

        val root =
            rootInActiveWindow

        if (root == null) {

            appendDiagnostic(
                "IRCTC ROOT: NULL"
            )

            Log.d(
                TAG,
                "IRCTC rootInActiveWindow = NULL"
            )

            return
        }

        try {

            appendDiagnostic(
                "IRCTC ROOT: AVAILABLE\n" +
                    "Capturing visible node tree..."
            )

            val builder =
                StringBuilder()

            captureNodeTree(
                node = root,
                depth = 0,
                builder = builder
            )

            val tree =
                builder.toString()

            if (tree.isBlank()) {

                appendDiagnostic(
                    "NODE TREE: EMPTY"
                )

                Log.d(
                    TAG,
                    "IRCTC node tree is empty"
                )

            } else {

                appendDiagnostic(
                    tree
                )

                Log.d(
                    TAG,
                    "IRCTC NODE TREE:\n$tree"
                )
            }

        } catch (exception: Exception) {

            Log.e(
                TAG,
                "Node tree capture failed",
                exception
            )

            appendDiagnostic(
                "TREE ERROR: " +
                    (exception.message ?: "Unknown error")
            )

        } finally {

            root.recycle()
        }
    }

    // ------------------------------------------------------------------------
    // NODE TREE
    // ------------------------------------------------------------------------

    private fun captureNodeTree(
        node: AccessibilityNodeInfo,
        depth: Int,
        builder: StringBuilder
    ) {

        /*
         * Prevent an unexpectedly deep tree from
         * consuming excessive diagnostic space.
         */
        if (depth > 30) {
            return
        }

        val className =
            node.className?.toString() ?: ""

        val text =
            node.text?.toString() ?: ""

        val hint =
            node.hintText?.toString() ?: ""

        val contentDescription =
            node.contentDescription?.toString() ?: ""

        val viewId =
            node.viewIdResourceName ?: ""

        val visible =
            node.isVisibleToUser

        val clickable =
            node.isClickable

        val editable =
            node.isEditable

        val focusable =
            node.isFocusable

        val enabled =
            node.isEnabled

        /*
         * Diagnostic target:
         * keep nodes that contain useful evidence.
         */
        val hasUsefulData =
            text.isNotBlank() ||
                hint.isNotBlank() ||
                contentDescription.isNotBlank() ||
                viewId.isNotBlank() ||
                clickable ||
                editable ||
                focusable

        if (hasUsefulData) {

            val indent =
                "  ".repeat(depth)

            builder.append(
                indent
            )

            builder.append(
                "NODE: $className"
            )

            builder.append(
                " | Visible=$visible"
            )

            builder.append(
                " | Clickable=$clickable"
            )

            builder.append(
                " | Editable=$editable"
            )

            builder.append(
                " | Focusable=$focusable"
            )

            builder.append(
                " | Enabled=$enabled"
            )

            builder.append("\n")

            if (text.isNotBlank()) {

                builder.append(
                    indent
                )

                builder.append(
                    "  Text='$text'\n"
                )
            }

            if (hint.isNotBlank()) {

                builder.append(
                    indent
                )

                builder.append(
                    "  Hint='$hint'\n"
                )
            }

            if (contentDescription.isNotBlank()) {

                builder.append(
                    indent
                )

                builder.append(
                    "  Description='$contentDescription'\n"
                )
            }

            if (viewId.isNotBlank()) {

                builder.append(
                    indent
                )

                builder.append(
                    "  ViewId='$viewId'\n"
                )
            }
        }

        /*
         * Traverse children.
         */
        for (index in 0 until node.childCount) {

            val child =
                node.getChild(index)
                    ?: continue

            try {

                captureNodeTree(
                    node = child,
                    depth = depth + 1,
                    builder = builder
                )

            } finally {

                child.recycle()
            }
        }
    }

    // ------------------------------------------------------------------------
    // DIAGNOSTIC OVERLAY
    // ------------------------------------------------------------------------

    private fun showDiagnosticOverlay() {

        if (overlayView != null) {
            return
        }

        try {

            windowManager =
                getSystemService(
                    WINDOW_SERVICE
                ) as WindowManager

            /*
             * IMPORTANT:
             *
             * AccessibilityService must use
             * TYPE_ACCESSIBILITY_OVERLAY.
             *
             * We do NOT use TYPE_APPLICATION_OVERLAY.
             */
            val overlayType =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
                } else {
                    @Suppress("DEPRECATION")
                    WindowManager.LayoutParams.TYPE_PHONE
                }

            val params =
                WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    overlayType,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT
                )

            params.gravity =
                Gravity.TOP or Gravity.START

            params.x = 0
            params.y = 0

            val container =
                LinearLayout(this).apply {

                    orientation =
                        LinearLayout.VERTICAL

                    setBackgroundColor(
                        Color.argb(
                            220,
                            0,
                            0,
                            0
                        )

                    setPadding(
                        16,
                        16,
                        16,
                        16
                    )
                }

            val title =
                TextView(this).apply {

                    text =
                        "VMAX DIAGNOSTIC"

                    setTextColor(
                        Color.GREEN
                    )

                    textSize =
                        16f

                    setPadding(
                        0,
                        0,
                        0,
                        8
                    )
                }

            val scrollView =
                ScrollView(this).apply {

                    layoutParams =
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            600
                        )
                }

            logTextView =
                TextView(this).apply {

                    text =
                        "Starting diagnostic..."

                    setTextColor(
                        Color.WHITE
                    )

                    textSize =
                        12f

                    setTextIsSelectable(
                        true
                    )
                }

            scrollView.addView(
                logTextView
            )

            container.addView(
                title
            )

            container.addView(
                scrollView
            )

            overlayView =
                container

            windowManager?.addView(
                container,
                params
            )

            Log.d(
                TAG,
                "Diagnostic overlay created successfully"
            )

        } catch (exception: Exception) {

            Log.e(
                TAG,
                "Diagnostic overlay creation failed",
                exception
            )
        }
    }

    // ------------------------------------------------------------------------
    // APPEND DIAGNOSTIC LOG
    // ------------------------------------------------------------------------

    private fun appendDiagnostic(
        message: String
    ) {

        val textView =
            logTextView
                ?: return

        textView.post {

            val oldText =
                textView.text?.toString()
                    ?: ""

            val combined =
                if (oldText.isBlank()) {
                    message
                } else {
                    "$oldText\n\n$message"
                }

            val lines =
                combined.lines()

            val finalText =
                if (lines.size > MAX_LOG_LINES) {
                    lines
                        .takeLast(MAX_LOG_LINES)
                        .joinToString("\n")
                } else {
                    combined
                }

            textView.text =
                finalText
        }
    }

    // ------------------------------------------------------------------------
    // EVENT TYPE
    // ------------------------------------------------------------------------

    private fun getEventTypeName(
        eventType: Int
    ): String {

        return when (eventType) {

            AccessibilityEvent.TYPE_VIEW_CLICKED ->
                "VIEW_CLICKED"

            AccessibilityEvent.TYPE_VIEW_FOCUSED ->
                "VIEW_FOCUSED"

            AccessibilityEvent.TYPE_VIEW_SELECTED ->
                "VIEW_SELECTED"

            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED ->
                "VIEW_TEXT_CHANGED"

            AccessibilityEvent.TYPE_VIEW_SCROLLED ->
                "VIEW_SCROLLED"

            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ->
                "WINDOW_STATE_CHANGED"

            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ->
                "WINDOW_CONTENT_CHANGED"

            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED ->
                "VIEW_TEXT_SELECTION_CHANGED"

            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED ->
                "VIEW_ACCESSIBILITY_FOCUSED"

            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER ->
                "VIEW_HOVER_ENTER"

            AccessibilityEvent.TYPE_VIEW_HOVER_EXIT ->
                "VIEW_HOVER_EXIT"

            else ->
                "OTHER($eventType)"
        }
    }

    // ------------------------------------------------------------------------
    // INTERRUPT
    // ------------------------------------------------------------------------

    override fun onInterrupt() {

        Log.d(
            TAG,
            "Accessibility Service INTERRUPTED"
        )

        appendDiagnostic(
            "SERVICE INTERRUPTED"
        )
    }

    // ------------------------------------------------------------------------
    // DESTROY
    // ------------------------------------------------------------------------

    override fun onDestroy() {

        Log.d(
            TAG,
            "Accessibility Service DESTROYED"
        )

        removeDiagnosticOverlay()

        super.onDestroy()
    }

    // ------------------------------------------------------------------------
    // REMOVE OVERLAY
    // ------------------------------------------------------------------------

    private fun removeDiagnosticOverlay() {

        try {

            val view =
                overlayView

            if (view != null) {

                windowManager?.removeView(
                    view
                )
            }

        } catch (exception: Exception) {

            Log.e(
                TAG,
                "Failed to remove diagnostic overlay",
                exception
            )

        } finally {

            overlayView =
                null

            logTextView =
                null
        }
    }
                        }
