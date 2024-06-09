package by.alexandr7035.ethereum.model.requests

import org.kethereum.model.Address

data class EthCall(
    val to: Address,
    val data: String,
    override val id: Int = 0
) : EthRequest<String>(id)
