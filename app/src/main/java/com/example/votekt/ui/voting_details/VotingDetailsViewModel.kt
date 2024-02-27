package com.example.votekt.ui.voting_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.votings.VotingRepository
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.votings.VoteType
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
    private val votingRepository: VotingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        ProposalDetailsScreenState(
            isProposalLoading = true
        )
    )

    val state = _state.asStateFlow()

    fun loadProposalById(id: Int) {
        votingRepository
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

    // TODO FIX ID
    fun makeVote(proposalId: Int, voteType: VoteType) {
        _state.update {
            it.copy(
                isSelfVoteProcessing = true
            )
        }

        viewModelScope.launch {
            when (val res = votingRepository.voteOnProposal(proposalId.toLong(), voteType)) {
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(
                            isSelfVoteProcessing = false,
                            selfVoteSubmittedEvent = triggered(res.data)
                        )
                    }
                }

                is OperationResult.Failure -> {
                    _state.update {
                        it.copy(
                            error = res.error.errorType.uiError
                        )
                    }
                }
            }
        }
    }

    fun onVoteSubmittedEvent() {
        _state.update {
            it.copy(
                selfVoteSubmittedEvent = consumed()
            )
        }
    }
}