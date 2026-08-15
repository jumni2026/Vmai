package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

/**
 * Android-specific implementation of ActionExecutor
 * Uses AccessibilityService to perform UI actions
 */
class AndroidActionExecutor(
    private val service: AccessibilityService
) : ActionExecutor {

    companion object {
        private const val TAG = "AndroidActionExecutor"
        private const val CLICK_RETRY_DELAY_MS = 300L
        private const val MAX_RETRIES = 3
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun executeAction(request: ActionExecutor.ActionRequest): Result<Unit> {
        Log.d(TAG, "Executing action: ${request.type} on target: ${request.targetId}")

        return try {
            when (request.type) {
                ActionExecutor.ActionType.CLICK -> executeClick(request)
                ActionExecutor.ActionType.SET_TEXT -> executeSetText(request)
                ActionExecutor.ActionType.SCROLL -> executeScroll(request)
                ActionExecutor.ActionType.WAIT -> executeWait(request)
                ActionExecutor.ActionType.BACK -> executeBack(request)
                ActionExecutor.ActionType.HOME -> executeHome(request)
                ActionExecutor.ActionType.RECENT_APPS -> executeRecentApps(request)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Action execution failed", e)
            Result.Error(e)
        }
    }

    private fun executeClick(request: ActionExecutor.ActionRequest): Result<Unit> {
        // ✅ FIX: Smart-cast issue resolved by using local variable
        val coordinates = request.coordinates
        
        return if (request.targetId != null) {
            executeClickById(request.targetId, request.sessionId)
        } else if (coordinates != null) {
            // ✅ FIXED: Using local variable 'coordinates' instead of request.coordinates
            val x = coordinates.first
            val y = coordinates.second
            executeClickByCoordinates(x, y, request.sessionId)
        } else {
            Result.Error(IllegalArgumentException("Either targetId or coordinates must be provided for CLICK action"))
        }
    }

    private fun executeClickById(
        targetId: String,
        sessionId: String
    ): Result<Unit> {
        Log.d(TAG, "Click by ID: $targetId")

        var retries = 0
        var lastError: Exception? = null

        while (retries < MAX_RETRIES) {
            try {
                val root = service.rootInActiveWindow
                if (root == null) {
                    lastError = IllegalStateException("No active window found")
                    Thread.sleep(CLICK_RETRY_DELAY_MS)
                    retries++
                    continue
                }

                val node = findNodeById(root, targetId)
                if (node == null) {
                    lastError = IllegalStateException("Node not found: $targetId")
                    root.recycle()
                    Thread.sleep(CLICK_RETRY_DELAY_MS)
                    retries++
                    continue
                }

                // Try to click
                val success = performClick(node)
                node.recycle()
                root.recycle()

                if (success) {
                    return Result.Success(Unit)
                } else {
                    lastError = IllegalStateException("Click failed for: $targetId")
                    Thread.sleep(CLICK_RETRY_DELAY_MS)
                    retries++
                }
            } catch (e: Exception) {
                lastError = e
                Thread.sleep(CLICK_RETRY_DELAY_MS)
                retries++
            }
        }

        return Result.Error(lastError ?: IllegalStateException("Click failed after $MAX_RETRIES retries"))
    }

    private fun executeClickByCoordinates(
        x: Int,
        y: Int,
        sessionId: String
    ): Result<Unit> {
        Log.d(TAG, "Click by coordinates: ($x, $y)")

        try {
            val root = service.rootInActiveWindow
            if (root == null) {
                return Result.Error(IllegalStateException("No active window found"))
            }

            val node = findNodeAtCoordinates(root, x, y)
            if (node == null) {
                root.recycle()
                return Result.Error(IllegalStateException("No clickable node found at coordinates ($x, $y)"))
            }

            val success = performClick(node)
            node.recycle()
            root.recycle()

            return if (success) {
                Result.Success(Unit)
            } else {
                Result.Error(IllegalStateException("Click at coordinates failed"))
            }
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    private fun executeSetText(request: ActionExecutor.ActionRequest): Result<Unit> {
        val targetId = request.targetId
        val text = request.text

        if (targetId == null) {
            return Result.Error(IllegalArgumentException("targetId is required for SET_TEXT action"))
        }

        if (text == null) {
            return Result.Error(IllegalArgumentException("text is required for SET_TEXT action"))
        }

        Log.d(TAG, "SetText: $targetId = '$text'")

        try {
            val root = service.rootInActiveWindow
            if (root == null) {
                return Result.Error(IllegalStateException("No active window found"))
            }

            val node = findNodeById(root, targetId)
            if (node == null) {
                root.recycle()
                return Result.Error(IllegalStateException("Node not found: $targetId"))
            }

            // Ensure the node is editable
            if (!node.isEditable) {
                node.recycle()
                root.recycle()
                return Result.Error(IllegalStateException("Node is not editable: $targetId"))
            }

            // Focus the node first
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)

            // Set text - use ACTION_SET_TEXT or perform action
            val success = performSetText(node, text)
            node.recycle()
            root.recycle()

            return if (success) {
                Result.Success(Unit)
            } else {
                Result.Error(IllegalStateException("SetText failed for: $targetId"))
            }
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    private fun executeScroll(request: ActionExecutor.ActionRequest): Result<Unit> {
        Log.d(TAG, "Scroll action requested")

        try {
            val root = service.rootInActiveWindow
            if (root == null) {
                return Result.Error(IllegalStateException("No active window found"))
            }

            // Find a scrollable node
            val scrollable = findScrollableNode(root)
            if (scrollable == null) {
                root.recycle()
                return Result.Error(IllegalStateException("No scrollable node found"))
            }

            // Determine scroll direction from request params
            val direction = request.direction ?: ActionExecutor.ScrollDirection.FORWARD
            val action = when (direction) {
                ActionExecutor.ScrollDirection.FORWARD -> AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                ActionExecutor.ScrollDirection.BACKWARD -> AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
                ActionExecutor.ScrollDirection.UP -> AccessibilityNodeInfo.ACTION_SCROLL_UP
                ActionExecutor.ScrollDirection.DOWN -> AccessibilityNodeInfo.ACTION_SCROLL_DOWN
                ActionExecutor.ScrollDirection.LEFT -> AccessibilityNodeInfo.ACTION_SCROLL_LEFT
                ActionExecutor.ScrollDirection.RIGHT -> AccessibilityNodeInfo.ACTION_SCROLL_RIGHT
            }

            val success = scrollable.performAction(action)
            scrollable.recycle()
            root.recycle()

            return if (success) {
                Result.Success(Unit)
            } else {
                Result.Error(IllegalStateException("Scroll action failed"))
            }
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    private fun executeWait(request: ActionExecutor.ActionRequest): Result<Unit> {
        val waitMs = request.waitMs ?: 1000L
        Log.d(TAG, "Wait: ${waitMs}ms")

        try {
            Thread.sleep(waitMs)
            return Result.Success(Unit)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            return Result.Error(e)
        }
    }

    private fun executeBack(request: ActionExecutor.ActionRequest): Result<Unit> {
        Log.d(TAG, "Back action")

        try {
            val success = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            return if (success) {
                Result.Success(Unit)
            } else {
                Result.Error(IllegalStateException("Back action failed"))
            }
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    private fun executeHome(request: ActionExecutor.ActionRequest): Result<Unit> {
        Log.d(TAG, "Home action")

        try {
            val success = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
            return if (success) {
                Result.Success(Unit)
            } else {
                Result.Error(IllegalStateException("Home action failed"))
            }
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    private fun executeRecentApps(request: ActionExecutor.ActionRequest): Result<Unit> {
        Log.d(TAG, "Recent apps action")

        try {
            val success = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
            return if (success) {
                Result.Success(Unit)
            } else {
                Result.Error(IllegalStateException("Recent apps action failed"))
            }
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    // --- Helper Methods ---

    private fun findNodeById(root: AccessibilityNodeInfo, id: String): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>().apply { add(root) }

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            // Check if this node has the target ID
            val viewId = node.viewIdResourceName
            if (viewId != null && viewId == id) {
                // We need to return a new instance
                return AccessibilityNodeInfo.obtain(node)
            }

            // Add children to queue
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }

            if (node !== root) {
                node.recycle()
            }
        }

        return null
    }

    private fun findNodeAtCoordinates(
        root: AccessibilityNodeInfo,
        x: Int,
        y: Int
    ): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>().apply { add(root) }

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            // Check if node contains the coordinates and is clickable
            val bounds = android.graphics.Rect()
            node.getBoundsInScreen(bounds)

            if (bounds.contains(x, y) && node.isClickable) {
                return AccessibilityNodeInfo.obtain(node)
            }

            // Add children to queue (they might be more specific)
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }

            if (node !== root) {
                node.recycle()
            }
        }

        return null
    }

    private fun findScrollableNode(root: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>().apply { add(root) }

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            // Check if node is scrollable
            if (node.isScrollable) {
                return AccessibilityNodeInfo.obtain(node)
            }

            // Add children to queue
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }

            if (node !== root) {
                node.recycle()
            }
        }

        return null
    }

    private fun performClick(node: AccessibilityNodeInfo): Boolean {
        // Try ACTION_CLICK first
        if (node.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
            return true
        }

        // Try ACTION_SELECT if click fails
        if (node.performAction(AccessibilityNodeInfo.ACTION_SELECT)) {
            return true
        }

        // Try ACTION_SET_SELECTION if available
        if (node.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION)) {
            return true
        }

        return false
    }

    private fun performSetText(node: AccessibilityNodeInfo, text: String): Boolean {
        // Try ACTION_SET_TEXT
        val arguments = android.os.Bundle()
        arguments.putCharSequence(
            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
            text
        )

        if (node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)) {
            return true
        }

        // Fallback: Try ACTION_PASTE if available
        // Some apps might need this for input fields
        if (node.performAction(AccessibilityNodeInfo.ACTION_PASTE)) {
            // Then we'd need to clear and set text differently
            // This is a simplified fallback
            return false
        }

        return false
    }
}
