package com.example.votekt.ui.voting_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.data.VotingRepository
import com.example.votekt.data.model.Proposal
import com.example.votekt.ui.core.ScreenState
import com.example.votekt.ui.create_proposal.SubmitTransactionResult
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VotingDetailsViewModel(
    private val votingRepository: VotingRepository
) : ViewModel() {

    private val _proposalUi = MutableStateFlow<ScreenState<Proposal>>(
        ScreenState(
            data = null,
            isLoading = true,
            error = null
        )
    )
    val proposalUi = _proposalUi.asStateFlow()

    private val _voteActionState = MutableStateFlow(
        VoteActionState()
    )

    val voteActionState = _voteActionState.asStateFlow()

    fun loadProposalById(id: Long) {
        viewModelScope.launch {
            _proposalUi.update { curr ->
                curr.copy(
                    isLoading = true
                )
            }

            when (val res = votingRepository.getProposalById(id)) {
                is OperationResult.Success -> {
                    _proposalUi.update { curr ->
                        curr.copy(
                            data = res.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is OperationResult.Failure -> {
                    _proposalUi.update { curr ->
                        curr.copy(
                            isLoading = false,
                            error = res.error
                        )
                    }
                }
            }
        }
    }


    fun makeVote(proposalId: Long, isFor: Boolean) {

        viewModelScope.launch {
            _voteActionState.update { prev ->
                prev.copy(isLoading = true)
            }

            when (val res = votingRepository.voteOnProposal(proposalId, isFor)) {
                is OperationResult.Success -> {
                    _voteActionState.update { prev ->
                        prev.copy(
                            voteTxSubmittedEvent = triggered(
                                SubmitTransactionResult(
                                    isTransactionSubmitted = true,
                                    transactionHash = res.data,
                                    error = null
                                )
                            ),
                            isLoading = false,
                        )
                    }
                }

                is OperationResult.Failure -> {
                    _voteActionState.update { prev ->
                        prev.copy(
                            voteTxSubmittedEvent = triggered(
                                SubmitTransactionResult(
                                    isTransactionSubmitted = false,
                                    transactionHash = null,
                                    error = res.error
                                )
                            ),
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    fun onVoteCreatedEvent() {
        _voteActionState.update { prev ->
            prev.copy(
                voteTxSubmittedEvent = consumed(),
            )
        }
    }
}