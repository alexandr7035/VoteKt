package com.example.votekt.data.repository_impl

import android.util.Log
import by.alexandr7035.contracts.VoteKtContractV1
import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.EthTransactionInput
import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import by.alexandr7035.utils.asEthereumAddressString
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
import com.example.votekt.domain.votings.CreateProposal
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.domain.votings.VotingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import pm.gnosis.model.Solidity
import pm.gnosis.utils.hexToByteArray
import java.util.UUID

class VotingContractRepositoryImpl(
    private val web3: EthereumClient,
    private val contractAddress: String,
    private val accountRepository: AccountRepository,
    private val proposalsDao: ProposalsDao,
    private val dispatcher: CoroutineDispatcher,
    private val sendTransactionRepository: SendTransactionRepository,
) : VotingContractRepository {
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
                    contractAddress = org.kethereum.model.Address(contractAddress),
                    contractInput = EthTransactionInput(solidityInput.hexToByteArray()),
                    proposalUuid = uuid
                )
            )
        }
    }

    override suspend fun voteOnProposal(proposalNumber: Int, vote: VoteType) = withContext(dispatcher) {
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
                    contractAddress = org.kethereum.model.Address(contractAddress),
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
        val ownerInput = VoteKtContractV1.Owner.encode()
        val ownerRes = web3.sendEthCall(
            to = org.kethereum.model.Address(contractAddress),
            input = ownerInput
        )
        val decodedOwner = VoteKtContractV1.Owner.decode(ownerRes).param0.value.asEthereumAddressString()

        val contractCallRes = VoteKtContractV1.GetProposalsList.encode()
        val callRes = web3.sendEthCall(
            to = org.kethereum.model.Address(contractAddress),
            input = contractCallRes,
        )
        val decoded = VoteKtContractV1.GetProposalsList.decode(callRes).param0.items

        decoded.forEach { raw ->
            val cached = proposalsDao.getProposalByUuid(uuid = raw.uuid.value)
            cached?.let {
                proposalsDao.updateProposal(
                    cached.copy(
                        number = raw.number.value.toInt(),
                        creatorAddress = decodedOwner,
                        votesFor = raw.votesfor.value.toInt(),
                        isDraft = false,
                        votesAgainst = raw.votesagainst.value.toInt(),
                        createdAt = raw.creationtime.value.toLong() * 1000L,
                        expiresAt = raw.expirationtime.value.toLong() * 1000L,
                        // TODO update contract with self vote
                    )
                )
            } ?: run {
                proposalsDao.cacheProposal(
                    ProposalEntity(
                        uuid = raw.uuid.value,
                        number = raw.number.value.toInt(),
                        creatorAddress = decodedOwner,
                        title = raw.title.value,
                        description = raw.description.value,
                        isDraft = false,
                        votesFor = raw.votesfor.value.toInt(),
                        votesAgainst = raw.votesagainst.value.toInt(),
                        expiresAt = raw.expirationtime.value.toLong() * 1000L,
                        createdAt = raw.creationtime.value.toLong() * 1000L,
                        deployTransactionHash = null,
                        // TODO update contract with self vote
                        selfVote = null,
                        selfVoteTransactionHash = null,
                    )
                )
            }
        }

        proposalsDao.cleanUpProposals(remainingProposals = decoded.map { it.uuid.value })
    }

    override suspend fun handleContractEvent(event: EthereumEvent.ContractEvent) {
        println("${TAG} wss topic ${event.eventTopic}")
        println("${TAG} contact topic ${VoteKtContractV1.Events.VoteCasted.EVENT_ID}")
        when (event.eventTopic) {
            VoteKtContractV1.Events.VoteCasted.EVENT_ID -> {
                val eventData = VoteKtContractV1.Events.VoteCasted.decode(
                    topics = listOf(event.eventTopic),
                    data = event.encodedData
                )
                processVoteCastedEvent(eventData)
            }

            else -> {
                println("${TAG} unknown contract event")
            }
        }
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

    private suspend fun processVoteCastedEvent(eventData: VoteKtContractV1.Events.VoteCasted.Arguments) {
        Log.d(TAG, "Vote casted ${eventData}")
        proposalsDao.addVoteToProposal(
            inFavor = eventData.infavor.value,
            proposalNumber = eventData.proposalnumber.value.toInt()
        )
    }

    companion object {
        private const val TAG = "CONTRACT_TAG"
    }
}
