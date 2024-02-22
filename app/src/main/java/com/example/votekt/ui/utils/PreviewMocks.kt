package com.example.votekt.ui.utils

import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.domain.account.MnemonicWordConfirm
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingData

fun Proposal.Companion.mock(): Proposal {
    return Proposal(
        id = 0,
        title = "My awesome Proposal",
        description = "My awesome description. Lorem ipsum lorem ipsum. My awesome description. Lorem ipsum lorem ipsum.",
        votingData = VotingData(10, 25, VoteType.VOTE_FOR),
        expirationTime = 0
    )
}

fun MnemonicWordConfirm.Companion.mock() = MnemonicWordConfirm(
    correctWord = MnemonicWord(0, "Lorem"), incorrectWords = listOf(
        MnemonicWord(5, "Wrong1"),
        MnemonicWord(10, "Wrong2"),
    )
)
