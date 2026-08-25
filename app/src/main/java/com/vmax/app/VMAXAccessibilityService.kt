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

            return enabledServices
                ?.split(':')
                ?.any { it.contains(context.packageName, ignoreCase = true) }
                == true
        }

        fun openAccessibilitySettings(context: Context) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

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

            flags =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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

        if (event.packageName?.toString() != IRCTC_PACKAGE) {
            return
        }

        if (!sessionActive) {
            return
        }

        val rootNode = rootInActiveWindow ?: return

        try {
            currentState = ServiceState.OBSERVING

            /*
             * Security boundary:
             * CAPTCHA, OTP and verification screens are user-controlled.
             * The service deliberately stops processing when detected.
             */
            if (containsSecurityBoundary(rootNode)) {
                currentState = ServiceState.USER_BOUNDARY

                logger.info(
                    TAG,
                    "Security boundary detected. Waiting for user."
                )

                return
            }

            when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
                AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                    observeRoot(rootNode)
                }
            }

        } catch (e: Exception) {
            logger.error(
                TAG,
                "Error processing accessibility event: ${e.message}"
            )
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

        logger.info(TAG, "VMAX Accessibility Service destroyed")
    }

    fun startSession(sessionId: String) {
        if (sessionId.isBlank()) {
            logger.warn(TAG, "Ignoring empty session ID")
            return
        }

        if (sessionActive) {
            logger.warn(
                TAG,
                "Session already active: $currentSessionId"
            )
            return
        }

        currentSessionId = sessionId
        sessionActive = true
        currentState = ServiceState.IDLE

        logger.info(TAG, "Session started: $sessionId")
    }

    fun stopSession() {
        if (!sessionActive && currentSessionId == null) {
            return
        }

        logger.info(
            TAG,
            "Session stopped: $currentSessionId"
        )

        sessionActive = false
        currentSessionId = null
        currentState = ServiceState.STOPPED
    }

    fun isSessionActive(): Boolean {
        return sessionActive
    }

    fun getCurrentState(): String {
        return currentState.name
    }

    fun getCurrentSessionId(): String? {
        return currentSessionId
    }

    private fun containsSecurityBoundary(
        root: AccessibilityNodeInfo
    ): Boolean {
        val securityTexts = listOf(
            "CAPTCHA",
            "OTP",
            "verification code",
            "verify code",
            "enter code",
            "security verification"
        )

        return containsAnyText(root, securityTexts)
    }

    private fun containsAnyText(
        node: AccessibilityNodeInfo?,
        targets: List<String>
    ): Boolean {
        if (node == null) return false

        val text = node.text?.toString()
        val description = node.contentDescription?.toString()

        if (
            matchesTarget(text, targets) ||
            matchesTarget(description, targets)
        ) {
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

    private fun matchesTarget(
        value: String?,
        targets: List<String>
    ): Boolean {
        if (value.isNullOrBlank()) {
            return false
        }

        return targets.any { target ->
            value.contains(
                target,
                ignoreCase = true
            )
        }
    }

    private fun observeRoot(
        root: AccessibilityNodeInfo
    ) {
        val packageName =
            root.packageName?.toString()
                ?: return

        logger.debug(
            TAG,
            "Observing package=$packageName state=$currentState"
        )
    }

    private fun cleanup() {
        /*
         * Keep cleanup idempotent.
         * Add non-sensitive resource cleanup here when required.
         */
        logger.debug(TAG, "Accessibility service cleanup completed")
    }
}
