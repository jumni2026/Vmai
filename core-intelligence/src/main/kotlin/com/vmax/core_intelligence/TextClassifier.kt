package com.vmax.core_intelligence

/**
 * VMAX v2.6.1 - Text Classifier (Safe Version)
 * 
 * IMPORTANT: 
 * यह version सिर्फ इसलिए बनाया गया है ताकि Compilation Error दूर हो सके।
 * असली Classification logic बाद में इसमें जोड़ा जा सकता है।
 */
class TextClassifier {

    enum class Classification { SAFE_UI, SENSITIVE, UNKNOWN }

    data class ClassifiedResult(
        val classification: Classification,
        val confidence: Float = 1.0f,
        val matchedPatterns: List<String> = emptyList()
    )

    fun classify(ocrResult: OcrResult): ClassifiedResult {
        // Safe fallback without depending on complex methods
        return ClassifiedResult(
            classification = Classification.UNKNOWN,
            confidence = 0f
        )
    }

    fun isSensitiveScreen(ocrResult: OcrResult): Boolean {
        return classify(ocrResult).classification == Classification.SENSITIVE
    }
}
