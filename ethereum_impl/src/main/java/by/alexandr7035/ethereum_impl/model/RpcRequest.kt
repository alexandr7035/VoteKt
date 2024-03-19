package by.alexandr7035.ethereum_impl.model

import by.alexandr7035.ethereum.model.EthNodeMethods
import by.alexandr7035.ethereum.model.EthNodeMethods.FUNCTION_GET_TRANSACTION_COUNT
import by.alexandr7035.ethereum.model.EthNodeMethods.FUNCTION_SEND_RAW_TRANSACTION
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.model.asString
import by.alexandr7035.ethereum.model.eth_requests.EthBalance
import by.alexandr7035.ethereum.model.eth_requests.EthGetTransactionCount
import by.alexandr7035.ethereum.model.eth_requests.EthRequest
import by.alexandr7035.ethereum.model.eth_requests.EthSendRawTransaction
import by.alexandr7035.utils.hexAsBigIntegerOrNull
import java.math.BigInteger


sealed class RpcRequest<out T : EthRequest<*>>(val raw: T) {
    abstract fun request(): JsonRpcRequest
    abstract fun parse(response: JsonRpcResult)
}

class RpcBalanceRequest(raw: EthBalance) : RpcRequest<EthBalance>(raw) {
    override fun request() =
        JsonRpcRequest(
            method = EthNodeMethods.FUNCTION_GET_BALANCE,
            params = listOf(raw.address.value, raw.block.asString()),
            id = raw.id
        )

    override fun parse(response: JsonRpcResult) {
        raw.response = response.error?.let { EthRequest.Response.Failure<Wei>(it.message) }
            ?: response.result?.hexAsBigIntegerOrNull()?.let { EthRequest.Response.Success(Wei(it)) }
                    ?: EthRequest.Response.Failure("Invalid balance!")
    }
}

class RpcSendRawTransaction(raw: EthSendRawTransaction) : RpcRequest<EthSendRawTransaction>(raw) {
    override fun request() =
        JsonRpcRequest(
            method = FUNCTION_SEND_RAW_TRANSACTION,
            params = listOf(raw.signedData),
            id = raw.id
        )

    override fun parse(response: JsonRpcResult) {
        raw.response =
            response.error?.let { EthRequest.Response.Failure<String>(it.message) }
                ?: response.result?.let { EthRequest.Response.Success(response.result) }
                        ?: EthRequest.Response.Failure("Missing result")
    }
}

class RpcTransactionCountRequest(raw: EthGetTransactionCount) :
    RpcRequest<EthGetTransactionCount>(raw) {
    override fun request() =
        JsonRpcRequest(
            method = FUNCTION_GET_TRANSACTION_COUNT,
            params = arrayListOf(raw.from, raw.block.asString()),
            id = raw.id
        )

    override fun parse(response: JsonRpcResult) {
        raw.response = response.error?.let { EthRequest.Response.Failure<BigInteger>(it.message) }
            ?: response.result?.hexAsBigIntegerOrNull()?.let { EthRequest.Response.Success(it) }
                    ?: EthRequest.Response.Failure("Invalid transaction count!")
    }
}
