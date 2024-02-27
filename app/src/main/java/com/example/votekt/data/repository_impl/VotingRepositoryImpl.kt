package com.example.votekt.data.repository_impl

import android.util.Log
import com.example.votekt.BuildConfig
import com.example.votekt.contracts.VotingContract
import com.example.votekt.data.cache.ProposalEntity
import com.example.votekt.data.cache.ProposalWithTransaction
import com.example.votekt.data.cache.ProposalsDao
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.transactions.TransactionHash
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.domain.transactions.TransactionStatus
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.domain.votings.CreateProposal
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.response.NoOpProcessor

class VotingRepositoryImpl(
    web3j: Web3j,
    private val transactionRepository: TransactionRepository,
    private val proposalsDao: ProposalsDao,
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

    override fun getProposalById(id: Int): Flow<Proposal> {
        return proposalsDao.getProposalById(id).flowOn(dispatcher).map {
            it.mapToDomain()
        }
    }

    override fun getProposals(): Flow<List<Proposal>> {
        return proposalsDao.getProposals().flowOn(dispatcher).map { list ->
            list.map { it.mapToDomain() }
        }
    }

    override suspend fun createProposal(req: CreateProposal): OperationResult<String> = withContext(dispatcher) {
        return@withContext OperationResult.runWrapped {
            val tx = votingContract.createProposal(
                req.title,
                req.desc,
                req.duration.getDurationInDays().toBigInteger()
            ).send()

            transactionRepository.addNewTransaction(
                type = TransactionType.CREATE_PROPOSAL,
                transactionHash = TransactionHash(tx.transactionHash)
            )

            proposalsDao.cacheProposal(
                ProposalEntity(
                    title = req.title,
                    description = req.desc,
                    deployTransactionHash = tx.transactionHash,
                    createdAt = System.currentTimeMillis()
                )
            )

            tx.transactionHash
        }
    }

    override suspend fun voteOnProposal(proposalId: Long, vote: VoteType): OperationResult<TransactionHash> = withContext(dispatcher) {
        return@withContext OperationResult.runWrapped {
            val isFor = when (vote) {
                VoteType.VOTE_FOR -> true
                VoteType.VOTE_AGAINST -> false
            }

            val tx = votingContract.vote(proposalId.toBigInteger(), isFor).send()

            transactionRepository.addNewTransaction(
                type = TransactionType.VOTE,
                transactionHash = TransactionHash(tx.transactionHash)
            )

            TransactionHash(tx.transactionHash)
        }
    }

    private fun ProposalWithTransaction.mapToDomain(): Proposal {
        return when (deploymentTransaction?.status) {
            TransactionStatus.MINED -> {
                Proposal.Deployed(
                    id = proposal.id,
                    title = proposal.title,
                    description = proposal.description,
//                    blockchainId = proposal.remoteId!!,
                    blockchainId = 0,
//                    expirationTime = proposal.expiresAt!!,
                    expirationTime = 0,
                    votesFor = proposal.votesFor,
                    votesAgainst = proposal.votesAgainst,
                )
            }
            else -> {
                Proposal.Draft(
                    id = proposal.id,
                    deploymentTransactionHash = deploymentTransaction?.hash?.let { TransactionHash(it) },
                    title = proposal.title,
                    description = proposal.description
                )
            }
        }
    }
}