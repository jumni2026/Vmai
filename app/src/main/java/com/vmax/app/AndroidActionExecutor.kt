package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionExecutor
import com.vmax.common.Result
import com.vmax.action.ActionExecutor.ActionRequest

class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_ORCHESTRATOR"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        // Pure UI Evidence (No User Data Hardcoding)
        private const val EVIDENCE_FROM = "From"
        private const val EVIDENCE_TO = "To"
        private const val EVIDENCE_DATE = "Date"
        private const val EVIDENCE_SEARCH = "Search"
        private const val EVIDENCE_ADD_NEW = "Add New"
        private const val EVIDENCE_ADD_PASSENGER = "Add Passenger"
        private const val EVIDENCE_REVIEW = "REVIEW JOURNEY DETAILS"
        private const val EVIDENCE_CONFIRM_BERTH = "confirm berths"
        private const val EVIDENCE_CAPTCHA = "CAPTCHA"
        private const val EVIDENCE_OTP = "OTP"
    }

    // Simplified State Machine (No Hardcoded Data Dependencies)
    private enum class State {
        IDLE,
        FROM_FIELD_FOCUSED, TO_FIELD_FOCUSED, DATE_FIELD_FOCUSED,
        SEARCH_CLICKED,
        USER_BOUNDARY, STOPPED
    }

    private var currentState = State.IDLE
    private lateinit var executor: AndroidActionExecutor

    override fun onServiceConnected() {
        super.onServiceConnected()
        executor = AndroidActionExecutor(this)
        Log.i(TAG, "VMAX Orchestrator Connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName != IRCTC_PACKAGE) return

        val root = rootInActiveWindow ?: return

        // CAPTCHA/OTP Boundary (Lock without killing service)
        if (isCaptchaOrOtpPresent(root)) {
            currentState = State.USER_BOUNDARY
            Log.w(TAG, "CAPTCHA/OTP detected. Locking to USER_BOUNDARY.")
            root.recycle()
            return
        }

        if (currentState == State.USER_BOUNDARY || currentState == State.STOPPED) {
            root.recycle()
            return
        }

        try {
            processWorkflow(root)
        } finally {
            root.recycle()
        }
    }

    private fun processWorkflow(root: AccessibilityNodeInfo) {
        when (currentState) {
            State.IDLE -> handleSearchFieldFlow(root)
            State.FROM_FIELD_FOCUSED -> handleDestinationFieldFlow(root)
            State.TO_FIELD_FOCUSED -> handleDateFieldFlow(root)
            State.DATE_FIELD_FOCUSED -> handleSearchAction(root)
            else -> { /* Awaiting event/evidence */ }
        }
    }

    // ----------------------------------------------------------------
    // FLOW 1: FROM -> TO -> DATE -> SEARCH (Pure Evidence-Based)
    // ----------------------------------------------------------------
    private fun handleSearchFieldFlow(root: AccessibilityNodeInfo) {
        val fromNode = findEditableNodeByEvidence(root, EVIDENCE_FROM)
        if (fromNode != null && currentState == State.IDLE) {
            executeClick(fromNode) { success ->
                if (success) {
                    currentState = State.FROM_FIELD_FOCUSED
                    Log.i(TAG, "FROM field clicked.")
                }
            }
        }
    }

    private fun handleDestinationFieldFlow(root: AccessibilityNodeInfo) {
        val toNode = findEditableNodeByEvidence(root, EVIDENCE_TO)
        if (toNode != null && currentState == State.FROM_FIELD_FOCUSED) {
            executeClick(toNode) { success ->
                if (success) {
                    currentState = State.TO_FIELD_FOCUSED
                    Log.i(TAG, "TO field clicked.")
                }
            }
        }
    }

    private fun handleDateFieldFlow(root: AccessibilityNodeInfo) {
        val dateNode = findNodeByEvidence(root, EVIDENCE_DATE, isClickable = true)
        if (dateNode != null && currentState == State.TO_FIELD_FOCUSED) {
            executeClick(dateNode) { success ->
                if (success) {
                    currentState = State.DATE_FIELD_FOCUSED
                    Log.i(TAG, "DATE field clicked.")
                }
            }
        }
    }

    private fun handleSearchAction(root: AccessibilityNodeInfo) {
        val searchNode = findNodeByEvidence(root, EVIDENCE_SEARCH, isClickable = true)
        if (searchNode != null && currentState == State.DATE_FIELD_FOCUSED) {
            executeClick(searchNode) { success ->
                if (success) {
                    currentState = State.STOPPED
                    Log.i(TAG, "SEARCH clicked. Automation Stopped at Train List.")
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS (Locked Contract)
    // ----------------------------------------------------------------
    private fun executeClick(node: AccessibilityNodeInfo?, onDispatched: (Boolean) -> Unit) {
        if (node == null) { onDispatched(false); return }
        val request = ActionRequest(
            type = ActionExecutor.ActionType.CLICK,
            targetId = node.viewIdResourceName,
            targetClass = node.className?.toString()
        )
        dispatchToExecutor(request, onDispatched)
    }

    private fun dispatchToExecutor(request: ActionRequest, onDispatched: (Boolean) -> Unit) {
        try {
            val result = executor.executeAction(request)
            when (result) {
                is Result.Success -> onDispatched(true)
                is Result.Error -> {
                    Log.e(TAG, "Executor error: ${result.error.message}")
                    onDispatched(false)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception dispatching action", e)
            onDispatched(false)
        }
    }

    // ----------------------------------------------------------------
    // EVIDENCE-BASED FINDERS (No Guesses)
    // ----------------------------------------------------------------
    private fun findNodeByEvidence(root: AccessibilityNodeInfo, evidence: String, isClickable: Boolean = false): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""

            if (node.isVisibleToUser && (!isClickable || node.isClickable)) {
                if (text.equals(evidence, ignoreCase = true) || hint.equals(evidence, ignoreCase = true) || desc.equals(evidence, ignoreCase = true)) {
                    return node
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun findEditableNodeByEvidence(root: AccessibilityNodeInfo, evidence: String): AccessibilityNodeInfo? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""

            if (node.isVisibleToUser && node.isEditable) {
                if (text.equals(evidence, ignoreCase = true) || hint.equals(evidence, ignoreCase = true) || desc.equals(evidence, ignoreCase = true)) {
                    return node
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    private fun isCaptchaOrOtpPresent(root: AccessibilityNodeInfo): Boolean {
        return findNodeByExactText(root, EVIDENCE_CAPTCHA) != null ||
               findNodeByExactText(root, EVIDENCE_OTP) != null
    }
    
    private fun findNodeByExactText(root: AccessibilityNodeInfo, targetText: String, isClickable: Boolean = false): AccessibilityNodeInfo? {
        if (targetText.isEmpty()) return null
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val text = node.text?.toString() ?: ""
            if (node.isVisibleToUser && (!isClickable || node.isClickable)) {
                if (text.equals(targetText, ignoreCase = true)) {
                    return node
                }
            }
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { queue.addLast(it) }
            }
        }
        return null
    }

    override fun onInterrupt() {
        Log.w(TAG, "Service interrupted")
    }
}
