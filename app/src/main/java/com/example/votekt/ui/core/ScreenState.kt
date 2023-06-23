package com.example.votekt.ui.core

import com.example.votekt.data.AppError

data class ScreenState<T>(
    val data: T?,
    val error: AppError?,
    val isLoading: Boolean
) {
    fun shouldShowData(): Boolean {
        return data != null && !isLoading && error == null
    }

    fun shouldShowFullLoading(): Boolean {
        return data == null && isLoading
    }

    fun shouldShowPartialLoading(): Boolean {
        return data != null && isLoading
    }

    fun shouldShowFullError(): Boolean {
        return this.error != null && !isLoading && data == null
    }

    fun shouldShowPartialError(): Boolean {
        return this.error != null && !isLoading && data != null
    }
}
