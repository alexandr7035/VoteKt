package com.example.votekt.ui.feature_create_account

import androidx.lifecycle.ViewModel
import com.example.votekt.data.account.mnemonic.MnemonicRepository
import com.example.votekt.ui.feature_create_account.model.ConfirmPhraseIntent
import com.example.votekt.ui.feature_create_account.model.ConfirmPhraseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConfirmPhraseViewModel(
    private val mnemonicRepository: MnemonicRepository
): ViewModel() {
    private val _state = MutableStateFlow(ConfirmPhraseState(
        isLoading = true
    ))
    val state = _state.asStateFlow()

    fun emitIntent(intent: ConfirmPhraseIntent) {
        when (intent) {
            is ConfirmPhraseIntent.LoadData -> reduceLoadData(intent)
        }
    }

    private fun reduceLoadData(intent: ConfirmPhraseIntent.LoadData) {
        val confirmationData = mnemonicRepository.getRandomMnemonicWords(
            mnemonic = intent.phrase
        )

        _state.update {
            it.copy(
                confirmData = confirmationData
            )
        }
    }
}