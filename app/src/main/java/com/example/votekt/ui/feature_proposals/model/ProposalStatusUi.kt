package com.example.votekt.ui.feature_proposals.model

import androidx.compose.ui.graphics.Color
import com.example.votekt.domain.votings.Proposal

enum class ProposalStatusUi(val title: String, val color: Color) {
    DRAFT("Draft", Color(0xFFFF9800)),
    ON_VOTE("On Vote", Color(0xFF5E5E5E)),
    FINISHED("Finished", Color(0xFF4CAF50)),
}

fun Proposal.getStatusUi(): ProposalStatusUi {
    return when (this) {
        is Proposal.Draft -> ProposalStatusUi.DRAFT
        is Proposal.Deployed -> {
            if (isFinished) {
                ProposalStatusUi.FINISHED
            } else {
                ProposalStatusUi.ON_VOTE
            }
        }
    }
}