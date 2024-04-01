package by.alexandr7035.ethereum.core

import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import kotlinx.coroutines.flow.Flow

interface EthereumEventListener {
    suspend fun subscribe(): Flow<EthereumEvent>
}