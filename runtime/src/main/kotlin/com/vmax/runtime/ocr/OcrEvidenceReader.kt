package com.vmax.runtime.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.vmax.core_intelligence.OcrResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * VMAX v2.6.1 - OCR Evidence Reader
 * 
 * Responsibility: ML Kit को call करके screenshot से text पढ़ना
 * 
 * Architecture Rule:
 * - सिर्फ text recognition करेगा
 * - कोई decision नहीं लेगा (sensitive या safe का फैसला TextClassifier करेगा)
 * - Result raw form में return करेगा
 */
class OcrEvidenceReader {
    
    /**
     * ML Kit Latin Text Recognizer
     * on-device model - no network required, fast processing
     */
    private val textRecognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }
    
    /**
     * Screenshot से text extract करें
     * 
     * @param screenshot Bitmap from AccessibilityService
     * @param screenId Unique identifier for this screen capture
     * @return OcrResult containing all recognized text blocks
     */
    suspend fun readFromScreenshot(
        screenshot: Bitmap,
        screenId: String = System.currentTimeMillis().toString()
    ): OcrResult = suspendCancellableCoroutine { continuation ->
        
        val inputImage = InputImage.fromBitmap(screenshot, 0)
        
        textRecognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                // VisionText को OcrResult में convert करें
                val result = convertToOcrResult(visionText, screenId)
                continuation.resume(result)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(
                    OcrReadException("ML Kit text recognition failed: ${exception.message}", exception)
                )
            }
        
        // Cancellation handling
        continuation.invokeOnCancellation {
            // ML Kit task को cancel करने का कोई direct way नहीं
            // लेकिन result ignore हो जाएगा
        }
    }
    
    /**
     * VisionText को structured OcrResult में convert करें
     */
    private fun convertToOcrResult(
        visionText: com.google.mlkit.vision.text.Text,
        screenId: String
    ): OcrResult {
        
        val textBlocks = mutableListOf<OcrResult.TextBlock>()
        
        // Har text block ko process karein
        visionText.textBlocks.forEach { block ->
            val blockText = block.text ?: return@forEach
            
            // Block ki position/rect
            val boundingBox = block.boundingBox
            
            val textBlock = OcrResult.TextBlock(
                text = blockText,
                confidence = estimateConfidence(blockText),
                boundingBox = boundingBox,
                lines = block.lines.map { it.text ?: "" }
            )
            
            textBlocks.add(textBlock)
        }
        
        // Complete raw text
        val fullText = visionText.text ?: ""
        
        return OcrResult(
            screenId = screenId,
            timestamp = System.currentTimeMillis(),
            fullText = fullText,
            textBlocks = textBlocks,
            language = detectLanguage(fullText)
        )
    }
    
    /**
     * Simple confidence estimation based on text characteristics
     * ML Kit confidence scores directly provide नहीं करता
     */
    private fun estimateConfidence(text: String): Float {
        if (text.isBlank()) return 0f
        
        // Heuristic: Special characters ratio, length, etc.
        val alphanumericCount = text.count { it.isLetterOrDigit() || it.isWhitespace() }
        val totalLength = text.length
        
        return if (totalLength > 0) {
            (alphanumericCount.toFloat() / totalLength) * 0.9f + 0.1f
        } else {
            0.5f
        }
    }
    
    /**
     * Basic language detection
     * ML Kit Latin recognizer = English + European languages
     */
    private fun detectLanguage(text: String): String {
        // Simple check - Latin script detector
        return when {
            text.isBlank() -> "unknown"
            text.any { it in '\u0900'..'\u097F' } -> "hi" // Hindi
            text.any { it in '\u0600'..'\u06FF' } -> "ar" // Arabic
            else -> "en" // Default English/Latin
        }
    }
    
    /**
     * Resource cleanup when service destroyed
     */
    fun close() {
        textRecognizer.close()
    }
    
    /**
     * Custom exception for OCR failures
     */
    class OcrReadException(message: String, cause: Throwable? = null) : Exception(message, cause)
}
