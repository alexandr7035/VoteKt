package by.alexandr7035.ethereum.core

import by.alexandr7035.ethereum.model.eth_requests.EthBulkRequest
import by.alexandr7035.ethereum.model.EthTransactionEstimation
import by.alexandr7035.ethereum.model.EthTransactionInput
import by.alexandr7035.ethereum.model.EthTransactionReceipt
import by.alexandr7035.ethereum.model.EthereumBlock
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.model.eth_requests.EthRequest
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import java.math.BigInteger

interface EthereumClient {
    suspend fun getBalance(address: Address): Wei
    suspend fun sendRawTransaction(signedTransactionData: String): String
    suspend fun sendEthCall(to: Address, input: String, ): String
    suspend fun getTransactionReceipt(transactionHash: String): EthTransactionReceipt
    suspend fun getBlockByHash(blockHash: String): EthereumBlock
    suspend fun getTransactionCount(address: Address): BigInteger
    suspend fun estimateTransaction(transaction: Transaction): EthTransactionEstimation
}
