package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * VMAX v2.6.1
 *
 * File:
 * VMAXAccessibilityService.kt
 *
 * Responsibility:
 * - Observe Accessibility events.
 * - Observe the currently visible UI.
 * - Maintain service/session lifecycle.
 * - Detect hard user/security boundaries.
 * - Provide safe UI-node observation helpers.
 *
 * Architecture boundary:
 *
 * VMAXAccessibilityService
 *          |
 *          +-- observes Android Accessibility UI
 *          |
 *          +-- detects hard safety boundaries
 *          |
 *          +-- exposes UI observation helpers
 *          |
 *          +-- does NOT perform booking/payment
 *
 * ScreenAnalyzer
 *          |
 *          +-- consumes UIEvidenceCollector evidence
 *          +-- classifies screens
 *          +-- suggests actions
 *
 * HARD STOP BOUNDARIES:
 * - CAPTCHA
 * - OTP
 * - PAYMENT
 * - Financial confirmation
 * - Completion
 * - Critical error
 *
 * This service does NOT:
 * - solve CAPTCHA
 * - enter OTP
 * - submit payment
 * - confirm financial transactions
 * - automatically book a ticket
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {

        private const val TAG = "VMAXAccessibilityService"

        /**
         * IRCTC Rail Connect package.
         */
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        /**
         * Accessibility event debounce.
         */
        private const val EVENT_DEBOUNCE_MS = 150L

        /**
         * Maximum amount of screen text kept for logging.
         */
        private const val MAX_LOG_TEXT_LENGTH = 160

        // --------------------------------------------------------
        // SECURITY / USER BOUNDARY KEYWORDS
        // --------------------------------------------------------

        private val CAPTCHA_KEYWORDS = listOf(
            "CAPTCHA",
            "ENTER CAPTCHA",
            "VERIFY CAPTCHA",
            "SECURITY CODE"
        )

        private val OTP_KEYWORDS = listOf(
            "OTP",
            "ENTER OTP",
            "ONE TIME PASSWORD",
            "ONE-TIME PASSWORD",
            "OTP VERIFICATION"
        )

        private val PAYMENT_KEYWORDS = listOf(
            "MAKE PAYMENT",
            "PAYMENT OPTIONS",
            "PROCEED TO PAY",
            "PAY USING UPI",
            "PAY USING WALLET",
            "UPI PAYMENT",
            "CREDIT CARD",
            "DEBIT CARD",
            "NET BANKING",
            "NETBANKING",
            "WALLET PAYMENT"
        )

        private val FINANCIAL_CONFIRMATION_KEYWORDS = listOf(
            "CONFIRM PAYMENT",
            "CONFIRM TRANSACTION",
            "AUTHORIZE PAYMENT",
            "PAY NOW",
            "FINAL PAYMENT"
        )

        private val COMPLETION_KEYWORDS = listOf(
            "BOOKING CONFIRMED",
            "TICKET CONFIRMED",
            "PAYMENT SUCCESSFUL",
            "TRANSACTION SUCCESSFUL",
            "PNR GENERATED"
        )

        private val ERROR_KEYWORDS = listOf(
            "SOMETHING WENT WRONG",
            "ERROR OCCURRED",
            "NETWORK ERROR",
            "SERVER ERROR",
            "REQUEST FAILED",
            "UNABLE TO PROCESS",
            "SESSION EXPIRED"
        )
    }

    // ============================================================
    // SERVICE STATE
    // ============================================================

    /**
     * True after Android successfully connects the service.
     */
    @Volatile
    private var serviceReady = false

    /**
     * Observation session state.
     *
     * This is NOT a booking transaction state.
     */
    @Volatile
    private var workflowRunning = false

    /**
     * True after a hard user boundary is detected.
     */
    @Volatile
    private var userBoundaryReached = false

    /**
     * Last detected local service screen state.
     */
    @Volatile
    private var lastScreenState = ScreenState.UNKNOWN

    /**
     * Last event timestamp used for debounce.
     */
    private var lastEventTimestamp = 0L

    /**
     * Last package observed.
     */
    private var lastPackageName = ""

    /**
     * Screen states owned by this service.
     *
     * This is intentionally smaller than ScreenAnalyzer.ScreenState.
     *
     * ScreenAnalyzer remains responsible for detailed screen
     * classification such as TRAIN_LIST, AVAILABILITY,
     * PASSENGER_INPUT, REVIEW_JOURNEY, etc.
     */
    enum class ScreenState {

        UNKNOWN,

        IRCTC_SCREEN,

        CAPTCHA_BOUNDARY,

        OTP_BOUNDARY,

        PAYMENT_BOUNDARY,

        FINANCIAL_CONFIRMATION_BOUNDARY,

        COMPLETED,

        ERROR,

        OTHER_APP
    }

    // ============================================================
    // SERVICE LIFECYCLE
    // ============================================================

    override fun onServiceConnected() {
        super.onServiceConnected()

        serviceReady = true
        workflowRunning = false
        userBoundaryReached = false
        lastScreenState = ScreenState.UNKNOWN
        lastEventTimestamp = 0L
        lastPackageName = ""

        configureService()

        log("Accessibility service connected")
    }

    override fun onInterrupt() {

        log("Accessibility service interrupted")

        stopWorkflow()

        serviceReady = false
    }

    override fun onDestroy() {

        log("Accessibility service destroyed")

        stopWorkflow()

        serviceReady = false

        super.onDestroy()
    }

    // ============================================================
    // ACCESSIBILITY EVENTS
    // ============================================================

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {

        if (!serviceReady) {
            return
        }

        if (event == null) {
            return
        }

        val packageName =
            event.packageName
                ?.toString()
                .orEmpty()

        lastPackageName = packageName

        /*
         * Only IRCTC events are relevant to this service.
         */
        if (packageName != IRCTC_PACKAGE) {
            lastScreenState = ScreenState.OTHER_APP
            return
        }

        /*
         * Ignore unsupported event types.
         */
        if (!isUsefulEvent(event)) {
            return
        }

        /*
         * Debounce event bursts.
         */
        val now = System.currentTimeMillis()

        if (now - lastEventTimestamp < EVENT_DEBOUNCE_MS) {
            return
        }

        lastEventTimestamp = now

        /*
         * During a hard user boundary, we continue observing the UI
         * but we never restart workflow automatically.
         */
        val root = try {
            rootInActiveWindow
        } catch (t: Throwable) {
            Log.w(
                TAG,
                "Unable to obtain active accessibility root",
                t
            )
            null
        }

        if (root == null) {
            log("IRCTC event received but root window is unavailable")
            return
        }

        try {

            val screenText =
                collectVisibleText(root)

            val state =
                classifyBoundary(
                    text = screenText
                )

            lastScreenState = state

            handleScreenState(
                state = state,
                screenText = screenText
            )

        } catch (t: Throwable) {

            /*
             * Accessibility trees can change while being traversed.
             * Never allow a malformed tree to crash the service.
             */
            Log.e(
                TAG,
                "Accessibility event processing failed",
                t
            )
        }
    }

    // ============================================================
    // SERVICE CONFIGURATION
    // ============================================================

    private fun configureService() {

        val info =
            serviceInfo ?: AccessibilityServiceInfo()

        info.eventTypes =
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED or
                AccessibilityEvent.TYPE_VIEW_CLICKED

        info.feedbackType =
            AccessibilityServiceInfo.FEEDBACK_GENERIC

        info.notificationTimeout = 100L

        info.flags =
            info.flags or
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS

        serviceInfo = info
    }

    // ============================================================
    // EVENT FILTER
    // ============================================================

    private fun isUsefulEvent(
        event: AccessibilityEvent
    ): Boolean {

        return when (event.eventType) {

            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED,
            AccessibilityEvent.TYPE_VIEW_CLICKED -> true

            else -> false
        }
    }

    // ============================================================
    // HARD BOUNDARY CLASSIFICATION
    // ============================================================

    /**
     * IMPORTANT:
     *
     * This is NOT the replacement for ScreenAnalyzer.
     *
     * This function only detects conditions where the service must
     * stop automated workflow involvement.
     */
    private fun classifyBoundary(
        text: String
    ): ScreenState {

        val normalized =
            normalizeText(text)

        /*
         * Completion must be checked BEFORE generic payment
         * keywords because "PAYMENT SUCCESSFUL" contains PAYMENT.
         */
        if (containsAny(
                normalized,
                COMPLETION_KEYWORDS
            )
        ) {
            return ScreenState.COMPLETED
        }

        /*
         * CAPTCHA has highest security priority.
         */
        if (containsAny(
                normalized,
                CAPTCHA_KEYWORDS
            )
        ) {
            return ScreenState.CAPTCHA_BOUNDARY
        }

        /*
         * OTP is always a user boundary.
         */
        if (containsAny(
                normalized,
                OTP_KEYWORDS
            )
        ) {
            return ScreenState.OTP_BOUNDARY
        }

        /*
         * Financial confirmation has higher priority than
         * generic payment UI.
         */
        if (containsAny(
                normalized,
                FINANCIAL_CONFIRMATION_KEYWORDS
            )
        ) {
            return ScreenState.FINANCIAL_CONFIRMATION_BOUNDARY
        }

        /*
         * Any payment-related screen is a hard boundary.
         */
        if (containsAny(
                normalized,
                PAYMENT_KEYWORDS
            )
        ) {
            return ScreenState.PAYMENT_BOUNDARY
        }

        /*
         * Critical application errors stop automated progression.
         */
        if (containsAny(
                normalized,
                ERROR_KEYWORDS
            )
        ) {
            return ScreenState.ERROR
        }

        return ScreenState.IRCTC_SCREEN
    }

    // ============================================================
    // SCREEN HANDLING
    // ============================================================

    private fun handleScreenState(
        state: ScreenState,
        screenText: String
    ) {

        when (state) {

            ScreenState.CAPTCHA_BOUNDARY -> {

                stopAtUserBoundary(
                    "CAPTCHA requires user interaction"
                )
            }

            ScreenState.OTP_BOUNDARY -> {

                stopAtUserBoundary(
                    "OTP requires user interaction"
                )
            }

            ScreenState.PAYMENT_BOUNDARY -> {

                stopAtUserBoundary(
                    "Payment screen requires user confirmation"
                )
            }

            ScreenState.FINANCIAL_CONFIRMATION_BOUNDARY -> {

                stopAtUserBoundary(
                    "Financial confirmation requires user interaction"
                )
            }

            ScreenState.COMPLETED -> {

                workflowRunning = false
                userBoundaryReached = true

                log("Completion detected; workflow stopped")
            }

            ScreenState.ERROR -> {

                stopAtUserBoundary(
                    "Critical error requires user decision"
                )
            }

            ScreenState.IRCTC_SCREEN -> {

                /*
                 * No automatic action here.
                 *
                 * Detailed classification is delegated to
                 * ScreenAnalyzer through the intelligence layer.
                 */
                if (
                    workflowRunning &&
                    !userBoundaryReached
                ) {

                    val preview =
                        screenText
                            .take(MAX_LOG_TEXT_LENGTH)

                    log(
                        "IRCTC screen observed: $preview"
                    )
                }
            }

            ScreenState.UNKNOWN,
            ScreenState.OTHER_APP -> {
                // Nothing to do.
            }
        }
    }

    // ============================================================
    // USER BOUNDARY
    // ============================================================

    private fun stopAtUserBoundary(
        reason: String
    ) {

        userBoundaryReached = true
        workflowRunning = false

        log("USER BOUNDARY: $reason")
    }

    // ============================================================
    // WORKFLOW CONTROL
    // ============================================================

    /**
     * Starts an observation session.
     *
     * IMPORTANT:
     * This does not book, pay, enter OTP, or solve CAPTCHA.
     */
    fun startWorkflow() {

        if (!serviceReady) {
            log("Cannot start workflow: service is not ready")
            return
        }

        if (workflowRunning) {
            log("Workflow observation already running")
            return
        }

        /*
         * Never restart after a hard boundary without explicit
         * user-controlled startWorkflow().
         */
        workflowRunning = true
        userBoundaryReached = false
        lastScreenState = ScreenState.UNKNOWN

        log("Workflow observation started")
    }

    /**
     * Stops the current observation session.
     */
    fun stopWorkflow() {

        val wasRunning = workflowRunning

        workflowRunning = false

        if (wasRunning) {
            log("Workflow observation stopped")
        }
    }

    /**
     * Returns whether observation is currently running.
     */
    fun isWorkflowRunning(): Boolean {
        return workflowRunning
    }

    /**
     * Returns whether a hard user boundary was reached.
     */
    fun isUserBoundaryReached(): Boolean {
        return userBoundaryReached
    }

    /**
     * Returns the service's last local screen state.
     */
    fun getLastScreenState(): ScreenState {
        return lastScreenState
    }

    /**
     * Returns whether Android has connected the service.
     */
    fun isServiceReady(): Boolean {
        return serviceReady
    }

    // ============================================================
    // UI TREE TEXT EXTRACTION
    // ============================================================

    /**
     * Reads visible text/content descriptions recursively.
     *
     * Observation only.
     *
     * No click.
     * No typing.
     * No submit.
     * No payment.
     */
    private fun collectVisibleText(
        root: AccessibilityNodeInfo
    ): String {

        val builder =
            StringBuilder()

        collectNodeText(
            node = root,
            builder = builder
        )

        return builder
            .toString()
            .trim()
    }

    private fun collectNodeText(
        node: AccessibilityNodeInfo?,
        builder: StringBuilder
    ) {

        if (node == null) {
            return
        }

        try {

            node.text
                ?.toString()
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    builder
                        .append(it)
                        .append(' ')
                }

            node.contentDescription
                ?.toString()
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    builder
                        .append(it)
                        .append(' ')
                }

            val childCount =
                node.childCount

            for (index in 0 until childCount) {

                val child =
                    try {
                        node.getChild(index)
                    } catch (_: Throwable) {
                        null
                    }

                if (child != null) {

                    collectNodeText(
                        node = child,
                        builder = builder
                    )

                    /*
                     * Child was obtained only for traversal and
                     * is not returned to the caller.
                     */
                    try {
                        child.recycle()
                    } catch (_: Throwable) {
                        // Ignore recycle failure.
                    }
                }
            }

        } catch (t: Throwable) {

            Log.w(
                TAG,
                "Unable to read accessibility node",
                t
            )
        }
    }

    // ============================================================
    // NODE SEARCH
    // ============================================================

    /**
     * Finds a visible node matching the supplied text.
     *
     * Observation only.
     *
     * Caller owns the returned node and must recycle it when done.
     */
    fun findNodeByText(
        text: String
    ): AccessibilityNodeInfo? {

        if (text.isBlank()) {
            return null
        }

        val root =
            rootInActiveWindow
                ?: return null

        return findNodeRecursive(
            node = root,
            target = normalizeText(text)
        )
    }

    private fun findNodeRecursive(
        node: AccessibilityNodeInfo?,
        target: String
    ): AccessibilityNodeInfo? {

        if (node == null) {
            return null
        }

        try {

            val nodeText =
                normalizeText(
                    node.text
                        ?.toString()
                        .orEmpty()
                )

            val description =
                normalizeText(
                    node.contentDescription
                        ?.toString()
                        .orEmpty()
                )

            if (
                nodeText == target ||
                description == target ||
                nodeText.contains(target) ||
                description.contains(target)
            ) {
                /*
                 * DO NOT recycle this node here.
                 * It is returned to the caller.
                 */
                return node
            }

            for (index in 0 until node.childCount) {

                val child =
                    try {
                        node.getChild(index)
                    } catch (_: Throwable) {
                        null
                    }

                val result =
                    findNodeRecursive(
                        node = child,
                        target = target
                    )

                if (result != null) {

                    /*
                     * If result is child, do not recycle child.
                     */
                    if (result !== child) {
                        try {
                            child?.recycle()
                        } catch (_: Throwable) {
                            // Ignore.
                        }
                    }

                    return result
                }

                try {
                    child?.recycle()
                } catch (_: Throwable) {
                    // Ignore.
                }
            }

        } catch (t: Throwable) {

            Log.w(
                TAG,
                "Node traversal failed",
                t
            )
        }

        return null
    }

    // ============================================================
    // CLICKABLE NODE SEARCH
    // ============================================================

    /**
     * Finds clickable nodes matching supplied text.
     *
     * Observation only.
     *
     * Returned nodes belong to the caller.
     */
    fun findClickableNodes(
        text: String
    ): List<AccessibilityNodeInfo> {

        if (text.isBlank()) {
            return emptyList()
        }

        val root =
            rootInActiveWindow
                ?: return emptyList()

        val result =
            mutableListOf<AccessibilityNodeInfo>()

        collectClickableNodes(
            node = root,
            target = normalizeText(text),
            result = result
        )

        return result
    }

    private fun collectClickableNodes(
        node: AccessibilityNodeInfo?,
        target: String,
        result: MutableList<AccessibilityNodeInfo>
    ) {

        if (node == null) {
            return
        }

        try {

            val nodeText =
                normalizeText(
                    node.text
                        ?.toString()
                        .orEmpty()
                )

            val description =
                normalizeText(
                    node.contentDescription
                        ?.toString()
                        .orEmpty()
                )

            val matches =
                nodeText.contains(target) ||
                    description.contains(target)

            if (
                node.isClickable &&
                matches
            ) {
                /*
                 * Caller owns this node.
                 */
                result.add(node)
            }

            for (index in 0 until node.childCount) {

                val child =
                    try {
                        node.getChild(index)
                    } catch (_: Throwable) {
                        null
                    }

                if (child == null) {
                    continue
                }

                val beforeCount =
                    result.size

                collectClickableNodes(
                    node = child,
                    target = target,
                    result = result
                )

                /*
                 * If child itself was added to result,
                 * caller owns it and it must NOT be recycled.
                 */
                val childReturned =
                    result
                        .drop(beforeCount)
                        .any { it === child }

                if (!childReturned) {
                    try {
                        child.recycle()
                    } catch (_: Throwable) {
                        // Ignore.
                    }
                }
            }

        } catch (t: Throwable) {

            Log.w(
                TAG,
                "Clickable node traversal failed",
                t
            )
        }
    }

    // ============================================================
    // TEXT NORMALIZATION
    // ============================================================

    private fun normalizeText(
        value: String
    ): String {

        return value
            .replace('\n', ' ')
            .replace('\r', ' ')
            .replace(
                Regex("\\s+"),
                " "
            )
            .trim()
            .uppercase()
    }

    private fun containsAny(
        text: String,
        values: List<String>
    ): Boolean {

        for (value in values) {

            if (
                text.contains(
                    normalizeText(value)
                )
            ) {
                return true
            }
        }

        return false
    }

    // ============================================================
    // PACKAGE / ACTIVE WINDOW
    // ============================================================

    /**
     * Returns true if the active window belongs to IRCTC.
     */
    fun isIRCTCActive(): Boolean {

        val packageName =
            try {
                rootInActiveWindow
                    ?.packageName
                    ?.toString()
            } catch (_: Throwable) {
                null
            }

        return packageName == IRCTC_PACKAGE
    }

    /**
     * Returns current active package name.
     */
    fun getCurrentPackageName(): String {

        return try {
            rootInActiveWindow
                ?.packageName
                ?.toString()
                .orEmpty()
        } catch (_: Throwable) {
            ""
        }
    }

    /**
     * Returns the last package received through AccessibilityEvent.
     */
    fun getLastPackageName(): String {
        return lastPackageName
    }

    // ============================================================
    // LOGGING
    // ============================================================

    private fun log(
        message: String
    ) {

        Log.d(
            TAG,
            message
        )
    }
}
