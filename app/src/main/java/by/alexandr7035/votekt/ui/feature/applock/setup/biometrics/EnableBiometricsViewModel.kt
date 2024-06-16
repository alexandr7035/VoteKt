package by.alexandr7035.votekt.ui.feature.applock.setup.biometrics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.model.security.BiometricsAvailability
import by.alexandr7035.votekt.domain.model.security.PinCode
import by.alexandr7035.votekt.domain.usecase.applock.CheckIfBiometricsAvailableUseCase
import by.alexandr7035.votekt.domain.usecase.applock.SetupAppLockedWithBiometricsUseCase
import by.alexandr7035.votekt.ui.core.resources.UiText
import by.alexandr7035.votekt.ui.feature.applock.core.BiometricAuthResult
import by.alexandr7035.votekt.ui.feature.applock.core.BiometricsPromptUi
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EnableBiometricsViewModel(
    private val setupAppLockedWithBiometricsUseCase: SetupAppLockedWithBiometricsUseCase,
    private val checkIfBiometricsAvailableUseCase: CheckIfBiometricsAvailableUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        EnableBiometricsState(
            prompt = BiometricsPromptUi(
                title = UiText.StringResource(R.string.setup_biometrics),
                cancelBtnText = UiText.StringResource(R.string.cancel)
            ),
            biometricsAvailability = BiometricsAvailability.Checking,
        )
    )

    val state = _state.asStateFlow()

    fun emitIntent(intent: EnableBiometricsIntent) {
        when (intent) {
            is EnableBiometricsIntent.EnterScreen -> {
                _state.update { it.copy(pinToEncrypt = intent.pinToEncrypt) }
            }

            is EnableBiometricsIntent.RefreshBiometricsAvailability -> {
                val availability = checkIfBiometricsAvailableUseCase.invoke()
                _state.update {
                    it.copy(biometricsAvailability = availability)
                }
            }

            is EnableBiometricsIntent.ConsumeAuthResult -> {
                if (intent.result is BiometricAuthResult.Success) {
                    _state.value.pinToEncrypt?.let {
                        enableBiometricsLock(it)
                    }
                }

                _state.update {
                    it.copy(authResultEvent = triggered(intent.result))
                }
            }

            is EnableBiometricsIntent.AuthenticateWithBiometrics -> {
                val prompt = intent.data.first
                val promptUi = intent.data.second
                prompt.authenticate(promptUi)
            }
        }
    }

    private fun enableBiometricsLock(pinToEncrypt: String) {
        viewModelScope.launch {
            OperationResult.runWrapped {
                setupAppLockedWithBiometricsUseCase.invoke(
                    isLocked = true,
                    pinToEncrypt = PinCode(pinToEncrypt)
                )
            }
        }
    }

    fun consumeAuthEvent() {
        _state.update {
            it.copy(authResultEvent = consumed())
        }
    }
}
