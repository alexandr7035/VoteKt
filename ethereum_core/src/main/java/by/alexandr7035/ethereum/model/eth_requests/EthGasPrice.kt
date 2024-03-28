package by.alexandr7035.ethereum.model.eth_requests

import java.math.BigInteger

data class EthGasPrice(
    override val id: Int = 0
) : EthRequest<BigInteger>(id)
