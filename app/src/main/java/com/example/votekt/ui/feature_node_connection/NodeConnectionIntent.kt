package com.example.votekt.ui.feature_node_connection

// todo offline mode
sealed class NodeConnectionIntent {
    object TryAgain: NodeConnectionIntent()
}