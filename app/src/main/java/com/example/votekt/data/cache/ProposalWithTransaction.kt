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

fun ProposalWithTransaction.isDeployedAndSynced(): Boolean {
    return if (deploymentTransaction != null) {
        deploymentTransaction.status == TransactionStatus.MINED &&
                proposal.number != null && proposal.expiresAt != null
    } else {
        proposal.number != null && proposal.expiresAt != null
    }
}

fun ProposalWithTransaction.isDeployFailed(): Boolean {
    return deploymentTransaction?.status == TransactionStatus.REVERTED
}

fun ProposalWithTransaction.shouldBeDeployed(isSelf: Boolean): Boolean {
    return if (isSelf) {
        (proposal.deployTransactionHash == null
                || deploymentTransaction?.status == TransactionStatus.REVERTED)
    } else {
        false
    }
}
