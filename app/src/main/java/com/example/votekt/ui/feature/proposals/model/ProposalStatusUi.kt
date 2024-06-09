package com.example.votekt.ui.feature.proposals.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.votekt.R
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.theme.Gray50
import com.example.votekt.ui.theme.SemanticColors

enum class ProposalStatusUi(@StringRes val title: Int, val color: Color) {
    DRAFT(R.string.draft, SemanticColors.AppWarningColor),
    ON_VOTE(R.string.on_vote, Gray50),
    SUPPORTED(R.string.supported, SemanticColors.AppPositiveColor),
    NOT_SUPPORTED(R.string.not_supported, SemanticColors.AppNegativeColor)
}

fun Proposal.getStatusUi(): ProposalStatusUi {
    return when (this) {
        is Proposal.Draft -> ProposalStatusUi.DRAFT
        is Proposal.Deployed -> {
            if (isFinished) {
                if (this.votingData.isSupported) {
                    ProposalStatusUi.SUPPORTED
                } else {
                    ProposalStatusUi.NOT_SUPPORTED
                }
            } else {
                ProposalStatusUi.ON_VOTE
            }
        }
    }
}
