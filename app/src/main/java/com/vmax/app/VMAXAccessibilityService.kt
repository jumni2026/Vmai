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

/**
 * VMAX v2.6.1
 *
 * Accessibility Service
 *
 * Responsibilities:
 * - Maintain service/session state.
 * - Observe the IRCTC accessibility tree.
 * - Detect user/security boundaries.
 * - Stop processing when CAPTCHA/OTP/verification is present.
 * - Provide a stable bridge for the rest of the VMAX runtime.
 *
 * Security boundary:
 * CAPTCHA, OTP and verification steps remain user-controlled.
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {

        private const val TAG = "VMAXAccessibility"

        /**
         * IRCTC Rail Connect package.
         */
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        /**
         * External commands.
         */
        const val ACTION_START = "com.vmax.action.START"
        const val ACTION_STOP = "com.vmax.action.STOP"

        @Volatile
        private var isServiceRunning = false

        /**
         * Checks whether this application's accessibility service
         * appears in the enabled accessibility services list.
         */
        fun isAccessibilityServiceEnabled(
            context: Context
        ): Boolean {

            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )

            if (enabledServices.isNullOrBlank()) {
                return false
            }

            return enabledServices
                .split(':')
                .any { serviceName ->
                    serviceName.contains(
                        context.packageName,
                        ignoreCase = true
                    )
                }
        }

        /**
         * Opens Android Accessibility Settings.
         */
        fun openAccessibilitySettings(context: Context) {

            val intent = Intent(
                Settings.ACTION_ACCESSIBILITY_SETTINGS
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        }

        /**
         * Returns whether the service instance is currently connected.
         */
        fun isRunning(): Boolean {
            return isServiceRunning
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

    // ---------------------------------------------------------------------
    // Android AccessibilityService lifecycle
    // ---------------------------------------------------------------------

    override fun onServiceConnected() {
        super.onServiceConnected()

        isServiceRunning = true
        currentState = ServiceState.IDLE

        logger.info(
            TAG,
            "VMAX Accessibility Service connected"
        )

        serviceInfo = AccessibilityServiceInfo().apply {

            eventTypes = AccessibilityEvent.TYPES_ALL_MASK

            feedbackType =
                AccessibilityServiceInfo.FEEDBACK_GENERIC

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

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {

        if (event == null) {
            return
        }

        /*
         * Ignore applications other than IRCTC.
         */
        if (event.packageName?.toString() != IRCTC_PACKAGE) {
            return
        }

        /*
         * Do nothing until a VMAX session has explicitly started.
         */
        if (!sessionActive) {
            return
        }

        val rootNode = rootInActiveWindow ?: return

        try {

            currentState = ServiceState.OBSERVING

            /*
             * Security boundary.
             *
             * CAPTCHA / OTP / verification must remain
             * under explicit user control.
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

        } catch (exception: Exception) {

            /*
             * Logger.error() accepts exactly:
             * error(tag, message)
             *
             * Therefore the exception is included in the message.
             */
            logger.error(
                TAG,
                "Error processing accessibility event: ${exception.message}"
            )

        } finally {

            /*
             * rootInActiveWindow is owned by AccessibilityService.
             * Do not explicitly recycle it here.
             */
        }
    }

    override fun onInterrupt() {

        logger.warn(
            TAG,
            "Accessibility service interrupted"
        )

        stopSession()
        cleanup()
    }

    override fun onDestroy() {

        isServiceRunning = false

        stopSession()
        cleanup()

        logger.info(
            TAG,
            "VMAX Accessibility Service destroyed"
        )

        super.onDestroy()
    }

    // ---------------------------------------------------------------------
    // Session management
    // ---------------------------------------------------------------------

    fun startSession(sessionId: String) {

        if (sessionId.isBlank()) {

            logger.warn(
                TAG,
                "Ignoring empty session ID"
            )

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

        logger.info(
            TAG,
            "Session started: $sessionId"
        )
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

    // ---------------------------------------------------------------------
    // Security boundary detection
    // ---------------------------------------------------------------------

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

        return containsAnyText(
            root,
            securityTexts
        )
    }

    private fun containsAnyText(
        node: AccessibilityNodeInfo?,
        targets: List<String>
    ): Boolean {

        if (node == null) {
            return false
        }

        val text = node.text?.toString()

        val description =
            node.contentDescription?.toString()

        if (
            matchesTarget(text, targets) ||
            matchesTarget(description, targets)
        ) {
            return true
        }

        for (index in 0 until node.childCount) {

            val child = node.getChild(index)
                ?: continue

            try {

                if (
                    containsAnyText(
                        child,
                        targets
                    )
                ) {
                    return true
                }

            } finally {

                /*
                 * Child nodes obtained with getChild()
                 * should be recycled after use.
                 */
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

    // ---------------------------------------------------------------------
    // Observation
    // ---------------------------------------------------------------------

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

    // ---------------------------------------------------------------------
    // Cleanup
    // ---------------------------------------------------------------------

    private fun cleanup() {

        /*
         * Keep cleanup idempotent.
         *
         * Add only non-sensitive resource cleanup here.
         */
        logger.debug(
            TAG,
            "Accessibility service cleanup completed"
        )
    }
}
