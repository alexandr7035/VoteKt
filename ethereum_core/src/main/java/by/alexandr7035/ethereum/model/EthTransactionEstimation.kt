package by.alexandr7035.ethereum.model

import java.math.BigInteger

data class EthTransactionEstimation(
    val gasPrice: Wei,
    val balance: Wei,
    val nonce: BigInteger,
    val estimatedGas: BigInteger,
)
