package com.example.votekt.ui

import com.example.votekt.domain.core.ErrorType

class UiErrorMessage(
    val title: String,
    val message: String
)

val ErrorType.defaultMessage: UiErrorMessage
    get() {
        return when (this) {
            ErrorType.NODE_CONNECTION_ERROR -> {
                UiErrorMessage(
                    title = "Connection failed", message = "Check your Internet connection and try again"
                )
            }

            ErrorType.UNKNOWN_ERROR -> {
                UiErrorMessage(
                    title = "Unknown error", message = "Contact the developer or try again later"
                )
            }
        }
    }