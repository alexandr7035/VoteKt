package com.example.votekt.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.votekt.domain.votings.VoteType
import kotlinx.coroutines.flow.Flow

@Dao
interface ProposalsDao {
    @Transaction
    @Query("SELECT * FROM proposals ORDER BY CASE WHEN number IS NULL THEN 0 ELSE 1 END, number DESC, createdAt DESC")
    fun observeProposals(): Flow<List<ProposalWithTransactions>>

    @Transaction
    @Query("SELECT * FROM proposals WHERE uuid = :uuid")
    fun observeProposalByUuid(uuid: String): Flow<ProposalWithTransactions>

    @Transaction
    @Query("SELECT * FROM proposals WHERE uuid = :uuid")
    fun getProposalByUuid(uuid: String): ProposalEntity?

    @Transaction
    @Query("SELECT * FROM proposals WHERE deployTransactionHash = :transactionHash")
    fun getProposalByDeployHash(transactionHash: String): ProposalEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheProposal(proposal: ProposalEntity)

    @Update
    suspend fun updateProposal(proposal: ProposalEntity)

    @Query("UPDATE proposals SET deployTransactionHash = :newDeployTransactionHash WHERE uuid = :proposalId")
    fun updateDeployTransactionHash(proposalId: String, newDeployTransactionHash: String)

    @Query("UPDATE proposals SET selfVote = :supported, selfVoteTransactionHash = :voteTransactionHash WHERE number = :proposalNumber")
    fun updateProposalVote(
        proposalNumber: Int,
        supported: Boolean,
        voteTransactionHash: String,
    )

    @Query("DELETE FROM proposals WHERE NOT isDraft AND uuid NOT IN (:remainingProposals)")
    suspend fun cleanUpProposals(remainingProposals: List<String>)
}