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

    fun loadProposalById(id: String) {
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

    fun makeVote(proposalNumber: Int, voteType: VoteType) {
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

    fun deployProposal(proposalUuid: Uuid) {
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

    fun deleteDraft(proposalUuid: Uuid) {
        println("call delete draft ${proposalUuid}")
        viewModelScope.launch {
            when (val res = votingContractRepository.deleteDraftProposal(proposalUuid)) {
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(
                            draftDeletedEvent = triggered
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

    fun consumeDraftDeletedEvent() {
        _state.update {
            it.copy(
                draftDeletedEvent = consumed
            )
        }
    }
}