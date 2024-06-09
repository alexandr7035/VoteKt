package by.alexandr7035.votekt.domain.transactions

enum class TransactionType {
    PAYMENT,
    CREATE_PROPOSAL,
    VOTE
}

fun TransactionType.isContractInteraction() = this == TransactionType.VOTE ||
    this == TransactionType.CREATE_PROPOSAL
