package by.alexandr7035.votekt.ui.feature.account.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.domain.usecase.account.GenerateAccountUseCase
import by.alexandr7035.votekt.ui.feature.account.create.model.GeneratePhraseNavigationEvent
import by.alexandr7035.votekt.ui.feature.account.create.model.GenerateSeedIntent
import by.alexandr7035.votekt.ui.feature.account.create.model.GenerateSeedState
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GeneratePhraseViewModel(
    private val generateAccountUseCase: GenerateAccountUseCase
) : ViewModel() {
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
