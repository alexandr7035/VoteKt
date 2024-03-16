package by.alexandr7035.abi

import by.alexandr7035.ethereum.model.EthTransactionInput
import java.math.BigInteger

interface VotingContractHelper {
    fun getVotingTransactionInput(
        proposalNumber: BigInteger,
        vote: Boolean
    ): EthTransactionInput

    fun getCreateProposalTransactionInput(
        uuid: String,
        title: String,
        description: String,
        durationInDays: BigInteger
    ): EthTransactionInput
}