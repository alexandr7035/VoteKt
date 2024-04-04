package by.alexandr7035.ethereum.model.eth_events

sealed class EthereumEvent {
    object SubscribedEvent: EthereumEvent()

    data class ContractEvent(
        val eventTopic: String,
        val encodedData: String,
    ): EthereumEvent()

    object UnknownEvent: EthereumEvent()
}