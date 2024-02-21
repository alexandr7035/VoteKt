package by.alexandr7035.ethereum.model

import java.math.BigInteger

data class EthTransactionReceipt(
    val status: BigInteger,
    val transactionHash: String,
    val transactionIndex: BigInteger,
    val blockHash: String,
    val blockNumber: BigInteger,
    val from: Address,
    val to: Address,
    val cumulativeGasUsed: BigInteger,
    val gasUsed: BigInteger,
    val effectiveGasPrice: BigInteger,
    val contractAddress: Address?,
) {
    fun isSuccessful() = status == BigInteger.ONE
}

fun EthTransactionReceipt.transactionFee() = Wei(effectiveGasPrice * gasUsed)