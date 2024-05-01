package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.domain.core.BlockchainActionStatus
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.domain.transactions.TransactionStatus
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingData
import java.math.BigInteger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

class ProposalListPreviewProvider: PreviewParameterProvider<List<Proposal>> {
    private val mockDeployTransaction = TransactionDomain(
        type = TransactionType.CREATE_PROPOSAL,
        hash = "0x12334",
        dateSent = 0,
        status = TransactionStatus.PENDING,
        gasFee = Wei(BigInteger("0")),
        value = null,
    )

    private val mockVoteTransaction = TransactionDomain(
        type = TransactionType.VOTE,
        hash = "0x12334",
        dateSent = 0,
        status = TransactionStatus.MINED,
        gasFee = Wei(BigInteger("0")),
        value = null,
    )

    override val values: Sequence<List<Proposal>>
        get() = sequenceOf(
            listOf(
                Proposal.Draft(
                    uuid = "123",
                    title = "Lorem ipsum Lorem ipsum - Test",
                    description = "Support our mission to make quality education accessible to all. This voting campaign aims to allocate resources to educational programs, scholarships, and technology for underserved communities.",
                    deploymentTransaction = mockDeployTransaction,
                    creatorAddress = Address("0x12345678abcd"),
                    isSelfCreated = true,
                    deployStatus = BlockchainActionStatus.Completed,
                    duration = 1.days
                ),
                Proposal.Deployed(
                    uuid = "123",
                    proposalNumber = 123,
                    title = "Lorem ipsum Lorem ipsum - Test",
                    description = "Support our mission to make quality education accessible to all. This voting campaign aims to allocate resources to educational programs, scholarships, and technology for underserved communities.",
                    expirationTime = 1000_000_000_000_000,
                    votingData = VotingData(
                        votesAgainst = 100,
                        votesFor = 50,
                        selfVote = VoteType.VOTE_FOR
                    ),
                    creatorAddress = Address("0x12345678abcd"),
                    isSelfCreated = true,
                    voteTransaction = mockVoteTransaction,
                    selfVoteStatus = BlockchainActionStatus.NotCompleted.Todo,
                ),
            )
        )
}