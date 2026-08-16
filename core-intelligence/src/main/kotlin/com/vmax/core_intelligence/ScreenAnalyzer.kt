package com.vmax.core_intelligence

import com.vmax.common.Logger

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
        val evidence: UIEvidenceCollector.ScreenEvidence? = null,
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
        val fullText = if (ocrEvidence != null) ocrEvidence.fullText.uppercase() else ""
        val keyValuePairs = if (ocrEvidence != null) ocrEvidence.keyValuePairs else emptyMap()

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
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): Boolean {
        return fullText.contains("YOU SEARCHED TRAINS FROM") &&
               fullText.contains("BUT BOOKING FROM") &&
               fullText.contains("DO YOU WANT TO CONTINUE WITH THE SAME?")
    }

    private fun isAddPassengerFormScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): Boolean {
        var hasNameField = false
        var hasAgeField = false
        var hasGenderOptions = false
        var hasAddPassengerButton = false
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            val hint = element.hint
            val text = element.text
            
            if (hint != null && hint.contains("Name", ignoreCase = true) ||
                text.contains("Name", ignoreCase = true)) {
                hasNameField = true
            }
            if (hint != null && hint.contains("Age", ignoreCase = true) ||
                text.contains("Age", ignoreCase = true)) {
                hasAgeField = true
            }
            if (text.contains("Male", ignoreCase = true) ||
                text.contains("Female", ignoreCase = true) ||
                text.contains("Transgender", ignoreCase = true)) {
                hasGenderOptions = true
            }
            if (element.isClickable && text.contains("Add Passenger", ignoreCase = true)) {
                hasAddPassengerButton = true
            }
            i++
        }
        
        return (hasNameField && hasAgeField && hasGenderOptions) || hasAddPassengerButton
    }

    private fun isPaymentUPIScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): Boolean {
        val hasUPITitle = fullText.contains("PAY USING UPI") ||
                          fullText.contains("UPI (CREDIT CARD/ CREDIT LINE)")
        
        var hasPaymentProviders = false
        var hasProceedPay = false
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("IRCTC iPay", ignoreCase = true) ||
                    text.contains("PayU", ignoreCase = true) ||
                    text.contains("Paytm", ignoreCase = true) ||
                    text.contains("PhonePe", ignoreCase = true)) {
                    hasPaymentProviders = true
                }
                if (text.contains("PROCEED TO PAY", ignoreCase = true)) {
                    hasProceedPay = true
                }
            }
            i++
        }
        
        return (hasUPITitle || hasPaymentProviders) && hasProceedPay
    }

    private fun isPaymentWalletScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): Boolean {
        val hasWalletTitle = fullText.contains("PAY USING WALLET") ||
                             fullText.contains("WALLET (INSTANT PAYMENT)")
        
        var hasWalletProviders = false
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("IRCTC", ignoreCase = true) ||
                    text.contains("Mobikwik", ignoreCase = true) ||
                    text.contains("Amazon Pay", ignoreCase = true)) {
                    hasWalletProviders = true
                }
            }
            i++
        }
        
        val hasInsufficientBalance = fullText.contains("INSUFFICIENT") &&
                                     fullText.contains("BALANCE")
        
        return hasWalletTitle || hasWalletProviders || hasInsufficientBalance
    }

    private fun isPaymentCategoryScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): Boolean {
        val hasMakePaymentTitle = fullText.contains("MAKE PAYMENT") ||
                                  fullText.contains("PAYMENT")
        
        val paymentCategories = arrayOf("Autopay", "Wallet", "EMI on Cards", 
                                       "UPI", "Credit Card", "Debit Card", 
                                       "NetBanking", "International Card")
        
        var hasCategories = false
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            var j = 0
            while (j < paymentCategories.size) {
                if (element.text.contains(paymentCategories[j], ignoreCase = true)) {
                    hasCategories = true
                    break
                }
                j++
            }
            if (hasCategories) break
            i++
        }
        
        val hasTotalAmount = fullText.contains("TOTAL AMOUNT") ||
                             fullText.contains("TOTAL")
        
        return (hasMakePaymentTitle || hasCategories) && hasTotalAmount
    }

    private fun isReviewJourneyScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasReviewTitle = fullText.contains("REVIEW JOURNEY")
        val hasTrainDetails = keyValuePairs.containsKey("train_number") ||
                              keyValuePairs.containsKey("from_station") ||
                              keyValuePairs.containsKey("to_station")
        val hasPassengerDetails = fullText.contains("PASSENGERS DETAILS") ||
                                  fullText.contains("PASSENGER DETAILS")
        
        var hasProceedButton = false
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("Proceed to Pay", ignoreCase = true) ||
                    text.contains("PROCEED TO PAY", ignoreCase = true)) {
                    hasProceedButton = true
                    break
                }
            }
            i++
        }
        
        return hasReviewTitle || (hasTrainDetails && hasPassengerDetails) || hasProceedButton
    }

    private fun isPassengerInputScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasPassengerTitle = fullText.contains("PASSENGER DETAILS")
        
        var hasAddNewButton = false
        var hasReviewButton = false
        var hasEditableFields = false
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("Add New", ignoreCase = true)) {
                    hasAddNewButton = true
                }
                if (text.contains("REVIEW JOURNEY DETAILS", ignoreCase = true)) {
                    hasReviewButton = true
                }
            }
            if (element.isEditable) {
                hasEditableFields = true
            }
            i++
        }
        
        return (hasPassengerTitle && hasAddNewButton) ||
               (hasAddNewButton && hasReviewButton) ||
               (hasEditableFields && hasReviewButton)
    }

    private fun isAvailabilityScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): Boolean {
        val classOptions = arrayOf("SL", "3A", "2A", "1A", "CC", "EC", "3E", "2S", "FC")
        var hasClassOptions = false
        var i = 0
        while (i < classOptions.size) {
            if (fullText.contains(classOptions[i])) {
                hasClassOptions = true
                break
            }
            i++
        }
        
        val hasAvailabilityText = fullText.contains("AVAILABLE") ||
                                  fullText.contains("RAC") ||
                                  fullText.contains("WL") ||
                                  fullText.contains("REFRESH")
        
        var hasRefreshButtons = false
        i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable && element.text.contains("Refresh", ignoreCase = true)) {
                hasRefreshButtons = true
                break
            }
            i++
        }
        
        return (hasClassOptions && hasAvailabilityText) || hasRefreshButtons
    }

    private fun isTrainListScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasTrainKeywords = fullText.contains("TRAINS") ||
                               fullText.contains("SORT BY")
        val hasTrainNames = keyValuePairs.containsKey("train_number") ||
                            keyValuePairs.containsKey("train_name")
        
        var hasSelectableTrains = false
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("SELECT", ignoreCase = true) ||
                    text.contains("VIEW", ignoreCase = true) ||
                    text.contains("BOOK", ignoreCase = true)) {
                    hasSelectableTrains = true
                    break
                }
            }
            i++
        }
        
        return hasTrainKeywords || hasTrainNames || hasSelectableTrains
    }

    private fun isLoadingScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): Boolean {
        val loadingText = fullText.contains("LOADING") ||
                          fullText.contains("PLEASE WAIT") ||
                          fullText.contains("PROCESSING") ||
                          fullText.contains("FETCHING")
        
        var hasProgressElements = false
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            val type = element.type
            if (type.contains("ProgressBar", ignoreCase = true) ||
                type.contains("Loading", ignoreCase = true) ||
                type.contains("Spinner", ignoreCase = true)) {
                hasProgressElements = true
                break
            }
            i++
        }
        
        return loadingText || hasProgressElements
    }

    private fun isErrorScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): Boolean {
        return fullText.contains("ERROR") ||
               fullText.contains("FAILED") ||
               fullText.contains("TRY AGAIN") ||
               fullText.contains("SOMETHING WENT WRONG")
    }

    private fun isCompletedScreen(
        fullText: String,
        uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>
    ): Boolean {
        return fullText.contains("BOOKING CONFIRMED") ||
               fullText.contains("TICKET CONFIRMED") ||
               fullText.contains("PAYMENT SUCCESSFUL") ||
               fullText.contains("THANK YOU")
    }

    // ==================== HANDLER FUNCTIONS ====================

    private fun handleAddPassengerForm(
        evidence: UIEvidenceCollector.ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var nameField: UIEvidenceCollector.ScreenEvidence.UIElement? = null
        var ageField: UIEvidenceCollector.ScreenEvidence.UIElement? = null
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isEditable) {
                val hint = element.hint
                val text = element.text
                if (hint != null && hint.contains("Name", ignoreCase = true) ||
                    text.contains("Name", ignoreCase = true)) {
                    nameField = element
                }
                if (hint != null && hint.contains("Age", ignoreCase = true) ||
                    text.contains("Age", ignoreCase = true)) {
                    ageField = element
                }
            }
            i++
        }
        
        var hasAddPassengerButton = false
        i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable && element.text.contains("Add Passenger", ignoreCase = true)) {
                hasAddPassengerButton = true
                break
            }
            i++
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
        evidence: UIEvidenceCollector.ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var provider: UIEvidenceCollector.ScreenEvidence.UIElement? = null
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("IRCTC iPay", ignoreCase = true) ||
                    text.contains("PayU", ignoreCase = true) ||
                    text.contains("Paytm", ignoreCase = true) ||
                    text.contains("PhonePe", ignoreCase = true)) {
                    provider = element
                    break
                }
            }
            i++
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
        evidence: UIEvidenceCollector.ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        val ocrEvidence = evidence.ocrEvidence
        val fullText = if (ocrEvidence != null) ocrEvidence.fullText.uppercase() else ""
        
        if (fullText.contains("INSUFFICIENT") && fullText.contains("BALANCE")) {
            return AnalysisResult(
                screenState = ScreenState.PAYMENT_WALLET,
                confidence = 0.95f,
                suggestedAction = SuggestedAction.ERROR_RECOVERY,
                evidence = evidence,
                reason = "Insufficient wallet balance - need recovery"
            )
        }
        
        var provider: UIEvidenceCollector.ScreenEvidence.UIElement? = null
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("IRCTC", ignoreCase = true) ||
                    text.contains("Mobikwik", ignoreCase = true) ||
                    text.contains("Amazon Pay", ignoreCase = true)) {
                    provider = element
                    break
                }
            }
            i++
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
        evidence: UIEvidenceCollector.ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var target: UIEvidenceCollector.ScreenEvidence.UIElement? = null
        
        // Try to find UPI option first
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable && element.text.contains("UPI", ignoreCase = true)) {
                target = element
                break
            }
            i++
        }
        
        // If no UPI, try wallet
        if (target == null) {
            i = 0
            while (i < uiElements.size) {
                val element = uiElements[i]
                if (element.isClickable && element.text.contains("Wallet", ignoreCase = true)) {
                    target = element
                    break
                }
                i++
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
        evidence: UIEvidenceCollector.ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var proceedButton: UIEvidenceCollector.ScreenEvidence.UIElement? = null
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("Proceed to Pay", ignoreCase = true) ||
                    text.contains("PROCEED TO PAY", ignoreCase = true)) {
                    proceedButton = element
                    break
                }
            }
            i++
        }
        
        val ocrEvidence = evidence.ocrEvidence
        val hasPaymentAmount = if (ocrEvidence != null) ocrEvidence.fullText.contains("₹") else false
        
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
        evidence: UIEvidenceCollector.ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var addNewButton: UIEvidenceCollector.ScreenEvidence.UIElement? = null
        var reviewButton: UIEvidenceCollector.ScreenEvidence.UIElement? = null
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("Add New", ignoreCase = true)) {
                    addNewButton = element
                }
                if (text.contains("REVIEW JOURNEY DETAILS", ignoreCase = true)) {
                    reviewButton = element
                }
            }
            i++
        }
        
        val ocrEvidence = evidence.ocrEvidence
        var hasPassengers = false
        if (ocrEvidence != null && ocrEvidence.fullText.contains("PASSENGER")) {
            hasPassengers = true
        } else {
            i = 0
            while (i < uiElements.size) {
                val element = uiElements[i]
                if (element.text.contains("TCCF", ignoreCase = true)) {
                    hasPassengers = true
                    break
                }
                i++
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
        evidence: UIEvidenceCollector.ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        val classCodes = arrayOf("SL", "3A", "2A", "1A", "CC", "EC", "3E", "2S", "FC")
        val availableClasses = mutableListOf<String>()
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text.uppercase()
                var j = 0
                while (j < classCodes.size) {
                    if (text.contains(classCodes[j])) {
                        availableClasses.add(element.text)
                        break
                    }
                    j++
                }
            }
            i++
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
        evidence: UIEvidenceCollector.ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        var hasSelectableTrains = false
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements[i]
            if (element.isClickable) {
                val text = element.text
                if (text.contains("SELECT", ignoreCase = true) ||
                    text.contains("VIEW", ignoreCase = true)) {
                    hasSelectableTrains = true
                    break
                }
            }
            i++
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

    fun getCurrentUIElements(): List<UIEvidenceCollector.ScreenEvidence.UIElement> {
        val evidence = evidenceCollector.getCurrentEvidence()
        return if (evidence != null) {
            evidence.uiElements
        } else {
            emptyList()
        }
    }

    fun findUIElementByText(text: String): UIEvidenceCollector.ScreenEvidence.UIElement? {
        val elements = getCurrentUIElements()
        var i = 0
        while (i < elements.size) {
            val element = elements[i]
            if (element.text.equals(text, ignoreCase = true) ||
                element.text.contains(text, ignoreCase = true)) {
                return element
            }
            i++
        }
        return null
    }

    fun findClickableUIElements(): List<UIEvidenceCollector.ScreenEvidence.UIElement> {
        val result = mutableListOf<UIEvidenceCollector.ScreenEvidence.UIElement>()
        val elements = getCurrentUIElements()
        var i = 0
        while (i < elements.size) {
            val element = elements[i]
            if (element.isClickable) {
                result.add(element)
            }
            i++
        }
        return result
    }

    fun findEditableUIElements(): List<UIEvidenceCollector.ScreenEvidence.UIElement> {
        val result = mutableListOf<UIEvidenceCollector.ScreenEvidence.UIElement>()
        val elements = getCurrentUIElements()
        var i = 0
        while (i < elements.size) {
            val element = elements[i]
            if (element.isEditable) {
                result.add(element)
            }
            i++
        }
        return result
    }

    fun getTextFromScreen(): String {
        val evidence = evidenceCollector.getCurrentEvidence()
        return if (evidence != null) {
            val ocrEvidence = evidence.ocrEvidence
            if (ocrEvidence != null) ocrEvidence.fullText else ""
        } else {
            ""
        }
    }

    fun getExtractedData(): Map<String, String> {
        val evidence = evidenceCollector.getCurrentEvidence()
        return if (evidence != null) {
            val ocrEvidence = evidence.ocrEvidence
            if (ocrEvidence != null) ocrEvidence.keyValuePairs else emptyMap()
        } else {
            emptyMap()
        }
    }
}
