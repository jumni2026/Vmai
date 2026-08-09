package com.vmax.core_intelligence

import android.graphics.Rect
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * VMAX v2.6.1 - OCR Result
 * 
 * Responsibility: OCR का structured result रखना
 * 
 * Architecture Rule:
 * - Immutable data class
 * - Parcelable for inter-component communication
 * - Raw evidence - no classification here (TextClassifier करेगा)
 */
@Parcelize
data class OcrResult(
    /**
     * Unique identifier for this screen capture session
     */
    val screenId: String,
    
    /**
     * Timestamp when OCR was performed
     */
    val timestamp: Long,
    
    /**
     * Complete raw text from entire screen
     * (All text blocks concatenated)
     */
    val fullText: String,
    
    /**
     * Individual text blocks with metadata
     * (Position, confidence, lines)
     */
    val textBlocks: List<TextBlock>,
    
    /**
     * Detected language code
     * en = English, hi = Hindi, etc.
     */
    val language: String = "unknown"
    
) : Parcelable {
    
    /**
     * Individual text block from screen
     */
    @Parcelize
    data class TextBlock(
        /**
         * Recognized text content
         */
        val text: String,
        
        /**
         * Confidence score (0.0 to 1.0)
         * ML Kit direct confidence नहीं देता, heuristic estimate
         */
        val confidence: Float,
        
        /**
         * Bounding box position on screen
         * Null if not available
         */
        val boundingBox: Rect?,
        
        /**
         * Individual lines within this block
         */
        val lines: List<String>
        
    ) : Parcelable
    
    /**
     * Helper: Get all text as single cleaned string
     * (Newlines preserved for structure)
     */
    fun getCleanedText(): String {
        return textBlocks
            .map { it.text.trim() }
            .filter { it.isNotBlank() }
            .joinToString("\n")
    }
    
    /**
     * Helper: Get text blocks sorted by vertical position (top to bottom)
     * Useful for reading order preservation
     */
    fun getBlocksByReadingOrder(): List<TextBlock> {
        return textBlocks.sortedBy { it.boundingBox?.top ?: 0 }
    }
    
    /**
     * Helper: Check if specific keyword exists in OCR result
     * Case-insensitive search
     */
    fun containsKeyword(keyword: String): Boolean {
        return fullText.contains(keyword, ignoreCase = true)
    }
    
    /**
     * Helper: Find text blocks containing specific pattern
     */
    fun findBlocksContaining(pattern: String): List<TextBlock> {
        return textBlocks.filter { 
            it.text.contains(pattern, ignoreCase = true) 
        }
    }
    
    /**
     * Helper: Get confidence statistics
     */
    fun getAverageConfidence(): Float {
        if (textBlocks.isEmpty()) return 0f
        return textBlocks.map { it.confidence }.average().toFloat()
    }
    
    /**
     * Helper: Check if OCR result is empty/invalid
     */
    fun isEmpty(): Boolean = textBlocks.isEmpty() || fullText.isBlank()
    
    /**
     * Helper: Get text in specific screen region
     */
    fun getTextInRegion(region: Rect): List<TextBlock> {
        return textBlocks.filter { block ->
            block.boundingBox?.let { Rect.intersects(it, region) } ?: false
        }
    }
    
    companion object {
        /**
         * Empty result for error cases
         */
        fun empty(screenId: String = ""): OcrResult {
            return OcrResult(
                screenId = screenId,
                timestamp = System.currentTimeMillis(),
                fullText = "",
                textBlocks = emptyList(),
                language = "unknown"
            )
        }
    }
}
