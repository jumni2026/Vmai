package com.vmax.action

import com.vmax.common.Result

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 15 — InputStrategy
 *
 * Defines input strategies for different text entry scenarios.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
interface InputStrategy {

    enum class InputMode {
        SET_TEXT,
        CHARACTER_BY_CHARACTER,
        PASTE
    }

    data class InputRequest(
        val targetId: String,
        val text: String,
        val mode: InputMode = InputMode.SET_TEXT,
        val delayBetweenChars: Long = 100L,
        val validateAfter: Boolean = true
    )

    data class InputResult(
        val success: Boolean,
        val mode: InputMode,
        val charactersEntered: Int,
        val expectedLength: Int,
        val message: String? = null
    )

    fun getInputModeForText(text: String): InputMode

    fun shouldUseSetText(text: String): Boolean

    fun shouldUseCharacterInput(text: String): Boolean

    fun executeInput(request: InputRequest): Result<InputResult, InputError>

    fun getDelayBetweenChars(): Long

    fun setDelayBetweenChars(delayMs: Long)

    fun getDefaultMode(): InputMode

    fun isTextLengthExceeded(text: String, maxLength: Int): Boolean
}

data class InputError(
    val code: String,
    val message: String,
    val targetId: String? = null,
    val text: String? = null
)
