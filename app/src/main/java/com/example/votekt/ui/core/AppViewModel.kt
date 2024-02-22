package com.example.votekt.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.account.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val accountRepository: AccountRepository
): ViewModel() {
    private val _appState: MutableStateFlow<AppState> = MutableStateFlow(AppState.Loading)
    val appState = _appState.asStateFlow()

    // This is a global app's viewModel
    init {
        emitIntent(AppIntent.EnterApp)
    }

    private fun emitIntent(intent: AppIntent) {
        when (intent) {
            AppIntent.EnterApp -> reduceCheckAccount()
        }
    }

    private fun reduceCheckAccount() {
        viewModelScope.launch {
            val shouldCreateAccount = accountRepository.isAccountPresent().not()

            _appState.update {
                AppState.Ready(
                    conditionalNavigation = ConditionalNavigation(
                        requireCreateAccount = shouldCreateAccount
                    )
                )
            }
        }
    }
}