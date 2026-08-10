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
    
    private val textRecognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }
    
    private var isClosed = false
    
    /**
     * Screenshot से text extract करें
     */
    suspend fun readFromScreenshot(
        screenshot: Bitmap,
        screenId: String = System.currentTimeMillis().toString()
    ): OcrResult = suspendCancellableCoroutine { continuation ->
        
        if (isClosed) {
            continuation.resumeWithException(IllegalStateException("OcrEvidenceReader is closed"))
            return@suspendCancellableCoroutine
        }
        
        if (screenshot.isRecycled) {
            continuation.resumeWithException(IllegalArgumentException("Bitmap is recycled"))
            return@suspendCancellableCoroutine
        }
        
        val inputImage = InputImage.fromBitmap(screenshot, 0)
        
        var cancelled = false
        continuation.invokeOnCancellation {
            cancelled = true
            // ML Kit tasks cannot be cancelled directly, but we ignore the result.
        }
        
        textRecognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                if (cancelled) {
                    // If cancelled, do not resume
                    return@addOnSuccessListener
                }
                val result = convertToOcrResult(visionText, screenId)
                if (continuation.isActive) {
                    continuation.resume(result)
                }
            }
            .addOnFailureListener { exception ->
                if (cancelled) {
                    return@addOnFailureListener
                }
                if (continuation.isActive) {
                    continuation.resumeWithException(
                        OcrReadException("ML Kit text recognition failed: ${exception.message}", exception)
                    )
                }
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
        
        visionText.textBlocks.forEach { block ->
            val blockText = block.text ?: return@forEach
            
            // Convert android.graphics.Rect? to OcrResult.BoundingBox?
            val boundingBox = block.boundingBox?.let { rect ->
                OcrResult.BoundingBox(
                    left = rect.left,
                    top = rect.top,
                    right = rect.right,
                    bottom = rect.bottom
                )
            }
            
            val textBlock = OcrResult.TextBlock(
                text = blockText,
                // ML Kit does not provide confidence scores, so we set a neutral value.
                // This is a raw evidence layer, not a confidence analysis layer.
                confidence = 1.0f,
                boundingBox = boundingBox,
                lines = block.lines.map { it.text ?: "" }
            )
            
            textBlocks.add(textBlock)
        }
        
        val fullText = visionText.text ?: ""
        
        return OcrResult(
            screenId = screenId,
            timestamp = System.currentTimeMillis(),
            fullText = fullText,
            textBlocks = textBlocks,
            language = "en" // Latin recognizer always returns English/Latin script
        )
    }
    
    /**
     * Resource cleanup when service destroyed
     */
    fun close() {
        isClosed = true
        textRecognizer.close()
    }
    
    class OcrReadException(message: String, cause: Throwable? = null) : Exception(message, cause)
}
