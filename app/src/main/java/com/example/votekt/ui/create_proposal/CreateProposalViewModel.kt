package com.example.votekt.ui.create_proposal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.domain.votings.CreateProposal
import com.example.votekt.ui.uiError
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateProposalViewModel(private val votingContractRepository: VotingContractRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CreateProposalScreenState()
    )

    val uiState = _uiState.asStateFlow()

    fun createProposal(data: CreateProposal) {

        viewModelScope.launch {
            _uiState.update { prev ->
                prev.copy(isLoading = true)
            }

            val res = votingContractRepository.createProposal(data)

            when (res) {
                is OperationResult.Success -> {
                    _uiState.update { prev ->
                        prev.copy(
                            submitProposalEvent = triggered(
                                CreateProposalResult(
                                    error = null,
                                    proposalUuid = res.data,
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
                                CreateProposalResult(
                                    error = res.error,
                                    proposalUuid = null,
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