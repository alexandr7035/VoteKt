package com.example.votekt.ui.feature_create_account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.account.mnemonic.MnemonicRepository
import com.example.votekt.data.account.mnemonic.Word
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.ui.feature_create_account.model.ConfirmPhraseIntent
import com.example.votekt.ui.feature_create_account.model.ConfirmPhraseNavigationEvent
import com.example.votekt.ui.feature_create_account.model.ConfirmPhraseState
import com.example.votekt.ui.uiError
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfirmPhraseViewModel(
    private val mnemonicRepository: MnemonicRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        ConfirmPhraseState(
            isLoading = true
        )
    )
    val state = _state.asStateFlow()

    private val confirmationSelection: MutableMap<Int, Word> = mutableMapOf()

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
        val confirmationData = mnemonicRepository.getRandomMnemonicWords(
            mnemonic = intent.phrase
        )

        _state.update {
            it.copy(
                confirmData = confirmationData,
                phrase = intent.phrase
            )
        }
    }

    private fun reduceSelectConfirmation(index: Int, word: Word) {
        confirmationSelection[index] = word
        // Clear error
        _state.update {
            it.copy(confirmationError = null)
        }
    }

    private fun reduceConfirmPhrase() {
        viewModelScope.launch {
            val res = OperationResult.runWrapped {
                mnemonicRepository.confirmPhrase(
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