package by.alexandr7035.votekt.ui.feature.applock.setup.pincode

sealed class CreatePinIntent {
    data class PinFieldChange(val pin: String) : CreatePinIntent()
}
