package com.example.votekt.ui.feature_wallet.model

sealed class WalletScreenIntent {
    object EnterScreen: WalletScreenIntent()

    sealed class WalletAction: WalletScreenIntent() {
        object Send: WalletAction()
        object Receive: WalletAction()
        object Vote: WalletAction()
    }
}
