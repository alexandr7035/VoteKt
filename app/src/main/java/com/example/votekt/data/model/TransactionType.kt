package com.example.votekt.data.model

enum class TransactionType {
    CREATE_PROPOSAL;

    val uiMessage: String
        get() {
            return when (this) {
                CREATE_PROPOSAL -> "Create proposal"
            }
        }
}