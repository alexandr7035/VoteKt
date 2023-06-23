package com.example.votekt.data

sealed class OperationResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : OperationResult<T>()
    data class Failure(val error: AppError) : OperationResult<Nothing>()
}