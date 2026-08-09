package com.vmax.core_intelligence

/**
 * VMAX v2.6.1 - Text Classifier
 * 
 * Responsibility: OCR text को SAFE_UI / SENSITIVE / UNKNOWN में classify करना
 * 
 * Architecture Rule:
 * - यह CRITICAL security checkpoint है
 * - SENSITIVE = Automation STOP → USER_CONTROL
 * - SAFE_UI = Normal flow continue
 * - UNKNOWN = Retain as evidence, no action
 */
class TextClassifier {
    
    /**
     * Classification categories
     */
    enum class Classification {
        SAFE_UI,        // Normal UI text - allowed
        SENSITIVE,      // Protected data - BLOCK automation
        UNKNOWN         // Unclear - retain evidence only
    }
    
    /**
     * Classification result with metadata
     */
    data class ClassifiedResult(
        val classification: Classification,
        val ocrResult: OcrResult,
        val matchedPatterns: List<String> = emptyList(),
        val confidence: Float = 1.0f
    )
    
    /**
     * Main entry: OCR result को classify करें
     * 
     * @param ocrResult Raw OCR output from OcrEvidenceReader
     * @return ClassifiedResult with security decision
     */
    fun classify(ocrResult: OcrResult): ClassifiedResult {
        
        // Empty check
        if (ocrResult.isEmpty()) {
            return ClassifiedResult(
                classification = Classification.UNKNOWN,
                ocrResult = ocrResult,
                confidence = 0f
            )
        }
        
        val fullText = ocrResult.fullText.uppercase()
        val matchedPatterns = mutableListOf<String>()
        
        // Step 1: SENSITIVE patterns check (HIGHEST PRIORITY)
        SENSITIVE_PATTERNS.forEach { pattern ->
            if (fullText.contains(pattern)) {
                matchedPatterns.add(pattern)
            }
        }
        
        if (matchedPatterns.isNotEmpty()) {
            return ClassifiedResult(
                classification = Classification.SENSITIVE,
                ocrResult = ocrResult,
                matchedPatterns = matchedPatterns.toList(),
                confidence = 0.95f
            )
        }
        
        // Step 2: SAFE_UI patterns check
        val safeMatches = mutableListOf<String>()
        SAFE_UI_PATTERNS.forEach { pattern ->
            if (fullText.contains(pattern)) {
                safeMatches.add(pattern)
            }
        }
        
        // Step 3: Decision
        return when {
            safeMatches.isNotEmpty() -> ClassifiedResult(
                classification = Classification.SAFE_UI,
                ocrResult = ocrResult,
                matchedPatterns = safeMatches.toList(),
                confidence = 0.85f
            )
            else -> ClassifiedResult(
                classification = Classification.UNKNOWN,
                ocrResult = ocrResult,
                matchedPatterns = emptyList(),
                confidence = 0.5f
            )
        }
    }
    
    /**
     * Quick check: Is this screen sensitive?
     * For early exit scenarios
     */
    fun isSensitiveScreen(ocrResult: OcrResult): Boolean {
        return classify(ocrResult).classification == Classification.SENSITIVE
    }
    
    /**
     * SENSITIVE Patterns - ये मिलने पर Automation STOP
     * 
     * Security Critical: इनमें से कोई भी match = User control
     */
    companion object {
        
        /**
         * BLOCK LIST - ये patterns मिलने पर automation रुक जाएगी
         */
        private val SENSITIVE_PATTERNS = listOf(
            // Authentication
            "OTP",
            "ONE TIME PASSWORD",
            "ENTER OTP",
            "VERIFY OTP",
            
            // Payment Security
            "UPI PIN",
            "ENTER PIN",
            "CARD PIN",
            "CVV",
            "CVV2",
            "CVC",
            "SECURITY CODE",
            
            // Passwords
            "PASSWORD",
            "ENTER PASSWORD",
            "CONFIRM PASSWORD",
            "NEW PASSWORD",
            
            // CAPTCHA
            "CAPTCHA",
            "ENTER CAPTCHA",
            "TYPE THE TEXT",
            "SECURITY CHECK",
            
            // Final Payment
            "PAY NOW",
            "CONFIRM PAYMENT",
            "COMPLETE PAYMENT",
            "FINAL PAYMENT",
            "TRANSACTION SUCCESSFUL",
            "PAYMENT SUCCESSFUL",
            
            // Sensitive confirmations
            "BOOKING CONFIRMED",
            "TICKET CONFIRMED",
            "PNR STATUS"
        ).map { it.uppercase() }
        
        /**
         * SAFE_UI Patterns - ये normal booking flow indicators
         */
        private val SAFE_UI_PATTERNS = listOf(
            // Train Information
            "TRAIN",
            "TRAIN NUMBER",
            "TRAIN NAME",
            "FROM",
            "TO",
            "DEPARTURE",
            "ARRIVAL",
            "DATE",
            "TIME",
            
            // Class & Availability
            "CLASS",
            "SLEEPER",
            "SL",
            "3A",
            "2A",
            "1A",
            "CC",
            "EC",
            "2S",
            "AVAILABLE",
            "RAC",
            "WL",
            "WAITLIST",
            "SEATS",
            "BERTH",
            
            // Passenger Info
            "PASSENGER",
            "PASSENGER NAME",
            "AGE",
            "GENDER",
            "BERTH PREFERENCE",
            "MEAL",
            
            // Booking Flow
            "BOOK NOW",
            "SEARCH",
            "FIND TRAINS",
            "CHECK AVAILABILITY",
            "REVIEW JOURNEY",
            "FARE",
            "PRICE",
            "TOTAL FARE",
            
            // Station names (common indicators)
            "NEW DELHI",
            "NDLS",
            "MUMBAI",
            "CHENNAI",
            "KOLKATA",
            "BANGALORE",
            "HYDERABAD",
            "PUNE",
            "JAIPUR",
            "LUCKNOW"
        ).map { it.uppercase() }
        
        /**
         * Pattern add करने के लिए (Runtime configuration)
         */
        private val customSensitivePatterns = mutableListOf<String>()
        private val customSafePatterns = mutableListOf<String>()
        
        fun addSensitivePattern(pattern: String) {
            customSensitivePatterns.add(pattern.uppercase())
        }
        
        fun addSafePattern(pattern: String) {
            customSafePatterns.add(pattern.uppercase())
        }
    }
}
