package by.alexandr7035.votekt.ui.feature.applock.core

import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.fragment.app.FragmentActivity
import by.alexandr7035.votekt.R

object BiometricsHelper {
    fun buildBiometricsPrompt(
        activity: FragmentActivity,
        prompt: BiometricsPromptUi,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (error: String) -> Unit,
    ): Pair<BiometricPrompt, PromptInfo> {
        val biometricPrompt = BiometricPrompt(
            activity,
            object : BiometricPrompt.AuthenticationCallback() {
                // Called when an unrecoverable error has been encountered and authentication has stopped.
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError.invoke(activity.getString(R.string.error_template, errString))
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess.invoke(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onError.invoke(activity.getString(R.string.error_auth_failed))
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(prompt.title.asString(activity))
            .setNegativeButtonText(prompt.cancelBtnText.asString(activity))
            .build()

        return biometricPrompt to promptInfo
    }
}
