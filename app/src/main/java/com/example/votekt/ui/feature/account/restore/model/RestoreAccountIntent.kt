package com.example.votekt.ui.feature.account.restore.model

sealed class RestoreAccountIntent {
    object EnterScreen : RestoreAccountIntent()
    object ConfirmClick : RestoreAccountIntent()
    data class ChangePhrase(
        val value: String,
    ) : RestoreAccountIntent()
}
