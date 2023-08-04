package com.example.votekt.ui.votings_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.OperationResult
import com.example.votekt.data.Web3Repository
import com.example.votekt.data.model.CreateProposalReq
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateProposalViewModel(private val web3Repository: Web3Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CreateProposalScreenState()
    )

    val uiState = _uiState.asStateFlow()

    fun createProposal(data: CreateProposalReq) {

        viewModelScope.launch {
            when (web3Repository.createProposal(data)) {
                is OperationResult.Success -> {
                    _uiState.update { prev ->
                        prev.copy(isCreateCompleted = triggered(true))
                    }
                }

                is OperationResult.Failure -> {
                    _uiState.update { prev ->
                        prev.copy(isCreateCompleted = triggered(false))
                    }
                }
            }
        }
    }

    fun onProposalCreatedEvent() {
        _uiState.update { prev ->
            prev.copy(isCreateCompleted = consumed())
        }
    }
}