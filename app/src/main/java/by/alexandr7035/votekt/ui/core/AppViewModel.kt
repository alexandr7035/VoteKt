package by.alexandr7035.votekt.ui.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum.model.events.EthEventsSubscriptionState
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.model.explorer.ExploreType
import by.alexandr7035.votekt.domain.model.transactions.ReviewTransactionData
import by.alexandr7035.votekt.domain.usecase.account.CheckAccountCreatedUseCase
import by.alexandr7035.votekt.domain.usecase.applock.CheckAppLockUseCase
import by.alexandr7035.votekt.domain.usecase.applock.CheckAppLockedWithBiometricsUseCase
import by.alexandr7035.votekt.domain.usecase.explorer.GetBlockchainExplorerUrlUseCase
import by.alexandr7035.votekt.domain.usecase.node.ConnectToNodeUseCase
import by.alexandr7035.votekt.domain.usecase.transactions.ConfirmOutgoingTransactionUseCase
import by.alexandr7035.votekt.domain.usecase.transactions.ObserveOutgoingTransactionUseCase
import by.alexandr7035.votekt.ui.core.resources.UiText
import by.alexandr7035.votekt.ui.feature.transactions.review.ReviewTransactionIntent
import by.alexandr7035.votekt.ui.feature.transactions.review.mapToUi
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// TODO fix suppress
@Suppress("TooManyFunctions")
class AppViewModel(
    private val checkAccountCreatedUseCase: CheckAccountCreatedUseCase,
    private val connectToNodeUseCase: ConnectToNodeUseCase,
    private val web3EventsRepository: EthereumEventListener,
    private val checkAppLockUseCase: CheckAppLockUseCase,
    private val checkAppLockedWithBiometricsUseCase: CheckAppLockedWithBiometricsUseCase,
    private val getBlockchainExplorerUrlUseCase: GetBlockchainExplorerUrlUseCase,
    private val observeOutgoingTransactionUseCase: ObserveOutgoingTransactionUseCase,
    private val cancelCurrentTransactionUseCase: ObserveOutgoingTransactionUseCase,
    private val confirmOutgoingTransactionUseCase: ConfirmOutgoingTransactionUseCase,
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
            is ReviewTransactionIntent.SubmitTransactionClick -> {
                onSubmitTransactionClick()
            }

            is ReviewTransactionIntent.BiometricAuthUnlock -> {
                if (intent.isSuccessful) {
                    submitTransaction()
                } else {
                    _appState.update {
                        it.copy(
                            txConfirmationState = it.txConfirmationState?.copy(
                                error = UiText.StringResource(R.string.authentication_is_required)
                            )
                        )
                    }
                }
            }

            is ReviewTransactionIntent.DismissDialog -> {
                cancelTransaction()
            }

            is ReviewTransactionIntent.ExplorerUrlClick -> onOpenExplorer(intent.payload, intent.exploreType)
        }
    }

    private fun cancelTransaction() {
        viewModelScope.launch {
            cancelCurrentTransactionUseCase.invoke()
        }
    }

    private fun reduceEnterApp() {
        viewModelScope.launch {
            _appState.update {
                it.copy(isLoading = true)
            }

            val isAccountCreated = checkAccountCreatedUseCase.invoke()
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
        observeOutgoingTransactionUseCase
            .invoke()
            .onEach {
                reduceConfirmTransactionState(it)
            }
            .launchIn(viewModelScope)
    }

    private fun reduceConfirmTransactionState(reviewTransactionData: ReviewTransactionData?) {
        _appState.update {
            it.copy(
                txConfirmationState = reviewTransactionData?.mapToUi()
            )
        }
    }

    private fun onSubmitTransactionClick() {
        val isAppLocked = checkAppLockedWithBiometricsUseCase.invoke()

        if (!isAppLocked) {
            submitTransaction()
        } else {
            _appState.update {
                it.copy(
                    txConfirmationState = it.txConfirmationState?.copy(
                        showBiometricConfirmationEvent = triggered
                    )
                )
            }
        }
    }

    private fun submitTransaction() {
        viewModelScope.launch {
            val confirmTx = OperationResult.runWrapped {
                confirmOutgoingTransactionUseCase.invoke()
            }

            when (confirmTx) {
                is OperationResult.Failure -> {}
                is OperationResult.Success -> {}
            }
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
            println("EXPLORER_TAG open $res")

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

    fun consumeBiometricTransactionConfirmationEvent() {
        _appState.update {
            it.copy(
                txConfirmationState = it.txConfirmationState?.copy(
                    showBiometricConfirmationEvent = consumed
                )
            )
        }
    }

    private companion object {
        const val APP_TAG = "APP_TAG"
    }
}
