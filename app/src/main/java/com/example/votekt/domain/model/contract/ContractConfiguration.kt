package com.example.votekt.domain.model.contract

import by.alexandr7035.ethereum.model.Wei

data class ContractConfiguration(
    val createProposalFee: Wei,
    val proposalTitleLimit: Int,
    val proposalDescriptionLimit: Int
)
