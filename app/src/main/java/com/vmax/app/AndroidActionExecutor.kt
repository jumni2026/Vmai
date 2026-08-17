package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6.1
 *
 * Android implementation of ActionExecutor.
 *
 * Responsibility:
 * - Translate platform-independent ActionRequest objects
 *   into Android Accessibility actions.
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
    }

    @Volatile
    private var lastActionResult: ActionExecutor.ActionResult? = null

    override fun executeAction(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        Log.d(
            TAG,
            "Executing action=${request.type}, targetId=${request.targetId}"
        )

        return try {
            when (request.type) {

                ActionExecutor.ActionType.TAP ->
                    executeTapInternal(request)

                ActionExecutor.ActionType.CLICK ->
                    executeClickInternal(request)

                ActionExecutor.ActionType.LONG_CLICK ->
                    executeLongClick(request)

                ActionExecutor.ActionType.DOUBLE_TAP ->
                    executeDoubleTap(request)

                ActionExecutor.ActionType.SWIPE ->
                    executeSwipe(request)

                ActionExecutor.ActionType.SCROLL ->
                    executeScrollInternal(request)

                ActionExecutor.ActionType.SET_TEXT ->
                    executeSetTextInternal(request)

                ActionExecutor.ActionType.CLEAR_TEXT ->
                    executeClearTextInternal(request)

                ActionExecutor.ActionType.WAIT ->
                    executeWaitInternal(request)
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

    override fun executeTap(
        targetId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.TAP,
                targetId = targetId
            )
        )
    }

    override fun executeClick(
        targetId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.CLICK,
                targetId = targetId
            )
        )
    }

    override fun executeSetText(
        targetId: String,
        text: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SET_TEXT,
                targetId = targetId,
                text = text
            )
        )
    }

    override fun executeClearText(
        targetId: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.CLEAR_TEXT,
                targetId = targetId
            )
        )
    }

    override fun executeScroll(
        direction: String,
        amount: Int
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SCROLL,
                targetText = direction,
                durationMs = amount.toLong()
            )
        )
    }

    override fun executeWait(
        durationMs: Long
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.WAIT,
                durationMs = durationMs
            )
        )
    }

    override fun isActionAvailable(
        actionType: ActionExecutor.ActionType
    ): Boolean {
        return when (actionType) {

            ActionExecutor.ActionType.TAP,
            ActionExecutor.ActionType.CLICK,
            ActionExecutor.ActionType.LONG_CLICK,
            ActionExecutor.ActionType.DOUBLE_TAP,
            ActionExecutor.ActionType.SCROLL,
            ActionExecutor.ActionType.SET_TEXT,
            ActionExecutor.ActionType.CLEAR_TEXT,
            ActionExecutor.ActionType.WAIT -> true

            ActionExecutor.ActionType.SWIPE ->
                android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.N
        }
    }

    override fun getLastActionResult():
        ActionExecutor.ActionResult? {
        return lastActionResult
    }

    // -------------------------------------------------------------------------
    // TAP / CLICK
    // -------------------------------------------------------------------------

    private fun executeTapInternal(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return executeClickInternal(request)
    }

    private fun executeClickInternal(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val targetId = request.targetId
        val coordinates = request.coordinates

        if (targetId != null) {
            return clickById(targetId, request)
        }

        if (coordinates != null) {
            return clickByCoordinates(
                x = coordinates.first,
                y = coordinates.second,
                request = request
            )
        }

        return failure(
            code = "CLICK_TARGET_MISSING",
            message = "Either targetId or coordinates are required for CLICK",
            request = request
        )
    }

    private fun clickById(
        targetId: String,
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        var retries = 0
        var lastError: Throwable? = null

        while (retries < MAX_RETRIES) {

            try {
                val root = service.rootInActiveWindow

                if (root == null) {
                    lastError =
                        IllegalStateException(
                            "No active accessibility window"
                        )

                    retries++
                    Thread.sleep(CLICK_RETRY_DELAY_MS)
                    continue
                }

                val node = findNodeById(
                    root = root,
                    id = targetId
                )

                if (node == null) {
                    root.recycle()

                    lastError =
                        IllegalStateException(
                            "Node not found: $targetId"
                        )

                    retries++
                    Thread.sleep(CLICK_RETRY_DELAY_MS)
                    continue
                }

                val success = performClick(node)

                node.recycle()
                root.recycle()

                if (success) {
                    return success(
                        actionType = request.type,
                        message = "Action executed successfully"
                    )
                }

                lastError =
                    IllegalStateException(
                        "Accessibility click failed"
                    )

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
            message = lastError?.message
                ?: "Click failed after $MAX_RETRIES retries",
            request = request,
            cause = lastError
        )
    }

    private fun clickByCoordinates(
        x: Int,
        y: Int,
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        return try {

            val root = service.rootInActiveWindow
                ?: return failure(
                    code = "NO_ACTIVE_WINDOW",
                    message = "No active accessibility window",
                    request = request
                )

            val node = findNodeAtCoordinates(
                root = root,
                x = x,
                y = y
            )

            if (node == null) {
                root.recycle()

                return failure(
                    code = "NODE_NOT_FOUND",
                    message = "No clickable node at ($x,$y)",
                    request = request
                )
            }

            val success = performClick(node)

            node.recycle()
            root.recycle()

            if (success) {
                success(
                    actionType = request.type,
                    message = "Coordinate click executed"
                )
            } else {
                failure(
                    code = "CLICK_FAILED",
                    message = "Coordinate click failed",
                    request = request
                )
            }

        } catch (e: Exception) {
            failure(
                code = "CLICK_EXCEPTION",
                message = e.message ?: "Coordinate click failed",
                request = request,
                cause = e
            )
        }
    }

    // -------------------------------------------------------------------------
    // LONG CLICK
    // -------------------------------------------------------------------------

    private fun executeLongClick(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val targetId = request.targetId

        if (targetId == null) {
            return failure(
                code = "LONG_CLICK_TARGET_MISSING",
                message = "targetId is required for LONG_CLICK",
                request = request
            )
        }

        return try {

            val root = service.rootInActiveWindow
                ?: return failure(
                    code = "NO_ACTIVE_WINDOW",
                    message = "No active accessibility window",
                    request = request
                )

            val node = findNodeById(
                root = root,
                id = targetId
            )

            if (node == null) {
                root.recycle()

                return failure(
                    code = "NODE_NOT_FOUND",
                    message = "Node not found: $targetId",
                    request = request
                )
            }

            val success =
                node.performAction(
                    AccessibilityNodeInfo.ACTION_LONG_CLICK
                )

            node.recycle()
            root.recycle()

            if (success) {
                success(
                    actionType = request.type,
                    message = "Long click executed"
                )
            } else {
                failure(
                    code = "LONG_CLICK_FAILED",
                    message = "Long click failed",
                    request = request
                )
            }

        } catch (e: Exception) {
            failure(
                code = "LONG_CLICK_EXCEPTION",
                message = e.message ?: "Long click failed",
                request = request,
                cause = e
            )
        }
    }

    // -------------------------------------------------------------------------
    // DOUBLE TAP
    // -------------------------------------------------------------------------

    private fun executeDoubleTap(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val first = executeClickInternal(
            request.copy(
                type = ActionExecutor.ActionType.CLICK
            )
        )

        if (first is Result.Error) {
            return first
        }

        Thread.sleep(100L)

        return executeClickInternal(
            request.copy(
                type = ActionExecutor.ActionType.CLICK
            )
        ).mapSuccess(
            actionType = request.type,
            message = "Double tap executed"
        )
    }

    // -------------------------------------------------------------------------
    // SWIPE
    // -------------------------------------------------------------------------

    private fun executeSwipe(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        /*
         * AccessibilityService gesture dispatch is intentionally kept
         * conservative here.
         *
         * The current platform-independent ActionRequest contract
         * carries only a single coordinate pair and does not define
         * start/end coordinates for a swipe.
         *
         * Therefore a SWIPE request cannot be safely executed yet.
         */
        return failure(
            code = "SWIPE_CONTRACT_INCOMPLETE",
            message = "SWIPE requires start/end coordinates in ActionRequest",
            request = request
        )
    }

    // -------------------------------------------------------------------------
    // SCROLL
    // -------------------------------------------------------------------------

    private fun executeScrollInternal(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val direction =
            request.targetText
                ?.trim()
                ?.uppercase()
                ?: DIRECTION_FORWARD

        return try {

            val root = service.rootInActiveWindow
                ?: return failure(
                    code = "NO_ACTIVE_WINDOW",
                    message = "No active accessibility window",
                    request = request
                )

            val scrollable = findScrollableNode(root)

            if (scrollable == null) {
                root.recycle()

                return failure(
                    code = "SCROLLABLE_NODE_NOT_FOUND",
                    message = "No scrollable node found",
                    request = request
                )
            }

            /*
             * Android AccessibilityNodeInfo provides the generic
             * forward/backward scroll actions used here.
             *
             * Direction-specific constants such as:
             * ACTION_SCROLL_UP
             * ACTION_SCROLL_DOWN
             * ACTION_SCROLL_LEFT
             * ACTION_SCROLL_RIGHT
             *
             * are not available in the SDK contract used by this project.
             */
            val action = when (direction) {

                DIRECTION_FORWARD ->
                    AccessibilityNodeInfo.ACTION_SCROLL_FORWARD

                DIRECTION_BACKWARD ->
                    AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD

                else -> {
                    scrollable.recycle()
                    root.recycle()

                    return failure(
                        code = "INVALID_SCROLL_DIRECTION",
                        message =
                            "Unsupported scroll direction: $direction. " +
                                "Use FORWARD or BACKWARD.",
                        request = request
                    )
                }
            }

            val success = scrollable.performAction(action)

            scrollable.recycle()
            root.recycle()

            if (success) {

                success(
                    actionType = request.type,
                    message = "Scroll executed: $direction"
                )

            } else {

                failure(
                    code = "SCROLL_FAILED",
                    message = "Scroll action failed",
                    request = request
                )
            }

        } catch (e: Exception) {

            failure(
                code = "SCROLL_EXCEPTION",
                message = e.message ?: "Scroll failed",
                request = request,
                cause = e
            )
        }
    }

    // -------------------------------------------------------------------------
    // SET TEXT
    // -------------------------------------------------------------------------

    private fun executeSetTextInternal(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val targetId = request.targetId
        val text = request.text

        if (targetId == null) {
            return failure(
                code = "SET_TEXT_TARGET_MISSING",
                message = "targetId is required for SET_TEXT",
                request = request
            )
        }

        if (text == null) {
            return failure(
                code = "SET_TEXT_VALUE_MISSING",
                message = "text is required for SET_TEXT",
                request = request
            )
        }

        return try {

            val root = service.rootInActiveWindow
                ?: return failure(
                    code = "NO_ACTIVE_WINDOW",
                    message = "No active accessibility window",
                    request = request
                )

            val node = findNodeById(
                root = root,
                id = targetId
            )

            if (node == null) {
                root.recycle()

                return failure(
                    code = "NODE_NOT_FOUND",
                    message = "Node not found: $targetId",
                    request = request
                )
            }

            if (!node.isEditable) {

                node.recycle()
                root.recycle()

                return failure(
                    code = "NODE_NOT_EDITABLE",
                    message = "Node is not editable: $targetId",
                    request = request
                )
            }

            node.performAction(
                AccessibilityNodeInfo.ACTION_FOCUS
            )

            val arguments = Bundle().apply {
                putCharSequence(
                    AccessibilityNodeInfo
                        .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    text
                )
            }

            val success = node.performAction(
                AccessibilityNodeInfo.ACTION_SET_TEXT,
                arguments
            )

            node.recycle()
            root.recycle()

            if (success) {

                success(
                    actionType = request.type,
                    message = "Text set successfully"
                )

            } else {

                failure(
                    code = "SET_TEXT_FAILED",
                    message = "ACTION_SET_TEXT failed",
                    request = request
                )
            }

        } catch (e: Exception) {

            failure(
                code = "SET_TEXT_EXCEPTION",
                message = e.message ?: "Set text failed",
                request = request,
                cause = e
            )
        }
    }

    // -------------------------------------------------------------------------
    // CLEAR TEXT
    // -------------------------------------------------------------------------

    private fun executeClearTextInternal(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val targetId = request.targetId

        if (targetId == null) {
            return failure(
                code = "CLEAR_TEXT_TARGET_MISSING",
                message = "targetId is required for CLEAR_TEXT",
                request = request
            )
        }

        return executeSetTextInternal(
            request.copy(
                type = ActionExecutor.ActionType.SET_TEXT,
                text = ""
            )
        ).mapSuccess(
            actionType = ActionExecutor.ActionType.CLEAR_TEXT,
            message = "Text cleared successfully"
        )
    }

    // -------------------------------------------------------------------------
    // WAIT
    // -------------------------------------------------------------------------

    private fun executeWaitInternal(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val duration =
            request.durationMs.coerceAtLeast(0L)

        return try {

            if (duration > 0L) {
                Thread.sleep(duration)
            }

            success(
                actionType = request.type,
                message = "Wait completed: ${duration}ms"
            )

        } catch (e: InterruptedException) {

            Thread.currentThread().interrupt()

            failure(
                code = "WAIT_INTERRUPTED",
                message = "Wait interrupted",
                request = request,
                cause = e
            )
        }
    }

    // -------------------------------------------------------------------------
    // Accessibility helpers
    // -------------------------------------------------------------------------

    private fun findNodeById(
        root: AccessibilityNodeInfo,
        id: String
    ): AccessibilityNodeInfo? {

        val queue = ArrayDeque<AccessibilityNodeInfo>()

        queue.add(
            AccessibilityNodeInfo.obtain(root)
        )

        while (queue.isNotEmpty()) {

            val node = queue.removeFirst()

            if (node.viewIdResourceName == id) {

                while (queue.isNotEmpty()) {
                    queue.removeFirst().recycle()
                }

                return node
            }

            for (index in 0 until node.childCount) {

                val child = node.getChild(index)

                if (child != null) {
                    queue.addLast(child)
                }
            }

            node.recycle()
        }

        return null
    }

    private fun findNodeAtCoordinates(
        root: AccessibilityNodeInfo,
        x: Int,
        y: Int
    ): AccessibilityNodeInfo? {

        val queue = ArrayDeque<AccessibilityNodeInfo>()

        queue.add(
            AccessibilityNodeInfo.obtain(root)
        )

        while (queue.isNotEmpty()) {

            val node = queue.removeFirst()

            val bounds = Rect()

            node.getBoundsInScreen(bounds)

            if (bounds.contains(x, y) && node.isClickable) {

                while (queue.isNotEmpty()) {
                    queue.removeFirst().recycle()
                }

                return node
            }

            for (index in 0 until node.childCount) {

                val child = node.getChild(index)

                if (child != null) {
                    queue.addLast(child)
                }
            }

            node.recycle()
        }

        return null
    }

    private fun findScrollableNode(
        root: AccessibilityNodeInfo
    ): AccessibilityNodeInfo? {

        val queue = ArrayDeque<AccessibilityNodeInfo>()

        queue.add(
            AccessibilityNodeInfo.obtain(root)
        )

        while (queue.isNotEmpty()) {

            val node = queue.removeFirst()

            if (node.isScrollable) {

                while (queue.isNotEmpty()) {
                    queue.removeFirst().recycle()
                }

                return node
            }

            for (index in 0 until node.childCount) {

                val child = node.getChild(index)

                if (child != null) {
                    queue.addLast(child)
                }
            }

            node.recycle()
        }

        return null
    }

    private fun performClick(
        node: AccessibilityNodeInfo
    ): Boolean {

        if (
            node.isClickable &&
            node.performAction(
                AccessibilityNodeInfo.ACTION_CLICK
            )
        ) {
            return true
        }

        return node.performAction(
            AccessibilityNodeInfo.ACTION_SELECT
        )
    }

    // -------------------------------------------------------------------------
    // Result helpers
    // -------------------------------------------------------------------------

    private fun success(
        actionType: ActionExecutor.ActionType,
        message: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        val result = ActionExecutor.ActionResult(
            success = true,
            actionType = actionType,
            message = message
        )

        lastActionResult = result

        return Result.Success(result)
    }

    private fun failure(
        code: String,
        message: String,
        request: ActionExecutor.ActionRequest,
        cause: Throwable? = null
    ): Result<ActionExecutor.ActionResult, ActionError> {

        if (cause != null) {
            Log.e(TAG, message, cause)
        } else {
            Log.e(TAG, message)
        }

        val error = ActionError(
            code = code,
            message = message,
            actionType = request.type,
            targetId = request.targetId
        )

        val result = ActionExecutor.ActionResult(
            success = false,
            actionType = request.type,
            message = message
        )

        lastActionResult = result

        return Result.Error(error)
    }

    private fun Result<ActionExecutor.ActionResult, ActionError>.mapSuccess(
        actionType: ActionExecutor.ActionType,
        message: String
    ): Result<ActionExecutor.ActionResult, ActionError> {

        return when (this) {

            is Result.Success ->
                success(
                    actionType = actionType,
                    message = message
                )

            is Result.Error ->
                this
        }
    }
}
