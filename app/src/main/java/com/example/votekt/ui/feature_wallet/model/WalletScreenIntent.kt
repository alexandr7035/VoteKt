package com.example.votekt.ui.feature_wallet.model

sealed class WalletScreenIntent {
    object LoadData: WalletScreenIntent()

    sealed class WalletAction: WalletScreenIntent() {
        object Send: WalletAction()
        object Receive: WalletAction()
        object Vote: WalletAction()
    }
}
