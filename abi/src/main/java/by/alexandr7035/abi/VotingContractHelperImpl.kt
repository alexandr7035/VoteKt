package by.alexandr7035.abi

import VotingAbiTransactionDecoder
import VotingAbiTransactionGenerator
import by.alexandr7035.ethereum.model.EthTransactionInput
import org.kethereum.model.Address
import java.math.BigInteger

class VotingContractHelperImpl : VotingContractHelper {
    private val decoder = VotingAbiTransactionDecoder()
    private val generator = VotingAbiTransactionGenerator(
        // Doesn't matter since we use generated ABI only for tx input
        address = Address("")
    )

    override fun getVotingTransactionInput(
        proposalNumber: BigInteger,
        vote: Boolean
    ): EthTransactionInput {
        val input =  generator.vote(
            inFavor = vote,
            proposalNumber = proposalNumber
        ).input

        return EthTransactionInput(input)
    }

    override fun getCreateProposalTransactionInput(
        uuid: String,
        title: String,
        description: String,
        durationInDays: BigInteger
    ): EthTransactionInput {
        val input =  generator.createProposal(
            uuid = uuid,
            title = title,
            description = description,
            durationInDays = durationInDays,
        ).input

        return EthTransactionInput(input)
    }
}