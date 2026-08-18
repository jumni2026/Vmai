package com.vmax.core_intelligence

import com.vmax.common.Logger

/**
 * VMAX v2.6.1 - UI Evidence Collector
 * Collects OCR evidence, enforces security classification, and maintains screen‑aligned evidence.
 */
class UIEvidenceCollector(private val logger: Logger) {

    companion object {
        private const val TAG = "UIEvidenceCollector"
        private const val MIN_OCR_CONFIDENCE = 0.3f
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
            val classification: TextClassifier.Classification = TextClassifier.Classification.UNKNOWN,
            val classificationConfidence: Float = 0f
        )
    }

    private var currentEvidence: ScreenEvidence? = null
    private var currentScreenId: String? = null

    @Synchronized
    fun collectOcrEvidence(classifiedResult: TextClassifier.ClassifiedResult): CollectionResult {
        val ocrResult = classifiedResult.ocrResult
        val classification = classifiedResult.classification
        val classConf = classifiedResult.confidence

        // 1. Empty check
        if (ocrResult.isEmpty()) {
            logger.warn(TAG, "Empty OCR result rejected")
            return CollectionResult.Rejected(RejectionReason.EMPTY_RESULT)
        }

        // 2. Low confidence check (unless SENSITIVE – we reject anyway)
        val avgConf = ocrResult.getAverageConfidence()
        if (avgConf < MIN_OCR_CONFIDENCE && classification != TextClassifier.Classification.SENSITIVE) {
            logger.warn(TAG, "Low OCR confidence $avgConf")
            return CollectionResult.Rejected(RejectionReason.LOW_CONFIDENCE)
        }

        // 3. SENSITIVE → hard reject (also stop automation)
        if (classification == TextClassifier.Classification.SENSITIVE) {
            logger.warn(TAG, "SENSITIVE evidence rejected")
            return CollectionResult.Rejected(
                reason = RejectionReason.SENSITIVE_CONTENT_BLOCKED,
                matchedPatterns = classifiedResult.matchedPatterns
            )
        }

        // 4. SAFE_UI or UNKNOWN → store evidence (with classification metadata)
        return processAndStore(ocrResult, classification, classConf)
    }

    private fun processAndStore(
        ocrResult: OcrResult,
        classification: TextClassifier.Classification,
        classConf: Float
    ): CollectionResult {
        val keyValues = extractKeyValuePairs(ocrResult)
        val ocrEvidence = ScreenEvidence.OcrEvidence(
            fullText = ocrResult.getCleanedText(),
            keyValuePairs = keyValues,
            rawBlocks = ocrResult.textBlocks,
            confidence = ocrResult.getAverageConfidence()
        )

        // Use existing screenId if available, otherwise from OCR
        val screenId = currentScreenId ?: ocrResult.screenId
        val existingElements = currentEvidence?.uiElements ?: emptyList()

        val evidence = ScreenEvidence(
            screenId = screenId,
            timestamp = System.currentTimeMillis(),
            uiElements = existingElements,
            ocrEvidence = ocrEvidence,
            metadata = ScreenEvidence.EvidenceMetadata(
                hasOcrData = true,
                ocrConfidence = ocrEvidence.confidence,
                elementCount = ocrResult.textBlocks.size,
                classification = classification,
                classificationConfidence = classConf
            )
        )

        currentEvidence = evidence
        currentScreenId = screenId
        logger.info(TAG, "Evidence stored: screen=$screenId, classification=$classification, keys=${keyValues.keys}")
        return CollectionResult.Success(evidence, keyValues.keys.toList())
    }

    @Synchronized
    fun updateUiElements(elements: List<ScreenEvidence.UIElement>, screenId: String? = null) {
        val newScreenId = screenId ?: currentScreenId ?: System.currentTimeMillis().toString()
        val existing = currentEvidence
        if (existing != null && existing.screenId == newScreenId) {
            currentEvidence = existing.copy(uiElements = elements, timestamp = System.currentTimeMillis())
        } else {
            currentEvidence = ScreenEvidence(
                screenId = newScreenId,
                timestamp = System.currentTimeMillis(),
                uiElements = elements
            )
        }
        currentScreenId = newScreenId
        logger.debug(TAG, "UI elements updated for screen $newScreenId")
    }

    @Synchronized
    fun getCurrentEvidence(): ScreenEvidence? = currentEvidence

    @Synchronized
    fun clearEvidence() {
        currentEvidence = null
        currentScreenId = null
        logger.debug(TAG, "Evidence cleared")
    }

    // ----- Improved key‑value extraction (non‑greedy, word boundaries) -----
    private fun extractKeyValuePairs(ocrResult: OcrResult): Map<String, String> {
        val pairs = mutableMapOf<String, String>()
        val text = ocrResult.fullText

        val trainNoRegex = Regex("\\bTRAIN\\s*(?:NO\\.?|No\\.?)?\\s*:?\\s*(\\d{1,5})\\b", RegexOption.IGNORE_CASE)
        trainNoRegex.find(text)?.groupValues?.get(1)?.let { pairs["train_number"] = it }

        val trainNameRegex = Regex("\\bTRAIN\\s*(?:NO\\.?|No\\.?)?\\s*:?\\s*\\d{1,5}\\s*([A-Za-z\\s]+?)(?=\\s*(?:FROM|TO|DATE|CLASS|\\d|$))", RegexOption.IGNORE_CASE)
        trainNameRegex.find(text)?.groupValues?.get(1)?.trim()?.let { if (it.length > 3) pairs["train_name"] = it }

        val fromRegex = Regex("\\bFROM\\s*(?:STATION|STN)?\\s*:?\\s*([A-Za-z\\s]+?)(?=\\s*(?:TO|DATE|CLASS|\\d|$))", RegexOption.IGNORE_CASE)
        fromRegex.find(text)?.groupValues?.get(1)?.trim()?.let { pairs["from_station"] = it }

        val toRegex = Regex("\\bTO\\s*(?:STATION|STN)?\\s*:?\\s*([A-Za-z\\s]+?)(?=\\s*(?:DATE|CLASS|\\d|$))", RegexOption.IGNORE_CASE)
        toRegex.find(text)?.groupValues?.get(1)?.trim()?.let { pairs["to_station"] = it }

        val dateRegex = Regex("\\b(\\d{2}[./-]\\d{2}[./-]\\d{4})\\b")
        dateRegex.find(text)?.value?.let { pairs["journey_date"] = it }

        val classRegex = Regex("\\b(SL|3A|2A|1A|CC|EC|3E|2S|FC)\\b", RegexOption.IGNORE_CASE)
        classRegex.find(text)?.value?.uppercase()?.let { pairs["travel_class"] = it }

        val availRegex = Regex("\\b(AVAILABLE|RAC|WL)\\s*(\\d+)?\\b", RegexOption.IGNORE_CASE)
        availRegex.find(text)?.let { match ->
            pairs["availability_type"] = match.groupValues[1].uppercase()
            match.groupValues[2].takeIf { it.isNotEmpty() }?.let { pairs["availability_count"] = it }
        }

        val fareRegex = Regex("(?:FARE|PRICE|TOTAL)\\s*:?\\s*(?:₹|Rs\\.?|\\$|INR)?\\s*(\\d{1,5}(?:[.,]\\d*)?)", RegexOption.IGNORE_CASE)
        fareRegex.find(text)?.groupValues?.get(1)?.let { pairs["fare"] = it }

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

    sealed class CollectionResult {
        data class Success(val evidence: ScreenEvidence, val extractedKeys: List<String>) : CollectionResult()
        data class Rejected(val reason: RejectionReason, val matchedPatterns: List<String> = emptyList()) : CollectionResult()
        // RetainedAsUnknown removed – UNKNOWN is stored as Success.
    }

    enum class RejectionReason {
        SENSITIVE_CONTENT_BLOCKED,
        LOW_CONFIDENCE,
        EMPTY_RESULT
    }
}
