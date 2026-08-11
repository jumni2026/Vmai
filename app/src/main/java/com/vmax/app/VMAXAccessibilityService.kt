package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

// Importing ACTUAL Existing Blueprint Contracts (Source of Truth)
import com.vmax.core.model.PassengerProfile
import com.vmax.core.model.BookingOption
import com.vmax.core.workflow.TrainDataProvider

class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_ORCHESTRATOR"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        // Pure UI Evidence (No User Data)
        private const val EVIDENCE_FROM = "From"
        private const val EVIDENCE_TO = "To"
        private const val EVIDENCE_DATE = "Date"
        private const val EVIDENCE_SEARCH = "Search"
        private const val EVIDENCE_ADD_NEW = "Add New"
        private const val EVIDENCE_ADD_PASSENGER = "Add Passenger"
        private const val EVIDENCE_REVIEW = "REVIEW JOURNEY DETAILS"
        private const val EVIDENCE_CONFIRM_BERTH = "confirm berths"
        private const val EVIDENCE_CAPTCHA = "CAPTCHA"
        private const val EVIDENCE_OTP = "OTP"
    }

    // ----------------------------------------------------------------
    // STATE MACHINE
    // ----------------------------------------------------------------
    private enum class State {
        IDLE,
        FROM_CLICKED, FROM_TYPED, FROM_SUGGESTION_CLICKED,
        TO_CLICKED, TO_TYPED, TO_SUGGESTION_CLICKED,
        DATE_CLICKED, DATE_SELECTED, SEARCH_CLICKED,
        TRAIN_SELECTED, CLASS_SELECTED,
        PASSENGER_ADD_CLICKED, PASSENGER_NAME_TYPED, PASSENGER_AGE_TYPED,
        PASSENGER_GENDER_CLICKED, PASSENGER_MEAL_CLICKED, PASSENGER_SUBMITTED,
        OPTIONS_REVIEW_CLICKED,
        USER_BOUNDARY, STOPPED
    }

    private var currentState = State.IDLE
    private lateinit var executor: AndroidActionExecutor

    // Real VMAX Configuration Source (Will be initialized by Dependency Injection in Future)
    private lateinit var passengerProfile: PassengerProfile
    private lateinit var bookingOption: BookingOption
    private lateinit var trainDataProvider: TrainDataProvider

    override fun onServiceConnected() {
        super.onServiceConnected()
        executor = AndroidActionExecutor(this)
        Log.i(TAG, "VMAX Orchestrator Connected")

        // Note: Actual initialization of passengerProfile, bookingOption, and trainDataProvider
        // will happen via a proper DI framework (e.g., Hilt/Koin) based on your APK's Config File.
        // This ensures Service is a pure Consumer, not a Factory.
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName != IRCTC_PACKAGE) return

        val root = rootInActiveWindow ?: return

        // CAPTCHA/OTP Boundary (Lock without killing service)
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
        when (currentState) {
            State.IDLE -> handleFromField(root)
            State.FROM_CLICKED -> handleFromTyping(root)
            State.FROM_TYPED -> handleFromSuggestion(root)
            State.FROM_SUGGESTION_CLICKED -> handleToField(root)
            State.TO_CLICKED -> handleToTyping(root)
            State.TO_TYPED -> handleToSuggestion(root)
            State.TO_SUGGESTION_CLICKED -> handleDateField(root)
            State.DATE_CLICKED -> handleDateSelection(root)
            State.DATE_SELECTED -> handleSearch(root)
            State.SEARCH_CLICKED -> handleTrainSelection(root)
            State.TRAIN_SELECTED -> handleClassSelection(root)
            State.CLASS_SELECTED -> handlePassengerScreen(root)
            State.PASSENGER_ADD_CLICKED -> handlePassengerDetails(root)
            State.PASSENGER_AGE_TYPED -> handlePassengerGender(root)
            State.PASSENGER_GENDER_CLICKED -> handlePassengerMeal(root)
            State.PASSENGER_MEAL_CLICKED -> handleAddPassengerSubmit(root)
            State.PASSENGER_SUBMITTED -> handleOptionsReview(root)
            else -> { /* Wait for Event/Evidence */ }
        }
    }

    // ----------------------------------------------------------------
    // ORCHESTRATION (Consumes real contracts: passengerProfile, bookingOption, trainDataProvider)
    // ----------------------------------------------------------------
    private fun handleFromField(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(root, EVIDENCE_FROM)?.let {
            executeClick(it) { if (it) currentState = State.FROM_CLICKED }
        }
    }

    private fun handleFromTyping(root: AccessibilityNodeInfo) {
        if (currentState == State.FROM_CLICKED) {
            findEditableNodeByEvidence(root, EVIDENCE_FROM)?.let {
                executeSetText(it, passengerProfile.getBoardingStation()) { if (it) currentState = State.FROM_TYPED }
            }
        }
    }

    private fun handleFromSuggestion(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, passengerProfile.getBoardingStation(), isClickable = true)?.let {
            executeClick(it) { if (it) currentState = State.FROM_SUGGESTION_CLICKED }
        }
    }

    private fun handleToField(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(root, EVIDENCE_TO)?.let {
            executeClick(it) { if (it) currentState = State.TO_CLICKED }
        }
    }

    private fun handleToTyping(root: AccessibilityNodeInfo) {
        if (currentState == State.TO_CLICKED) {
            findEditableNodeByEvidence(root, EVIDENCE_TO)?.let {
                executeSetText(it, passengerProfile.getDestinationStation()) { if (it) currentState = State.TO_TYPED }
            }
        }
    }

    private fun handleToSuggestion(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, passengerProfile.getDestinationStation(), isClickable = true)?.let {
            executeClick(it) { if (it) currentState = State.TO_SUGGESTION_CLICKED }
        }
    }

    private fun handleDateField(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_DATE, isClickable = true)?.let {
            executeClick(it) { if (it) currentState = State.DATE_CLICKED }
        }
    }

    private fun handleDateSelection(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, passengerProfile.getJourneyDate(), isClickable = true)?.let {
            executeClick(it) { if (it) currentState = State.DATE_SELECTED }
        }
    }

    private fun handleSearch(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_SEARCH, isClickable = true)?.let {
            executeClick(it) { if (it) currentState = State.SEARCH_CLICKED }
        }
    }

    private fun handleTrainSelection(root: AccessibilityNodeInfo) {
        val trainNumber = passengerProfile.getTrainNumber()
        val trainName = trainDataProvider.getTrainName(trainNumber)

        // Exact Match Rule
        findNodeByExactText(root, trainNumber, isClickable = true)?.let {
            executeClick(it) {
                if (it) {
                    // Verification / Non-blocking Rule
                    if (trainName.isNotEmpty()) {
                        findNodeByExactText(root, trainName) // Just verify, no action
                    }
                    currentState = State.TRAIN_SELECTED
                }
            }
        }
    }

    private fun handleClassSelection(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, passengerProfile.getClassType(), isClickable = true)?.let {
            executeClick(it) { if (it) currentState = State.CLASS_SELECTED }
        }
    }

    private fun handlePassengerScreen(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_ADD_NEW, isClickable = true)?.let {
            executeClick(it) { if (it) currentState = State.PASSENGER_ADD_CLICKED }
        }
    }

    private fun handlePassengerDetails(root: AccessibilityNodeInfo) {
        val allPassengers = passengerProfile.getPassengers()
        if (allPassengers.isEmpty()) return

        // Iterate over every passenger, exactly as Notebook Count dictates
        allPassengers.forEachIndexed { index, passenger ->
            // Logic to handle multiple passengers (Will be implemented in next phase)
            if (index == 0) { // Filling the first passenger for current scope
                findEditableNodeByEvidence(root, "Passenger Name")?.let { nameNode ->
                    executeSetText(nameNode, passenger.name) {
                        findEditableNodeByEvidence(root, "Age")?.let { ageNode ->
                            executeSetText(ageNode, passenger.age.toString()) {
                                currentState = State.PASSENGER_AGE_TYPED
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handlePassengerGender(root: AccessibilityNodeInfo) {
        val passenger = passengerProfile.getPassengers().firstOrNull() ?: return
        findNodeByExactText(root, passenger.gender, isClickable = true)?.let {
            executeClick(it) { if (it) currentState = State.PASSENGER_GENDER_CLICKED }
        }
    }

    private fun handlePassengerMeal(root: AccessibilityNodeInfo) {
        val passenger = passengerProfile.getPassengers().firstOrNull() ?: return
        findNodeByEvidence(root, "Meal Preference", isClickable = true)?.let {
            executeClick(it) { if (it) currentState = State.PASSENGER_MEAL_CLICKED }
        }
    }

    private fun handleAddPassengerSubmit(root: AccessibilityNodeInfo) {
        val passenger = passengerProfile.getPassengers().firstOrNull() ?: return
        findNodeByExactText(root, passenger.mealPreference, isClickable = true)?.let { mealNode ->
            executeClick(mealNode) {
                findNodeByExactText(root, EVIDENCE_ADD_PASSENGER, isClickable = true)?.let { addBtn ->
                    executeClick(addBtn) { if (it) currentState = State.PASSENGER_SUBMITTED }
                }
            }
        }
    }

    private fun handleOptionsReview(root: AccessibilityNodeInfo) {
        // Use Real BookingOption Contract
        if (bookingOption.isConfirmBerthsRequired()) {
            findNodeByExactText(root, EVIDENCE_CONFIRM_BERTH, isClickable = true)?.let {
                executeClick(it) {}
            }
        }

        // Final Step
        findNodeByExactText(root, EVIDENCE_REVIEW, isClickable = true)?.let {
            executeClick(it) {
                currentState = State.STOPPED
                Log.i(TAG, "Review Journey Details clicked. Automation Stopped.")
            }
        }
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS (Unchanged & Locked)
    // ----------------------------------------------------------------
    private fun executeClick(node: AccessibilityNodeInfo?, onDispatched: (Boolean) -> Unit) {
        if (node == null) { onDispatched(false); return }
        val request = ActionRequest(
            type = ActionExecutor.ActionType.CLICK,
            targetId = node.viewIdResourceName,
            targetClass = node.className?.toString()
        )
        dispatchToExecutor(request, onDispatched)
    }

    private fun executeSetText(node: AccessibilityNodeInfo?, text: String, onDispatched: (Boolean) -> Unit) {
        if (node == null) { onDispatched(false); return }
        val request = ActionRequest(
            type = ActionExecutor.ActionType.SET_TEXT,
            targetId = node.viewIdResourceName,
            targetClass = node.className?.toString(),
            text = text
        )
        dispatchToExecutor(request, onDispatched)
    }

    private fun dispatchToExecutor(request: ActionRequest, onDispatched: (Boolean) -> Unit) {
        try {
            val result = executor.executeAction(request)
            when (result) {
                is Result.Success -> onDispatched(true)
                is Result.Error -> {
                    Log.e(TAG, "Executor error: ${result.error.message}")
                    onDispatched(false)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception dispatching action", e)
            onDispatched(false)
        }
    }

    // ----------------------------------------------------------------
    // EVIDENCE-BASED FINDERS
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

    private fun isCaptchaOrOtpPresent(root: AccessibilityNodeInfo): Boolean {
        return findNodeByExactText(root, EVIDENCE_CAPTCHA) != null ||
               findNodeByExactText(root, EVIDENCE_OTP) != null
    }

    override fun onInterrupt() {
        Log.w(TAG, "Service interrupted")
    }
}
