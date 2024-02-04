package com.example.votekt.domain.core

sealed class AppError() {
    object ConnectionError : AppError()
    data class UnknownError(val details: String) : AppError()

    val defaultMessage: UiErrorMessage
        get() {
            return when (this) {
                is ConnectionError -> {
                    UiErrorMessage(
                        title = "Connection failed", message = "Check your Internet connection and try again"
                    )
                }

                is UnknownError -> {
                    UiErrorMessage(
                        title = "Unknown error", message = "Contact the developer or try again later"
                    )
                }
            }
        }
}

class UiErrorMessage(val title: String, val message: String)

