package by.alexandr7035.votekt.ui.feature.applock.setup.pincode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.security.CheckIfBiometricsAvailableUseCase
import by.alexandr7035.votekt.domain.security.SetupAppLockUseCase
import by.alexandr7035.votekt.domain.security.model.BiometricsAvailability
import by.alexandr7035.votekt.domain.security.model.PinCode
import by.alexandr7035.votekt.ui.core.resources.UiText
import by.alexandr7035.votekt.ui.feature.applock.core.AppLockUiState
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePinViewModel(
    private val setupAppLockUseCase: SetupAppLockUseCase,
    private val checkIfBiometricsAvailableUseCase: CheckIfBiometricsAvailableUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CreatePinState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                uiState = it.uiState.copy(
                    prompt = UiText.StringResource(R.string.create_pin)
                )
            )
        }
    }

    fun onIntent(intent: CreatePinIntent) {
        when (intent) {
            is CreatePinIntent.PinFieldChange -> reduce(intent)
        }
    }

    private fun reduce(intent: CreatePinIntent.PinFieldChange) {
        _state.update {
            it.copy(
                uiState = it.uiState.copy(
                    pinValue = intent.pin,
                    // Clear error on edit
                    error = null
                )
            )
        }

        if (intent.pin.length == PIN_LENGTH) {
            _state.update {
                it.copy(uiState = it.uiState.copy(isLoading = true))
            }
            reducePinCompleted(intent.pin)
        }
    }

    private fun reducePinCompleted(pin: String) {
        val currentState = _state.value

        viewModelScope.launch {
            // A little delay for UI UX
            delay(100)

            if (!currentState.isConfirmationStage) {
                // Reduce confirmation when first pin entered
                _state.update {
                    it.copy(
                        initialPin = pin,
                        isConfirmationStage = true,
                        // Reset UI state
                        uiState = AppLockUiState(
                            prompt = UiText.StringResource(R.string.confirm_pin)
                        )
                    )
                }
            } else {
                val pinsMatch = currentState.initialPin == pin
                if (pinsMatch) {
                    // Save pin
                    setupAppLockUseCase.invoke(pinCode = PinCode(pin))

                    // Check biometrics available for next step
                    val biometricsRes = checkIfBiometricsAvailableUseCase.invoke()
                    val shouldRequestBiometrics = biometricsRes != BiometricsAvailability.NotAvailable

                    _state.update {
                        it.copy(
                            pinCreatedEvent = triggered(
                                PinCreatedResult(
                                    shouldRequestBiometrics = shouldRequestBiometrics,
                                    pin = pin,
                                )
                            )
                        )
                    }
                } else {
                    _state.value = CreatePinState(
                        // Reset default state
                        uiState = AppLockUiState(
                            prompt = UiText.StringResource(R.string.create_pin),
                            error = UiText.StringResource(R.string.pins_don_t_match)
                        )
                    )
                }
            }
        }
    }

    fun consumePinCreatedEvent() {
        _state.update {
            it.copy(pinCreatedEvent = consumed())
        }
    }

    companion object {
        private const val PIN_LENGTH = 4
    }
}
