package com.example.votekt.domain.core

import android.util.Log

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
        private val TAG = AppError::class.java.simpleName

        fun fromThrowable(e: Throwable): ErrorType {
            Log.d(TAG, "wrapping error ${e}: ${e.message}")
            // Here may be additional mapping depending on exception type
            return when (e) {
                is AppError -> e.errorType
                else -> UNKNOWN_ERROR
            }
        }
    }
}
