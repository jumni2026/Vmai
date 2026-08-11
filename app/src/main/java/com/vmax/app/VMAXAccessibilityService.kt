package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

// Real Project Contract Import (Based on CI Evidence)
import com.vmax.action.ActionExecutor

class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_CLEAN_SERVICE"
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
        USER_BOUNDARY,
        STOPPED
    }

    private var currentState = State.IDLE
    private lateinit var executor: AndroidActionExecutor

    override fun onServiceConnected() {
        super.onServiceConnected()
        
        // ✅ CORRECT: Instantiate the concrete class, not the Interface
        executor = AndroidActionExecutor(this)
        Log.i(TAG, "VMAX Service Connected with AndroidActionExecutor")
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
        // In this clean version, we are just detecting UI elements and preparing to dispatch.
        // Real CLICK / SET_TEXT actions will be added in the next step.
        when (currentState) {
            State.IDLE -> detectAndDispatch(root)
            else -> { /* Awaiting event/evidence */ }
        }
    }

    private fun detectAndDispatch(root: AccessibilityNodeInfo) {
        // This method just detects UI Elements and prepares for CLICK.
        // The actual CLICK dispatch will be added immediately after this step.
        findEditableNodeByEvidence(root, EVIDENCE_FROM)?.let {
            Log.i(TAG, "FROM field detected.")
        }
        
        findEditableNodeByEvidence(root, EVIDENCE_TO)?.let {
            Log.i(TAG, "TO field detected.")
        }
        
        findNodeByEvidence(root, EVIDENCE_DATE, isClickable = true)?.let {
            Log.i(TAG, "DATE field detected.")
        }
        
        findNodeByEvidence(root, EVIDENCE_SEARCH, isClickable = true)?.let {
            Log.i(TAG, "SEARCH button detected.")
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
