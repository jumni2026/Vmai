package com.vmax.core_intelligence

/**
 * VMAX Enterprise v2.6.1 - OCR Result
 * Immutable data class holding structured OCR output.
 */
data class OcrResult(
    val screenId: String,
    val timestamp: Long,
    val fullText: String,                      // Raw OCR text, as-is
    val textBlocks: List<TextBlock>,
    val language: String = "unknown"
) {
    init {
        // Validate each block's confidence and bounding box
        textBlocks.forEach { block ->
            require(block.confidence in 0f..1f) { "Confidence must be in [0,1]" }
            block.boundingBox?.let { box ->
                require(box.left <= box.right && box.top <= box.bottom) {
                    "Invalid bounding box: left=${box.left}, right=${box.right}, top=${box.top}, bottom=${box.bottom}"
                }
            }
        }
    }

    data class TextBlock(
        val text: String,
        val confidence: Float,
        val boundingBox: BoundingBox?,
        val lines: List<String>
    ) {
        init {
            require(confidence in 0f..1f) { "Confidence must be in [0,1]" }
        }
    }

    data class BoundingBox(
        val left: Int,
        val top: Int,
        val right: Int,
        val bottom: Int
    ) {
        init {
            require(left <= right) { "left must be <= right" }
            require(top <= bottom) { "top must be <= bottom" }
        }

        fun intersects(other: BoundingBox): Boolean =
            !(other.left > right || other.right < left ||
                    other.top > bottom || other.bottom < top)

        fun contains(x: Int, y: Int): Boolean =
            x in left..right && y in top..bottom
    }

    /**
     * Cleans and joins text from all blocks.
     * This is the canonical "cleaned" text source.
     * (fullText remains raw; this provides normalized version from blocks)
     */
    fun getCleanedText(): String =
        textBlocks.map { it.text.trim() }
            .filter { it.isNotBlank() }
            .joinToString("\n")

    /**
     * Sorts blocks by top, then left (reading order).
     */
    fun getBlocksByReadingOrder(): List<TextBlock> =
        textBlocks.sortedWith(compareBy(
            { it.boundingBox?.top ?: 0 },
            { it.boundingBox?.left ?: 0 }
        ))

    fun containsKeyword(keyword: String): Boolean =
        fullText.contains(keyword, ignoreCase = true)

    fun findBlocksContaining(pattern: String): List<TextBlock> =
        textBlocks.filter { it.text.contains(pattern, ignoreCase = true) }

    /**
     * Average confidence with clamping.
     */
    fun getAverageConfidence(): Float {
        if (textBlocks.isEmpty()) return 0f
        var total = 0f
        var count = 0
        for (block in textBlocks) {
            total += block.confidence.coerceIn(0f, 1f)
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
        fun empty(screenId: String = ""): OcrResult =
            OcrResult(
                screenId = screenId,
                timestamp = System.currentTimeMillis(),
                fullText = "",
                textBlocks = emptyList(),
                language = "unknown"
            )
    }
}
