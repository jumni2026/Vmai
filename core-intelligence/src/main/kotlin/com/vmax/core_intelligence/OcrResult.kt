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
        fun intersects(other: BoundingBox): Boolean {
            return !(other.left > right || other.right < left ||
                     other.top > bottom || other.bottom < top)
        }

        fun contains(x: Int, y: Int): Boolean {
            return x in left..right && y in top..bottom
        }
    }

    fun getCleanedText(): String {
        return textBlocks
            .map { it.text.trim() }
            .filter { it.isNotBlank() }
            .joinToString("\n")
    }

    fun getBlocksByReadingOrder(): List<TextBlock> {
        return textBlocks.sortedBy { it.boundingBox?.top ?: 0 }
    }

    fun containsKeyword(keyword: String): Boolean {
        return fullText.contains(keyword, ignoreCase = true)
    }

    fun findBlocksContaining(pattern: String): List<TextBlock> {
        return textBlocks.filter { 
            it.text.contains(pattern, ignoreCase = true) 
        }
    }

    fun getAverageConfidence(): Float {
        if (textBlocks.isEmpty()) return 0f
        var total = 0f
        var count = 0
        for (block in textBlocks) {
            total += block.confidence
            count++
        }
        return total / count
    }

    fun isEmpty(): Boolean = textBlocks.isEmpty() || fullText.isBlank()

    fun getTextInRegion(region: BoundingBox): List<TextBlock> {
        val result = mutableListOf<TextBlock>()
        for (block in textBlocks) {
            val box = block.boundingBox
            if (box != null && region.intersects(box)) {
                result.add(block)
            }
        }
        return result
    }

    companion object {
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
