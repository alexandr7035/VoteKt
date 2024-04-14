package com.example.votekt.ui.feature_restore_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.usecase.account.AddAccountUseCase
import com.example.votekt.domain.usecase.account.GetTestMnemonicUseCase
import com.example.votekt.domain.usecase.account.VerifyMnemonicPhraseUseCase
import com.example.votekt.ui.asTextError
import com.example.votekt.ui.feature_restore_account.model.RestoreAccountIntent
import com.example.votekt.ui.feature_restore_account.model.RestoreAccountNavigationEvent
import com.example.votekt.ui.feature_restore_account.model.RestoreAccountState
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestoreAccountViewModel(
    private val getTestMnemonicUseCase: GetTestMnemonicUseCase,
    private val verifyMnemonicPhraseUseCase: VerifyMnemonicPhraseUseCase,
    private val addAccountUseCase: AddAccountUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(RestoreAccountState())
    val state = _state.asStateFlow()

    fun onIntent(intent: RestoreAccountIntent) {
        when (intent) {
            RestoreAccountIntent.EnterScreen -> onEnterScreen()
            RestoreAccountIntent.ConfirmClick -> onConfirm()
        }
    }

    private fun onEnterScreen() {
        // TODO toggle flag
        val testPhrase = getTestMnemonicUseCase.invoke()

        _state.update {
            it.copy(wordInput = testPhrase)
        }
    }

    private fun onConfirm() {
        val enteredMnemonic = _state.value.wordInput

        viewModelScope.launch {
            val verifyMnemonic = OperationResult.runWrapped {
                verifyMnemonicPhraseUseCase.invoke(enteredMnemonic.map { it.value })
            }

            when (verifyMnemonic) {
                is OperationResult.Success -> {
                    addAccount(enteredMnemonic.map { it.value })
                }

                is OperationResult.Failure -> {
                    reduceError(verifyMnemonic)
                }
            }
        }
    }

    private suspend fun addAccount(enteredMnemonic: List<String>) {
        val addAccountRes = OperationResult.runWrapped {
            addAccountUseCase.invoke(enteredMnemonic.mapIndexed { index, it ->
                MnemonicWord(index, it)
            })
        }

        when (addAccountRes) {
            is OperationResult.Failure -> {
                reduceError(addAccountRes)
            }

            is OperationResult.Success -> {
                _state.update {
                    it.copy(
                        navigationEvent = triggered(RestoreAccountNavigationEvent.GoToHome)
                    )
                }
            }
        }

    }

    private fun reduceError(errorResult: OperationResult.Failure) {
        _state.update {
            it.copy(error = errorResult.error.errorType.asTextError())
        }
    }

    fun consumeNavigationEvent() {
        _state.update { it.copy(navigationEvent = consumed()) }
    }
}