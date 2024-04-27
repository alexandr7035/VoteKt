package com.example.votekt.ui.feature_create_proposal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.model.contract.CreateDraftProposal
import com.example.votekt.domain.usecase.contract.CreateDraftProposalUseCase
import com.example.votekt.domain.usecase.contract.GetContractConfigurationUseCase
import com.example.votekt.ui.feature_create_proposal.model.CreateProposalResult
import com.example.votekt.ui.feature_create_proposal.model.CreateProposalScreenIntent
import com.example.votekt.ui.feature_create_proposal.model.CreateProposalScreenState
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration

class CreateProposalViewModel(
    private val createDraftProposalUseCase: CreateDraftProposalUseCase,
    private val getContractConfigurationUseCase: GetContractConfigurationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CreateProposalScreenState())
    val state = _state.asStateFlow()

    fun onIntent(intent: CreateProposalScreenIntent) {
        when (intent) {
            is CreateProposalScreenIntent.EnterScreen -> reduceEnterScreen()
            is CreateProposalScreenIntent.SelectProposalDuration -> reduceChooseDuration(intent.duration)
        }
    }

    private fun reduceEnterScreen() {
        _state.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val proposalConfig = getContractConfigurationUseCase.invoke()

            _state.update {
                it.copy(
                    titleMaxLength = proposalConfig.proposalTitleLimit,
                    descMaxLength = proposalConfig.proposalDescriptionLimit,
                    isLoading = false,
                )
            }
        }
    }

    private fun reduceChooseDuration(duration: Duration) {
        _state.update {
            it.copy(proposalDuration = duration)
        }
    }

    fun createProposal(data: CreateDraftProposal) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            when (val res = createDraftProposalUseCase.invoke(data)) {
                is OperationResult.Success -> {
                    _state.update { prev ->
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
                    _state.update { prev ->
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
        _state.update { prev ->
            prev.copy(submitProposalEvent = consumed())
        }
    }
}