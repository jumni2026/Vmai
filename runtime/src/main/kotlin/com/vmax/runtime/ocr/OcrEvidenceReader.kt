package com.vmax.runtime.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.vmax.core_intelligence.OcrResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * VMAX v2.6.1
 *
 * OCR Evidence Reader
 *
 * Responsibility:
 * - ML Kit से screenshot का raw text पढ़ना
 * - OCR result को platform-independent OcrResult में बदलना
 *
 * यह class कोई decision नहीं लेती।
 * Sensitive/safe का निर्णय TextClassifier करेगा।
 */
class OcrEvidenceReader {

    private val textRecognizer by lazy {
        TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )
    }

    private var isClosed = false

    /**
     * Screenshot से text extract करता है।
     */
    suspend fun readFromScreenshot(
        screenshot: Bitmap,
        screenId: String = System.currentTimeMillis().toString()
    ): OcrResult = suspendCancellableCoroutine { continuation ->

        if (isClosed) {
            continuation.resumeWithException(
                IllegalStateException("OcrEvidenceReader is closed")
            )
            return@suspendCancellableCoroutine
        }

        if (screenshot.isRecycled) {
            continuation.resumeWithException(
                IllegalArgumentException("Bitmap is recycled")
            )
            return@suspendCancellableCoroutine
        }

        val inputImage = InputImage.fromBitmap(
            screenshot,
            0
        )

        var cancelled = false

        continuation.invokeOnCancellation {
            cancelled = true
        }

        textRecognizer
            .process(inputImage)
            .addOnSuccessListener { visionText ->

                if (cancelled || !continuation.isActive) {
                    return@addOnSuccessListener
                }

                try {
                    val result = convertToOcrResult(
                        visionText = visionText,
                        screenId = screenId
                    )

                    if (continuation.isActive) {
                        continuation.resume(result)
                    }
                } catch (exception: Exception) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(exception)
                    }
                }
            }
            .addOnFailureListener { exception ->

                if (cancelled || !continuation.isActive) {
                    return@addOnFailureListener
                }

                continuation.resumeWithException(
                    OcrReadException(
                        message = "ML Kit text recognition failed: ${exception.message}",
                        cause = exception
                    )
                )
            }
    }

    /**
     * ML Kit Text को platform-independent OcrResult में convert करता है।
     */
    private fun convertToOcrResult(
        visionText: Text,
        screenId: String
    ): OcrResult {

        val textBlocks = mutableListOf<OcrResult.TextBlock>()

        visionText.textBlocks.forEach { block ->

            val boundingBox = block.boundingBox?.let { rect ->
                OcrResult.BoundingBox(
                    left = rect.left,
                    top = rect.top,
                    right = rect.right,
                    bottom = rect.bottom
                )
            }

            val lines = block.lines.map { line ->
                line.text
            }

            textBlocks.add(
                OcrResult.TextBlock(
                    text = block.text,
                    confidence = 1.0f,
                    boundingBox = boundingBox,
                    lines = lines
                )
            )
        }

        return OcrResult(
            screenId = screenId,
            timestamp = System.currentTimeMillis(),
            fullText = visionText.text,
            textBlocks = textBlocks,
            language = "en"
        )
    }

    /**
     * OCR recognizer resources release करता है।
     */
    fun close() {
        if (isClosed) {
            return
        }

        isClosed = true
        textRecognizer.close()
    }

    class OcrReadException(
        message: String,
        cause: Throwable? = null
    ) : Exception(message, cause)
}
