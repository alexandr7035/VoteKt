package com.example.votekt.ui.core

sealed class AppIntent {
    object EnterApp : AppIntent()
    object ReconnectToNode : AppIntent()
    object ConsumeAppUnlocked : AppIntent()
}