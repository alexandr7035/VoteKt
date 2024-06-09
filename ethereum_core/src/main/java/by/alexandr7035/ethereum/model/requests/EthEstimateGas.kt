package by.alexandr7035.ethereum.model.requests

import org.kethereum.model.Address
import org.kethereum.model.Transaction
import java.math.BigInteger

data class EthEstimateGas(
    val from: Address,
    val transaction: Transaction,
    override val id: Int = 0
) : EthRequest<BigInteger>(id)
