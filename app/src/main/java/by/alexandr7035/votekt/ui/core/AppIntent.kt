package by.alexandr7035.votekt.ui.core

import by.alexandr7035.votekt.domain.model.explorer.ExploreType

sealed class AppIntent {
    object EnterApp : AppIntent()
    object ReconnectToNode : AppIntent()
    object ConsumeAppUnlocked : AppIntent()
    data class OpenBlockExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ) : AppIntent()
}
