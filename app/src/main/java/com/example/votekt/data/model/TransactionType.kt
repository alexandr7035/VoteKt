package com.example.votekt.data.model

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