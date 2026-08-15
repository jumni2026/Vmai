package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

class AndroidActionExecutor(
    private val accessibilityService: AccessibilityService
) : ActionExecutor {

    companion object {
        private const val TAP_DURATION_MS = 200L
        private const val LONG_CLICK_DURATION_MS = 500L
        private const val DOUBLE_TAP_DELAY_MS = 100L
        private const val DEFAULT_WAIT_MS = 1000L
        private const val DEFAULT_SWIPE_DISTANCE = 100f
    }

    private var lastResult: ActionExecutor.ActionResult? = null
    private data class SwipeCoords(val startX: Float, val startY: Float, val endX: Float, val endY: Float)

    // --- Node Discovery ---
    private fun findNode(
        targetId: String?,
        targetText: String?,
        targetClass: String?
    ): AccessibilityNodeInfo? {
        if (targetId == null && targetText == null && targetClass == null) return null
        val root = accessibilityService.rootInActiveWindow ?: return null
        return try {
            findNodeRecursive(root, targetId, targetText, targetClass)
        } finally {
            root.recycle()
        }
    }

    private fun findNodeRecursive(
        node: AccessibilityNodeInfo,
        targetId: String?,
        targetText: String?,
        targetClass: String?
    ): AccessibilityNodeInfo? {
        if (matchesTarget(node, targetId, targetText, targetClass)) {
            return AccessibilityNodeInfo.obtain(node)
        }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            try {
                val found = findNodeRecursive(child, targetId, targetText, targetClass)
                if (found != null) return found
            } finally {
                child.recycle()
            }
        }
        return null
    }

    private fun matchesTarget(
        node: AccessibilityNodeInfo,
        targetId: String?,
        targetText: String?,
        targetClass: String?
    ): Boolean {
        if (!node.isVisibleToUser) return false
        if (targetId != null && node.viewIdResourceName != targetId) return false
        if (targetText != null) {
            val nodeText = node.text?.toString()
            val nodeDesc = node.contentDescription?.toString()
            if (nodeText != targetText && nodeDesc != targetText) return false
        }
        if (targetClass != null && node.className?.toString() != targetClass) return false
        return true
    }

    // --- Coordinate Helpers ---
    private fun getBoundsCenter(node: AccessibilityNodeInfo): Pair<Float, Float> {
        val bounds = Rect().also { node.getBoundsInScreen(it) }
        return Pair(bounds.centerX().toFloat(), bounds.centerY().toFloat())
    }

    private fun getTargetCoordinates(node: AccessibilityNodeInfo, request: ActionExecutor.ActionRequest): Pair<Float, Float> {
        return request.coordinates?.let { Pair(it.first.toFloat(), it.second.toFloat()) }
               ?: getBoundsCenter(node)
    }

    // --- Gesture Helpers ---
    private fun performTap(x: Float, y: Float): Boolean {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0L, TAP_DURATION_MS))
            .build()
        return accessibilityService.dispatchGesture(gesture, null, null)
    }

    private fun performLongTap(x: Float, y: Float): Boolean {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0L, LONG_CLICK_DURATION_MS))
            .build()
        return accessibilityService.dispatchGesture(gesture, null, null)
    }

    private fun performSwipe(pathPoints: List<Pair<Float, Float>>, durationMs: Long): Boolean {
        if (pathPoints.size < 2) return false
        val gesturePath = Path().apply {
            moveTo(pathPoints[0].first, pathPoints[0].second)
            for (i in 1 until pathPoints.size) lineTo(pathPoints[i].first, pathPoints[i].second)
        }
        val safeDuration = durationMs.coerceAtLeast(1L)
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(gesturePath, 0L, safeDuration))
            .build()
        return accessibilityService.dispatchGesture(gesture, null, null)
    }

    // --- Swipe Helpers ---
    private fun getSwipeCoordinatesFromScreen(direction: String): SwipeCoords {
        val metrics = accessibilityService.resources.displayMetrics
        val cX = metrics.widthPixels.toFloat() / 2f
        val cY = metrics.heightPixels.toFloat() / 2f
        val gap = DEFAULT_SWIPE_DISTANCE
        return when (direction.uppercase()) {
            "UP" -> SwipeCoords(cX, cY + gap, cX, cY - gap)
            "DOWN" -> SwipeCoords(cX, cY - gap, cX, cY + gap)
            "LEFT" -> SwipeCoords(cX + gap, cY, cX - gap, cY)
            "RIGHT" -> SwipeCoords(cX - gap, cY, cX + gap, cY)
            else -> SwipeCoords(cX, cY - gap, cX, cY + gap)
        }
    }

    private fun getSwipePath(request: ActionExecutor.ActionRequest, direction: String): List<Pair<Float, Float>> {
        val coords = request.coordinates
        if (coords != null) {
            val startX = coords.first.toFloat()
            val startY = coords.second.toFloat()
            val gap = DEFAULT_SWIPE_DISTANCE
            val end = when (direction.uppercase()) {
                "UP" -> Pair(startX, startY - gap)
                "DOWN" -> Pair(startX, startY + gap)
                "LEFT" -> Pair(startX - gap, startY)
                "RIGHT" -> Pair(startX + gap, startY)
                else -> Pair(startX, startY + gap)
            }
            return listOf(Pair(startX, startY), end)
        }
        val screenCoords = getSwipeCoordinatesFromScreen(direction)
        return listOf(Pair(screenCoords.startX, screenCoords.startY), Pair(screenCoords.endX, screenCoords.endY))
    }

    // --- Result Helpers ---
    private fun success(actionType: ActionExecutor.ActionType, message: String): Result<ActionExecutor.ActionResult, ActionError> {
        val result = ActionExecutor.ActionResult(true, actionType, message)
        lastResult = result
        return Result.Success(result)
    }

    private fun failure(code: String, message: String, actionType: ActionExecutor.ActionType, targetId: String?): Result<ActionExecutor.ActionResult, ActionError> {
        return Result.Error(ActionError(code, message, actionType, targetId))
    }

    // --- Main Execute ---
    override fun executeAction(request: ActionExecutor.ActionRequest): Result<ActionExecutor.ActionResult, ActionError> {
        val actionType = request.type
        var node: AccessibilityNodeInfo? = null

        try {
            val searchParams = when (actionType) {
                ActionExecutor.ActionType.SCROLL -> Triple(request.targetId, null, request.targetClass)
                ActionExecutor.ActionType.SWIPE, ActionExecutor.ActionType.WAIT -> Triple(null, null, null)
                else -> Triple(request.targetId, request.targetText, request.targetClass)
            }

            node = findNode(searchParams.first, searchParams.second, searchParams.third)

            // SCROLL Fix: Find scrollable node
            if (actionType == ActionExecutor.ActionType.SCROLL && node == null) {
                node = findFirstScrollableNodeSafe()
            }

            val requiresNode = when (actionType) {
                ActionExecutor.ActionType.SWIPE, ActionExecutor.ActionType.WAIT -> false
                else -> true
            }

            // ✅ FIX: Smart Cast (store local variable first)
            val requestCoordinates = request.coordinates

            if (requiresNode && node == null) {
                if (requestCoordinates != null && (actionType == ActionExecutor.ActionType.TAP || actionType == ActionExecutor.ActionType.CLICK)) {
                    val coords = Pair(requestCoordinates.first.toFloat(), requestCoordinates.second.toFloat())
                    if (performTap(coords.first, coords.second)) {
                        return success(actionType, "Coordinate click performed (fallback)")
                    } else {
                        return failure("ACTION_FAILED", "Coordinate click failed", actionType, request.targetId)
                    }
                } else {
                    return failure("NODE_NOT_FOUND", "Target UI node not found", actionType, request.targetId)
                }
            }

            val result = when (actionType) {
                ActionExecutor.ActionType.TAP -> {
                    val targetNode = node!!
                    val coords = getTargetCoordinates(targetNode, request)
                    if (performTap(coords.first, coords.second)) success(actionType, "Tap performed")
                    else failure("ACTION_FAILED", "Tap failed", actionType, request.targetId)
                }
                ActionExecutor.ActionType.CLICK -> {
                    val targetNode = node!!
                    val coords = getTargetCoordinates(targetNode, request)
                    if (request.coordinates == null && targetNode.isClickable && targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                        success(actionType, "Native click performed")
                    } else if (performTap(coords.first, coords.second)) {
                        success(actionType, "Coordinate click performed")
                    } else {
                        failure("ACTION_FAILED", "Click failed", actionType, request.targetId)
                    }
                }
                ActionExecutor.ActionType.DOUBLE_TAP -> {
                    val targetNode = node!!
                    val coords = getTargetCoordinates(targetNode, request)
                    if (!performTap(coords.first, coords.second)) {
                        failure("ACTION_FAILED", "First tap failed", actionType, request.targetId)
                    } else {
                        Thread.sleep(DOUBLE_TAP_DELAY_MS)
                        if (performTap(coords.first, coords.second)) success(actionType, "Double tap performed")
                        else failure("ACTION_FAILED", "Second tap failed", actionType, request.targetId)
                    }
                }
                ActionExecutor.ActionType.LONG_CLICK -> {
                    val targetNode = node!!
                    val coords = getTargetCoordinates(targetNode, request)
                    if (request.coordinates == null && targetNode.isLongClickable && targetNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)) {
                        success(actionType, "Native long-click performed")
                    } else if (performLongTap(coords.first, coords.second)) {
                        success(actionType, "Coordinate long-click performed")
                    } else {
                        failure("ACTION_FAILED", "Long-click failed", actionType, request.targetId)
                    }
                }
                ActionExecutor.ActionType.SET_TEXT -> {
                    val text = request.text
                    if (text == null) failure("INVALID_REQUEST", "Text is null", actionType, request.targetId)
                    else if (node?.isEditable != true) failure("TARGET_NOT_EDITABLE", "Node not editable", actionType, request.targetId)
                    else {
                        val args = Bundle().apply { putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text) }
                        if (node!!.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)) success(actionType, "Text set")
                        else failure("ACTION_FAILED", "SetText failed", actionType, request.targetId)
                    }
                }
                ActionExecutor.ActionType.CLEAR_TEXT -> {
                    if (node?.isEditable != true) failure("TARGET_NOT_EDITABLE", "Node not editable", actionType, request.targetId)
                    else {
                        val args = Bundle().apply { putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "") }
                        if (node!!.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)) success(actionType, "Text cleared")
                        else failure("ACTION_FAILED", "ClearText failed", actionType, request.targetId)
                    }
                }
                ActionExecutor.ActionType.SCROLL -> {
                    val direction = request.targetText?.trim()?.uppercase() ?: "DOWN"
                    val targetNode = node ?: return failure("NODE_NOT_FOUND", "Node needed for scroll", actionType, request.targetId)
                    if (direction == "UP" || direction == "DOWN") {
                        val actionId = if (direction == "UP") AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD else AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                        if (targetNode.performAction(actionId)) success(actionType, "Scroll $direction")
                        else failure("ACTION_FAILED", "Scroll failed", actionType, request.targetId)
                    } else if (direction == "LEFT" || direction == "RIGHT") {
                        val path = getSwipePath(request, direction)
                        val duration = if (request.durationMs > 0L) request.durationMs else TAP_DURATION_MS
                        if (performSwipe(path, duration)) success(actionType, "Swipe $direction")
                        else failure("ACTION_FAILED", "Swipe failed", actionType, request.targetId)
                    } else {
                        failure("INVALID_DIRECTION", "Invalid direction", actionType, request.targetId)
                    }
                }
                ActionExecutor.ActionType.SWIPE -> {
                    val direction = request.targetText?.trim()?.uppercase() ?: "DOWN"
                    val path = getSwipePath(request, direction)
                    val duration = if (request.durationMs > 0L) request.durationMs else TAP_DURATION_MS
                    if (performSwipe(path, duration)) success(actionType, "Swipe $direction")
                    else failure("ACTION_FAILED", "Swipe failed", actionType, request.targetId)
                }
                ActionExecutor.ActionType.WAIT -> {
                    val duration = if (request.durationMs > 0L) request.durationMs else DEFAULT_WAIT_MS
                    Thread.sleep(duration)
                    success(actionType, "Waited $duration ms")
                }
            }

            if (result is Result.Success && request.waitAfterMs > 0L) Thread.sleep(request.waitAfterMs)
            return result

        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            return failure("INTERRUPTED", "Interrupted", actionType, request.targetId)
        } catch (e: Exception) {
            return failure("EXCEPTION", e.message ?: "Error", actionType, request.targetId)
        } finally {
            node?.recycle()
        }
    }

    private fun findFirstScrollableNodeSafe(): AccessibilityNodeInfo? {
        val root = accessibilityService.rootInActiveWindow ?: return null
        val queue = ArrayDeque<AccessibilityNodeInfo>().apply { add(root) }
        var result: AccessibilityNodeInfo? = null

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            if (node.isScrollable && node.isVisibleToUser) {
                result = AccessibilityNodeInfo.obtain(node)
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }

        queue.forEach { if (it !== root) it.recycle() }
        queue.clear()

        return result
    }

    // --- Contract Methods ---
    override fun executeTap(targetId: String) = executeAction(ActionExecutor.ActionRequest(ActionExecutor.ActionType.TAP, targetId = targetId))
    override fun executeClick(targetId: String) = executeAction(ActionExecutor.ActionRequest(ActionExecutor.ActionType.CLICK, targetId = targetId))
    override fun executeSetText(targetId: String, text: String) = executeAction(ActionExecutor.ActionRequest(ActionExecutor.ActionType.SET_TEXT, targetId = targetId, text = text))
    override fun executeClearText(targetId: String) = executeAction(ActionExecutor.ActionRequest(ActionExecutor.ActionType.CLEAR_TEXT, targetId = targetId))
    override fun executeScroll(direction: String, amount: Int) = executeAction(ActionExecutor.ActionRequest(ActionExecutor.ActionType.SCROLL, targetText = direction))
    override fun executeWait(durationMs: Long) = executeAction(ActionExecutor.ActionRequest(ActionExecutor.ActionType.WAIT, durationMs = durationMs))
    override fun isActionAvailable(actionType: ActionExecutor.ActionType): Boolean = true
    override fun getLastActionResult(): ActionExecutor.ActionResult? = lastResult
}
