package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionExecutor
import com.vmax.action.ActionError
import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6.1
 *
 * File — AndroidActionExecutor.kt
 * 
 * Android implementation of the platform-independent
 * com.vmax.action.ActionExecutor contract.
 *
 * Architecture:
 *
 * ActionExecutor (contract)
 *         ↓
 * AndroidActionExecutor (Android implementation)
 *
 * No business logic.
 * No IRCTC-specific logic.
 */
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

    private data class SwipeCoords(
        val startX: Float,
        val startY: Float,
        val endX: Float,
        val endY: Float
    )

    // ----------------------------------------------------------------
    // NODE DISCOVERY
    // ----------------------------------------------------------------
    private fun findNode(
        targetId: String?,
        targetText: String?,
        targetClass: String?
    ): AccessibilityNodeInfo? {
        if (targetId == null && targetText == null && targetClass == null) {
            return null
        }

        val root = accessibilityService.rootInActiveWindow ?: return null

        try {
            return findNodeRecursive(
                node = root,
                targetId = targetId,
                targetText = targetText,
                targetClass = targetClass
            )
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

        for (index in 0 until node.childCount) {
            val child = node.getChild(index) ?: continue
            try {
                val found = findNodeRecursive(
                    node = child,
                    targetId = targetId,
                    targetText = targetText,
                    targetClass = targetClass
                )
                if (found != null) {
                    return found
                }
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

        if (targetId != null && node.viewIdResourceName != targetId) {
            return false
        }

        if (targetText != null) {
            val nodeText = node.text?.toString()
            val nodeDesc = node.contentDescription?.toString()

            val textMatch = nodeText == targetText
            val descMatch = nodeDesc == targetText

            if (!textMatch && !descMatch) {
                return false
            }
        }

        if (targetClass != null && node.className?.toString() != targetClass) {
            return false
        }

        return true
    }

    // ----------------------------------------------------------------
    // COORDINATE HELPERS
    // ----------------------------------------------------------------
    private fun getBoundsCenter(node: AccessibilityNodeInfo): Pair<Float, Float> {
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        return Pair(
            bounds.centerX().toFloat(),
            bounds.centerY().toFloat()
        )
    }

    private fun getTargetCoordinates(
        node: AccessibilityNodeInfo,
        request: ActionExecutor.ActionRequest
    ): Pair<Float, Float> {
        val coordinates = request.coordinates
        return if (coordinates != null) {
            Pair(
                coordinates.first.toFloat(),
                coordinates.second.toFloat()
            )
        } else {
            getBoundsCenter(node)
        }
    }

    // ----------------------------------------------------------------
    // GESTURE HELPERS
    // ----------------------------------------------------------------
    private fun performTap(
        x: Float,
        y: Float
    ): Boolean {
        val path = Path().apply {
            moveTo(x, y)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(
                GestureDescription.StrokeDescription(
                    path,
                    0L,
                    TAP_DURATION_MS
                )
            )
            .build()

        return accessibilityService.dispatchGesture(
            gesture,
            null,
            null
        )
    }

    private fun performLongTap(
        x: Float,
        y: Float
    ): Boolean {
        val path = Path().apply {
            moveTo(x, y)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(
                GestureDescription.StrokeDescription(
                    path,
                    0L,
                    LONG_CLICK_DURATION_MS
                )
            )
            .build()

        return accessibilityService.dispatchGesture(
            gesture,
            null,
            null
        )
    }

    private fun performSwipe(
        pathPoints: List<Pair<Float, Float>>,
        durationMs: Long = TAP_DURATION_MS
    ): Boolean {
        if (pathPoints.size < 2) return false

        val gesturePath = Path().apply {
            moveTo(
                pathPoints[0].first,
                pathPoints[0].second
            )
            for (index in 1 until pathPoints.size) {
                lineTo(
                    pathPoints[index].first,
                    pathPoints[index].second
                )
            }
        }

        val safeDuration = durationMs.coerceAtLeast(1L)

        val gesture = GestureDescription.Builder()
            .addStroke(
                GestureDescription.StrokeDescription(
                    gesturePath,
                    0L,
                    safeDuration
                )
            )
            .build()

        return accessibilityService.dispatchGesture(
            gesture,
            null,
            null
        )
    }

    // ----------------------------------------------------------------
    // SWIPE COORDINATES
    // ----------------------------------------------------------------
    private fun getSwipeCoordinatesFromScreen(
        direction: String
    ): SwipeCoords {
        val metrics = accessibilityService.resources.displayMetrics
        val width = metrics.widthPixels.toFloat()
        val height = metrics.heightPixels.toFloat()

        val centerX = width / 2f
        val centerY = height / 2f
        val gap = DEFAULT_SWIPE_DISTANCE

        return when (direction.uppercase()) {
            "UP" -> SwipeCoords(
                centerX,
                centerY + gap,
                centerX,
                centerY - gap
            )
            "DOWN" -> SwipeCoords(
                centerX,
                centerY - gap,
                centerX,
                centerY + gap
            )
            "LEFT" -> SwipeCoords(
                centerX + gap,
                centerY,
                centerX - gap,
                centerY
            )
            "RIGHT" -> SwipeCoords(
                centerX - gap,
                centerY,
                centerX + gap,
                centerY
            )
            else -> SwipeCoords(
                centerX,
                centerY - gap,
                centerX,
                centerY + gap
            )
        }
    }

    // ----------------------------------------------------------------
    // MAIN ACTION EXECUTION
    // ----------------------------------------------------------------
    override fun executeAction(
        request: ActionExecutor.ActionRequest
    ): Result<ActionExecutor.ActionResult, ActionError> {
        val actionType = request.type

        var node: AccessibilityNodeInfo? = null

        try {
            val (searchId, searchText, searchClass) = when (actionType) {
                // SCROLL uses targetText as direction, NOT as node selector
                ActionExecutor.ActionType.SCROLL ->
                    Triple(request.targetId, null, request.targetClass)
                // SWIPE and WAIT don't require a node
                ActionExecutor.ActionType.SWIPE,
                ActionExecutor.ActionType.WAIT ->
                    Triple(null, null, null)
                else ->
                    Triple(request.targetId, request.targetText, request.targetClass)
            }

            node = findNode(
                targetId = searchId,
                targetText = searchText,
                targetClass = searchClass
            )

            val requiresNode = when (actionType) {
                ActionExecutor.ActionType.SWIPE,
                ActionExecutor.ActionType.WAIT -> false
                else -> true
            }

            if (requiresNode && node == null) {
                return Result.Error(
                    ActionError(
                        code = "NODE_NOT_FOUND",
                        message = "Target UI node not found on screen",
                        actionType = actionType,
                        targetId = request.targetId
                    )
                )
            }

            val actionResult = when (actionType) {
                // ------------------------------------------------------------
                // TAP / CLICK
                // ------------------------------------------------------------
                ActionExecutor.ActionType.TAP,
                ActionExecutor.ActionType.CLICK -> {
                    val targetNode = node!!
                    val coordinates = getTargetCoordinates(targetNode, request)

                    if (request.coordinates != null) {
                        if (performTap(coordinates.first, coordinates.second)) {
                            Result.Success(
                                ActionExecutor.ActionResult(
                                    success = true,
                                    actionType = actionType,
                                    message = "Coordinate tap performed"
                                )
                            )
                        } else {
                            Result.Error(
                                ActionError(
                                    code = "ACTION_FAILED",
                                    message = "Coordinate tap could not be dispatched",
                                    actionType = actionType,
                                    targetId = request.targetId
                                )
                            )
                        }
                    } else if (targetNode.isClickable && targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                        Result.Success(
                            ActionExecutor.ActionResult(
                                success = true,
                                actionType = actionType,
                                message = "Native accessibility click performed"
                            )
                        )
                    } else if (performTap(coordinates.first, coordinates.second)) {
                        Result.Success(
                            ActionExecutor.ActionResult(
                                success = true,
                                actionType = actionType,
                                message = "Coordinate tap performed as fallback"
                            )
                        )
                    } else {
                        Result.Error(
                            ActionError(
                                code = "ACTION_FAILED",
                                message = "Could not perform TAP/CLICK",
                                actionType = actionType,
                                targetId = request.targetId
                            )
                        )
                    }
                }

                // ------------------------------------------------------------
                // DOUBLE TAP
                // ------------------------------------------------------------
                ActionExecutor.ActionType.DOUBLE_TAP -> {
                    val targetNode = node!!
                    val coordinates = getTargetCoordinates(targetNode, request)

                    val firstTap = performTap(coordinates.first, coordinates.second)
                    if (!firstTap) {
                        Result.Error(
                            ActionError(
                                code = "ACTION_FAILED",
                                message = "First tap of DOUBLE_TAP could not be dispatched",
                                actionType = actionType,
                                targetId = request.targetId
                            )
                        )
                    } else {
                        Thread.sleep(DOUBLE_TAP_DELAY_MS)
                        val secondTap = performTap(coordinates.first, coordinates.second)

                        if (secondTap) {
                            Result.Success(
                                ActionExecutor.ActionResult(
                                    success = true,
                                    actionType = actionType,
                                    message = "Double tap dispatched"
                                )
                            )
                        } else {
                            Result.Error(
                                ActionError(
                                    code = "ACTION_FAILED",
                                    message = "Second tap of DOUBLE_TAP could not be dispatched",
                                    actionType = actionType,
                                    targetId = request.targetId
                                )
                            )
                        }
                    }
                }

                // ------------------------------------------------------------
                // LONG CLICK
                // ------------------------------------------------------------
                ActionExecutor.ActionType.LONG_CLICK -> {
                    val targetNode = node!!
                    val coordinates = getTargetCoordinates(targetNode, request)

                    if (request.coordinates == null && targetNode.isLongClickable && targetNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)) {
                        Result.Success(
                            ActionExecutor.ActionResult(
                                success = true,
                                actionType = actionType,
                                message = "Native accessibility long-click performed"
                            )
                        )
                    } else if (performLongTap(coordinates.first, coordinates.second)) {
                        Result.Success(
                            ActionExecutor.ActionResult(
                                success = true,
                                actionType = actionType,
                                message = "Coordinate long tap performed"
                            )
                        )
                    } else {
                        Result.Error(
                            ActionError(
                                code = "ACTION_FAILED",
                                message = "Could not perform LONG_CLICK",
                                actionType = actionType,
                                targetId = request.targetId
                            )
                        )
                    }
                }

                // ------------------------------------------------------------
                // SET TEXT
                // ------------------------------------------------------------
                ActionExecutor.ActionType.SET_TEXT -> {
                    val text = request.text
                    if (text == null) {
                        Result.Error(
                            ActionError(
                                code = "INVALID_REQUEST",
                                message = "Text is null for SET_TEXT",
                                actionType = actionType,
                                targetId = request.targetId
                            )
                        )
                    } else if (node?.isEditable != true) {
                        Result.Error(
                            ActionError(
                                code = "TARGET_NOT_EDITABLE",
                                message = "Target node is not editable",
                                actionType = actionType,
                                targetId = request.targetId
                            )
                        )
                    } else {
                        val arguments = Bundle().apply {
                            putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                text
                            )
                        }
                        if (node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)) {
                            Result.Success(
                                ActionExecutor.ActionResult(
                                    success = true,
                                    actionType = actionType,
                                    message = "Text set successfully"
                                )
                            )
                        } else {
                            Result.Error(
                                ActionError(
                                    code = "ACTION_FAILED",
                                    message = "Failed to set text",
                                    actionType = actionType,
                                    targetId = request.targetId
                                )
                            )
                        }
                    }
                }

                // ------------------------------------------------------------
                // CLEAR TEXT
                // ------------------------------------------------------------
                ActionExecutor.ActionType.CLEAR_TEXT -> {
                    if (node?.isEditable != true) {
                        Result.Error(
                            ActionError(
                                code = "TARGET_NOT_EDITABLE",
                                message = "Target node is not editable",
                                actionType = actionType,
                                targetId = request.targetId
                            )
                        )
                    } else {
                        val arguments = Bundle().apply {
                            putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                ""
                            )
                        }
                        if (node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)) {
                            Result.Success(
                                ActionExecutor.ActionResult(
                                    success = true,
                                    actionType = actionType,
                                    message = "Text cleared successfully"
                                )
                            )
                        } else {
                            Result.Error(
                                ActionError(
                                    code = "ACTION_FAILED",
                                    message = "Failed to clear text",
                                    actionType = actionType,
                                    targetId = request.targetId
                                )
                            )
                        }
                    }
                }

                // ------------------------------------------------------------
                // SCROLL
                // ------------------------------------------------------------
                ActionExecutor.ActionType.SCROLL -> {
                    val direction = request.targetText?.trim()?.uppercase() ?: "DOWN"

                    when (direction) {
                        "UP", "DOWN" -> {
                            if (node == null) {
                                return Result.Error(
                                    ActionError(
                                        code = "NODE_NOT_FOUND",
                                        message = "Target node required for vertical scroll",
                                        actionType = actionType,
                                        targetId = request.targetId
                                    )
                                )
                            }
                            val actionId = if (direction == "UP") {
                                AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
                            } else {
                                AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                            }
                            if (node.performAction(actionId) == true) {
                                Result.Success(
                                    ActionExecutor.ActionResult(
                                        success = true,
                                        actionType = actionType,
                                        message = "Native scroll performed ($direction)"
                                    )
                                )
                            } else {
                                Result.Error(
                                    ActionError(
                                        code = "ACTION_FAILED",
                                        message = "Could not perform vertical scroll",
                                        actionType = actionType,
                                        targetId = request.targetId
                                    )
                                )
                            }
                        }
                        "LEFT", "RIGHT" -> {
                            val path = getSwipePath(request, direction)
                            val duration = if (request.durationMs > 0L) {
                                request.durationMs
                            } else {
                                TAP_DURATION_MS
                            }
                            if (performSwipe(path, duration)) {
                                Result.Success(
                                    ActionExecutor.ActionResult(
                                        success = true,
                                        actionType = actionType,
                                        message = "Horizontal scroll via swipe ($direction)"
                                    )
                                )
                            } else {
                                Result.Error(
                                    ActionError(
                                        code = "ACTION_FAILED",
                                        message = "Could not perform horizontal scroll",
                                        actionType = actionType,
                                        targetId = request.targetId
                                    )
                                )
                            }
                        }
                        else -> {
                            if (node == null) {
                                return Result.Error(
                                    ActionError(
                                        code = "NODE_NOT_FOUND",
                                        message = "Target node required for default scroll",
                                        actionType = actionType,
                                        targetId = request.targetId
                                    )
                                )
                            }
                            if (node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) == true) {
                                Result.Success(
                                    ActionExecutor.ActionResult(
                                        success = true,
                                        actionType = actionType,
                                        message = "Default scroll performed"
                                    )
                                )
                            } else {
                                Result.Error(
                                    ActionError(
                                        code = "ACTION_FAILED",
                                        message = "Could not perform default scroll",
                                        actionType = actionType,
                                        targetId = request.targetId
                                    )
                                )
                            }
                        }
                    }
                }

                // ------------------------------------------------------------
                // SWIPE
                // ------------------------------------------------------------
                ActionExecutor.ActionType.SWIPE -> {
                    val direction = request.targetText?.trim()?.uppercase() ?: "DOWN"
                    val path = getSwipePath(request, direction)
                    val duration = if (request.durationMs > 0L) {
                        request.durationMs
                    } else {
                        TAP_DURATION_MS
                    }
                    if (performSwipe(path, duration)) {
                        Result.Success(
                            ActionExecutor.ActionResult(
                                success = true,
                                actionType = actionType,
                                message = "Swipe dispatched ($direction)"
                            )
                        )
                    } else {
                        Result.Error(
                            ActionError(
                                code = "ACTION_FAILED",
                                message = "Swipe could not be dispatched",
                                actionType = actionType,
                                targetId = request.targetId
                            )
                        )
                    }
                }

                // ------------------------------------------------------------
                // WAIT
                // ------------------------------------------------------------
                ActionExecutor.ActionType.WAIT -> {
                    val duration = if (request.durationMs > 0L) {
                        request.durationMs
                    } else {
                        DEFAULT_WAIT_MS
                    }
                    Thread.sleep(duration)

                    Result.Success(
                        ActionExecutor.ActionResult(
                            success = true,
                            actionType = actionType,
                            message = "Waited ${duration} ms"
                        )
                    )
                }
            }

            if (actionResult is Result.Success) {
                lastResult = actionResult.data
                if (request.waitAfterMs > 0L) {
                    Thread.sleep(request.waitAfterMs)
                }
                return actionResult
            } else {
                return actionResult
            }

        } catch (interrupted: InterruptedException) {
            Thread.currentThread().interrupt()
            return Result.Error(
                ActionError(
                    code = "INTERRUPTED",
                    message = "Action execution was interrupted",
                    actionType = actionType,
                    targetId = request.targetId
                )
            )
        } catch (exception: Exception) {
            return Result.Error(
                ActionError(
                    code = "EXECUTION_EXCEPTION",
                    message = exception.message ?: "Unexpected action execution failure",
                    actionType = actionType,
                    targetId = request.targetId
                )
            )
        } finally {
            node?.recycle()
        }
    }

    // ----------------------------------------------------------------
    // ✅ NEW: executeTap Implementation (Abstract Method fulfilled)
    // ----------------------------------------------------------------
    override fun executeTap(targetId: String): Result<ActionExecutor.ActionResult, ActionError> {
        val request = ActionExecutor.ActionRequest(
            type = ActionExecutor.ActionType.TAP,
            targetId = targetId
        )
        return executeAction(request)
    }

    // ----------------------------------------------------------------
    // ✅ FIXED: getLastActionResult return type matches Interface
    // ----------------------------------------------------------------
    override fun getLastActionResult(): ActionExecutor.ActionResult? {
        return lastResult
    }

    // ----------------------------------------------------------------
    // SWIPE PATH HELPER
    // ----------------------------------------------------------------
    private fun getSwipePath(
        request: ActionExecutor.ActionRequest,
        direction: String
    ): List<Pair<Float, Float>> {
        val coordinates = request.coordinates

        if (coordinates != null) {
            val startX = coordinates.first.toFloat()
            val startY = coordinates.second.toFloat()
            val gap = DEFAULT_SWIPE_DISTANCE

            val endPoint = when (direction.uppercase()) {
                "UP" -> Pair(startX, startY - gap)
                "DOWN" -> Pair(startX, startY + gap)
                "LEFT" -> Pair(startX - gap, startY)
                "RIGHT" -> Pair(startX + gap, startY)
                else -> Pair(startX, startY + gap)
            }
            return listOf(Pair(startX, startY), endPoint)
        }

        val screenCoordinates = getSwipeCoordinatesFromScreen(direction)
        return listOf(
            Pair(screenCoordinates.startX, screenCoordinates.startY),
            Pair(screenCoordinates.endX, screenCoordinates.endY)
        )
    }
}
