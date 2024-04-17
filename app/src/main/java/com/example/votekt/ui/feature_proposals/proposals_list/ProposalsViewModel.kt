package com.example.votekt.ui.feature_proposals.proposals_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.datasync.SyncWithContractUseCase
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.ui.uiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class ProposalsViewModel(
    private val votingContractRepository: VotingContractRepository,
    private val syncWithContractUseCase: SyncWithContractUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProposalsScreenState())
    val state = _state.asStateFlow()

    init {
        subscribeToProposals()
    }

    fun onIntent(intent: ProposalsScreenIntent) {
        when (intent) {
            is ProposalsScreenIntent.ChangeControlsVisibility -> {
                _state.update {
                    it.copy(controlsAreVisible = intent.isVisible)
                }
            }
        }
    }

    fun subscribeToProposals() {
        votingContractRepository
            .getProposals()
            .onStart {
                val syncResult = OperationResult.runWrapped {
                    syncWithContractUseCase.invoke()
                }

                when (syncResult) {
                    is OperationResult.Success -> {}
                    is OperationResult.Failure -> throw syncResult.error
                }
            }
            .onEach { proposals ->
                _state.update {
                    it.copy(
                        proposals = proposals,
                        error = null,
                        isLoading = false
                    )
                }
            }
            .catch { error ->
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