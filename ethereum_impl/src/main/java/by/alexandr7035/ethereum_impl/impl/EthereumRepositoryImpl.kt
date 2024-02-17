package by.alexandr7035.ethereum_impl.impl

import by.alexandr7035.ethereum.core.EthereumRepository
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.eth_requests.EthRequest
import by.alexandr7035.ethereum.model.EthereumBlock
import by.alexandr7035.ethereum.model.TransactionData
import by.alexandr7035.ethereum.model.TransactionReceipt
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.model.eth_requests.EthBalance
import by.alexandr7035.ethereum_impl.api.RetrofitEthereumRpcApi
import by.alexandr7035.ethereum_impl.model.toRpcRequest

class EthereumRepositoryImpl(
    private val api: RetrofitEthereumRpcApi
): EthereumRepository {
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

    override suspend fun getTransactionReceipt(transactionHash: String): TransactionReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionByHash(transactionHash: String): TransactionData {
        TODO("Not yet implemented")
    }

    override suspend fun getBlockByHash(blockHash: String): EthereumBlock {
        TODO("Not yet implemented")
    }
}