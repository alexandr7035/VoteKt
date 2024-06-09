package by.alexandr7035.ethereum.model

import org.kethereum.model.Address
import java.math.BigInteger

data class EthereumBlock(
    val number: BigInteger?,
    val hash: String?,
    val parentHash: String?,
    val nonce: String?,
    val sha3Uncles: String?,
    val logsBloom: String?,
    val transactionsRoot: String,
    val stateRoot: String,
    val receiptsRoot: String,
    val miner: Address,
    val difficulty: BigInteger,
    val totalDifficulty: BigInteger,
    val extraData: String?,
    val size: BigInteger,
    val gasLimit: BigInteger,
    val gasUsed: BigInteger,
    val timestamp: BigInteger
)
