package by.alexandr7035.ethereum_impl.model

import by.alexandr7035.ethereum.model.eth_requests.EthBalance
//import by.alexandr7035.ethereum.model.eth_requests.EthCall
import by.alexandr7035.ethereum.model.eth_requests.EthRequest

fun <T> EthRequest<T>.toRpcRequest() =
    when (this) {
//        is EthCall -> RpcCallRequest(this)
        is EthBalance -> RpcBalanceRequest(this)
    }
