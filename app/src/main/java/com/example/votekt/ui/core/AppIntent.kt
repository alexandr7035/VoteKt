package com.example.votekt.ui.core

import com.example.votekt.domain.model.explorer.ExploreType

sealed class AppIntent {
    object EnterApp : AppIntent()
    object ReconnectToNode : AppIntent()
    object ConsumeAppUnlocked : AppIntent()
    data class OpenBlockExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ) : AppIntent()
}
