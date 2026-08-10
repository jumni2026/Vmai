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
class UIEvidenceCollector {
    
    companion object {
        private const val TAG = "UIEvidenceCollector"
    }
    
    /**
     * Current screen evidence holder
     */
    data class ScreenEvidence(
        val screenId: String,
        val timestamp: Long,
        val uiElements: List<UIElement> = emptyList(),
        val ocrEvidence: OcrEvidence? = null,
        val metadata: EvidenceMetadata = EvidenceMetadata()
    ) {
        /**
         * OCR-specific evidence container
         */
        data class OcrEvidence(
            val fullText: String,
            val keyValuePairs: Map<String, String>,
            val rawBlocks: List<OcrResult.TextBlock>,
            val confidence: Float
        )
        
        /**
         * Standard UI element from accessibility service
         * ✅ FIX: Added 'hint' field and replaced android Rect with BoundingBox
         */
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
        
        /**
         * Evidence quality metadata
         */
        data class EvidenceMetadata(
            val hasOcrData: Boolean = false,
            val ocrConfidence: Float = 0f,
            val elementCount: Int = 0,
            val classification: TextClassifier.Classification = TextClassifier.Classification.UNKNOWN
        )
    }
    
    /**
     * Active screen evidence
     */
    private var currentEvidence: ScreenEvidence? = null
    
    /**
     * Main entry: Classified OCR result को evidence system में add करें
     */
    fun collectOcrEvidence(classifiedResult: TextClassifier.ClassifiedResult): CollectionResult {
        
        // Security check: SENSITIVE evidence को बिल्कुल reject करें
        if (classifiedResult.classification == TextClassifier.Classification.SENSITIVE) {
            Logger.log(TAG, "SENSITIVE evidence rejected - screenId: ${classifiedResult.ocrResult.screenId}", LogLevel.WARN)
            return CollectionResult.Rejected(
                reason = RejectionReason.SENSITIVE_CONTENT_BLOCKED,
                matchedPatterns = classifiedResult.matchedPatterns
            )
        }
        
        // UNKNOWN evidence को retain करें लेकिन limited capacity में
        if (classifiedResult.classification == TextClassifier.Classification.UNKNOWN) {
            Logger.log(TAG, "UNKNOWN evidence retained for analysis - screenId: ${classifiedResult.ocrResult.screenId}", LogLevel.DEBUG)
            return CollectionResult.RetainedAsUnknown(
                screenId = classifiedResult.ocrResult.screenId
            )
        }
        
        // SAFE_UI evidence को process करें
        return processSafeEvidence(classifiedResult)
    }
    
    /**
     * SAFE_UI evidence को structured format में convert करें
     */
    private fun processSafeEvidence(
        classifiedResult: TextClassifier.ClassifiedResult
    ): CollectionResult {
        
        val ocrResult = classifiedResult.ocrResult
        
        // Key-value extraction from OCR text
        val keyValuePairs = extractKeyValuePairs(ocrResult)
        
        // Create OCR evidence container
        val ocrEvidence = ScreenEvidence.OcrEvidence(
            fullText = ocrResult.getCleanedText(),
            keyValuePairs = keyValuePairs,
            rawBlocks = ocrResult.textBlocks,
            confidence = ocrResult.getAverageConfidence()
        )
        
        // Build complete screen evidence
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
        
        // Store current evidence
        currentEvidence = evidence
        
        Logger.log(TAG, "SAFE_UI evidence collected - screenId: ${evidence.screenId}, keys: ${keyValuePairs.keys}, confidence: ${ocrEvidence.confidence}", LogLevel.INFO)
        
        return CollectionResult.Success(
            evidence = evidence,
            extractedKeys = keyValuePairs.keys.toList()
        )
    }
    
    /**
     * OCR text से key-value pairs extract करें
     */
    private fun extractKeyValuePairs(ocrResult: OcrResult): Map<String, String> {
        val pairs = mutableMapOf<String, String>()
        val text = ocrResult.fullText
        
        // Pattern-based extraction
        Regex("TRAIN\\s*:?\\s*(\\d{1,5})", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.get(1)?.let {
                pairs["train_number"] = it
            }
        
        Regex("TRAIN\\s*:?\\s*\\d*\\s*([A-Za-z\\s]+)", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.get(1)?.trim()?.let {
                if (it.length > 3) pairs["train_name"] = it
            }
        
        Regex("FROM\\s*:?\\s*([A-Za-z\\s]+)", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.get(1)?.trim()?.let {
                pairs["from_station"] = it
            }
        
        Regex("TO\\s*:?\\s*([A-Za-z\\s]+)", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.get(1)?.trim()?.let {
                pairs["to_station"] = it
            }
        
        Regex("(\\d{2}[-/]\\d{2}[-/]\\d{4})")
            .find(text)?.value?.let {
                pairs["journey_date"] = it
            }
        
        Regex("\\b(SL|3A|2A|1A|CC|EC|2S)\\b", RegexOption.IGNORE_CASE)
            .find(text)?.value?.uppercase()?.let {
                pairs["travel_class"] = it
            }
        
        Regex("(AVAILABLE|RAC|WL)\\s*(\\d+)", RegexOption.IGNORE_CASE)
            .find(text)?.let { match ->
                val type = match.groupValues[1].uppercase()
                val count = match.groupValues[2]
                pairs["availability_type"] = type
                pairs["availability_count"] = count
            }
        
        Regex("(?:FARE|PRICE|TOTAL)[:\\s]*[₹$]?\\s*(\\d+[.,]?\\d*)", RegexOption.IGNORE_CASE)
            .find(text)?.groupValues?.get(1)?.let {
                pairs["fare"] = it
            }
        
        if (text.contains("PASSENGER", ignoreCase = true)) {
            pairs["has_passenger_fields"] = "true"
        }
        
        if (text.contains("REVIEW JOURNEY", ignoreCase = true) ||
            text.contains("REVIEW", ignoreCase = true)) {
            pairs["screen_type"] = "review"
        }
        
        if (text.contains("BOOK NOW", ignoreCase = true)) {
            pairs["screen_type"] = "booking"
            pairs["action_available"] = "book_now"
        }
        
        return pairs
    }
    
    fun updateUiElements(elements: List<ScreenEvidence.UIElement>) {
        currentEvidence = currentEvidence?.copy(
            uiElements = elements
        ) ?: ScreenEvidence(
            screenId = System.currentTimeMillis().toString(),
            timestamp = System.currentTimeMillis(),
            uiElements = elements
        )
    }
    
    fun getCurrentEvidence(): ScreenEvidence? = currentEvidence
    
    fun clearEvidence() {
        currentEvidence = null
        Logger.log(TAG, "Evidence cleared", LogLevel.DEBUG)
    }
    
    sealed class CollectionResult {
        data class Success(
            val evidence: ScreenEvidence,
            val extractedKeys: List<String>
        ) : CollectionResult()
        
        data class Rejected(
            val reason: RejectionReason,
            val matchedPatterns: List<String>
        ) : CollectionResult()
        
        data class RetainedAsUnknown(
            val screenId: String
        ) : CollectionResult()
    }
    
    enum class RejectionReason {
        SENSITIVE_CONTENT_BLOCKED,
        LOW_CONFIDENCE,
        EMPTY_RESULT
    }
}
