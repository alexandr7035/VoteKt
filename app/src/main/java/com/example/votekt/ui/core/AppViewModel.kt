package com.example.votekt.ui.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum.model.eth_events.EthEventsSubscriptionState
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.model.blockchain_explorer.ExploreType
import com.example.votekt.domain.security.CheckAppLockUseCase
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.ReviewTransactionData
import com.example.votekt.domain.usecase.blockchain_explorer.GetBlockchainExplorerUrlUseCase
import com.example.votekt.domain.usecase.node_connection.ConnectToNodeUseCase
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionIntent
import com.example.votekt.ui.feature_confirm_transaction.mapToUi
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
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
    private val getBlockchainExplorerUrlUseCase: GetBlockchainExplorerUrlUseCase,
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
            is AppIntent.EnterApp -> reduceEnterApp()
            is AppIntent.ReconnectToNode -> reduceReconnectToNode()
            is AppIntent.ConsumeAppUnlocked -> reduceAppUnlocked()
            is AppIntent.OpenBlockExplorer -> onOpenExplorer(intent.payload, intent.exploreType)
        }
    }

    private fun reduceAppUnlocked() {
        println("STATE_TAG app unlocked ${_appState.value}")
        _appState.update {
            it.copy(
                conditionalNavigation = it.conditionalNavigation.copy(
                    requireUnlockApp = false
                )
            )
        }
    }

    fun onTransactionIntent(intent: ReviewTransactionIntent) {
        when (intent) {
            is ReviewTransactionIntent.SubmitTransaction -> {
                viewModelScope.launch {
                    val confirmTx = OperationResult.runWrapped {
                        sendTransactionRepository.confirmTransaction()
                    }

                    when (confirmTx) {
                        is OperationResult.Failure -> {}
                        is OperationResult.Success -> {}
                    }
                }
            }

            is ReviewTransactionIntent.DismissDialog -> {
                viewModelScope.launch {
                    sendTransactionRepository.cancelTransaction()
                }
            }

            is ReviewTransactionIntent.ExplorerUrlClick -> onOpenExplorer(intent.payload, intent.exploreType)
        }
    }


    private fun reduceEnterApp() {
        viewModelScope.launch {
            _appState.update {
                it.copy(isLoading = true)
            }

            val isAccountCreated = accountRepository.isAccountPresent()
            val isAppLocked = false
            val shouldSetupAppLock = false && isAccountCreated

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

    private fun onOpenExplorer(
        payload: String,
        exploreType: ExploreType,
    ) {
        viewModelScope.launch {
            val res = getBlockchainExplorerUrlUseCase.invoke(
                value = payload,
                exploreType = exploreType,
            )
            println("EXPLORER_TAG open ${res}")

            res?.let { url ->
                _appState.update {
                    it.copy(
                        openExplorerEvent = triggered(url)
                    )
                }
            }
        }
    }

    fun consumeOpenExplorerEvent() {
        _appState.update { it.copy(openExplorerEvent = consumed()) }
    }

    private companion object {
        const val APP_TAG = "APP_TAG"
    }
}