package com.example.votekt.ui.feature_create_account.model

sealed class GenerateSeedIntent {
    object Load: GenerateSeedIntent()

    object Confirm: GenerateSeedIntent()
}