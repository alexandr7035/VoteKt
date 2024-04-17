package com.example.votekt.ui.feature_restore_account.model

sealed class RestoreAccountIntent {
    object EnterScreen: RestoreAccountIntent()
    object ConfirmClick: RestoreAccountIntent()
    data class ChangePhrase(
        val value: String,
    ): RestoreAccountIntent()
}
