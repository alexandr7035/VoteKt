package com.example.votekt.data

import by.alexandr7035.ethereum.model.EthTransactionReceipt
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.web3_core.transactions.TxHash
import com.example.votekt.data.web3_core.transactions.TxStatus
import com.example.votekt.domain.core.OperationResult

interface TransactionRepository {
    suspend fun getTransactions(): List<Transaction>

    suspend fun cacheTransaction(transaction: Transaction)

    suspend fun refreshTxStatus(txHash: String): OperationResult<TxStatus>

    suspend fun clearTransactions(): OperationResult<Unit>
    suspend fun updateTransactionIfExists(receipt: EthTransactionReceipt)
}