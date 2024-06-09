package com.example.votekt.ui.feature.applock.setup.pincode

sealed class CreatePinIntent {
    data class PinFieldChange(val pin: String) : CreatePinIntent()
}
