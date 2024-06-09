package by.alexandr7035.votekt.ui.feature.wallet.actions

sealed class WalletAction {
    object Send : WalletAction()
    object Receive : WalletAction()
    object Vote : WalletAction()
}
