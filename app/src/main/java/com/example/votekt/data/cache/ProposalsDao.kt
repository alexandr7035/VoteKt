package com.example.votekt.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ProposalsDao {
    @Transaction
    @Query("SELECT * FROM proposals ORDER by createdAt DESC")
    fun getProposals(): Flow<List<ProposalWithTransaction>>

    @Transaction
    @Query("SELECT * FROM proposals WHERE id = :id")
    fun getProposalById(id: Int): Flow<ProposalWithTransaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheProposal(proposal: ProposalEntity)

    @Query("UPDATE proposals SET deployTransactionHash = :newDeployTransactionHash WHERE id = :proposalId")
    fun updateDeployTransactionHash(proposalId: Int, newDeployTransactionHash: String)
}