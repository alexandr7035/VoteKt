package com.example.votekt.domain.transactions

import by.alexandr7035.ethereum.model.EthTransactionInput
import by.alexandr7035.ethereum.model.Wei
import org.kethereum.model.Address

sealed class PrepareTransactionData {
    data class SendValue(
        val amount: Wei,
        val receiver: Address,
    ): PrepareTransactionData()

    data class ContractInteraction(
        val operation: TransactionType,
        val contractAddress: Address,
        val contractInput: EthTransactionInput,
    ): PrepareTransactionData()
}
