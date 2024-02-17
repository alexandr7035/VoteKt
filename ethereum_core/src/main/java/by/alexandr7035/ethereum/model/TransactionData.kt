package by.alexandr7035.ethereum.model

import java.math.BigInteger

data class TransactionData(
    val hash: String,
    val from: Address,
//    val transaction: Transaction.Legacy,
    val transactionIndex: BigInteger?,
    val blockHash: String?,
    val blockNumber: BigInteger?
)