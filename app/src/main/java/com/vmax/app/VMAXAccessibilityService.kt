package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.AndroidActionExecutor
import com.vmax.common.Logger
import com.vmax.workflow.WorkflowController
import com.vmax.workflow.WorkflowState
import java.util.ArrayDeque

/**
 * VMAX v2.6.1 - Final Production Ready
 * 
 * Role: The "Eyes and Hands" of the automation.
 * - Observes the screen.
 * - Enforces Security Boundaries (CAPTCHA/OTP).
 * - Coordinates between WorkflowController (Brain) and AndroidActionExecutor (Muscle).
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAXAccessibility"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        const val ACTION_START = "com.vmax.action.START"
        const val ACTION_STOP = "com.vmax.action.STOP"

        @Volatile
        private var isServiceRunning = false

        fun isAccessibilityServiceEnabled(context: Context): Boolean {
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false
            
            return enabledServices.split(':').any { 
                it.contains(context.packageName, ignoreCase = true) 
            }
        }

        fun openAccessibilitySettings(context: Context) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        fun isRunning(): Boolean = isServiceRunning
    }

    private var sessionActive = false
    private var currentSessionId: String? = null
    private var currentState = ServiceState.IDLE
    
    private val logger: Logger = AndroidLogger()

    // ✅ BRIDGE 1: Singleton WorkflowController (The Brain)
    private val workflowController: WorkflowController 
        get() = WorkflowController.getInstance()

    // ✅ BRIDGE 2: AndroidActionExecutor (The Muscle)
    private lateinit var actionExecutor: AndroidActionExecutor

    private enum class ServiceState {
        IDLE, OBSERVING, USER_BOUNDARY, STOPPED
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceRunning = true
        currentState = ServiceState.IDLE

        logger.info(TAG, "VMAX Accessibility Service connected")

        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
            } else {
                AccessibilityServiceInfo.DEFAULT
            }
            notificationTimeout = 100
        }

        // Initialize the Executor with the current Service context
        actionExecutor = AndroidActionExecutor(this)
        logger.info(TAG, "✅ AndroidActionExecutor initialized.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val sessionId = intent.getStringExtra("SESSION_ID") ?: "vmax_session_${System.currentTimeMillis()}"
                
                // Note: In a real scenario, you should pass PassengerDetails via Intent or a shared Repository.
                // For now, we ensure the session starts. The actual details should be injected before calling startWorkflow.
                startSession(sessionId) 
            }
            ACTION_STOP -> {
                stopSession()
            }
        }
        return START_STICKY
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.packageName?.toString() != IRCTC_PACKAGE) return
        if (!sessionActive) return

        val rootNode = rootInActiveWindow ?: return

        try {
            currentState = ServiceState.OBSERVING

            // 🛡️ SECURITY BOUNDARY: Unchanged and fully intact
            if (containsSecurityBoundary(rootNode)) {
                currentState = ServiceState.USER_BOUNDARY
                workflowController.updateState(WorkflowState.USER_BOUNDARY)
                logger.info(TAG, "⚠️ Security boundary (CAPTCHA/OTP) detected. Automation PAUSED.")
                return // Stop processing, wait for user
            }

            when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
                AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                    observeAndAct(rootNode) // ✅ THE MISSING LINK
                }
            }
        } catch (exception: Exception) {
            logger.error(TAG, "Error processing accessibility event: ${exception.message}")
        }
    }

    override fun onInterrupt() {
        logger.warn(TAG, "Accessibility service interrupted")
        stopSession()
        cleanup()
    }

    override fun onDestroy() {
        isServiceRunning = false
        stopSession()
        cleanup()
        logger.info(TAG, "VMAX Accessibility Service destroyed")
        super.onDestroy()
    }

    // ---------------------------------------------------------------------
    // Session Management
    // ---------------------------------------------------------------------

    fun startSession(sessionId: String, details: WorkflowController.PassengerDetails? = null) {
        if (sessionId.isBlank()) {
            logger.warn(TAG, "Ignoring empty session ID")
            return
        }
        if (sessionActive) {
            logger.warn(TAG, "Session already active: $currentSessionId")
            return
        }

        currentSessionId = sessionId
        sessionActive = true
        currentState = ServiceState.IDLE

        // Start the workflow in the Controller if details are provided
        if (details != null) {
            val success = workflowController.startWorkflow(details, sessionId)
            if (success) {
                logger.info(TAG, "✅ Session started & Workflow triggered: $sessionId")
            } else {
                logger.error(TAG, "❌ Failed to start workflow in Controller")
            }
        } else {
            logger.info(TAG, "✅ Session started (Waiting for PassengerDetails): $sessionId")
        }
    }

    fun stopSession() {
        if (!sessionActive && currentSessionId == null) return

        workflowController.stopWorkflow()
        logger.info(TAG, "Session stopped: $currentSessionId")
        sessionActive = false
        currentSessionId = null
        currentState = ServiceState.STOPPED
    }

    fun isSessionActive(): Boolean = sessionActive
    fun getCurrentState(): String = currentState.name
    fun getCurrentSessionId(): String? = currentSessionId

    // ---------------------------------------------------------------------
    // Security boundary detection (Unchanged)
    // ---------------------------------------------------------------------

    private fun containsSecurityBoundary(root: AccessibilityNodeInfo): Boolean {
        val securityTexts = listOf("CAPTCHA", "OTP", "verification code", "verify code", "enter code", "security verification")
        return containsAnyText(root, securityTexts)
    }

    private fun containsAnyText(node: AccessibilityNodeInfo?, targets: List<String>): Boolean {
        if (node == null) return false
        val text = node.text?.toString()
        val description = node.contentDescription?.toString()

        if (matchesTarget(text, targets) || matchesTarget(description, targets)) return true

        for (index in 0 until node.childCount) {
            val child = node.getChild(index) ?: continue
            try {
                if (containsAnyText(child, targets)) return true
            } finally {
                child.recycle()
            }
        }
        return false
    }

    private fun matchesTarget(value: String?, targets: List<String>): Boolean {
        if (value.isNullOrBlank()) return false
        return targets.any { target -> value.contains(target, ignoreCase = true) }
    }

    // ---------------------------------------------------------------------
    // 🧠 OBSERVATION & ACTION COORDINATION (THE CORE AUTOMATION)
    // ---------------------------------------------------------------------

    private fun observeAndAct(root: AccessibilityNodeInfo) {
        if (!sessionActive) return
        
        // Check if the brain (WorkflowController) says we are allowed to act
        if (!workflowController.isActive()) {
            Log.d(TAG, "Workflow is not active (State: ${workflowController.getCurrentState()}). Skipping action.")
            return
        }

        val currentWorkflowState = workflowController.getCurrentState()
        Log.d(TAG, "Acting on screen. Workflow State: $currentWorkflowState")

        when (currentWorkflowState) {
            WorkflowState.RUNNING, WorkflowState.CONFIGURED -> {
                // Example Step 1: Look for "Search" or "Book" or "Find Trains"
                val searchKeywords = listOf("search", "find trains", "book")
                val targetId = findClickableNodeIdByText(root, searchKeywords)

                if (targetId != null) {
                    logger.info(TAG, "🎯 Found target: $targetId. Executing Click...")
                    
                    // ✅ HANDS: Tell the Executor to click this specific ID
                    val result = actionExecutor.executeClick(targetId)
                    
                    if (result is com.vmax.common.Result.Success) {
                        logger.info(TAG, "✅ Click successful!")
                        // Optional: Update state to next step if needed
                        // workflowController.updateState(WorkflowState.PASSENGER_NAME_TYPED) 
                    } else {
                        logger.error(TAG, "❌ Click failed: ${(result as com.vmax.common.Result.Error).error.message}")
                    }
                }
            }
            
            WorkflowState.USER_BOUNDARY -> {
                // Do nothing, waiting for user to solve CAPTCHA
            }
            
            else -> {
                Log.d(TAG, "No action defined for state: $currentWorkflowState")
            }
        }
    }

    // ---------------------------------------------------------------------
    // 🔍 Helper: Find Node ID by Text (BFS Algorithm)
    // ---------------------------------------------------------------------
    
    private fun findClickableNodeIdByText(root: AccessibilityNodeInfo, keywords: List<String>): String? {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(AccessibilityNodeInfo.obtain(root))

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            
            val text = node.text?.toString()?.lowercase() ?: ""
            val desc = node.contentDescription?.toString()?.lowercase() ?: ""
            
            val isMatch = keywords.any { keyword -> 
                text.contains(keyword) || desc.contains(keyword) 
            }

            if (isMatch) {
                // If the node itself is clickable, use its ID
                if (node.isClickable && node.viewIdResourceName != null) {
                    val id = node.viewIdResourceName
                    node.recycle()
                    clearQueue(queue)
                    return id
                }
                
                // If not, check if parent is clickable
                var parent = node.parent
                while (parent != null) {
                    if (parent.isClickable && parent.viewIdResourceName != null) {
                        val id = parent.viewIdResourceName
                        parent.recycle()
                        node.recycle()
                        clearQueue(queue)
                        return id
                    }
                    val temp = parent.parent
                    parent.recycle()
                    parent = temp
                }
            }

            for (i in 0 until node.childCount) {
                val child = node.getChild(i)
                if (child != null) queue.addLast(child)
            }
            node.recycle()
        }
        return null
    }

    private fun clearQueue(queue: ArrayDeque<AccessibilityNodeInfo>) {
        while (queue.isNotEmpty()) {
            queue.removeFirst().recycle()
        }
    }

    // ---------------------------------------------------------------------
    // Cleanup
    // ---------------------------------------------------------------------

    private fun cleanup() {
        logger.debug(TAG, "Accessibility service cleanup completed")
    }
}
