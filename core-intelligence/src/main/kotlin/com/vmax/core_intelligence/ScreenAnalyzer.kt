package com.vmax.core_intelligence

import android.util.Log

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
    
    /**
     * Detected screen states
     */
    enum class ScreenState {
        UNKNOWN,
        TRAIN_LIST,           // Train search results
        AVAILABILITY,         // Seat availability page
        PASSENGER_INPUT,      // Passenger details form
        REVIEW_JOURNEY,       // Review booking
        PAYMENT,              // Payment page (SENSITIVE - should not reach here)
        CONFIRMATION,         // Booking confirmed
        ERROR                 // Error state
    }
    
    /**
     * Screen analysis result
     */
    data class AnalysisResult(
        val screenState: ScreenState,
        val confidence: Float,
        val extractedData: Map<String, String>,
        val suggestedAction: SuggestedAction,
        val evidence: UIEvidenceCollector.ScreenEvidence?
    )
    
    /**
     * Suggested actions based on analysis
     */
    enum class SuggestedAction {
        NONE,
        SELECT_TRAIN,
        CHECK_AVAILABILITY,
        FILL_PASSENGER_DETAILS,
        REVIEW_AND_PROCEED,
        STOP_AWAIT_USER,      // For sensitive screens
        ERROR_RECOVERY
    }
    
    /**
     * Main entry: Current screen analyze करें
     */
    fun analyzeCurrentScreen(): AnalysisResult {
        
        // Evidence collector से current evidence लें
        val evidence = evidenceCollector.getCurrentEvidence()
        
        if (evidence == null) {
            Log.w(TAG, "No evidence available for analysis")
            return createUnknownResult(null)
        }
        
        // OCR evidence available है?
        val ocrEvidence = evidence.ocrEvidence
        
        return if (ocrEvidence != null) {
            analyzeWithOcr(evidence, ocrEvidence)
        } else {
            analyzeWithoutOcr(evidence)
        }
    }
    
    /**
     * OCR evidence के साथ analysis
     */
    private fun analyzeWithOcr(
        evidence: UIEvidenceCollector.ScreenEvidence,
        ocrEvidence: UIEvidenceCollector.ScreenEvidence.OcrEvidence
    ): AnalysisResult {
        
        val keyValuePairs = ocrEvidence.keyValuePairs
        val fullText = ocrEvidence.fullText.uppercase()
        
        Log.d(TAG, "Analyzing with OCR - keys: ${keyValuePairs.keys}")
        
        // Screen state detection based on OCR patterns
        
        return when {
            // Review Journey Page
            isReviewJourneyScreen(keyValuePairs, fullText) -> {
                AnalysisResult(
                    screenState = ScreenState.REVIEW_JOURNEY,
                    confidence = 0.9f,
                    extractedData = keyValuePairs,
                    suggestedAction = SuggestedAction.REVIEW_AND_PROCEED,
                    evidence = evidence
                )
            }
            
            // Passenger Input Page
            isPassengerInputScreen(keyValuePairs, fullText, evidence) -> {
                AnalysisResult(
                    screenState = ScreenState.PASSENGER_INPUT,
                    confidence = 0.85f,
                    extractedData = keyValuePairs,
                    suggestedAction = SuggestedAction.FILL_PASSENGER_DETAILS,
                    evidence = evidence
                )
            }
            
            // Availability Page
            isAvailabilityScreen(keyValuePairs, fullText) -> {
                AnalysisResult(
                    screenState = ScreenState.AVAILABILITY,
                    confidence = 0.88f,
                    extractedData = keyValuePairs,
                    suggestedAction = SuggestedAction.CHECK_AVAILABILITY,
                    evidence = evidence
                )
            }
            
            // Train List Page
            isTrainListScreen(keyValuePairs, fullText) -> {
                AnalysisResult(
                    screenState = ScreenState.TRAIN_LIST,
                    confidence = 0.82f,
                    extractedData = keyValuePairs,
                    suggestedAction = SuggestedAction.SELECT_TRAIN,
                    evidence = evidence
                )
            }
            
            // Unknown but with OCR data
            else -> {
                AnalysisResult(
                    screenState = ScreenState.UNKNOWN,
                    confidence = 0.5f,
                    extractedData = keyValuePairs,
                    suggestedAction = SuggestedAction.NONE,
                    evidence = evidence
                )
            }
        }
    }
    
    /**
     * Review Journey screen detect करें
     */
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
    
    /**
     * Passenger Input screen detect करें
     */
    private fun isPassengerInputScreen(
        keyValuePairs: Map<String, String>,
        fullText: String,
        evidence: UIEvidenceCollector.ScreenEvidence
    ): Boolean {
        // OCR indicators
        val ocrIndicator = keyValuePairs["has_passenger_fields"] == "true" ||
                fullText.contains("PASSENGER NAME") ||
                fullText.contains("AGE") ||
                fullText.contains("GENDER")
        
        // UI elements indicators
        val uiIndicator = evidence.uiElements.any { element ->
            element.isEditable && (
                    element.text.contains("Name", ignoreCase = true) ||
                            element.text.contains("Age", ignoreCase = true) ||
                            element.hint?.contains("Passenger", ignoreCase = true) == true
                    )
        }
        
        return ocrIndicator || uiIndicator
    }
    
    /**
     * Availability screen detect करें
     */
    private fun isAvailabilityScreen(
        keyValuePairs: Map<String, String>,
        fullText: String
    ): Boolean {
        return keyValuePairs.containsKey("availability_type") ||
                keyValuePairs.containsKey("travel_class") &&
                (fullText.contains("AVAILABLE") || fullText.contains("RAC") || fullText.contains("WL"))
    }
    
    /**
     * Train List screen detect करें
     */
    private fun isTrainListScreen(
        keyValuePairs: Map<String, String>,
        fullText: String
    ): Boolean {
        return keyValuePairs.containsKey("train_number") &&
                keyValuePairs.containsKey("train_name") &&
                !keyValuePairs.containsKey("availability_type")
    }
    
    /**
     * OCR evidence के बिना analysis (fallback)
     */
    private fun analyzeWithoutOcr(
        evidence: UIEvidenceCollector.ScreenEvidence
    ): AnalysisResult {
        
        Log.d(TAG, "Analyzing without OCR - UI elements only")
        
        // UI elements के आधार पर basic analysis
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
    
    /**
     * Unknown result create करें
     */
    private fun createUnknownResult(evidence: UIEvidenceCollector.ScreenEvidence?): AnalysisResult {
        return AnalysisResult(
            screenState = ScreenState.UNKNOWN,
            confidence = 0f,
            extractedData = emptyMap(),
            suggestedAction = SuggestedAction.NONE,
            evidence = evidence
        )
    }
    
    /**
     * PrecisionMatchEngine के लिए enriched evidence prepare करें
     */
    fun prepareEvidenceForMatching(
        analysisResult: AnalysisResult
    ): PrecisionEvidence {
        return PrecisionEvidence(
            screenState = analysisResult.screenState,
            confidence = analysisResult.confidence,
            ocrData = analysisResult.evidence?.ocrEvidence?.keyValuePairs ?: emptyMap(),
            uiElements = analysisResult.evidence?.uiElements ?: emptyList(),
            timestamp = analysisResult.evidence?.timestamp ?: System.currentTimeMillis()
        )
    }
    
    /**
     * PrecisionMatchEngine को भेजने वाला evidence format
     */
    data class PrecisionEvidence(
        val screenState: ScreenState,
        val confidence: Float,
        val ocrData: Map<String, String>,
        val uiElements: List<UIEvidenceCollector.ScreenEvidence.UIElement>,
        val timestamp: Long
    )
}
