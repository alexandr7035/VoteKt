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

    val proposalsListUi = _proposalsListUi.asStateFlow()

    fun loadProposals() {
        viewModelScope.launch {
            // Emit loading state

            _proposalsListUi.update { prev ->
                prev.copy(
                    isLoading = true
                )
            }

            val res = web3Repository.getProposals()
            Log.d("TEST", res.toString())

            when (res) {
                is OperationResult.Success -> {

                    _proposalsListUi.update { prev ->
                        prev.copy(
                            data = res.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is OperationResult.Failure -> {
                    _proposalsListUi.update { prev ->
                        prev.copy(
                            isLoading = false,
                            error = res.error
                        )
                    }
                }
            }
        }
    }
}