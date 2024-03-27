package by.alexandr7035.ethereum_impl.model

import by.alexandr7035.ethereum.model.eth_requests.EthBalance
import by.alexandr7035.ethereum.model.eth_requests.EthCall
import by.alexandr7035.ethereum.model.eth_requests.EthEstimateGas
import by.alexandr7035.ethereum.model.eth_requests.EthGasPrice
import by.alexandr7035.ethereum.model.eth_requests.EthGetTransactionCount
import by.alexandr7035.ethereum.model.eth_requests.EthRequest
import by.alexandr7035.ethereum.model.eth_requests.EthSendRawTransaction

fun <T> EthRequest<T>.toRpcRequest() =
    when (this) {
        is EthBalance -> RpcBalanceRequest(this)
        is EthSendRawTransaction -> RpcSendRawTransaction(this)
        is EthGetTransactionCount -> RpcTransactionCountRequest(this)
        is EthEstimateGas -> RpcEstimateGasRequest(this)
        is EthGasPrice -> RpcGasPriceRequest(this)
        is EthCall -> RpcCallRequest(this)
    }
