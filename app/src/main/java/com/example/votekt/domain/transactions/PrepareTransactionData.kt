package com.example.votekt.domain.transactions

import by.alexandr7035.ethereum.model.EthTransactionInput
import by.alexandr7035.ethereum.model.WEI
import by.alexandr7035.ethereum.model.Wei
import org.kethereum.model.Address
import java.math.BigInteger

sealed class PrepareTransactionData(
    open val value: Wei
) {
    data class SendValue(
        override val value: Wei,
        val receiver: Address,
    ): PrepareTransactionData(value)

    sealed class ContractInteraction(
        open val contractAddress: Address,
        open val contractInput: EthTransactionInput,
        override val value: Wei,
    ): PrepareTransactionData(value) {
        data class CreateProposal(
            override val contractAddress: Address,
            override val contractInput: EthTransactionInput,
            override val value: Wei,
            val proposalUuid: String,
        ): ContractInteraction(contractAddress, contractInput, value)

        data class VoteOnProposal(
            override val contractAddress: Address,
            override val contractInput: EthTransactionInput,
            override val value: Wei = 0.WEI,
            val proposalNumber: Int,
            val vote: Boolean,
        ): ContractInteraction(contractAddress, contractInput, value)
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
