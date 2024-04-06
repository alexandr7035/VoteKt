package by.alexandr7035.ethereum.core

import by.alexandr7035.ethereum.model.eth_events.EthEventsSubscriptionState
import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import kotlinx.coroutines.flow.Flow

interface EthereumEventListener {
    fun subscriptionStateFlow(): Flow<EthEventsSubscriptionState>
    suspend fun subscribeToEthereumEvents(): Flow<EthereumEvent>
    suspend fun disconnect()
}