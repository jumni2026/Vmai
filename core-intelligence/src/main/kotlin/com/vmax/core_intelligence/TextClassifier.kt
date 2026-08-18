package com.vmax.core_intelligence

/**
 * VMAX v2.6.1 - Text Classifier
 * Security checkpoint: classifies OCR text as SAFE_UI / SENSITIVE / UNKNOWN.
 */
class TextClassifier {

    enum class Classification { SAFE_UI, SENSITIVE, UNKNOWN }

    data class ClassifiedResult(
        val classification: Classification,
        val ocrResult: OcrResult,
        val matchedPatterns: List<String> = emptyList(),
        val confidence: Float = 1.0f
    )

    fun classify(ocrResult: OcrResult): ClassifiedResult {
        if (ocrResult.isEmpty()) {
            return ClassifiedResult(Classification.UNKNOWN, ocrResult, confidence = 0f)
        }

        val fullText = ocrResult.fullText.uppercase()
        val ocrConf = ocrResult.getAverageConfidence()
        val matched = mutableListOf<String>()

        // Merge built‑in and custom patterns
        val allSensitive = SENSITIVE_PATTERNS + customSensitivePatterns
        val allSafe = SAFE_UI_PATTERNS + customSafePatterns

        // 1. Check SENSITIVE (using word boundary for single‑word patterns)
        for (pattern in allSensitive) {
            if (pattern.isBlank()) continue
            val regex = if (pattern.matches(Regex("^[A-Z0-9]+$"))) {
                Regex("\\b${Regex.escape(pattern)}\\b")
            } else {
                Regex(Regex.escape(pattern), RegexOption.IGNORE_CASE)
            }
            if (regex.containsMatchIn(fullText)) {
                matched.add(pattern)
            }
        }
        if (matched.isNotEmpty()) {
            // Confidence = (0.95 * OCR confidence) capped at 1.0
            val conf = (0.95f * ocrConf).coerceAtMost(1f)
            return ClassifiedResult(Classification.SENSITIVE, ocrResult, matched, conf)
        }

        // 2. Check SAFE_UI (also with word boundaries for short patterns)
        val safeMatches = mutableListOf<String>()
        for (pattern in allSafe) {
            if (pattern.isBlank()) continue
            val regex = if (pattern.matches(Regex("^[A-Z0-9]+$"))) {
                Regex("\\b${Regex.escape(pattern)}\\b")
            } else {
                Regex(Regex.escape(pattern), RegexOption.IGNORE_CASE)
            }
            if (regex.containsMatchIn(fullText)) {
                safeMatches.add(pattern)
            }
        }
        if (safeMatches.isNotEmpty()) {
            val conf = (0.85f * ocrConf).coerceAtMost(1f)
            return ClassifiedResult(Classification.SAFE_UI, ocrResult, safeMatches, conf)
        }

        // 3. UNKNOWN
        val conf = (0.5f * ocrConf).coerceAtMost(1f)
        return ClassifiedResult(Classification.UNKNOWN, ocrResult, emptyList(), conf)
    }

    fun isSensitiveScreen(ocrResult: OcrResult): Boolean =
        classify(ocrResult).classification == Classification.SENSITIVE

    companion object {
        // Built‑in sensitive patterns
        private val SENSITIVE_PATTERNS = listOf(
            "OTP", "ONE TIME PASSWORD", "ENTER OTP", "VERIFY OTP",
            "UPI PIN", "ENTER PIN", "CARD PIN", "CVV", "CVV2", "CVC", "SECURITY CODE",
            "PASSWORD", "ENTER PASSWORD", "CONFIRM PASSWORD", "NEW PASSWORD",
            "CAPTCHA", "ENTER CAPTCHA", "TYPE THE TEXT", "SECURITY CHECK",
            "PAY NOW", "CONFIRM PAYMENT", "COMPLETE PAYMENT", "FINAL PAYMENT",
            "TRANSACTION SUCCESSFUL", "PAYMENT SUCCESSFUL",
            "BOOKING CONFIRMED", "TICKET CONFIRMED", "PNR STATUS"
        ).map { it.uppercase() }

        // Built‑in safe patterns (tightened – removed overly broad single words)
        private val SAFE_UI_PATTERNS = listOf(
            "TRAIN NUMBER", "TRAIN NAME", "DEPARTURE", "ARRIVAL",
            "SLEEPER", "SL", "3A", "2A", "1A", "CC", "EC", "2S",
            "AVAILABLE", "RAC", "WAITLIST",
            "PASSENGER NAME", "BERTH PREFERENCE", "MEAL",
            "BOOK NOW", "FIND TRAINS", "CHECK AVAILABILITY",
            "REVIEW JOURNEY", "TOTAL FARE",
            "NEW DELHI", "NDLS", "MUMBAI", "CHENNAI", "KOLKATA",
            "BANGALORE", "HYDERABAD", "PUNE", "JAIPUR", "LUCKNOW"
        ).map { it.uppercase() }

        // Custom patterns (mutable, thread‑safe)
        private val customSensitivePatterns = mutableListOf<String>()
        private val customSafePatterns = mutableListOf<String>()

        @Synchronized
        fun addSensitivePattern(pattern: String) {
            val trimmed = pattern.trim().uppercase()
            if (trimmed.isNotEmpty() && !customSensitivePatterns.contains(trimmed)) {
                customSensitivePatterns.add(trimmed)
            }
        }

        @Synchronized
        fun addSafePattern(pattern: String) {
            val trimmed = pattern.trim().uppercase()
            if (trimmed.isNotEmpty() && !customSafePatterns.contains(trimmed)) {
                customSafePatterns.add(trimmed)
            }
        }
    }
}
