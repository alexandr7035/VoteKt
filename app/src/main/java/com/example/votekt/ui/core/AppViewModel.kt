package com.example.votekt.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.transactions.ConfirmTransactionState
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.isContractInteraction
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionData
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionIntent
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionState
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
        emitIntent(AppIntent.EnterApp)
    }

    private fun emitIntent(intent: AppIntent) {
        when (intent) {
            AppIntent.EnterApp -> {
                reduceCheckAccount()
            }
        }
    }

    fun onTransactionIntent(intent: ReviewTransactionIntent) {
        when (intent) {
            ReviewTransactionIntent.LoadTransactionData -> {

            }
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


    private fun reduceCheckAccount() {
        viewModelScope.launch {
            val shouldCreateAccount = accountRepository.isAccountPresent().not()

            _appState.update {
                AppState.Ready(
                    conditionalNavigation = ConditionalNavigation(
                        requireCreateAccount = shouldCreateAccount
                    )
                )
            }

            sendTransactionRepository
                .state
                .onEach {
                    reduceConfirmTransactionState(it)
                }
                .launchIn(viewModelScope)

            if (shouldCreateAccount.not()) {
                web3EventsRepository
                    .subscribe()
                    .onEach {
                        if (it is EthereumEvent.ContractEvent) {
                            votingContractRepository.handleContractEvent(it)
                        }
                    }
                    .launchIn(viewModelScope)
            }
        }
    }

    private fun reduceConfirmTransactionState(state: ConfirmTransactionState) {
        val currentAppState = _appState.value
        if (currentAppState !is AppState.Ready) return

        val uiState  = when (state) {
                is ConfirmTransactionState.Idle -> ReviewTransactionState(data = null)
                is ConfirmTransactionState.TxReview -> {
                    ReviewTransactionState(data = reduceTxReviewState(state))
                }
        }

        _appState.update {
            currentAppState.copy(txConfirmationState = uiState)
        }
    }

    private fun reduceTxReviewState(
        state: ConfirmTransactionState.TxReview
    ): ReviewTransactionData {
        val data = when {
            state.transactionType.isContractInteraction() -> {
                ReviewTransactionData.ContractInteraction(
                    recipient = state.to,
                    contractInput = state.input!!,
                    minerTipFee = state.minerTipFee,
                    totalEstimatedFee = state.totalEstimatedFee,
                    transactionType = state.transactionType,
                    isBalanceSufficient = state.isSufficientBalance,
                )
            }

            else -> {
                ReviewTransactionData.SendAmount(
                    recipient = state.to,
                    value = state.value!!,
                    minerTipFee = state.minerTipFee,
                    totalEstimatedFee = state.totalEstimatedFee,
                    transactionType = state.transactionType,
                    isBalanceSufficient = state.isSufficientBalance,
                )
            }
        }

        return data
    }
}