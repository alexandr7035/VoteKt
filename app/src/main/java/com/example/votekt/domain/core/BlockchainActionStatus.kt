package com.example.votekt.domain.core

sealed class BlockchainActionStatus {
    sealed class NotCompleted : BlockchainActionStatus() {
        object Todo : NotCompleted()

        object Failed : NotCompleted()
    }

    object Pending : BlockchainActionStatus()

    object Completed : BlockchainActionStatus()
}
