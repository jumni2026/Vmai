package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.vmax.action.AndroidActionExecutor
import com.vmax.common.Result
import com.vmax.workflow.WorkflowController
import com.vmax.workflow.WorkflowState
import java.util.ArrayDeque
import java.util.Locale

/**
 * Persistent automation data.
 *
 * Responsibilities:
 * - Store armed state.
 * - Store passenger information.
 * - Store current workflow state.
 *
 * This class intentionally contains no UI automation logic.
 */
class AutomationDataStore(
    private val context: Context
) {

    companion object {
        private const val PREFS_NAME = "vmax_automation_state"

        private const val KEY_IS_ARMED = "is_armed"
        private const val KEY_STATE = "workflow_state"

        private const val KEY_NAME = "passenger_name"
        private const val KEY_AGE = "passenger_age"
        private const val KEY_GENDER = "passenger_gender"

        fun armAutomationSync(
            context: Context,
            name: String,
            age: String,
            gender: String
        ): Boolean {
            return context
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_IS_ARMED, true)
                .putString(KEY_NAME, name.trim())
                .putString(KEY_AGE, age.trim())
                .putString(KEY_GENDER, gender.trim())
                .commit()
        }

        fun clearAutomationSync(
            context: Context
        ): Boolean {
            return context
                .getSharedPreferences(
                    PREFS_NAME,
                    Context.MODE_PRIVATE
                )
                .edit()
                .clear()
                .commit()
        }
    }

    private val prefs =
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

    fun isArmed(): Boolean =
        prefs.getBoolean(KEY_IS_ARMED, false)

    fun getName(): String =
        prefs.getString(KEY_NAME, "") ?: ""

    fun getAge(): String =
        prefs.getString(KEY_AGE, "") ?: ""

    fun getGender(): String =
        prefs.getString(KEY_GENDER, "") ?: ""

    fun saveWorkflowStateSync(
        state: WorkflowState
    ): Boolean {
        return prefs
            .edit()
            .putString(KEY_STATE, state.name)
            .commit()
    }

    fun getWorkflowState(): WorkflowState? {
        val value = prefs.getString(
            KEY_STATE,
            null
        ) ?: return null

        return try {
            WorkflowState.valueOf(value)
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}

/**
 * VMAX Accessibility Service.
 *
 * Responsibilities:
 * - Observe the target application.
 * - Detect relevant UI context.
 * - Perform safe assistive actions.
 * - Persist workflow state.
 * - Stop at CAPTCHA/security and final booking/payment boundaries.
 *
 * CAPTCHA is always completed manually by the user.
 */
class VMAXAccessibilityService : AccessibilityService() {

    companion object {

        private const val TAG =
            "VMAX_A11y_Service"

        private const val IRCTC_PACKAGE =
            "cris.org.in.prs.ima"

        enum class AutomationState {
            IDLE,
            RUNNING,
            WAITING_FOR_CAPTCHA,
            PAUSED,
            STOPPED
        }

        @Volatile
        var automationState =
            AutomationState.IDLE
            private set

        var onAutomationStateChanged:
                ((AutomationState) -> Unit)? = null

        fun armAutomation(
            context: Context,
            name: String,
            age: String,
            gender: String
        ) {
            val success =
                AutomationDataStore
                    .armAutomationSync(
                        context = context,
                        name = name,
                        age = age,
                        gender = gender
                    )

            if (success) {
                setState(
                    AutomationState.RUNNING
                )

                Log.i(
                    TAG,
                    "Automation armed and persisted"
                )
            } else {
                Log.e(
                    TAG,
                    "Failed to persist automation data"
                )

                setState(
                    AutomationState.IDLE
                )
            }
        }

        fun stopAutomation(
            context: Context
        ) {
            val success =
                AutomationDataStore
                    .clearAutomationSync(context)

            if (success) {
                setState(
                    AutomationState.STOPPED
                )

                Log.i(
                    TAG,
                    "Automation stopped"
                )
            } else {
                Log.e(
                    TAG,
                    "Failed to clear automation data"
                )
            }
        }

        private fun setState(
            newState: AutomationState
        ) {
            automationState = newState
            onAutomationStateChanged
                ?.invoke(newState)
        }
    }

    private lateinit var actionExecutor:
            AndroidActionExecutor

    private lateinit var workflowController:
            WorkflowController

    private lateinit var dataStore:
            AutomationDataStore

    override fun onServiceConnected() {
        super.onServiceConnected()

        serviceInfo =
            AccessibilityServiceInfo().apply {

                eventTypes =
                    AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED

                feedbackType =
                    AccessibilityServiceInfo.FEEDBACK_GENERIC

                flags =
                    AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                    AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS

                notificationTimeout = 100L
            }

        actionExecutor =
            AndroidActionExecutor(this)

        workflowController =
            WorkflowController()

        dataStore =
            AutomationDataStore(this)

        restorePersistedState()

        Log.i(
            TAG,
            "VMAX Accessibility Service connected"
        )
    }

    /**
     * Restores persistent state without inventing
     * any WorkflowController API.
     */
    private fun restorePersistedState() {

        if (!dataStore.isArmed()) {
            setState(
                AutomationState.IDLE
            )

            return
        }

        val savedState =
            dataStore.getWorkflowState()

        if (savedState != null) {
            workflowController.updateState(
                savedState
            )

            Log.i(
                TAG,
                "Restored workflow state: $savedState"
            )
        }

        setState(
            AutomationState.RUNNING
        )
    }

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {

        if (event == null) {
            return
        }

        if (
            event.packageName
                ?.toString() != IRCTC_PACKAGE
        ) {
            return
        }

        if (
            automationState !=
            AutomationState.RUNNING &&
            automationState !=
            AutomationState.WAITING_FOR_CAPTCHA
        ) {
            return
        }

        val root =
            rootInActiveWindow
                ?: return

        try {

            /*
             * Security boundary has priority over
             * every normal workflow action.
             */
            if (
                containsSecurityBoundary(root)
            ) {
                handleSecurityBoundary()
                return
            }

            /*
             * When CAPTCHA disappears, restore the
             * previously persisted workflow state.
             */
            if (
                automationState ==
                AutomationState.WAITING_FOR_CAPTCHA
            ) {
                handleSecurityBoundaryCleared()
                return
            }

            if (
                automationState ==
                AutomationState.RUNNING
            ) {
                observeAndAct(root)
            }

        } catch (exception: Exception) {

            Log.e(
                TAG,
                "Accessibility event processing failed",
                exception
            )
        }
    }

    /**
     * Stop workflow at CAPTCHA/security boundary.
     */
    private fun handleSecurityBoundary() {

        if (
            automationState ==
            AutomationState.WAITING_FOR_CAPTCHA
        ) {
            return
        }

        val currentState =
            workflowController.getCurrentState()

        /*
         * Preserve the exact workflow state before
         * entering the manual security boundary.
         */
        dataStore.saveWorkflowStateSync(
            currentState
        )

        setState(
            AutomationState.WAITING_FOR_CAPTCHA
        )

        showToast(
            "CAPTCHA आया है। कृपया इसे मैन्युअली पूरा करें।"
        )

        Log.w(
            TAG,
            "Security boundary detected at $currentState"
        )
    }

    /**
     * Resume the persisted workflow state after
     * the security boundary disappears.
     */
    private fun handleSecurityBoundaryCleared() {

        val savedState =
            dataStore.getWorkflowState()

        if (savedState != null) {

            workflowController.updateState(
                savedState
            )

            Log.i(
                TAG,
                "Workflow restored after security boundary: $savedState"
            )
        } else {

            Log.w(
                TAG,
                "No persisted workflow state available after CAPTCHA"
            )
        }

        setState(
            AutomationState.RUNNING
        )

        showToast(
            "CAPTCHA पूरा हो गया। Workflow फिर से शुरू है।"
        )
    }

    /**
     * Main workflow dispatcher.
     *
     * Final booking/payment actions intentionally
     * remain outside this service.
     */
    private fun observeAndAct(
        root: AccessibilityNodeInfo
    ) {

        when (
            workflowController.getCurrentState()
        ) {

            WorkflowState.CONFIGURED -> {

                if (
                    hasScreenContext(
                        root,
                        "from",
                        "to",
                        "date"
                    )
                ) {

                    val result =
                        actionExecutor
                            .executeClickByText(
                                root,
                                "search",
                                "find trains"
                            )

                    if (
                        result is Result.Success
                    ) {

                        transitionTo(
                            WorkflowState.SEARCHING_TRAINS
                        )
                    }
                }
            }

            WorkflowState.SEARCHING_TRAINS -> {

                /*
                 * Do not select/book a train automatically.
                 *
                 * Wait for the next user-selected screen.
                 */
                if (
                    hasScreenContext(
                        root,
                        "passenger",
                        "details"
                    )
                ) {

                    transitionTo(
                        WorkflowState.WAITING_FOR_PASSENGER_FORM
                    )
                }
            }

            WorkflowState.WAITING_FOR_PASSENGER_FORM -> {

                if (
                    hasScreenContext(
                        root,
                        "passenger",
                        "name"
                    )
                ) {

                    transitionTo(
                        WorkflowState.FILLING_PASSENGER_NAME
                    )
                }
            }

            WorkflowState.FILLING_PASSENGER_NAME -> {

                val name =
                    dataStore.getName()

                if (name.isBlank()) {
                    Log.w(
                        TAG,
                        "Passenger name is empty"
                    )
                    return
                }

                val result =
                    actionExecutor
                        .executeSetTextByText(
                            root,
                            name,
                            "passenger name",
                            "name"
                        )

                if (
                    result is Result.Success
                ) {

                    transitionTo(
                        WorkflowState.FILLING_PASSENGER_AGE
                    )
                }
            }

            WorkflowState.FILLING_PASSENGER_AGE -> {

                val age =
                    dataStore.getAge()

                if (age.isBlank()) {
                    Log.w(
                        TAG,
                        "Passenger age is empty"
                    )
                    return
                }

                val result =
                    actionExecutor
                        .executeSetTextByText(
                            root,
                            age,
                            "age"
                        )

                if (
                    result is Result.Success
                ) {

                    transitionTo(
                        WorkflowState.SELECTING_GENDER
                    )
                }
            }

            WorkflowState.SELECTING_GENDER -> {

                val gender =
                    dataStore
                        .getGender()
                        .trim()
                        .lowercase(Locale.ROOT)

                if (gender.isBlank()) {
                    Log.w(
                        TAG,
                        "Passenger gender is empty"
                    )
                    return
                }

                val result =
                    actionExecutor
                        .executeClickByText(
                            root,
                            gender
                        )

                if (
                    result is Result.Success
                ) {

                    /*
                     * Stop at the manual boundary.
                     *
                     * No automatic booking/payment/final
                     * confirmation is performed here.
                     */
                    transitionTo(
                        WorkflowState.USER_BOUNDARY
                    )

                    showToast(
                        "Passenger details पूरे हैं। आगे की प्रक्रिया मैन्युअली करें।"
                    )
                }
            }

            WorkflowState.USER_BOUNDARY -> {

                Log.i(
                    TAG,
                    "User boundary reached; waiting for manual action"
                )
            }

            WorkflowState.WAITING_FOR_PAYMENT -> {

                Log.i(
                    TAG,
                    "Payment stage requires manual user action"
                )
            }

            else -> Unit
        }
    }

    /**
     * Central state transition.
     *
     * Controller = runtime source of truth.
     * DataStore = persistence layer.
     */
    private fun transitionTo(
        newState: WorkflowState
    ) {

        workflowController.updateState(
            newState
        )

        val persisted =
            dataStore.saveWorkflowStateSync(
                newState
            )

        if (!persisted) {

            Log.e(
                TAG,
                "Failed to persist workflow state: $newState"
            )

        } else {

            Log.d(
                TAG,
                "Workflow state -> $newState"
            )
        }
    }

    /**
     * Requires at least two independent UI keywords.
     */
    private fun hasScreenContext(
        root: AccessibilityNodeInfo,
        vararg keywords: String
    ): Boolean {

        val normalizedKeywords =
            keywords
                .map {
                    it.trim()
                        .lowercase(Locale.ROOT)
                }
                .filter {
                    it.isNotEmpty()
                }
                .distinct()

        if (
            normalizedKeywords.size < 2
        ) {
            return false
        }

        val queue =
            ArrayDeque<AccessibilityNodeInfo>()

        val owned =
            mutableListOf<AccessibilityNodeInfo>()

        enqueueChildren(
            root,
            queue
        )

        val matched =
            mutableSetOf<String>()

        while (queue.isNotEmpty()) {

            val node =
                queue.removeFirst()

            val text =
                node.text
                    ?.toString()
                    ?.lowercase(Locale.ROOT)
                    ?: ""

            val description =
                node.contentDescription
                    ?.toString()
                    ?.lowercase(Locale.ROOT)
                    ?: ""

            val combined =
                "$text $description"

            normalizedKeywords.forEach { keyword ->

                if (
                    combined.contains(keyword)
                ) {
                    matched.add(keyword)
                }
            }

            if (
                matched.size >= 2
            ) {

                recycleNodes(
                    queue,
                    owned
                )

                return true
            }

            owned.add(node)

            enqueueChildren(
                node,
                queue
            )
        }

        recycleNodes(
            queue,
            owned
        )

        return false
    }

    /**
     * Detect explicit CAPTCHA/security boundary.
     *
     * This function only detects the boundary.
     * It does not solve or bypass it.
     */
    private fun containsSecurityBoundary(
        root: AccessibilityNodeInfo
    ): Boolean {

        val securityKeywords =
            listOf(
                "captcha",
                "enter captcha",
                "captcha code",
                "refresh captcha",
                "verification code",
                "verify code"
            )

        val queue =
            ArrayDeque<AccessibilityNodeInfo>()

        val owned =
            mutableListOf<AccessibilityNodeInfo>()

        enqueueChildren(
            root,
            queue
        )

        while (queue.isNotEmpty()) {

            val node =
                queue.removeFirst()

            val text =
                node.text
                    ?.toString()
                    ?.lowercase(Locale.ROOT)
                    ?: ""

            val description =
                node.contentDescription
                    ?.toString()
                    ?.lowercase(Locale.ROOT)
                    ?: ""

            val combined =
                "$text $description"

            if (
                securityKeywords.any {
                    combined.contains(it)
                }
            ) {

                recycleNodes(
                    queue,
                    owned
                )

                return true
            }

            owned.add(node)

            enqueueChildren(
                node,
                queue
            )
        }

        recycleNodes(
            queue,
            owned
        )

        return false
    }

    private fun enqueueChildren(
        node: AccessibilityNodeInfo,
        queue: ArrayDeque<AccessibilityNodeInfo>
    ) {

        for (
            index in 0 until node.childCount
        ) {

            node.getChild(index)
                ?.let(queue::addLast)
        }
    }

    private fun recycleNodes(
        queue: ArrayDeque<AccessibilityNodeInfo>,
        owned: MutableList<AccessibilityNodeInfo>
    ) {

        while (
            queue.isNotEmpty()
        ) {

            try {
                queue
                    .removeFirst()
                    .recycle()
            } catch (_: Exception) {
            }
        }

        owned.forEach { node ->

            try {
                node.recycle()
            } catch (_: Exception) {
            }
        }

        owned.clear()
    }

    private fun showToast(
        message: String
    ) {

        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onInterrupt() {

        setState(
            AutomationState.PAUSED
        )

        Log.w(
            TAG,
            "Accessibility service interrupted"
        )
    }

    override fun onDestroy() {

        setState(
            AutomationState.STOPPED
        )

        Log.i(
            TAG,
            "VMAX Accessibility Service destroyed"
        )

        super.onDestroy()
    }
}
