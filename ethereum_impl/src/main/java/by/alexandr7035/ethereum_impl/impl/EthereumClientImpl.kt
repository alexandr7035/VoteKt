package by.alexandr7035.ethereum_impl.impl

import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.errors.TransactionReceiptNotFound
import by.alexandr7035.ethereum.model.EthNodeMethods
import by.alexandr7035.ethereum.model.EthTransactionEstimation
import by.alexandr7035.ethereum.model.EthTransactionReceipt
import by.alexandr7035.ethereum.model.EthereumBlock
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.model.eth_requests.EthBalance
import by.alexandr7035.ethereum.model.eth_requests.EthBulkRequest
import by.alexandr7035.ethereum.model.eth_requests.EthCall
import by.alexandr7035.ethereum.model.eth_requests.EthEstimateGas
import by.alexandr7035.ethereum.model.eth_requests.EthGasPrice
import by.alexandr7035.ethereum.model.eth_requests.EthGetTransactionCount
import by.alexandr7035.ethereum.model.eth_requests.EthRequest
import by.alexandr7035.ethereum.model.eth_requests.EthSendRawTransaction
import by.alexandr7035.ethereum_impl.api.RetrofitEthereumRpcApi
import by.alexandr7035.ethereum_impl.model.JsonRpcRequest
import by.alexandr7035.ethereum_impl.model.toRpcRequest
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import java.math.BigInteger

class EthereumClientImpl(
    private val api: RetrofitEthereumRpcApi
): EthereumClient {
    override suspend fun <R : EthRequest<*>> request(request: R): R {
        return request.toRpcRequest().let { rpcRequest ->
            api.post(rpcRequest.request())
                .let {
                    rpcRequest.parse(it)
                    request
                }
        }
    }

    override suspend fun <R : EthBulkRequest> request(bulk: R): R {
        return bulk.requests.associate { it.id to it.toRpcRequest() }
            .let { rpcRequests ->
                api.post(rpcRequests.values.map { it.request() })
                    .map { rpcRequests[it.id]?.parse(it) }
                    .let { bulk }
            }
    }

    override suspend fun getBalance(address: Address): Wei {
        return request(EthBalance(address)).checkedResult("Could not get balance")
    }

    override suspend fun sendRawTransaction(signedTransactionData: String): String {
        return request(EthSendRawTransaction(signedTransactionData))
            .checkedResult("Could not send raw transaction")
    }

    override suspend fun sendEthCall(to: Address, input: String): String {
        return request(EthCall(to, input)).checkedResult("Could not execute call")
    }

    override suspend fun getTransactionReceipt(transactionHash: String): EthTransactionReceipt {
        return api.receipt(
            JsonRpcRequest(
                method = EthNodeMethods.FUNCTION_GET_TRANSACTION_RECEIPT,
                params = listOf(transactionHash)
            )
        ).result?.let {
            EthTransactionReceipt(
                status = it.status,
                transactionHash = it.transactionHash,
                transactionIndex = it.transactionIndex,
                blockHash = it.blockHash,
                blockNumber = it.blockNumber,
                from = it.from,
                to = it.to,
                cumulativeGasUsed = it.cumulativeGasUsed,
                gasUsed = it.gasUsed,
                effectiveGasPrice = it.effectiveGasPrice,
                contractAddress = it.contractAddress,
            )
        } ?: throw TransactionReceiptNotFound()
    }

    override suspend fun getBlockByHash(blockHash: String): EthereumBlock {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionCount(address: Address): BigInteger {
        return request(EthGetTransactionCount(address))
            .checkedResult("Could not get tx count")
    }

    override suspend fun estimateTransaction(transaction: Transaction): EthTransactionEstimation {
        // remove the fee (it will be estimated)
        transaction.apply {
            gasLimit = null
            gasPrice = null
            maxPriorityFeePerGas = null
            maxFeePerGas = null
        }

        val gasPriceRequest = EthGasPrice(id = 1)
        val balanceRequest = EthBalance(address = transaction.from!!, id = 2)
        val nonceRequest = EthGetTransactionCount(from = transaction.from!!, id = 3)
        val estimateRequest = EthEstimateGas(from = transaction.from!!, transaction = transaction, id = 4)

        request(
            EthBulkRequest(
                listOf(
                    gasPriceRequest,
                    balanceRequest,
                    nonceRequest,
                    estimateRequest
                )
            )
        )

        val gasPrice = gasPriceRequest.checkedResult("Failed to get gas price")
        val balance = balanceRequest.checkedResult("Failed to get balance")
        val nonce = nonceRequest.checkedResult("Failed to get nonce")
        val estimate = estimateRequest.checkedResult("Failed to estimate transaction")

        return EthTransactionEstimation(
            gasPrice = Wei(gasPrice),
            balance = balance,
            nonce = nonce,
            estimatedGas = estimate,
        )
    }
}