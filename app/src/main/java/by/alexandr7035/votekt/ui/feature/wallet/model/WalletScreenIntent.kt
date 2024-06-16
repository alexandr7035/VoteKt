package by.alexandr7035.votekt.ui.feature.wallet.model

import by.alexandr7035.votekt.domain.model.explorer.ExploreType

sealed class WalletScreenIntent {
    object EnterScreen : WalletScreenIntent()
    object ResumeScreen : WalletScreenIntent()

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
    object RefreshBalance : WalletScreenIntent()
}
