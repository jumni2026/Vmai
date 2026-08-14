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

/**
 * VMAX Enterprise v2.6.1
 *
 * Android implementation of the platform-independent
 * ActionExecutor contract.
 *
 * Architecture:
 *
 * ActionExecutor
 *       ↓
 * AndroidActionExecutor
 *
 * Rules:
 * - No business logic
 * - No IRCTC-specific logic
 * - No VMAXAccessibilityService implementation
 * - SCROLL targetText is treated as direction
 * - targetText is NOT used as a node selector for SCROLL
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

        return try {
            findNodeRecursive(
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

        if (
            matchesTarget(
                node = node,
                targetId = targetId,
                targetText = targetText,
                targetClass = targetClass
            )
        ) {
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

        if (!node.isVisibleToUser) {
            return false
        }

        if (targetId != null && node.viewIdResourceName != targetId) {
            return false
        }

        if (targetText != null) {
            val nodeText = node.text?.toString()
            val nodeDescription = node.contentDescription?.toString()

            if (nodeText != targetText && nodeDescription != targetText) {
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

    private fun performSwipe(
        pathPoints: List<Pair<Float, Float>>,
        durationMs: Long
    ): Boolean {
        if (pathPoints.size < 2) return false

        val gesturePath = Path().apply {
            moveTo(pathPoints[0].first, pathPoints[0].second)
            for (index in 1 until pathPoints.size) {
                lineTo(pathPoints[index].first, pathPoints[index].second)
            }
        }

        val safeDuration = durationMs.coerceAtLeast(1L)
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(gesturePath, 0L, safeDuration))
            .build()
        return accessibilityService.dispatchGesture(gesture, null, null)
    }

    // ----------------------------------------------------------------
    // SWIPE COORDINATES
    // ----------------------------------------------------------------

    private fun getSwipeCoordinatesFromScreen(direction: String): SwipeCoords {
        val metrics = accessibilityService.resources.displayMetrics
        val width = metrics.widthPixels.toFloat()
        val height = metrics.heightPixels.toFloat()

        val centerX = width / 2f
        val centerY = height / 2f
        val gap = DEFAULT_SWIPE_DISTANCE

        return when (direction.uppercase()) {
            "UP" -> SwipeCoords(centerX, centerY + gap, centerX, centerY - gap)
            "DOWN" -> SwipeCoords(centerX, centerY - gap, centerX, centerY + gap)
            "LEFT" -> SwipeCoords(centerX + gap, centerY, centerX - gap, centerY)
            "RIGHT" -> SwipeCoords(centerX - gap, centerY, centerX + gap, centerY)
            else -> SwipeCoords(centerX, centerY - gap, centerX, centerY + gap)
        }
    }

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

    // ----------------------------------------------------------------
    // ACTION RESULT HELPERS
    // ----------------------------------------------------------------

    private fun success(
        actionType: ActionExecutor.ActionType,
        message: String
    ): Result<ActionExecutor.ActionResult, ActionError> {
        val result = ActionExecutor.ActionResult(
            success = true,
            actionType = actionType,
            message = message
        )
        lastResult = result
        return Result.Success(result)
    }

    private fun failure(
        code: String,
        message: String,
        actionType: ActionExecutor.ActionType,
        targetId: String?
    ): Result<ActionExecutor.ActionResult, ActionError> {
        return Result.Error(
            ActionError(
                code = code,
                message = message,
                actionType = actionType,
                targetId = targetId
            )
        )
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
            val searchParameters = when (actionType) {
                ActionExecutor.ActionType.SCROLL ->
                    Triple(request.targetId, null, request.targetClass)
                ActionExecutor.ActionType.SWIPE, ActionExecutor.ActionType.WAIT ->
                    Triple(null, null, null)
                else ->
                    Triple(request.targetId, request.targetText, request.targetClass)
            }

            node = findNode(
                targetId = searchParameters.first,
                targetText = searchParameters.second,
                targetClass = searchParameters.third
            )

            val requiresNode = when (actionType) {
                ActionExecutor.ActionType.SWIPE, ActionExecutor.ActionType.WAIT -> false
                else -> true
            }

            if (requiresNode && node == null) {
                return failure(
                    code = "NODE_NOT_FOUND",
                    message = "Target UI node not found on screen",
                    actionType = actionType,
                    targetId = request.targetId
                )
            }

            val result = when (actionType) {

                ActionExecutor.ActionType.TAP -> {
                    val targetNode = node!!
                    val coordinates = getTargetCoordinates(targetNode, request)

                    if (performTap(coordinates.first, coordinates.second)) {
                        success(actionType, "Coordinate tap performed")
                    } else {
                        failure("ACTION_FAILED", "Could not perform TAP", actionType, request.targetId)
                    }
                }

                ActionExecutor.ActionType.CLICK -> {
                    val targetNode = node!!
                    val coordinates = getTargetCoordinates(targetNode, request)

                    if (request.coordinates == null &&
                        targetNode.isClickable &&
                        targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    ) {
                        success(actionType, "Native accessibility click performed")
                    } else if (performTap(coordinates.first, coordinates.second)) {
                        success(actionType, "Coordinate click performed")
                    } else {
                        failure("ACTION_FAILED", "Could not perform CLICK", actionType, request.targetId)
                    }
                }

                ActionExecutor.ActionType.DOUBLE_TAP -> {
                    val targetNode = node!!
                    val coordinates = getTargetCoordinates(targetNode, request)

                    if (!performTap(coordinates.first, coordinates.second)) {
                        failure("ACTION_FAILED", "First tap of DOUBLE_TAP failed", actionType, request.targetId)
                    } else {
                        Thread.sleep(DOUBLE_TAP_DELAY_MS)
                        if (performTap(coordinates.first, coordinates.second)) {
                            success(actionType, "Double tap performed")
                        } else {
                            failure("ACTION_FAILED", "Second tap of DOUBLE_TAP failed", actionType, request.targetId)
                        }
                    }
                }

                ActionExecutor.ActionType.LONG_CLICK -> {
                    val targetNode = node!!
                    val coordinates = getTargetCoordinates(targetNode, request)

                    if (request.coordinates == null &&
                        targetNode.isLongClickable &&
                        targetNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)
                    ) {
                        success(actionType, "Native accessibility long-click performed")
                    } else if (performLongTap(coordinates.first, coordinates.second)) {
                        success(actionType, "Coordinate long-click performed")
                    } else {
                        failure("ACTION_FAILED", "Could not perform LONG_CLICK", actionType, request.targetId)
                    }
                }

                ActionExecutor.ActionType.SET_TEXT -> {
                    val text = request.text

                    if (text == null) {
                        failure("INVALID_REQUEST", "Text is null for SET_TEXT", actionType, request.targetId)
                    } else if (node?.isEditable != true) {
                        failure("TARGET_NOT_EDITABLE", "Target node is not editable", actionType, request.targetId)
                    } else {
                        val arguments = Bundle().apply {
                            putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                text
                            )
                        }
                        if (node!!.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)) {
                            success(actionType, "Text set successfully")
                        } else {
                            failure("ACTION_FAILED", "Failed to set text", actionType, request.targetId)
                        }
                    }
                }

                ActionExecutor.ActionType.CLEAR_TEXT -> {
                    if (node?.isEditable != true) {
                        failure("TARGET_NOT_EDITABLE", "Target node is not editable", actionType, request.targetId)
                    } else {
                        val arguments = Bundle().apply {
                            putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                ""
                            )
                        }
                        if (node!!.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)) {
                            success(actionType, "Text cleared successfully")
                        } else {
                            failure("ACTION_FAILED", "Failed to clear text", actionType, request.targetId)
                        }
                    }
                }

                ActionExecutor.ActionType.SCROLL -> {
                    val direction = request.targetText?.trim()?.uppercase() ?: "DOWN"

                    when (direction) {
                        "UP", "DOWN" -> {
                            val targetNode = node!!
                            val actionId = if (direction == "UP") {
                                AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
                            } else {
                                AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                            }

                            if (targetNode.performAction(actionId)) {
                                success(actionType, "Native scroll performed ($direction)")
                            } else {
                                failure("ACTION_FAILED", "Could not perform vertical scroll", actionType, request.targetId)
                            }
                        }

                        "LEFT", "RIGHT" -> {
                            val path = getSwipePath(request, direction)
                            val duration = if (request.durationMs > 0L) request.durationMs else TAP_DURATION_MS

                            if (performSwipe(path, duration)) {
                                success(actionType, "Horizontal scroll performed ($direction)")
                            } else {
                                failure("ACTION_FAILED", "Could not perform horizontal scroll", actionType, request.targetId)
                            }
                        }

                        else -> {
                            failure("INVALID_DIRECTION", "Unsupported scroll direction: $direction", actionType, request.targetId)
                        }
                    }
                }

                ActionExecutor.ActionType.SWIPE -> {
                    val direction = request.targetText?.trim()?.uppercase() ?: "DOWN"
                    val path = getSwipePath(request, direction)
                    val duration = if (request.durationMs > 0L) request.durationMs else TAP_DURATION_MS

                    if (performSwipe(path, duration)) {
                        success(actionType, "Swipe performed ($direction)")
                    } else {
                        failure("ACTION_FAILED", "Swipe could not be dispatched", actionType, request.targetId)
                    }
                }

                ActionExecutor.ActionType.WAIT -> {
                    val duration = if (request.durationMs > 0L) request.durationMs else DEFAULT_WAIT_MS
                    Thread.sleep(duration)
                    success(actionType, "Waited ${duration} ms")
                }
            }

            if (result is Result.Success && request.waitAfterMs > 0L) {
                Thread.sleep(request.waitAfterMs)
            }

            return result

        } catch (interrupted: InterruptedException) {
            Thread.currentThread().interrupt()
            return failure(
                code = "INTERRUPTED",
                message = "Action execution was interrupted",
                actionType = actionType,
                targetId = request.targetId
            )
        } catch (exception: Exception) {
            return failure(
                code = "EXECUTION_EXCEPTION",
                message = exception.message ?: "Unexpected action execution failure",
                actionType = actionType,
                targetId = request.targetId
            )
        } finally {
            node?.recycle()
        }
    }

    // ----------------------------------------------------------------
    // EXPLICIT ACTIONEXECUTOR CONTRACT METHODS
    // ----------------------------------------------------------------

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
        return executeAction(ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.SCROLL, targetText = direction))
    }

    override fun executeWait(durationMs: Long): Result<ActionExecutor.ActionResult, ActionError> {
        return executeAction(ActionExecutor.ActionRequest(type = ActionExecutor.ActionType.WAIT, durationMs = durationMs, waitAfterMs = 0L))
    }

    override fun isActionAvailable(actionType: ActionExecutor.ActionType): Boolean = true

    override fun getLastActionResult(): ActionExecutor.ActionResult? = lastResult
}
