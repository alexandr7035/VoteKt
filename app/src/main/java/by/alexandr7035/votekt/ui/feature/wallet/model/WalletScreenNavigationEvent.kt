package by.alexandr7035.votekt.ui.feature.wallet.model

import by.alexandr7035.votekt.domain.model.explorer.ExploreType

sealed class WalletScreenNavigationEvent {
    object ToSend : WalletScreenNavigationEvent()
    object ToReceive : WalletScreenNavigationEvent()
    object ToVote : WalletScreenNavigationEvent()
    object ToWelcomeScreen : WalletScreenNavigationEvent()

    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ) : WalletScreenNavigationEvent()
}
