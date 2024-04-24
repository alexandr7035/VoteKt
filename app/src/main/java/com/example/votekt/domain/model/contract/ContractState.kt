package com.example.votekt.domain.model.contract

import org.kethereum.model.Address

data class ContractState(
    val address: Address,
    val owner: Address,
    val maxProposals: Int,
    val currentProposals: Int,
    val fullPercentage: Float,
    val pendingProposals: Int,
    val supportedProposals: Int,
    val notSupportedProposals: Int,
)
