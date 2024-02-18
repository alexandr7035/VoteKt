package com.example.votekt.ui

import com.example.votekt.domain.core.ErrorType
import org.bouncycastle.asn1.x500.style.RFC4519Style.title

class UiErrorMessage(
    val title: String,
    val message: String
)

val ErrorType.uiError: UiErrorMessage
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

            ErrorType.MNEMONIC_CONFIRMATION_INCOMPLETE -> UiErrorMessage(
                title = "Phrase is incomplete", message = "Please, fill all words"
            )
            ErrorType.MNEMONIC_CONFIRMATION_WRONG -> UiErrorMessage(
                title = "Wrong phrase", message = "Wrong words. Please, try again"
            )
        }
    }