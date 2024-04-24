package com.example.votekt.ui.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum.model.eth_events.EthEventsSubscriptionState
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.security.CheckAppLockUseCase
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.ReviewTransactionData
import com.example.votekt.domain.usecase.node_connection.ConnectToNodeUseCase
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionIntent
import com.example.votekt.ui.feature_confirm_transaction.mapToUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val accountRepository: AccountRepository,
    private val connectToNodeUseCase: ConnectToNodeUseCase,
    private val sendTransactionRepository: SendTransactionRepository,
    private val web3EventsRepository: EthereumEventListener,
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
            subscribeToNodeConnectionState()
        }
    }

    private fun reduceReconnectToNode() {
        connectToNodeUseCase.invoke()
    }

    private fun subscribeToNodeConnectionState() {
        web3EventsRepository
            .subscriptionStateFlow()
            .onEach { nodeConnectionState ->
                _appState.update {
                    it.copy(
                        appConnectionState = nodeConnectionState.mapNodeConnectionState()
                    )
                }

                Log.d(APP_TAG, "connection change: ${_appState.value.appConnectionState}")
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

    private fun EthEventsSubscriptionState.mapNodeConnectionState(): AppConnectionState {
        return when (this) {
            EthEventsSubscriptionState.Connected -> AppConnectionState.ONLINE
            EthEventsSubscriptionState.Connecting -> AppConnectionState.CONNECTING
            EthEventsSubscriptionState.Disconnected -> AppConnectionState.OFFLINE
        }
    }

    private companion object {
        const val APP_TAG = "APP_TAG"
    }
}