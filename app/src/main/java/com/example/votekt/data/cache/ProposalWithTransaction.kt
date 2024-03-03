package com.example.votekt.data.cache

import androidx.room.Embedded
import androidx.room.Relation
import com.example.votekt.domain.transactions.TransactionStatus

data class ProposalWithTransaction(
    @Embedded val proposal: ProposalEntity,
    @Relation(
        parentColumn = "deployTransactionHash",
        entityColumn = "hash"
    )
    val deploymentTransaction: TransactionEntity?
)

fun ProposalWithTransaction.isDeployFailed(): Boolean {
    return deploymentTransaction?.status == TransactionStatus.REVERTED
}

fun ProposalWithTransaction.shouldBeDeployed(): Boolean {
    return if (deploymentTransaction?.status == null) {
        proposal.isDraft
    } else {
        proposal.isDraft && deploymentTransaction.status == TransactionStatus.REVERTED
    }
}
