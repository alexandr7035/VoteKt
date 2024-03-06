package com.example.votekt.data.cache

import androidx.room.Embedded
import androidx.room.Relation
import com.example.votekt.domain.transactions.TransactionStatus

data class ProposalWithTransactions(
    @Embedded val proposal: ProposalEntity,
    @Relation(
        parentColumn = "deployTransactionHash",
        entityColumn = "hash"
    )
    val deploymentTransaction: TransactionEntity?,
    @Relation(
        parentColumn = "selfVoteTransactionHash",
        entityColumn = "hash"
    )
    val voteTransaction: TransactionEntity?,
)

fun ProposalWithTransactions.isDeployFailed(): Boolean {
    return deploymentTransaction?.status == TransactionStatus.REVERTED
}

fun ProposalWithTransactions.shouldBeDeployed(): Boolean {
    return if (deploymentTransaction?.status == null) {
        proposal.isDraft
    } else {
        proposal.isDraft && deploymentTransaction.status == TransactionStatus.REVERTED
    }
}

fun ProposalWithTransactions.isDeployPending(): Boolean {
    return deploymentTransaction?.status == TransactionStatus.PENDING
}

fun ProposalWithTransactions.canVote(): Boolean {
    return if (voteTransaction?.status != null) {
        proposal.selfVote != null && voteTransaction.status != TransactionStatus.PENDING
    } else {
        proposal.selfVote == null
    }
}

fun ProposalWithTransactions.isVotePending(): Boolean {
    return voteTransaction?.status == TransactionStatus.PENDING
}

fun ProposalWithTransactions.isVoteFailed(): Boolean {
    return voteTransaction?.status == TransactionStatus.REVERTED
}