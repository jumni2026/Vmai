package com.vmax.core_intelligence

import com.vmax.common.Logger
import com.vmax.core_intelligence.UIEvidenceCollector.ScreenEvidence
import com.vmax.core_intelligence.UIEvidenceCollector.ScreenEvidence.UIElement

/**
 * VMAX v2.6.1 - IRCTC Screen Analyzer (UPGRADED)
 * 
 * Responsibility: Analyze IRCTC screens and suggest appropriate actions
 * Supports all 23+ screens from IRCTC booking flow
 */
class ScreenAnalyzer(
    private val evidenceCollector: UIEvidenceCollector,
    private val logger: Logger
) {
    companion object {
        private const val TAG = "ScreenAnalyzer"
    }

    enum class ScreenState {
        UNKNOWN,
        STATION_CONFIRMATION,
        TRAIN_LIST,
        AVAILABILITY,
        PASSENGER_INPUT,
        ADD_PASSENGER_FORM,
        REVIEW_JOURNEY,
        PAYMENT_CATEGORY,
        PAYMENT_WALLET,
        PAYMENT_UPI,
        PAYMENT_CONFIRMATION,
        LOADING,
        ERROR_SCREEN,
        COMPLETED
    }

    enum class SuggestedAction {
        NONE,
        CONFIRM_STATION,
        SELECT_TRAIN,
        SELECT_CLASS,
        ADD_PASSENGER,
        FILL_PASSENGER_NAME,
        FILL_PASSENGER_AGE,
        SELECT_GENDER,
        SELECT_BERTH_PREFERENCE,
        SELECT_MEAL_PREFERENCE,
        SELECT_LOYALTY_POINTS,
        SKIP_LOYALTY_POINTS,
        SELECT_NO_FOOD,
        SELECT_AUTO_UPGRADE,
        SELECT_CONFIRM_BOOKING,
        SELECT_TRAVEL_INSURANCE,
        SELECT_NO_INSURANCE,
        ENTER_COACH_NUMBER,
        REVIEW_JOURNEY,
        ADD_PASSENGER_CONFIRM,
        PROCEED_TO_PAY,
        SELECT_PAYMENT_CATEGORY,
        SELECT_PAYMENT_PROVIDER,
        WAIT_FOR_LOADING,
        ERROR_RECOVERY,
        STOP_AWAIT_USER
    }

    data class AnalysisResult(
        val screenState: ScreenState,
        val confidence: Float,
        val suggestedAction: SuggestedAction,
        val extractedData: Map<String, String> = emptyMap(),
        val evidence: ScreenEvidence? = null,
        val reason: String = ""
    )

    fun analyzeCurrentScreen(): AnalysisResult {
        val evidence = evidenceCollector.getCurrentEvidence()
        if (evidence == null) {
            return AnalysisResult(
                screenState = ScreenState.UNKNOWN,
                confidence = 0f,
                suggestedAction = SuggestedAction.NONE,
                reason = "No evidence available"
            )
        }

        val uiElements = evidence.uiElements
        val ocrEvidence = evidence.ocrEvidence
        val fullText = ocrEvidence?.fullText?.uppercase() ?: ""
        val keyValuePairs = ocrEvidence?.keyValuePairs ?: emptyMap()

        return when {
            isStationConfirmationScreen(fullText, uiElements) -> {
                AnalysisResult(
                    screenState = ScreenState.STATION_CONFIRMATION,
                    confidence = 0.95f,
                    suggestedAction = SuggestedAction.CONFIRM_STATION,
                    evidence = evidence,
                    reason = "Station confirmation popup detected"
                )
            }
            isAddPassengerFormScreen(fullText, uiElements) -> {
                handleAddPassengerForm(evidence)
            }
            isPaymentUPIScreen(fullText, uiElements) -> {
                handlePaymentUPI(evidence)
            }
            isPaymentWalletScreen(fullText, uiElements) -> {
                handlePaymentWallet(evidence)
            }
            isPaymentCategoryScreen(fullText, uiElements) -> {
                handlePaymentCategory(evidence)
            }
            isReviewJourneyScreen(fullText, uiElements, keyValuePairs) -> {
                handleReviewJourney(evidence)
            }
            isPassengerInputScreen(fullText, uiElements, keyValuePairs) -> {
                handlePassengerInput(evidence)
            }
            isAvailabilityScreen(fullText, uiElements) -> {
                handleAvailability(evidence)
            }
            isTrainListScreen(fullText, uiElements, keyValuePairs) -> {
                handleTrainList(evidence)
            }
            isLoadingScreen(fullText, uiElements) -> {
                AnalysisResult(
                    screenState = ScreenState.LOADING,
                    confidence = 0.9f,
                    suggestedAction = SuggestedAction.WAIT_FOR_LOADING,
                    evidence = evidence,
                    reason = "Loading screen detected"
                )
            }
            isErrorScreen(fullText, uiElements) -> {
                AnalysisResult(
                    screenState = ScreenState.ERROR_SCREEN,
                    confidence = 0.9f,
                    suggestedAction = SuggestedAction.ERROR_RECOVERY,
                    evidence = evidence,
                    reason = "Error screen detected"
                )
            }
            isCompletedScreen(fullText, uiElements) -> {
                AnalysisResult(
                    screenState = ScreenState.COMPLETED,
                    confidence = 1.0f,
                    suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                    evidence = evidence,
                    reason = "Booking completed successfully"
                )
            }
            else -> {
                AnalysisResult(
                    screenState = ScreenState.UNKNOWN,
                    confidence = 0f,
                    suggestedAction = SuggestedAction.NONE,
                    evidence = evidence,
                    reason = "Unknown screen"
                )
            }
        }
    }

    // ==================== SCREEN DETECTION FUNCTIONS ====================

    private fun isStationConfirmationScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        return fullText.contains("YOU SEARCHED TRAINS FROM") &&
               fullText.contains("BUT BOOKING FROM") &&
               fullText.contains("DO YOU WANT TO CONTINUE WITH THE SAME?")
    }

    private fun isAddPassengerFormScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        var hasNameField = false
        var hasAgeField = false
        var hasGenderOptions = false
        var hasAddPassengerButton = false
        
        for (element in uiElements) {
            if (element.hint?.contains("Name", ignoreCase = true) == true ||
                element.text.contains("Name", ignoreCase = true)) {
                hasNameField = true
            }
            if (element.hint?.contains("Age", ignoreCase = true) == true ||
                element.text.contains("Age", ignoreCase = true)) {
                hasAgeField = true
            }
            if (element.text.contains("Male", ignoreCase = true) ||
                element.text.contains("Female", ignoreCase = true) ||
                element.text.contains("Transgender", ignoreCase = true)) {
                hasGenderOptions = true
            }
            if (element.isClickable && element.text.contains("Add Passenger", ignoreCase = true)) {
                hasAddPassengerButton = true
            }
        }
        
        return (hasNameField && hasAgeField && hasGenderOptions) || hasAddPassengerButton
    }

    private fun isPaymentUPIScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        val hasUPITitle = fullText.contains("PAY USING UPI") ||
                          fullText.contains("UPI (CREDIT CARD/ CREDIT LINE)")
        
        var hasPaymentProviders = false
        var hasProceedPay = false
        
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("IRCTC iPay", ignoreCase = true) ||
                    element.text.contains("PayU", ignoreCase = true) ||
                    element.text.contains("Paytm", ignoreCase = true) ||
                    element.text.contains("PhonePe", ignoreCase = true)) {
                    hasPaymentProviders = true
                }
                if (element.text.contains("PROCEED TO PAY", ignoreCase = true)) {
                    hasProceedPay = true
                }
            }
        }
        
        return (hasUPITitle || hasPaymentProviders) && hasProceedPay
    }

    private fun isPaymentWalletScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        val hasWalletTitle = fullText.contains("PAY USING WALLET") ||
                             fullText.contains("WALLET (INSTANT PAYMENT)")
        
        var hasWalletProviders = false
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("IRCTC", ignoreCase = true) ||
                    element.text.contains("Mobikwik", ignoreCase = true) ||
                    element.text.contains("Amazon Pay", ignoreCase = true)) {
                    hasWalletProviders = true
                }
            }
        }
        
        val hasInsufficientBalance = fullText.contains("INSUFFICIENT") &&
                                     fullText.contains("BALANCE")
        
        return hasWalletTitle || hasWalletProviders || hasInsufficientBalance
    }

    private fun isPaymentCategoryScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        val hasMakePaymentTitle = fullText.contains("MAKE PAYMENT") ||
                                  fullText.contains("PAYMENT")
        
        val paymentCategories = listOf("Autopay", "Wallet", "EMI on Cards", 
                                      "UPI", "Credit Card", "Debit Card", 
                                      "NetBanking", "International Card")
        
        var hasCategories = false
        for (element in uiElements) {
            for (category in paymentCategories) {
                if (element.text.contains(category, ignoreCase = true)) {
                    hasCategories = true
                    break
                }
            }
            if (hasCategories) break
        }
        
        val hasTotalAmount = fullText.contains("TOTAL AMOUNT") ||
                             fullText.contains("TOTAL")
        
        return (hasMakePaymentTitle || hasCategories) && hasTotalAmount
    }

    private fun isReviewJourneyScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasReviewTitle = fullText.contains("REVIEW JOURNEY")
        val hasTrainDetails = keyValuePairs.containsKey("train_number") ||
                              keyValuePairs.containsKey("from_station") ||
                              keyValuePairs.containsKey("to_station")
        val hasPassengerDetails = fullText.contains("PASSENGERS DETAILS") ||
                                  fullText.contains("PASSENGER DETAILS")
        
        var hasProceedButton = false
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("Proceed to Pay", ignoreCase = true) ||
                    element.text.contains("PROCEED TO PAY", ignoreCase = true)) {
                    hasProceedButton = true
                    break
                }
            }
        }
        
        return hasReviewTitle || (hasTrainDetails && hasPassengerDetails) || hasProceedButton
    }

    private fun isPassengerInputScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasPassengerTitle = fullText.contains("PASSENGER DETAILS")
        
        var hasAddNewButton = false
        var hasReviewButton = false
        var hasEditableFields = false
        
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("Add New", ignoreCase = true)) {
                    hasAddNewButton = true
                }
                if (element.text.contains("REVIEW JOURNEY DETAILS", ignoreCase = true)) {
                    hasReviewButton = true
                }
            }
            if (element.isEditable) {
                hasEditableFields = true
            }
        }
        
        return (hasPassengerTitle && hasAddNewButton) ||
               (hasAddNewButton && hasReviewButton) ||
               (hasEditableFields && hasReviewButton)
    }

    private fun isAvailabilityScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        val classOptions = listOf("SL", "3A", "2A", "1A", "CC", "EC", "3E", "2S", "FC")
        var hasClassOptions = false
        for (classCode in classOptions) {
            if (fullText.contains(classCode)) {
                hasClassOptions = true
                break
            }
        }
        
        val hasAvailabilityText = fullText.contains("AVAILABLE") ||
                                  fullText.contains("RAC") ||
                                  fullText.contains("WL") ||
                                  fullText.contains("REFRESH")
        
        var hasRefreshButtons = false
        for (element in uiElements) {
            if (element.isClickable && element.text.contains("Refresh", ignoreCase = true)) {
                hasRefreshButtons = true
                break
            }
        }
        
        return (hasClassOptions && hasAvailabilityText) || hasRefreshButtons
    }

    private fun isTrainListScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasTrainKeywords = fullText.contains("TRAINS") ||
                               fullText.contains("SORT BY")
        val hasTrainNames = keyValuePairs.containsKey("train_number") ||
                            keyValuePairs.containsKey("train_name")
        
        var hasSelectableTrains = false
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("SELECT", ignoreCase = true) ||
                    element.text.contains("VIEW", ignoreCase = true) ||
                    element.text.contains("BOOK", ignoreCase = true)) {
                    hasSelectableTrains = true
                    break
                }
            }
        }
        
        return hasTrainKeywords || hasTrainNames || hasSelectableTrains
    }

    private fun isLoadingScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        val loadingText = fullText.contains("LOADING") ||
                          fullText.contains("PLEASE WAIT") ||
                          fullText.contains("PROCESSING") ||
                          fullText.contains("FETCHING")
        
        var hasProgressElements = false
        for (element in uiElements) {
            if (element.type.contains("ProgressBar", ignoreCase = true) ||
                element.type.contains("Loading", ignoreCase = true) ||
                element.type.contains("Spinner", ignoreCase = true)) {
                hasProgressElements = true
                break
            }
        }
        
        return loadingText || hasProgressElements
    }

    private fun isErrorScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        return fullText.contains("ERROR") ||
               fullText.contains("FAILED") ||
               fullText.contains("TRY AGAIN") ||
               fullText.contains("SOMETHING WENT WRONG")
    }

    private fun isCompletedScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        return fullText.contains("BOOKING CONFIRMED") ||
               fullText.contains("TICKET CONFIRMED") ||
               fullText.contains("PAYMENT SUCCESSFUL") ||
               fullText.contains("THANK YOU")
    }

    // ==================== HANDLER FUNCTIONS ====================

    private fun handleAddPassengerForm(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var nameField: UIElement? = null
        var ageField: UIElement? = null
        
        for (element in uiElements) {
            if (element.isEditable) {
                if (element.hint?.contains("Name", ignoreCase = true) == true ||
                    element.text.contains("Name", ignoreCase = true)) {
                    nameField = element
                }
                if (element.hint?.contains("Age", ignoreCase = true) == true ||
                    element.text.contains("Age", ignoreCase = true)) {
                    ageField = element
                }
            }
        }
        
        var hasAddPassengerButton = false
        for (element in uiElements) {
            if (element.isClickable && element.text.contains("Add Passenger", ignoreCase = true)) {
                hasAddPassengerButton = true
                break
            }
        }
        
        val action = when {
            nameField != null && nameField.text.isBlank() -> SuggestedAction.FILL_PASSENGER_NAME
            ageField != null && ageField.text.isBlank() -> SuggestedAction.FILL_PASSENGER_AGE
            hasAddPassengerButton -> SuggestedAction.ADD_PASSENGER_CONFIRM
            else -> SuggestedAction.STOP_AWAIT_USER
        }
        
        return AnalysisResult(
            screenState = ScreenState.ADD_PASSENGER_FORM,
            confidence = 0.9f,
            suggestedAction = action,
            evidence = evidence,
            reason = "Add passenger form detected"
        )
    }

    private fun handlePaymentUPI(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var provider: UIElement? = null
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("IRCTC iPay", ignoreCase = true) ||
                    element.text.contains("PayU", ignoreCase = true) ||
                    element.text.contains("Paytm", ignoreCase = true) ||
                    element.text.contains("PhonePe", ignoreCase = true)) {
                    provider = element
                    break
                }
            }
        }
        
        return if (provider != null) {
            AnalysisResult(
                screenState = ScreenState.PAYMENT_UPI,
                confidence = 0.9f,
                suggestedAction = SuggestedAction.SELECT_PAYMENT_PROVIDER,
                extractedData = mapOf("provider" to provider.text),
                evidence = evidence,
                reason = "UPI payment provider selection"
            )
        } else {
            AnalysisResult(
                screenState = ScreenState.PAYMENT_UPI,
                confidence = 0.7f,
                suggestedAction = SuggestedAction.PROCEED_TO_PAY,
                evidence = evidence,
                reason = "UPI payment screen - proceed to pay"
            )
        }
    }

    private fun handlePaymentWallet(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        val fullText = evidence.ocrEvidence?.fullText?.uppercase() ?: ""
        
        if (fullText.contains("INSUFFICIENT") && fullText.contains("BALANCE")) {
            return AnalysisResult(
                screenState = ScreenState.PAYMENT_WALLET,
                confidence = 0.95f,
                suggestedAction = SuggestedAction.ERROR_RECOVERY,
                evidence = evidence,
                reason = "Insufficient wallet balance - need recovery"
            )
        }
        
        var provider: UIElement? = null
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("IRCTC", ignoreCase = true) ||
                    element.text.contains("Mobikwik", ignoreCase = true) ||
                    element.text.contains("Amazon Pay", ignoreCase = true)) {
                    provider = element
                    break
                }
            }
        }
        
        return if (provider != null) {
            AnalysisResult(
                screenState = ScreenState.PAYMENT_WALLET,
                confidence = 0.9f,
                suggestedAction = SuggestedAction.SELECT_PAYMENT_PROVIDER,
                extractedData = mapOf("provider" to provider.text),
                evidence = evidence,
                reason = "Wallet payment provider selection"
            )
        } else {
            AnalysisResult(
                screenState = ScreenState.PAYMENT_WALLET,
                confidence = 0.6f,
                suggestedAction = SuggestedAction.PROCEED_TO_PAY,
                evidence = evidence,
                reason = "Wallet payment screen - proceed"
            )
        }
    }

    private fun handlePaymentCategory(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var target: UIElement? = null
        
        // Try to find UPI option first
        for (element in uiElements) {
            if (element.isClickable && element.text.contains("UPI", ignoreCase = true)) {
                target = element
                break
            }
        }
        
        // If no UPI, try wallet
        if (target == null) {
            for (element in uiElements) {
                if (element.isClickable && element.text.contains("Wallet", ignoreCase = true)) {
                    target = element
                    break
                }
            }
        }
        
        return if (target != null) {
            AnalysisResult(
                screenState = ScreenState.PAYMENT_CATEGORY,
                confidence = 0.85f,
                suggestedAction = SuggestedAction.SELECT_PAYMENT_CATEGORY,
                extractedData = mapOf("category" to target.text),
                evidence = evidence,
                reason = "Payment category selection: ${target.text}"
            )
        } else {
            AnalysisResult(
                screenState = ScreenState.PAYMENT_CATEGORY,
                confidence = 0.5f,
                suggestedAction = SuggestedAction.NONE,
                evidence = evidence,
                reason = "No recognizable payment category"
            )
        }
    }

    private fun handleReviewJourney(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var proceedButton: UIElement? = null
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("Proceed to Pay", ignoreCase = true) ||
                    element.text.contains("PROCEED TO PAY", ignoreCase = true)) {
                    proceedButton = element
                    break
                }
            }
        }
        
        val hasPaymentAmount = evidence.ocrEvidence?.fullText?.contains("₹") == true
        
        return if (proceedButton != null && hasPaymentAmount) {
            AnalysisResult(
                screenState = ScreenState.REVIEW_JOURNEY,
                confidence = 0.95f,
                suggestedAction = SuggestedAction.PROCEED_TO_PAY,
                evidence = evidence,
                reason = "Review journey - proceed to payment"
            )
        } else {
            AnalysisResult(
                screenState = ScreenState.REVIEW_JOURNEY,
                confidence = 0.7f,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Review journey - awaiting user input"
            )
        }
    }

    private fun handlePassengerInput(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var addNewButton: UIElement? = null
        var reviewButton: UIElement? = null
        
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("Add New", ignoreCase = true)) {
                    addNewButton = element
                }
                if (element.text.contains("REVIEW JOURNEY DETAILS", ignoreCase = true)) {
                    reviewButton = element
                }
            }
        }
        
        var hasPassengers = false
        if (evidence.ocrEvidence?.fullText?.contains("PASSENGER") == true) {
            hasPassengers = true
        } else {
            for (element in uiElements) {
                if (element.text.contains("TCCF", ignoreCase = true)) {
                    hasPassengers = true
                    break
                }
            }
        }
        
        return when {
            addNewButton != null && !hasPassengers -> {
                AnalysisResult(
                    screenState = ScreenState.PASSENGER_INPUT,
                    confidence = 0.9f,
                    suggestedAction = SuggestedAction.ADD_PASSENGER,
                    evidence = evidence,
                    reason = "No passengers added - click Add New"
                )
            }
            reviewButton != null && hasPassengers -> {
                AnalysisResult(
                    screenState = ScreenState.PASSENGER_INPUT,
                    confidence = 0.85f,
                    suggestedAction = SuggestedAction.REVIEW_JOURNEY,
                    evidence = evidence,
                    reason = "Passengers added - review journey"
                )
            }
            else -> {
                AnalysisResult(
                    screenState = ScreenState.PASSENGER_INPUT,
                    confidence = 0.5f,
                    suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                    evidence = evidence,
                    reason = "Passenger screen - need user input"
                )
            }
        }
    }

    private fun handleAvailability(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        val classCodes = listOf("SL", "3A", "2A", "1A", "CC", "EC", "3E", "2S", "FC")
        val availableClasses = mutableListOf<String>()
        
        for (element in uiElements) {
            if (element.isClickable) {
                val text = element.text.uppercase()
                for (classCode in classCodes) {
                    if (text.contains(classCode)) {
                        availableClasses.add(element.text)
                        break
                    }
                }
            }
        }
        
        return if (availableClasses.isNotEmpty()) {
            AnalysisResult(
                screenState = ScreenState.AVAILABILITY,
                confidence = 0.85f,
                suggestedAction = SuggestedAction.SELECT_CLASS,
                extractedData = mapOf("available_classes" to availableClasses.joinToString()),
                evidence = evidence,
                reason = "Class availability - select class"
            )
        } else {
            AnalysisResult(
                screenState = ScreenState.AVAILABILITY,
                confidence = 0.5f,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Availability screen - need user input"
            )
        }
    }

    private fun handleTrainList(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var hasSelectableTrains = false
        for (element in uiElements) {
            if (element.isClickable) {
                if (element.text.contains("SELECT", ignoreCase = true) ||
                    element.text.contains("VIEW", ignoreCase = true)) {
                    hasSelectableTrains = true
                    break
                }
            }
        }
        
        return if (hasSelectableTrains) {
            AnalysisResult(
                screenState = ScreenState.TRAIN_LIST,
                confidence = 0.85f,
                suggestedAction = SuggestedAction.SELECT_TRAIN,
                evidence = evidence,
                reason = "Train list - select train"
            )
        } else {
            AnalysisResult(
                screenState = ScreenState.TRAIN_LIST,
                confidence = 0.5f,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Train list - need user input"
            )
        }
    }

    // ==================== PUBLIC HELPER FUNCTIONS ====================

    fun getCurrentUIElements(): List<UIElement> {
        val evidence = evidenceCollector.getCurrentEvidence()
        return if (evidence != null) {
            evidence.uiElements
        } else {
            emptyList()
        }
    }

    fun findUIElementByText(text: String): UIElement? {
        val elements = getCurrentUIElements()
        for (element in elements) {
            if (element.text.equals(text, ignoreCase = true) ||
                element.text.contains(text, ignoreCase = true)) {
                return element
            }
        }
        return null
    }

    fun findClickableUIElements(): List<UIElement> {
        val result = mutableListOf<UIElement>()
        val elements = getCurrentUIElements()
        for (element in elements) {
            if (element.isClickable) {
                result.add(element)
            }
        }
        return result
    }

    fun findEditableUIElements(): List<UIElement> {
        val result = mutableListOf<UIElement>()
        val elements = getCurrentUIElements()
        for (element in elements) {
            if (element.isEditable) {
                result.add(element)
            }
        }
        return result
    }

    fun getTextFromScreen(): String {
        val evidence = evidenceCollector.getCurrentEvidence()
        return evidence?.ocrEvidence?.fullText ?: ""
    }

    fun getExtractedData(): Map<String, String> {
        val evidence = evidenceCollector.getCurrentEvidence()
        return evidence?.ocrEvidence?.keyValuePairs ?: emptyMap()
    }
}
