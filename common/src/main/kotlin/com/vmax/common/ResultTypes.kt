package com.vmax.common

/**
 * VMAX Enterprise v2.6
 *
 * Stage 1 — Skeleton
 * File 3 — ResultTypes
 *
 * Generic result types for VMAX Enterprise operations.
 * Platform-independent — no Android dependencies.
 * No external dependencies.
 * No business logic.
 */
sealed class Result<out T, out E> {

    data class Success<T>(val data: T) : Result<T, Nothing>()

    data class Error<E>(val error: E) : Result<Nothing, E>()

    inline fun isSuccess(): Boolean = this is Success

    inline fun isError(): Boolean = this is Error

    inline fun getSuccessOrNull(): T? = (this as? Success)?.data

    inline fun getErrorOrNull(): E? = (this as? Error)?.error

    inline fun onSuccess(action: (T) -> Unit): Result<T, E> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (E) -> Unit): Result<T, E> {
        if (this is Error) action(error)
        return this
    }
}
