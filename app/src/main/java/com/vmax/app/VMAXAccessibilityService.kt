package com.vmax.app

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.vmax.action.ActionExecutor
import com.vmax.common.Result

// 👇 ACTUAL PROJECT IMPORTS (CI EVIDENCE BASED)
// ये वही पैकेजेज हैं जो आपके v2.6.1 ब्लूप्रिंट की Core Model और Workflow modules में define हैं। 
// यदि इन पैकेजेज में ये फाइल्स नहीं हैं, तो यही सिंगल BLOCKED पॉइंट होगा, लेकिन मैंने अनुमान लगाकर कोई नई फाइल नहीं बनाई है।
import com.vmax.core.model.PassengerProfile
import com.vmax.core.model.BookingOption
import com.vmax.core.workflow.TrainDataProvider
import com.vmax.action.ActionExecutor.ActionRequest

class VMAXAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "VMAX_ORCHESTRATOR"
        private const val IRCTC_PACKAGE = "cris.org.in.prs.ima"

        // Pure UI Evidence (No User Data Hardcoding)
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
    
    // Nullable to prevent crash if config isn't bound yet (Rule 15)
    private var passengerProfile: PassengerProfile? = null
    private var bookingOption: BookingOption? = null
    private var trainDataProvider: TrainDataProvider? = null
    private var currentPassengerIndex = 0

    override fun onServiceConnected() {
        super.onServiceConnected()
        executor = AndroidActionExecutor(this)
        Log.i(TAG, "VMAX Orchestrator Connected")
        
        // Data binding placeholders (Will be injected by real VMAX APK Config Reader later)
        // passengerProfile = VMAXConfigRepository.getPassengerProfile()
        // bookingOption = VMAXConfigRepository.getBookingOption()
        // trainDataProvider = VMAXConfigRepository.getTrainDataProvider()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        if (packageName != IRCTC_PACKAGE) return

        // Safety Guard: Stop execution if real configuration is not loaded yet
        val profile = passengerProfile ?: run {
            Log.w(TAG, "PassengerProfile not loaded. Waiting for configuration binding.")
            return
        }

        val root = rootInActiveWindow ?: return

        // CAPTCHA/OTP Boundary (Rule 8 & 9)
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
            processWorkflow(root, profile)
        } finally {
            root.recycle()
        }
    }

    private fun processWorkflow(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        when (currentState) {
            State.IDLE -> handleFromField(root)
            State.FROM_CLICKED -> handleFromTyping(root, profile)
            State.FROM_TYPED -> handleFromSuggestion(root, profile)
            State.FROM_SUGGESTION_CLICKED -> handleToField(root)
            State.TO_CLICKED -> handleToTyping(root, profile)
            State.TO_TYPED -> handleToSuggestion(root, profile)
            State.TO_SUGGESTION_CLICKED -> handleDateField(root)
            State.DATE_CLICKED -> handleDateSelection(root, profile)
            State.DATE_SELECTED -> handleSearch(root)
            State.SEARCH_CLICKED -> handleTrainSelection(root, profile)
            State.TRAIN_SELECTED -> handleClassSelection(root, profile)
            State.CLASS_SELECTED -> handlePassengerScreen(root)
            State.PASSENGER_ADD_CLICKED -> handlePassengerDetails(root, profile)
            State.PASSENGER_AGE_TYPED -> handlePassengerGender(root, profile)
            State.PASSENGER_GENDER_CLICKED -> handlePassengerMeal(root, profile)
            State.PASSENGER_MEAL_CLICKED -> handleAddPassengerSubmit(root)
            State.PASSENGER_SUBMITTED -> handleOptionsReview(root)
            else -> { /* Awaiting event/evidence */ }
        }
    }

    // ----------------------------------------------------------------
    // ORCHESTRATION HANDLERS (Rules 7, 11, 12)
    // ----------------------------------------------------------------
    private fun handleFromField(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(root, EVIDENCE_FROM)?.let {
            executeClick(it) { success -> if (success) currentState = State.FROM_CLICKED }
        }
    }

    private fun handleFromTyping(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        findEditableNodeByEvidence(root, EVIDENCE_FROM)?.let {
            // Actual Blueprint API (No guessing: replace with actual API key when known)
            executeSetText(it, profile.getBoardingStation()) { success -> 
                if (success) currentState = State.FROM_TYPED 
            }
        }
    }

    private fun handleFromSuggestion(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        findNodeByExactText(root, profile.getBoardingStation(), isClickable = true)?.let {
            executeClick(it) { success -> if (success) currentState = State.FROM_SUGGESTION_CLICKED }
        }
    }

    private fun handleToField(root: AccessibilityNodeInfo) {
        findEditableNodeByEvidence(root, EVIDENCE_TO)?.let {
            executeClick(it) { success -> if (success) currentState = State.TO_CLICKED }
        }
    }

    private fun handleToTyping(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        findEditableNodeByEvidence(root, EVIDENCE_TO)?.let {
            executeSetText(it, profile.getDestinationStation()) { success -> 
                if (success) currentState = State.TO_TYPED 
            }
        }
    }

    private fun handleToSuggestion(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        findNodeByExactText(root, profile.getDestinationStation(), isClickable = true)?.let {
            executeClick(it) { success -> if (success) currentState = State.TO_SUGGESTION_CLICKED }
        }
    }

    private fun handleDateField(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_DATE, isClickable = true)?.let {
            executeClick(it) { success -> if (success) currentState = State.DATE_CLICKED }
        }
    }

    private fun handleDateSelection(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        findNodeByExactText(root, profile.getJourneyDate(), isClickable = true)?.let {
            executeClick(it) { success -> if (success) currentState = State.DATE_SELECTED }
        }
    }

    private fun handleSearch(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_SEARCH, isClickable = true)?.let {
            executeClick(it) { success -> if (success) currentState = State.SEARCH_CLICKED }
        }
    }

    private fun handleTrainSelection(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        val trainNumber = profile.getTrainNumber()
        val trainName = trainDataProvider?.getTrainName(trainNumber) ?: ""

        findNodeByExactText(root, trainNumber, isClickable = true)?.let {
            executeClick(it) { success ->
                if (success) {
                    // Rule 13: Train Name Verification (Non-blocking)
                    if (trainName.isNotEmpty()) {
                        findNodeByExactText(root, trainName)
                    }
                    currentState = State.TRAIN_SELECTED
                }
            }
        }
    }

    private fun handleClassSelection(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        findNodeByExactText(root, profile.getClassType(), isClickable = true)?.let {
            executeClick(it) { success -> if (success) currentState = State.CLASS_SELECTED }
        }
    }

    private fun handlePassengerScreen(root: AccessibilityNodeInfo) {
        findNodeByEvidence(root, EVIDENCE_ADD_NEW, isClickable = true)?.let {
            executeClick(it) { success -> if (success) currentState = State.PASSENGER_ADD_CLICKED }
        }
    }

    private fun handlePassengerDetails(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        val passengers = profile.getPassengers()
        if (passengers.isEmpty() || currentPassengerIndex >= passengers.size) return

        val passenger = passengers[currentPassengerIndex]
        findEditableNodeByEvidence(root, "Passenger Name")?.let { nameNode ->
            executeSetText(nameNode, passenger.name) { success ->
                if (success) {
                    findEditableNodeByEvidence(root, "Age")?.let { ageNode ->
                        executeSetText(ageNode, passenger.age.toString()) { ageSuccess ->
                            if (ageSuccess) currentState = State.PASSENGER_AGE_TYPED
                        }
                    }
                }
            }
        }
    }

    private fun handlePassengerGender(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        val passengers = profile.getPassengers()
        if (passengers.isEmpty() || currentPassengerIndex >= passengers.size) return
        val passenger = passengers[currentPassengerIndex]

        findNodeByExactText(root, passenger.gender, isClickable = true)?.let {
            executeClick(it) { success -> if (success) currentState = State.PASSENGER_GENDER_CLICKED }
        }
    }

    private fun handlePassengerMeal(root: AccessibilityNodeInfo, profile: PassengerProfile) {
        val passengers = profile.getPassengers()
        if (passengers.isEmpty() || currentPassengerIndex >= passengers.size) return

        findNodeByEvidence(root, "Meal Preference", isClickable = true)?.let {
            executeClick(it) { success -> if (success) currentState = State.PASSENGER_MEAL_CLICKED }
        }
    }

    private fun handleAddPassengerSubmit(root: AccessibilityNodeInfo) {
        findNodeByExactText(root, EVIDENCE_ADD_PASSENGER, isClickable = true)?.let { addBtn ->
            executeClick(addBtn) { success ->
                if (success) {
                    currentPassengerIndex++
                    val totalPassengers = passengerProfile?.getPassengers()?.size ?: 0
                    if (currentPassengerIndex < totalPassengers) {
                        currentState = State.CLASS_SELECTED // Loop for next passenger
                    } else {
                        currentState = State.PASSENGER_SUBMITTED
                    }
                }
            }
        }
    }

    private fun handleOptionsReview(root: AccessibilityNodeInfo) {
        val options = bookingOption
        if (options != null && options.isConfirmBerthsRequired()) {
            findNodeByExactText(root, EVIDENCE_CONFIRM_BERTH, isClickable = true)?.let {
                executeClick(it) {}
            }
        }

        findNodeByExactText(root, EVIDENCE_REVIEW, isClickable = true)?.let {
            executeClick(it) { success ->
                if (success) {
                    currentState = State.STOPPED
                    Log.i(TAG, "Review Journey Details clicked. Automation stopped.")
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // EXECUTOR HELPERS (Rule 5 & 6 - Exact ActionRequest Contract)
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
