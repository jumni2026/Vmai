package com.vmax.core_intelligence

import com.vmax.common.Logger
import com.vmax.common.LogLevel

/**
 * VMAX v2.6.1 - UI Evidence Collector (OCR Integration)
 *
 * Responsibility: Allowed OCR evidence को existing UI evidence system में जोड़ना
 *
 * Architecture Rule:
 * - सिर्फ SAFE_UI evidence accept करेगा
 * - SENSITIVE evidence explicitly reject करेगा
 * - Existing evidence structure में seamless integrate होगा
 */
class UIEvidenceCollector(
    private val logger: Logger
) {

    companion object {
        private const val TAG = "UIEvidenceCollector"
    }

    data class ScreenEvidence(
        val screenId: String,
        val timestamp: Long,
        val uiElements: List<UIElement> = emptyList(),
        val ocrEvidence: OcrEvidence? = null,
        val metadata: EvidenceMetadata = EvidenceMetadata()
    ) {
        data class OcrEvidence(
            val fullText: String,
            val keyValuePairs: Map<String, String>,
            val rawBlocks: List<OcrResult.TextBlock>,
            val confidence: Float
        )

        data class UIElement(
            val id: String,
            val type: String,
            val text: String,
            val contentDescription: String?,
            val bounds: OcrResult.BoundingBox? = null,
            val isClickable: Boolean,
            val isEditable: Boolean,
            val hint: String? = null
        )

        data class EvidenceMetadata(
            val hasOcrData: Boolean = false,
            val ocrConfidence: Float = 0f,
            val elementCount: Int = 0,
            val classification: TextClassifier.Classification = TextClassifier.Classification.UNKNOWN
        )
    }

    private var currentEvidence: ScreenEvidence? = null

    fun collectOcrEvidence(classifiedResult: TextClassifier.ClassifiedResult): CollectionResult {
        if (classifiedResult.classification == TextClassifier.Classification.SENSITIVE) {
            logger.warn(TAG, "SENSITIVE evidence rejected - screenId: ${classifiedResult.ocrResult.screenId}")
            return CollectionResult.Rejected(
                reason = RejectionReason.SENSITIVE_CONTENT_BLOCKED,
                matchedPatterns = classifiedResult.matchedPatterns
            )
        }

        if (classifiedResult.classification == TextClassifier.Classification.UNKNOWN) {
            logger.debug(TAG, "UNKNOWN evidence retained for analysis - screenId: ${classifiedResult.ocrResult.screenId}")
            return CollectionResult.RetainedAsUnknown(
                screenId = classifiedResult.ocrResult.screenId
            )
        }

        return processSafeEvidence(classifiedResult)
    }

    private fun processSafeEvidence(classifiedResult: TextClassifier.ClassifiedResult): CollectionResult {
        val ocrResult = classifiedResult.ocrResult
        val keyValuePairs = extractKeyValuePairs(ocrResult)
        val ocrEvidence = ScreenEvidence.OcrEvidence(
            fullText = ocrResult.getCleanedText(),
            keyValuePairs = keyValuePairs,
            rawBlocks = ocrResult.textBlocks,
            confidence = ocrResult.getAverageConfidence()
        )
        val evidence = ScreenEvidence(
            screenId = ocrResult.screenId,
            timestamp = ocrResult.timestamp,
            uiElements = currentEvidence?.uiElements ?: emptyList(),
            ocrEvidence = ocrEvidence,
            metadata = ScreenEvidence.EvidenceMetadata(
                hasOcrData = true,
                ocrConfidence = ocrEvidence.confidence,
                elementCount = ocrResult.textBlocks.size,
                classification = TextClassifier.Classification.SAFE_UI
            )
        )
        currentEvidence = evidence
        logger.info(
            TAG,
            "SAFE_UI evidence collected - screenId: ${evidence.screenId}, keys: ${keyValuePairs.keys}, confidence: ${ocrEvidence.confidence}"
        )
        return CollectionResult.Success(
            evidence = evidence,
            extractedKeys = keyValuePairs.keys.toList()
        )
    }

    /**
     * ✅ UPGRADED: Robust Regex Extraction
     * - अब "TRAIN NO. 12345", "FROM STATION : DELHI", "3A", "AVAILABLE" सब पकड़ता है।
     */
    private fun extractKeyValuePairs(ocrResult: OcrResult): Map<String, String> {
        val pairs = mutableMapOf<String, String>()
        val text = ocrResult.fullText

        // Train Number (Handles "TRAIN", "TRAIN NO", "TRAIN NO." etc.)
        val trainNoRegex = Regex("\\bTRAIN\\s*(?:NO\\.?|No\\.?)?\\s*:?\\s*(\\d{1,5})\\b", RegexOption.IGNORE_CASE)
        trainNoRegex.find(text)?.groupValues?.get(1)?.let { pairs["train_number"] = it }

        // Train Name (Captures text immediately after the number)
        val trainNameRegex = Regex("\\bTRAIN\\s*(?:NO\\.?|No\\.?)?\\s*:?\\s*\\d{1,5}\\s*([A-Za-z\\s]+)", RegexOption.IGNORE_CASE)
        trainNameRegex.find(text)?.groupValues?.get(1)?.trim()?.let { name ->
            if (name.length > 3) pairs["train_name"] = name
        }

        // From Station (Handles "FROM :", "FROM STATION :")
        val fromRegex = Regex("(?:FROM)\\s*(?:STATION|STN)?\\s*:?\\s*([A-Za-z\\s]+)", RegexOption.IGNORE_CASE)
        fromRegex.find(text)?.groupValues?.get(1)?.trim()?.let { pairs["from_station"] = it }

        // To Station
        val toRegex = Regex("(?:TO)\\s*(?:STATION|STN)?\\s*:?\\s*([A-Za-z\\s]+)", RegexOption.IGNORE_CASE)
        toRegex.find(text)?.groupValues?.get(1)?.trim()?.let { pairs["to_station"] = it }

        // Date (Strict word boundaries to prevent partial matches)
        val dateRegex = Regex("\\b(\\d{2}[./-]\\d{2}[./-]\\d{4})\\b")
        dateRegex.find(text)?.value?.let { pairs["journey_date"] = it }

        // Class (Handles all common IRCTC classes with word boundaries)
        val classRegex = Regex("\\b(SL|3A|2A|1A|CC|EC|3E|2S|FC)\\b", RegexOption.IGNORE_CASE)
        classRegex.find(text)?.value?.uppercase()?.let { pairs["travel_class"] = it }

        // Availability (Handles "AVAILABLE", "RAC", "WL", both with and without numbers)
        val availRegex = Regex("\\b(AVAILABLE|RAC|WL)\\b", RegexOption.IGNORE_CASE)
        val availMatch = availRegex.find(text)
        if (availMatch != null) {
            pairs["availability_type"] = availMatch.value.uppercase()
            // Try to capture the count immediately following
            val countRegex = Regex("\\b(AVAILABLE|RAC|WL)\\s*(\\d+)", RegexOption.IGNORE_CASE)
            countRegex.find(text)?.groupValues?.get(2)?.let { pairs["availability_count"] = it }
        }

        // ✅ FIX: Fare Regex (Corrected to handle "Rs.", "₹", "$", and "INR")
        val fareRegex = Regex("(?:FARE|PRICE|TOTAL)\\s*:?\\s*(?:₹|Rs\\.?|\\$|INR)?\\s*(\\d{1,5}(?:[.,]\\d*)?)", RegexOption.IGNORE_CASE)
        fareRegex.find(text)?.groupValues?.get(1)?.let { pairs["fare"] = it }

        // Additional flags for ScreenAnalyzer
        if (text.contains("PASSENGER", ignoreCase = true)) {
            pairs["has_passenger_fields"] = "true"
        }
        if (text.contains("REVIEW JOURNEY", ignoreCase = true) || text.contains("REVIEW", ignoreCase = true)) {
            pairs["screen_type"] = "review"
        }
        if (text.contains("BOOK NOW", ignoreCase = true)) {
            pairs["screen_type"] = "booking"
            pairs["action_available"] = "book_now"
        }

        return pairs
    }

    fun updateUiElements(elements: List<ScreenEvidence.UIElement>) {
        currentEvidence = currentEvidence?.copy(uiElements = elements) ?: ScreenEvidence(
            screenId = System.currentTimeMillis().toString(),
            timestamp = System.currentTimeMillis(),
            uiElements = elements
        )
    }

    fun getCurrentEvidence(): ScreenEvidence? = currentEvidence

    fun clearEvidence() {
        currentEvidence = null
        logger.debug(TAG, "Evidence cleared")
    }

    sealed class CollectionResult {
        data class Success(val evidence: ScreenEvidence, val extractedKeys: List<String>) : CollectionResult()
        data class Rejected(val reason: RejectionReason, val matchedPatterns: List<String>) : CollectionResult()
        data class RetainedAsUnknown(val screenId: String) : CollectionResult()
    }

    enum class RejectionReason { SENSITIVE_CONTENT_BLOCKED, LOW_CONFIDENCE, EMPTY_RESULT }
}
