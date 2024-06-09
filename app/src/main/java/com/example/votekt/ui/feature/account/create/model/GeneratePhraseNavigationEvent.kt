package com.example.votekt.ui.feature.account.create.model

sealed class GeneratePhraseNavigationEvent {
    object ToConfirmPhrase : GeneratePhraseNavigationEvent()
}
