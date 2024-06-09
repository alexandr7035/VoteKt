package by.alexandr7035.votekt.domain.usecase.node

import by.alexandr7035.votekt.domain.repository.WebsocketManager

class ConnectToNodeUseCase(
    private val websocketManager: WebsocketManager
) {
    fun invoke() {
        websocketManager.connect()
    }
}
