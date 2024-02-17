package by.alexandr7035.ethereum.model.eth_requests

import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.EthBlock
import by.alexandr7035.ethereum.model.Wei


data class EthBalance(
    val address: Address,
    val block: EthBlock = EthBlock.PENDING,
    override val id: Int = 0
) : EthRequest<Wei>(id)