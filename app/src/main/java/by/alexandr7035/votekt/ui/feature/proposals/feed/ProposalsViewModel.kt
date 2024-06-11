package by.alexandr7035.votekt.ui.feature.proposals.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.domain.core.ErrorType
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.usecase.contract.SyncWithContractUseCase
import by.alexandr7035.votekt.domain.usecase.proposal.ObserveProposalsUseCase
import by.alexandr7035.votekt.ui.uiError
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProposalsViewModel(
    private val observeProposalsUseCase: ObserveProposalsUseCase,
    private val syncWithContractUseCase: SyncWithContractUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProposalsScreenState())
    val state = _state.asStateFlow()

    fun onIntent(intent: ProposalsScreenIntent) {
        when (intent) {
            is ProposalsScreenIntent.ChangeControlsVisibility -> {
                _state.update {
                    it.copy(controlsAreVisible = intent.isVisible)
                }
            }

            ProposalsScreenIntent.EnterScreen -> {
                reduceEnterScreen()
            }

            is ProposalsScreenIntent.ExplorerUrlClick -> _state.update {
                it.copy(
                    navigationEvent = triggered(
                        ProposalsScreenNavigationEvent.ToExplorer(
                            intent.payload,
                            intent.exploreType
                        )
                    )
                )
            }

            is ProposalsScreenIntent.ProposalClick -> {
                _state.update {
                    it.copy(
                        navigationEvent = triggered(
                            ProposalsScreenNavigationEvent.ToProposal(intent.proposalId)
                        )
                    )
                }
            }

            ProposalsScreenIntent.AddProposalClick -> {
                _state.update {
                    it.copy(
                        navigationEvent = triggered(
                            ProposalsScreenNavigationEvent.ToAddProposal
                        )
                    )
                }
            }
        }
    }

    private fun reduceEnterScreen() {
        subscribeToProposals()

        viewModelScope.launch {
            val syncProposalsResult = OperationResult.runWrapped {
                syncWithContractUseCase.invoke()
            }

            when (syncProposalsResult) {
                is OperationResult.Success -> {}
                is OperationResult.Failure -> {
                    reduceError(ErrorType.fromThrowable(syncProposalsResult.error))
                }
            }
        }
    }

    fun subscribeToProposals() {
        observeProposalsUseCase
            .invoke()
            .onEach { proposals ->
                _state.update {
                    it.copy(
                        proposals = proposals,
                        error = null,
                        isLoading = false
                    )
                }
            }
            .catch { error ->
                reduceError(ErrorType.fromThrowable(error))
            }
            .launchIn(viewModelScope)
    }

    private fun reduceError(error: ErrorType) {
        _state.update {
            it.copy(
                error = error.uiError,
                isLoading = false
            )
        }
    }

    fun consumeNavigationEvent() {
        _state.update { it.copy(navigationEvent = consumed()) }
    }
}
