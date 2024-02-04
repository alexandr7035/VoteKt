package com.example.votekt.data.web3_core.transactions.processor

import android.os.SystemClock
import com.example.votekt.data.web3_core.transactions.TxError
import com.example.votekt.data.web3_core.transactions.TxHash
import com.example.votekt.data.web3_core.transactions.TxResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.TransactionReceipt
import kotlin.coroutines.coroutineContext
import kotlin.jvm.optionals.getOrNull
import kotlin.time.Duration


class AwaitingTxProcessor(
    private val web3j: Web3j,
    private val maxWaitingTime: Duration
): TxProcessor {
    override fun observeTransactionResult(hash: TxHash): Flow<TxResult> = flow {
        val startTime = SystemClock.uptimeMillis()
        emit(TxResult.AwaitingResult)

        while (coroutineContext.isActive) {
            val receipt = checkTransactionReceipt(hash)

            if (receipt != null) {
                emit(TxResult.Success(receipt))
                break
            }

            if (isTimeExceeded(startTime)) {
                emit(TxResult.TimeExceeded)
            }
        }
    }

    private fun isTimeExceeded(startTimeMills: Long): Boolean {
        return (SystemClock.uptimeMillis() - startTimeMills) > maxWaitingTime.inWholeMilliseconds
    }

    private suspend fun checkTransactionReceipt(hash: TxHash): TransactionReceipt? = withContext(Dispatchers.IO) {
        val transactionReceipt = web3j.ethGetTransactionReceipt(hash.value).send()

        if (transactionReceipt.hasError()) {
            throw TxError(transactionReceipt.error.message)
        }

        return@withContext transactionReceipt.transactionReceipt.getOrNull()
    }
}