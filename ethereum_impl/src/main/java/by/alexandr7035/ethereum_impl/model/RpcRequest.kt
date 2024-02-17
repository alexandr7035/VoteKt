package by.alexandr7035.ethereum_impl.model

import by.alexandr7035.ethereum.model.EthNodeMethods
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.model.asString
import by.alexandr7035.ethereum.model.eth_requests.EthBalance
import by.alexandr7035.ethereum.model.eth_requests.EthRequest
import by.alexandr7035.ethereum.utils.hexAsBigIntegerOrNull


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
