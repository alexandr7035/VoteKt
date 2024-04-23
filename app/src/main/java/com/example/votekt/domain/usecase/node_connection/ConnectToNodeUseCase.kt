package com.example.votekt.domain.usecase.node_connection

import com.example.votekt.domain.repository.WebsocketManager

class ConnectToNodeUseCase(
    private val websocketManager: WebsocketManager
) {
    fun invoke() {
        websocketManager.connect()
    }
}