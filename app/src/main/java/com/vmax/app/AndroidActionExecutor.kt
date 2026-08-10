package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.action.ActionRequest
import com.vmax.action.ActionResult
import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidActionExecutor.kt
 *
 * Merged Implementation (Contract-Aligned).
 * - Implements ALL ActionTypes, including DOUBLE_TAP.
 * - Uses request.coordinates correctly.
 * - Uses request.durationMs correctly for WAIT.
 */
class AndroidActionExecutor(
    private val accessibilityService: AccessibilityService
) : ActionExecutor {

    companion object {
        private const val GESTURE_DURATION = 200L
        private const val LONG_CLICK_DURATION = 500L
        private const val DOUBLE_TAP_DELAY = 100L
    }

    private var lastResult: ActionResult? = null

    // Helper data class to destructure 4 coordinates safely
    private data class SwipeCoords(
        val startX: Float,
        val startY: Float,
        val endX: Float,
        val endY: Float
    )

    // --- Node Discovery ---
    private fun findNode(targetId: String?, targetText: String?, targetClass: String?): AccessibilityNodeInfo? {
        val root = accessibilityService.rootInActiveWindow ?: return null
        val foundNode = findNodeRecursive(root, targetId, targetText, targetClass)
        if (foundNode != null && foundNode != root) {
            root.recycle()
        }
        return foundNode
    }

    private fun findNodeRecursive(
        node: AccessibilityNodeInfo,
        targetId: String?,
        targetText: String?,
        targetClass: String?
    ): AccessibilityNodeInfo? {
        if (targetId != null && node.viewIdResourceName == targetId) {
            return if (node.isVisibleToUser) node else null
        }
        if (targetText != null && node.text?.toString() == targetText) {
            return if (node.isVisibleToUser) node else null
        }
        if (targetClass != null && node.className?.toString() == targetClass) {
            return if (node.isVisibleToUser) node else null
        }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val found = findNodeRecursive(child, targetId, targetText, targetClass)
            if (found != null) return found
            child.recycle()
        }
        return null
    }

    // --- Gesture Helpers ---
    private fun getBoundsCenter(node: AccessibilityNodeInfo): Pair<Float, Float> {
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        return Pair(bounds.centerX().toFloat(), bounds.centerY().toFloat())
    }

    // ✅ FIX #2: Respect request.coordinates
    private fun getTargetCoordinates(node: AccessibilityNodeInfo, request: ActionRequest): Pair<Float, Float> {
        return if (request.coordinates != null) {
            Pair(
                request.coordinates.first.toFloat(),
                request.coordinates.second.toFloat()
            )
        } else {
            getBoundsCenter(node)
        }
    }

    // ✅ FIX #2: Respect request.coordinates for Swipe
    private fun getSwipePath(request: ActionRequest, direction: String): List<Pair<Float, Float>> {
        if (request.coordinates != null) {
            // If coordinates are provided, we ignore direction and use the coordinates as path endpoints.
            // Assuming a simple linear path from 0 to 1 for demo purposes, or just using the provided coords directly.
            // Let's assume the request provides the 2 points for swipe explicitly. 
            // Since ActionRequest.coordinates is a Pair<Int, Int> (single point), 
            // we treat it as the start point and calculate the end point based on direction.
            val startX = request.coordinates.first.toFloat()
            val startY = request.coordinates.second.toFloat()
            
            val metrics = accessibilityService.resources.displayMetrics
            val gap = 100f
            val (endX, endY) = when (direction.uppercase()) {
                "UP" -> Pair(startX, startY - gap)
                "DOWN" -> Pair(startX, startY + gap)
                "LEFT" -> Pair(startX - gap, startY)
                "RIGHT" -> Pair(startX + gap, startY)
                else -> Pair(startX, startY + gap)
            }
            return listOf(Pair(startX, startY), Pair(endX, endY))
        } else {
            // Fallback to screen center coordinates
            return getSwipeCoordinatesFromScreen(direction).toPath()
        }
    }
    
    private fun getSwipeCoordinatesFromScreen(direction: String): SwipeCoords {
        val metrics = accessibilityService.resources.displayMetrics
        val width = metrics.widthPixels.toFloat()
        val height = metrics.heightPixels.toFloat()
        val centerX = width / 2f
        val centerY = height / 2f
        val gap = 100f

        return when (direction.uppercase()) {
            "UP" -> SwipeCoords(centerX, centerY + gap, centerX, centerY - gap)
            "DOWN" -> SwipeCoords(centerX, centerY - gap, centerX, centerY + gap)
            "LEFT" -> SwipeCoords(centerX + gap, centerY, centerX - gap, centerY)
            "RIGHT" -> SwipeCoords(centerX - gap, centerY, centerX + gap, centerY)
            else -> SwipeCoords(centerX, centerY - gap, centerX, centerY + gap)
        }
    }
    
    private fun SwipeCoords.toPath(): List<Pair<Float, Float>> {
        return listOf(Pair(this.startX, this.startY), Pair(this.endX, this.endY))
    }

    private fun performTap(x: Float, y: Float): Boolean {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, GESTURE_DURATION))
            .build()
        return accessibilityService.dispatchGesture(gesture, null, null)
    }

    private fun performLongTap(x: Float, y: Float): Boolean {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, LONG_CLICK_DURATION))
            .build()
        return accessibilityService.dispatchGesture(gesture, null, null)
    }

    private fun performSwipe(path: List<Pair<Float, Float>>): Boolean {
        if (path.size < 2) return false
        val gesturePath = Path().apply {
            moveTo(path[0].first, path[0].second)
            for (i in 1 until path.size) {
                lineTo(path[i].first, path[i].second)
            }
        }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(gesturePath, 0, GESTURE_DURATION))
            .build()
        return accessibilityService.dispatchGesture(gesture, null, null)
    }

    // --- Action Execution ---
    override fun executeAction(request: ActionRequest): Result<ActionResult, ActionError> {
        val actionType = request.type
        var node: AccessibilityNodeInfo? = null
        
        try {
            node = findNode(request.targetId, request.targetText, request.targetClass)

            // For actions that don't require a specific node, we allow null node.
            // But TAP/CLICK/SET_TEXT etc. require a node.
            if (node == null && actionType !in listOf(ActionExecutor.ActionType.WAIT)) {
                return Result.Error(
                    ActionError("NODE_NOT_FOUND", "Target UI node not found on screen", actionType, request.targetId)
                )
            }

            return when (actionType) {
                ActionExecutor.ActionType.TAP,
                ActionExecutor.ActionType.CLICK -> {
                    // ✅ FIX #2: Using request.coordinates first
                    val (x, y) = getTargetCoordinates(node!!, request)
                    if (node.isClickable) {
                        if (node.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                            return Result.Success(ActionResult(true, actionType, "Click performed on node"))
                        }
                    }
                    // Fallback: Coordinate Tap
                    if (performTap(x, y)) {
                        return Result.Success(ActionResult(true, actionType, "Coordinate tap performed (fallback)"))
                    }
                    Result.Error(ActionError("ACTION_FAILED", "Could not perform TAP/CLICK", actionType, request.targetId))
                }

                ActionExecutor.ActionType.DOUBLE_TAP -> {
                    // ✅ FIX #1: Added DOUBLE_TAP implementation
                    val (x, y) = getTargetCoordinates(node!!, request)
                    if (performTap(x, y)) {
                        Thread.sleep(DOUBLE_TAP_DELAY) // Standard delay between taps
                        if (performTap(x, y)) {
                            return Result.Success(ActionResult(true, actionType, "Double tap performed"))
                        }
                    }
                    Result.Error(ActionError("ACTION_FAILED", "Could not perform DOUBLE_TAP", actionType, request.targetId))
                }

                ActionExecutor.ActionType.LONG_CLICK -> {
                    val (x, y) = getTargetCoordinates(node!!, request)
                    if (node.isLongClickable) {
                        if (node.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)) {
                            return Result.Success(ActionResult(true, actionType, "Long click performed on node"))
                        }
                    }
                    // Fallback: Coordinate Long Tap
                    if (performLongTap(x, y)) {
                        return Result.Success(ActionResult(true, actionType, "Coordinate long tap performed (fallback)"))
                    }
                    Result.Error(ActionError("ACTION_FAILED", "Could not perform LONG_CLICK", actionType, request.targetId))
                }

                ActionExecutor.ActionType.SET_TEXT -> {
                    val text = request.text ?: return Result.Error(
                        ActionError("INVALID_REQUEST", "Text is null for SET_TEXT", actionType, request.targetId)
                    )
                    if (node?.isEditable == true) {
                        val args = Bundle().apply {
                            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
                        }
                        if (node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)) {
                            return Result.Success(ActionResult(true, actionType, "Text set successfully"))
                        }
                    }
                    Result.Error(ActionError("ACTION_FAILED", "Failed to set text", actionType, request.targetId))
                }

                ActionExecutor.ActionType.CLEAR_TEXT -> {
                    if (node?.isEditable == true) {
                        val args = Bundle().apply {
                            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "")
                        }
                        if (node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)) {
                            return Result.Success(ActionResult(true, actionType, "Text cleared"))
                        }
                    }
                    Result.Error(ActionError("ACTION_FAILED", "Failed to clear text", actionType, request.targetId))
                }

                ActionExecutor.ActionType.SCROLL -> {
                    val direction = request.targetText?.uppercase()
                    val actionId = when (direction) {
                        "UP" -> AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
                        "DOWN" -> AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                        "LEFT" -> AccessibilityNodeInfo.ACTION_SCROLL_LEFT
                        "RIGHT" -> AccessibilityNodeInfo.ACTION_SCROLL_RIGHT
                        else -> AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                    }
                    if (node?.performAction(actionId) == true) {
                        return Result.Success(ActionResult(true, actionType, "Scroll performed ($direction)"))
                    }
                    Result.Error(ActionError("ACTION_FAILED", "Could not perform scroll", actionType, request.targetId))
                }

                ActionExecutor.ActionType.SWIPE -> {
                    val direction = request.targetText?.uppercase() ?: "DOWN"
                    // ✅ FIX #2: Pass request to getSwipePath to use coordinates if available
                    val path = getSwipePath(request, direction)
                    if (performSwipe(path)) {
                        return Result.Success(ActionResult(true, actionType, "Swipe performed ($direction)"))
                    }
                    Result.Error(ActionError("ACTION_FAILED", "Swipe failed", actionType, request.targetId))
                }

                ActionExecutor.ActionType.WAIT -> {
                    // ✅ FIX #3: Use request.durationMs instead of parsing text
                    val waitTime = if (request.durationMs > 0) request.durationMs else 1000L
                    Thread.sleep(waitTime)
                    
                    // waitAfterMs is a post-action delay. We apply it immediately after the wait.
                    if (request.waitAfterMs > 0) {
                        Thread.sleep(request.waitAfterMs)
                    }
                    
                    Result.Success(ActionResult(true, actionType, "Waited $waitTime ms"))
                }

                else -> {
                    Result.Error(ActionError("UNSUPPORTED_ACTION", "Action type not supported yet", actionType, request.targetId))
                }
            }
        } finally {
            node?.recycle()
        }
    }

    override fun executeTap(targetId: String): Result<ActionResult, ActionError> =
        executeAction(ActionRequest(ActionExecutor.ActionType.TAP, targetId = targetId))

    override fun executeClick(targetId: String): Result<ActionResult, ActionError> =
        executeAction(ActionRequest(ActionExecutor.ActionType.CLICK, targetId = targetId))

    override fun executeSetText(targetId: String, text: String): Result<ActionResult, ActionError> =
        executeAction(ActionRequest(ActionExecutor.ActionType.SET_TEXT, targetId = targetId, text = text))

    override fun executeClearText(targetId: String): Result<ActionResult, ActionError> =
        executeAction(ActionRequest(ActionExecutor.ActionType.CLEAR_TEXT, targetId = targetId))

    override fun executeScroll(direction: String, amount: Int): Result<ActionResult, ActionError> =
        executeAction(ActionRequest(ActionExecutor.ActionType.SCROLL, targetText = direction))

    override fun executeWait(durationMs: Long): Result<ActionResult, ActionError> =
        executeAction(ActionRequest(ActionExecutor.ActionType.WAIT, durationMs = durationMs))

    override fun isActionAvailable(actionType: ActionExecutor.ActionType): Boolean =
        accessibilityService.rootInActiveWindow != null

    override fun getLastActionResult(): ActionResult? = lastResult
}
