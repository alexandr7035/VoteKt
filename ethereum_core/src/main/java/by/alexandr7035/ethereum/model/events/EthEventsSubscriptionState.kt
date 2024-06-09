package by.alexandr7035.ethereum.model.events

sealed class EthEventsSubscriptionState {
    object Connecting : EthEventsSubscriptionState()
    object Connected : EthEventsSubscriptionState()
    object Disconnected : EthEventsSubscriptionState()
}
