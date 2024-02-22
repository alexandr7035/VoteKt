package com.example.votekt.ui.feature_wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _state = MutableStateFlow(WalletScreenState())
    val state = _state.asStateFlow()

    init {
        subscribeToBalance()
    }

    fun onWalletIntent(intent: WalletScreenIntent) {
        when (intent) {
            is WalletScreenIntent.LoadData -> {}
            is WalletScreenIntent.WalletAction -> reduceWalletAction(intent)
        }
    }

    private fun subscribeToBalance() {
        viewModelScope.launch {
            val address = accountRepository.getSelfAddress()
            _state.update {
                it.copy(address = address)
            }
        }

        accountRepository
            .getAccountBalance()
            .onEach {
                reduceBalance(it)
            }
            .catch {
                reduceError(ErrorType.fromThrowable(it))
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