package by.alexandr7035.ethereum.model

enum class EthBlock {
    EARLIEST,
    LATEST,
    PENDING,;
}

fun EthBlock.asString(): String {
    return when (this) {
        EthBlock.EARLIEST -> "earliest"
        EthBlock.LATEST -> "latest"
        EthBlock.PENDING -> "pending"
    }
}