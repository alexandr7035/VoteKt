package com.example.votekt.ui.feature_welcome

import androidx.lifecycle.ViewModel
import com.example.votekt.ui.feature_welcome.model.WelcomeScreenIntent
import com.example.votekt.ui.feature_welcome.model.WelcomeScreenNavigationEvent
import com.example.votekt.ui.feature_welcome.model.WelcomeScreenState
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WelcomeScreenViewModel: ViewModel() {
    private val _state = MutableStateFlow(WelcomeScreenState())
    val state = _state.asStateFlow()

    fun onIntent(intent: WelcomeScreenIntent) {
        val navEvent = when (intent) {
            WelcomeScreenIntent.AlreadyHaveAccountClick -> WelcomeScreenNavigationEvent.ToRestoreAccount
            WelcomeScreenIntent.CreateAccountClick -> WelcomeScreenNavigationEvent.ToCreateAccount
        }

        _state.update {
            it.copy(navigationEvent = triggered(navEvent))
        }
    }

    fun consumeNavigationEvent() {
        _state.update { it.copy(navigationEvent = consumed()) }
    }
}