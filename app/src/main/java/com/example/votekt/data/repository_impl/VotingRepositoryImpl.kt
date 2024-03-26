package com.example.votekt.data.repository_impl

import android.util.Log
import by.alexandr7035.contracts.VoteKtContractV1
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.EthTransactionInput
import by.alexandr7035.web3j_contracts.VotingContract
import com.example.votekt.BuildConfig
import com.example.votekt.data.cache.ProposalEntity
import com.example.votekt.data.cache.ProposalWithTransactions
import com.example.votekt.data.cache.ProposalsDao
import com.example.votekt.data.cache.mapDeployStatus
import com.example.votekt.data.cache.mapSelfVote
import com.example.votekt.data.cache.mapVoteStatus
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.transactions.PrepareTransactionData
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.TransactionRepository
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
import pm.gnosis.model.Solidity
import pm.gnosis.utils.hexToByteArray
import java.util.UUID

class VotingRepositoryImpl(
    web3j: Web3j,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val proposalsDao: ProposalsDao,
    private val dispatcher: CoroutineDispatcher,
    private val sendTransactionRepository: SendTransactionRepository
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

    override suspend fun createProposal(req: CreateProposal): OperationResult<Unit> = withContext(dispatcher) {
        return@withContext OperationResult.runWrapped {
            val uuid = UUID.randomUUID().toString()

            val solidityInput = VoteKtContractV1.CreateProposal.encode(
                uuid = Solidity.String(uuid),
                title = Solidity.String(req.title),
                description = Solidity.String(req.desc),
                durationInDays = Solidity.UInt256(req.duration.getDurationInDays().toBigInteger())
            )

            proposalsDao.cacheProposal(
                ProposalEntity(
                    uuid = uuid,
                    isDraft = true,
                    isSelfCreated = true,
                    title = req.title,
                    description = req.desc,
                    deployTransactionHash = null,
                    createdAt = System.currentTimeMillis(),
                    creatorAddress = accountRepository.getSelfAddress().hex,
                )
            )

            sendTransactionRepository.requirePrepareTransaction(
                data = PrepareTransactionData.ContractInteraction.CreateProposal(
                    contractAddress = org.kethereum.model.Address(votingContract.contractAddress),
                    contractInput = EthTransactionInput(solidityInput.hexToByteArray()),
                    proposalUuid = uuid
                )
            )
        }
    }

    override suspend fun voteOnProposal(proposalNumber: Int, vote: VoteType): OperationResult<Unit> = withContext(dispatcher) {
        return@withContext OperationResult.runWrapped {
            val isFor = when (vote) {
                VoteType.VOTE_FOR -> true
                VoteType.VOTE_AGAINST -> false
            }

            val solidityInput = VoteKtContractV1.Vote.encode(
                proposalNumber = Solidity.UInt256(proposalNumber.toBigInteger()),
                inFavor = Solidity.Bool(isFor)
            )

            sendTransactionRepository.requirePrepareTransaction(
                data = PrepareTransactionData.ContractInteraction.VoteOnProposal(
                    contractAddress = org.kethereum.model.Address(votingContract.contractAddress),
                    contractInput = EthTransactionInput(solidityInput.hexToByteArray()),
                    proposalNumber = proposalNumber,
                    vote = when (vote) {
                        VoteType.VOTE_FOR -> true
                        VoteType.VOTE_AGAINST -> false
                    },
                )
            )
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
                        creatorAddress = contractOwner,
                        votesFor = raw.votesFor.toInt(),
                        isDraft = false,
                        votesAgainst = raw.votesAgainst.toInt(),
                        createdAt = raw.creationTime.toLong() * 1000L,
                        expiresAt = raw.expirationTime.toLong() * 1000L,
                        // TODO update contract with self vote
                    )
                )
            } ?: run {
                proposalsDao.cacheProposal(
                    ProposalEntity(
                        uuid = raw.uuid,
                        number = raw.number.toInt(),
                        creatorAddress = contractOwner,
                        title = raw.title,
                        description = raw.description,
                        isDraft = false,
                        votesFor = raw.votesFor.toInt(),
                        votesAgainst = raw.votesAgainst.toInt(),
                        expiresAt = raw.expirationTime.toLong() * 1000L,
                        createdAt = raw.creationTime.toLong() * 1000L,
                        deployTransactionHash = null,
                        // TODO update contract with self vote
                        selfVote = null,
                        selfVoteTransactionHash = null,
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
                    selfVote = mapSelfVote()
                ),
                voteTransaction = voteTransaction?.mapToData(),
                selfVoteStatus = mapVoteStatus(),
            )
        } else {
            Proposal.Draft(
                uuid = proposal.uuid,
                title = proposal.title,
                description = proposal.description,
                creatorAddress = Address(proposal.creatorAddress),
                isSelfCreated = proposal.isSelfCreated,
                deploymentTransaction = deploymentTransaction?.mapToData(),
                deployStatus = mapDeployStatus(),
            )
        }
    }

    companion object {
        private const val TAG = "CONTRACT_TAG"
    }
}
