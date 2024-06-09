package by.alexandr7035.ethereumimpl.model

import by.alexandr7035.ethereum.model.requests.EthBalance
import by.alexandr7035.ethereum.model.requests.EthCall
import by.alexandr7035.ethereum.model.requests.EthEstimateGas
import by.alexandr7035.ethereum.model.requests.EthGasPrice
import by.alexandr7035.ethereum.model.requests.EthGetTransactionCount
import by.alexandr7035.ethereum.model.requests.EthRequest
import by.alexandr7035.ethereum.model.requests.EthSendRawTransaction

fun <T> EthRequest<T>.toRpcRequest() =
    when (this) {
        is EthBalance -> RpcBalanceRequest(this)
        is EthSendRawTransaction -> RpcSendRawTransaction(this)
        is EthGetTransactionCount -> RpcTransactionCountRequest(this)
        is EthEstimateGas -> RpcEstimateGasRequest(this)
        is EthGasPrice -> RpcGasPriceRequest(this)
        is EthCall -> RpcCallRequest(this)
    }
