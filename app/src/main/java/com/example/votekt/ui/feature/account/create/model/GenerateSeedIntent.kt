package com.example.votekt.ui.feature.account.create.model

sealed class GenerateSeedIntent {
    object Load : GenerateSeedIntent()

    object Confirm : GenerateSeedIntent()
}
