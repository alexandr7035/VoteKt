package com.example.votekt.ui.feature_create_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.account.MnemonicRepository
import com.example.votekt.domain.usecase.account.GenerateAccountUseCase
import com.example.votekt.ui.feature_create_account.model.GeneratePhraseNavigationEvent
import com.example.votekt.ui.feature_create_account.model.GenerateSeedIntent
import com.example.votekt.ui.feature_create_account.model.GenerateSeedState
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GeneratePhraseViewModel(
    private val generateAccountUseCase: GenerateAccountUseCase
): ViewModel() {
    private val _state = MutableStateFlow(GenerateSeedState())
    val state = _state.asStateFlow()

    fun emitIntent(intent: GenerateSeedIntent) {
        when (intent) {
            GenerateSeedIntent.Load -> reduceGeneratePhrase()
            GenerateSeedIntent.Confirm -> reduceConfirm()
        }
    }

    private fun reduceGeneratePhrase() {
        viewModelScope.launch {
            val phrase = generateAccountUseCase.invoke()
            _state.update {
                it.copy(
                    words = phrase,
                )
            }
        }
    }

    private fun reduceConfirm() {
        _state.update {
            it.copy(
                navigationEvent = triggered(GeneratePhraseNavigationEvent.ToConfirmPhrase),
            )
        }
    }

    fun consumeNavigationEvent() {
        _state.update {
            it.copy(navigationEvent = consumed())
        }
    }
}