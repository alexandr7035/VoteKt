package com.example.votekt.domain.core

sealed class OperationResult<out T> {
    data class Success<out T>(val data: T) : OperationResult<T>()
    data class Failure(val error: AppError) : OperationResult<Nothing>()

    companion object {
        inline fun <R> runWrapped(block: () -> R): OperationResult<R> {
            return try {
                val res = block()
                Success(res)
            } catch (e: Exception) {
                when (e) {
                    is AppError -> Failure(e)
                    else -> Failure(AppError(ErrorType.fromThrowable(e)))
                }
            }
        }
    }
}