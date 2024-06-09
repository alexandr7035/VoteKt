package by.alexandr7035.votekt.ui.feature.applock.setup.pincode

data class PinCreatedResult(
    val pin: String,
    val shouldRequestBiometrics: Boolean,
)
