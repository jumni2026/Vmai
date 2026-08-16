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

    // Type aliases for cleaner code
    private typealias ScreenEvidence = UIEvidenceCollector.ScreenEvidence
    private typealias UIElement = UIEvidenceCollector.ScreenEvidence.UIElement
    private typealias OcrEvidence = UIEvidenceCollector.ScreenEvidence.OcrEvidence

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
        
        var fullText = ""
        var keyValuePairs: Map<String, String> = emptyMap()
        if (ocrEvidence != null) {
            fullText = ocrEvidence.fullText.uppercase()
            keyValuePairs = ocrEvidence.keyValuePairs
        }

        if (isStationConfirmationScreen(fullText, uiElements)) {
            return AnalysisResult(
                screenState = ScreenState.STATION_CONFIRMATION,
                confidence = 0.95f,
                suggestedAction = SuggestedAction.CONFIRM_STATION,
                evidence = evidence,
                reason = "Station confirmation popup detected"
            )
        }
        
        if (isAddPassengerFormScreen(fullText, uiElements)) {
            return handleAddPassengerForm(evidence)
        }
        
        if (isPaymentUPIScreen(fullText, uiElements)) {
            return handlePaymentUPI(evidence)
        }
        
        if (isPaymentWalletScreen(fullText, uiElements)) {
            return handlePaymentWallet(evidence)
        }
        
        if (isPaymentCategoryScreen(fullText, uiElements)) {
            return handlePaymentCategory(evidence)
        }
        
        if (isReviewJourneyScreen(fullText, uiElements, keyValuePairs)) {
            return handleReviewJourney(evidence)
        }
        
        if (isPassengerInputScreen(fullText, uiElements, keyValuePairs)) {
            return handlePassengerInput(evidence)
        }
        
        if (isAvailabilityScreen(fullText, uiElements)) {
            return handleAvailability(evidence)
        }
        
        if (isTrainListScreen(fullText, uiElements, keyValuePairs)) {
            return handleTrainList(evidence)
        }
        
        if (isLoadingScreen(fullText, uiElements)) {
            return AnalysisResult(
                screenState = ScreenState.LOADING,
                confidence = 0.9f,
                suggestedAction = SuggestedAction.WAIT_FOR_LOADING,
                evidence = evidence,
                reason = "Loading screen detected"
            )
        }
        
        if (isErrorScreen(fullText, uiElements)) {
            return AnalysisResult(
                screenState = ScreenState.ERROR_SCREEN,
                confidence = 0.9f,
                suggestedAction = SuggestedAction.ERROR_RECOVERY,
                evidence = evidence,
                reason = "Error screen detected"
            )
        }
        
        if (isCompletedScreen(fullText, uiElements)) {
            return AnalysisResult(
                screenState = ScreenState.COMPLETED,
                confidence = 1.0f,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Booking completed successfully"
            )
        }

        return AnalysisResult(
            screenState = ScreenState.UNKNOWN,
            confidence = 0f,
            suggestedAction = SuggestedAction.NONE,
            evidence = evidence,
            reason = "Unknown screen"
        )
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
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            val hint = element.hint
            val text = element.text
            
            if (hint != null && hint.contains("Name", ignoreCase = true)) {
                hasNameField = true
            }
            if (text.contains("Name", ignoreCase = true)) {
                hasNameField = true
            }
            if (hint != null && hint.contains("Age", ignoreCase = true)) {
                hasAgeField = true
            }
            if (text.contains("Age", ignoreCase = true)) {
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
            i = i + 1
        }
        
        if (hasNameField && hasAgeField && hasGenderOptions) {
            return true
        }
        if (hasAddPassengerButton) {
            return true
        }
        return false
    }

    private fun isPaymentUPIScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        val hasUPITitle = fullText.contains("PAY USING UPI") ||
                          fullText.contains("UPI (CREDIT CARD/ CREDIT LINE)")
        
        var hasPaymentProviders = false
        var hasProceedPay = false
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
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
            i = i + 1
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
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val text = element.text
                if (text.contains("IRCTC", ignoreCase = true) ||
                    text.contains("Mobikwik", ignoreCase = true) ||
                    text.contains("Amazon Pay", ignoreCase = true)) {
                    hasWalletProviders = true
                }
            }
            i = i + 1
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
        
        val paymentCategories = arrayOf("Autopay", "Wallet", "EMI on Cards", 
                                       "UPI", "Credit Card", "Debit Card", 
                                       "NetBanking", "International Card")
        
        var hasCategories = false
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            var j = 0
            while (j < paymentCategories.size) {
                if (element.text.contains(paymentCategories.get(j), ignoreCase = true)) {
                    hasCategories = true
                    break
                }
                j = j + 1
            }
            if (hasCategories) {
                break
            }
            i = i + 1
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
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val text = element.text
                if (text.contains("Proceed to Pay", ignoreCase = true) ||
                    text.contains("PROCEED TO PAY", ignoreCase = true)) {
                    hasProceedButton = true
                    break
                }
            }
            i = i + 1
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
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
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
            i = i + 1
        }
        
        return (hasPassengerTitle && hasAddNewButton) ||
               (hasAddNewButton && hasReviewButton) ||
               (hasEditableFields && hasReviewButton)
    }

    private fun isAvailabilityScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        val classOptions = arrayOf("SL", "3A", "2A", "1A", "CC", "EC", "3E", "2S", "FC")
        var hasClassOptions = false
        var i = 0
        while (i < classOptions.size) {
            if (fullText.contains(classOptions.get(i))) {
                hasClassOptions = true
                break
            }
            i = i + 1
        }
        
        val hasAvailabilityText = fullText.contains("AVAILABLE") ||
                                  fullText.contains("RAC") ||
                                  fullText.contains("WL") ||
                                  fullText.contains("REFRESH")
        
        var hasRefreshButtons = false
        i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable && element.text.contains("Refresh", ignoreCase = true)) {
                hasRefreshButtons = true
                break
            }
            i = i + 1
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
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val text = element.text
                if (text.contains("SELECT", ignoreCase = true) ||
                    text.contains("VIEW", ignoreCase = true) ||
                    text.contains("BOOK", ignoreCase = true)) {
                    hasSelectableTrains = true
                    break
                }
            }
            i = i + 1
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
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            val type = element.type
            if (type.contains("ProgressBar", ignoreCase = true) ||
                type.contains("Loading", ignoreCase = true) ||
                type.contains("Spinner", ignoreCase = true)) {
                hasProgressElements = true
                break
            }
            i = i + 1
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
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isEditable) {
                val hint = element.hint
                val text = element.text
                if (hint != null && hint.contains("Name", ignoreCase = true)) {
                    nameField = element
                }
                if (text.contains("Name", ignoreCase = true)) {
                    nameField = element
                }
                if (hint != null && hint.contains("Age", ignoreCase = true)) {
                    ageField = element
                }
                if (text.contains("Age", ignoreCase = true)) {
                    ageField = element
                }
            }
            i = i + 1
        }
        
        var hasAddPassengerButton = false
        i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable && element.text.contains("Add Passenger", ignoreCase = true)) {
                hasAddPassengerButton = true
                break
            }
            i = i + 1
        }
        
        var action = SuggestedAction.STOP_AWAIT_USER
        if (nameField != null && nameField.text.isBlank()) {
            action = SuggestedAction.FILL_PASSENGER_NAME
        } else if (ageField != null && ageField.text.isBlank()) {
            action = SuggestedAction.FILL_PASSENGER_AGE
        } else if (hasAddPassengerButton) {
            action = SuggestedAction.ADD_PASSENGER_CONFIRM
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
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
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
            i = i + 1
        }
        
        if (provider != null) {
            val data: MutableMap<String, String> = mutableMapOf()
            data.put("provider", provider.text)
            return AnalysisResult(
                screenState = ScreenState.PAYMENT_UPI,
                confidence = 0.9f,
                suggestedAction = SuggestedAction.SELECT_PAYMENT_PROVIDER,
                extractedData = data,
                evidence = evidence,
                reason = "UPI payment provider selection"
            )
        } else {
            return AnalysisResult(
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
        val ocrEvidence = evidence.ocrEvidence
        var fullText = ""
        if (ocrEvidence != null) {
            fullText = ocrEvidence.fullText.uppercase()
        }
        
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
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val text = element.text
                if (text.contains("IRCTC", ignoreCase = true) ||
                    text.contains("Mobikwik", ignoreCase = true) ||
                    text.contains("Amazon Pay", ignoreCase = true)) {
                    provider = element
                    break
                }
            }
            i = i + 1
        }
        
        if (provider != null) {
            val data: MutableMap<String, String> = mutableMapOf()
            data.put("provider", provider.text)
            return AnalysisResult(
                screenState = ScreenState.PAYMENT_WALLET,
                confidence = 0.9f,
                suggestedAction = SuggestedAction.SELECT_PAYMENT_PROVIDER,
                extractedData = data,
                evidence = evidence,
                reason = "Wallet payment provider selection"
            )
        } else {
            return AnalysisResult(
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
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable && element.text.contains("UPI", ignoreCase = true)) {
                target = element
                break
            }
            i = i + 1
        }
        
        if (target == null) {
            i = 0
            while (i < uiElements.size) {
                val element = uiElements.get(i)
                if (element.isClickable && element.text.contains("Wallet", ignoreCase = true)) {
                    target = element
                    break
                }
                i = i + 1
            }
        }
        
        if (target != null) {
            val data: MutableMap<String, String> = mutableMapOf()
            data.put("category", target.text)
            return AnalysisResult(
                screenState = ScreenState.PAYMENT_CATEGORY,
                confidence = 0.85f,
                suggestedAction = SuggestedAction.SELECT_PAYMENT_CATEGORY,
                extractedData = data,
                evidence = evidence,
                reason = "Payment category selection"
            )
        } else {
            return AnalysisResult(
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
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val text = element.text
                if (text.contains("Proceed to Pay", ignoreCase = true) ||
                    text.contains("PROCEED TO PAY", ignoreCase = true)) {
                    proceedButton = element
                    break
                }
            }
            i = i + 1
        }
        
        val ocrEvidence = evidence.ocrEvidence
        var hasPaymentAmount = false
        if (ocrEvidence != null) {
            hasPaymentAmount = ocrEvidence.fullText.contains("₹")
        }
        
        if (proceedButton != null && hasPaymentAmount) {
            return AnalysisResult(
                screenState = ScreenState.REVIEW_JOURNEY,
                confidence = 0.95f,
                suggestedAction = SuggestedAction.PROCEED_TO_PAY,
                evidence = evidence,
                reason = "Review journey - proceed to payment"
            )
        } else {
            return AnalysisResult(
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
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val text = element.text
                if (text.contains("Add New", ignoreCase = true)) {
                    addNewButton = element
                }
                if (text.contains("REVIEW JOURNEY DETAILS", ignoreCase = true)) {
                    reviewButton = element
                }
            }
            i = i + 1
        }
        
        val ocrEvidence = evidence.ocrEvidence
        var hasPassengers = false
        if (ocrEvidence != null && ocrEvidence.fullText.contains("PASSENGER")) {
            hasPassengers = true
        } else {
            i = 0
            while (i < uiElements.size) {
                val element = uiElements.get(i)
                if (element.text.contains("TCCF", ignoreCase = true)) {
                    hasPassengers = true
                    break
                }
                i = i + 1
            }
        }
        
        if (addNewButton != null && !hasPassengers) {
            return AnalysisResult(
                screenState = ScreenState.PASSENGER_INPUT,
                confidence = 0.9f,
                suggestedAction = SuggestedAction.ADD_PASSENGER,
                evidence = evidence,
                reason = "No passengers added - click Add New"
            )
        } else if (reviewButton != null && hasPassengers) {
            return AnalysisResult(
                screenState = ScreenState.PASSENGER_INPUT,
                confidence = 0.85f,
                suggestedAction = SuggestedAction.REVIEW_JOURNEY,
                evidence = evidence,
                reason = "Passengers added - review journey"
            )
        } else {
            return AnalysisResult(
                screenState = ScreenState.PASSENGER_INPUT,
                confidence = 0.5f,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Passenger screen - need user input"
            )
        }
    }

    private fun handleAvailability(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        val classCodes = arrayOf("SL", "3A", "2A", "1A", "CC", "EC", "3E", "2S", "FC")
        val availableClasses = mutableListOf<String>()
        
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val text = element.text.uppercase()
                var j = 0
                while (j < classCodes.size) {
                    if (text.contains(classCodes.get(j))) {
                        availableClasses.add(element.text)
                        break
                    }
                    j = j + 1
                }
            }
            i = i + 1
        }
        
        if (availableClasses.size > 0) {
            val data: MutableMap<String, String> = mutableMapOf()
            var classesStr = ""
            var k = 0
            while (k < availableClasses.size) {
                if (k > 0) {
                    classesStr = classesStr + ","
                }
                classesStr = classesStr + availableClasses.get(k)
                k = k + 1
            }
            data.put("available_classes", classesStr)
            return AnalysisResult(
                screenState = ScreenState.AVAILABILITY,
                confidence = 0.85f,
                suggestedAction = SuggestedAction.SELECT_CLASS,
                extractedData = data,
                evidence = evidence,
                reason = "Class availability - select class"
            )
        } else {
            return AnalysisResult(
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
        var i = 0
        while (i < uiElements.size) {
            val element = uiElements.get(i)
            if (element.isClickable) {
                val text = element.text
                if (text.contains("SELECT", ignoreCase = true) ||
                    text.contains("VIEW", ignoreCase = true)) {
                    hasSelectableTrains = true
                    break
                }
            }
            i = i + 1
        }
        
        if (hasSelectableTrains) {
            return AnalysisResult(
                screenState = ScreenState.TRAIN_LIST,
                confidence = 0.85f,
                suggestedAction = SuggestedAction.SELECT_TRAIN,
                evidence = evidence,
                reason = "Train list - select train"
            )
        } else {
            return AnalysisResult(
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
        if (evidence != null) {
            return evidence.uiElements
        }
        return emptyList()
    }

    fun findUIElementByText(text: String): UIElement? {
        val elements = getCurrentUIElements()
        var i = 0
        while (i < elements.size) {
            val element = elements.get(i)
            val elementText = element.text
            if (elementText.equals(text, ignoreCase = true) ||
                elementText.contains(text, ignoreCase = true)) {
                return element
            }
            i = i + 1
        }
        return null
    }

    fun findClickableUIElements(): List<UIElement> {
        val result = mutableListOf<UIElement>()
        val elements = getCurrentUIElements()
        var i = 0
        while (i < elements.size) {
            val element = elements.get(i)
            if (element.isClickable) {
                result.add(element)
            }
            i = i + 1
        }
        return result
    }

    fun findEditableUIElements(): List<UIElement> {
        val result = mutableListOf<UIElement>()
        val elements = getCurrentUIElements()
        var i = 0
        while (i < elements.size) {
            val element = elements.get(i)
            if (element.isEditable) {
                result.add(element)
            }
            i = i + 1
        }
        return result
    }

    fun getTextFromScreen(): String {
        val evidence = evidenceCollector.getCurrentEvidence()
        if (evidence != null) {
            val ocrEvidence = evidence.ocrEvidence
            if (ocrEvidence != null) {
                return ocrEvidence.fullText
            }
        }
        return ""
    }

    fun getExtractedData(): Map<String, String> {
        val evidence = evidenceCollector.getCurrentEvidence()
        if (evidence != null) {
            val ocrEvidence = evidence.ocrEvidence
            if (ocrEvidence != null) {
                return ocrEvidence.keyValuePairs
            }
        }
        return emptyMap()
    }
}
