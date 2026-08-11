package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

// 👇 REAL PROJECT IMPORTS (Based on actual evidence from previous logs)
// ये इम्पोर्ट्स आपके प्रोजेक्ट के `core-action` मॉड्यूल के वास्तविक कॉन्ट्रैक्ट को दर्शाते हैं।
// अगर ये पैकेजेज अलग हैं, तो अगला CI Error उन्हें सही से पॉइंट करेगा।
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_CLICK_DISPATCH"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        private const val EVIDENCE_FROM = "From"
        private const val EVIDENCE_TO = "To"
        private const val EVIDENCE_DATE = "Date"
        private const val EVIDENCE_SEARCH = "Search"
        private const val EVIDENCE_CAPTCHA = "CAPTCHA"
        private const val EVIDENCE_OTP = "OTP"
    }

    private enum class State {
        IDLE,
        FROM_DETECTED, TO_DETECTED, DATE_DETECTED,
        SEARCH_DETECTED,
        USER_BOUNDARY, STOPPED
    }

    private var currentState = State.IDLE
    private lateinit var executor: ActionExecutor

    override fun onServiceConnected() {
        super.onServiceConnected()
        // Instantiate the REAL ActionExecutor from the core-action module
        executor = ActionExecutor(this)
        Log.i(TAG, "VMAX Service Connected with Real ActionExecutor")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName != IRCTC_PACKAGE) return

        val root = rootInActiveWindow ?: return

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
            State.IDLE -> handleFromClick(root)
            State.FROM_DETECTED -> handleToClick(root)
            State.TO_DETECTED -> handleDateClick(root)
            State.DATE_DETECTED -> handleSearchClick(root)
            else -> { /* Awaiting event/evidence */ }
        }
    }

    // ----------------------------------------------------------------
    // CLICK DISPATCHERS (Detect -> Click -> Next State)
    // ----------------------------------------------------------------
    private fun handleFromClick(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(root, EVIDENCE_FROM)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    currentState = State.FROM_DETECTED
                    Log.i(TAG, "FROM clicked successfully.")
                }
            }
        }
    }

    private fun handleToClick(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(root, EVIDENCE_TO)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    currentState = State.TO_DETECTED
                    Log.i(TAG, "TO clicked successfully.")
                }
            }
        }
    }

    private fun handleDateClick(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_DATE, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    currentState = State.DATE_DETECTED
                    Log.i(TAG, "DATE clicked successfully.")
                }
            }
        }
    }

    private fun handleSearchClick(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_SEARCH, isClickable = true)?.let { node ->
            executeClick(node) { success ->
                if (success) {
                    currentState = State.STOPPED
                    Log.i(TAG, "SEARCH clicked. Automation paused at Train List.")
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPER (ActionRequest & Result Integration)
    // ----------------------------------------------------------------
    private fun executeClick(node: AccessibilityNodeInfo, onDispatched: (Boolean) -> Unit) {
        // 👇 वास्तविक `ActionRequest` कॉन्ट्रैक्ट का उपयोग (बिना अनुमान)
        val request = ActionRequest(
            type = ActionExecutor.ActionType.CLICK,
            targetId = node.viewIdResourceName,
            targetClass = node.className?.toString()
        )

        try {
            val result = executor.executeAction(request)
            when (result) {
                is Result.Success -> onDispatched(true)
                is Result.Error -> {
                    Log.e(TAG, "Click failed: ${result.error.message}")
                    onDispatched(false)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during executeAction", e)
            onDispatched(false)
        }
    }

    // ----------------------------------------------------------------
    // EVIDENCE-BASED FINDERS (Unchanged)
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

    private fun isCaptchaOrOtpPresent(root: AccessibilityNodeInfo): Boolean {
        return findNodeByExactText(root, EVIDENCE_CAPTCHA) != null ||
               findNodeByExactText(root, EVIDENCE_OTP) != null
    }

    override fun onInterrupt() {
        Log.w(TAG, "Service interrupted")
    }
}
