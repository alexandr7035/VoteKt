package by.alexandr7035.ethereum.model.requests

open class EthBulkRequest(val requests: List<EthRequest<*>>) {
    constructor(vararg requests: EthRequest<*>) : this(requests.toList())
}
