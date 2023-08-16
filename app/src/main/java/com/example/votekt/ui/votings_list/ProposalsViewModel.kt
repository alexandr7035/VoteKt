package com.example.votekt.ui.votings_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.OperationResult
import com.example.votekt.data.Web3Repository
import com.example.votekt.data.model.Proposal
import com.example.votekt.ui.core.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProposalsViewModel(private val web3Repository: Web3Repository) : ViewModel() {
    private val _proposalsListUi = MutableStateFlow<ScreenState<List<Proposal>>>(
        ScreenState(
            data = null,
            isLoading = true,
            error = null
        )
    )

    private val _proposalUi = MutableStateFlow<ScreenState<Proposal>>(
        ScreenState(
            data = null,
            isLoading = true,
            error = null
        )
    )

    val proposalsListUi = _proposalsListUi.asStateFlow()
    val proposalUi = _proposalUi.asStateFlow()

    fun loadProposals() {
        viewModelScope.launch {
            // Emit loading state
            val loadingState = _proposalsListUi.value.copy(
                isLoading = true
            )
            _proposalsListUi.value = loadingState

            val res = web3Repository.getProposals()
            Log.d("TEST", res.toString())

            when (res) {
                is OperationResult.Success -> {
                    _proposalsListUi.value = _proposalsListUi.value.copy(
                        data = res.data,
                        isLoading = false,
                        error = null
                    )
                }

                is OperationResult.Failure -> {
                    _proposalsListUi.value = _proposalsListUi.value.copy(
                        isLoading = false,
                        error = res.error
                    )
                }
            }
        }
    }


    fun loadProposalById(id: Long) {
        viewModelScope.launch {
            _proposalUi.update { curr ->
                curr.copy(
                    isLoading = true
                )
            }

            when (val res = web3Repository.getProposalById(id)) {
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
}