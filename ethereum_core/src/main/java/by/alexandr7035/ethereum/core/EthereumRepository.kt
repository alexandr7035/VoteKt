package by.alexandr7035.ethereum.core

import by.alexandr7035.ethereum.errors.RequestFailedException
import by.alexandr7035.ethereum.errors.RequestNotExecutedException
import by.alexandr7035.ethereum.model.TransactionData
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.EthRequest
import by.alexandr7035.ethereum.model.EthereumBlock
import by.alexandr7035.ethereum.model.TransactionReceipt
import by.alexandr7035.ethereum.model.Wei

interface EthereumRepository {
    suspend fun <R : EthRequest<*>> request(request: R): R

    suspend fun getBalance(address: Address): Wei

    suspend fun sendRawTransaction(signedTransactionData: String): String

    suspend fun getTransactionReceipt(transactionHash: String): TransactionReceipt

    suspend fun getTransactionByHash(transactionHash: String): TransactionData

    suspend fun getBlockByHash(blockHash: String): EthereumBlock
}
