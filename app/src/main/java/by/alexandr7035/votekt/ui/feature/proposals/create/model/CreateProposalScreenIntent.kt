package by.alexandr7035.votekt.ui.feature.proposals.create.model

import kotlin.time.Duration

sealed class CreateProposalScreenIntent {
    object EnterScreen : CreateProposalScreenIntent()
    data class ChangeTitle(val title: String) : CreateProposalScreenIntent()
    data class ChangeDescription(val description: String) : CreateProposalScreenIntent()
    object SubmitProposal : CreateProposalScreenIntent()
    object BackClick : CreateProposalScreenIntent()

    data class SelectProposalDuration(val duration: Duration) : CreateProposalScreenIntent()
}
