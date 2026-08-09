package com.vmax.core_intelligence

/**
 * VMAX Enterprise v2.6.1 - OCR Result
 * 
 * Responsibility: Structured OCR execution payload holder.
 * 
 * Architecture Rules:
 * - Immutable data class
 * - Pure Kotlin / Platform-Independent (Zero Android SDK dependencies)
 * - Raw evidence contract for downstream processing (TextClassifier / Workflow)
 */
data class OcrResult(
    /**
     * Unique identifier for the screen capture session
     */
    val screenId: String,
    
    /**
     * Timestamp when OCR was performed
     */
    val timestamp: Long,
    
    /**
     * Complete raw text extracted from the entire screen
     */
    val fullText: String,
    
    /**
     * Individual text blocks with structural metadata
     */
    val textBlocks: List<TextBlock>,
    
    /**
     * Detected language code (e.g., "en", "hi")
     */
    val language: String = "unknown"
) {

    /**
     * Represents a single recognized text block
     */
    data class TextBlock(
        val text: String,
        val confidence: Float,
        val boundingBox: BoundingBox?,
        val lines: List<String>
    )

    /**
     * Pure Kotlin platform-independent bounding box representation
     */
    data class BoundingBox(
        val left: Int,
        val top: Int,
        val right: Int,
        val bottom: Int
    ) {
        /**
         * Checks if this bounding box intersects with another bounding box
         */
        fun intersects(other: BoundingBox): Boolean {
            return !(other.left > right || other.right < left ||
                     other.top > bottom || other.bottom < top)
        }

        /**
         * Checks if a specific point (x, y) resides inside this bounding box
         */
        fun contains(x: Int, y: Int): Boolean {
            return x in left..right && y in top..bottom
        }
    }

    /**
     * Returns cleaned, non-blank text lines joined by newlines
     */
    fun getCleanedText(): String {
        return textBlocks
            .map { it.text.trim() }
            .filter { it.isNotBlank() }
            .joinToString("\n")
    }

    /**
     * Sorts text blocks vertically from top to bottom based on top coordinate
     */
    fun getBlocksByReadingOrder(): List<TextBlock> {
        return textBlocks.sortedBy { it.boundingBox?.top ?: 0 }
    }

    /**
     * Case-insensitive keyword lookup against full extracted text
     */
    fun containsKeyword(keyword: String): Boolean {
        return fullText.contains(keyword, ignoreCase = true)
    }

    /**
     * Finds text blocks matching a given string pattern
     */
    fun findBlocksContaining(pattern: String): List<TextBlock> {
        return textBlocks.filter { 
            it.text.contains(pattern, ignoreCase = true) 
        }
    }

    /**
     * Calculates average confidence across all detected text blocks
     */
    fun getAverageConfidence(): Float {
        if (textBlocks.isEmpty()) return 0f
        return textBlocks.map { it.confidence }.average().toFloat()
    }

    /**
     * Returns true if no text blocks exist or full text is blank
     */
    fun isEmpty(): Boolean = textBlocks.isEmpty() || fullText.isBlank()

    /**
     * Filters text blocks residing within or intersecting a specified rectangular region
     */
    fun getTextInRegion(region: BoundingBox): List<TextBlock> {
        return textBlocks.filter { block ->
            block.boundingBox?.let { region.intersects(it) } ?: false
        }
    }

    companion object {
        /**
         * Fallback empty instance for error or uninitialized states
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
