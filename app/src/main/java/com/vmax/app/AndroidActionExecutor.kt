package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.action.ActionExecutor.ActionRequest
import com.vmax.action.ActionExecutor.ActionResult
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
 * ActionExecutor (contract)
 *          ↓
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

    private var lastResult: ActionResult? = null

    private data class SwipeCoords(
        val startX: Float,
        val startY: Float,
        val endX: Float,
        val endY: Float
    )

    // ------------------------------------------------------------------------
    // Node Discovery (Enhanced with contentDescription support)
    // ------------------------------------------------------------------------

    private fun findNode(
        targetId: String?,
        targetText: String?,
        targetClass: String?
    ): AccessibilityNodeInfo? {

        if (
            targetId == null &&
            targetText == null &&
            targetClass == null
        ) {
            return null
        }

        val root = accessibilityService.rootInActiveWindow
            ?: return null

        try {
            // ✅ Find and RETURN a NEW node reference (safe lifecycle)
            val result = findNodeRecursive(
                node = root,
                targetId = targetId,
                targetText = targetText,
                targetClass = targetClass
            )

            // ✅ result is either:
            //    - A NEW node via AccessibilityNodeInfo.obtain()
            //    - null (no match found)
            return result

        } finally {
            // ✅ root is ALWAYS recycled here (only once)
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
            // ✅ Return a NEW reference (caller owns it)
            // ✅ Current node remains owned by caller (parent)
            return AccessibilityNodeInfo.obtain(node)
        }

        // ✅ Traverse children
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
                    // ✅ Found a match, return it (child is already a NEW reference)
                    // ✅ Current node remains owned by caller (will be recycled by parent)
                    return found
                }
            } finally {
                // ✅ Child is ALWAYS recycled here (regardless of match)
                // ✅ If match found, child is recycled AFTER obtain() created a copy
                child.recycle()
            }
        }

        // ✅ No match found, return null
        // ✅ Current node remains owned by caller (will be recycled by parent)
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

        if (targetId != null &&
            node.viewIdResourceName != targetId
        ) {
            return false
        }

        if (targetText != null) {
            // ✅ Diagnostic-backed: Check both text AND contentDescription
            val nodeText = node.text?.toString()
            val nodeDesc = node.contentDescription?.toString()

            val textMatch = nodeText == targetText
            val descMatch = nodeDesc == targetText

            if (!textMatch && !descMatch) {
                return false
            }
        }

        if (targetClass != null &&
            node.className?.toString() != targetClass
        ) {
            return false
        }

        return true
    }

    // ------------------------------------------------------------------------
    // Coordinate Helpers
    // ------------------------------------------------------------------------

    private fun getBoundsCenter(
        node: AccessibilityNodeInfo
    ): Pair<Float, Float> {

        val bounds = Rect()
        node.getBoundsInScreen(bounds)

        return Pair(
            bounds.centerX().toFloat(),
            bounds.centerY().toFloat()
        )
    }

    private fun getTargetCoordinates(
        node: AccessibilityNodeInfo,
        request: ActionRequest
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

    // ------------------------------------------------------------------------
    // Gesture Helpers
    // ------------------------------------------------------------------------

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

        if (pathPoints.size < 2) {
            return false
        }

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

    // ------------------------------------------------------------------------
    // Swipe Coordinates
    // ------------------------------------------------------------------------

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

    private fun getSwipePath(
        request: ActionRequest,
        direction: String
    ): List<Pair<Float, Float>> {

        val coordinates = request.coordinates

        if (coordinates != null) {

            val startX = coordinates.first.toFloat()
            val startY = coordinates.second.toFloat()

            val gap = DEFAULT_SWIPE_DISTANCE

            val endPoint = when (direction.uppercase()) {

                "UP" -> Pair(
                    startX,
                    startY - gap
                )

                "DOWN" -> Pair(
                    startX,
                    startY + gap
                )

                "LEFT" -> Pair(
                    startX - gap,
                    startY
                )

                "RIGHT" -> Pair(
                    startX + gap,
                    startY
                )

                else -> Pair(
                    startX,
                    startY + gap
                )
            }

            return listOf(
                Pair(startX, startY),
                endPoint
            )
        }

        val screenCoordinates =
            getSwipeCoordinatesFromScreen(direction)

        return listOf(
            Pair(
                screenCoordinates.startX,
                screenCoordinates.startY
            ),
            Pair(
                screenCoordinates.endX,
                screenCoordinates.endY
            )
        )
    }

    // ------------------------------------------------------------------------
    // Result Handling
    // ------------------------------------------------------------------------

    private fun success(
        actionType: ActionExecutor.ActionType,
        message: String
    ): Result<ActionResult, ActionError> {

        val result = ActionResult(
            success = true,
            actionType = actionType,
            message = message
        )

        lastResult = result

        return Result.Success(result)
    }

    private fun error(
        code: String,
        message: String,
        actionType: ActionExecutor.ActionType,
        targetId: String?
    ): Result<ActionResult, ActionError> {

        // ✅ Store last result even for errors (contract requires it)
        val errorResult = ActionResult(
            success = false,
            actionType = actionType,
            message = message
        )
        lastResult = errorResult

        val error = ActionError(
            code = code,
            message = message,
            actionType = actionType,
            targetId = targetId
        )

        return Result.Error(error)
    }

    // ------------------------------------------------------------------------
    // Main Action Execution
    // ------------------------------------------------------------------------

    override fun executeAction(
        request: ActionRequest
    ): Result<ActionResult, ActionError> {

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

                return error(
                    code = "NODE_NOT_FOUND",
                    message = "Target UI node not found on screen",
                    actionType = actionType,
                    targetId = request.targetId
                )
            }

            val actionResult = when (actionType) {

                // ------------------------------------------------------------
                // TAP / CLICK
                // ------------------------------------------------------------

                ActionExecutor.ActionType.TAP,
                ActionExecutor.ActionType.CLICK -> {

                    val targetNode = node!!

                    val coordinates =
                        getTargetCoordinates(
                            targetNode,
                            request
                        )

                    /*
                     * Explicit coordinates have priority.
                     * This guarantees that a coordinate request is not
                     * silently replaced by ACTION_CLICK.
                     */
                    if (request.coordinates != null) {

                        if (
                            performTap(
                                coordinates.first,
                                coordinates.second
                            )
                        ) {
                            success(
                                actionType,
                                "Coordinate tap performed"
                            )
                        } else {
                            error(
                                "ACTION_FAILED",
                                "Coordinate tap could not be dispatched",
                                actionType,
                                request.targetId
                            )
                        }

                    } else if (
                        targetNode.isClickable &&
                        targetNode.performAction(
                            AccessibilityNodeInfo.ACTION_CLICK
                        )
                    ) {

                        success(
                            actionType,
                            "Native accessibility click performed"
                        )

                    } else if (
                        performTap(
                            coordinates.first,
                            coordinates.second
                        )
                    ) {

                        success(
                            actionType,
                            "Coordinate tap performed as fallback"
                        )

                    } else {

                        error(
                            "ACTION_FAILED",
                            "Could not perform TAP/CLICK",
                            actionType,
                            request.targetId
                        )
                    }
                }

                // ------------------------------------------------------------
                // DOUBLE TAP
                // ------------------------------------------------------------

                ActionExecutor.ActionType.DOUBLE_TAP -> {

                    val targetNode = node!!

                    val coordinates =
                        getTargetCoordinates(
                            targetNode,
                            request
                        )

                    val firstTap = performTap(
                        coordinates.first,
                        coordinates.second
                    )

                    if (!firstTap) {

                        error(
                            "ACTION_FAILED",
                            "First tap of DOUBLE_TAP could not be dispatched",
                            actionType,
                            request.targetId
                        )

                    } else {

                        Thread.sleep(DOUBLE_TAP_DELAY_MS)

                        val secondTap = performTap(
                            coordinates.first,
                            coordinates.second
                        )

                        if (secondTap) {

                            success(
                                actionType,
                                "Double tap dispatched"
                            )

                        } else {

                            error(
                                "ACTION_FAILED",
                                "Second tap of DOUBLE_TAP could not be dispatched",
                                actionType,
                                request.targetId
                            )
                        }
                    }
                }

                // ------------------------------------------------------------
                // LONG CLICK
                // ------------------------------------------------------------

                ActionExecutor.ActionType.LONG_CLICK -> {

                    val targetNode = node!!

                    val coordinates =
                        getTargetCoordinates(
                            targetNode,
                            request
                        )

                    if (
                        request.coordinates == null &&
                        targetNode.isLongClickable &&
                        targetNode.performAction(
                            AccessibilityNodeInfo.ACTION_LONG_CLICK
                        )
                    ) {

                        success(
                            actionType,
                            "Native accessibility long-click performed"
                        )

                    } else if (
                        performLongTap(
                            coordinates.first,
                            coordinates.second
                        )
                    ) {

                        success(
                            actionType,
                            "Coordinate long tap performed"
                        )

                    } else {

                        error(
                            "ACTION_FAILED",
                            "Could not perform LONG_CLICK",
                            actionType,
                            request.targetId
                        )
                    }
                }

                // ------------------------------------------------------------
                // SET TEXT
                // ------------------------------------------------------------

                ActionExecutor.ActionType.SET_TEXT -> {

                    val text = request.text

                    if (text == null) {

                        error(
                            "INVALID_REQUEST",
                            "Text is null for SET_TEXT",
                            actionType,
                            request.targetId
                        )

                    } else if (node?.isEditable != true) {

                        error(
                            "TARGET_NOT_EDITABLE",
                            "Target node is not editable",
                            actionType,
                            request.targetId
                        )

                    } else {

                        val arguments = Bundle().apply {
                            putCharSequence(
                                AccessibilityNodeInfo
                                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                text
                            )
                        }

                        if (
                            node.performAction(
                                AccessibilityNodeInfo.ACTION_SET_TEXT,
                                arguments
                            )
                        ) {

                            success(
                                actionType,
                                "Text set successfully"
                            )

                        } else {

                            error(
                                "ACTION_FAILED",
                                "Failed to set text",
                                actionType,
                                request.targetId
                            )
                        }
                    }
                }

                // ------------------------------------------------------------
                // CLEAR TEXT
                // ------------------------------------------------------------

                ActionExecutor.ActionType.CLEAR_TEXT -> {

                    if (node?.isEditable != true) {

                        error(
                            "TARGET_NOT_EDITABLE",
                            "Target node is not editable",
                            actionType,
                            request.targetId
                        )

                    } else {

                        val arguments = Bundle().apply {
                            putCharSequence(
                                AccessibilityNodeInfo
                                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                ""
                            )
                        }

                        if (
                            node.performAction(
                                AccessibilityNodeInfo.ACTION_SET_TEXT,
                                arguments
                            )
                        ) {

                            success(
                                actionType,
                                "Text cleared successfully"
                            )

                        } else {

                            error(
                                "ACTION_FAILED",
                                "Failed to clear text",
                                actionType,
                                request.targetId
                            )
                        }
                    }
                }

                // ------------------------------------------------------------
                // SCROLL
                // ------------------------------------------------------------

                ActionExecutor.ActionType.SCROLL -> {

                    val direction =
                        request.targetText
                            ?.trim()
                            ?.uppercase()
                            ?: "DOWN"

                    when (direction) {

                        "UP", "DOWN" -> {

                            if (node == null) {
                                return error(
                                    "NODE_NOT_FOUND",
                                    "Target node required for vertical scroll",
                                    actionType,
                                    request.targetId
                                )
                            }

                            val actionId =
                                if (direction == "UP") {
                                    AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
                                } else {
                                    AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                                }

                            if (
                                node.performAction(actionId) == true
                            ) {

                                success(
                                    actionType,
                                    "Native scroll performed ($direction)"
                                )

                            } else {

                                error(
                                    "ACTION_FAILED",
                                    "Could not perform vertical scroll",
                                    actionType,
                                    request.targetId
                                )
                            }
                        }

                        "LEFT", "RIGHT" -> {

                            val path =
                                getSwipePath(
                                    request,
                                    direction
                                )

                            val duration =
                                if (request.durationMs > 0L) {
                                    request.durationMs
                                } else {
                                    TAP_DURATION_MS
                                }

                            if (
                                performSwipe(
                                    path,
                                    duration
                                )
                            ) {

                                success(
                                    actionType,
                                    "Horizontal scroll via swipe ($direction)"
                                )

                            } else {

                                error(
                                    "ACTION_FAILED",
                                    "Could not perform horizontal scroll",
                                    actionType,
                                    request.targetId
                                )
                            }
                        }

                        else -> {

                            if (node == null) {
                                return error(
                                    "NODE_NOT_FOUND",
                                    "Target node required for default scroll",
                                    actionType,
                                    request.targetId
                                )
                            }

                            if (
                                node.performAction(
                                    AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                                ) == true
                            ) {

                                success(
                                    actionType,
                                    "Default scroll performed"
                                )

                            } else {

                                error(
                                    "ACTION_FAILED",
                                    "Could not perform default scroll",
                                    actionType,
                                    request.targetId
                                )
                            }
                        }
                    }
                }

                // ------------------------------------------------------------
                // SWIPE
                // ------------------------------------------------------------

                ActionExecutor.ActionType.SWIPE -> {

                    val direction =
                        request.targetText
                            ?.trim()
                            ?.uppercase()
                            ?: "DOWN"

                    val path =
                        getSwipePath(
                            request,
                            direction
                        )

                    val duration =
                        if (request.durationMs > 0L) {
                            request.durationMs
                        } else {
                            TAP_DURATION_MS
                        }

                    if (
                        performSwipe(
                            path,
                            duration
                        )
                    ) {

                        success(
                            actionType,
                            "Swipe dispatched ($direction)"
                        )

                    } else {

                        error(
                            "ACTION_FAILED",
                            "Swipe could not be dispatched",
                            actionType,
                            request.targetId
                        )
                    }
                }

                // ------------------------------------------------------------
                // WAIT
                // ------------------------------------------------------------

                ActionExecutor.ActionType.WAIT -> {

                    val duration =
                        if (request.durationMs > 0L) {
                            request.durationMs
                        } else {
                            DEFAULT_WAIT_MS
                        }

                    Thread.sleep(duration)

                    success(
                        actionType,
                        "Waited ${duration} ms"
                    )
                }
            }

            // ✅ FINAL FIX: waitAfterMs is applied ONLY after a successful Result
            return if (actionResult is Result.Success) {
                if (request.waitAfterMs > 0L) {
                    Thread.sleep(request.waitAfterMs)
                }
                actionResult
            } else {
                // If it's an error, do NOT apply waitAfterMs.
                actionResult
            }

        } catch (interrupted: InterruptedException) {

            Thread.currentThread().interrupt()

            return error(
                "INTERRUPTED",
                "Action execution was interrupted",
                actionType,
                request.targetId
            )

        } catch (exception: Exception) {

            return error(
                "EXECUTION_EXCEPTION",
                exception.message
                    ?: "Unexpected action execution failure",
                actionType,
                request.targetId
            )

        } finally {

            /*
             * ✅ SAFE LIFECYCLE:
             * The node returned by findNode() is owned by this method.
             * Always recycle it after action execution.
             */
            node?.recycle()
        }
    }

    // ------------------------------------------------------------------------
    // Contract Convenience Methods
    // ------------------------------------------------------------------------

    override fun executeTap(
        targetId: String
    ): Result<ActionResult, ActionError> =
        executeAction(
            ActionRequest(
                type = ActionExecutor.ActionType.TAP,
                targetId = targetId
            )
        )

    override fun executeClick(
        targetId: String
    ): Result<ActionResult, ActionError> =
        executeAction(
            ActionRequest(
                type = ActionExecutor.ActionType.CLICK,
                targetId = targetId
            )
        )

    override fun executeSetText(
        targetId: String,
        text: String
    ): Result<ActionResult, ActionError> =
        executeAction(
            ActionRequest(
                type = ActionExecutor.ActionType.SET_TEXT,
                targetId = targetId,
                text = text
            )
        )

    override fun executeClearText(
        targetId: String
    ): Result<ActionResult, ActionError> =
        executeAction(
            ActionRequest(
                type = ActionExecutor.ActionType.CLEAR_TEXT,
                targetId = targetId
            )
        )

    override fun executeScroll(
        direction: String,
        amount: Int
    ): Result<ActionResult, ActionError> =
        executeAction(
            ActionRequest(
                type = ActionExecutor.ActionType.SCROLL,
                targetText = direction
            )
        )

    override fun executeWait(
        durationMs: Long
    ): Result<ActionResult, ActionError> =
        executeAction(
            ActionRequest(
                type = ActionExecutor.ActionType.WAIT,
                durationMs = durationMs
            )
        )

    // ------------------------------------------------------------------------
    // Availability / State
    // ------------------------------------------------------------------------

    override fun isActionAvailable(
        actionType: ActionExecutor.ActionType
    ): Boolean {

        return when (actionType) {

            ActionExecutor.ActionType.WAIT ->
                true

            ActionExecutor.ActionType.SWIPE ->
                true

            else ->
                accessibilityService.rootInActiveWindow != null
        }
    }

    override fun getLastActionResult():
        ActionResult? =
        lastResult
}
