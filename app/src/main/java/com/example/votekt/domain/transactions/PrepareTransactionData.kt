package com.example.votekt.domain.transactions

import by.alexandr7035.ethereum.model.EthTransactionInput
import by.alexandr7035.ethereum.model.Wei
import org.kethereum.model.Address

sealed class PrepareTransactionData {
    data class SendValue(
        val amount: Wei,
        val receiver: Address,
    ): PrepareTransactionData()

    sealed class ContractInteraction(
        open val contractAddress: Address,
        open val contractInput: EthTransactionInput,
    ): PrepareTransactionData() {
        data class CreateProposal(
            override val contractAddress: Address,
            override val contractInput: EthTransactionInput,
            val proposalUuid: String,
        ): ContractInteraction(contractAddress, contractInput)

        data class VoteOnProposal(
            override val contractAddress: Address,
            override val contractInput: EthTransactionInput,
            val proposalNumber: Int,
            val vote: Boolean,
        ): ContractInteraction(contractAddress, contractInput)
    }

    val to: Address
        get() = when (this) {
            is ContractInteraction -> this.contractAddress
            is SendValue -> this.receiver
        }

    val transactionType
        get() = when (this) {
            is ContractInteraction.CreateProposal -> TransactionType.CREATE_PROPOSAL
            is SendValue -> TransactionType.PAYMENT
            is ContractInteraction.VoteOnProposal -> TransactionType.VOTE
        }
}
