package by.alexandr7035.votekt.ui.feature.applock.lockscreen

import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.model.security.AuthenticationResult
import by.alexandr7035.votekt.domain.model.security.BiometricsAvailability
import by.alexandr7035.votekt.domain.model.security.PinCode
import by.alexandr7035.votekt.domain.usecase.account.LogoutUseCase
import by.alexandr7035.votekt.domain.usecase.applock.AuthenticateWithPinUseCase
import by.alexandr7035.votekt.domain.usecase.applock.CheckAppLockedWithBiometricsUseCase
import by.alexandr7035.votekt.domain.usecase.applock.CheckIfBiometricsAvailableUseCase
import by.alexandr7035.votekt.domain.usecase.applock.DecryptPinWithBiometricsUseCase
import by.alexandr7035.votekt.domain.usecase.applock.GetBiometricDecryptionCipherUseCase
import by.alexandr7035.votekt.domain.usecase.applock.GetBiometricEncryptedPinUseCase
import by.alexandr7035.votekt.ui.core.resources.UiText
import by.alexandr7035.votekt.ui.feature.applock.core.AppLockUiState
import by.alexandr7035.votekt.ui.feature.applock.core.BiometricAuthResult
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
            is LockScreenIntent.PinFieldChange -> onPinChanged(intent)
            is LockScreenIntent.BiometricsBtnClicked -> onShowBiometricPrompt()
            is LockScreenIntent.ConsumeBiometricAuthResult -> onConsumeBiometricAuthResult(intent)
            is LockScreenIntent.RefreshBiometricsAvailability -> onRefreshBiometricsAvailabillity()
            is LockScreenIntent.AuthenticateWithBiometrics -> onAuthenticateWithBiometrics(intent)
            is LockScreenIntent.LogOut -> onLogOut()
        }
    }

    private fun onShowBiometricPrompt() {
        _state.update {
            it.copy(showBiometricsPromptEvent = triggered)
        }
    }

    private fun onAuthenticateWithBiometrics(intent: LockScreenIntent.AuthenticateWithBiometrics) {
        val prompt = intent.data.first
        val promptUi = intent.data.second

        getBiometricDecryptionCipherUseCase.invoke()?.let {
            prompt.authenticate(promptUi, BiometricPrompt.CryptoObject(it))
        } ?: run {
            reduceBiometricsError()
        }
    }

    private fun onRefreshBiometricsAvailabillity() {
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

    private fun onConsumeBiometricAuthResult(
        intent: LockScreenIntent.ConsumeBiometricAuthResult,
    ) {
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

    private fun onLogOut() {
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

    private fun onPinChanged(intent: LockScreenIntent.PinFieldChange) {
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
