package com.vmax.core_intelligence

import com.vmax.common.Logger
import java.util.Locale

private typealias ScreenEvidence = UIEvidenceCollector.ScreenEvidence
private typealias UIElement = UIEvidenceCollector.ScreenEvidence.UIElement

/**
 * VMAX v2.6.1
 *
 * ScreenAnalyzer
 *
 * Responsibility:
 * - Analyze current UI evidence.
 * - Classify the current screen conservatively.
 * - Extract useful screen data.
 * - Suggest a safe next action.
 *
 * IMPORTANT:
 * - This class NEVER executes an action.
 * - This class only analyzes evidence.
 * - Payment / transaction screens remain behind the user boundary.
 * - Sensitive screens stop automation.
 * - Stale evidence is rejected.
 * - No Android API is used here.
 * - Null / empty evidence must never crash the analyzer.
 */
class ScreenAnalyzer(
    private val evidenceCollector: UIEvidenceCollector,
    private val logger: Logger
) {

    companion object {

        private const val TAG = "ScreenAnalyzer"

        private const val CONFIDENCE_UNKNOWN = 0.0f
        private const val CONFIDENCE_LOW = 0.50f
        private const val CONFIDENCE_MEDIUM = 0.70f
        private const val CONFIDENCE_HIGH = 0.85f
        private const val CONFIDENCE_VERY_HIGH = 0.95f
        private const val CONFIDENCE_CERTAIN = 1.0f

        /**
         * Evidence older than this is considered unsafe for a
         * new workflow recommendation.
         */
        private const val STALE_THRESHOLD_MS = 3000L

        private val TRAIN_ACTION_KEYWORDS = listOf(
            "SELECT",
            "BOOK NOW",
            "FIND TRAINS",
            "SEARCH"
        )

        private val CLASS_CODES = listOf(
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

        private val PAYMENT_CATEGORY_NAMES = listOf(
            "UPI",
            "WALLET",
            "CREDIT CARD",
            "DEBIT CARD",
            "NETBANKING",
            "NET BANKING",
            "INTERNATIONAL CARD",
            "EMI",
            "AUTOPAY"
        )

        private val UPI_PROVIDER_NAMES = listOf(
            "IRCTC IPAY",
            "PAYU",
            "PAYTM",
            "PHONEPE"
        )

        private val WALLET_PROVIDER_NAMES = listOf(
            "MOBIKWIK",
            "AMAZON PAY",
            "IRCTC WALLET"
        )
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

        COMPLETED,

        SENSITIVE_BLOCKED
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

    // ============================================================
    // MAIN ANALYSIS
    // ============================================================

    fun analyzeCurrentScreen(): AnalysisResult {

        val evidence = evidenceCollector.getCurrentEvidence()

        if (evidence == null) {
            logDebug("No evidence available")

            return result(
                state = ScreenState.UNKNOWN,
                confidence = CONFIDENCE_UNKNOWN,
                action = SuggestedAction.NONE,
                reason = "No screen evidence available"
            )
        }

        // --------------------------------------------------------
        // 1. SECURITY BOUNDARY
        // --------------------------------------------------------

        if (
            evidence.metadata.classification ==
            TextClassifier.Classification.SENSITIVE
        ) {

            logDebug("Sensitive screen detected")

            return result(
                state = ScreenState.SENSITIVE_BLOCKED,
                confidence = evidence.metadata.classificationConfidence,
                action = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Sensitive content detected; user control required"
            )
        }

        // --------------------------------------------------------
        // 2. STALE EVIDENCE PROTECTION
        // --------------------------------------------------------

        val age = System.currentTimeMillis() - evidence.timestamp

        if (age < 0L || age > STALE_THRESHOLD_MS) {

            logDebug("Stale evidence: ${age}ms")

            return result(
                state = ScreenState.UNKNOWN,
                confidence = CONFIDENCE_UNKNOWN,
                action = SuggestedAction.NONE,
                evidence = evidence,
                reason = "Evidence is stale or timestamp is invalid"
            )
        }

        // --------------------------------------------------------
        // 3. NORMALIZED EVIDENCE
        // --------------------------------------------------------

        val uiElements = evidence.uiElements

        val fullText = normalizeText(
            evidence.ocrEvidence?.fullText.orEmpty()
        )

        val keyValuePairs =
            evidence.ocrEvidence?.keyValuePairs
                ?: emptyMap()

        val ocrConfidence =
            evidence.metadata.ocrConfidence
                .coerceIn(
                    CONFIDENCE_UNKNOWN,
                    CONFIDENCE_CERTAIN
                )

        // --------------------------------------------------------
        // 4. DETECTION ORDER
        //
        // Most specific / dangerous states first.
        // --------------------------------------------------------

        if (
            isStationConfirmationScreen(
                fullText,
                uiElements
            )
        ) {

            return result(
                state = ScreenState.STATION_CONFIRMATION,
                confidence = 0.98f * ocrConfidence,
                action = SuggestedAction.CONFIRM_STATION,
                evidence = evidence,
                reason = "Station confirmation screen detected"
            )
        }

        if (
            isPaymentConfirmationScreen(
                fullText,
                uiElements
            )
        ) {

            return result(
                state = ScreenState.PAYMENT_CONFIRMATION,
                confidence = CONFIDENCE_CERTAIN * ocrConfidence,
                action = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Payment confirmation detected"
            )
        }

        if (
            isCompletedScreen(
                fullText,
                uiElements
            )
        ) {

            return result(
                state = ScreenState.COMPLETED,
                confidence = CONFIDENCE_CERTAIN * ocrConfidence,
                action = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Booking completion detected"
            )
        }

        if (
            isErrorScreen(
                fullText,
                uiElements
            )
        ) {

            return result(
                state = ScreenState.ERROR_SCREEN,
                confidence = CONFIDENCE_HIGH * ocrConfidence,
                action = SuggestedAction.ERROR_RECOVERY,
                evidence = evidence,
                reason = "Error screen detected"
            )
        }

        if (
            isLoadingScreen(
                fullText,
                uiElements
            )
        ) {

            return result(
                state = ScreenState.LOADING,
                confidence = 0.90f * ocrConfidence,
                action = SuggestedAction.WAIT_FOR_LOADING,
                evidence = evidence,
                reason = "Loading/progress state detected"
            )
        }

        // --------------------------------------------------------
        // 5. TRANSACTION / REVIEW BOUNDARIES
        // --------------------------------------------------------

        if (
            isReviewJourneyScreen(
                fullText,
                uiElements,
                keyValuePairs
            )
        ) {

            return handleReviewJourney(evidence)
        }

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

        // --------------------------------------------------------
        // 6. PASSENGER
        // --------------------------------------------------------

        if (
            isAddPassengerFormScreen(
                fullText,
                uiElements
            )
        ) {

            return handleAddPassengerForm(evidence)
        }

        if (
            isPassengerInputScreen(
                fullText,
                uiElements
            )
        ) {

            return handlePassengerInput(evidence)
        }

        // --------------------------------------------------------
        // 7. TRAIN / AVAILABILITY
        // --------------------------------------------------------

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

        // --------------------------------------------------------
        // 8. UNKNOWN
        // --------------------------------------------------------

        return result(
            state = ScreenState.UNKNOWN,
            confidence = CONFIDENCE_UNKNOWN,
            action = SuggestedAction.NONE,
            evidence = evidence,
            reason = "No known screen signature matched"
        )
    }

    // ============================================================
    // SCREEN DETECTION
    // ============================================================

    private fun isStationConfirmationScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val hasSearched =
            fullText.contains(
                "YOU SEARCHED TRAINS FROM"
            )

        val hasBooking =
            fullText.contains(
                "BUT BOOKING FROM"
            )

        val hasContinue =
            fullText.contains(
                "CONTINUE"
            )

        return hasSearched &&
            hasBooking &&
            hasContinue
    }

    private fun isPaymentConfirmationScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val paymentSuccess =
            fullText.contains("PAYMENT SUCCESSFUL") ||
                fullText.contains("TRANSACTION SUCCESSFUL") ||
                fullText.contains("PAYMENT CONFIRMED")

        val hasPnr =
            fullText.contains("PNR")

        return paymentSuccess && hasPnr
    }

    private fun isCompletedScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        if (
            fullText.contains("BOOKING CONFIRMED") ||
            fullText.contains("TICKET CONFIRMED") ||
            fullText.contains("TICKET BOOKED SUCCESSFULLY") ||
            fullText.contains("PNR GENERATED") ||
            fullText.contains("THANK YOU FOR BOOKING")
        ) {
            return true
        }

        val hasPnr =
            fullText.contains("PNR")

        val hasConfirmed =
            fullText.contains("CONFIRMED") ||
                fullText.contains("SUCCESSFUL")

        val hasTicketContext =
            fullText.contains("TICKET") ||
                fullText.contains("BOOKING CONFIRMATION")

        return hasPnr &&
            hasConfirmed &&
            hasTicketContext
    }

    private fun isErrorScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val strongErrors = listOf(
            "SOMETHING WENT WRONG",
            "TECHNICAL ERROR",
            "SESSION EXPIRED",
            "NETWORK ERROR",
            "UNABLE TO PROCESS",
            "REQUEST FAILED",
            "SERVER ERROR"
        )

        if (
            strongErrors.any {
                fullText.contains(it)
            }
        ) {
            return true
        }

        val hasError =
            fullText.contains("ERROR") ||
                fullText.contains("FAILED")

        val hasRecovery =
            fullText.contains("TRY AGAIN") ||
                fullText.contains("RETRY") ||
                fullText.contains("CLOSE") ||
                fullText.contains("BACK")

        return hasError && hasRecovery
    }

    private fun isLoadingScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val directLoading =
            fullText.contains("PLEASE WAIT") ||
                fullText.contains("LOADING") ||
                fullText.contains("FETCHING") ||
                fullText.contains("SEARCHING")

        if (directLoading) {
            return true
        }

        val processing =
            fullText.contains("PROCESSING") &&
                (
                    fullText.contains("PLEASE") ||
                        fullText.contains("WAIT") ||
                        fullText.contains("REQUEST") ||
                        fullText.contains("FETCH")
                    )

        if (processing) {
            return true
        }

        return uiElements.any { element ->

            val type =
                normalizeText(element.type)

            type.contains("PROGRESSBAR") ||
                type.contains("SPINNER") ||
                type.contains("LOADING")
        }
    }

    private fun isAddPassengerFormScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        var hasName = false
        var hasAge = false
        var hasGender = false
        var hasAddButton = false

        for (element in uiElements) {

            val text =
                normalizedElementText(element)

            val hint =
                normalizedHint(element)

            if (element.isEditable) {

                if (
                    hint.contains("NAME") ||
                    text.contains("NAME")
                ) {
                    hasName = true
                }

                if (
                    hint.contains("AGE") ||
                    text.contains("AGE")
                ) {
                    hasAge = true
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
                (
                    text.contains("ADD PASSENGER") ||
                        text.contains("ADD NEW PASSENGER")
                    )
            ) {
                hasAddButton = true
            }
        }

        val strongForm =
            hasName &&
                hasAge &&
                hasGender

        val headingEvidence =
            fullText.contains("PASSENGER DETAILS") &&
                (
                    hasName ||
                        hasAge
                    )

        val partialForm =
            hasAddButton &&
                (
                    hasName ||
                        hasAge ||
                        hasGender
                    )

        return strongForm ||
            headingEvidence ||
            partialForm
    }

    private fun isReviewJourneyScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {

        val hasReview =
            fullText.contains("REVIEW JOURNEY") ||
                fullText.contains("REVIEW JOURNEY DETAILS")

        val hasPassenger =
            fullText.contains("PASSENGER DETAILS") ||
                fullText.contains("PASSENGERS DETAILS")

        val hasTrain =
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

        val hasProceed =
            hasClickableText(
                uiElements,
                "PROCEED TO PAY"
            )

        return (
            hasReview &&
                (
                    hasPassenger ||
                        hasTrain
                    )
            ) ||
            (
                hasProceed &&
                    (
                        hasPassenger ||
                            hasTrain
                        )
                )
    }

    private fun isPaymentUPIScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val hasUpiTitle =
            fullText.contains("PAY USING UPI") ||
                fullText.contains("UPI PAYMENT") ||
                fullText.contains("ENTER UPI") ||
                fullText.contains("UPI ID")

        val hasProvider =
            uiElements.any { element ->

                element.isClickable &&
                    containsAny(
                        normalizedElementText(element),
                        UPI_PROVIDER_NAMES
                    )
            }

        val hasPaymentContext =
            fullText.contains("PAYMENT") ||
                fullText.contains("UPI")

        return (
            hasUpiTitle &&
                hasPaymentContext
            ) ||
            (
                hasProvider &&
                    hasPaymentContext
                )
    }

    private fun isPaymentWalletScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        if (
            fullText.contains("PAY USING WALLET") ||
            fullText.contains("WALLET PAYMENT") ||
            fullText.contains("WALLET BALANCE") ||
            fullText.contains("INSUFFICIENT BALANCE")
        ) {
            return true
        }

        val hasProvider =
            uiElements.any { element ->

                element.isClickable &&
                    containsAny(
                        normalizedElementText(element),
                        WALLET_PROVIDER_NAMES
                    )
            }

        return hasProvider &&
            (
                fullText.contains("PAYMENT") ||
                    fullText.contains("PAY") ||
                    fullText.contains("WALLET")
                )
    }

    private fun isPaymentCategoryScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val categoryCount =
            countClickableMatches(
                uiElements,
                PAYMENT_CATEGORY_NAMES
            )

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

    private fun isPassengerInputScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        val hasTitle =
            fullText.contains("PASSENGER DETAILS") ||
                fullText.contains("PASSENGERS DETAILS")

        var hasAddNew = false
        var hasReview = false
        var hasEditable = false

        for (element in uiElements) {

            val text =
                normalizedElementText(element)

            if (element.isClickable) {

                if (
                    text.contains("ADD NEW") ||
                    text.contains("ADD PASSENGER")
                ) {
                    hasAddNew = true
                }

                if (
                    text.contains("REVIEW JOURNEY DETAILS") ||
                    text == "REVIEW JOURNEY"
                ) {
                    hasReview = true
                }
            }

            if (element.isEditable) {
                hasEditable = true
            }
        }

        return (
            hasTitle &&
                (
                    hasAddNew ||
                        hasReview
                    )
            ) ||
            (
                hasEditable &&
                    hasReview
                )
    }

    private fun isAvailabilityScreen(
        fullText: String,
        uiElements: List<UIElement>
    ): Boolean {

        var classCount = 0
        var hasAvailability = false
        var hasRefresh = false

        for (element in uiElements) {

            val text =
                normalizedElementText(element)

            if (
                element.isClickable &&
                containsClassCode(
                    text,
                    CLASS_CODES
                )
            ) {
                classCount++
            }

            if (
                text.contains("AVAILABLE") ||
                text.contains("RAC") ||
                text.contains("WAITLIST") ||
                text.contains("WL")
            ) {
                hasAvailability = true
            }

            if (
                element.isClickable &&
                text.contains("REFRESH")
            ) {
                hasRefresh = true
            }
        }

        return (
            classCount >= 1 &&
                hasAvailability
            ) ||
            (
                hasRefresh &&
                    (
                        fullText.contains("AVAILABILITY") ||
                            fullText.contains("TRAIN")
                        )
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

        val hasHeading =
            fullText.contains("TRAIN LIST") ||
                fullText.contains("TRAINS") ||
                fullText.contains("SORT BY") ||
                fullText.contains("FIND TRAINS")

        val hasTrainAction =
            uiElements.any { element ->

                element.isClickable &&
                    containsAny(
                        normalizedElementText(element),
                        TRAIN_ACTION_KEYWORDS
                    )
            }

        /*
         * Train list should not be detected only because
         * some generic clickable element exists.
         */
        return (
            (
                hasTrainNumber ||
                    hasTrainName
                ) &&
                hasTrainAction
            ) ||
            (
                hasHeading &&
                    hasTrainAction
                )
    }

    // ============================================================
    // HANDLERS
    // ============================================================

    private fun handleAddPassengerForm(
        evidence: ScreenEvidence
    ): AnalysisResult {

        var nameField: UIElement? = null
        var ageField: UIElement? = null
        var hasGender = false
        var hasAddButton = false

        for (element in evidence.uiElements) {

            val text =
                normalizedElementText(element)

            val hint =
                normalizedHint(element)

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
                (
                    text.contains("ADD PASSENGER") ||
                        text.contains("ADD NEW PASSENGER")
                    )
            ) {
                hasAddButton = true
            }
        }

        val ocrConfidence =
            evidence.metadata.ocrConfidence

        if (
            nameField != null &&
            normalizeText(
                nameField.text
            ).isEmpty()
        ) {

            return result(
                state = ScreenState.ADD_PASSENGER_FORM,
                confidence = 0.96f * ocrConfidence,
                action = SuggestedAction.FILL_PASSENGER_NAME,
                evidence = evidence,
                reason = "Passenger name field is empty"
            )
        }

        if (
            ageField != null &&
            normalizeText(
                ageField.text
            ).isEmpty()
        ) {

            return result(
                state = ScreenState.ADD_PASSENGER_FORM,
                confidence = 0.96f * ocrConfidence,
                action = SuggestedAction.FILL_PASSENGER_AGE,
                evidence = evidence,
                reason = "Passenger age field is empty"
            )
        }

        if (hasGender) {

            return result(
                state = ScreenState.ADD_PASSENGER_FORM,
                confidence = 0.90f * ocrConfidence,
                action = SuggestedAction.SELECT_GENDER,
                evidence = evidence,
                reason = "Gender selection detected"
            )
        }

        if (hasAddButton) {

            return result(
                state = ScreenState.ADD_PASSENGER_FORM,
                confidence = 0.90f * ocrConfidence,
                action = SuggestedAction.ADD_PASSENGER_CONFIRM,
                evidence = evidence,
                reason = "Add passenger confirmation control detected"
            )
        }

        return result(
            state = ScreenState.ADD_PASSENGER_FORM,
            confidence = CONFIDENCE_MEDIUM * ocrConfidence,
            action = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Passenger form detected but next field is ambiguous"
        )
    }

    private fun handlePassengerInput(
        evidence: ScreenEvidence
    ): AnalysisResult {

        var addNew: UIElement? = null
        var review: UIElement? = null

        var hasPassenger = false

        for (element in evidence.uiElements) {

            val text =
                normalizedElementText(element)

            if (element.isClickable) {

                if (
                    (
                        text.contains("ADD NEW") ||
                            text.contains("ADD PASSENGER")
                        ) &&
                    addNew == null
                ) {
                    addNew = element
                }

                if (
                    (
                        text.contains("REVIEW JOURNEY DETAILS") ||
                            text == "REVIEW JOURNEY"
                        ) &&
                    review == null
                ) {
                    review = element
                }
            }

            if (
                text.contains("PASSENGER 1") ||
                text.contains("PASSENGER") ||
                text.contains("TCCF")
            ) {
                hasPassenger = true
            }
        }

        val ocrText =
            normalizeText(
                evidence.ocrEvidence?.fullText.orEmpty()
            )

        if (
            ocrText.contains("PASSENGER NAME") ||
            (
                ocrText.contains("AGE") &&
                    ocrText.contains("GENDER")
                )
        ) {
            hasPassenger = true
        }

        val ocrConfidence =
            evidence.metadata.ocrConfidence

        if (
            addNew != null &&
            !hasPassenger
        ) {

            return result(
                state = ScreenState.PASSENGER_INPUT,
                confidence = 0.90f * ocrConfidence,
                action = SuggestedAction.ADD_PASSENGER,
                evidence = evidence,
                reason = "Passenger input screen detected; no passenger data found"
            )
        }

        if (
            review != null &&
            hasPassenger
        ) {

            return result(
                state = ScreenState.PASSENGER_INPUT,
                confidence = 0.90f * ocrConfidence,
                action = SuggestedAction.REVIEW_JOURNEY,
                evidence = evidence,
                reason = "Passenger data appears present and review control is available"
            )
        }

        return result(
            state = ScreenState.PASSENGER_INPUT,
            confidence = CONFIDENCE_MEDIUM * ocrConfidence,
            action = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Passenger screen detected but state is ambiguous"
        )
    }

    private fun handleTrainList(
        evidence: ScreenEvidence
    ): AnalysisResult {

        val hasAction =
            evidence.uiElements.any { element ->

                element.isClickable &&
                    containsAny(
                        normalizedElementText(element),
                        TRAIN_ACTION_KEYWORDS
                    )
            }

        val ocrConfidence =
            evidence.metadata.ocrConfidence

        if (hasAction) {

            return result(
                state = ScreenState.TRAIN_LIST,
                confidence = 0.90f * ocrConfidence,
                action = SuggestedAction.SELECT_TRAIN,
                evidence = evidence,
                reason = "Train selection control detected"
            )
        }

        return result(
            state = ScreenState.TRAIN_LIST,
            confidence = 0.65f * ocrConfidence,
            action = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Train list detected but target train is ambiguous"
        )
    }

    private fun handleAvailability(
        evidence: ScreenEvidence
    ): AnalysisResult {

        val availableClasses =
            mutableListOf<String>()

        for (element in evidence.uiElements) {

            if (!element.isClickable) {
                continue
            }

            val text =
                normalizedElementText(element)

            if (
                containsClassCode(
                    text,
                    CLASS_CODES
                )
            ) {

                val value =
                    element.text.trim()

                if (value.isNotEmpty()) {
                    availableClasses.add(value)
                }
            }
        }

        val ocrConfidence =
            evidence.metadata.ocrConfidence

        if (availableClasses.isNotEmpty()) {

            val data =
                mapOf(
                    "available_classes" to
                        availableClasses
                            .distinct()
                            .joinToString(",")
                )

            return result(
                state = ScreenState.AVAILABILITY,
                confidence = 0.90f * ocrConfidence,
                action = SuggestedAction.SELECT_CLASS,
                extractedData = data,
                evidence = evidence,
                reason = "Selectable class controls detected"
            )
        }

        return result(
            state = ScreenState.AVAILABILITY,
            confidence = 0.65f * ocrConfidence,
            action = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Availability screen detected but no clear class control found"
        )
    }

    // ============================================================
    // PAYMENT / TRANSACTION BOUNDARY
    // ============================================================

    private fun handleReviewJourney(
        evidence: ScreenEvidence
    ): AnalysisResult {

        val hasProceed =
            hasClickableText(
                evidence.uiElements,
                "PROCEED TO PAY"
            )

        val fullText =
            normalizeText(
                evidence.ocrEvidence?.fullText.orEmpty()
            )

        val hasAmount =
            fullText.contains("₹") ||
                fullText.contains("TOTAL AMOUNT") ||
                fullText.contains("TOTAL FARE")

        val ocrConfidence =
            evidence.metadata.ocrConfidence

        val confidence =
            if (
                hasProceed &&
                hasAmount
            ) {
                0.95f * ocrConfidence
            } else {
                0.75f * ocrConfidence
            }

        /*
         * IMPORTANT:
         *
         * Review screen may lead to a financial transaction.
         * Therefore no automatic PROCEED_TO_PAY action is returned.
         */
        return result(
            state = ScreenState.REVIEW_JOURNEY,
            confidence = confidence,
            action = SuggestedAction.STOP_AWAIT_USER,
            evidence = evidence,
            reason = "Journey review detected; user confirmation required before transaction"
        )
    }

    private fun handlePaymentUPI(
        evidence: ScreenEvidence
    ): AnalysisResult {

        val provider =
            evidence.uiElements
                .firstOrNull { element ->

                    element.isClickable &&
                        containsAny(
                            normalizedElementText(element),
                            UPI_PROVIDER_NAMES
                        )
                }
                ?.text
                ?.trim()

        val ocrConfidence =
            evidence.metadata.ocrConfidence

        val data =
            if (!provider.isNullOrBlank()) {
                mapOf(
                    "payment_provider" to provider
                )
            } else {
                emptyMap()
            }

        val confidence =
            if (!provider.isNullOrBlank()) {
                0.92f * ocrConfidence
            } else {
                0.75f * ocrConfidence
            }

        return result(
            state = ScreenState.PAYMENT_UPI,
            confidence = confidence,
            action = SuggestedAction.STOP_AWAIT_USER,
            extractedData = data,
            evidence = evidence,
            reason = "UPI payment boundary detected; user confirmation required"
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

            return result(
                state = ScreenState.PAYMENT_WALLET,
                confidence = 0.96f *
                    evidence.metadata.ocrConfidence,
                action = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Insufficient wallet balance detected"
            )
        }

        val provider =
            evidence.uiElements
                .firstOrNull { element ->

                    element.isClickable &&
                        containsAny(
                            normalizedElementText(element),
                            WALLET_PROVIDER_NAMES
                        )
                }
                ?.text
                ?.trim()

        val ocrConfidence =
            evidence.metadata.ocrConfidence

        val data =
            if (!provider.isNullOrBlank()) {
                mapOf(
                    "payment_provider" to provider
                )
            } else {
                emptyMap()
            }

        val confidence =
            if (!provider.isNullOrBlank()) {
                0.90f * ocrConfidence
            } else {
                0.70f * ocrConfidence
            }

        return result(
            state = ScreenState.PAYMENT_WALLET,
            confidence = confidence,
            action = SuggestedAction.STOP_AWAIT_USER,
            extractedData = data,
            evidence = evidence,
            reason = "Wallet payment boundary detected; user confirmation required"
        )
    }

    private fun handlePaymentCategory(
        evidence: ScreenEvidence
    ): AnalysisResult {

        val target =
            evidence.uiElements.firstOrNull { element ->

                element.isClickable &&
                    containsAny(
                        normalizedElementText(element),
                        PAYMENT_CATEGORY_NAMES
                    )
            }

        val ocrConfidence =
            evidence.metadata.ocrConfidence

        val data =
            if (target != null) {
                mapOf(
                    "payment_category" to
                        target.text.trim()
                )
            } else {
                emptyMap()
            }

        val confidence =
            if (target != null) {
                0.88f * ocrConfidence
            } else {
                0.65f * ocrConfidence
            }

        return result(
            state = ScreenState.PAYMENT_CATEGORY,
            confidence = confidence,
            action = SuggestedAction.STOP_AWAIT_USER,
            extractedData = data,
            evidence = evidence,
            reason = "Payment category boundary detected; user selection required"
        )
    }

    // ============================================================
    // PASSENGER DETECTION
    // ============================================================

    private fun detectExistingPassenger(
        evidence: ScreenEvidence
    ): Boolean {

        val ocrText =
            normalizeText(
                evidence.ocrEvidence?.fullText.orEmpty()
            )

        if (
            ocrText.contains("PASSENGER 1") ||
            (
                ocrText.contains("PASSENGER DETAILS") &&
                    (
                        ocrText.contains("AGE") ||
                            ocrText.contains("GENDER")
                        )
                )
        ) {
            return true
        }

        return evidence.uiElements.any { element ->

            val text =
                normalizedElementText(element)

            text.contains("PASSENGER 1") ||
                text.contains("ADULT") ||
                text.contains("TCCF")
        }
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

        return getCurrentUIElements()
            .firstOrNull { element ->

                val elementText =
                    normalizedElementText(element)

                elementText == target ||
                    elementText.contains(target)
            }
    }

    fun findClickableUIElements(): List<UIElement> {

        return getCurrentUIElements()
            .filter { it.isClickable }
    }

    fun findEditableUIElements(): List<UIElement> {

        return getCurrentUIElements()
            .filter { it.isEditable }
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

    fun getScreenState(): ScreenState {

        return analyzeCurrentScreen()
            .screenState
    }

    fun getSuggestedAction(): SuggestedAction {

        return analyzeCurrentScreen()
            .suggestedAction
    }

    // ============================================================
    // INTERNAL HELPERS
    // ============================================================

    private fun result(
        state: ScreenState,
        confidence: Float,
        action: SuggestedAction,
        evidence: ScreenEvidence? = null,
        extractedData: Map<String, String> = emptyMap(),
        reason: String
    ): AnalysisResult {

        return AnalysisResult(
            screenState = state,
            confidence = confidence.coerceIn(
                CONFIDENCE_UNKNOWN,
                CONFIDENCE_CERTAIN
            ),
            suggestedAction = action,
            extractedData = extractedData,
            evidence = evidence,
            reason = reason
        )
    }

    private fun normalizeText(
        value: String
    ): String {

        return value
            .replace('\n', ' ')
            .replace('\r', ' ')
            .replace(Regex("\\s+"), " ")
            .trim()
            .uppercase(Locale.ROOT)
    }

    private fun normalizedElementText(
        element: UIElement
    ): String {

        return normalizeText(
            element.text
        )
    }

    private fun normalizedHint(
        element: UIElement
    ): String {

        return normalizeText(
            element.hint.orEmpty()
        )
    }

    private fun containsAny(
        text: String,
        values: List<String>
    ): Boolean {

        return values.any { value ->

            text.contains(
                normalizeText(value)
            )
        }
    }

    private fun containsClassCode(
        text: String,
        classCodes: List<String>
    ): Boolean {

        val normalized =
            normalizeText(text)

        for (code in classCodes) {

            val normalizedCode =
                normalizeText(code)

            if (normalizedCode.isBlank()) {
                continue
            }

            val regex =
                Regex(
                    "(^|\\s|[^A-Z0-9])" +
                        Regex.escape(normalizedCode) +
                        "($|\\s|[^A-Z0-9])"
                )

            if (
                regex.containsMatchIn(normalized)
            ) {
                return true
            }
        }

        return false
    }

    private fun hasClickableText(
        elements: List<UIElement>,
        vararg values: String
    ): Boolean {

        return elements.any { element ->

            element.isClickable &&
                containsAny(
                    normalizedElementText(element),
                    values.toList()
                )
        }
    }

    private fun findClickableElement(
        elements: List<UIElement>,
        vararg values: String
    ): UIElement? {

        return elements.firstOrNull { element ->

            element.isClickable &&
                containsAny(
                    normalizedElementText(element),
                    values.toList()
                )
        }
    }

    private fun countClickableMatches(
        elements: List<UIElement>,
        values: List<String>
    ): Int {

        return elements.count { element ->

            element.isClickable &&
                values.any { value ->

                    normalizedElementText(element)
                        .contains(
                            normalizeText(value)
                        )
                }
        }
    }

    private fun hasKey(
        map: Map<String, String>,
        key: String
    ): Boolean {

        return map.keys.any {
            it.equals(
                key,
                ignoreCase = true
            )
        }
    }

    private fun logDebug(
        message: String
    ) {

        /*
         * Logger API is intentionally not assumed.
         *
         * This keeps ScreenAnalyzer pure JVM-compatible
         * and prevents compile failures caused by an unknown
         * Logger method contract.
         */
        @Suppress("UNUSED_VARIABLE")
        val ignoredLogger = logger

        @Suppress("UNUSED_VARIABLE")
        val ignoredTag = TAG

        @Suppress("UNUSED_VARIABLE")
        val ignoredMessage = message
    }
}
