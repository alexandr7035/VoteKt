package com.example.votekt.ui.feature.proposals.details

import com.example.votekt.domain.core.Uuid
import com.example.votekt.domain.model.explorer.ExploreType
import com.example.votekt.domain.votings.VoteType

sealed class ProposalDetailsScreenIntent {
    data class EnterScreen(val proposalUuid: String) : ProposalDetailsScreenIntent()
    data class ErrorRetryClick(val proposalUuid: String) : ProposalDetailsScreenIntent()
    data class ExplorerUrlClick(
        val payload: String,
        val exploreType: ExploreType,
    ) : ProposalDetailsScreenIntent()

    data class MakeVoteClick(
        val proposalNumber: Int,
        val voteType: VoteType
    ) : ProposalDetailsScreenIntent()

    data class DeployClick(
        val proposalUuid: Uuid,
    ) : ProposalDetailsScreenIntent()

    data class DeleteClick(
        val proposalUuid: Uuid,
    ) : ProposalDetailsScreenIntent()
    object GoBack : ProposalDetailsScreenIntent()
}
