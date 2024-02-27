package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import by.alexandr7035.ethereum.model.Address
import com.example.votekt.domain.transactions.TransactionHash
import com.example.votekt.domain.votings.Proposal

class ProposalPreviewProvider: PreviewParameterProvider<Proposal> {
    override val values: Sequence<Proposal> = sequenceOf(
        Proposal.Draft(
            id = 0,
            title = "Lorem ipsum Lorem ipsum - Test",
            description = "Support our mission to make quality education accessible to all. This voting campaign aims to allocate resources to educational programs, scholarships, and technology for underserved communities.",
            deploymentTransactionHash = TransactionHash("0x12334"),
            creatorAddress = Address("0x12345678abcd"),
            isSelfCreated = true,
            shouldDeploy = true,
            deployFailed = true
        ),
        Proposal.Draft(
            id = 0,
            title = "Lorem ipsum Lorem ipsum - Test",
            description = "Support our mission to make quality education accessible to all. This voting campaign aims to allocate resources to educational programs, scholarships, and technology for underserved communities.",
            deploymentTransactionHash = TransactionHash("0x12334"),
            creatorAddress = Address("0x12345678abcd"),
            isSelfCreated = true,
            shouldDeploy = false,
            deployFailed = false
        ),
        Proposal.Deployed(
            id = 0,
            blockchainId = 123,
            title = "Lorem ipsum Lorem ipsum - Test",
            description = "Support our mission to make quality education accessible to all. This voting campaign aims to allocate resources to educational programs, scholarships, and technology for underserved communities.",
            expirationTime = 1000_000_000_000_000,
            votesAgainst = 100,
            votesFor = 50,
            creatorAddress = Address("0x12345678abcd"),
            isSelfCreated = true
        ),
        Proposal.Deployed(
            id = 0,
            blockchainId = 123,
            title = "Lorem ipsum Lorem ipsum - Test",
            description = "Support our mission to make quality education accessible to all. This voting campaign aims to allocate resources to educational programs, scholarships, and technology for underserved communities.",
            expirationTime = 0,
            votesAgainst = 25,
            votesFor = 100,
            creatorAddress = Address("0x12345678abcd"),
            isSelfCreated = false
        )
    )
}