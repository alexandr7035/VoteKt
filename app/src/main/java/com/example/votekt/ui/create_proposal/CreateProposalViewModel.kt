package com.example.votekt.ui.create_proposal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.votings.VotingRepository
import com.example.votekt.domain.votings.CreateProposal
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateProposalViewModel(private val votingRepository: VotingRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CreateProposalScreenState()
    )

    val uiState = _uiState.asStateFlow()

    fun createProposal(data: CreateProposal) {

        viewModelScope.launch {
            _uiState.update { prev ->
                prev.copy(isLoading = true)
            }

            val res = votingRepository.createProposal(data)

            when (res) {
                is OperationResult.Success -> {
                    _uiState.update { prev ->
                        prev.copy(
                            submitProposalEvent = triggered(
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
                    _uiState.update { prev ->
                        prev.copy(
                            submitProposalEvent = triggered(
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

    fun onProposalCreatedEvent() {
        _uiState.update { prev ->
            prev.copy(submitProposalEvent = consumed())
        }
    }
}