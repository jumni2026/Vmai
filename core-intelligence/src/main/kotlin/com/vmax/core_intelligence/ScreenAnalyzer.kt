package com.vmax.core_intelligence

import com.vmax.common.Logger
import java.util.Locale

private typealias ScreenEvidence = UIEvidenceCollector.ScreenEvidence
private typealias UIElement = UIEvidenceCollector.ScreenEvidence.UIElement

/**
 * VMAX v2.6.1 – ScreenAnalyzer
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
        private const val STALE_THRESHOLD_MS = 3000L

        // Tightened keyword lists – removed overly broad terms like "BOOK", "VIEW"
        private val TRAIN_ACTION_KEYWORDS = listOf(
            "SELECT", "BOOK NOW", "TRAIN", "SEARCH", "FIND TRAINS", "CHECK"
        )

        private val CLASS_CODES = listOf("SL", "3A", "2A", "1A", "CC", "EC", "3E", "2S", "FC")
        private val PAYMENT_CATEGORY_NAMES = listOf("UPI", "WALLET", "CREDIT CARD", "DEBIT CARD", "NETBANKING", "INTERNATIONAL CARD", "EMI")
        private val UPI_PROVIDER_NAMES = listOf("IRCTC IPAY", "PAYU", "PAYTM", "PHONEPE")
        private val WALLET_PROVIDER_NAMES = listOf("MOBIKWIK", "AMAZON PAY", "IRCTC WALLET")
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
        PAYMENT_CONFIRMATION,   // now used
        LOADING,
        ERROR_SCREEN,
        COMPLETED,
        SENSITIVE_BLOCKED       // explicit security stop
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
            logDebug("No evidence available")
            return AnalysisResult(ScreenState.UNKNOWN, CONFIDENCE_UNKNOWN, SuggestedAction.NONE, reason = "No evidence")
        }

        // ---- 1. Security: SENSITIVE classification check ----
        if (evidence.metadata.classification == TextClassifier.Classification.SENSITIVE) {
            logDebug("SENSITIVE screen – stopping automation")
            return AnalysisResult(
                screenState = ScreenState.SENSITIVE_BLOCKED,
                confidence = evidence.metadata.classificationConfidence,
                suggestedAction = SuggestedAction.STOP_AWAIT_USER,
                evidence = evidence,
                reason = "Sensitive content – user control required"
            )
        }

        // ---- 2. Freshness check (avoid stale evidence) ----
        val age = System.currentTimeMillis() - evidence.timestamp
        if (age > STALE_THRESHOLD_MS) {
            logDebug("Stale evidence (${age}ms)")
            return AnalysisResult(
                ScreenState.UNKNOWN,
                CONFIDENCE_UNKNOWN,
                SuggestedAction.NONE,
                evidence = evidence,
                reason = "Stale evidence (>${STALE_THRESHOLD_MS}ms)"
            )
        }

        // ---- 3. Proceed with normal analysis ----
        val uiElements = evidence.uiElements
        val fullText = normalizeText(evidence.ocrEvidence?.fullText.orEmpty())
        val keyValuePairs = evidence.ocrEvidence?.keyValuePairs ?: emptyMap()
        val ocrConf = evidence.metadata.ocrConfidence  // used for confidence scaling

        // Detection order (most specific first)
        if (isStationConfirmationScreen(fullText, uiElements)) {
            return result(ScreenState.STATION_CONFIRMATION, 0.98f * ocrConf, SuggestedAction.CONFIRM_STATION, evidence, "Station confirmation")
        }

        if (isPaymentConfirmationScreen(fullText, uiElements)) {
            return result(ScreenState.PAYMENT_CONFIRMATION, CONFIDENCE_COMPLETED * ocrConf, SuggestedAction.STOP_AWAIT_USER, evidence, "Payment confirmation")
        }

        if (isCompletedScreen(fullText, uiElements)) {
            return result(ScreenState.COMPLETED, CONFIDENCE_COMPLETED * ocrConf, SuggestedAction.STOP_AWAIT_USER, evidence, "Booking completed")
        }

        if (isErrorScreen(fullText, uiElements)) {
            return result(ScreenState.ERROR_SCREEN, CONFIDENCE_ERROR * ocrConf, SuggestedAction.ERROR_RECOVERY, evidence, "Error screen")
        }

        if (isLoadingScreen(fullText, uiElements)) {
            return result(ScreenState.LOADING, CONFIDENCE_LOADING * ocrConf, SuggestedAction.WAIT_FOR_LOADING, evidence, "Loading")
        }

        if (isAddPassengerFormScreen(fullText, uiElements)) {
            return handleAddPassengerForm(evidence)
        }

        if (isReviewJourneyScreen(fullText, uiElements, keyValuePairs)) {
            return handleReviewJourney(evidence)
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

        if (isPassengerInputScreen(fullText, uiElements)) {
            return handlePassengerInput(evidence)
        }

        if (isAvailabilityScreen(fullText, uiElements)) {
            return handleAvailability(evidence)
        }

        if (isTrainListScreen(fullText, uiElements, keyValuePairs)) {
            return handleTrainList(evidence)
        }

        // Fallback
        return AnalysisResult(
            ScreenState.UNKNOWN,
            CONFIDENCE_UNKNOWN * ocrConf,
            SuggestedAction.NONE,
            evidence = evidence,
            reason = "No known screen signature matched"
        )
    }

    // Helper to create result with clamped confidence
    private fun result(
        state: ScreenState,
        conf: Float,
        action: SuggestedAction,
        evidence: ScreenEvidence,
        reason: String
    ): AnalysisResult =
        AnalysisResult(state, conf.coerceIn(0f, 1f), action, evidence = evidence, reason = reason)

    // ---- Normalization ----
    private fun normalizeText(value: String): String =
        value.replace('\n', ' ')
            .replace('\r', ' ')
            .replace(Regex("\\s+"), " ")
            .trim()
            .uppercase(Locale.ROOT)

    private fun normalizedElementText(element: UIElement): String = normalizeText(element.text)
    private fun normalizedHint(element: UIElement): String = normalizeText(element.hint.orEmpty())

    private fun containsAny(text: String, values: List<String>): Boolean =
        values.any { text.contains(it) }

    // ---- Screen Detection (tightened) ----
    private fun isStationConfirmationScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        val hasSearched = fullText.contains("YOU SEARCHED TRAINS FROM")
        val hasBooking = fullText.contains("BUT BOOKING FROM")
        val hasContinue = fullText.contains("CONTINUE")
        return hasSearched && hasBooking && hasContinue
    }

    private fun isPaymentConfirmationScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        val keywords = listOf("PAYMENT SUCCESSFUL", "TRANSACTION SUCCESSFUL", "PAYMENT CONFIRMED")
        return keywords.any { fullText.contains(it) } && fullText.contains("PNR")
    }

    private fun isCompletedScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        if (fullText.contains("BOOKING CONFIRMED") || fullText.contains("TICKET CONFIRMED") ||
            fullText.contains("TICKET BOOKED SUCCESSFULLY")) {
            return true
        }
        val hasPnr = fullText.contains("PNR")
        val hasConfirmed = fullText.contains("CONFIRMED") || fullText.contains("SUCCESSFUL")
        val hasTicketContext = fullText.contains("TICKET") || fullText.contains("BOOKING CONFIRMATION")
        return hasPnr && hasConfirmed && hasTicketContext
    }

    private fun isErrorScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        if (fullText.contains("SOMETHING WENT WRONG") || fullText.contains("TECHNICAL ERROR") ||
            fullText.contains("SESSION EXPIRED") || fullText.contains("NETWORK ERROR") || fullText.contains("UNABLE TO PROCESS")) {
            return true
        }
        val hasError = fullText.contains("ERROR") || fullText.contains("FAILED")
        val hasRecovery = fullText.contains("TRY AGAIN") || fullText.contains("RETRY") || fullText.contains("CLOSE") || fullText.contains("BACK")
        return hasError && hasRecovery
    }

    private fun isLoadingScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        if (fullText.contains("PLEASE WAIT") || fullText.contains("LOADING") || fullText.contains("FETCHING")) return true
        if (fullText.contains("PROCESSING") && (fullText.contains("PLEASE") || fullText.contains("WAIT") ||
                    fullText.contains("REQUEST") || fullText.contains("FETCH"))) return true
        return uiElements.any {
            val type = it.type
            type.contains("PROGRESSBAR", ignoreCase = true) ||
                    type.contains("SPINNER", ignoreCase = true) ||
                    type.contains("LOADING", ignoreCase = true)
        }
    }

    private fun isAddPassengerFormScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        var hasName = false
        var hasAge = false
        var hasGender = false
        var hasAddButton = false
        for (element in uiElements) {
            val text = normalizedElementText(element)
            val hint = normalizedHint(element)
            if (element.isEditable) {
                if (hint.contains("NAME") || text.contains("NAME")) hasName = true
                if (hint.contains("AGE") || text.contains("AGE")) hasAge = true
            }
            if (text == "MALE" || text == "FEMALE" || text == "TRANSGENDER") hasGender = true
            if (element.isClickable && text.contains("ADD PASSENGER")) hasAddButton = true
        }
        return (hasName && hasAge && hasGender) || (hasAddButton && (hasName || hasAge || hasGender))
    }

    private fun isReviewJourneyScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasReview = fullText.contains("REVIEW JOURNEY") || fullText.contains("REVIEW JOURNEY DETAILS")
        val hasPassenger = fullText.contains("PASSENGER DETAILS") || fullText.contains("PASSENGERS DETAILS")
        val hasTrain = keyValuePairs.containsKey("train_number") || keyValuePairs.containsKey("train_name") ||
                keyValuePairs.containsKey("from_station") || keyValuePairs.containsKey("to_station")
        val hasProceed = uiElements.any { it.isClickable && normalizedElementText(it).contains("PROCEED TO PAY") }
        return (hasReview && (hasPassenger || hasTrain)) || (hasProceed && (hasPassenger || hasTrain))
    }

    private fun isPaymentUPIScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        val hasUpiTitle = fullText.contains("PAY USING UPI") || fullText.contains("UPI PAYMENT") ||
                fullText.contains("ENTER UPI") || fullText.contains("UPI ID")
        val hasProvider = uiElements.any { it.isClickable && containsAny(normalizedElementText(it), UPI_PROVIDER_NAMES) }
        val hasPaymentContext = fullText.contains("PAYMENT") || fullText.contains("PAY") || fullText.contains("UPI")
        return (hasUpiTitle && hasPaymentContext) || (hasProvider && hasPaymentContext)
    }

    private fun isPaymentWalletScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        if (fullText.contains("PAY USING WALLET") || fullText.contains("WALLET PAYMENT") ||
            fullText.contains("WALLET BALANCE") || fullText.contains("INSUFFICIENT BALANCE")) return true
        val hasProvider = uiElements.any { it.isClickable && containsAny(normalizedElementText(it), WALLET_PROVIDER_NAMES) }
        return hasProvider && (fullText.contains("PAYMENT") || fullText.contains("PAY") || fullText.contains("WALLET"))
    }

    private fun isPaymentCategoryScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        val categories = uiElements.filter { it.isClickable && containsAny(normalizedElementText(it), PAYMENT_CATEGORY_NAMES) }
        val hasPaymentTitle = fullText.contains("MAKE PAYMENT") || fullText.contains("PAYMENT OPTIONS") || fullText.contains("SELECT PAYMENT")
        val hasTotal = fullText.contains("TOTAL AMOUNT") || fullText.contains("TOTAL FARE")
        return (categories.size >= 1 && hasPaymentTitle) || (categories.size >= 2 && hasTotal)
    }

    private fun isPassengerInputScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        val hasTitle = fullText.contains("PASSENGER DETAILS") || fullText.contains("PASSENGERS DETAILS")
        var hasAddNew = false
        var hasReview = false
        var hasEditable = false
        for (element in uiElements) {
            val text = normalizedElementText(element)
            if (element.isClickable) {
                if (text.contains("ADD NEW")) hasAddNew = true
                if (text.contains("REVIEW JOURNEY DETAILS") || text == "REVIEW JOURNEY") hasReview = true
            }
            if (element.isEditable) hasEditable = true
        }
        return (hasTitle && (hasAddNew || hasReview)) || (hasEditable && hasReview)
    }

    private fun isAvailabilityScreen(fullText: String, uiElements: List<UIElement>): Boolean {
        var classCount = 0
        var hasAvailability = false
        var hasRefresh = false
        for (element in uiElements) {
            val text = normalizedElementText(element)
            if (element.isClickable && containsClassCode(text, CLASS_CODES)) classCount++
            if (text.contains("AVAILABLE") || text.contains("RAC") || text.contains("WAITLIST") || text.contains("WL")) hasAvailability = true
            if (element.isClickable && text.contains("REFRESH")) hasRefresh = true
        }
        return (classCount >= 1 && hasAvailability) || (hasRefresh && (fullText.contains("AVAILABILITY") || fullText.contains("TRAIN")))
    }

    private fun isTrainListScreen(
        fullText: String,
        uiElements: List<UIElement>,
        keyValuePairs: Map<String, String>
    ): Boolean {
        val hasTrainNumber = keyValuePairs.containsKey("train_number")
        val hasTrainName = keyValuePairs.containsKey("train_name")
        val hasHeading = fullText.contains("TRAIN LIST") || fullText.contains("TRAINS") || fullText.contains("SORT BY") || fullText.contains("FIND TRAINS")
        val hasAction = uiElements.any { it.isClickable && containsAny(normalizedElementText(it), TRAIN_ACTION_KEYWORDS) }
        return ((hasTrainNumber || hasTrainName) && hasAction) || (hasHeading && hasAction)
    }

    // ---- Handlers (with OCR confidence scaling) ----
    private fun handleAddPassengerForm(evidence: ScreenEvidence): AnalysisResult {
        var nameField: UIElement? = null
        var ageField: UIElement? = null
        var hasGender = false
        var hasAddButton = false

        for (element in evidence.uiElements) {
            val text = normalizedElementText(element)
            val hint = normalizedHint(element)
            if (element.isEditable) {
                if (nameField == null && (hint.contains("NAME") || text.contains("NAME"))) nameField = element
                if (ageField == null && (hint.contains("AGE") || text.contains("AGE"))) ageField = element
            }
            if (text == "MALE" || text == "FEMALE" || text == "TRANSGENDER") hasGender = true
            if (element.isClickable && text.contains("ADD PASSENGER")) hasAddButton = true
        }

        val ocrConf = evidence.metadata.ocrConfidence
        if (nameField != null && nameField.text.isBlank()) {
            return result(ScreenState.ADD_PASSENGER_FORM, 0.96f * ocrConf, SuggestedAction.FILL_PASSENGER_NAME, evidence, "Name empty")
        }
        if (ageField != null && ageField.text.isBlank()) {
            return result(ScreenState.ADD_PASSENGER_FORM, 0.96f * ocrConf, SuggestedAction.FILL_PASSENGER_AGE, evidence, "Age empty")
        }
        if (hasGender) {
            return result(ScreenState.ADD_PASSENGER_FORM, 0.90f * ocrConf, SuggestedAction.SELECT_GENDER, evidence, "Gender selection")
        }
        if (hasAddButton) {
            return result(ScreenState.ADD_PASSENGER_FORM, 0.90f * ocrConf, SuggestedAction.ADD_PASSENGER_CONFIRM, evidence, "Add passenger button")
        }
        return result(ScreenState.ADD_PASSENGER_FORM, 0.60f * ocrConf, SuggestedAction.STOP_AWAIT_USER, evidence, "Ambiguous passenger form")
    }

    private fun handlePassengerInput(evidence: ScreenEvidence): AnalysisResult {
        var addNew: UIElement? = null
        var review: UIElement? = null
        var hasPassenger = false
        for (element in evidence.uiElements) {
            val text = normalizedElementText(element)
            if (element.isClickable) {
                if (text.contains("ADD NEW") && addNew == null) addNew = element
                if ((text.contains("REVIEW JOURNEY DETAILS") || text == "REVIEW JOURNEY") && review == null) review = element
            }
            if (text.contains("PASSENGER") || text.contains("TCCF")) hasPassenger = true
        }
        val ocrText = normalizeText(evidence.ocrEvidence?.fullText.orEmpty())
        if (ocrText.contains("PASSENGER NAME") || ocrText.contains("AGE") || ocrText.contains("GENDER")) hasPassenger = true

        val ocrConf = evidence.metadata.ocrConfidence
        if (addNew != null && !hasPassenger) {
            return result(ScreenState.PASSENGER_INPUT, 0.90f * ocrConf, SuggestedAction.ADD_PASSENGER, evidence, "Empty passenger list")
        }
        if (review != null && hasPassenger) {
            return result(ScreenState.PASSENGER_INPUT, 0.90f * ocrConf, SuggestedAction.REVIEW_JOURNEY, evidence, "Passenger data ready")
        }
        return result(ScreenState.PASSENGER_INPUT, 0.60f * ocrConf, SuggestedAction.STOP_AWAIT_USER, evidence, "Ambiguous passenger screen")
    }

    private fun handleTrainList(evidence: ScreenEvidence): AnalysisResult {
        val hasAction = evidence.uiElements.any { it.isClickable && containsAny(normalizedElementText(it), TRAIN_ACTION_KEYWORDS) }
        val ocrConf = evidence.metadata.ocrConfidence
        if (hasAction) {
            return result(ScreenState.TRAIN_LIST, 0.90f * ocrConf, SuggestedAction.SELECT_TRAIN, evidence, "Train selection available")
        }
        return result(ScreenState.TRAIN_LIST, 0.65f * ocrConf, SuggestedAction.STOP_AWAIT_USER, evidence, "No selectable train")
    }

    private fun handleAvailability(evidence: ScreenEvidence): AnalysisResult {
        val available = mutableListOf<String>()
        for (element in evidence.uiElements) {
            if (!element.isClickable) continue
            val text = normalizedElementText(element)
            if (containsClassCode(text, CLASS_CODES)) {
                val value = element.text.trim()
                if (value.isNotEmpty()) available.add(value)
            }
        }
        val ocrConf = evidence.metadata.ocrConfidence
        if (available.isNotEmpty()) {
            val data = mapOf("available_classes" to available.distinct().joinToString(","))
            return AnalysisResult(
                ScreenState.AVAILABILITY,
                0.90f * ocrConf,
                SuggestedAction.SELECT_CLASS,
                extractedData = data,
                evidence = evidence,
                reason = "Classes available"
            )
        }
        return result(ScreenState.AVAILABILITY, 0.65f * ocrConf, SuggestedAction.STOP_AWAIT_USER, evidence, "No class control")
    }

    private fun handleReviewJourney(evidence: ScreenEvidence): AnalysisResult {
        val hasProceed = evidence.uiElements.any { it.isClickable && normalizedElementText(it).contains("PROCEED TO PAY") }
        val fullText = normalizeText(evidence.ocrEvidence?.fullText.orEmpty())
        val hasAmount = fullText.contains("₹") || fullText.contains("TOTAL AMOUNT") || fullText.contains("TOTAL FARE")
        val ocrConf = evidence.metadata.ocrConfidence
        if (hasProceed && hasAmount) {
            return result(ScreenState.REVIEW_JOURNEY, 0.95f * ocrConf, SuggestedAction.STOP_AWAIT_USER, evidence, "Review ready – user boundary")
        }
        return result(ScreenState.REVIEW_JOURNEY, 0.75f * ocrConf, SuggestedAction.STOP_AWAIT_USER, evidence, "Review detected")
    }

    private fun handlePaymentUPI(evidence: ScreenEvidence): AnalysisResult {
        val provider = evidence.uiElements.firstOrNull { it.isClickable && containsAny(normalizedElementText(it), UPI_PROVIDER_NAMES) }
            ?.text?.trim()
        val ocrConf = evidence.metadata.ocrConfidence
        val data = if (!provider.isNullOrBlank()) mapOf("provider" to provider) else emptyMap()
        val conf = if (!provider.isNullOrBlank()) 0.92f * ocrConf else 0.75f * ocrConf
        return AnalysisResult(
            ScreenState.PAYMENT_UPI,
            conf,
            SuggestedAction.STOP_AWAIT_USER,
            data,
            evidence,
            "UPI payment screen"
        )
    }

    private fun handlePaymentWallet(evidence: ScreenEvidence): AnalysisResult {
        val fullText = normalizeText(evidence.ocrEvidence?.fullText.orEmpty())
        if (fullText.contains("INSUFFICIENT BALANCE")) {
            return result(ScreenState.PAYMENT_WALLET, 0.96f * evidence.metadata.ocrConfidence, SuggestedAction.STOP_AWAIT_USER, evidence, "Insufficient balance")
        }
        val provider = evidence.uiElements.firstOrNull { it.isClickable && containsAny(normalizedElementText(it), WALLET_PROVIDER_NAMES) }
            ?.text?.trim()
        val ocrConf = evidence.metadata.ocrConfidence
        val data = if (!provider.isNullOrBlank()) mapOf("provider" to provider) else emptyMap()
        val conf = if (!provider.isNullOrBlank()) 0.90f * ocrConf else 0.70f * ocrConf
        return AnalysisResult(
            ScreenState.PAYMENT_WALLET,
            conf,
            SuggestedAction.STOP_AWAIT_USER,
            data,
            evidence,
            "Wallet payment"
        )
    }

    private fun handlePaymentCategory(evidence: ScreenEvidence): AnalysisResult {
        val target = evidence.uiElements.firstOrNull { it.isClickable && containsAny(normalizedElementText(it), PAYMENT_CATEGORY_NAMES) }
        val ocrConf = evidence.metadata.ocrConfidence
        val data = if (target != null) mapOf("category" to target.text.trim()) else emptyMap()
        val conf = if (target != null) 0.88f * ocrConf else 0.65f * ocrConf
        return AnalysisResult(
            ScreenState.PAYMENT_CATEGORY,
            conf,
            SuggestedAction.STOP_AWAIT_USER,
            data,
            evidence,
            "Payment category"
        )
    }

    // ---- Helpers ----
    private fun containsClassCode(text: String, classCodes: List<String>): Boolean {
        for (code in classCodes) {
            val regex = Regex("(^|\\s|[^A-Z0-9])${Regex.escape(code)}($|\\s|[^A-Z0-9])")
            if (regex.containsMatchIn(text)) return true
        }
        return false
    }

    private fun hasKey(map: Map<String, String>, key: String): Boolean =
        map.keys.any { it.equals(key, ignoreCase = true) }

    private fun logDebug(message: String) {
        // Use actual logger if needed; currently stub to avoid Android dependencies
        // logger.debug(TAG, message)
    }

    // ---- Public Helpers (no change, but they now work with improved evidence) ----
    fun getCurrentUIElements(): List<UIElement> =
        evidenceCollector.getCurrentEvidence()?.uiElements ?: emptyList()

    fun findUIElementByText(text: String): UIElement? {
        if (text.isBlank()) return null
        val target = normalizeText(text)
        return getCurrentUIElements().firstOrNull {
            val elemText = normalizedElementText(it)
            elemText == target || elemText.contains(target)
        }
    }

    fun findClickableUIElements(): List<UIElement> =
        getCurrentUIElements().filter { it.isClickable }

    fun findEditableUIElements(): List<UIElement> =
        getCurrentUIElements().filter { it.isEditable }

    fun getTextFromScreen(): String =
        evidenceCollector.getCurrentEvidence()?.ocrEvidence?.fullText.orEmpty()

    fun getExtractedData(): Map<String, String> =
        evidenceCollector.getCurrentEvidence()?.ocrEvidence?.keyValuePairs ?: emptyMap()
}
