package by.alexandr7035.ethereum.model.requests

data class EthSendRawTransaction(
    val signedData: String,
    override val id: Int = 0
) : EthRequest<String>(id)
