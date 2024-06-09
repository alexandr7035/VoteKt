package com.example.votekt.ui.feature.applock.setup.pincode

data class PinCreatedResult(
    val pin: String,
    val shouldRequestBiometrics: Boolean,
)
