package by.alexandr7035.votekt.ui.feature.proposals.details

import by.alexandr7035.votekt.domain.core.Uuid
import by.alexandr7035.votekt.domain.model.explorer.ExploreType
import by.alexandr7035.votekt.domain.votings.VoteType

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
