package com.example.votekt.ui.voting_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.votings.VotingRepository
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.votings.VoteType
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VotingDetailsViewModel(
    private val votingRepository: VotingRepository
) : ViewModel() {

    private val _screenUi = MutableStateFlow(
        ProposalDetailsScreenState(
            isProposalLoading = true
        )
    )

    val screenUi = _screenUi.asStateFlow()

    fun loadProposalById(id: Long) {
        viewModelScope.launch {
            _screenUi.update { curr ->
                curr.copy(
                    isProposalLoading = true
                )
            }

            when (val res = votingRepository.getProposalById(id)) {
                is OperationResult.Success -> {
                    _screenUi.update { curr ->
                        curr.copy(
                            proposal = res.data,
                            isProposalLoading = false,
                            error = null
                        )
                    }
                }

                is OperationResult.Failure -> {
                    _screenUi.update { curr ->
                        curr.copy(
                            isProposalLoading = false,
                            error = res.error
                        )
                    }
                }
            }
        }
    }


    fun makeVote(proposalId: Long, voteType: VoteType) {
        _screenUi.update {
            it.copy(
                isSelfVoteProcessing = true
            )
        }

        viewModelScope.launch {
            when (val res = votingRepository.voteOnProposal(proposalId, voteType)) {
                is OperationResult.Success -> {
                    _screenUi.update {
                        it.copy(
                            isSelfVoteProcessing = false,
                            selfVoteSubmittedEvent = triggered(res.data)
                        )
                    }
                }

                is OperationResult.Failure -> {
                    _screenUi.update {
                        it.copy(
                            error = res.error
                        )
                    }
                }
            }
        }
    }

    fun onVoteSubmittedEvent() {
        _screenUi.update {
            it.copy(
                selfVoteSubmittedEvent = consumed()
            )
        }
    }
}