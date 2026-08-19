package com.vmax.core_intelligence

/**
 * VMAX Enterprise v2.6.1
 *
 * File:
 * OcrResult.kt
 *
 * Canonical immutable OCR result contract.
 *
 * Responsibilities:
 * - Store raw OCR output.
 * - Store structured OCR text blocks.
 * - Store screen identity and timestamp.
 * - Validate OCR confidence and geometry.
 * - Provide safe text/search helpers.
 *
 * Architecture:
 * - Platform independent.
 * - No Android imports.
 * - No ML Kit imports.
 * - No runtime dependencies.
 *
 * IMPORTANT:
 * Constructor names are canonical:
 *
 * screenId
 * timestamp
 * fullText
 * textBlocks
 * language
 */
data class OcrResult(
    val screenId: String,
    val timestamp: Long,
    val fullText: String,
    val textBlocks: List<TextBlock>,
    val language: String = UNKNOWN_LANGUAGE
) {

    init {

        require(timestamp >= 0L) {
            "timestamp must be >= 0"
        }

        textBlocks.forEach { block ->

            require(block.confidence in 0f..1f) {
                "OCR block confidence must be in [0,1]"
            }

            block.boundingBox?.let { box ->

                require(box.left <= box.right) {
                    "Invalid bounding box: left=${box.left}, right=${box.right}"
                }

                require(box.top <= box.bottom) {
                    "Invalid bounding box: top=${box.top}, bottom=${box.bottom}"
                }
            }
        }
    }

    /**
     * Structured OCR text block.
     */
    data class TextBlock(
        val text: String,
        val confidence: Float,
        val boundingBox: BoundingBox? = null,
        val lines: List<String> = emptyList()
    ) {

        init {

            require(confidence in 0f..1f) {
                "confidence must be in [0,1]"
            }
        }

        /**
         * Returns trimmed block text.
         */
        fun cleanedText(): String =
            text.trim()

        /**
         * Returns true when this block contains
         * non-blank text.
         */
        fun isNotBlank(): Boolean =
            text.isNotBlank()

        /**
         * Case-insensitive text matching.
         */
        fun contains(
            value: String,
            ignoreCase: Boolean = true
        ): Boolean {

            if (value.isBlank()) {
                return false
            }

            return text.contains(
                other = value,
                ignoreCase = ignoreCase
            )
        }
    }

    /**
     * Immutable rectangular OCR region.
     */
    data class BoundingBox(
        val left: Int,
        val top: Int,
        val right: Int,
        val bottom: Int
    ) {

        init {

            require(left <= right) {
                "left must be <= right"
            }

            require(top <= bottom) {
                "top must be <= bottom"
            }
        }

        /**
         * Width of the bounding box.
         */
        val width: Int
            get() = right - left

        /**
         * Height of the bounding box.
         */
        val height: Int
            get() = bottom - top

        /**
         * True when the rectangle has usable dimensions.
         */
        fun isValid(): Boolean =
            width > 0 && height > 0

        /**
         * Checks whether this box intersects another box.
         */
        fun intersects(
            other: BoundingBox
        ): Boolean {

            return !(
                other.left > right ||
                    other.right < left ||
                    other.top > bottom ||
                    other.bottom < top
                )
        }

        /**
         * Checks whether this box contains a point.
         */
        fun contains(
            x: Int,
            y: Int
        ): Boolean {

            return x in left..right &&
                y in top..bottom
        }

        /**
         * Returns the center X coordinate.
         */
        fun centerX(): Int =
            left + width / 2

        /**
         * Returns the center Y coordinate.
         */
        fun centerY(): Int =
            top + height / 2
    }

    // -------------------------------------------------------------------------
    // Text helpers
    // -------------------------------------------------------------------------

    /**
     * Returns OCR text reconstructed from structured blocks.
     *
     * fullText remains the original/raw OCR text.
     */
    fun getCleanedText(): String {

        return textBlocks
            .map { it.text.trim() }
            .filter { it.isNotBlank() }
            .joinToString(separator = "\n")
    }

    /**
     * Returns blocks in approximate reading order:
     * top first, then left.
     */
    fun getBlocksByReadingOrder(): List<TextBlock> {

        return textBlocks.sortedWith(
            compareBy(
                { it.boundingBox?.top ?: Int.MAX_VALUE },
                { it.boundingBox?.left ?: Int.MAX_VALUE }
            )
        )
    }

    /**
     * Returns true if the raw OCR text contains the keyword.
     */
    fun containsKeyword(
        keyword: String
    ): Boolean {

        if (keyword.isBlank()) {
            return false
        }

        return fullText.contains(
            other = keyword,
            ignoreCase = true
        )
    }

    /**
     * Returns true if any OCR block contains the supplied text.
     */
    fun containsText(
        value: String
    ): Boolean {

        if (value.isBlank()) {
            return false
        }

        return textBlocks.any {
            it.contains(value)
        }
    }

    /**
     * Finds all blocks containing the supplied pattern.
     */
    fun findBlocksContaining(
        pattern: String
    ): List<TextBlock> {

        if (pattern.isBlank()) {
            return emptyList()
        }

        return textBlocks.filter {
            it.contains(pattern)
        }
    }

    /**
     * Returns blocks intersecting the requested region.
     */
    fun getTextInRegion(
        region: BoundingBox
    ): List<TextBlock> {

        return textBlocks.filter { block ->

            val box =
                block.boundingBox
                    ?: return@filter false

            region.intersects(box)
        }
    }

    /**
     * Returns blocks fully contained by the requested region.
     */
    fun getTextFullyInRegion(
        region: BoundingBox
    ): List<TextBlock> {

        return textBlocks.filter { block ->

            val box =
                block.boundingBox
                    ?: return@filter false

            box.left >= region.left &&
                box.top >= region.top &&
                box.right <= region.right &&
                box.bottom <= region.bottom
        }
    }

    // -------------------------------------------------------------------------
    // Confidence
    // -------------------------------------------------------------------------

    /**
     * Returns average OCR confidence.
     *
     * Empty OCR result returns 0f.
     */
    fun getAverageConfidence(): Float {

        if (textBlocks.isEmpty()) {
            return 0f
        }

        return textBlocks
            .map { block ->
                block.confidence.coerceIn(0f, 1f)
            }
            .average()
            .toFloat()
    }

    /**
     * Returns the highest confidence among OCR blocks.
     */
    fun getMaxConfidence(): Float {

        return textBlocks
            .maxOfOrNull {
                it.confidence.coerceIn(0f, 1f)
            }
            ?: 0f
    }

    /**
     * Returns the lowest confidence among OCR blocks.
     */
    fun getMinConfidence(): Float {

        return textBlocks
            .minOfOrNull {
                it.confidence.coerceIn(0f, 1f)
            }
            ?: 0f
    }

    /**
     * Returns true when OCR contains no usable text.
     */
    fun isEmpty(): Boolean {

        return fullText.isBlank() &&
            textBlocks.none {
                it.text.isNotBlank()
            }
    }

    /**
     * Returns true when OCR contains at least one
     * usable text block.
     */
    fun isNotEmpty(): Boolean =
        !isEmpty()

    // -------------------------------------------------------------------------
    // Normalized text
    // -------------------------------------------------------------------------

    /**
     * Returns normalized OCR text for matching/classification.
     *
     * Raw fullText is never modified.
     */
    fun getNormalizedText(): String {

        return fullText
            .trim()
            .replace(
                Regex("\\s+"),
                " "
            )
    }

    /**
     * Case-insensitive normalized keyword matching.
     */
    fun containsNormalized(
        keyword: String
    ): Boolean {

        if (keyword.isBlank()) {
            return false
        }

        val source =
            getNormalizedText()
                .lowercase()

        val target =
            keyword
                .trim()
                .replace(
                    Regex("\\s+"),
                    " "
                )
                .lowercase()

        return source.contains(target)
    }

    // -------------------------------------------------------------------------
    // Safe copies
    // -------------------------------------------------------------------------

    /**
     * Returns a copy containing only non-blank OCR blocks.
     */
    fun withoutBlankBlocks(): OcrResult {

        return copy(
            textBlocks =
                textBlocks.filter {
                    it.text.isNotBlank()
                }
        )
    }

    /**
     * Returns OCR blocks above the supplied confidence threshold.
     */
    fun withMinimumConfidence(
        minimumConfidence: Float
    ): OcrResult {

        require(
            minimumConfidence in 0f..1f
        ) {
            "minimumConfidence must be in [0,1]"
        }

        return copy(
            textBlocks =
                textBlocks.filter {
                    it.confidence >= minimumConfidence
                }
        )
    }

    companion object {

        const val UNKNOWN_LANGUAGE =
            "unknown"

        /**
         * Creates an empty OCR result.
         */
        @JvmStatic
        fun empty(
            screenId: String = ""
        ): OcrResult {

            return OcrResult(
                screenId = screenId,
                timestamp = System.currentTimeMillis(),
                fullText = "",
                textBlocks = emptyList(),
                language = UNKNOWN_LANGUAGE
            )
        }

        /**
         * Creates a validated OCR result with the
         * current timestamp.
         */
        @JvmStatic
        fun create(
            screenId: String,
            fullText: String,
            textBlocks: List<TextBlock>,
            language: String = UNKNOWN_LANGUAGE
        ): OcrResult {

            return OcrResult(
                screenId = screenId,
                timestamp = System.currentTimeMillis(),
                fullText = fullText,
                textBlocks = textBlocks.toList(),
                language = language
            )
        }
    }
}
