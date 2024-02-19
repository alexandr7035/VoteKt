package com.example.votekt.ui.utils

import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.data.account.mnemonic.Word
import com.example.votekt.data.account.mnemonic.WordToConfirm
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TransactionType
import com.example.votekt.data.web3_core.transactions.TxStatus
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingData
import java.math.BigInteger

fun Proposal.Companion.mock(): Proposal {
    return Proposal(
        id = 0,
        title = "My awesome Proposal",
        description = "My awesome description. Lorem ipsum lorem ipsum. My awesome description. Lorem ipsum lorem ipsum.",
        votingData = VotingData(10, 25, VoteType.VOTE_FOR),
        expirationTime = 0
    )
}

fun WordToConfirm.Companion.mock() = WordToConfirm(
    correctWord = Word(0, "Lorem"), incorrectWords = listOf(
        Word(5, "Wrong1"),
        Word(10, "Wrong2"),
    )
)

fun Transaction.Companion.mock() = Transaction(
    type = TransactionType.CREATE_PROPOSAL,
    hash = "abcdef1234",
    status = TxStatus.MINED,
    dateSent = 0,
    gasUsed = BigInteger("1000")
)