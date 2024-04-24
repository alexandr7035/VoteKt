package com.example.votekt.ui.feature_app_lock.setup_applock.create_pin

sealed class CreatePinIntent {
    data class PinFieldChange(val pin: String): CreatePinIntent()
}
