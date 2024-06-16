package by.alexandr7035.votekt.ui.feature.account.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.domain.core.ErrorType
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.domain.usecase.account.AddAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.ConfirmNewAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.GetAccountConfirmationData
import by.alexandr7035.votekt.ui.feature.account.create.model.ConfirmPhraseIntent
import by.alexandr7035.votekt.ui.feature.account.create.model.ConfirmPhraseNavigationEvent
import by.alexandr7035.votekt.ui.feature.account.create.model.ConfirmPhraseState
import by.alexandr7035.votekt.ui.uiError
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfirmPhraseViewModel(
    private val addAccountUseCase: AddAccountUseCase,
    private val confirmNewAccountUseCase: ConfirmNewAccountUseCase,
    private val getAccountConfirmationData: GetAccountConfirmationData,
) : ViewModel() {
    private val _state = MutableStateFlow(
        ConfirmPhraseState(
            isLoading = true
        )
    )
    val state = _state.asStateFlow()

    private val confirmationSelection: MutableMap<Int, MnemonicWord> = mutableMapOf()

    fun emitIntent(intent: ConfirmPhraseIntent) {
        when (intent) {
            is ConfirmPhraseIntent.LoadData -> reduceLoadData(intent)
            is ConfirmPhraseIntent.SelectWordToConfirm -> reduceSelectConfirmation(
                index = intent.index,
                word = intent.word
            )

            is ConfirmPhraseIntent.ConfirmSeed -> reduceConfirmPhrase()
        }
    }

    private fun reduceLoadData(intent: ConfirmPhraseIntent.LoadData) {
        val confirmationData = getAccountConfirmationData.invoke(
            mnemonic = intent.phrase
        )

        _state.update {
            it.copy(
                confirmData = confirmationData,
                phrase = intent.phrase
            )
        }
    }

    private fun reduceSelectConfirmation(index: Int, word: MnemonicWord) {
        confirmationSelection[index] = word
        // Clear error
        _state.update {
            it.copy(confirmationError = null)
        }
    }

    private fun reduceConfirmPhrase() {
        viewModelScope.launch {
            val res = OperationResult.runWrapped {
                confirmNewAccountUseCase.invoke(
                    mnemonic = _state.value.phrase,
                    proposedWordsToConfirm = _state.value.confirmData,
                    confirmationData = confirmationSelection
                )
            }

            when (res) {
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(
                            navigationEvent = triggered(ConfirmPhraseNavigationEvent.ToHome)
                        )
                    }

                    addAccountUseCase.invoke(
                        _state.value.phrase
                    )
                }

                is OperationResult.Failure -> {
                    Log.d("WEB3_TAG", "failed confirmation ${res.error.errorType}")
                    if (res.error.errorType == ErrorType.MNEMONIC_CONFIRMATION_WRONG) {
                        _state.update {
                            it.copy(
                                confirmationError = res.error.errorType.uiError.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun consumeNavigationEvent() {
        _state.update {
            it.copy(
                navigationEvent = consumed()
            )
        }
    }
}
