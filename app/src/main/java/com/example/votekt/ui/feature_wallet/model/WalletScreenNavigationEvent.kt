package com.example.votekt.ui.feature_wallet.model

import com.example.votekt.domain.model.blockchain_explorer.ExploreType

sealed class WalletScreenNavigationEvent {
    object ToSend: WalletScreenNavigationEvent()
    object ToReceive: WalletScreenNavigationEvent()
    object ToVote: WalletScreenNavigationEvent()
    object ToNetworkDetails: WalletScreenNavigationEvent()
    object ToWelcomeScreen: WalletScreenNavigationEvent()

    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ): WalletScreenNavigationEvent()
}
