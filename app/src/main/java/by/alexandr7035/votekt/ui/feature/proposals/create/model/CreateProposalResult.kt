package by.alexandr7035.votekt.ui.feature.proposals.create.model

import by.alexandr7035.votekt.domain.core.AppError
import by.alexandr7035.votekt.domain.core.Uuid

data class CreateProposalResult(
    val proposalUuid: Uuid?,
    val error: AppError?,
)
