package com.vmax.core_intelligence

import com.vmax.common.Logger

/**
 * VMAX v2.6.1 - Screen Analyzer
 * 
 * Responsibility: Screen state को समझना और Workflow decision लेना
 * 
 * OCR Integration:
 * - UIEvidenceCollector से OCR evidence लेगा
 * - UI elements + OCR text को combine करके analysis करेगा
 * - PrecisionMatchEngine को enriched evidence भेजेगा
 */
class ScreenAnalyzer(
    private val evidenceCollector: UIEvidenceCollector
) {
    companion object {
        private const val TAG = "ScreenAnalyzer"
    }
    
    enum class ScreenState {
        UNKNOWN, TRAIN_LIST, AVAILABILITY, PASSENGER_INPUT,
        REVIEW_JOURNEY, PAYMENT, CONFIRMATION, ERROR
    }
    
    data class AnalysisResult(
        val screenState: ScreenState,
        val confidence: Float,
        val extractedData: Map<String, String>,
        val suggestedAction: SuggestedAction,
        val evidence: UIEvidenceCollector.ScreenEvidence?
    )
    
    enum class SuggestedAction {
        NONE, SELECT_TRAIN, CHECK_AVAILABILITY,
        FILL_PASSENGER_DETAILS, REVIEW_AND_PROCEED,
        STOP_AWAIT_USER, ERROR_RECOVERY
    }
    
    fun analyzeCurrentScreen(): AnalysisResult {
        val evidence = evidenceCollector.getCurrentEvidence()
        if (evidence == null) {
            Logger.warn(TAG, "No evidence available for analysis")
            return createUnknownResult(null)
        }
        val ocrEvidence = evidence.ocrEvidence
        return if (ocrEvidence != null) {
            analyzeWithOcr(evidence, ocrEvidence)
        } else {
            analyzeWithoutOcr(evidence)
        }
    }
    
    private fun analyzeWithOcr(
        evidence: UIEvidenceCollector.ScreenEvidence,
        ocrEvidence: UIEvidenceCollector.ScreenEvidence.OcrEvidence
    ): AnalysisResult {
        val keyValuePairs = ocrEvidence.keyValuePairs
        val fullText = ocrEvidence.fullText.uppercase()
        Logger.debug(TAG, "Analyzing with OCR - keys: ${keyValuePairs.keys}")
        return when {
            isReviewJourneyScreen(keyValuePairs, fullText) -> AnalysisResult(
                screenState = ScreenState.REVIEW_JOURNEY,
                confidence = 0.9f,
                extractedData = keyValuePairs,
                suggestedAction = SuggestedAction.REVIEW_AND_PROCEED,
                evidence = evidence
            )
            isPassengerInputScreen(keyValuePairs, fullText, evidence) -> AnalysisResult(
                screenState = ScreenState.PASSENGER_INPUT,
                confidence = 0.85f,
                extractedData = keyValuePairs,
                suggestedAction = SuggestedAction.FILL_PASSENGER_DETAILS,
                evidence = evidence
            )
            isAvailabilityScreen(keyValuePairs, fullText) -> AnalysisResult(
                screenState = ScreenState.AVAILABILITY,
                confidence = 0.88f,
                extractedData = keyValuePairs,
                suggestedAction = SuggestedAction.CHECK_AVAILABILITY,
                evidence = evidence
            )
            isTrainListScreen(keyValuePairs, fullText) -> AnalysisResult(
                screenState = ScreenState.TRAIN_LIST,
                confidence = 0.82f,
                extractedData = keyValuePairs,
                suggestedAction = SuggestedAction.SELECT_TRAIN,
                evidence = evidence
            )
            else -> AnalysisResult(
                screenState = ScreenState.UNKNOWN,
                confidence = 0.5f,
                extractedData = keyValuePairs,
                suggestedAction = SuggestedAction.NONE,
                evidence = evidence
            )
        }
    }
    
    private fun isReviewJourneyScreen(
        keyValuePairs: Map<String, String>,
        fullText: String
    ): Boolean {
        return keyValuePairs["screen_type"] == "review" ||
                (keyValuePairs.containsKey("train_number") &&
                 keyValuePairs.containsKey("from_station") &&
                 keyValuePairs.containsKey("to_station") &&
                 fullText.contains("REVIEW"))
    }
    
    private fun isPassengerInputScreen(
        keyValuePairs: Map<String, String>,
        fullText: String,
        evidence: UIEvidenceCollector.ScreenEvidence
    ): Boolean {
        val ocrIndicator = keyValuePairs["has_passenger_fields"] == "true" ||
                fullText.contains("PASSENGER NAME") ||
                fullText.contains("AGE") ||
                fullText.contains("GENDER")
        val uiIndicator = evidence.uiElements.any { element ->
            element.isEditable && (
                    element.text.contains("Name", ignoreCase = true) ||
                    element.text.contains("Age", ignoreCase = true) ||
                    element.hint?.contains("Passenger", ignoreCase = true) == true
            )
        }
        return ocrIndicator || uiIndicator
    }
    
    private fun isAvailabilityScreen(
        keyValuePairs: Map<String, String>,
        fullText: String
    ): Boolean {
        return keyValuePairs.containsKey("availability_type") ||
                (keyValuePairs.containsKey("travel_class") &&
                 (fullText.contains("AVAILABLE") || fullText.contains("RAC") || fullText.contains("WL")))
    }
    
    private fun isTrainListScreen(
        keyValuePairs: Map<String, String>,
        fullText: String
    ): Boolean {
        return keyValuePairs.containsKey("train_number") &&
                keyValuePairs.containsKey("train_name") &&
                !keyValuePairs.containsKey("availability_type")
    }
    
    private fun analyzeWithoutOcr(evidence: UIEvidenceCollector.ScreenEvidence): AnalysisResult {
        Logger.debug(TAG, "Analyzing without OCR - UI elements only")
        val uiElements = evidence.uiElements
        val hasInputFields = uiElements.any { it.isEditable }
        val hasButtons = uiElements.any { it.isClickable && it.text.contains("Book", ignoreCase = true) }
        return when {
            hasInputFields && hasButtons -> AnalysisResult(
                screenState = ScreenState.PASSENGER_INPUT,
                confidence = 0.6f,
                extractedData = emptyMap(),
                suggestedAction = SuggestedAction.FILL_PASSENGER_DETAILS,
                evidence = evidence
            )
            hasButtons -> AnalysisResult(
                screenState = ScreenState.UNKNOWN,
                confidence = 0.4f,
                extractedData = emptyMap(),
                suggestedAction = SuggestedAction.NONE,
                evidence = evidence
            )
            else -> createUnknownResult(evidence)
        }
    }
    
    private fun createUnknownResult(evidence: UIEvidenceCollector.ScreenEvidence?): AnalysisResult {
        return AnalysisResult(
            screenState = ScreenState.UNKNOWN,
            confidence = 0f,
            extractedData = emptyMap(),
            suggestedAction = SuggestedAction.NONE,
            evidence = evidence
        )
    }
    
    fun prepareEvidenceForMatching(analysisResult: AnalysisResult): PrecisionEvidence {
        return PrecisionEvidence(
            screenState = analysisResult.screenState,
            confidence = analysisResult.confidence,
            ocrData = analysisResult.evidence?.ocrEvidence?.keyValuePairs ?: emptyMap(),
            uiElements = analysisResult.evidence?.uiElements ?: emptyList(),
            timestamp = analysisResult.evidence?.timestamp ?: System.currentTimeMillis()
        )
    }
    
    data class PrecisionEvidence(
        val screenState: ScreenState,
        val confidence: Float,
        val ocrData: Map<String, String>,
        val uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>,
        val timestamp: Long
    )
}
