package com.example.votekt.ui.feature_proposals.proposals_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.datasync.SyncWithContractUseCase
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.ui.uiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProposalsViewModel(
    private val votingContractRepository: VotingContractRepository,
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
        votingContractRepository
            .getProposals()
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
}