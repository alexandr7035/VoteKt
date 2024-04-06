package com.example.votekt.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum.model.eth_events.EthEventsSubscriptionState
import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.ReviewTransactionData
import com.example.votekt.domain.votings.VotingContractRepository
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
    private val sendTransactionRepository: SendTransactionRepository,
    private val web3EventsRepository: EthereumEventListener,
    private val votingContractRepository: VotingContractRepository
) : ViewModel() {
    private val _appState: MutableStateFlow<AppState> = MutableStateFlow(AppState.Loading)
    val appState = _appState.asStateFlow()

    // This is a global app's viewModel
    init {
        onAppIntent(AppIntent.EnterApp)
    }

    fun onAppIntent(intent: AppIntent) {
        when (intent) {
            AppIntent.EnterApp -> reduceEnterApp()
            AppIntent.ReconnectToNode -> reduceReconnectToNode()
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
            val shouldCreateAccount = accountRepository.isAccountPresent().not()

            if (shouldCreateAccount) {
                println("Create account")
                _appState.update {
                    AppState.Ready(
                        conditionalNavigation = ConditionalNavigation(
                            requireCreateAccount = true
                        )
                    )
                }
            } else {
                subscribeToEthereumNode()
                subscribeToTransactionConfirmationRequests()

                web3EventsRepository
                    .subscriptionStateFlow()
                    .onEach {
                        println("NODE_CONNECTION state changed ${it}")
                        when (it) {
                            EthEventsSubscriptionState.Connecting -> {
                                _appState.update {
                                    AppState.Loading
                                }
                            }
                            EthEventsSubscriptionState.Connected -> {
                                _appState.update {
                                    AppState.Ready(
                                        conditionalNavigation = ConditionalNavigation(
                                            requireCreateAccount = false,
                                        )
                                    )
                                }
                            }
                            EthEventsSubscriptionState.Disconnected -> {
                                _appState.update { AppState.NodeConnectionError }
                            }
                        }
                    }
                    .launchIn(viewModelScope)
            }
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
            .onEach {
                if (it is EthereumEvent.ContractEvent) {
                    votingContractRepository.handleContractEvent(it)
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
        val currentAppState = _appState.value
        if (currentAppState !is AppState.Ready) return

        _appState.update {
            currentAppState.copy(txConfirmationState = reviewTransactionData?.mapToUi())
        }
    }
}