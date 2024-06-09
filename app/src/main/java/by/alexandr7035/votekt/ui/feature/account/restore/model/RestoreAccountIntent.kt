package by.alexandr7035.votekt.ui.feature.account.restore.model

sealed class RestoreAccountIntent {
    object EnterScreen : RestoreAccountIntent()
    object ConfirmClick : RestoreAccountIntent()
    data class ChangePhrase(
        val value: String,
    ) : RestoreAccountIntent()
}
