package by.alexandr7035.votekt.ui.feature.wallet.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.usecase.account.GetSelfAccountUseCase
import by.alexandr7035.votekt.ui.asTextError
import by.alexandr7035.votekt.ui.feature.wallet.share.model.SelfWalletDialogIntent
import by.alexandr7035.votekt.ui.feature.wallet.share.model.SelfWalletDialogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelfWalletQrDialogViewModel(
    private val getSelfAccountUseCase: GetSelfAccountUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SelfWalletDialogState())
    val state = _state.asStateFlow()

    fun emitIntent(intent: SelfWalletDialogIntent) {
        when (intent) {
            is SelfWalletDialogIntent.EnterScreen -> {
                _state.update {
                    it.copy(isLoading = true)
                }

                viewModelScope.launch {
                    val res = OperationResult.runWrapped {
                        getSelfAccountUseCase.invoke()
                    }

                    when (res) {
                        is OperationResult.Failure -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = res.error.errorType.asTextError()
                                )
                            }
                        }
                        is OperationResult.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    address = res.data
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
