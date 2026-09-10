package com.vmax.workflow

import com.vmax.action.ActionError
import com.vmax.action.ActionExecutor
import com.vmax.common.Result
import java.util.PriorityQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * ActionOrchestrator
 *
 * Responsibilities:
 * 1. Action dispatch + tracking (direct execution)
 * 2. Priority-based queue management
 * 3. Thread-safe enqueue/dequeue
 * 4. FIFO within same priority (stable ordering)
 *
 * Pipeline:
 *   Incoming Action
 *        ↓
 *   Priority Queue (FIFO within priority)
 *        ↓
 *   Authorization / Validation
 *        ↓
 *   Reorder / Dequeue (highest priority first)
 *        ↓
 *   ActionExecutor.executeAction()
 */
class ActionOrchestrator(
    private val actionExecutor: ActionExecutor
) {

    // ─────────────────────────────────────────────────────────
    // QUEUE INFRASTRUCTURE
    // ─────────────────────────────────────────────────────────

    /**
     * Internal queue item = ActionRequest + priority + sequence
     * sequence ensures FIFO within same priority (stable sort)
     */
    private data class QueuedAction(
        val request: ActionExecutor.ActionRequest,
        val priority: Int,
        val sequence: Long
    )

    /**
     * PriorityQueue:
     *   - Higher priority first (compareByDescending)
     *   - Same priority → lower sequence first (FIFO)
     */
    private val queue = PriorityQueue<QueuedAction>(
        compareByDescending<QueuedAction> { it.priority }
            .thenBy { it.sequence }
    )

    private val lock = ReentrantLock()
    private var sequenceCounter: Long = 0L
    private val isProcessing = AtomicBoolean(false)

    // ─────────────────────────────────────────────────────────
    // PRIORITY CONSTANTS
    // ─────────────────────────────────────────────────────────

    companion object {
        const val PRIORITY_CRITICAL = 100
        const val PRIORITY_HIGH     = 75
        const val PRIORITY_NORMAL   = 50
        const val PRIORITY_LOW      = 25

        private const val TAG = "ActionOrchestrator"
    }

    // ─────────────────────────────────────────────────────────
    // 1. QUEUE MANAGEMENT (Priority + FIFO)
    // ─────────────────────────────────────────────────────────

    /**
     * Enqueue an action with priority.
     * Higher priority → processed first.
     * Same priority → FIFO order maintained.
     */
    fun enqueue(
        request: ActionExecutor.ActionRequest,
        priority: Int = PRIORITY_NORMAL
    ) {
        lock.withLock {
            queue.add(
                QueuedAction(
                    request = request,
                    priority = priority,
                    sequence = sequenceCounter++
                )
            )
        }
    }

    /**
     * Dequeue next highest-priority action.
     * Returns null if queue is empty.
     */
    fun processNext(): ActionExecutor.ActionRequest? {
        return lock.withLock {
            queue.poll()?.request
        }
    }

    /**
     * Process all queued actions in priority order.
     * Returns list of results for each action.
     */
    fun processAll(): List<Result<Unit, ActionError>> {
        if (!isProcessing.compareAndSet(false, true)) {
            return listOf(
                Result.Error(
                    ActionError(
                        code = "ALREADY_PROCESSING",
                        message = "Queue is already being processed"
                    )
                )
            )
        }

        val results = mutableListOf<Result<Unit, ActionError>>()

        try {
            while (true) {
                val request = processNext() ?: break
                results.add(dispatchAndTrack(request))
            }
        } finally {
            isProcessing.set(false)
        }

        return results
    }

    /**
     * Check if queue is empty.
     */
    fun isEmpty(): Boolean = lock.withLock { queue.isEmpty() }

    /**
     * Current queue size.
     */
    fun size(): Int = lock.withLock { queue.size }

    /**
     * Clear all pending actions.
     */
    fun clear() {
        lock.withLock { queue.clear() }
    }

    // ─────────────────────────────────────────────────────────
    // 2. CORE DISPATCH (with validation + tracking)
    // ─────────────────────────────────────────────────────────

    /**
     * Core dispatcher:
     *   Validate → Execute → Map result
     */
    fun dispatchAndTrack(
        request: ActionExecutor.ActionRequest
    ): Result<Unit, ActionError> {

        // Authorization / Validation gate
        val validationError = validate(request)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        // Execute
        val result = try {
            actionExecutor.executeAction(request)
        } catch (t: Throwable) {
            return Result.Error(
                ActionError(
                    code = "EXECUTION_EXCEPTION",
                    message = t.message ?: "Unknown exception during execution"
                )
            )
        }

        // Map result
        return when (result) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(
                ActionError(
                    code = "EXECUTION_FAILED",
                    message = result.error.message ?: "Action failed"
                )
            )
        }
    }

    // ─────────────────────────────────────────────────────────
    // 3. AUTHORIZATION / VALIDATION GATE
    // ─────────────────────────────────────────────────────────

    private fun validate(
        request: ActionExecutor.ActionRequest
    ): ActionError? {
        return when (request.type) {
            ActionExecutor.ActionType.SET_TEXT -> {
                if (request.text.isNullOrEmpty()) {
                    ActionError(
                        code = "INVALID_REQUEST",
                        message = "Text must not be empty"
                    )
                } else null
            }

            ActionExecutor.ActionType.WAIT -> {
                if (request.durationMs < 0L) {
                    ActionError(
                        code = "INVALID_REQUEST",
                        message = "Wait duration must not be negative"
                    )
                } else null
            }

            ActionExecutor.ActionType.SCROLL -> {
                if (request.durationMs < 0L) {
                    ActionError(
                        code = "INVALID_REQUEST",
                        message = "Scroll amount must not be negative"
                    )
                } else null
            }

            else -> null
        }
    }

    // ─────────────────────────────────────────────────────────
    // 4. PUBLIC API (direct execution, bypasses queue)
    // ─────────────────────────────────────────────────────────

    fun click(targetId: String): Result<Unit, ActionError> =
        dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.CLICK,
                targetId = targetId
            )
        )

    fun tap(targetId: String): Result<Unit, ActionError> =
        dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.TAP,
                targetId = targetId
            )
        )

    fun setText(targetId: String, text: String): Result<Unit, ActionError> =
        dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SET_TEXT,
                targetId = targetId,
                text = text
            )
        )

    fun clearText(targetId: String): Result<Unit, ActionError> =
        dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.CLEAR_TEXT,
                targetId = targetId
            )
        )

    fun scroll(direction: String, amount: Int): Result<Unit, ActionError> =
        dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SCROLL,
                targetClass = direction,
                durationMs = amount.toLong()
            )
        )

    fun wait(durationMs: Long): Result<Unit, ActionError> =
        dispatchAndTrack(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.WAIT,
                durationMs = durationMs
            )
        )

    // ─────────────────────────────────────────────────────────
    // 5. QUEUED API (enqueue + auto-process)
    // ─────────────────────────────────────────────────────────

    /**
     * Enqueue a CLICK with priority.
     */
    fun enqueueClick(
        targetId: String,
        priority: Int = PRIORITY_NORMAL
    ) {
        enqueue(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.CLICK,
                targetId = targetId
            ),
            priority
        )
    }

    /**
     * Enqueue a TAP with priority.
     */
    fun enqueueTap(
        targetId: String,
        priority: Int = PRIORITY_NORMAL
    ) {
        enqueue(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.TAP,
                targetId = targetId
            ),
            priority
        )
    }

    /**
     * Enqueue SET_TEXT with priority.
     */
    fun enqueueSetText(
        targetId: String,
        text: String,
        priority: Int = PRIORITY_NORMAL
    ) {
        enqueue(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.SET_TEXT,
                targetId = targetId,
                text = text
            ),
            priority
        )
    }

    /**
     * Enqueue WAIT with priority.
     */
    fun enqueueWait(
        durationMs: Long,
        priority: Int = PRIORITY_NORMAL
    ) {
        enqueue(
            ActionExecutor.ActionRequest(
                type = ActionExecutor.ActionType.WAIT,
                durationMs = durationMs
            ),
            priority
        )
    }
}
