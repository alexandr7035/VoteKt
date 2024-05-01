package com.example.votekt.ui.feature_proposals.proposal_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.core.Uuid
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.ui.uiError
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VotingDetailsViewModel(
    private val votingContractRepository: VotingContractRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        ProposalDetailsScreenState(
            isProposalLoading = true
        )
    )

    val state = _state.asStateFlow()

    fun onIntent(intent: ProposalDetailsScreenIntent) {
        when (intent) {
            is ProposalDetailsScreenIntent.EnterScreen -> loadProposalById(intent.proposalUuid)
            is ProposalDetailsScreenIntent.ErrorRetryClick -> loadProposalById(intent.proposalUuid)
            is ProposalDetailsScreenIntent.ExplorerUrlClick -> _state.update {
                it.copy(navigationEvent = triggered(
                    ProposalDetailsScreenNavigationEvent.ToExplorer(intent.payload, intent.exploreType)
                ))
            }

            is ProposalDetailsScreenIntent.GoBack -> _state.update {
                it.copy(navigationEvent = triggered(
                    ProposalDetailsScreenNavigationEvent.PopupBack
                ))
            }

            is ProposalDetailsScreenIntent.MakeVoteClick -> onMakeVoteClick(intent.proposalNumber, intent.voteType)
            is ProposalDetailsScreenIntent.DeployClick -> onDeployClick(intent.proposalUuid)
            is ProposalDetailsScreenIntent.DeleteClick -> onDeleteDraftClick(intent.proposalUuid)
        }
    }

    fun loadProposalById(id: String) {
        val deploymentFee = runCatching {
            votingContractRepository.getContractConfiguration().createProposalFee
        }.getOrNull()

        _state.update { it.copy(proposalDeploymentFee = deploymentFee) }

        votingContractRepository
            .getProposalById(id)
            .catch { error ->
                _state.update { curr ->
                    curr.copy(
                        isProposalLoading = false,
                        error = ErrorType.fromThrowable(error).uiError
                    )
                }
            }
            .onEach { proposal ->
                _state.update {
                    it.copy(
                        proposal = proposal,
                        isProposalLoading = false,
                        error = null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onMakeVoteClick(proposalNumber: Int, voteType: VoteType) {
        viewModelScope.launch {
            when (val res = votingContractRepository.voteOnProposal(proposalNumber, voteType)) {
                is OperationResult.Success -> {}
                is OperationResult.Failure -> {
                    _state.update {
                        it.copy(error = res.error.errorType.uiError)
                    }
                }
            }
        }
    }

    private fun onDeployClick(proposalUuid: Uuid) {
        viewModelScope.launch {
            when (val res = votingContractRepository.deployDraftProposal(proposalUuid)) {
                is OperationResult.Success -> {}
                is OperationResult.Failure -> {
                    _state.update {
                        it.copy(error = res.error.errorType.uiError)
                    }
                }
            }
        }
    }

    private fun onDeleteDraftClick(proposalUuid: Uuid) {
        viewModelScope.launch {
            when (val res = votingContractRepository.deleteDraftProposal(proposalUuid)) {
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(
                            navigationEvent = triggered(
                                ProposalDetailsScreenNavigationEvent.PopupBack
                            ),
                        )
                    }
                }
                is OperationResult.Failure -> {
                    _state.update {
                        it.copy(error = res.error.errorType.uiError)
                    }
                }
            }
        }
    }

    fun consumeNavigationEvent() {
        _state.update {
            it.copy(navigationEvent = consumed())
        }
    }
}
