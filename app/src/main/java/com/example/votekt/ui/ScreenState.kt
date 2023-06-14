package com.example.votekt.ui

data class ScreenState<T>(
    val value: T?,
    val error: Exception?,
    val isLoading: Boolean
)
