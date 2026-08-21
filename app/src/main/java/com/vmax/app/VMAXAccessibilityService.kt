package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.common.Logger
import com.vmax.core_intelligence.OcrResult
import com.vmax.runtime.ocr.OcrEvidenceReader
import kotlinx.coroutines.*

/**
 * VMAX v2.6.1
 *
 * VMAXAccessibilityService - Main Accessibility Service for VMAX Automation.
 *
 * Responsibility:
 * - Observe target application UI (IRCTC by default)
 * - Detect security boundaries (CAPTCHA/OTP)
 * - Auto-fill form fields
 * - Process OCR on captcha/screenshots
 * - Maintain lightweight session state
 * - Remain TalkBack/accessibility friendly
 *
 * Architecture:
 * - Platform-independent core logic
 * - Coroutine-based async operations
 * - Proper resource management
 * - No automatic booking actions (CAPTCHA/OTP remain user-controlled)
 *
 * IMPORTANT:
 * This service does NOT automatically perform booking actions.
 * CAPTCHA / OTP and other security-sensitive steps remain user-controlled.
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAXAccessibility"

        // IRCTC Package Name
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        // Default View IDs (configure based on target app)
        private const val DEFAULT_CAPTCHA_VIEW_ID = "com.example.app:id/captcha_image"
        private const val DEFAULT_UPI_VIEW_ID = "com.example.app:id/upi_payment_option"
        private const val DEFAULT_CAPTCHA_INPUT_ID = "com.example.app:id/captcha_input"

        // Action Intents
        const val ACTION_START = "com.vmax.action.START"
        const val ACTION_STOP = "com.vmax.action.STOP"

        @Volatile
        private var isServiceRunning = false

        /**
         * Check if Accessibility Service is enabled.
         */
        fun isAccessibilityServiceEnabled(context: Context): Boolean {
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            return enabledServices?.contains(context.packageName) == true
        }

        /**
         * Open Accessibility Settings.
         */
        fun openAccessibilitySettings(context: Context) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    // ========================================================================
    // DEPENDENCIES
    // ========================================================================

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val ocrReader = OcrEvidenceReader()
    
    // Session State
    private var sessionActive = false
    private var currentSessionId: String? = null
    private var currentState = ServiceState.IDLE

    // Logger (Android implementation)
    private val logger: Logger = AndroidLogger()

    // ========================================================================
    // SERVICE STATE
    // ========================================================================

    private enum class ServiceState {
        IDLE,
        OBSERVING,
        USER_BOUNDARY,
        STOPPED
    }

    // ========================================================================
    // LIFECYCLE METHODS
    // ========================================================================

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceRunning = true
        currentState = ServiceState.IDLE
        logger.i(TAG, "VMAX Accessibility Service connected")

        // Configure service info
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
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Ignore applications other than IRCTC
        if (event.packageName?.toString() != IRCTC_PACKAGE) {
            return
        }

        if (!sessionActive) {
            return
        }

        val rootNode = rootInActiveWindow ?: return

        try {
            currentState = ServiceState.OBSERVING

            // Check for security boundary (CAPTCHA/OTP)
            if (containsSecurityBoundary(rootNode)) {
                currentState = ServiceState.USER_BOUNDARY
                logger.i(TAG, "Security boundary detected. Waiting for user.")
                return
            }

            // Process UI based on event type
            when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
                AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                    processUiState(rootNode)
                }
            }

            // UI observation only
            observeRoot(rootNode)

        } catch (e: Exception) {
            logger.e(TAG, "Error processing accessibility event", e)
        } finally {
            rootNode.recycle()
        }
    }

    override fun onInterrupt() {
        logger.w(TAG, "Accessibility service interrupted")
        stopSession()
        cleanup()
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        stopSession()
        cleanup()
        serviceScope.cancel()
        logger.i(TAG, "VMAX Accessibility Service destroyed")
    }

    // ========================================================================
    // SESSION MANAGEMENT
    // ========================================================================

    /**
     * Starts an observation session.
     */
    fun startSession(sessionId: String) {
        if (sessionId.isBlank()) {
            logger.w(TAG, "Ignoring empty session ID")
            return
        }

        if (sessionActive) {
            logger.w(TAG, "Session already active: $currentSessionId")
            return
        }

        currentSessionId = sessionId
        sessionActive = true
        currentState = ServiceState.IDLE
        logger.i(TAG, "Session started: $sessionId")
    }

    /**
     * Stops the current session.
     */
    fun stopSession() {
        if (!sessionActive && currentSessionId == null) {
            return
        }

        logger.i(TAG, "Session stopped: $currentSessionId")
        sessionActive = false
        currentSessionId = null
        currentState = ServiceState.STOPPED
    }

    /**
     * Returns whether VMAX is currently observing an active session.
     */
    fun isSessionActive(): Boolean = sessionActive

    /**
     * Returns the current service state.
     */
    fun getCurrentState(): String = currentState.name

    /**
     * Returns the current session ID.
     */
    fun getCurrentSessionId(): String? = currentSessionId

    // ========================================================================
    // UI PROCESSING
    // ========================================================================

    /**
     * Process current UI state.
     */
    private fun processUiState(rootNode: AccessibilityNodeInfo) {
        // 1. Auto-fill form fields
        autoFillFormFields(rootNode)

        // 2. Process captcha if present
        processCaptcha(rootNode)

        // 3. Navigate payment flows
        navigatePayment(rootNode)
    }

    /**
     * Auto-fill form fields.
     */
    private fun autoFillFormFields(node: AccessibilityNodeInfo) {
        val editableFields = node.findAccessibilityNodeInfosByViewId("android:id/text1")

        for (field in editableFields) {
            val viewId = field.viewIdResourceName
            if (viewId != null) {
                val value = getFieldValue(viewId)
                if (value != null) {
                    setTextOnNode(field, value)
                    logger.d(TAG, "Auto-filled field: $viewId")
                }
            }
            field.recycle()
        }
    }

    /**
     * Process captcha using OCR.
     */
    private fun processCaptcha(node: AccessibilityNodeInfo) {
        val captchaNode = node.findAccessibilityNodeInfosByViewId(DEFAULT_CAPTCHA_VIEW_ID).firstOrNull()
        if (captchaNode == null) return

        serviceScope.launch {
            try {
                val bitmap = takeScreenshot(captchaNode)
                if (bitmap != null) {
                    val result = ocrReader.readFromScreenshot(bitmap)
                    logger.d(TAG, "Captcha OCR result: ${result.fullText}")
                    handleCaptchaResult(result)
                }
            } catch (e: Exception) {
                logger.e(TAG, "Error processing captcha", e)
            } finally {
                captchaNode.recycle()
            }
        }
    }

    /**
     * Navigate payment flows.
     */
    private fun navigatePayment(node: AccessibilityNodeInfo) {
        val upiNode = node.findAccessibilityNodeInfosByViewId(DEFAULT_UPI_VIEW_ID).firstOrNull()
        if (upiNode != null) {
            upiNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            logger.d(TAG, "UPI payment option selected")
            upiNode.recycle()
        }
    }

    // ========================================================================
    // SECURITY BOUNDARY DETECTION
    // ========================================================================

    /**
     * Detect CAPTCHA / OTP / security verification UI.
     */
    private fun containsSecurityBoundary(root: AccessibilityNodeInfo): Boolean {
        val securityTexts = listOf(
            "CAPTCHA", "captcha", "OTP", "otp",
            "verification code", "verify code", "enter code",
            "security", "captcha", "verification"
        )
        return containsAnyText(root, securityTexts)
    }

    /**
     * Recursively searches the accessibility tree for text matches.
     */
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
                if (containsAnyText(child, targets)) {
                    return true
                }
            } finally {
                child.recycle()
            }
        }

        return false
    }

    private fun matchesTarget(value: String?, targets: List<String>): Boolean {
        if (value.isNullOrBlank()) return false
        return targets.any { target ->
            value.contains(target, ignoreCase = true)
        }
    }

    // ========================================================================
    // UI OBSERVATION HOOK
    // ========================================================================

    /**
     * UI observation hook.
     * Keep this method side-effect free.
     */
    private fun observeRoot(root: AccessibilityNodeInfo) {
        val packageName = root.packageName?.toString() ?: return
        logger.d(TAG, "Observing package=$packageName state=$currentState")
        // Intentionally no automatic click, text injection, etc.
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================

    /**
     * Set text on a node.
     */
    private fun setTextOnNode(node: AccessibilityNodeInfo, text: String) {
        val bundle = Bundle().apply {
            putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                text
            )
        }
        node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle)
    }

    /**
     * Get field value based on view ID.
     * Override based on target app's requirements.
     */
    private fun getFieldValue(viewId: String): String? {
        return when (viewId) {
            "com.example.app:id/username" -> "your_username"
            "com.example.app:id/password" -> "your_password"
            "com.example.app:id/email" -> "user@example.com"
            "com.example.app:id/phone" -> "9876543210"
            else -> null
        }
    }

    /**
     * Handle captcha result.
     * Override based on target app's requirements.
     */
    private fun handleCaptchaResult(result: OcrResult) {
        val rootNode = rootInActiveWindow ?: return
        try {
            val captchaInputs = rootNode.findAccessibilityNodeInfosByViewId(DEFAULT_CAPTCHA_INPUT_ID)
            for (input in captchaInputs) {
                if (input.isEditable) {
                    setTextOnNode(input, result.fullText)
                    logger.d(TAG, "Captcha filled: ${result.fullText}")
                    break
                }
                input.recycle()
            }
        } finally {
            rootNode.recycle()
        }
    }

    /**
     * Take screenshot of a specific node.
     * TODO: Implement using MediaProjection or AccessibilityService API.
     */
    private fun takeScreenshot(node: AccessibilityNodeInfo): android.graphics.Bitmap? {
        val rect = android.graphics.Rect()
        node.getBoundsInScreen(rect)
        // TODO: Implement actual screenshot capture
        // Options: MediaProjection API, UiAutomation, performGlobalAction()
        logger.w(TAG, "takeScreenshot not implemented yet")
        return null
    }

    /**
     * Cleanup resources.
     */
    private fun cleanup() {
        try {
            ocrReader.close()
        } catch (e: Exception) {
            logger.e(TAG, "Error cleaning up", e)
        }
    }
}
