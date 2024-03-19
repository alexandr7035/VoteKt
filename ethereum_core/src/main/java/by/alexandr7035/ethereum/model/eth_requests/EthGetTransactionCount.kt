package by.alexandr7035.ethereum.model.eth_requests

import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.EthBlock
import java.math.BigInteger

data class EthGetTransactionCount(
    val from: Address,
    val block: EthBlock = EthBlock.PENDING,
    override val id: Int = 0
) : EthRequest<BigInteger>(id)
