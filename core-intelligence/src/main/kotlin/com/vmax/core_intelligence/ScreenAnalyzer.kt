package com.vmax.core_intelligence

import com.vmax.common.Logger
import com.vmax.core_intelligence.UIEvidenceCollector.ScreenEvidence
import com.vmax.core_intelligence.UIEvidenceCollector.ScreenEvidence.UIElement
import com.vmax.core_intelligence.UIEvidenceCollector.ScreenEvidence.OcrEvidence

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
            logger.debug(TAG, "No evidence available for analysis")
            return createUnknownResult("No evidence available")
        }

        val uiElements = evidence.uiElements
        val ocrEvidence = evidence.ocrEvidence
        val fullText = ocrEvidence?.fullText?.uppercase() ?: ""
        val keyValuePairs = ocrEvidence?.keyValuePairs ?: emptyMap()

        logger.debug(TAG, "Analyzing screen with ${uiElements.size} UI elements, OCR text length: ${fullText.length}")

        // Check screens in priority order
        return when {
            isStationConfirmationScreen(fullText, uiElements) -> {
                handleStationConfirmation(evidence)
            }
            isAddPassengerFormScreen(fullText, uiElements) -> {
                handleAddPassengerForm(evidence)
            }
            isPaymentUPIScreen(fullText, uiElements) -> {
                handlePaymentUPI(evidence, keyValuePairs)
            }
            isPaymentWalletScreen(fullText, uiElements) -> {
                handlePaymentWallet(evidence, keyValuePairs)
            }
            isPaymentCategoryScreen(fullText, uiElements) -> {
                handlePaymentCategory(evidence, keyValuePairs)
            }
            isReviewJourneyScreen(fullText, uiElements, keyValuePairs) -> {
                handleReviewJourney(evidence, keyValuePairs)
            }
            isPassengerInputScreen(fullText, uiElements, keyValuePairs) -> {
                handlePassengerInput(evidence, keyValuePairs)
            }
            isAvailabilityScreen(fullText, uiElements, keyValuePairs) -> {
                handleAvailability(evidence, keyValuePairs)
            }
            isTrainListScreen(fullText, uiElements, keyValuePairs) -> {
                handleTrainList(evidence, keyValuePairs)
            }
            isLoadingScreen(fullText, uiElements) -> {
                handleLoadingScreen(evidence)
            }
            isErrorScreen(fullText, uiElements) -> {
                handleErrorScreen(evidence)
            }
            isCompletedScreen(fullText, uiElements) -> {
                handleCompletedScreen(evidence)
            }
            else -> {
                logger.debug(TAG, "Unknown screen detected")
                createUnknownResult("No matching screen pattern found")
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
        val hasNameField = uiElements.any { 
            it.hint?.contains("Name", ignoreCase = true) == true ||
            it.text.contains("Name", ignoreCase = true)
        }
        val hasAgeField = uiElements.any { 
            it.hint?.contains("Age", ignoreCase = true) == true ||
            it.text.contains("Age", ignoreCase = true)
        }
        val hasGenderOptions = uiElements.any { 
            it.text.contains("Male", ignoreCase = true) ||
            it.text.contains("Female", ignoreCase = true) ||
            it.text.contains("Transgender", ignoreCase = true)
        }
        val hasAddPassengerButton = uiElements.any {
            it.isClickable && it.text.contains("Add Passenger", ignoreCase = true)
        }
        
        return (hasNameField && hasAgeField && hasGenderOptions) || hasAddPassengerButton
    }

    private fun isPaymentUPIScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        val hasUPITitle = fullText.contains("PAY USING UPI") ||
                          fullText.contains("UPI (CREDIT CARD/ CREDIT LINE)")
        val hasPaymentProviders = uiElements.any {
            it.isClickable && (
                it.text.contains("IRCTC iPay", ignoreCase = true) ||
                it.text.contains("PayU", ignoreCase = true) ||
                it.text.contains("Paytm", ignoreCase = true) ||
                it.text.contains("PhonePe", ignoreCase = true)
            )
        }
        val hasProceedPay = uiElements.any {
            it.isClickable && it.text.contains("PROCEED TO PAY", ignoreCase = true)
        }
        
        return (hasUPITitle || hasPaymentProviders) && hasProceedPay
    }

    private fun isPaymentWalletScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {
        val hasWalletTitle = fullText.contains("PAY USING WALLET") ||
                             fullText.contains("WALLET (INSTANT PAYMENT)")
        val hasWalletProviders = uiElements.any {
            it.isClickable && (
                it.text.contains("IRCTC", ignoreCase = true) ||
                it.text.contains("Mobikwik", ignoreCase = true) ||
                it.text.contains("Amazon Pay", ignoreCase = true)
            )
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
        val hasCategories = uiElements.any { element ->
            paymentCategories.any { element.text.contains(it, ignoreCase = true) }
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
        val hasProceedButton = uiElements.any {
            it.isClickable && (
                it.text.contains("Proceed to Pay", ignoreCase = true) ||
                it.text.contains("PROCEED TO PAY", ignoreCase = true)
            )
        }
        
        return hasReviewTitle || (hasTrainDetails && hasPassengerDetails) || hasProceedButton
    }

    private fun isPassengerInputScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasPassengerTitle = fullText.contains("PASSENGER DETAILS")
        val hasAddNewButton = uiElements.any {
            it.isClickable && it.text.contains("Add New", ignoreCase = true)
        }
        val hasAddExistingButton = uiElements.any {
            it.isClickable && it.text.contains("Add Existing", ignoreCase = true)
        }
        val hasReviewButton = uiElements.any {
            it.isClickable && it.text.contains("REVIEW JOURNEY DETAILS", ignoreCase = true)
        }
        val hasEditableFields = uiElements.any { it.isEditable }
        
        return (hasPassengerTitle && hasAddNewButton) ||
               (hasAddNewButton && hasReviewButton) ||
               (hasEditableFields && hasReviewButton)
    }

    private fun isAvailabilityScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasClassOptions = listOf("SL", "3A", "2A", "1A", "CC", "EC", "3E", "2S", "FC")
            .any { fullText.contains(it) }
        val hasAvailabilityText = fullText.contains("AVAILABLE") ||
                                  fullText.contains("RAC") ||
                                  fullText.contains("WL") ||
                                  fullText.contains("REFRESH")
        val hasRefreshButtons = uiElements.any {
            it.isClickable && it.text.contains("Refresh", ignoreCase = true)
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
        val hasSelectableTrains = uiElements.any {
            it.isClickable && (
                it.text.contains("SELECT", ignoreCase = true) ||
                it.text.contains("VIEW", ignoreCase = true) ||
                it.text.contains("BOOK", ignoreCase = true)
            )
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
        val progressElements = uiElements.any {
            it.type.contains("ProgressBar", ignoreCase = true) ||
            it.type.contains("Loading", ignoreCase = true) ||
            it.type.contains("Spinner", ignoreCase = true)
        }
        
        return loadingText || progressElements
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

    private fun handleStationConfirmation(
        evidence: ScreenEvidence
    ): AnalysisResult {
        return AnalysisResult(
            screenState = ScreenState.STATION_CONFIRMATION,
            confidence = 0.95f,
            suggestedAction = SuggestedAction.CONFIRM_STATION,
            evidence = evidence,
            reason = "Station confirmation popup detected - Click Yes"
        )
    }

    private fun handleAddPassengerForm(
        evidence: ScreenEvidence
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        // Check if Name field is present
        val nameField = uiElements.firstOrNull {
            it.isEditable && (it.hint?.contains("Name", ignoreCase = true) == true ||
                              it.text.contains("Name", ignoreCase = true))
        }
        
        // Check if Age field is present
        val ageField = uiElements.firstOrNull {
            it.isEditable && (it.hint?.contains("Age", ignoreCase = true) == true ||
                              it.text.contains("Age", ignoreCase = true))
        }
        
        // Determine what to do next
        val action = when {
            nameField != null && nameField.text.isBlank() -> SuggestedAction.FILL_PASSENGER_NAME
            ageField != null && ageField.text.isBlank() -> SuggestedAction.FILL_PASSENGER_AGE
            uiElements.any { it.isClickable && it.text.contains("Add Passenger", ignoreCase = true) } -> 
                SuggestedAction.ADD_PASSENGER_CONFIRM
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
        evidence: ScreenEvidence,
        keyValuePairs: Map<String, String>
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        // Check for payment providers
        val provider = uiElements.firstOrNull {
            it.isClickable && (
                it.text.contains("IRCTC iPay", ignoreCase = true) ||
                it.text.contains("PayU", ignoreCase = true) ||
                it.text.contains("Paytm", ignoreCase = true) ||
                it.text.contains("PhonePe", ignoreCase = true)
            )
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
        evidence: ScreenEvidence,
        keyValuePairs: Map<String, String>
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        val fullText = evidence.ocrEvidence?.fullText?.uppercase() ?: ""
        
        // Check if insufficient balance
        if (fullText.contains("INSUFFICIENT") && fullText.contains("BALANCE")) {
            return AnalysisResult(
                screenState = ScreenState.PAYMENT_WALLET,
                confidence = 0.95f,
                suggestedAction = SuggestedAction.ERROR_RECOVERY,
                evidence = evidence,
                reason = "Insufficient wallet balance - need recovery"
            )
        }
        
        // Check for wallet providers
        val provider = uiElements.firstOrNull {
            it.isClickable && (
                it.text.contains("IRCTC", ignoreCase = true) ||
                it.text.contains("Mobikwik", ignoreCase = true) ||
                it.text.contains("Amazon Pay", ignoreCase = true)
            )
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
        evidence: ScreenEvidence,
        keyValuePairs: Map<String, String>
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        // Try to find UPI option first (preferred)
        val upiOption = uiElements.firstOrNull {
            it.isClickable && it.text.contains("UPI", ignoreCase = true)
        }
        
        // Then wallet option
        val walletOption = uiElements.firstOrNull {
            it.isClickable && it.text.contains("Wallet", ignoreCase = true)
        }
        
        val target = upiOption ?: walletOption
        
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
        evidence: ScreenEvidence,
        keyValuePairs: Map<String, String>
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        // Check if we need to proceed to payment
        val proceedButton = uiElements.firstOrNull {
            it.isClickable && (
                it.text.contains("Proceed to Pay", ignoreCase = true) ||
                it.text.contains("PROCEED TO PAY", ignoreCase = true)
            )
        }
        
        // Check if this is the final review before payment
        val hasPaymentAmount = keyValuePairs.containsKey("fare") ||
                               evidence.ocrEvidence?.fullText?.contains("₹") == true
        
        return if (proceedButton != null && hasPaymentAmount) {
            AnalysisResult(
                screenState = ScreenState.REVIEW_JOURNEY,
                confidence = 0.95f,
                suggestedAction = SuggestedAction.PROCEED_TO_PAY,
                extractedData = keyValuePairs,
                evidence = evidence,
                reason = "Review journey - proceed to payment"
            )
        } else {
            AnalysisResult(
                screenState = ScreenState.REVIEW_JOURNEY,
                confidence = 0.7f,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                extractedData = keyValuePairs,
                evidence = evidence,
                reason = "Review journey - awaiting user input"
            )
        }
    }

    private fun handlePassengerInput(
        evidence: ScreenEvidence,
        keyValuePairs: Map<String, String>
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        // Check if we need to add a passenger
        val addNewButton = uiElements.firstOrNull {
            it.isClickable && it.text.contains("Add New", ignoreCase = true)
        }
        
        val reviewButton = uiElements.firstOrNull {
            it.isClickable && it.text.contains("REVIEW JOURNEY DETAILS", ignoreCase = true)
        }
        
        // Check if there are existing passengers
        val hasPassengers = evidence.ocrEvidence?.fullText?.contains("PASSENGER") == true ||
                            uiElements.any { it.text.contains("TCCF", ignoreCase = true) }
        
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
        evidence: ScreenEvidence,
        keyValuePairs: Map<String, String>
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        // Find class selection elements
        val classElements = uiElements.filter {
            it.isClickable && listOf("SL", "3A", "2A", "1A", "CC", "EC", "3E", "2S", "FC")
                .any { classCode -> it.text.uppercase().contains(classCode) }
        }
        
        return if (classElements.isNotEmpty()) {
            AnalysisResult(
                screenState = ScreenState.AVAILABILITY,
                confidence = 0.85f,
                suggestedAction = SuggestedAction.SELECT_CLASS,
                extractedData = mapOf("available_classes" to classElements.map { it.text }.joinToString()),
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
        evidence: ScreenEvidence,
        keyValuePairs: Map<String, String>
    ): AnalysisResult {
        val uiElements = evidence.uiElements
        
        // Find selectable trains
        val trainElements = uiElements.filter {
            it.isClickable && (
                it.text.contains("SELECT", ignoreCase = true) ||
                it.text.contains("VIEW", ignoreCase = true)
            )
        }
        
        return if (trainElements.isNotEmpty()) {
            AnalysisResult(
                screenState = ScreenState.TRAIN_LIST,
                confidence = 0.85f,
                suggestedAction = SuggestedAction.SELECT_TRAIN,
                extractedData = keyValuePairs,
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

    private fun handleLoadingScreen(
        evidence: ScreenEvidence
    ): AnalysisResult {
        return AnalysisResult(
            screenState = ScreenState.LOADING,
            confidence = 0.9f,
            suggestedAction = SuggestedAction.WAIT_FOR_LOADING,
            evidence = evidence,
            reason = "Loading screen - waiting for completion"
        )
    }

    private fun handleErrorScreen(
        evidence: ScreenEvidence
    ): AnalysisResult {
        return AnalysisResult(
            screenState = ScreenState.ERROR_SCREEN,
            confidence = 0.9f,
            suggestedAction = SuggestedAction.ERROR_RECOVERY,
            evidence = evidence,
            reason = "Error screen detected - need recovery"
        )
    }

    private fun handleCompletedScreen(
        evidence: ScreenEvidence
    ): AnalysisResult {
        return AnalysisResult(
            screenState = ScreenState.COMPLETED,
            confidence = 1.0f,
            suggestedAction = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Booking completed successfully"
        )
    }

    private fun createUnknownResult(reason: String): AnalysisResult {
        return AnalysisResult(
            screenState = ScreenState.UNKNOWN,
            confidence = 0f,
            suggestedAction = SuggestedAction.NONE,
            reason = reason
        )
    }

    // ==================== PUBLIC HELPER FUNCTIONS ====================

    fun getCurrentUIElements(): List<UIElement> {
        val evidence = evidenceCollector.getCurrentEvidence()
        return evidence?.uiElements ?: emptyList()
    }

    fun findUIElementByText(text: String): UIElement? {
        return getCurrentUIElements().firstOrNull { 
            it.text.equals(text, ignoreCase = true) ||
            it.text.contains(text, ignoreCase = true)
        }
    }

    fun findClickableUIElements(): List<UIElement> {
        return getCurrentUIElements().filter { it.isClickable }
    }

    fun findEditableUIElements(): List<UIElement> {
        return getCurrentUIElements().filter { it.isEditable }
    }

    fun getTextFromScreen(): String {
        return evidenceCollector.getCurrentEvidence()?.ocrEvidence?.fullText ?: ""
    }

    fun getExtractedData(): Map<String, String> {
        return evidenceCollector.getCurrentEvidence()?.ocrEvidence?.keyValuePairs ?: emptyMap()
    }
}
