package com.example.votekt.data.cache

import androidx.room.Embedded
import androidx.room.Relation
import com.example.votekt.domain.core.BlockchainActionStatus
import com.example.votekt.domain.transactions.TransactionStatus
import com.example.votekt.domain.votings.VoteType

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

fun ProposalWithTransactions.mapSelfVote(): VoteType? {
    return when (this.proposal.selfVote) {
        true -> VoteType.VOTE_FOR
        false -> VoteType.VOTE_AGAINST
        null -> null
    }
}

fun ProposalWithTransactions.mapDeployStatus(): BlockchainActionStatus {
    return when {
        isDeployFailed() -> BlockchainActionStatus.NotCompleted.Failed
        shouldBeDeployed() -> BlockchainActionStatus.NotCompleted.Todo
        isDeployPending() -> BlockchainActionStatus.Pending
        else -> BlockchainActionStatus.Completed
    }
}

fun ProposalWithTransactions.mapVoteStatus(): BlockchainActionStatus {
    return when {
        shouldVote() -> BlockchainActionStatus.NotCompleted.Todo
        isVotePending() -> BlockchainActionStatus.Pending
        isVoteFailed() -> BlockchainActionStatus.NotCompleted.Failed
        else -> BlockchainActionStatus.Completed
    }
}

private fun ProposalWithTransactions.shouldBeDeployed(): Boolean {
    return proposal.isDraft && deploymentTransaction?.status == null
}

private fun ProposalWithTransactions.isDeployFailed(): Boolean {
    return proposal.isDraft && deploymentTransaction?.status == TransactionStatus.REVERTED
}

private fun ProposalWithTransactions.isDeployPending(): Boolean {
    return proposal.isDraft && deploymentTransaction?.status == TransactionStatus.PENDING
}

private fun ProposalWithTransactions.shouldVote(): Boolean {
    return proposal.isDraft.not() && voteTransaction?.status == null && proposal.selfVote == null
}

private fun ProposalWithTransactions.isVoteFailed(): Boolean {
    return proposal.isDraft.not() && voteTransaction?.status == TransactionStatus.REVERTED && proposal.selfVote != null
}

private fun ProposalWithTransactions.isVotePending(): Boolean {
    return proposal.isDraft.not() && voteTransaction?.status == TransactionStatus.PENDING && proposal.selfVote != null
}
