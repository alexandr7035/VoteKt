package com.example.votekt.ui.votings_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.OperationResult
import com.example.votekt.data.Web3Repository
import com.example.votekt.data.model.Proposal
import com.example.votekt.ui.core.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProposalsViewModel(private val web3Repository: Web3Repository) : ViewModel() {
    private val _proposalsUi = MutableStateFlow<ScreenState<List<Proposal>>>(
        ScreenState(
            data = null,
            isLoading = true,
            error = null
        )
    )

    val proposalsUi = _proposalsUi.asStateFlow()

    fun loadProposals() {
        viewModelScope.launch {
            // Emit loading state
            val loadingState = _proposalsUi.value.copy(
                isLoading = true
            )
            _proposalsUi.value = loadingState

            val res = web3Repository.getProposals()

            when (res) {
                is OperationResult.Success -> {
                    _proposalsUi.value = _proposalsUi.value.copy(
                        data = res.data,
                        isLoading = false
                    )
                }

                is OperationResult.Failure -> {
                    _proposalsUi.value = _proposalsUi.value.copy(
                        isLoading = false,
                        error = res.error
                    )
                }
            }
        }
    }

    // TODO screen
    fun createProposal() {
        viewModelScope.launch {
            web3Repository.createProposal(
                title = "Mocked proposal",
                description = "Mock description lorem ipsum"
            )
        }
    }
}