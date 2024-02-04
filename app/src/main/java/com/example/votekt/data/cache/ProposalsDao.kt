package com.example.votekt.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProposalsDao {
    @Query("SELECT * FROM proposals ORDER by expiresAt DESC")
    suspend fun getProposals(): List<ProposalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheProposal(proposal: ProposalEntity)
}