package by.alexandr7035.votekt.ui

import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.core.ErrorType
import by.alexandr7035.votekt.ui.core.resources.UiText

fun ErrorType.asTextError(): UiText {
    return when (this) {
        ErrorType.NODE_CONNECTION_ERROR -> UiText.StringResource(R.string.no_connection)
        ErrorType.MNEMONIC_CONFIRMATION_INCOMPLETE -> UiText.StringResource(
            R.string.error_mnemonic_confirmation_incomplete
        )
        ErrorType.MNEMONIC_CONFIRMATION_WRONG -> UiText.StringResource(R.string.error_mnemonic_confirmation_invalid)
        ErrorType.MNEMONIC_INVALID -> UiText.StringResource(R.string.error_mnemonic_invalid)
        ErrorType.MNEMONIC_INVALID_WORD_COUNT -> UiText.StringResource(R.string.error_mnemonic_incomplete)

        ErrorType.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error)
    }
}

class UiErrorMessage(
    val title: String,
    val message: String
)

val ErrorType.uiError: UiErrorMessage
    get() {
        return when (this) {
            ErrorType.NODE_CONNECTION_ERROR -> {
                UiErrorMessage(
                    title = "Connection failed",
                    message = "Check your Internet connection and try again"
                )
            }

            ErrorType.UNKNOWN_ERROR -> {
                UiErrorMessage(
                    title = "Unknown error",
                    message = "Contact the developer or try again later"
                )
            }

            ErrorType.MNEMONIC_CONFIRMATION_INCOMPLETE -> UiErrorMessage(
                title = "Phrase is incomplete",
                message = "Please, fill all words"
            )
            ErrorType.MNEMONIC_CONFIRMATION_WRONG -> UiErrorMessage(
                title = "Wrong phrase",
                message = "Wrong words. Please, try again"
            )

            // TODO remove uiErrorMessage
            else -> UiErrorMessage("todo", "remove this")
        }
    }
