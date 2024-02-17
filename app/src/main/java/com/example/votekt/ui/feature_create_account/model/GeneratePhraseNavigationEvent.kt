package com.example.votekt.ui.feature_create_account.model

sealed class GeneratePhraseNavigationEvent {
    object ToConfirmPhrase: GeneratePhraseNavigationEvent()
}
