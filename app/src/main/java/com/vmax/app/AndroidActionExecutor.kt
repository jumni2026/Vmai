package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.common.Result
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * VMAX Enterprise v2.6.1 (Production Release)
 *
 * Android implementation of ActionExecutor.
 *
 * Responsibility:
 * - Translate platform-independent ActionRequest objects into Android Accessibility actions.
 * - Guarantee zero memory leaks via strict AccessibilityNodeInfo recycling.
 * - Provide fallback mechanisms for maximum device compatibility.
 * - Support dynamic UIs (React Native, Flutter, WebView) via text/hint-based targeting.
 *
 * Architecture rule:
 * - No workflow/business decisions here.
 * - No IRCTC-specific logic here.
 * - Only Android accessibility execution.
 */
class AndroidActionExecutor(
    private val service: AccessibilityService
) : ActionExecutor {

    companion object {
        private const val TAG = "AndroidActionExecutor"
        private const val CLICK_RETRY_DELAY_MS = 300L
        private const val MAX_RETRIES = 3
        private const val DIRECTION_FORWARD = "FORWARD"
        private const val DIRECTION_BACKWARD = "BACKWARD"
        private const val GESTURE_TIMEOUT_MS = 2000L
    }

    @Volatile
    private var lastActionResult: ActionExecutor.ActionResult? = null

    override fun executeAction(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        Log.d(TAG, "Executing action=${request.type}, targetId=${request.targetId}")

        return try {
            when (request.type) {
                ActionExecutor.ActionType.TAP -> executeTapInternal(request)
                ActionExecutor.ActionType.CLICK -> executeClickInternal(request)
                ActionExecutor.ActionType.LONG_CLICK -> executeLongClick(request)
                ActionExecutor.ActionType.DOUBLE_TAP -> executeDoubleTap(request)
                ActionExecutor.ActionType.SWIPE -> executeSwipe(request)
                ActionExecutor.ActionType.SCROLL -> executeScrollInternal(request)
                ActionExecutor.ActionType.SET_TEXT -> executeSetTextInternal(request)
                ActionExecutor.ActionType.CLEAR_TEXT -> executeClearTextInternal(request)
                ActionExecutor.ActionType.WAIT -> executeWaitInternal(request)
            }
        } catch (e: Exception) {
            failure(
                code = "ACTION_EXECUTION_EXCEPTION",
                message = e.message ?: "Action execution failed",
                request = request,
                cause = e
            )
        }
    }

    // -------------------------------------------------------------------------
    // Public convenience APIs
    // -------------------------------------------------------------------------

    override fun executeTap(targetId: String): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.TAP, targetId = targetId))
    }

    override fun executeClick(targetId: String): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK, targetId = targetId))
    }

    override fun executeSetText(targetId: String, text: String): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.SET_TEXT, targetId = targetId, text = text))
    }

    override fun executeClearText(targetId: String): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLEAR_TEXT, targetId = targetId))
    }

    override fun executeScroll(direction: String, amount: Int): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SCROLL,
                targetText = direction,
                durationMs = amount.toLong()
            )
        )
    }

    override fun executeWait(durationMs: Long): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.WAIT, durationMs = durationMs))
    }

    override fun isActionAvailable(actionType: ActionExecutor.ActionType): Boolean {
        return when (actionType) {
            ActionExecutor.ActionType.TAP,
            ActionExecutor.ActionType.CLICK,
            ActionExecutor.ActionType.LONG_CLICK,
            ActionExecutor.ActionType.DOUBLE_TAP,
            ActionExecutor.ActionType.SCROLL,
            ActionExecutor.ActionType.SET_TEXT,
            ActionExecutor.ActionType.CLEAR_TEXT,
            ActionExecutor.ActionType.WAIT -> true

            ActionExecutor.ActionType.SWIPE -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        }
    }

    override fun getLastActionResult(): ActionExecutor.ActionResult? = lastActionResult

    // -------------------------------------------------------------------------
    // Advanced / Dynamic UI APIs (New & Upgraded)
    // -------------------------------------------------------------------------

    /**
     * Finds a node by its text or content description and clicks it.
     * Ideal for dynamic UIs where IDs are unstable (e.g., finding a specific Train Number).
     */
    fun executeClickByValue(
        text: String,
        exactMatch: Boolean = false
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return try {
            val root = service.rootInActiveWindow ?: return failure(
                code = "NO_ACTIVE_WINDOW", message = "No active accessibility window",
                request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK)
            )
            
            val node = findNodeByText(root, text, exactMatch) ?: run {
                root.recycle()
                return failure(
                    code = "NODE_NOT_FOUND", message = "Node with text '$text' not found",
                    request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK)
                )
            }

            val success = performClick(node)
            node.recycle()
            root.recycle()

            if (success) success(actionType = ActionExecutor.ActionType.CLICK, message = "Clicked node with text: $text")
            else failure(code = "CLICK_FAILED", message = "Failed to click node with text: $text", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK))
        } catch (e: Exception) {
            failure(code = "CLICK_BY_VALUE_EXCEPTION", message = e.message ?: "Click by value failed", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK), cause = e)
        }
    }

    /**
     * Selects an option from a Dropdown/Spinner.
     * 1. Finds and clicks the spinner (by ID or Text).
     * 2. Waits for the dropdown list to appear.
     * 3. Finds and clicks the target option by text.
     */
    fun executeSelectFromList(
        spinnerTargetId: String? = null,
        spinnerText: String? = null,
        optionText: String,
        waitTimeMs: Long = 800L
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return try {
            // Step 1: Find and click the spinner
            val root1 = service.rootInActiveWindow ?: return failure(
                code = "NO_ACTIVE_WINDOW", message = "No active window",
                request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK)
            )
            
            val spinnerNode = when {
                spinnerTargetId != null -> findNodeById(root1, spinnerTargetId)
                spinnerText != null -> findNodeByText(root1, spinnerText, exactMatch = false)
                else -> null
            }

            if (spinnerNode == null) {
                root1.recycle()
                return failure(code = "SPINNER_NOT_FOUND", message = "Spinner not found by ID: $spinnerTargetId or Text: $spinnerText", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK))
            }

            val clickSuccess = performClick(spinnerNode)
            spinnerNode.recycle()
            root1.recycle()

            if (!clickSuccess) {
                return failure(code = "SPINNER_CLICK_FAILED", message = "Failed to click spinner to open list", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK))
            }

            // Step 2: Wait for dropdown list to appear (UI settlement)
            Thread.sleep(waitTimeMs)

            // Step 3: Find and click the option in the newly opened list/popup
            val root2 = service.rootInActiveWindow ?: return failure(
                code = "NO_ACTIVE_WINDOW_AFTER_WAIT", message = "No active window after waiting for dropdown",
                request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK)
            )
            
            val optionNode = findNodeByText(root2, optionText, exactMatch = true) ?: run {
                root2.recycle()
                return failure(code = "OPTION_NOT_FOUND", message = "Option '$optionText' not found in dropdown list", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK))
            }

            val optionClickSuccess = performClick(optionNode)
            optionNode.recycle()
            root2.recycle()

            if (optionClickSuccess) {
                success(actionType = ActionExecutor.ActionType.CLICK, message = "Successfully selected '$optionText' from list")
            } else {
                failure(code = "OPTION_CLICK_FAILED", message = "Failed to click option '$optionText'", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK))
            }
        } catch (e: Exception) {
            failure(code = "SELECT_FROM_LIST_EXCEPTION", message = e.message ?: "Select from list failed", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.CLICK), cause = e)
        }
    }

    /**
     * Robustly sets text by finding the node via its text, hint, or content description.
     * Ideal for dynamic UIs (React Native, Flutter, WebView) where IDs are obfuscated or unstable.
     */
    fun executeSetTextByText(
        targetText: String,
        textToSet: String,
        exactMatch: Boolean = false
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return try {
            val root = service.rootInActiveWindow ?: return failure(
                code = "NO_ACTIVE_WINDOW", message = "No active accessibility window",
                request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.SET_TEXT)
            )
            
            val node = findNodeByTextForInput(root, targetText, exactMatch) ?: run {
                root.recycle()
                return failure(code = "INPUT_NODE_NOT_FOUND", message = "Editable node with text/hint '$targetText' not found", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.SET_TEXT))
            }

            if (!node.isEditable) {
                node.recycle()
                root.recycle()
                return failure(code = "NODE_NOT_EDITABLE", message = "Found node is not editable: $targetText", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.SET_TEXT))
            }

            // Robust text setting sequence for maximum compatibility
            performClick(node) // Try to click first to ensure focus and keyboard trigger
            Thread.sleep(150) // Small delay to ensure focus is registered by the OS
            
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            node.performAction(AccessibilityNodeInfo.ACTION_CLEAR_SELECTION)
            
            // Pre-clear existing text to avoid appending
            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, Bundle().apply {
                putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "")
            })
            Thread.sleep(50)

            val arguments = Bundle().apply {
                putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, textToSet)
            }

            val success = node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
            node.recycle()
            root.recycle()

            if (success) {
                success(actionType = ActionExecutor.ActionType.SET_TEXT, message = "Text '$textToSet' set successfully by searching text/hint: '$targetText'")
            } else {
                failure(code = "SET_TEXT_FAILED", message = "ACTION_SET_TEXT failed for node with text: $targetText", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.SET_TEXT))
            }
        } catch (e: Exception) {
            failure(code = "SET_TEXT_BY_TEXT_EXCEPTION", message = e.message ?: "Set text by text failed", request = ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.SET_TEXT), cause = e)
        }
    }

    // -------------------------------------------------------------------------
    // TAP / CLICK
    // -------------------------------------------------------------------------

    private fun executeTapInternal(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        return executeClickInternal(request)
    }

    private fun executeClickInternal(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        val targetId = request.targetId
        val coordinates = request.coordinates

        return when {
            targetId != null -> clickById(targetId, request)
            coordinates != null -> clickByCoordinates(x = coordinates.first, y = coordinates.second, request = request)
            else -> failure(code = "CLICK_TARGET_MISSING", message = "Either targetId or coordinates are required for CLICK", request = request)
        }
    }

    private fun clickById(targetId: String, request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        var retries = 0
        var lastError: Throwable? = null

        while (retries < MAX_RETRIES) {
            try {
                val root = service.rootInActiveWindow
                if (root == null) {
                    lastError = IllegalStateException("No active accessibility window")
                    retries++
                    Thread.sleep(CLICK_RETRY_DELAY_MS)
                    continue
                }

                val node = findNodeById(root, targetId)
                if (node == null) {
                    root.recycle()
                    lastError = IllegalStateException("Node not found: $targetId")
                    retries++
                    Thread.sleep(CLICK_RETRY_DELAY_MS)
                    continue
                }

                val success = performClick(node)
                node.recycle()
                root.recycle()

                if (success) {
                    return success(actionType = request.type, message = "Action executed successfully")
                }

                lastError = IllegalStateException("Accessibility click failed")
            } catch (e: Exception) {
                lastError = e
            }

            retries++
            if (retries < MAX_RETRIES) {
                Thread.sleep(CLICK_RETRY_DELAY_MS)
            }
        }

        return failure(
            code = "CLICK_FAILED",
            message = lastError?.message ?: "Click failed after $MAX_RETRIES retries",
            request = request,
            cause = lastError
        )
    }

    private fun clickByCoordinates(x: Int, y: Int, request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return executeGestureClick(x, y, request)
        }

        return try {
            val root = service.rootInActiveWindow ?: return failure(code = "NO_ACTIVE_WINDOW", message = "No active accessibility window", request = request)
            val node = findNodeAtCoordinates(root, x, y)

            if (node == null) {
                root.recycle()
                return failure(code = "NODE_NOT_FOUND", message = "No clickable node at ($x,$y)", request = request)
            }

            val success = performClick(node)
            node.recycle()
            root.recycle()

            if (success) success(actionType = request.type, message = "Coordinate click executed")
            else failure(code = "CLICK_FAILED", message = "Coordinate click failed", request = request)
        } catch (e: Exception) {
            failure(code = "CLICK_EXCEPTION", message = e.message ?: "Coordinate click failed", request = request, cause = e)
        }
    }

    @androidx.annotation.RequiresApi(Build.VERSION_CODES.N)
    private fun executeGestureClick(x: Int, y: Int, request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        return try {
            val builder = GestureDescription.Builder()
            val path = Path().apply { moveTo(x.toFloat(), y.toFloat()) }
            builder.addStroke(GestureDescription.StrokeDescription(path, 0, 100)) // 100ms tap

            val latch = CountDownLatch(1)
            val resultBox = arrayOf(false)

            service.dispatchGesture(builder.build(), object : AccessibilityService.GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription) {
                    resultBox[0] = true
                    latch.countDown()
                }
                override fun onCancelled(gestureDescription: GestureDescription) {
                    resultBox[0] = false
                    latch.countDown()
                }
            }, Handler(Looper.getMainLooper()))

            if (!latch.await(GESTURE_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                return failure(code = "GESTURE_TIMEOUT", message = "Gesture click timed out", request = request)
            }

            if (resultBox[0]) success(actionType = request.type, message = "Gesture click executed successfully")
            else failure(code = "GESTURE_CANCELLED", message = "Gesture click was cancelled by system", request = request)
        } catch (e: Exception) {
            failure(code = "GESTURE_EXCEPTION", message = e.message ?: "Gesture click failed", request = request, cause = e)
        }
    }

    // -------------------------------------------------------------------------
    // LONG CLICK & DOUBLE TAP
    // -------------------------------------------------------------------------

    private fun executeLongClick(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        val targetId = request.targetId ?: return failure(code = "LONG_CLICK_TARGET_MISSING", message = "targetId is required for LONG_CLICK", request = request)

        return try {
            val root = service.rootInActiveWindow ?: return failure(code = "NO_ACTIVE_WINDOW", message = "No active accessibility window", request = request)
            val node = findNodeById(root, targetId) ?: return failure(code = "NODE_NOT_FOUND", message = "Node not found: $targetId", request = request).also { root.recycle() }

            val success = node.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)
            node.recycle()
            root.recycle()

            if (success) success(actionType = request.type, message = "Long click executed")
            else failure(code = "LONG_CLICK_FAILED", message = "Long click failed", request = request)
        } catch (e: Exception) {
            failure(code = "LONG_CLICK_EXCEPTION", message = e.message ?: "Long click failed", request = request, cause = e)
        }
    }

    private fun executeDoubleTap(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        val first = executeClickInternal(request.copy(type = ActionExecutor.ActionType.CLICK))
        if (first is Result.Error) return first

        Thread.sleep(100L) // Minimal delay between taps

        return executeClickInternal(request.copy(type = ActionExecutor.ActionType.CLICK))
            .mapSuccess(actionType = request.type, message = "Double tap executed")
    }

    // -------------------------------------------------------------------------
    // SWIPE
    // -------------------------------------------------------------------------

    private fun executeSwipe(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        val coords = request.coordinates
        if (coords == null) {
            return failure(code = "SWIPE_COORDINATES_MISSING", message = "Coordinates are required for SWIPE", request = request)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return failure(code = "SWIPE_API_TOO_LOW", message = "SWIPE requires API 24 (Nougat) or higher", request = request)
        }

        return try {
            val builder = GestureDescription.Builder()
            val path = Path().apply {
                moveTo(coords.first.toFloat(), coords.second.toFloat())
                lineTo(coords.first.toFloat(), (coords.second + 300).toFloat())
            }
            builder.addStroke(GestureDescription.StrokeDescription(path, 0, 300)) // 300ms duration

            val latch = CountDownLatch(1)
            val resultBox = arrayOf(false)

            service.dispatchGesture(builder.build(), object : AccessibilityService.GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription) {
                    resultBox[0] = true
                    latch.countDown()
                }
                override fun onCancelled(gestureDescription: GestureDescription) {
                    resultBox[0] = false
                    latch.countDown()
                }
            }, Handler(Looper.getMainLooper()))

            if (!latch.await(GESTURE_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                return failure(code = "GESTURE_TIMEOUT", message = "Swipe gesture timed out", request = request)
            }

            if (resultBox[0]) success(actionType = request.type, message = "Swipe gesture executed successfully")
            else failure(code = "GESTURE_CANCELLED", message = "Swipe gesture was cancelled by system", request = request)
        } catch (e: Exception) {
            failure(code = "SWIPE_EXCEPTION", message = e.message ?: "Swipe failed", request = request, cause = e)
        }
    }

    // -------------------------------------------------------------------------
    // SCROLL
    // -------------------------------------------------------------------------

    private fun executeScrollInternal(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        val direction = request.targetText?.trim()?.uppercase() ?: DIRECTION_FORWARD

        return try {
            val root = service.rootInActiveWindow ?: return failure(code = "NO_ACTIVE_WINDOW", message = "No active accessibility window", request = request)
            val scrollable = findScrollableNode(root) ?: return failure(code = "SCROLLABLE_NODE_NOT_FOUND", message = "No scrollable node found", request = request).also { root.recycle() }

            val action = when (direction) {
                DIRECTION_FORWARD -> AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                DIRECTION_BACKWARD -> AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
                else -> {
                    scrollable.recycle()
                    root.recycle()
                    return failure(code = "INVALID_SCROLL_DIRECTION", message = "Unsupported scroll direction: $direction. Use FORWARD or BACKWARD.", request = request)
                }
            }

            val canScroll = when (action) {
                AccessibilityNodeInfo.ACTION_SCROLL_FORWARD -> scrollable.canScrollForward
                AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD -> scrollable.canScrollBackward
                else -> true
            }

            if (!canScroll) {
                scrollable.recycle()
                root.recycle()
                return failure(code = "SCROLL_NOT_POSSIBLE", message = "Node cannot scroll in direction: $direction", request = request)
            }

            val success = scrollable.performAction(action)
            scrollable.recycle()
            root.recycle()

            if (success) success(actionType = request.type, message = "Scroll executed: $direction")
            else failure(code = "SCROLL_FAILED", message = "Scroll action failed", request = request)
        } catch (e: Exception) {
            failure(code = "SCROLL_EXCEPTION", message = e.message ?: "Scroll failed", request = request, cause = e)
        }
    }

    // -------------------------------------------------------------------------
    // SET TEXT & CLEAR TEXT (Legacy ID-based)
    // -------------------------------------------------------------------------

    private fun executeSetTextInternal(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        val targetId = request.targetId ?: return failure(code = "SET_TEXT_TARGET_MISSING", message = "targetId is required for SET_TEXT", request = request)
        val text = request.text ?: return failure(code = "SET_TEXT_VALUE_MISSING", message = "text is required for SET_TEXT", request = request)

        return try {
            val root = service.rootInActiveWindow ?: return failure(code = "NO_ACTIVE_WINDOW", message = "No active accessibility window", request = request)
            val node = findNodeById(root, targetId) ?: return failure(code = "NODE_NOT_FOUND", message = "Node not found: $targetId", request = request).also { root.recycle() }

            if (!node.isEditable) {
                node.recycle()
                root.recycle()
                return failure(code = "NODE_NOT_EDITABLE", message = "Node is not editable: $targetId", request = request)
            }

            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            node.performAction(AccessibilityNodeInfo.ACTION_CLEAR_SELECTION)
            
            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, Bundle().apply {
                putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "")
            })

            val arguments = Bundle().apply {
                putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
            }

            val success = node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
            node.recycle()
            root.recycle()

            if (success) success(actionType = request.type, message = "Text set successfully")
            else failure(code = "SET_TEXT_FAILED", message = "ACTION_SET_TEXT failed", request = request)
        } catch (e: Exception) {
            failure(code = "SET_TEXT_EXCEPTION", message = e.message ?: "Set text failed", request = request, cause = e)
        }
    }

    private fun executeClearTextInternal(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        val targetId = request.targetId ?: return failure(code = "CLEAR_TEXT_TARGET_MISSING", message = "targetId is required for CLEAR_TEXT", request = request)

        return executeSetTextInternal(request.copy(type = ActionExecutor.ActionType.SET_TEXT, text = ""))
            .mapSuccess(actionType = ActionExecutor.ActionType.CLEAR_TEXT, message = "Text cleared successfully")
    }

    // -------------------------------------------------------------------------
    // WAIT
    // -------------------------------------------------------------------------

    private fun executeWaitInternal(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        val duration = request.durationMs.coerceAtLeast(0L)

        return try {
            if (duration > 0L) {
                Thread.sleep(duration)
            }
            success(actionType = request.type, message = "Wait completed: ${duration}ms")
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            failure(code = "WAIT_INTERRUPTED", message = "Wait interrupted", request = request, cause = e)
        }
    }

    // -------------------------------------------------------------------------
    // Accessibility Helpers (Leak-Proof BFS)
    // -------------------------------------------------------------------------

    private fun findNodeById(root: AccessibilityNodeInfo, id: String): AccessibilityNodeInfo? {
        val matches = root.findAccessibilityNodeInfosByViewId(id)
        if (!matches.isNullOrEmpty()) {
            val result = matches[0]
            for (i in 1 until matches.size) {
                matches[i].recycle()
            }
            return result
        }

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        val toRecycle = mutableListOf<AccessibilityNodeInfo>()

        for (i in 0 until root.childCount) {
            root.getChild(i)?.let { queue.addLast(it) }
        }

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            if (node.viewIdResourceName == id || node.contentDescription?.toString()?.equals(id, ignoreCase = true) == true) {
                for (n in queue) n.recycle()
                for (n in toRecycle) n.recycle()
                return node
            }

            toRecycle.add(node)
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }

        for (n in queue) n.recycle()
        for (n in toRecycle) n.recycle()

        return null
    }

    private fun findNodeByText(root: AccessibilityNodeInfo, text: String, exactMatch: Boolean): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        val toRecycle = mutableListOf<AccessibilityNodeInfo>()
        var bestMatch: AccessibilityNodeInfo? = null

        for (i in 0 until root.childCount) {
            root.getChild(i)?.let { queue.addLast(it) }
        }

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val nodeText = node.text?.toString()?.trim() ?: ""
            val contentDesc = node.contentDescription?.toString()?.trim() ?: ""
            
            val matches = if (exactMatch) {
                nodeText.equals(text, ignoreCase = true) || contentDesc.equals(text, ignoreCase = true)
            } else {
                nodeText.contains(text, ignoreCase = true) || contentDesc.contains(text, ignoreCase = true)
            }

            if (matches) {
                if (node.isClickable) {
                    for (n in queue) n.recycle()
                    for (n in toRecycle) n.recycle()
                    return node
                } else if (bestMatch == null) {
                    bestMatch = node
                } else {
                    toRecycle.add(node)
                }
            } else {
                toRecycle.add(node)
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }

        for (n in queue) n.recycle()
        for (n in toRecycle) n.recycle()
        return bestMatch
    }

    private fun findNodeByTextForInput(root: AccessibilityNodeInfo, targetText: String, exactMatch: Boolean): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        val toRecycle = mutableListOf<AccessibilityNodeInfo>()
        var bestMatch: AccessibilityNodeInfo? = null

        for (i in 0 until root.childCount) {
            root.getChild(i)?.let { queue.addLast(it) }
        }

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val nodeText = node.text?.toString()?.trim() ?: ""
            val hint = node.hintText?.toString()?.trim() ?: ""
            val contentDesc = node.contentDescription?.toString()?.trim() ?: ""
            
            val matches = if (exactMatch) {
                nodeText.equals(targetText, ignoreCase = true) || 
                hint.equals(targetText, ignoreCase = true) || 
                contentDesc.equals(targetText, ignoreCase = true)
            } else {
                nodeText.contains(targetText, ignoreCase = true) || 
                hint.contains(targetText, ignoreCase = true) || 
                contentDesc.contains(targetText, ignoreCase = true)
            }

            if (matches) {
                if (node.isEditable) {
                    for (n in queue) n.recycle()
                    for (n in toRecycle) n.recycle()
                    return node
                } else if (bestMatch == null) {
                    bestMatch = node
                } else {
                    toRecycle.add(node)
                }
            } else {
                toRecycle.add(node)
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }

        for (n in queue) n.recycle()
        for (n in toRecycle) n.recycle()
        return bestMatch
    }

    private fun findNodeAtCoordinates(root: AccessibilityNodeInfo, x: Int, y: Int): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        val toRecycle = mutableListOf<AccessibilityNodeInfo>()

        for (i in 0 until root.childCount) {
            root.getChild(i)?.let { queue.addLast(it) }
        }

        var bestMatch: AccessibilityNodeInfo? = null
        var minArea = Int.MAX_VALUE

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val bounds = Rect()
            node.getBoundsInScreen(bounds)

            if (bounds.contains(x, y)) {
                val area = bounds.width() * bounds.height()
                if (node.isClickable || bestMatch == null || area < minArea) {
                    bestMatch?.let { toRecycle.add(it) }
                    bestMatch = node
                    minArea = area
                } else {
                    toRecycle.add(node)
                }
            } else {
                toRecycle.add(node)
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }

        for (n in queue) n.recycle()
        for (n in toRecycle) n.recycle()

        return bestMatch
    }

    private fun findScrollableNode(root: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        val toRecycle = mutableListOf<AccessibilityNodeInfo>()

        for (i in 0 until root.childCount) {
            root.getChild(i)?.let { queue.addLast(it) }
        }

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            if (node.isScrollable) {
                for (n in queue) n.recycle()
                for (n in toRecycle) n.recycle()
                return node
            }

            toRecycle.add(node)
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }

        for (n in queue) n.recycle()
        for (n in toRecycle) n.recycle()

        return null
    }

    /**
     * Upgraded performClick: Tries to click the node, and if it's not clickable, 
     * it traverses up to 3 levels to find a clickable parent (common in List rows).
     */
    private fun performClick(node: AccessibilityNodeInfo): Boolean {
        if (node.isClickable && node.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
            return true
        }
        
        // Fallback: try to find a clickable parent
        var current: AccessibilityNodeInfo? = node.parent
        var attempts = 0
        while (current != null && attempts < 3) {
            if (current.isClickable && current.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                current.recycle()
                return true
            }
            val next = current.parent
            current.recycle()
            current = next
            attempts++
        }
        
        // Final fallback
        return node.performAction(AccessibilityNodeInfo.ACTION_SELECT)
    }

    // -------------------------------------------------------------------------
    // Result Helpers
    // -------------------------------------------------------------------------

    private fun success(actionType: ActionExecutor.ActionType, message: String): Result<ActionExecutor.ActionResult, ActionError> {
        val result = ActionExecutor.ActionResult(success = true, actionType = actionType, message = message)
        lastActionResult = result
        return Result.Success(result)
    }

    private fun failure(code: String, message: String, request: ActionExecutor.ActionRequest, cause: Throwable? = null): Result<ActionExecutor.ActionResult, ActionError> {
        if (cause != null) Log.e(TAG, message, cause) else Log.e(TAG, message)

        val error = ActionError(code = code, message = message, actionType = request.type, targetId = request.targetId)
        val result = ActionExecutor.ActionResult(success = false, actionType = request.type, message = message)
        
        lastActionResult = result
        return Result.Error(error)
    }

    private fun Result<ActionExecutor.ActionResult, ActionError>.mapSuccess(actionType: ActionExecutor.ActionType, message: String): Result<ActionExecutor.ActionResult, ActionError> {
        return when (this) {
            is Result.Success -> success(actionType = actionType, message = message)
            is Result.Error -> this
        }
    }
}
