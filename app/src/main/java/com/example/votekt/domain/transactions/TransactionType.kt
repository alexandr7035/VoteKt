package com.example.votekt.domain.transactions

enum class TransactionType {
    PAYMENT,
    CREATE_PROPOSAL,
    VOTE;
}

fun TransactionType.isContractInteraction() = this == TransactionType.VOTE
        || this == TransactionType.CREATE_PROPOSAL