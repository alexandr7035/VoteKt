package com.example.votekt.ui.votings_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.votings.VotingRepository
import com.example.votekt.ui.uiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ProposalsViewModel(private val votingRepository: VotingRepository) : ViewModel() {
    private val _state = MutableStateFlow(ProposalsScreenState())
    val state = _state.asStateFlow()

    init {
        votingRepository
            .getProposals()
            .onEach { proposals ->
                _state.update {
                    it.copy(
                        proposals = proposals,
                        error = null,
                        isLoading = false
                    )
                }
            }
            .catch {error ->
                _state.update {
                    it.copy(
                        error = ErrorType.fromThrowable(error).uiError,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}