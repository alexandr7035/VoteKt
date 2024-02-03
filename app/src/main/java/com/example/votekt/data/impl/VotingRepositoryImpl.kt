package com.example.votekt.data.impl

import android.util.Log
import com.example.votekt.BuildConfig
import com.example.votekt.contracts.VotingContract
import com.example.votekt.data.AppError
import com.example.votekt.data.OperationResult
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.VoterAddress
import com.example.votekt.data.VotingRepository
import com.example.votekt.data.helpers.executeWeb3Call
import com.example.votekt.data.model.CreateProposalReq
import com.example.votekt.data.model.Proposal
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TransactionType
import com.example.votekt.data.web3_core.transactions.TxStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
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
            Proposal(
                id = res.id.toLong(),
                title = res.title,
                description = res.description,
                votesAgainst = res.votesAgainst.toInt(),
                votesFor = res.votesFor.toInt(),
                expirationTime = res.expirationTime.toLong() * 1000,
            )
        }
    }

    override suspend fun getProposals(): OperationResult<List<Proposal>> {
        return executeWeb3Call {
            val raw = votingContract.proposalsList.send()
            Log.d("DEBUG_TAG", "${raw}")
            raw.map { it as VotingContract.ProposalRaw }.map { rawProposal ->
                Proposal(
                    id = rawProposal.id.toLong(),
                    title = rawProposal.title,
                    description = rawProposal.description,
                    votesAgainst = rawProposal.votesAgainst.toInt(),
                    votesFor = rawProposal.votesFor.toInt(),
                    expirationTime = rawProposal.expirationTime.toLong() * 1000
                )
            }
        }
    }


    override suspend fun createProposal(req: CreateProposalReq): OperationResult<String> = withContext(dispatcher) {
        try {
            val tx = votingContract.createProposal(
                req.title,
                req.desc,
                req.duration.getDurationInDays().toBigInteger()
            ).send()

            transactionRepository.cacheTransaction(
                Transaction(
                    type = TransactionType.CREATE_PROPOSAL,
                    hash = tx.transactionHash,
                    dateSent = System.currentTimeMillis(),
                    status = TxStatus.PENDING
                )
            )

            return@withContext OperationResult.Success(tx.transactionHash)
        } catch (e: Exception) {
            return@withContext OperationResult.Failure(AppError.UnknownError(e.toString()))
        }
    }

    override suspend fun voteOnProposal(proposalId: Long, isFor: Boolean): OperationResult<String> = withContext(dispatcher) {
        try {
            val tx = votingContract.vote(proposalId.toBigInteger(), isFor).send()

            transactionRepository.cacheTransaction(
                Transaction(
                    type = TransactionType.VOTE,
                    hash = tx.transactionHash,
                    dateSent = System.currentTimeMillis(),
                    status = TxStatus.PENDING
                )
            )

            return@withContext OperationResult.Success(tx.transactionHash)

        } catch (e: Exception) {
            return@withContext OperationResult.Failure(AppError.UnknownError(e.toString()))
        }
    }

    override suspend fun getVotedAddresses(): List<VoterAddress> {
        delay(3000)

        val addresses = List(12) { index ->
            VoterAddress(address = "0x32424535esdf3242344bc${index}", votedFor = index % 2 == 0)
        }

        return addresses
    }
}