package com.example.votekt.data.cache

import androidx.room.Embedded
import androidx.room.Relation

data class ProposalWithTransaction(
    @Embedded val proposal: ProposalEntity,
    @Relation(
        parentColumn = "deployTransactionHash",
        entityColumn = "hash"
    )
    val deploymentTransaction: TransactionEntity?
)
