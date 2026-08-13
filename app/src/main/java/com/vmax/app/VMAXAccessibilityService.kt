package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.common.Logger
import com.vmax.common.Result
import com.vmax.runtime.ExecutionTracker
import com.vmax.workflow.ActionOrchestrator

class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_EXECUTION_SERVICE"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        // UI Evidence
        private const val EVIDENCE_FROM = "From"
        private const val EVIDENCE_TO = "To"
        private const val EVIDENCE_DATE = "Date"
        private const val EVIDENCE_SEARCH = "Search"
        private const val EVIDENCE_ADD_NEW = "Add New"
        private const val EVIDENCE_ADD_PASSENGER = "Add Passenger"
        private const val EVIDENCE_REVIEW = "REVIEW JOURNEY DETAILS"
        private const val EVIDENCE_CAPTCHA = "CAPTCHA"
        private const val EVIDENCE_OTP = "OTP"
    }

    // ----------------------------------------------------------------
    // CONFIGURATION DATA
    // ----------------------------------------------------------------

    private var targetFrom: String = ""
    private var targetTo: String = ""
    private var targetDate: String = ""
    private var targetTrain: String = ""
    private var targetClass: String = ""

    private var passengerName: String = ""
    private var passengerAge: String = ""
    private var passengerGender: String = ""
    private var passengerMeal: String = ""

    // Single session ID for the complete workflow
    private var currentSessionId: String = ""

    // ----------------------------------------------------------------
    // STATE MACHINE
    // ----------------------------------------------------------------

    private enum class State {
        IDLE,

        FROM_CLICKED,
        FROM_TYPED,
        FROM_SUGGESTION_CLICKED,

        TO_CLICKED,
        TO_TYPED,
        TO_SUGGESTION_CLICKED,

        DATE_CLICKED,
        DATE_SELECTED,

        SEARCH_CLICKED,
        TRAIN_SELECTED,
        CLASS_SELECTED,

        PASSENGER_ADD_CLICKED,
        PASSENGER_NAME_TYPED,
        PASSENGER_AGE_TYPED,
        PASSENGER_GENDER_CLICKED,
        PASSENGER_MEAL_CLICKED,
        PASSENGER_SUBMITTED,

        OPTIONS_REVIEW_CLICKED,

        USER_BOUNDARY,
        STOPPED
    }

    private var currentState = State.IDLE

    private lateinit var executor: AndroidActionExecutor
    private lateinit var orchestrator: ActionOrchestrator
    private lateinit var tracker: ExecutionTracker

    // ----------------------------------------------------------------
    // PUBLIC WORKFLOW CONTRACT
    // ----------------------------------------------------------------

    fun startWorkflow(
        from: String,
        to: String,
        date: String,
        train: String,
        trainClass: String,
        name: String,
        age: String,
        gender: String,
        meal: String
    ) {
        targetFrom = from
        targetTo = to
        targetDate = date
        targetTrain = train
        targetClass = trainClass

        passengerName = name
        passengerAge = age
        passengerGender = gender
        passengerMeal = meal

        currentSessionId = "SESSION_${System.currentTimeMillis()}"
        currentState = State.IDLE

        Log.i(TAG, "Workflow started: $currentSessionId")
    }

    fun stopWorkflow() {
        currentState = State.STOPPED
        Log.i(TAG, "Workflow stopped.")
    }

    // ----------------------------------------------------------------
    // SERVICE LIFECYCLE
    // ----------------------------------------------------------------

    override fun onServiceConnected() {
        super.onServiceConnected()

        executor = AndroidActionExecutor(this)

        // FINAL FIX:
        // Logger has constructor, but no getInstance()
        tracker = ExecutionTracker(Logger())

        orchestrator = ActionOrchestrator(
            executor,
            tracker
        )

        Log.i(
            TAG,
            "VMAX Service Connected with Real Executor and ActionOrchestrator"
        )
    }

    // ----------------------------------------------------------------
    // ACCESSIBILITY EVENT
    // ----------------------------------------------------------------

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return

        if (packageName != IRCTC_PACKAGE) return

        val root = rootInActiveWindow ?: return

        try {

            // --------------------------------------------------------
            // CAPTCHA / OTP USER BOUNDARY
            // --------------------------------------------------------

            if (isCaptchaOrOtpPresent(root)) {
                currentState = State.USER_BOUNDARY

                Log.w(
                    TAG,
                    "CAPTCHA/OTP detected. Locking to USER_BOUNDARY."
                )

                return
            }

            // --------------------------------------------------------
            // TERMINAL STATES
            // --------------------------------------------------------

            if (
                currentState == State.USER_BOUNDARY ||
                currentState == State.STOPPED
            ) {
                return
            }

            processWorkflow(root)

        } finally {
            root.recycle()
        }
    }

    // ----------------------------------------------------------------
    // WORKFLOW STATE MACHINE
    // ----------------------------------------------------------------

    private fun processWorkflow(root: AccessibilityNodeInfo) {

        when (currentState) {

            State.IDLE ->
                handleFromField(root)

            State.FROM_CLICKED ->
                handleFromTyping(root)

            State.FROM_TYPED ->
                handleFromSuggestion(root)

            State.FROM_SUGGESTION_CLICKED ->
                handleToField(root)

            State.TO_CLICKED ->
                handleToTyping(root)

            State.TO_TYPED ->
                handleToSuggestion(root)

            State.TO_SUGGESTION_CLICKED ->
                handleDateField(root)

            State.DATE_CLICKED ->
                handleDateSelection(root)

            State.DATE_SELECTED ->
                handleSearch(root)

            State.SEARCH_CLICKED ->
                handleTrainSelection(root)

            State.TRAIN_SELECTED ->
                handleClassSelection(root)

            State.CLASS_SELECTED ->
                handlePassengerScreen(root)

            State.PASSENGER_ADD_CLICKED ->
                handlePassengerDetails(root)

            State.PASSENGER_AGE_TYPED ->
                handlePassengerGender(root)

            State.PASSENGER_GENDER_CLICKED ->
                handlePassengerMeal(root)

            State.PASSENGER_MEAL_CLICKED ->
                handleAddPassengerSubmit(root)

            State.PASSENGER_SUBMITTED ->
                handleOptionsReview(root)

            State.PASSENGER_NAME_TYPED,
            State.OPTIONS_REVIEW_CLICKED,
            State.USER_BOUNDARY,
            State.STOPPED -> {
                // Awaiting next valid workflow event
            }
        }
    }

    // ----------------------------------------------------------------
    // FROM
    // ----------------------------------------------------------------

    private fun handleFromField(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(
            root,
            EVIDENCE_FROM
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.FROM_CLICKED
                }
            }
        }
    }

    private fun handleFromTyping(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(
            root,
            EVIDENCE_FROM
        )?.let { node ->

            executeSetText(
                node,
                targetFrom
            ) { success ->

                if (success) {
                    currentState = State.FROM_TYPED
                }
            }
        }
    }

    private fun handleFromSuggestion(root: AccessibilityNodeInfo) {
        findNodeByExactText(
            root,
            targetFrom,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.FROM_SUGGESTION_CLICKED
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // TO
    // ----------------------------------------------------------------

    private fun handleToField(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(
            root,
            EVIDENCE_TO
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.TO_CLICKED
                }
            }
        }
    }

    private fun handleToTyping(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(
            root,
            EVIDENCE_TO
        )?.let { node ->

            executeSetText(
                node,
                targetTo
            ) { success ->

                if (success) {
                    currentState = State.TO_TYPED
                }
            }
        }
    }

    private fun handleToSuggestion(root: AccessibilityNodeInfo) {
        findNodeByExactText(
            root,
            targetTo,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.TO_SUGGESTION_CLICKED
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // DATE
    // ----------------------------------------------------------------

    private fun handleDateField(root: AccessibilityNodeInfo) {
        findNodeByEvidence(
            root,
            EVIDENCE_DATE,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.DATE_CLICKED
                }
            }
        }
    }

    private fun handleDateSelection(root: AccessibilityNodeInfo) {
        findNodeByExactText(
            root,
            targetDate,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.DATE_SELECTED
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // SEARCH / TRAIN / CLASS
    // ----------------------------------------------------------------

    private fun handleSearch(root: AccessibilityNodeInfo) {
        findNodeByEvidence(
            root,
            EVIDENCE_SEARCH,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.SEARCH_CLICKED
                }
            }
        }
    }

    private fun handleTrainSelection(root: AccessibilityNodeInfo) {
        findNodeByExactText(
            root,
            targetTrain,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.TRAIN_SELECTED
                }
            }
        }
    }

    private fun handleClassSelection(root: AccessibilityNodeInfo) {
        findNodeByExactText(
            root,
            targetClass,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.CLASS_SELECTED
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // PASSENGER
    // ----------------------------------------------------------------

    private fun handlePassengerScreen(root: AccessibilityNodeInfo) {
        findNodeByEvidence(
            root,
            EVIDENCE_ADD_NEW,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.PASSENGER_ADD_CLICKED
                }
            }
        }
    }

    private fun handlePassengerDetails(root: AccessibilityNodeInfo) {

        findEditableNodeByEvidence(
            root,
            "Passenger Name"
        )?.let { nameNode ->

            executeSetText(
                nameNode,
                passengerName
            ) { nameSuccess ->

                if (!nameSuccess) return@executeSetText

                currentState = State.PASSENGER_NAME_TYPED

                findEditableNodeByEvidence(
                    root,
                    "Age"
                )?.let { ageNode ->

                    executeSetText(
                        ageNode,
                        passengerAge
                    ) { ageSuccess ->

                        if (ageSuccess) {
                            currentState = State.PASSENGER_AGE_TYPED
                        }
                    }
                }
            }
        }
    }

    private fun handlePassengerGender(root: AccessibilityNodeInfo) {
        findNodeByExactText(
            root,
            passengerGender,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.PASSENGER_GENDER_CLICKED
                }
            }
        }
    }

    private fun handlePassengerMeal(root: AccessibilityNodeInfo) {
        findNodeByEvidence(
            root,
            "Meal Preference",
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->
                if (success) {
                    currentState = State.PASSENGER_MEAL_CLICKED
                }
            }
        }
    }

    private fun handleAddPassengerSubmit(root: AccessibilityNodeInfo) {

        findNodeByExactText(
            root,
            passengerMeal,
            isClickable = true
        )?.let { mealNode ->

            executeClick(mealNode) {

                findNodeByExactText(
                    root,
                    EVIDENCE_ADD_PASSENGER,
                    isClickable = true
                )?.let { addNode ->

                    executeClick(addNode) { success ->

                        if (success) {
                            currentState = State.PASSENGER_SUBMITTED
                        }
                    }
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // REVIEW / TERMINAL
    // ----------------------------------------------------------------

    private fun handleOptionsReview(root: AccessibilityNodeInfo) {

        findNodeByExactText(
            root,
            EVIDENCE_REVIEW,
            isClickable = true
        )?.let { node ->

            executeClick(node) { success ->

                if (success) {
                    currentState = State.OPTIONS_REVIEW_CLICKED

                    Log.i(
                        TAG,
                        "Review Journey Details clicked. Automation stopped."
                    )

                    currentState = State.STOPPED
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS
    // ----------------------------------------------------------------

    private fun executeClick(
        node: AccessibilityNodeInfo?,
        onDispatched: (Boolean) -> Unit
    ) {

        if (node == null) {
            onDispatched(false)
            return
        }

        val targetId = node.viewIdResourceName ?: ""

        if (targetId.isEmpty()) {
            Log.w(
                TAG,
                "Click failed: Node has no viewIdResourceName"
            )

            onDispatched(false)
            return
        }

        val result = orchestrator.click(
            targetId,
            currentSessionId
        )

        when (result) {

            is Result.Success -> {
                onDispatched(true)
            }

            is Result.Error -> {
                Log.e(
                    TAG,
                    "Click failed: ${result.error.message}"
                )

                onDispatched(false)
            }
        }
    }

    private fun executeSetText(
        node: AccessibilityNodeInfo?,
        text: String,
        onDispatched: (Boolean) -> Unit
    ) {

        if (node == null) {
            onDispatched(false)
            return
        }

        val targetId = node.viewIdResourceName ?: ""

        if (targetId.isEmpty()) {
            Log.w(
                TAG,
                "SetText failed: Node has no viewIdResourceName"
            )

            onDispatched(false)
            return
        }

        val result = orchestrator.setText(
            targetId,
            text,
            currentSessionId
        )

        when (result) {

            is Result.Success -> {
                onDispatched(true)
            }

            is Result.Error -> {
                Log.e(
                    TAG,
                    "SetText failed: ${result.error.message}"
                )

                onDispatched(false)
            }
        }
    }

    // ----------------------------------------------------------------
    // EVIDENCE FINDERS
    // ----------------------------------------------------------------

    private fun findNodeByEvidence(
        root: AccessibilityNodeInfo,
        evidence: String,
        isClickable: Boolean = false
    ): AccessibilityNodeInfo? {

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (queue.isNotEmpty()) {

            val node = queue.removeFirst()

            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""

            if (
                node.isVisibleToUser &&
                (!isClickable || node.isClickable)
            ) {

                if (
                    text.equals(evidence, ignoreCase = true) ||
                    hint.equals(evidence, ignoreCase = true) ||
                    desc.equals(evidence, ignoreCase = true)
                ) {
                    return node
                }
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let {
                    queue.addLast(it)
                }
            }
        }

        return null
    }

    private fun findEditableNodeByEvidence(
        root: AccessibilityNodeInfo,
        evidence: String
    ): AccessibilityNodeInfo? {

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (queue.isNotEmpty()) {

            val node = queue.removeFirst()

            val text = node.text?.toString() ?: ""
            val hint = node.hintText?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""

            if (
                node.isVisibleToUser &&
                node.isEditable
            ) {

                if (
                    text.equals(evidence, ignoreCase = true) ||
                    hint.equals(evidence, ignoreCase = true) ||
                    desc.equals(evidence, ignoreCase = true)
                ) {
                    return node
                }
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let {
                    queue.addLast(it)
                }
            }
        }

        return null
    }

    private fun findNodeByExactText(
        root: AccessibilityNodeInfo,
        targetText: String,
        isClickable: Boolean = false
    ): AccessibilityNodeInfo? {

        if (targetText.isEmpty()) return null

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (queue.isNotEmpty()) {

            val node = queue.removeFirst()

            val text = node.text?.toString() ?: ""

            if (
                node.isVisibleToUser &&
                (!isClickable || node.isClickable)
            ) {

                if (
                    text.equals(
                        targetText,
                        ignoreCase = true
                    )
                ) {
                    return node
                }
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let {
                    queue.addLast(it)
                }
            }
        }

        return null
    }

    // ----------------------------------------------------------------
    // CAPTCHA / OTP BOUNDARY
    // ----------------------------------------------------------------

    private fun isCaptchaOrOtpPresent(
        root: AccessibilityNodeInfo
    ): Boolean {

        return findNodeByExactText(
            root,
            EVIDENCE_CAPTCHA
        ) != null ||
            findNodeByExactText(
                root,
                EVIDENCE_OTP
            ) != null
    }

    // ----------------------------------------------------------------
    // INTERRUPT
    // ----------------------------------------------------------------

    override fun onInterrupt() {
        Log.w(
            TAG,
            "Service interrupted"
        )
    }
}
