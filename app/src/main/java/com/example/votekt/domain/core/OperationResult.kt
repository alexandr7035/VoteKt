package com.example.votekt.domain.core

sealed class OperationResult<out T> {
    data class Success<out T>(val data: T) : OperationResult<T>()
    data class Failure(val error: AppError) : OperationResult<Nothing>()

    companion object {
        suspend inline fun <R> runWrapped(
            crossinline block: suspend () -> R
        ): OperationResult<R> {
            return try {
                val res = block()
                Success(res)
            } catch (e: Exception) {
                Failure(AppError.fromThrowable(e))
            }
        }
    }
}
