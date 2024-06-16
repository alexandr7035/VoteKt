package by.alexandr7035.votekt.ui.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.domain.core.ErrorType
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.usecase.account.GetSelfAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.LogoutUseCase
import by.alexandr7035.votekt.domain.usecase.account.ObserveBalanceUseCase
import by.alexandr7035.votekt.domain.usecase.account.RefreshBalanceUseCase
import by.alexandr7035.votekt.domain.usecase.contract.ObserveContractStateUseCase
import by.alexandr7035.votekt.domain.usecase.contract.SyncWithContractUseCase
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
    private val refreshBalanceUseCase: RefreshBalanceUseCase,
    private val observeBalanceUseCase: ObserveBalanceUseCase,
    private val observeContractStateUseCase: ObserveContractStateUseCase,
    private val syncWithContractUseCase: SyncWithContractUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(WalletScreenState())
    val state = _state.asStateFlow()

    fun onWalletIntent(intent: WalletScreenIntent) {
        when (intent) {
            is WalletScreenIntent.EnterScreen -> onEnterScreen()
            is WalletScreenIntent.ResumeScreen -> onResumeScreen()
            is WalletScreenIntent.WalletAction -> onWalletAction(intent)
            is WalletScreenIntent.LogOut -> onLogOutClick()
            is WalletScreenIntent.ChangeHeaderVisibility -> onChangeHeaderVisibility(intent.isVisible)
            is WalletScreenIntent.ExplorerUrlClick -> onOpenExplorerClick(intent)
            is WalletScreenIntent.RefreshBalance -> refreshBalance()
        }
    }

    private fun onEnterScreen() {
        loadSelfAccount()
        syncWithContract()

        observeBalanceUseCase.invoke()
            .catch { error ->
                reduceError(ErrorType.fromThrowable(error))
            }
            .onEach { balance ->
                _state.update {
                    it.copy(
                        balanceState = it.balanceState.copy(
                            balanceFormatted = BalanceFormatter.formatAmountWithSymbol(
                                balance.toEther(),
                                "ETH"
                            ),
                        ),
                    )
                }
            }
            .launchIn(viewModelScope)

        observeContractStateUseCase.invoke()
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

    private fun onResumeScreen() {
        refreshBalance()
    }

    private fun onChangeHeaderVisibility(isVisible: Boolean) {
        _state.update {
            it.copy(isHeaderVisible = isVisible)
        }
    }

    private fun onLogOutClick() {
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

    private fun syncWithContract() {
        viewModelScope.launch {
            val res = OperationResult.runWrapped {
                syncWithContractUseCase.invoke()
            }

            when (res) {
                is OperationResult.Failure -> {
                    reduceError(errorType = res.error.errorType)
                }
                is OperationResult.Success -> {}
            }
        }
    }

    private fun refreshBalance() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    balanceState = it.balanceState.copy(
                        isBalanceLoading = true
                    )
                )
            }

            val res = OperationResult.runWrapped {
                refreshBalanceUseCase.invoke()
            }

            when (res) {
                is OperationResult.Failure -> {
                    _state.update {
                        it.copy(
                            balanceState = it.balanceState.copy(
                                isBalanceLoading = false,
                                isBalanceError = true,
                            )
                        )
                    }
                }
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(
                            balanceState = it.balanceState.copy(
                                isBalanceLoading = false
                            )
                        )
                    }
                }
            }

            println("refreshed balance ${_state.value.balanceState}")
        }
    }

    private fun loadSelfAccount() {
        viewModelScope.launch {
            val res = OperationResult.runWrapped {
                getSelfAccountUseCase.invoke()
            }

            when (res) {
                is OperationResult.Failure -> {
                    reduceError(res.error.errorType)
                }
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(address = res.data)
                    }
                }
            }
        }
    }

    private fun onWalletAction(action: WalletScreenIntent.WalletAction) {
        val navigationEvent = when (action) {
            WalletScreenIntent.WalletAction.Receive -> WalletScreenNavigationEvent.ToReceive
            WalletScreenIntent.WalletAction.Send -> WalletScreenNavigationEvent.ToSend
            WalletScreenIntent.WalletAction.Vote -> WalletScreenNavigationEvent.ToVote
        }
        _state.update { it.copy(navigationEvent = triggered(navigationEvent)) }
    }

    private fun onOpenExplorerClick(intent: WalletScreenIntent.ExplorerUrlClick) {
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
