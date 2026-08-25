package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.common.Logger
import com.vmax.core_intelligence.OcrResult
import com.vmax.runtime.ocr.OcrEvidenceReader
import kotlinx.coroutines.*

class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAXAccessibility"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"
        private const val DEFAULT_CAPTCHA_VIEW_ID = "com.example.app:id/captcha_image"
        private const val DEFAULT_UPI_VIEW_ID = "com.example.app:id/upi_payment_option"
        private const val DEFAULT_CAPTCHA_INPUT_ID = "com.example.app:id/captcha_input"

        const val ACTION_START = "com.vmax.action.START"
        const val ACTION_STOP = "com.vmax.action.STOP"

        @Volatile
        private var isServiceRunning = false

        fun isAccessibilityServiceEnabled(context: Context): Boolean {
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            return enabledServices?.contains(context.packageName) == true
        }

        fun openAccessibilitySettings(context: Context) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val ocrReader = OcrEvidenceReader()

    private var sessionActive = false
    private var currentSessionId: String? = null
    private var currentState = ServiceState.IDLE

    private val logger: Logger = AndroidLogger()

    private enum class ServiceState {
        IDLE,
        OBSERVING,
        USER_BOUNDARY,
        STOPPED
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
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.packageName?.toString() != IRCTC_PACKAGE) return
        if (!sessionActive) return

        val rootNode = rootInActiveWindow ?: return

        try {
            currentState = ServiceState.OBSERVING

            if (containsSecurityBoundary(rootNode)) {
                currentState = ServiceState.USER_BOUNDARY
                logger.info(TAG, "Security boundary detected. Waiting for user.")
                return
            }

            when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
                AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                    processUiState(rootNode)
                }
            }

            observeRoot(rootNode)

        } catch (e: Exception) {
            logger.error(TAG, "Error processing accessibility event", e)
        } finally {
            rootNode.recycle()
        }
    }

    override fun onInterrupt() {
        logger.warn(TAG, "Accessibility service interrupted")
        stopSession()
        cleanup()
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        stopSession()
        cleanup()
        serviceScope.cancel()
        logger.info(TAG, "VMAX Accessibility Service destroyed")
    }

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
        logger.info(TAG, "Session started: $sessionId")
    }

    fun stopSession() {
        if (!sessionActive && currentSessionId == null) return

        logger.info(TAG, "Session stopped: $currentSessionId")
        sessionActive = false
        currentSessionId = null
        currentState = ServiceState.STOPPED
    }

    fun isSessionActive(): Boolean = sessionActive

    fun getCurrentState(): String = currentState.name

    fun getCurrentSessionId(): String? = currentSessionId

    private fun processUiState(rootNode: AccessibilityNodeInfo) {
        autoFillFormFields(rootNode)
        processCaptcha(rootNode)
        navigatePayment(rootNode)
    }

    private fun autoFillFormFields(node: AccessibilityNodeInfo) {
        val editableFields = node.findAccessibilityNodeInfosByViewId("android:id/text1")
        for (field in editableFields) {
            val viewId = field.viewIdResourceName
            if (viewId != null) {
                val value = getFieldValue(viewId)
                if (value != null) {
                    setTextOnNode(field, value)
                    logger.debug(TAG, "Auto-filled field: $viewId")
                }
            }
            field.recycle()
        }
    }

    private fun processCaptcha(node: AccessibilityNodeInfo) {
        val captchaNode = node.findAccessibilityNodeInfosByViewId(DEFAULT_CAPTCHA_VIEW_ID).firstOrNull()
        if (captchaNode == null) return

        serviceScope.launch {
            try {
                val bitmap = takeScreenshot(captchaNode)
                if (bitmap != null) {
                    val result = ocrReader.readFromScreenshot(bitmap)
                    logger.debug(TAG, "Captcha OCR result: ${result.fullText}")
                    handleCaptchaResult(result)
                }
            } catch (e: Exception) {
                logger.error(TAG, "Error processing captcha", e)
            } finally {
                captchaNode.recycle()
            }
        }
    }

    private fun navigatePayment(node: AccessibilityNodeInfo) {
        val upiNode = node.findAccessibilityNodeInfosByViewId(DEFAULT_UPI_VIEW_ID).firstOrNull()
        if (upiNode != null) {
            upiNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            logger.debug(TAG, "UPI payment option selected")
            upiNode.recycle()
        }
    }

    private fun containsSecurityBoundary(root: AccessibilityNodeInfo): Boolean {
        val securityTexts = listOf(
            "CAPTCHA", "captcha", "OTP", "otp",
            "verification code", "verify code", "enter code",
            "security", "captcha", "verification"
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

    private fun observeRoot(root: AccessibilityNodeInfo) {
        val packageName = root.packageName?.toString() ?: return
        logger.debug(TAG, "Observing package=$packageName state=$currentState")
    }

    private fun setTextOnNode(node: AccessibilityNodeInfo, text: String) {
        val bundle = Bundle().apply {
            putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                text
            )
        }
        node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle)
    }

    private fun getFieldValue(viewId: String): String? {
        return when (viewId) {
            "com.example.app:id/username" -> "your_username"
            "com.example.app:id/password" -> "your_password"
            "com.example.app:id/email" -> "user@example.com"
            "com.example.app:id/phone" -> "9876543210"
            else -> null
        }
    }

    private fun handleCaptchaResult(result: OcrResult) {
        val rootNode = rootInActiveWindow ?: return
        try {
            val captchaInputs = rootNode.findAccessibilityNodeInfosByViewId(DEFAULT_CAPTCHA_INPUT_ID)
            for (input in captchaInputs) {
                if (input.isEditable) {
                    setTextOnNode(input, result.fullText)
                    logger.debug(TAG, "Captcha filled: ${result.fullText}")
                    break
                }
                input.recycle()
            }
        } finally {
            rootNode.recycle()
        }
    }

    private fun takeScreenshot(node: AccessibilityNodeInfo): android.graphics.Bitmap? {
        val rect = android.graphics.Rect()
        node.getBoundsInScreen(rect)
        logger.warn(TAG, "takeScreenshot not implemented yet")
        return null
    }

    private fun cleanup() {
        try {
            ocrReader.close()
        } catch (e: Exception) {
            logger.error(TAG, "Error cleaning up", e)
        }
    }
}
