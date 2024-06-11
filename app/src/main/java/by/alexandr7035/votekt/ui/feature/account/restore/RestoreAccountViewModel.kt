package by.alexandr7035.votekt.ui.feature.account.restore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.usecase.account.AddAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.VerifyMnemonicPhraseUseCase
import by.alexandr7035.votekt.domain.usecase.debug.GetTestMnemonicUseCase
import by.alexandr7035.votekt.ui.asTextError
import by.alexandr7035.votekt.ui.feature.account.restore.model.RestoreAccountIntent
import by.alexandr7035.votekt.ui.feature.account.restore.model.RestoreAccountNavigationEvent
import by.alexandr7035.votekt.ui.feature.account.restore.model.RestoreAccountState
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
) : ViewModel() {
    private val _state = MutableStateFlow(RestoreAccountState())
    val state = _state.asStateFlow()

    fun onIntent(intent: RestoreAccountIntent) {
        println("intent $intent")
        when (intent) {
            RestoreAccountIntent.EnterScreen -> onEnterScreen()
            RestoreAccountIntent.ConfirmClick -> onConfirm()
            is RestoreAccountIntent.ChangePhrase -> {
                _state.update {
                    it.copy(
                        wordInput = intent.value,
                        error = null,
                    )
                }
            }
        }
    }

    private fun onEnterScreen() {
        // TODO toggle flag
        val testPhrase = getTestMnemonicUseCase.invoke()

        _state.update {
            it.copy(wordInput = testPhrase.joinToString(WORD_SEPARATOR) { word -> word.value })
        }
    }

    private fun onConfirm() {
        val enteredMnemonic = _state.value.wordInput

        viewModelScope.launch {
            val verifyMnemonic = OperationResult.runWrapped {
                verifyMnemonicPhraseUseCase.invoke(enteredMnemonic.trim().split(WORD_SEPARATOR))
            }

            when (verifyMnemonic) {
                is OperationResult.Success -> {
                    addAccount(enteredMnemonic.trim().split(WORD_SEPARATOR))
                }

                is OperationResult.Failure -> {
                    reduceError(verifyMnemonic)
                }
            }
        }
    }

    private suspend fun addAccount(enteredMnemonic: List<String>) {
        val addAccountRes = OperationResult.runWrapped {
            addAccountUseCase.invoke(
                enteredMnemonic.mapIndexed { index, it ->
                    MnemonicWord(index, it)
                }
            )
        }

        when (addAccountRes) {
            is OperationResult.Failure -> {
                reduceError(addAccountRes)
            }

            is OperationResult.Success -> {
                _state.update {
                    it.copy(
                        navigationEvent = triggered(RestoreAccountNavigationEvent.GoToSetupAppLock)
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

    companion object {
        private const val WORD_SEPARATOR = " "
    }
}
