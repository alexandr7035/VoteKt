package by.alexandr7035.ethereum.model.requests

import by.alexandr7035.ethereum.model.EthBlock
import by.alexandr7035.ethereum.model.Wei
import org.kethereum.model.Address

data class EthBalance(
    val address: Address,
    val block: EthBlock = EthBlock.PENDING,
    override val id: Int = 0
) : EthRequest<Wei>(id)
