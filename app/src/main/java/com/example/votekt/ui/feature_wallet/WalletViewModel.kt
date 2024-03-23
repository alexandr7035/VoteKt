package com.example.votekt.ui.feature_wallet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.ui.utils.BalanceFormatter
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.core.ErrorType
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
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _state = MutableStateFlow(WalletScreenState())
    val state = _state.asStateFlow()

    fun onWalletIntent(intent: WalletScreenIntent) {
        when (intent) {
            is WalletScreenIntent.LoadData -> subscribeToBalance()
            is WalletScreenIntent.WalletAction -> reduceWalletAction(intent)
        }
    }

    // Consider using offline-first approach
    // Without full-screen errors
    private fun subscribeToBalance() {
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
            .catch {
                Log.d("DEBUG_TAG", "catch ${it}")
                if (ErrorType.fromThrowable(it) == ErrorType.NODE_CONNECTION_ERROR) {
                    reduceNoConnection()
                }
            }
            .onCompletion { error ->
                Log.d("DEBUG_TAG", "balance flow completes! ${error}")
                error?.let {
                    if (ErrorType.fromThrowable(it) == ErrorType.NODE_CONNECTION_ERROR) {
                        reduceNoConnection()
                    } else {
                        reduceError(ErrorType.fromThrowable(error))
                    }
                }
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
                noConnection = false,
                isBalanceLoading = false,
                balanceFormatted = BalanceFormatter.formatAmountWithSymbol(
                    balance.toEther(), "ETH"
                )
            )
        }
    }

    private fun reduceNoConnection() {
        _state.update {
            it.copy(noConnection = true)
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