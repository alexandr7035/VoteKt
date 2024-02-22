package com.example.votekt.data.repository_impl

import android.util.Log
import com.example.votekt.BuildConfig
import com.example.votekt.contracts.VotingContract
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.domain.core.AppError
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.votings.VotingRepository
import com.example.votekt.data.helpers.executeWeb3Call
import com.example.votekt.domain.votings.CreateProposal
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.domain.transactions.TransactionHash
import com.example.votekt.domain.transactions.TransactionStatus
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.response.NoOpProcessor
import java.math.BigInteger

class VotingRepositoryImpl(
    web3j: Web3j,
    private val transactionRepository: TransactionRepository,
    private val dispatcher: CoroutineDispatcher,
) : VotingRepository {
    private val votingContract: VotingContract

    init {
        val credentials = Bip44WalletUtils.loadBip44Credentials("", BuildConfig.TEST_MNEMONIC)
        Log.d("DEBUG_TAG", "KEY PAIR for ${credentials.address} created")

        // NoOpProcessor provides an EmptyTransactionReceipt to clients which only contains the transaction hash.
        // This is for clients who do not want web3j to perform any polling for a transaction receipt.
        val receiptProcessor = NoOpProcessor(web3j)

        // val txManager = RawTransactionManager(web3j, credentials)
        val txManager = RawTransactionManager(
            web3j,
            credentials,
            BuildConfig.CHAIN_ID.toLong(),
            receiptProcessor
        )

        // TODO gas estimation
        votingContract = VotingContract.load(
            BuildConfig.CONTRACT_ADDRESS, web3j, txManager, DefaultGasProvider()
        )
    }

    override suspend fun getProposalById(id: Long): OperationResult<Proposal> {
        return executeWeb3Call {
            val res = votingContract.getProposalDetails(BigInteger.valueOf(id)).send()
            res.mapToDomain()
        }
    }

    override suspend fun getProposals(): OperationResult<List<Proposal>> {
        return executeWeb3Call {
            val raw = votingContract.proposalsList.send()
            raw.map { it as VotingContract.ProposalRaw }.map() { rawProposal ->
                rawProposal.mapToDomain()
            }
        }
    }


    override suspend fun createProposal(req: CreateProposal): OperationResult<String> = withContext(dispatcher) {
        try {
            val tx = votingContract.createProposal(
                req.title,
                req.desc,
                req.duration.getDurationInDays().toBigInteger()
            ).send()

            transactionRepository.addNewTransaction(
                TransactionDomain(
                    type = TransactionType.CREATE_PROPOSAL,
                    hash = tx.transactionHash,
                    dateSent = System.currentTimeMillis(),
                    status = TransactionStatus.PENDING,
                    gasFee = null
                )
            )

            return@withContext OperationResult.Success(tx.transactionHash)
        } catch (e: Exception) {
            return@withContext OperationResult.Failure(AppError.fromThrowable(e))
        }
    }

    override suspend fun voteOnProposal(proposalId: Long, voteType: VoteType): OperationResult<TransactionHash> = withContext(dispatcher) {
        try {
            val isFor = when (voteType) {
                VoteType.VOTE_FOR -> true
                VoteType.VOTE_AGAINST -> false
            }

            val tx = votingContract.vote(proposalId.toBigInteger(), isFor).send()

            transactionRepository.addNewTransaction(
                TransactionDomain(
                    type = TransactionType.VOTE,
                    hash = tx.transactionHash,
                    dateSent = System.currentTimeMillis(),
                    status = TransactionStatus.PENDING,
                    gasFee = null
                )
            )

            return@withContext OperationResult.Success(TransactionHash(tx.transactionHash))

        } catch (e: Exception) {
            return@withContext OperationResult.Failure(AppError.fromThrowable(e))
        }
    }

    private fun VotingContract.ProposalRaw.mapToDomain() = Proposal(
        id = id.toLong(),
        title = title,
        description = description,
        votingData = VotingData(
            votesFor = votesFor.toInt(),
            votesAgainst = votesAgainst.toInt(),
            // TODO
            selfVote = null
        ),
        expirationTime = expirationTime.toLong() * 1000,
    )
}