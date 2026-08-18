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
 * - Analyze current UI evidence.
 * - Classify the current screen.
 * - Produce a conservative suggested action.
 * - Extract useful screen data.
 *
 * IMPORTANT:
 * - This class NEVER executes an action.
 * - This class only analyzes evidence.
 * - Payment screens remain behind the user boundary.
 * - This file must remain pure JVM Kotlin.
 * - No Android API is used here.
 */
class ScreenAnalyzer(
    private val evidenceCollector: UIEvidenceCollector,
    private val logger: Logger
) {

    companion object {
        private const val TAG = "ScreenAnalyzer"

        private const val CONFIDENCE_UNKNOWN = 0.0f
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
     * Main screen analysis entry point.
     */
    fun analyzeCurrentScreen(): AnalysisResult {

        val evidence = evidenceCollector.getCurrentEvidence()

        if (evidence == null) {
            logDebug("No UI evidence available")

            return AnalysisResult(
                screenState = ScreenState.UNKNOWN,
                confidence = CONFIDENCE_UNKNOWN,
                suggestedAction = SuggestedAction.NONE,
                reason = "No evidence available"
            )
        }

        val uiElements = evidence.uiElements

        val fullText = normalizeText(
            evidence.ocrEvidence?.fullText.orEmpty()
        )

        val keyValuePairs =
            evidence.ocrEvidence?.keyValuePairs
                ?: emptyMap()

        /*
         * Detection priority:
         *
         * 1. Modal/dialog
         * 2. Completed
         * 3. Error
         * 4. Loading
         * 5. Add passenger form
         * 6. Review
         * 7. Payment
         * 8. Passenger input
         * 9. Availability
         * 10. Train list
         * 11. Unknown
         */

        if (
            isStationConfirmationScreen(
                fullText,
                uiElements
            )
        ) {
            return AnalysisResult(
                screenState = ScreenState.STATION_CONFIRMATION,
                confidence = 0.98f,
                suggestedAction = SuggestedAction.CONFIRM_STATION,
                evidence = evidence,
                reason = "Station confirmation dialog detected"
            )
        }

        if (
            isCompletedScreen(
                fullText,
                uiElements
            )
        ) {
            return AnalysisResult(
                screenState = ScreenState.COMPLETED,
                confidence = CONFIDENCE_COMPLETED,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Booking or payment completion evidence detected"
            )
        }

        if (
            isErrorScreen(
                fullText,
                uiElements
            )
        ) {
            return AnalysisResult(
                screenState = ScreenState.ERROR_SCREEN,
                confidence = CONFIDENCE_ERROR,
                suggestedAction = SuggestedAction.ERROR_RECOVERY,
                evidence = evidence,
                reason = "Error or recovery screen detected"
            )
        }

        if (
            isLoadingScreen(
                fullText,
                uiElements
            )
        ) {
            return AnalysisResult(
                screenState = ScreenState.LOADING,
                confidence = CONFIDENCE_LOADING,
                suggestedAction = SuggestedAction.WAIT_FOR_LOADING,
                evidence = evidence,
                reason = "Loading state detected"
            )
        }

        /*
         * Specific passenger form must come before
         * generic passenger input detection.
         */
        if (
            isAddPassengerFormScreen(
                fullText,
                uiElements
            )
        ) {
            return handleAddPassengerForm(evidence)
        }

        /*
         * Review must come before payment.
         */
        if (
            isReviewJourneyScreen(
                fullText,
                uiElements,
                keyValuePairs
            )
        ) {
            return handleReviewJourney(evidence)
        }

        /*
         * Payment is classified only.
         * It does not authorize payment.
         */
        if (
            isPaymentUPIScreen(
                fullText,
                uiElements
            )
        ) {
            return handlePaymentUPI(evidence)
        }

        if (
            isPaymentWalletScreen(
                fullText,
                uiElements
            )
        ) {
            return handlePaymentWallet(evidence)
        }

        if (
            isPaymentCategoryScreen(
                fullText,
                uiElements
            )
        ) {
            return handlePaymentCategory(evidence)
        }

        if (
            isPassengerInputScreen(
                fullText,
                uiElements
            )
        ) {
            return handlePassengerInput(evidence)
        }

        if (
            isAvailabilityScreen(
                fullText,
                uiElements
            )
        ) {
            return handleAvailability(evidence)
        }

        if (
            isTrainListScreen(
                fullText,
                uiElements,
                keyValuePairs
            )
        ) {
            return handleTrainList(evidence)
        }

        return AnalysisResult(
            screenState = ScreenState.UNKNOWN,
            confidence = CONFIDENCE_UNKNOWN,
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

    private fun normalizedElementText(
        element: UIElement
    ): String {
        return normalizeText(element.text)
    }

    private fun normalizedHint(
        element: UIElement
    ): String {
        return normalizeText(element.hint.orEmpty())
    }

    private fun containsAny(
        text: String,
        values: List<String>
    ): Boolean {

        for (value in values) {
            if (
                text.contains(
                    value.uppercase()
                )
            ) {
                return true
            }
        }

        return false
    }

    // ============================================================
    // SCREEN DETECTION
    // ============================================================

    private fun isStationConfirmationScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val textSignature =
            fullText.contains(
                "YOU SEARCHED TRAINS FROM"
            ) &&
            fullText.contains(
                "BUT BOOKING FROM"
            ) &&
            (
                fullText.contains(
                    "DO YOU WANT TO CONTINUE"
                ) ||
                fullText.contains(
                    "CONTINUE WITH THE SAME"
                )
            )

        if (textSignature) {
            return true
        }

        /*
         * UI fallback for dialog text not captured by OCR.
         */
        var hasSearchedFrom = false
        var hasBookingFrom = false
        var hasContinue = false

        for (element in uiElements) {

            val text = normalizedElementText(element)

            if (
                text.contains(
                    "YOU SEARCHED TRAINS FROM"
                )
            ) {
                hasSearchedFrom = true
            }

            if (
                text.contains(
                    "BUT BOOKING FROM"
                )
            ) {
                hasBookingFrom = true
            }

            if (
                text.contains("CONTINUE")
            ) {
                hasContinue = true
            }
        }

        return hasSearchedFrom &&
            hasBookingFrom &&
            hasContinue
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
                text == "MALE" ||
                text == "FEMALE" ||
                text == "TRANSGENDER" ||
                text.contains("MALE") ||
                text.contains("FEMALE") ||
                text.contains("TRANSGENDER")
            ) {
                hasGenderOption = true
            }

            if (
                element.isClickable &&
                (
                    text == "ADD PASSENGER" ||
                    text.contains("ADD PASSENGER")
                )
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
         * Button + field signature.
         */
        if (
            hasAddPassengerButton &&
            (
                hasNameField ||
                hasAgeField ||
                hasGenderOption
            )
        ) {
            return true
        }

        /*
         * OCR-supported fallback.
         */
        return fullText.contains("ADD PASSENGER") &&
            (
                hasNameField ||
                hasAgeField
            )
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

                if (
                    text.contains("ADD NEW")
                ) {
                    hasAddNewButton = true
                }

                if (
                    text.contains(
                        "REVIEW JOURNEY DETAILS"
                    ) ||
                    text == "REVIEW JOURNEY"
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
                (
                    hasAddNewButton ||
                    hasReviewButton
                )
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
                text.contains("PROCEED TO PAY")
            ) {
                hasProceedButton = true
                break
            }
        }

        if (
            hasReviewTitle &&
            (
                hasPassengerDetails ||
                hasTrainDetails
            )
        ) {
            return true
        }

        if (
            hasProceedButton &&
            (
                hasPassengerDetails ||
                hasTrainDetails
            )
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
            fullText.contains("UPI PAYMENT")

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

        return hasUpiTitle ||
            (
                hasProvider &&
                    (
                        fullText.contains("UPI") ||
                        fullText.contains("PAYMENT")
                    )
            ) ||
            fullText.contains("ENTER UPI") ||
            fullText.contains("UPI ID")
    }

    private fun isPaymentWalletScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val hasWalletTitle =
            fullText.contains("PAY USING WALLET") ||
            fullText.contains("WALLET PAYMENT")

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

        return hasWalletTitle ||
            (
                hasWalletProvider &&
                    fullText.contains("PAYMENT")
            ) ||
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
            fullText.contains("PAYMENT OPTIONS") ||
            fullText.contains("SELECT PAYMENT")

        val hasTotal =
            fullText.contains("TOTAL AMOUNT") ||
            fullText.contains("TOTAL FARE")

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
                containsClassCode(
                    text,
                    classCodes
                )
            ) {
                classCount++
            }

            if (
                text.contains("AVAILABLE") ||
                text.contains("RAC") ||
                text.contains("WAITLIST") ||
                text.contains("WAIT LIST") ||
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
                text.contains("SELECT TRAIN") ||
                text.contains("SELECT") ||
                text.contains("VIEW") ||
                text.contains("BOOK")
            ) {
                hasTrainAction = true
                break
            }
        }

        if (
            (
                hasTrainNumber ||
                hasTrainName
            ) &&
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
            fullText.contains("LOADING")

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
                ) ||
                type.contains(
                    "LOADING",
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
            fullText.contains(
                "SOMETHING WENT WRONG"
            ) ||
            fullText.contains(
                "TECHNICAL ERROR"
            ) ||
            fullText.contains(
                "SESSION EXPIRED"
            ) ||
            fullText.contains(
                "NETWORK ERROR"
            ) ||
            fullText.contains(
                "UNABLE TO PROCESS"
            )
        ) {
            return true
        }

        val hasGenericError =
            fullText.contains("ERROR") ||
            fullText.contains("FAILED")

        val hasRecovery =
            fullText.contains("TRY AGAIN") ||
            fullText.contains("RETRY") ||
            fullText.contains("CLOSE") ||
            fullText.contains("BACK")

        return hasGenericError &&
            hasRecovery
    }

    private fun isCompletedScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val directCompletion =
            fullText.contains("BOOKING CONFIRMED") ||
            fullText.contains("TICKET CONFIRMED") ||
            fullText.contains("PAYMENT SUCCESSFUL") ||
            fullText.contains("TRANSACTION SUCCESSFUL")

        if (directCompletion) {
            return true
        }

        val hasPnr =
            fullText.contains("PNR")

        val hasConfirmation =
            fullText.contains("CONFIRMED") ||
            fullText.contains("BOOKING")

        return hasPnr &&
            hasConfirmation
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
                text == "MALE" ||
                text == "FEMALE" ||
                text == "TRANSGENDER"
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
                        text.contains(
                            "REVIEW JOURNEY DETAILS"
                        ) ||
                        text == "REVIEW JOURNEY"
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

        if (
            ocrText.contains("PASSENGER")
        ) {
            hasPassengerEvidence = true
        }

        /*
         * "PASSENGER DETAILS" itself is not enough to
         * conclude that a passenger already exists.
         *
         * A visible Add New control with no concrete
         * passenger evidence is treated conservatively.
         */
        if (
            addNewButton != null &&
            !hasPassengerEvidence
        ) {
            return AnalysisResult(
                screenState = ScreenState.PASSENGER_INPUT,
                confidence = 0.90f,
                suggestedAction = SuggestedAction.ADD_PASSENGER,
                evidence = evidence,
                reason = "Passenger list appears empty"
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

        val availableClasses =
            mutableListOf<String>()

        for (element in evidence.uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text = normalizedElementText(element)

            if (
                containsClassCode(
                    text,
                    classCodes
                )
            ) {
                val value =
                    element.text.trim()

                if (value.isNotEmpty()) {
                    availableClasses.add(value)
                }
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
            return AnalysisResult(
                screenState = ScreenState.REVIEW_JOURNEY,
                confidence = 0.95f,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Review journey is ready; payment transition remains a user boundary"
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
                mapOf(
                    "provider" to provider
                )
            } else {
                emptyMap()
            }

        return AnalysisResult(
            screenState = ScreenState.PAYMENT_UPI,
            confidence =
                if (provider != null) {
                    0.92f
                } else {
                    0.75f
                },
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
            fullText.contains(
                "INSUFFICIENT BALANCE"
            )
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
                mapOf(
                    "provider" to provider
                )
            } else {
                emptyMap()
            }

        return AnalysisResult(
            screenState = ScreenState.PAYMENT_WALLET,
            confidence =
                if (provider != null) {
                    0.90f
                } else {
                    0.70f
                },
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
                text.contains("NETBANKING") ||
                text.contains("EMI")
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
            confidence =
                if (target != null) {
                    0.88f
                } else {
                    0.65f
                },
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

            val regex =
                Regex(
                    "(^|\\s|[^A-Z0-9])" +
                        Regex.escape(code) +
                        "($|\\s|[^A-Z0-9])"
                )

            if (
                regex.containsMatchIn(text)
            ) {
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

    private fun logDebug(
        message: String
    ) {
        /*
         * Logger contract is intentionally not assumed.
         *
         * This keeps core-intelligence independent from
         * Android logging APIs and from an unknown Logger
         * implementation contract.
         *
         * Keep parameters referenced to avoid compiler warnings
         * if the project enables strict warning checks.
         */
        @Suppress("UNUSED_VARIABLE")
        val ignoredTag = TAG

        @Suppress("UNUSED_VARIABLE")
        val ignoredLogger = logger

        @Suppress("UNUSED_VARIABLE")
        val ignoredMessage = message
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

        val result =
            mutableListOf<UIElement>()

        for (element in getCurrentUIElements()) {

            if (element.isClickable) {
                result.add(element)
            }
        }

        return result
    }

    fun findEditableUIElements(): List<UIElement> {

        val result =
            mutableListOf<UIElement>()

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
