package by.alexandr7035.votekt.domain.votings

import by.alexandr7035.ethereum.model.events.EthereumEvent
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.core.Uuid
import by.alexandr7035.votekt.domain.model.contract.ContractConfiguration
import by.alexandr7035.votekt.domain.model.contract.ContractState
import by.alexandr7035.votekt.domain.model.contract.CreateDraftProposal
import kotlinx.coroutines.flow.Flow

interface VotingContractRepository {
    fun observeContractState(): Flow<ContractState>
    fun getContractConfiguration(): ContractConfiguration
    fun observeProposals(): Flow<List<Proposal>>
    fun observeProposalById(id: String): Flow<Proposal>
    suspend fun createDraftProposal(req: CreateDraftProposal): OperationResult<Uuid>
    suspend fun deployDraftProposal(proposalUuid: Uuid): OperationResult<Unit>
    suspend fun deleteDraftProposal(proposalUuid: Uuid): OperationResult<Unit>
    suspend fun voteOnProposal(proposalNumber: Int, vote: VoteType): OperationResult<Unit>
    suspend fun syncProposalsWithContract()
    suspend fun handleContractEvent(event: EthereumEvent.ContractEvent)
    suspend fun clearContractData()
}
