package com.example.votekt.domain.core

data class AppError(val errorType: ErrorType): Exception() {
    companion object {
        fun fromThrowable(e: Throwable) = AppError(ErrorType.fromThrowable(e))
    }
}

enum class ErrorType {
    NODE_CONNECTION_ERROR,
    UNKNOWN_ERROR,
    MNEMONIC_CONFIRMATION_INCOMPLETE,
    MNEMONIC_CONFIRMATION_WRONG,
    ;

    companion object {
        fun fromThrowable(e: Throwable): ErrorType {
            // Here may be additional mapping depending on exception type
            return when (e) {
                is AppError -> e.errorType
                else -> UNKNOWN_ERROR
            }
        }
    }
}
