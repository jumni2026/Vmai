package com.vmax.intelligence

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 9 — ScreenAnalyzer
 *
 * Analyzes screen content and structure for automation decisions.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface ScreenAnalyzer {

    data class ScreenElement(
        val id: String?,
        val text: String?,
        val className: String?,
        val bounds: String?,
        val isClickable: Boolean = false,
        val isEditable: Boolean = false,
        val isVisible: Boolean = true
    )

    data class ScreenAnalysis(
        val screenName: String,
        val elements: List<ScreenElement>,
        val visibleTexts: List<String>,
        val timestamp: Long = System.currentTimeMillis()
    )

    fun analyzeScreen(): ScreenAnalysis

    fun findElementById(id: String): ScreenElement?

    fun findElementByText(text: String): ScreenElement?

    fun findElementsByClass(className: String): List<ScreenElement>

    fun getVisibleTexts(): List<String>

    fun getClickableElements(): List<ScreenElement>

    fun getEditableElements(): List<ScreenElement>
}
