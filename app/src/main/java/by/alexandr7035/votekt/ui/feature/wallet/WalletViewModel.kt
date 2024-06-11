package by.alexandr7035.votekt.ui.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.votekt.domain.core.ErrorType
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.usecase.account.GetSelfAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.LogoutUseCase
import by.alexandr7035.votekt.domain.usecase.account.ObserveBalanceUseCase
import by.alexandr7035.votekt.domain.usecase.contract.GetContractStateUseCase
import by.alexandr7035.votekt.ui.feature.wallet.model.WalletScreenIntent
import by.alexandr7035.votekt.ui.feature.wallet.model.WalletScreenNavigationEvent
import by.alexandr7035.votekt.ui.feature.wallet.model.WalletScreenState
import by.alexandr7035.votekt.ui.uiError
import by.alexandr7035.votekt.ui.utils.BalanceFormatter
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
    private val getSelfAccountUseCase: GetSelfAccountUseCase,
    private val observeBalanceUseCase: ObserveBalanceUseCase,
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
            is WalletScreenIntent.ChangeHeaderVisibility -> reduceHeaderVisibility(intent.isVisible)
            is WalletScreenIntent.ExplorerUrlClick -> reduceExplorerClick(intent)
        }
    }

    private fun reduceHeaderVisibility(isVisible: Boolean) {
        _state.update {
            it.copy(isHeaderVisible = isVisible)
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
            val address = getSelfAccountUseCase.invoke()
            _state.update {
                it.copy(address = address)
            }
        }

        observeBalanceUseCase.invoke()
            .catch { error ->
                reduceError(ErrorType.fromThrowable(error))
            }
            .onEach {
                reduceBalance(it)
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
                    balance.toEther(),
                    "ETH"
                )
            )
        }
    }

    private fun reduceExplorerClick(intent: WalletScreenIntent.ExplorerUrlClick) {
        _state.update {
            it.copy(
                navigationEvent = triggered(
                    WalletScreenNavigationEvent.ToExplorer(
                        exploreType = intent.exploreType,
                        payload = intent.payload
                    )
                )
            )
        }
    }

    private fun reduceError(errorType: ErrorType) {
        // Connection error is displayed separately
        if (errorType != ErrorType.NODE_CONNECTION_ERROR) {
            _state.update {
                it.copy(error = errorType.uiError)
            }
        }
    }

    fun consumeNavigationEvent() {
        _state.update {
            it.copy(navigationEvent = consumed())
        }
    }
}
