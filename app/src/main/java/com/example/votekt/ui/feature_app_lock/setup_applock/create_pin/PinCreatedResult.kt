package com.example.votekt.ui.feature_app_lock.setup_applock.create_pin

data class PinCreatedResult(
    val pin: String,
    val shouldRequestBiometrics: Boolean,
)
