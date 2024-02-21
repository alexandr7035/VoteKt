package by.alexandr7035.ethereum_impl.impl

import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.errors.TransactionReceiptNotFound
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.EthNodeMethods
import by.alexandr7035.ethereum.model.eth_requests.EthRequest
import by.alexandr7035.ethereum.model.EthereumBlock
import by.alexandr7035.ethereum.model.TransactionData
import by.alexandr7035.ethereum.model.EthTransactionReceipt
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.model.eth_requests.EthBalance
import by.alexandr7035.ethereum_impl.api.RetrofitEthereumRpcApi
import by.alexandr7035.ethereum_impl.model.JsonRpcRequest
import by.alexandr7035.ethereum_impl.model.toRpcRequest

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

    override suspend fun getBalance(address: Address): Wei {
        return request(EthBalance(address)).checkedResult()
    }

    override suspend fun sendRawTransaction(signedTransactionData: String): String {
        TODO("Not yet implemented")
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

    override suspend fun getTransactionByHash(transactionHash: String): TransactionData {
        TODO("Not yet implemented")
    }

    override suspend fun getBlockByHash(blockHash: String): EthereumBlock {
        TODO("Not yet implemented")
    }
}