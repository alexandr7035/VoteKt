package com.example.votekt.ui.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum.model.eth_events.EthEventsSubscriptionState
import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.datasync.SyncWithContractUseCase
import com.example.votekt.domain.security.CheckAppLockUseCase
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.ReviewTransactionData
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionIntent
import com.example.votekt.ui.feature_confirm_transaction.mapToUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val accountRepository: AccountRepository,
    private val sendTransactionRepository: SendTransactionRepository,
    private val web3EventsRepository: EthereumEventListener,
    private val votingContractRepository: VotingContractRepository,
    private val syncWithContractUseCase: SyncWithContractUseCase,
    private val checkAppLockUseCase: CheckAppLockUseCase,
) : ViewModel() {
    private val _appState: MutableStateFlow<AppState> = MutableStateFlow(
        AppState()
    )
    val appState = _appState.asStateFlow()

    // This is a global app's viewModel
    init {
        onAppIntent(AppIntent.EnterApp)
    }

    fun onAppIntent(intent: AppIntent) {
        when (intent) {
            AppIntent.EnterApp -> reduceEnterApp()
            AppIntent.ReconnectToNode -> reduceReconnectToNode()
            AppIntent.ConsumeAppUnlocked -> reduceAppUnlocked()
        }
    }

    private fun reduceAppUnlocked() {
        println("STATE_TAG app unlocked ${_appState.value}")
        _appState.update {
            it.copy(conditionalNavigation = it.conditionalNavigation.copy(
                requireUnlockApp = false
            ))
        }
    }

    fun onTransactionIntent(intent: ReviewTransactionIntent) {
        when (intent) {
            ReviewTransactionIntent.SubmitTransaction -> {
                viewModelScope.launch {
                    sendTransactionRepository.confirmTransaction()
                }
            }

            ReviewTransactionIntent.DismissDialog -> {
                viewModelScope.launch {
                    sendTransactionRepository.cancelTransaction()
                }
            }
        }
    }


    private fun reduceEnterApp() {
        viewModelScope.launch {
            _appState.update {
                it.copy(isLoading = true)
            }

            val isAccountCreated = accountRepository.isAccountPresent()
            val isAppLocked = checkAppLockUseCase.invoke()
            val shouldSetupAppLock = !isAppLocked && isAccountCreated

            if (!isAccountCreated) {
                _appState.update {
                    it.copy(
                        isLoading = false,
                        conditionalNavigation = ConditionalNavigation(
                            requireCreateAccount = true,
                            requireUnlockApp = false,
                            requireCreateAppLock = false,
                        )
                    )
                }
            } else {
                _appState.update {
                    it.copy(
                        isLoading = false,
                        conditionalNavigation = ConditionalNavigation(
                            requireCreateAccount = false,
                            requireUnlockApp = isAppLocked,
                            requireCreateAppLock = !isAppLocked && shouldSetupAppLock
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            subscribeToTransactionConfirmationRequests()
            subscribeToEthereumNode()
        }
    }

    private fun reduceReconnectToNode() {
        viewModelScope.launch {
            subscribeToEthereumNode()
        }
    }

    private suspend fun subscribeToEthereumNode() {
        web3EventsRepository
            .subscribeToEthereumEvents()
            .onStart {
                val syncResult = OperationResult.runWrapped {
                    syncWithContractUseCase.invoke()
                }

                when (syncResult) {
                    is OperationResult.Success -> Log.d(APP_TAG, "data synced with contract")
                    is OperationResult.Failure -> throw syncResult.error
                }
            }
            .catch {
                _appState.update {
                    it.copy(nodeConnection = it.nodeConnection.copy(
                        isLoading = false,
                        isConnected = false,
                    ))
                }
            }
            .onEach {
                if (it is EthereumEvent.ContractEvent) {
                    votingContractRepository.handleContractEvent(it)
                }
            }
            .launchIn(viewModelScope)

        web3EventsRepository
            .subscriptionStateFlow()
            .onEach { nodeConnectionState ->
                println("NODE_CONNECTION state changed ${nodeConnectionState}")
                when (nodeConnectionState) {
                    EthEventsSubscriptionState.Connecting -> {
                        _appState.update {
                            it.copy(
                                appConnectionState = AppConnectionState.CONNECTING
                            )
                        }
                    }

                    EthEventsSubscriptionState.Connected -> {
                        _appState.update {
                            it.copy(
                                appConnectionState = AppConnectionState.ONLINE
                            )
                        }
                    }

                    EthEventsSubscriptionState.Disconnected -> {
                        _appState.update {
                            it.copy(
                                appConnectionState = AppConnectionState.OFFLINE
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun subscribeToTransactionConfirmationRequests() {
        sendTransactionRepository
            .state
            .onEach {
                reduceConfirmTransactionState(it)
            }
            .launchIn(viewModelScope)
    }

    private fun reduceConfirmTransactionState(reviewTransactionData: ReviewTransactionData?) {
        _appState.update {
            it.copy(txConfirmationState = reviewTransactionData?.mapToUi())
        }
    }

    private companion object {
        const val APP_TAG = "APP_TAG"
    }
}