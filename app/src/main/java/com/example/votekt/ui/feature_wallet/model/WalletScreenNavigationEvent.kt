package com.example.votekt.ui.feature_wallet.model

sealed class WalletScreenNavigationEvent {
    object ToSend: WalletScreenNavigationEvent()
    object ToReceive: WalletScreenNavigationEvent()
    object ToVote: WalletScreenNavigationEvent()
    object ToNetworkDetails: WalletScreenNavigationEvent()
    object ToWelcomeScreen: WalletScreenNavigationEvent()
}
