package com.example.votekt.data.repository_impl

import android.util.Log
import by.alexandr7035.contracts.VoteKtContractV1
import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.EthTransactionInput
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import by.alexandr7035.utils.asEthereumAddressString
import com.cioccarellia.ksprefs.KsPrefs
import com.example.votekt.data.cache.PrefKeys
import com.example.votekt.data.cache.ProposalEntity
import com.example.votekt.data.cache.ProposalWithTransactions
import com.example.votekt.data.cache.ProposalsDao
import com.example.votekt.data.cache.mapDeployStatus
import com.example.votekt.data.cache.mapSelfVote
import com.example.votekt.data.cache.mapVoteStatus
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.core.BlockchainActionStatus
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.core.Uuid
import com.example.votekt.domain.model.contract.ContractConfiguration
import com.example.votekt.domain.model.contract.ContractState
import com.example.votekt.domain.transactions.PrepareTransactionData
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.model.contract.CreateDraftProposal
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.domain.votings.VotingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pm.gnosis.model.Solidity
import pm.gnosis.model.safeIntValueExact
import pm.gnosis.utils.hexToByteArray
import java.math.BigInteger
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class VotingContractRepositoryImpl(
    private val web3: EthereumClient,
    private val contractAddress: String,
    private val accountRepository: AccountRepository,
    private val proposalsDao: ProposalsDao,
    private val dispatcher: CoroutineDispatcher,
    private val sendTransactionRepository: SendTransactionRepository,
    private val ksPrefs: KsPrefs,
) : VotingContractRepository {
    override fun getProposalById(id: String): Flow<Proposal> {
        return proposalsDao.observeProposalByUuid(id).flowOn(dispatcher).map {
            it.mapToDomain()
        }
    }

    override fun getContractState(): Flow<ContractState> {
        return getProposals()
            .map {
                val filtered = it.filterIsInstance<Proposal.Deployed>()
                val currentCount = filtered.size

                val max = ksPrefs.pull<Int>(PrefKeys.CONTRACT_MAX_PROPOSALS)
                val percentage = (currentCount / max.toFloat())

                val pending = filtered.count { proposal ->
                    proposal.isFinished.not()
                }

                val supported = filtered.count { proposal ->
                    proposal.isFinished && proposal.votingData.isSupported
                }

                val notSupported = filtered.count { proposal ->
                    proposal.isFinished && proposal.votingData.isSupported.not()
                }

                ContractState(
                    address = org.kethereum.model.Address(contractAddress),
                    owner = org.kethereum.model.Address(ksPrefs.pull(PrefKeys.CONTRACT_CREATOR_ADDRESS)),
                    maxProposals = max,
                    currentProposals = currentCount,
                    fullPercentage = percentage,
                    supportedProposals = supported,
                    notSupportedProposals = notSupported,
                    pendingProposals = pending,
                )
            }
    }

    override fun getContractConfiguration(): ContractConfiguration {
        val createProposalFee = Wei(
            ksPrefs.pull<String>(PrefKeys.CONTRACT_CREATE_PROPOSAL_FEE)
        )

        val titleMaxLength = ksPrefs.pull<Int>(PrefKeys.CONTRACT_MAX_PROPOSAL_TITLE_LENGTH)
        val descMaxLength = ksPrefs.pull<Int>(PrefKeys.CONTRACT_MAX_DESCRIPTION_LENGTH)

        return ContractConfiguration(
            createProposalFee = createProposalFee,
            proposalTitleLimit = titleMaxLength,
            proposalDescriptionLimit = descMaxLength,
        )
    }

    override fun getProposals(): Flow<List<Proposal>> {
        return proposalsDao.observeProposals()
            .flowOn(dispatcher)
            .map { list ->
                list.map { it.mapToDomain() }
            }
    }

    override suspend fun createDraftProposal(req: CreateDraftProposal): OperationResult<Uuid> = withContext(dispatcher) {
        return@withContext OperationResult.runWrapped {
            val uuid = UUID.randomUUID().toString()

            proposalsDao.cacheProposal(
                ProposalEntity(
                    uuid = uuid,
                    isDraft = true,
                    title = req.title,
                    description = req.desc,
                    deployTransactionHash = null,
                    createdAt = System.currentTimeMillis(),
                    creatorAddress = accountRepository.getSelfAddress().hex,
                    durationInHours = req.duration.inWholeHours.toInt(),
                )
            )

            Uuid(uuid)
        }
    }

    override suspend fun deployDraftProposal(proposalUuid: Uuid): OperationResult<Unit> = withContext(dispatcher) {
        return@withContext OperationResult.runWrapped {
            val cachedProposal = proposalsDao.getProposalByUuid(proposalUuid.value)
            require(cachedProposal != null) { "Proposal draft not found" }
            Log.d(TAG, "deploy draft proposal ${cachedProposal}")

            val solidityInput = VoteKtContractV1.CreateProposal.encode(
                uuid = Solidity.String(proposalUuid.value),
                title = Solidity.String(cachedProposal.title),
                description = Solidity.String(cachedProposal.description),
                durationInHours = cachedProposal.durationInHours?.let {
                    Solidity.UInt256(it.toBigInteger())
                } ?: kotlin.run {
                    Solidity.UInt256(BigInteger.ONE)
                }
            )

            sendTransactionRepository.requirePrepareTransaction(
                data = PrepareTransactionData.ContractInteraction.CreateProposal(
                    contractAddress = org.kethereum.model.Address(contractAddress),
                    contractInput = EthTransactionInput(solidityInput.hexToByteArray()),
                    proposalUuid = proposalUuid.value,
                    value = getContractConfiguration().createProposalFee,
                )
            )
        }
    }

    override suspend fun deleteDraftProposal(proposalUuid: Uuid): OperationResult<Unit> = withContext(dispatcher) {
        return@withContext OperationResult.runWrapped {
            val cachedProposalData = proposalsDao
                .getProposalWithTransactionsByUuid(proposalUuid.value)
                ?.mapToDomain()
            require(cachedProposalData != null) { "Proposal draft not found" }
            require(cachedProposalData is Proposal.Draft) { "Proposal is not draft" }
            require(cachedProposalData.deployStatus is BlockchainActionStatus.NotCompleted) { "Proposal deploy must be not completed" }

            proposalsDao.deleteProposal(proposalUuid.value)
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
        updateContractConfiguration()

        val getProposalsCall = VoteKtContractV1.GetProposalsList.encode()
        val getProposalRes = web3.sendEthCall(
            to = org.kethereum.model.Address(contractAddress),
            input = getProposalsCall,
        )
        val decodedGetProposalRes = VoteKtContractV1.GetProposalsList.decode(getProposalRes).param0.items

        decodedGetProposalRes.forEach { raw -> updateProposalInCache(raw) }
        proposalsDao.cleanUpProposals(remainingProposals = decodedGetProposalRes.map { it.uuid.value })
    }

    private suspend fun updateContractConfiguration() {
        val ownerRes = web3.sendEthCall(
            to = org.kethereum.model.Address(contractAddress),
            input = VoteKtContractV1.Owner.encode()
        )
        val ownerDecoded = VoteKtContractV1.Owner.decode(ownerRes).param0.value.asEthereumAddressString()
        ksPrefs.push(PrefKeys.CONTRACT_CREATOR_ADDRESS, ownerDecoded)

        val maxProposalRes = web3.sendEthCall(
            to = org.kethereum.model.Address(contractAddress),
            input = VoteKtContractV1.MAX_PROPOSAL_COUNT.encode()
        )
        val decodedMaxProposals = VoteKtContractV1.MAX_PROPOSAL_COUNT.decode(maxProposalRes).param0.value.safeIntValueExact()
        ksPrefs.push(PrefKeys.CONTRACT_MAX_PROPOSALS, decodedMaxProposals)

        val proposalFeeRes = web3.sendEthCall(
            to = org.kethereum.model.Address(contractAddress),
            input = VoteKtContractV1.CREATE_PROPOSAL_FEE.encode()
        )
        val proposalFeeDecoded = VoteKtContractV1.CREATE_PROPOSAL_FEE.decode(proposalFeeRes).param0.value
        ksPrefs.push(PrefKeys.CONTRACT_CREATE_PROPOSAL_FEE, proposalFeeDecoded)

        val proposalTitleLimit = web3.sendEthCall(
            to = org.kethereum.model.Address(contractAddress),
            input = VoteKtContractV1.MAX_PROPOSAL_TITLE_LENGTH.encode()
        )
        val proposalTitleLimitDecoded = VoteKtContractV1.MAX_PROPOSAL_TITLE_LENGTH.decode(proposalTitleLimit).param0.value.toInt()
        ksPrefs.push(PrefKeys.CONTRACT_MAX_PROPOSAL_TITLE_LENGTH, proposalTitleLimitDecoded)

        val proposalDescriptionLimit = web3.sendEthCall(
            to = org.kethereum.model.Address(contractAddress),
            input = VoteKtContractV1.MAX_PROPOSAL_DESCRIPTION_LENGTH.encode()
        )
        val proposalDescriptionLimitDecoded =
            VoteKtContractV1.MAX_PROPOSAL_DESCRIPTION_LENGTH.decode(proposalDescriptionLimit).param0.value.toInt()
        ksPrefs.push(PrefKeys.CONTRACT_MAX_DESCRIPTION_LENGTH, proposalDescriptionLimitDecoded)
    }

    override suspend fun handleContractEvent(event: EthereumEvent.ContractEvent) {
        when (event.eventTopic) {
            VoteKtContractV1.Events.VoteCasted.EVENT_ID -> {
                val eventData = VoteKtContractV1.Events.VoteCasted.decode(
                    topics = listOf(event.eventTopic),
                    data = event.encodedData
                )
                processVoteCastedEvent(eventData)
            }

            VoteKtContractV1.Events.ProposalCreated.EVENT_ID -> {
                val eventData = VoteKtContractV1.Events.ProposalCreated.decode(
                    topics = listOf(event.eventTopic),
                    data = event.encodedData
                )
                processProposalCreatedEvent(eventData)
            }

            else -> {
                println("${TAG} unknown contract event, skip")
            }
        }
    }

    override suspend fun clearContractData() {
        proposalsDao.clearAll()
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
                isSelfCreated = accountRepository.getSelfAddress().hex == proposal.creatorAddress,
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
                isSelfCreated = accountRepository.getSelfAddress().hex == proposal.creatorAddress,
                deploymentTransaction = deploymentTransaction?.mapToData(),
                deployStatus = mapDeployStatus(),
                duration = proposal.durationInHours?.let {
                    it.toDuration(DurationUnit.HOURS)
                } ?: run { Duration.ZERO },
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

    private suspend fun processProposalCreatedEvent(eventData: VoteKtContractV1.Events.ProposalCreated.Arguments) {
        val input = VoteKtContractV1.GetProposalDetails.encode(eventData.proposalnumber)
        val res = web3.sendEthCall(
            to = org.kethereum.model.Address(contractAddress),
            input = input
        )

        val raw = VoteKtContractV1.GetProposalDetails.decode(res).param0
        updateProposalInCache(contractProposal = raw)
    }

    private suspend fun updateProposalInCache(contractProposal: VoteKtContractV1.TupleA) = withContext(dispatcher) {
        val cached = proposalsDao.getProposalByUuid(uuid = contractProposal.uuid.value)
        cached?.let {
            proposalsDao.updateProposal(
                cached.copy(
                    number = contractProposal.number.value.toInt(),
                    creatorAddress = contractProposal.creatoraddress.value.asEthereumAddressString(),
                    votesFor = contractProposal.votesfor.value.toInt(),
                    isDraft = false,
                    votesAgainst = contractProposal.votesagainst.value.toInt(),
                    createdAt = contractProposal.creationtimemills.value.toLong() * 1000L,
                    expiresAt = contractProposal.expirationtimemills.value.toLong() * 1000L,
                    // TODO update contract with self vote
                )
            )
        } ?: run {
            proposalsDao.cacheProposal(
                ProposalEntity(
                    uuid = contractProposal.uuid.value,
                    number = contractProposal.number.value.toInt(),
                    creatorAddress = contractProposal.creatoraddress.value.asEthereumAddressString(),
                    title = contractProposal.title.value,
                    description = contractProposal.description.value,
                    isDraft = false,
                    votesFor = contractProposal.votesfor.value.toInt(),
                    votesAgainst = contractProposal.votesagainst.value.toInt(),
                    expiresAt = contractProposal.expirationtimemills.value.toLong() * 1000L,
                    createdAt = contractProposal.creationtimemills.value.toLong() * 1000L,
                    deployTransactionHash = null,
                    // TODO update contract with self vote
                    selfVote = null,
                    selfVoteTransactionHash = null,
                )
            )
        }
    }

    companion object {
        private const val TAG = "CONTRACT_TAG"
    }
}
