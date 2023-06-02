package com.example.votekt.ui

data class SimpleState<T>(
    val lastValue: T,
    val isLoading: Boolean
)
