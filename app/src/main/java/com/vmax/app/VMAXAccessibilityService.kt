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
 * Stage 3 — Runtime Diagnostic
 *
 * Purpose:
 * - Verify AccessibilityService connection.
 * - Verify real AccessibilityEvent delivery.
 * - Log event package/class information.
 * - Capture the active Accessibility Window.
 * - Capture the Accessibility Node Tree.
 *
 * IMPORTANT:
 * - Diagnostic only.
 * - No automation.
 * - No station database.
 * - No autocomplete selection.
 * - No ActionExecutor changes.
 * - No business logic.
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {

        private const val TAG = "VMAX_DIAGNOSTIC"

        /**
         * Prevents excessive tree logging from every minor event.
         *
         * Tree capture is still triggered for the important events below.
         */
        private const val MAX_TREE_DEPTH = 30
    }

    // ------------------------------------------------------------------------
    // SERVICE CONNECTION
    // ------------------------------------------------------------------------

    override fun onServiceConnected() {
        super.onServiceConnected()

        Log.d(
            TAG,
            "=================================================="
        )

        Log.d(
            TAG,
            "VMAX AccessibilityService CONNECTED"
        )

        Log.d(
            TAG,
            "Service package = $packageName"
        )

        Log.d(
            TAG,
            "=================================================="
        )

        /*
         * Capture the current window once when the service connects.
         *
         * This does NOT perform any action.
         */
        captureRootTree("SERVICE_CONNECTED")
    }

    // ------------------------------------------------------------------------
    // ACCESSIBILITY EVENTS
    // ------------------------------------------------------------------------

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {

        if (event == null) {
            Log.d(
                TAG,
                "EVENT = NULL"
            )
            return
        }

        val eventType = getEventTypeName(
            event.eventType
        )

        val packageName =
            event.packageName?.toString()
                ?: "<null>"

        val className =
            event.className?.toString()
                ?: "<null>"

        val text =
            event.text
                ?.joinToString(" | ")
                ?: ""

        val contentDescription =
            event.contentDescription
                ?.toString()
                ?: ""

        Log.d(
            TAG,
            "--------------------------------------------------"
        )

        Log.d(
            TAG,
            "EVENT RECEIVED"
        )

        Log.d(
            TAG,
            "Type = $eventType"
        )

        Log.d(
            TAG,
            "TypeCode = ${event.eventType}"
        )

        Log.d(
            TAG,
            "Package = $packageName"
        )

        Log.d(
            TAG,
            "Class = $className"
        )

        Log.d(
            TAG,
            "Text = '$text'"
        )

        Log.d(
            TAG,
            "ContentDescription = '$contentDescription'"
        )

        Log.d(
            TAG,
            "EventTime = ${event.eventTime}"
        )

        Log.d(
            TAG,
            "--------------------------------------------------"
        )

        /*
         * Important diagnostic events.
         *
         * We intentionally do NOT check for a specific package name.
         *
         * This allows us to discover the actual package used by the
         * application currently visible on the device.
         */
        when (event.eventType) {

            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED,
            AccessibilityEvent.TYPE_VIEW_CLICKED,
            AccessibilityEvent.TYPE_VIEW_FOCUSED,
            AccessibilityEvent.TYPE_VIEW_SELECTED -> {

                captureRootTree(
                    reason = eventType
                )
            }

            else -> {
                /*
                 * Event logged above.
                 * No tree capture required for less useful events.
                 */
            }
        }
    }

    // ------------------------------------------------------------------------
    // ROOT WINDOW DIAGNOSTIC
    // ------------------------------------------------------------------------

    private fun captureRootTree(
        reason: String
    ) {

        Log.d(
            TAG,
            "##################################################"
        )

        Log.d(
            TAG,
            "ROOT TREE CAPTURE"
        )

        Log.d(
            TAG,
            "Reason = $reason"
        )

        val root = rootInActiveWindow

        if (root == null) {

            Log.d(
                TAG,
                "ROOT = NULL"
            )

            Log.d(
                TAG,
                "##################################################"
            )

            return
        }

        try {

            Log.d(
                TAG,
                "ROOT package = ${
                    root.packageName?.toString() ?: "<null>"
                }"
            )

            Log.d(
                TAG,
                "ROOT class = ${
                    root.className?.toString() ?: "<null>"
                }"
            )

            Log.d(
                TAG,
                "ROOT childCount = ${root.childCount}"
            )

            logNodeTree(
                node = root,
                depth = 0,
                path = "0"
            )

        } catch (exception: Exception) {

            Log.e(
                TAG,
                "ROOT TREE ERROR: ${exception.message}",
                exception
            )

        } finally {

            root.recycle()

            Log.d(
                TAG,
                "ROOT TREE CAPTURE END"
            )

            Log.d(
                TAG,
                "##################################################"
            )
        }
    }

    // ------------------------------------------------------------------------
    // NODE TREE
    // ------------------------------------------------------------------------

    private fun logNodeTree(
        node: AccessibilityNodeInfo,
        depth: Int,
        path: String
    ) {

        if (depth > MAX_TREE_DEPTH) {

            Log.d(
                TAG,
                "TREE LIMIT REACHED | path=$path"
            )

            return
        }

        val indent =
            "  ".repeat(depth)

        val className =
            node.className?.toString()
                ?: ""

        val packageName =
            node.packageName?.toString()
                ?: ""

        val text =
            node.text?.toString()
                ?: ""

        val hint =
            node.hintText?.toString()
                ?: ""

        val contentDescription =
            node.contentDescription?.toString()
                ?: ""

        val viewId =
            node.viewIdResourceName
                ?: ""

        val visible =
            node.isVisibleToUser

        val enabled =
            node.isEnabled

        val clickable =
            node.isClickable

        val focusable =
            node.isFocusable

        val focused =
            node.isFocused

        val editable =
            node.isEditable

        val selected =
            node.isSelected

        val scrollable =
            node.isScrollable

        val childCount =
            node.childCount

        /*
         * Log nodes that contain useful evidence.
         *
         * Container-only nodes are also logged when they have children,
         * because autocomplete structures are often nested.
         */
        val hasUsefulData =
            text.isNotBlank() ||
            hint.isNotBlank() ||
            contentDescription.isNotBlank() ||
            viewId.isNotBlank() ||
            clickable ||
            editable ||
            focused ||
            selected ||
            scrollable ||
            childCount > 0

        if (hasUsefulData) {

            Log.d(
                TAG,
                "$indent" +
                    "NODE[$path] " +
                    "class='$className' " +
                    "package='$packageName' " +
                    "text='$text' " +
                    "hint='$hint' " +
                    "desc='$contentDescription' " +
                    "id='$viewId' " +
                    "visible=$visible " +
                    "enabled=$enabled " +
                    "clickable=$clickable " +
                    "focusable=$focusable " +
                    "focused=$focused " +
                    "editable=$editable " +
                    "selected=$selected " +
                    "scrollable=$scrollable " +
                    "children=$childCount"
            )
        }

        for (index in 0 until childCount) {

            val child = node.getChild(index)
                ?: continue

            try {

                logNodeTree(
                    node = child,
                    depth = depth + 1,
                    path = "$path.$index"
                )

            } catch (exception: Exception) {

                Log.e(
                    TAG,
                    "${indent}CHILD ERROR path=$path.$index: " +
                        "${exception.message}"
                )

            } finally {

                child.recycle()
            }
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

            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED ->
                "NOTIFICATION_STATE_CHANGED"

            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER ->
                "VIEW_HOVER_ENTER"

            AccessibilityEvent.TYPE_VIEW_HOVER_EXIT ->
                "VIEW_HOVER_EXIT"

            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START ->
                "TOUCH_EXPLORATION_GESTURE_START"

            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END ->
                "TOUCH_EXPLORATION_GESTURE_END"

            AccessibilityEvent.TYPE_GESTURE_DETECTION_START ->
                "GESTURE_DETECTION_START"

            AccessibilityEvent.TYPE_GESTURE_DETECTION_END ->
                "GESTURE_DETECTION_END"

            AccessibilityEvent.TYPE_ANNOUNCEMENT ->
                "ANNOUNCEMENT"

            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START ->
                "TOUCH_INTERACTION_START"

            AccessibilityEvent.TYPE_TOUCH_INTERACTION_END ->
                "TOUCH_INTERACTION_END"

            AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT ->
                "ASSIST_READING_CONTEXT"

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
            "VMAX AccessibilityService INTERRUPTED"
        )
    }

    // ------------------------------------------------------------------------
    // SERVICE DESTROY
    // ------------------------------------------------------------------------

    override fun onDestroy() {

        Log.d(
            TAG,
            "VMAX AccessibilityService DESTROYED"
        )

        super.onDestroy()
    }
}
