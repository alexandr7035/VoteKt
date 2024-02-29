package com.example.votekt.domain.transactions

enum class TransactionType {
    CREATE_PROPOSAL,
    VOTE;

    val uiMessage: String
        get() {
            return when (this) {
                CREATE_PROPOSAL -> "Create proposal"
                VOTE -> "Vote on proposal"
            }
        }
}

fun TransactionType.isContractInteraction() = this == TransactionType.VOTE
        || this ==TransactionType.CREATE_PROPOSAL