package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.common.Logger
import com.vmax.workflow.WorkflowController // ✅ 1. IMPORT: core-workflow module से असली Controller

/**
 * VMAX v2.6.1 - Final Production Ready
 *
 * Responsibilities:
 * - Maintain service/session state.
 * - Observe the IRCTC accessibility tree.
 * - Detect user/security boundaries (CAPTCHA/OTP).
 * - Delegate actual automation (Click/Type) to WorkflowController.
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
            )
            if (enabledServices.isNullOrBlank()) return false
            return enabledServices.split(':').any { it.contains(context.packageName, ignoreCase = true) }
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
    
    // मान लें कि AndroidLogger आपकी project में पहले से मौजूद है
    private val logger: Logger = AndroidLogger() 

    // ✅ 2. INITIALIZE: core-workflow वाले Controller का Reference (No duplicate class)
    private var workflowController: WorkflowController? = null

    private enum class ServiceState {
        IDLE, OBSERVING, USER_BOUNDARY, STOPPED
    }

    // ---------------------------------------------------------------------
    // Android AccessibilityService lifecycle
    // ---------------------------------------------------------------------

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

        // ✅ "WorkflowController has not been initialized" ERROR FIX:
        // यहाँ असली module को initialize करें। 
        // (नोट: अगर आपके core-workflow में constructor अलग है, जैसे WorkflowController(), तो 'this' हटा दें)
        workflowController = WorkflowController(this)
        logger.info(TAG, "✅ WorkflowController successfully initialized and linked.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val sessionId = intent.getStringExtra("SESSION_ID") ?: "vmax_session_${System.currentTimeMillis()}"
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

        // केवल IRCTC पर काम करें
        if (event.packageName?.toString() != IRCTC_PACKAGE) return

        // जब तक session start नहीं हुआ, कुछ न करें
        if (!sessionActive) return

        val rootNode = rootInActiveWindow ?: return

        try {
            /*
             * Security boundary: CAPTCHA / OTP / verification
             * यह आपका original सुरक्षा कोड है, यह बिल्कुल वैसा ही रहेगा।
             */
            if (containsSecurityBoundary(rootNode)) {
                currentState = ServiceState.USER_BOUNDARY
                logger.info(TAG, "⚠️ Security boundary detected. Automation paused. Waiting for user.")
                
                // Workflow को बताएं कि रुक जाए (अगर आपके controller में pause method है)
                workflowController?.pauseAutomation() 
                return
            }

            // अगर user ने CAPTCHA हटा दिया, तो वापस Observing state में आ जाएं
            currentState = ServiceState.OBSERVING

            when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
                AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                    observeRoot(rootNode) // ✅ यहाँ से असली automation trigger होगा
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
        workflowController = null // Memory cleanup
        logger.info(TAG, "VMAX Accessibility Service destroyed")
        super.onDestroy()
    }

    // ---------------------------------------------------------------------
    // Session management
    // ---------------------------------------------------------------------

    fun startSession(sessionId: String) {
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
        
        // ✅ Session start होने पर WorkflowController को भी notify करें
        workflowController?.startWorkflow(sessionId)
        
        logger.info(TAG, "✅ Session started: $sessionId. Automation is now ACTIVE.")
    }

    fun stopSession() {
        if (!sessionActive && currentSessionId == null) return

        workflowController?.stopWorkflow()
        
        logger.info(TAG, "Session stopped: $currentSessionId")
        sessionActive = false
        currentSessionId = null
        currentState = ServiceState.STOPPED
    }

    fun isSessionActive(): Boolean = sessionActive
    fun getCurrentState(): String = currentState.name
    fun getCurrentSessionId(): String? = currentSessionId

    // ---------------------------------------------------------------------
    // Security boundary detection (Original Code - Unchanged)
    // ---------------------------------------------------------------------

    private fun containsSecurityBoundary(root: AccessibilityNodeInfo): Boolean {
        val securityTexts = listOf(
            "CAPTCHA", "OTP", "verification code", "verify code", "enter code", "security verification"
        )
        return containsAnyText(root, securityTexts)
    }

    private fun containsAnyText(node: AccessibilityNodeInfo?, targets: List<String>): Boolean {
        if (node == null) return false

        val text = node.text?.toString()
        val description = node.contentDescription?.toString()

        if (matchesTarget(text, targets) || matchesTarget(description, targets)) {
            return true
        }

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
    // Observation & Automation Trigger (THE MISSING LINK)
    // ---------------------------------------------------------------------

    private fun observeRoot(root: AccessibilityNodeInfo) {
        if (!sessionActive) return

        val packageName = root.packageName?.toString() ?: return
        logger.debug(TAG, "Observing package=$packageName state=$currentState")

        // ✅ 3. EXECUTE: असली काम यहाँ होगा। 
        // rootNode को WorkflowController को भेजें ताकि वह ActionExecutor का उपयोग करके Click/Type कर सके।
        // (नोट: 'processScreen' वह method name है। अगर आपके core-workflow में इसका नाम 'handleNode' या 'executeStep' है, 
        // तो कृपया यहाँ उस exact name से बदल दें)
        workflowController?.processScreen(root)
    }

    // ---------------------------------------------------------------------
    // Cleanup
    // ---------------------------------------------------------------------

    private fun cleanup() {
        logger.debug(TAG, "Accessibility service cleanup completed")
    }
}
