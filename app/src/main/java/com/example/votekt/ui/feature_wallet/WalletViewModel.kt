package com.example.votekt.ui.feature_wallet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.ui.utils.BalanceFormatter
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.usecase.account.LogoutUseCase
import com.example.votekt.domain.usecase.contract.GetContractStateUseCase
import com.example.votekt.ui.feature_wallet.model.WalletScreenIntent
import com.example.votekt.ui.feature_wallet.model.WalletScreenNavigationEvent
import com.example.votekt.ui.feature_wallet.model.WalletScreenState
import com.example.votekt.ui.uiError
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletViewModel(
    private val accountRepository: AccountRepository,
    private val contractStateUseCase: GetContractStateUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(WalletScreenState())
    val state = _state.asStateFlow()

    fun onWalletIntent(intent: WalletScreenIntent) {
        when (intent) {
            is WalletScreenIntent.LoadData -> reduceLoadData()
            is WalletScreenIntent.WalletAction -> reduceWalletAction(intent)
            is WalletScreenIntent.LogOut -> reduceLogOut()
        }
    }

    private fun reduceLogOut() {
        viewModelScope.launch {
            val logoutRes = OperationResult.runWrapped {
                logoutUseCase.invoke()
            }

            when (logoutRes) {
                is OperationResult.Failure -> _state.update {
                    it.copy(error = logoutRes.error.errorType.uiError)
                }
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(
                            navigationEvent = triggered(WalletScreenNavigationEvent.ToWelcomeScreen)
                        )
                    }
                }
            }
        }
    }

    // Consider using offline-first approach
    // Without full-screen errors
    private fun reduceLoadData() {
        viewModelScope.launch {
            val address = accountRepository.getSelfAddress()
            _state.update {
                it.copy(address = Address(address.hex))
            }
        }

        accountRepository
            .getAccountBalance()
            .onEach {
                reduceBalance(it)
            }
            .onCompletion { error ->
                error?.let {
                    reduceError(ErrorType.fromThrowable(error))
                }
            }
            .launchIn(viewModelScope)

        contractStateUseCase.invoke()
            .onEach { contractState ->
                _state.update {
                    it.copy(contractState = contractState)
                }
            }
            .catch { error ->
                reduceError(ErrorType.fromThrowable(error))
            }
            .launchIn(viewModelScope)
    }

    private fun reduceWalletAction(action: WalletScreenIntent.WalletAction) {
        val navigationEvent = when (action) {
            WalletScreenIntent.WalletAction.Receive -> WalletScreenNavigationEvent.ToReceive
            WalletScreenIntent.WalletAction.Send -> WalletScreenNavigationEvent.ToSend
            WalletScreenIntent.WalletAction.Vote -> WalletScreenNavigationEvent.ToVote
        }
        _state.update { it.copy(navigationEvent = triggered(navigationEvent)) }
    }

    private fun reduceBalance(balance: Wei) {
        _state.update {
            it.copy(
                error = null,
                isBalanceLoading = false,
                balanceFormatted = BalanceFormatter.formatAmountWithSymbol(
                    balance.toEther(), "ETH"
                )
            )
        }
    }

    private fun reduceError(errorType: ErrorType) {
        _state.update {
            it.copy(error = errorType.uiError)
        }
    }

    fun consumeNavigationEvent() {
        _state.update {
            it.copy(navigationEvent = consumed())
        }
    }
}