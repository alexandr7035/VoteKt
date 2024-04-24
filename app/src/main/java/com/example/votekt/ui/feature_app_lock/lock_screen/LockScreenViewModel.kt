package com.example.votekt.ui.feature_app_lock.lock_screen

import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.R
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.security.AuthenticateWithPinUseCase
import com.example.votekt.domain.security.CheckAppLockedWithBiometricsUseCase
import com.example.votekt.domain.security.CheckIfBiometricsAvailableUseCase
import com.example.votekt.domain.security.DecryptPinWithBiometricsUseCase
import com.example.votekt.domain.security.GetBiometricDecryptionCipherUseCase
import com.example.votekt.domain.security.GetBiometricEncryptedPinUseCase
import com.example.votekt.domain.security.model.AuthenticationResult
import com.example.votekt.domain.security.model.BiometricsAvailability
import com.example.votekt.domain.security.model.PinCode
import com.example.votekt.domain.usecase.account.LogoutUseCase
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.feature_app_lock.ui.AppLockUiState
import com.example.votekt.ui.feature_app_lock.ui.BiometricAuthResult
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LockScreenViewModel(
    private val authenticateWithPinUseCase: AuthenticateWithPinUseCase,
    private val checkIfBiometricsAvailableUseCase: CheckIfBiometricsAvailableUseCase,
    private val checkIfAppLockedWithBiometricsUseCase: CheckAppLockedWithBiometricsUseCase,

    private val getBiometricEncryptedPinUseCase: GetBiometricEncryptedPinUseCase,
    private val getBiometricDecryptionCipherUseCase: GetBiometricDecryptionCipherUseCase,
    private val decryptPinWithBiometricsUseCase: DecryptPinWithBiometricsUseCase,

    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(
        LockScreenState(
            uiState = AppLockUiState(
                prompt = UiText.StringResource(R.string.unlock_app),
                showBiometricsBtn = true
            ),
        )
    )

    val state = _state.asStateFlow()

    fun onIntent(intent: LockScreenIntent) {
        when (intent) {
            is LockScreenIntent.PinFieldChange -> reducePinChanged(intent)
            is LockScreenIntent.BiometricsBtnClicked -> {
                _state.update {
                    it.copy(showBiometricsPromptEvent = triggered)
                }
            }

            is LockScreenIntent.ConsumeBiometricAuthResult -> {
                when (intent.result) {
                    is BiometricAuthResult.Success -> {
                        val encryptedPin = getBiometricEncryptedPinUseCase.invoke()

                        intent.result.authData.cryptoObject?.cipher?.let { cipher ->
                            encryptedPin?.let {
                                val pinCode = decryptPinWithBiometricsUseCase.invoke(encryptedPin, cipher)
                                authenticateWithPinCode(pinCode)
                            } ?: run {
                                reduceBiometricsError()
                            }
                        }
                    }

                    is BiometricAuthResult.Failure -> {}
                }
            }

            is LockScreenIntent.RefreshBiometricsAvailability -> {
                val appLockedWithBiometrics = checkIfAppLockedWithBiometricsUseCase.invoke()

                // Check if locked first, as it's faster
                if (!appLockedWithBiometrics) {
                    _state.update {
                        it.copy(uiState = it.uiState.copy(showBiometricsBtn = false))
                    }
                } else {
                    // Check biometrics still available
                    val biometricsAvailable = checkIfBiometricsAvailableUseCase.invoke()
                    val showBiometricsBtn = biometricsAvailable == BiometricsAvailability.Available

                    _state.update {
                        it.copy(
                            uiState = it.uiState.copy(showBiometricsBtn = showBiometricsBtn),
                            showBiometricsPromptEvent = triggered
                        )
                    }
                }
            }

            is LockScreenIntent.AuthenticateWithBiometrics -> {
                val prompt = intent.data.first
                val promptUi = intent.data.second

                getBiometricDecryptionCipherUseCase.invoke()?.let {
                    prompt.authenticate(promptUi, BiometricPrompt.CryptoObject(it))
                } ?: run {
                    reduceBiometricsError()
                }
            }

            LockScreenIntent.LogOut -> {
                reduceLogoOut()
            }
        }
    }

    private fun reduceLogoOut() {
        viewModelScope.launch {
            val res = OperationResult.runWrapped {
                logoutUseCase.invoke()
            }

            _state.update {
                it.copy(
                    logoutEvent = triggered(res)
                )
            }
        }
    }

    private fun reducePinChanged(intent: LockScreenIntent.PinFieldChange) {
        _state.update {
            it.copy(
                uiState = it.uiState.copy(
                    pinValue = intent.pin,
                    error = null
                )
            )
        }

        if (intent.pin.length == PIN_LENGTH) {
            _state.update {
                it.copy(
                    uiState = it.uiState.copy(
                        isLoading = true
                    )
                )
            }

            authenticateWithPinCode(
                pinCode = PinCode(
                    value = _state.value.uiState.pinValue
                )
            )
        }
    }

    private fun authenticateWithPinCode(pinCode: PinCode) {
        viewModelScope.launch {
            val pinRes = authenticateWithPinUseCase.invoke(
                pin = pinCode
            )

            when (pinRes) {
                is AuthenticationResult.Success -> {
                    reducePinSuccess()
                }

                is AuthenticationResult.Failure -> {
                    reducePinError()
                }
            }
        }
    }

    private fun reducePinSuccess() {
        _state.update {
            it.copy(
                appUnlockEvent = triggered,
                uiState = it.uiState.copy(
                    isLoading = false
                )
            )
        }
    }

    private fun reducePinError() {
        _state.update {
            it.copy(
                uiState = it.uiState.copy(
                    isLoading = false,
                    pinValue = DEFAULT_PIN,
                    error = UiText.StringResource(R.string.invalid_pin)
                ),
            )
        }
    }

    fun consumeUnlockWithPinEvent() {
        _state.update {
            it.copy(
                appUnlockEvent = consumed
            )
        }
    }

    private fun reduceBiometricsError() {
        _state.update {
            it.copy(
                uiState = it.uiState.copy(
                    error = UiText.StringResource(R.string.error_biometrics_corrupted)
                ),
            )
        }
    }

    fun consumeShowBiometricsPromptEvent() {
        _state.update {
            it.copy(
                showBiometricsPromptEvent = consumed
            )
        }
    }

    fun consumeLogoutEvent() {
        _state.update {
            it.copy(
                logoutEvent = consumed()
            )
        }
    }

    companion object {
        private const val PIN_LENGTH = 4
        private const val DEFAULT_PIN = ""
    }
}