package com.example.votekt.data

sealed class AppError() {
    object ConnectionError : AppError()
    data class UnknownError(val details: String): AppError()
}
