package com.example.votekt.data.repository_impl

import android.util.Log
import by.alexandr7035.ethereum.model.Address
import com.example.votekt.BuildConfig
import by.alexandr7035.web3j_contracts.VotingContract
import com.example.votekt.data.cache.ProposalEntity
import com.example.votekt.data.cache.ProposalWithTransactions
import com.example.votekt.data.cache.ProposalsDao
import com.example.votekt.data.cache.canVote
import com.example.votekt.data.cache.isDeployFailed
import com.example.votekt.data.cache.isDeployPending
import com.example.votekt.data.cache.isVoteFailed
import com.example.votekt.data.cache.isVotePending
import com.example.votekt.data.cache.shouldBeDeployed
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.transactions.TransactionHash
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.domain.votings.CreateProposal
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingData
import com.example.votekt.domain.votings.VotingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.response.NoOpProcessor
import java.util.UUID

class VotingRepositoryImpl(
    web3j: Web3j,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
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

    override fun getProposalById(id: String): Flow<Proposal> {
        return proposalsDao.observeProposalByUuid(id).flowOn(dispatcher).map {
            it.mapToDomain()
        }
    }

    override fun getProposals(): Flow<List<Proposal>> {
        return proposalsDao.observeProposals()
            .flowOn(dispatcher)
            .onStart {
                withContext(dispatcher) {
                    syncProposalsWithContract()
                }
            }
            .map { list ->
                list.map { it.mapToDomain() }
            }
    }

    override suspend fun createProposal(req: CreateProposal): OperationResult<String> = withContext(dispatcher) {
        return@withContext OperationResult.runWrapped {
            val uuid = UUID.randomUUID().toString()
            Log.d("DEBUG_TAG", "Create proposal with ${uuid}")

            val tx = votingContract.createProposal(
                uuid,
                req.title,
                req.desc,
                req.duration.getDurationInDays().toBigInteger()
            ).send()

            transactionRepository.addNewTransaction(
                transactionType = TransactionType.CREATE_PROPOSAL,
                transactionHash = TransactionHash(tx.transactionHash)
            )

            proposalsDao.cacheProposal(
                ProposalEntity(
                    uuid = uuid,
                    isDraft = true,
                    isSelfCreated = true,
                    title = req.title,
                    description = req.desc,
                    deployTransactionHash = tx.transactionHash,
                    createdAt = System.currentTimeMillis(),
                    creatorAddress = accountRepository.getSelfAddress().value,
                )
            )

            tx.transactionHash
        }
    }

    override suspend fun voteOnProposal(proposalNumber: Int, vote: VoteType): OperationResult<TransactionHash> = withContext(dispatcher) {
        return@withContext OperationResult.runWrapped {
            val isFor = when (vote) {
                VoteType.VOTE_FOR -> true
                VoteType.VOTE_AGAINST -> false
            }

            val tx = votingContract.vote(proposalNumber.toBigInteger(), isFor).send()

            transactionRepository.addNewTransaction(
                transactionType = TransactionType.VOTE,
                transactionHash = TransactionHash(tx.transactionHash)
            )

            proposalsDao.updateProposalVote(
                proposalNumber = proposalNumber,
                supported = when (vote) {
                    VoteType.VOTE_FOR -> true
                    VoteType.VOTE_AGAINST -> false
                }
            )

            TransactionHash(tx.transactionHash)
        }
    }

    override suspend fun syncProposalsWithContract(): Unit = withContext(dispatcher) {
        val contractOwner = votingContract.owner().send()

        val contractProposals = votingContract.proposalsList
            .send()
            .map { it as VotingContract.ProposalRaw }

        contractProposals.forEach { raw ->
            val cached = proposalsDao.getProposalByUuid(uuid = raw.uuid)
            cached?.let {
                proposalsDao.updateProposal(
                    cached.copy(
                        number = raw.number.toInt(),
                        votesFor = raw.votesFor.toInt(),
                        isDraft = false,
                        votesAgainst = raw.votesAgainst.toInt(),
                        expiresAt = raw.expirationTime.toLong() * 1000L,
                        creatorAddress = contractOwner,
                        createdAt = raw.creationTime.toLong() * 1000L,
                        // TODO update contract with self vote
                    )
                )
            } ?: run {
                proposalsDao.cacheProposal(
                    ProposalEntity(
                        uuid = raw.uuid,
                        number = raw.number.toInt(),
                        isDraft = false,
                        votesFor = raw.votesFor.toInt(),
                        votesAgainst = raw.votesAgainst.toInt(),
                        expiresAt = raw.expirationTime.toLong() * 1000L,
                        createdAt = raw.creationTime.toLong() * 1000L,
                        deployTransactionHash = null,
                        creatorAddress = contractOwner,
                        title = raw.title,
                        description = raw.description,
                        // TODO update contract with self vote
                    )
                )
            }
        }

        proposalsDao.cleanUpProposals(remainingProposals = contractProposals.map { it.uuid })
    }

    private suspend fun ProposalWithTransactions.mapToDomain(): Proposal = withContext(dispatcher) {
        return@withContext if (!proposal.isDraft) {
            Proposal.Deployed(
                uuid = proposal.uuid,
                title = proposal.title,
                description = proposal.description,
                proposalNumber = proposal.number!!,
                expirationTime = proposal.expiresAt!!,
                creatorAddress = Address(proposal.creatorAddress),
                isSelfCreated = proposal.isSelfCreated,
                votingData = VotingData(
                    votesFor = proposal.votesFor,
                    votesAgainst = proposal.votesAgainst,
                    // TODO mapper
                    selfVote = when (proposal.selfVote) {
                        true -> VoteType.VOTE_FOR
                        false -> VoteType.VOTE_AGAINST
                        null -> null
                    }
                ),
                // TODO refactoring
                voteTransaction = voteTransaction?.mapToData(),
                canVote = canVote(),
                isVotePending = isVotePending(),
                isVoteFailed = isVoteFailed(),
            )
        } else {
            Proposal.Draft(
                uuid = proposal.uuid,
                title = proposal.title,
                description = proposal.description,
                creatorAddress = Address(proposal.creatorAddress),
                isSelfCreated = proposal.isSelfCreated,
                // TODO refactoring
                deploymentTransaction = deploymentTransaction?.mapToData(),
                shouldBeDeployed = shouldBeDeployed(),
                isDeployFailed = isDeployFailed(),
                isDeployPending = isDeployPending(),
            )
        }
    }
}
