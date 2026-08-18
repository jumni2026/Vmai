package com.vmax.core_intelligence

import com.vmax.common.Logger

private typealias ScreenEvidence = UIEvidenceCollector.ScreenEvidence
private typealias UIElement = UIEvidenceCollector.ScreenEvidence.UIElement

/**
 * VMAX v2.6.1
 *
 * ScreenAnalyzer
 *
 * Responsibility:
 * - Analyze the current UI evidence.
 * - Classify the current screen.
 * - Produce a conservative suggested action.
 * - Extract useful screen data.
 *
 * Important:
 * - This class does NOT execute actions.
 * - It only analyzes evidence and returns an AnalysisResult.
 * - Payment screens intentionally stop at the user boundary.
 */
class ScreenAnalyzer(
    private val evidenceCollector: UIEvidenceCollector,
    private val logger: Logger
) {

    companion object {
        private const val TAG = "ScreenAnalyzer"

        private const val MIN_CONFIDENCE_UNKNOWN = 0.0f
        private const val CONFIDENCE_LOADING = 0.90f
        private const val CONFIDENCE_ERROR = 0.90f
        private const val CONFIDENCE_COMPLETED = 1.0f
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

    /**
     * Main analysis entry point.
     */
    fun analyzeCurrentScreen(): AnalysisResult {

        val evidence = evidenceCollector.getCurrentEvidence()

        if (evidence == null) {
            logDebug("No UI evidence available")

            return AnalysisResult(
                screenState = ScreenState.UNKNOWN,
                confidence = MIN_CONFIDENCE_UNKNOWN,
                suggestedAction = SuggestedAction.NONE,
                reason = "No evidence available"
            )
        }

        val uiElements = evidence.uiElements

        val rawOcrText = evidence.ocrEvidence?.fullText.orEmpty()
        val fullText = normalizeText(rawOcrText)

        val keyValuePairs =
            evidence.ocrEvidence?.keyValuePairs ?: emptyMap()

        /*
         * Priority matters.
         *
         * Modal/dialog screens first.
         * Then terminal/error/loading states.
         * Then workflow screens.
         */
        if (isStationConfirmationScreen(fullText)) {
            return AnalysisResult(
                screenState = ScreenState.STATION_CONFIRMATION,
                confidence = 0.98f,
                suggestedAction = SuggestedAction.CONFIRM_STATION,
                evidence = evidence,
                reason = "Station confirmation dialog detected"
            )
        }

        if (isCompletedScreen(fullText)) {
            return AnalysisResult(
                screenState = ScreenState.COMPLETED,
                confidence = CONFIDENCE_COMPLETED,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Booking/payment completion evidence detected"
            )
        }

        if (isErrorScreen(fullText)) {
            return AnalysisResult(
                screenState = ScreenState.ERROR_SCREEN,
                confidence = CONFIDENCE_ERROR,
                suggestedAction = SuggestedAction.ERROR_RECOVERY,
                evidence = evidence,
                reason = "Error/recovery screen detected"
            )
        }

        if (isLoadingScreen(fullText, uiElements)) {
            return AnalysisResult(
                screenState = ScreenState.LOADING,
                confidence = CONFIDENCE_LOADING,
                suggestedAction = SuggestedAction.WAIT_FOR_LOADING,
                evidence = evidence,
                reason = "Loading state detected"
            )
        }

        /*
         * Passenger form must be checked before generic passenger screen.
         */
        if (isAddPassengerFormScreen(fullText, uiElements)) {
            return handleAddPassengerForm(evidence)
        }

        /*
         * Review must be checked before payment because
         * "Proceed to Pay" can appear on review.
         */
        if (isReviewJourneyScreen(fullText, uiElements, keyValuePairs)) {
            return handleReviewJourney(evidence)
        }

        /*
         * Payment screens are classified but intentionally kept
         * behind the user boundary.
         */
        if (isPaymentUPIScreen(fullText, uiElements)) {
            return handlePaymentUPI(evidence)
        }

        if (isPaymentWalletScreen(fullText, uiElements)) {
            return handlePaymentWallet(evidence)
        }

        if (isPaymentCategoryScreen(fullText, uiElements)) {
            return handlePaymentCategory(evidence)
        }

        if (isPassengerInputScreen(fullText, uiElements)) {
            return handlePassengerInput(evidence)
        }

        if (isAvailabilityScreen(fullText, uiElements)) {
            return handleAvailability(evidence)
        }

        if (isTrainListScreen(fullText, uiElements, keyValuePairs)) {
            return handleTrainList(evidence)
        }

        return AnalysisResult(
            screenState = ScreenState.UNKNOWN,
            confidence = MIN_CONFIDENCE_UNKNOWN,
            suggestedAction = SuggestedAction.NONE,
            evidence = evidence,
            reason = "No known screen signature matched"
        )
    }

    // ============================================================
    // NORMALIZATION
    // ============================================================

    private fun normalizeText(value: String): String {
        return value
            .replace('\n', ' ')
            .replace('\r', ' ')
            .replace(Regex("\\s+"), " ")
            .trim()
            .uppercase()
    }

    private fun normalizedElementText(element: UIElement): String {
        return normalizeText(element.text)
    }

    private fun normalizedHint(element: UIElement): String {
        return normalizeText(element.hint.orEmpty())
    }

    private fun containsAny(
        text: String,
        values: List<String>
    ): Boolean {
        for (value in values) {
            if (text.contains(value.uppercase())) {
                return true
            }
        }

        return false
    }

    // ============================================================
    // SCREEN DETECTION
    // ============================================================

    private fun isStationConfirmationScreen(
        fullText: String
    ): Boolean {
        return fullText.contains("YOU SEARCHED TRAINS FROM") &&
            fullText.contains("BUT BOOKING FROM") &&
            (
                fullText.contains("DO YOU WANT TO CONTINUE") ||
                    fullText.contains("CONTINUE WITH THE SAME")
                )
    }

    private fun isAddPassengerFormScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        var hasNameField = false
        var hasAgeField = false
        var hasGenderOption = false
        var hasAddPassengerButton = false

        for (element in uiElements) {

            val text = normalizedElementText(element)
            val hint = normalizedHint(element)

            if (element.isEditable) {

                if (
                    hint.contains("NAME") ||
                    text.contains("NAME")
                ) {
                    hasNameField = true
                }

                if (
                    hint.contains("AGE") ||
                    text.contains("AGE")
                ) {
                    hasAgeField = true
                }
            }

            if (
                text.contains("MALE") ||
                text.contains("FEMALE") ||
                text.contains("TRANSGENDER")
            ) {
                hasGenderOption = true
            }

            if (
                element.isClickable &&
                text.contains("ADD PASSENGER")
            ) {
                hasAddPassengerButton = true
            }
        }

        /*
         * Strong form signature.
         */
        if (
            hasNameField &&
            hasAgeField &&
            hasGenderOption
        ) {
            return true
        }

        /*
         * Button-only fallback.
         */
        if (hasAddPassengerButton) {
            return true
        }

        /*
         * OCR title can support the detection but should not
         * be sufficient on its own.
         */
        return fullText.contains("ADD PASSENGER") &&
            (hasNameField || hasAgeField)
    }

    private fun isPassengerInputScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val hasPassengerTitle =
            fullText.contains("PASSENGER DETAILS") ||
                fullText.contains("PASSENGERS DETAILS")

        var hasAddNewButton = false
        var hasReviewButton = false
        var hasEditableField = false

        for (element in uiElements) {

            val text = normalizedElementText(element)

            if (element.isClickable) {

                if (text.contains("ADD NEW")) {
                    hasAddNewButton = true
                }

                if (
                    text.contains("REVIEW JOURNEY DETAILS") ||
                    text.contains("REVIEW JOURNEY")
                ) {
                    hasReviewButton = true
                }
            }

            if (element.isEditable) {
                hasEditableField = true
            }
        }

        return (
            hasPassengerTitle &&
                (hasAddNewButton || hasReviewButton)
            ) ||
            (
                hasEditableField &&
                    hasReviewButton
                )
    }

    private fun isReviewJourneyScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {

        val hasReviewTitle =
            fullText.contains("REVIEW JOURNEY") ||
                fullText.contains("REVIEW JOURNEY DETAILS")

        val hasPassengerDetails =
            fullText.contains("PASSENGER DETAILS") ||
                fullText.contains("PASSENGERS DETAILS")

        val hasTrainDetails =
            hasKey(
                keyValuePairs,
                "train_number"
            ) ||
                hasKey(
                    keyValuePairs,
                    "train_name"
                ) ||
                hasKey(
                    keyValuePairs,
                    "from_station"
                ) ||
                hasKey(
                    keyValuePairs,
                    "to_station"
                )

        var hasProceedButton = false

        for (element in uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                text.contains("PROCEED TO PAY") ||
                text.contains("PROCEED")
            ) {
                hasProceedButton = true
                break
            }
        }

        /*
         * Strongest signature.
         */
        if (
            hasReviewTitle &&
            (hasPassengerDetails || hasTrainDetails)
        ) {
            return true
        }

        /*
         * Button + train/passenger evidence.
         */
        if (
            hasProceedButton &&
            (hasPassengerDetails || hasTrainDetails)
        ) {
            return true
        }

        return false
    }

    private fun isPaymentUPIScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val hasUpiTitle =
            fullText.contains("PAY USING UPI") ||
                fullText.contains("UPI")

        if (!hasUpiTitle) {
            return false
        }

        var hasProvider = false

        for (element in uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                text.contains("IRCTC IPAY") ||
                text.contains("PAYU") ||
                text.contains("PAYTM") ||
                text.contains("PHONEPE")
            ) {
                hasProvider = true
                break
            }
        }

        return hasProvider ||
            fullText.contains("UPI ID") ||
            fullText.contains("ENTER UPI")
    }

    private fun isPaymentWalletScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val hasWalletTitle =
            fullText.contains("PAY USING WALLET") ||
                fullText.contains("WALLET")

        if (!hasWalletTitle) {
            return false
        }

        var hasWalletProvider = false

        for (element in uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                text.contains("MOBIKWIK") ||
                text.contains("AMAZON PAY") ||
                text.contains("IRCTC WALLET")
            ) {
                hasWalletProvider = true
                break
            }
        }

        return hasWalletProvider ||
            fullText.contains("WALLET BALANCE") ||
            fullText.contains("INSUFFICIENT BALANCE")
    }

    private fun isPaymentCategoryScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val categoryNames = listOf(
            "UPI",
            "WALLET",
            "CREDIT CARD",
            "DEBIT CARD",
            "NETBANKING",
            "INTERNATIONAL CARD",
            "EMI"
        )

        var categoryCount = 0

        for (element in uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            for (category in categoryNames) {

                if (text.contains(category)) {
                    categoryCount++
                    break
                }
            }
        }

        val hasPaymentTitle =
            fullText.contains("MAKE PAYMENT") ||
                fullText.contains("PAYMENT")

        val hasTotal =
            fullText.contains("TOTAL AMOUNT") ||
                fullText.contains("TOTAL FARE")

        /*
         * Avoid treating every screen containing the word
         * PAYMENT as a payment category screen.
         */
        return (
            categoryCount >= 1 &&
                hasPaymentTitle
            ) ||
            (
                categoryCount >= 2 &&
                    hasTotal
                )
    }

    private fun isAvailabilityScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val classCodes = listOf(
            "SL",
            "3A",
            "2A",
            "1A",
            "CC",
            "EC",
            "3E",
            "2S",
            "FC"
        )

        var classCount = 0
        var hasAvailabilityIndicator = false
        var hasRefresh = false

        for (element in uiElements) {

            val text = normalizedElementText(element)

            if (
                element.isClickable &&
                containsClassCode(text, classCodes)
            ) {
                classCount++
            }

            if (
                text.contains("AVAILABLE") ||
                text.contains("RAC") ||
                text.contains("WAITLIST") ||
                text.contains("WL")
            ) {
                hasAvailabilityIndicator = true
            }

            if (
                element.isClickable &&
                text.contains("REFRESH")
            ) {
                hasRefresh = true
            }
        }

        /*
         * Require multiple signals.
         */
        if (
            classCount >= 1 &&
            hasAvailabilityIndicator
        ) {
            return true
        }

        return hasRefresh &&
            (
                fullText.contains("AVAILABILITY") ||
                    fullText.contains("TRAIN")
                )
    }

    private fun isTrainListScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {

        val hasTrainNumber =
            hasKey(
                keyValuePairs,
                "train_number"
            )

        val hasTrainName =
            hasKey(
                keyValuePairs,
                "train_name"
            )

        val hasTrainHeading =
            fullText.contains("TRAIN LIST") ||
                fullText.contains("TRAINS") ||
                fullText.contains("SORT BY")

        var hasTrainAction = false

        for (element in uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                text.contains("SELECT") ||
                text.contains("VIEW") ||
                text.contains("BOOK")
            ) {
                hasTrainAction = true
                break
            }
        }

        /*
         * Strong train evidence.
         */
        if (
            (hasTrainNumber || hasTrainName) &&
                hasTrainAction
        ) {
            return true
        }

        if (
            hasTrainHeading &&
                hasTrainAction
        ) {
            return true
        }

        return false
    }

    private fun isLoadingScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val hasStrongLoadingText =
            fullText.contains("PLEASE WAIT") ||
                fullText.contains("PROCESSING") ||
                fullText.contains("FETCHING") ||
                fullText == "LOADING"

        var hasProgressElement = false

        for (element in uiElements) {

            val type = element.type

            if (
                type.contains(
                    "PROGRESSBAR",
                    ignoreCase = true
                ) ||
                type.contains(
                    "SPINNER",
                    ignoreCase = true
                )
            ) {
                hasProgressElement = true
                break
            }
        }

        return hasStrongLoadingText ||
            hasProgressElement
    }

    private fun isErrorScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        if (
            fullText.contains("SOMETHING WENT WRONG") ||
            fullText.contains("TECHNICAL ERROR") ||
            fullText.contains("SESSION EXPIRED") ||
            fullText.contains("NETWORK ERROR") ||
            fullText.contains("UNABLE TO PROCESS")
        ) {
            return true
        }

        /*
         * Generic ERROR/FAILED is accepted only with
         * an accompanying recovery signal.
         */
        val hasGenericError =
            fullText.contains("ERROR") ||
                fullText.contains("FAILED")

        val hasRecovery =
            fullText.contains("TRY AGAIN") ||
                fullText.contains("RETRY") ||
                fullText.contains("CLOSE") ||
                fullText.contains("BACK")

        return hasGenericError && hasRecovery
    }

    private fun isCompletedScreen(
        fullText: String
    ): Boolean {

        return fullText.contains("BOOKING CONFIRMED") ||
            fullText.contains("TICKET CONFIRMED") ||
            fullText.contains("PAYMENT SUCCESSFUL") ||
            fullText.contains("TRANSACTION SUCCESSFUL") ||
            fullText.contains("PNR") &&
            (
                fullText.contains("CONFIRMED") ||
                    fullText.contains("BOOKING")
                )
    }

    // ============================================================
    // HANDLERS
    // ============================================================

    private fun handleAddPassengerForm(
        evidence: ScreenEvidence
    ): AnalysisResult {

        val uiElements = evidence.uiElements

        var nameField: UIElement? = null
        var ageField: UIElement? = null

        var hasGender = false
        var hasAddPassengerButton = false

        for (element in uiElements) {

            val text = normalizedElementText(element)
            val hint = normalizedHint(element)

            if (element.isEditable) {

                if (
                    nameField == null &&
                    (
                        hint.contains("NAME") ||
                            text.contains("NAME")
                        )
                ) {
                    nameField = element
                }

                if (
                    ageField == null &&
                    (
                        hint.contains("AGE") ||
                            text.contains("AGE")
                        )
                ) {
                    ageField = element
                }
            }

            if (
                text.contains("MALE") ||
                text.contains("FEMALE") ||
                text.contains("TRANSGENDER")
            ) {
                hasGender = true
            }

            if (
                element.isClickable &&
                text.contains("ADD PASSENGER")
            ) {
                hasAddPassengerButton = true
            }
        }

        if (
            nameField != null &&
            nameField.text.isBlank()
        ) {
            return AnalysisResult(
                screenState = ScreenState.ADD_PASSENGER_FORM,
                confidence = 0.96f,
                suggestedAction = SuggestedAction.FILL_PASSENGER_NAME,
                evidence = evidence,
                reason = "Passenger name field is empty"
            )
        }

        if (
            ageField != null &&
            ageField.text.isBlank()
        ) {
            return AnalysisResult(
                screenState = ScreenState.ADD_PASSENGER_FORM,
                confidence = 0.96f,
                suggestedAction = SuggestedAction.FILL_PASSENGER_AGE,
                evidence = evidence,
                reason = "Passenger age field is empty"
            )
        }

        if (hasGender) {
            return AnalysisResult(
                screenState = ScreenState.ADD_PASSENGER_FORM,
                confidence = 0.90f,
                suggestedAction = SuggestedAction.SELECT_GENDER,
                evidence = evidence,
                reason = "Passenger gender selection detected"
            )
        }

        if (hasAddPassengerButton) {
            return AnalysisResult(
                screenState = ScreenState.ADD_PASSENGER_FORM,
                confidence = 0.90f,
                suggestedAction = SuggestedAction.ADD_PASSENGER_CONFIRM,
                evidence = evidence,
                reason = "Add Passenger confirmation control detected"
            )
        }

        return AnalysisResult(
            screenState = ScreenState.ADD_PASSENGER_FORM,
            confidence = 0.60f,
            suggestedAction = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Passenger form detected but next field is not safely identifiable"
        )
    }

    private fun handlePassengerInput(
        evidence: ScreenEvidence
    ): AnalysisResult {

        val uiElements = evidence.uiElements

        var addNewButton: UIElement? = null
        var reviewButton: UIElement? = null

        var hasPassengerEvidence = false

        for (element in uiElements) {

            val text = normalizedElementText(element)

            if (element.isClickable) {

                if (
                    text.contains("ADD NEW") &&
                    addNewButton == null
                ) {
                    addNewButton = element
                }

                if (
                    (
                        text.contains("REVIEW JOURNEY DETAILS") ||
                            text.contains("REVIEW JOURNEY")
                        ) &&
                    reviewButton == null
                ) {
                    reviewButton = element
                }
            }

            if (
                text.contains("PASSENGER") ||
                text.contains("TCCF")
            ) {
                hasPassengerEvidence = true
            }
        }

        val ocrText =
            normalizeText(
                evidence.ocrEvidence?.fullText.orEmpty()
            )

        if (ocrText.contains("PASSENGER")) {
            hasPassengerEvidence = true
        }

        if (
            addNewButton != null &&
            !hasPassengerEvidence
        ) {
            return AnalysisResult(
                screenState = ScreenState.PASSENGER_INPUT,
                confidence = 0.90f,
                suggestedAction = SuggestedAction.ADD_PASSENGER,
                evidence = evidence,
                reason = "Passenger list is empty"
            )
        }

        if (
            reviewButton != null &&
            hasPassengerEvidence
        ) {
            return AnalysisResult(
                screenState = ScreenState.PASSENGER_INPUT,
                confidence = 0.90f,
                suggestedAction = SuggestedAction.REVIEW_JOURNEY,
                evidence = evidence,
                reason = "Passenger data detected and review control is available"
            )
        }

        return AnalysisResult(
            screenState = ScreenState.PASSENGER_INPUT,
            confidence = 0.60f,
            suggestedAction = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Passenger screen detected but next action is ambiguous"
        )
    }

    private fun handleTrainList(
        evidence: ScreenEvidence
    ): AnalysisResult {

        var hasSelectableTrain = false

        for (element in evidence.uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                text.contains("SELECT") ||
                text.contains("VIEW") ||
                text.contains("BOOK")
            ) {
                hasSelectableTrain = true
                break
            }
        }

        if (hasSelectableTrain) {
            return AnalysisResult(
                screenState = ScreenState.TRAIN_LIST,
                confidence = 0.90f,
                suggestedAction = SuggestedAction.SELECT_TRAIN,
                evidence = evidence,
                reason = "Selectable train control detected"
            )
        }

        return AnalysisResult(
            screenState = ScreenState.TRAIN_LIST,
            confidence = 0.65f,
            suggestedAction = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Train list detected but no safe selectable train control found"
        )
    }

    private fun handleAvailability(
        evidence: ScreenEvidence
    ): AnalysisResult {

        val classCodes = listOf(
            "SL",
            "3A",
            "2A",
            "1A",
            "CC",
            "EC",
            "3E",
            "2S",
            "FC"
        )

        val availableClasses = mutableListOf<String>()

        for (element in evidence.uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (containsClassCode(text, classCodes)) {
                availableClasses.add(
                    element.text.trim()
                )
            }
        }

        if (availableClasses.isNotEmpty()) {

            val uniqueClasses =
                availableClasses
                    .distinct()
                    .joinToString(",")

            return AnalysisResult(
                screenState = ScreenState.AVAILABILITY,
                confidence = 0.90f,
                suggestedAction = SuggestedAction.SELECT_CLASS,
                extractedData = mapOf(
                    "available_classes" to uniqueClasses
                ),
                evidence = evidence,
                reason = "Available class controls detected"
            )
        }

        return AnalysisResult(
            screenState = ScreenState.AVAILABILITY,
            confidence = 0.65f,
            suggestedAction = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Availability screen detected but no class control identified"
        )
    }

    private fun handleReviewJourney(
        evidence: ScreenEvidence
    ): AnalysisResult {

        var hasProceedButton = false

        for (element in evidence.uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                text.contains("PROCEED TO PAY")
            ) {
                hasProceedButton = true
                break
            }
        }

        val fullText =
            normalizeText(
                evidence.ocrEvidence?.fullText.orEmpty()
            )

        val hasAmount =
            fullText.contains("₹") ||
                fullText.contains("TOTAL AMOUNT") ||
                fullText.contains("TOTAL FARE")

        if (
            hasProceedButton &&
            hasAmount
        ) {
            /*
             * Review can be identified, but payment transition
             * remains a user boundary.
             */
            return AnalysisResult(
                screenState = ScreenState.REVIEW_JOURNEY,
                confidence = 0.95f,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Review journey is ready; payment transition requires user confirmation"
            )
        }

        return AnalysisResult(
            screenState = ScreenState.REVIEW_JOURNEY,
            confidence = 0.75f,
            suggestedAction = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Review journey detected; awaiting user confirmation"
        )
    }

    private fun handlePaymentUPI(
        evidence: ScreenEvidence
    ): AnalysisResult {

        var provider: String? = null

        for (element in evidence.uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                text.contains("IRCTC IPAY") ||
                text.contains("PAYU") ||
                text.contains("PAYTM") ||
                text.contains("PHONEPE")
            ) {
                provider = element.text.trim()
                break
            }
        }

        val data =
            if (provider != null) {
                mapOf("provider" to provider)
            } else {
                emptyMap()
            }

        return AnalysisResult(
            screenState = ScreenState.PAYMENT_UPI,
            confidence = if (provider != null) 0.92f else 0.75f,
            suggestedAction = SuggestedAction.STOP_AWAIT_USER,
            extractedData = data,
            evidence = evidence,
            reason = "UPI payment screen detected; user confirmation required"
        )
    }

    private fun handlePaymentWallet(
        evidence: ScreenEvidence
    ): AnalysisResult {

        val fullText =
            normalizeText(
                evidence.ocrEvidence?.fullText.orEmpty()
            )

        if (
            fullText.contains("INSUFFICIENT BALANCE")
        ) {
            return AnalysisResult(
                screenState = ScreenState.PAYMENT_WALLET,
                confidence = 0.96f,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Wallet balance is insufficient"
            )
        }

        var provider: String? = null

        for (element in evidence.uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                text.contains("MOBIKWIK") ||
                text.contains("AMAZON PAY") ||
                text.contains("IRCTC WALLET")
            ) {
                provider = element.text.trim()
                break
            }
        }

        val data =
            if (provider != null) {
                mapOf("provider" to provider)
            } else {
                emptyMap()
            }

        return AnalysisResult(
            screenState = ScreenState.PAYMENT_WALLET,
            confidence = if (provider != null) 0.90f else 0.70f,
            suggestedAction = SuggestedAction.STOP_AWAIT_USER,
            extractedData = data,
            evidence = evidence,
            reason = "Wallet payment screen detected; user confirmation required"
        )
    }

    private fun handlePaymentCategory(
        evidence: ScreenEvidence
    ): AnalysisResult {

        var target: UIElement? = null

        for (element in evidence.uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                text.contains("UPI") ||
                text.contains("WALLET") ||
                text.contains("CREDIT CARD") ||
                text.contains("DEBIT CARD") ||
                text.contains("NETBANKING")
            ) {
                target = element
                break
            }
        }

        val data =
            if (target != null) {
                mapOf(
                    "category" to target.text.trim()
                )
            } else {
                emptyMap()
            }

        return AnalysisResult(
            screenState = ScreenState.PAYMENT_CATEGORY,
            confidence = if (target != null) 0.88f else 0.65f,
            suggestedAction = SuggestedAction.STOP_AWAIT_USER,
            extractedData = data,
            evidence = evidence,
            reason = "Payment category detected; user confirmation required"
        )
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private fun containsClassCode(
        text: String,
        classCodes: List<String>
    ): Boolean {

        for (code in classCodes) {

            /*
             * Avoid matching arbitrary words containing
             * letters such as "CC".
             */
            val regex =
                Regex(
                    "(^|\\s|[^A-Z0-9])${Regex.escape(code)}($|\\s|[^A-Z0-9])"
                )

            if (regex.containsMatchIn(text)) {
                return true
            }
        }

        return false
    }

    private fun hasKey(
        map: Map<String, String>,
        key: String
    ): Boolean {

        for (existingKey in map.keys) {
            if (
                existingKey.equals(
                    key,
                    ignoreCase = true
                )
            ) {
                return true
            }
        }

        return false
    }

    private fun logDebug(message: String) {
        /*
         * Logger contract is project-specific.
         *
         * Do not assume a log method signature here.
         * TAG is retained for project diagnostics.
         */
        @Suppress("UNUSED_VARIABLE")
        val tag = TAG

        @Suppress("UNUSED_VARIABLE")
        val loggerInstance = logger

        /*
         * Intentionally no direct logger invocation because
         * the current com.vmax.common.Logger contract has not
         * been supplied in this file.
         */
    }

    // ============================================================
    // PUBLIC HELPERS
    // ============================================================

    fun getCurrentUIElements(): List<UIElement> {
        return evidenceCollector
            .getCurrentEvidence()
            ?.uiElements
            ?: emptyList()
    }

    fun findUIElementByText(
        text: String
    ): UIElement? {

        if (text.isBlank()) {
            return null
        }

        val target =
            normalizeText(text)

        for (element in getCurrentUIElements()) {

            val elementText =
                normalizedElementText(element)

            if (
                elementText == target ||
                elementText.contains(target)
            ) {
                return element
            }
        }

        return null
    }

    fun findClickableUIElements(): List<UIElement> {

        val result = mutableListOf<UIElement>()

        for (element in getCurrentUIElements()) {

            if (element.isClickable) {
                result.add(element)
            }
        }

        return result
    }

    fun findEditableUIElements(): List<UIElement> {

        val result = mutableListOf<UIElement>()

        for (element in getCurrentUIElements()) {

            if (element.isEditable) {
                result.add(element)
            }
        }

        return result
    }

    fun getTextFromScreen(): String {

        return evidenceCollector
            .getCurrentEvidence()
            ?.ocrEvidence
            ?.fullText
            .orEmpty()
    }

    fun getExtractedData(): Map<String, String> {

        return evidenceCollector
            .getCurrentEvidence()
            ?.ocrEvidence
            ?.keyValuePairs
            ?: emptyMap()
    }
}
