package com.example.votekt.ui.feature.wallet.model

import com.example.votekt.domain.model.explorer.ExploreType

sealed class WalletScreenIntent {
    object LoadData : WalletScreenIntent()

    sealed class WalletAction : WalletScreenIntent() {
        object Send : WalletAction()
        object Receive : WalletAction()
        object Vote : WalletAction()
    }

    data class ChangeHeaderVisibility(val isVisible: Boolean) : WalletScreenIntent()

    data class ExplorerUrlClick(
        val payload: String,
        val exploreType: ExploreType,
    ) : WalletScreenIntent()

    object LogOut : WalletScreenIntent()
}
