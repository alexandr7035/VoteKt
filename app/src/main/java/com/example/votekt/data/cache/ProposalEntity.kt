package com.example.votekt.data.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.votekt.domain.votings.VoteType

@Entity(tableName = "proposals")
data class ProposalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val remoteId: Long,
    val title: String,
    val description: String,
    val expiresAt: Long,
    @Embedded
    val votingData: VotingDataEntity?,
    val isCreationPending: Boolean?,
)

data class VotingDataEntity(
    val votesFor: Int,
    val votesAgainst: Int,
    val selfVote: VoteType?,
)

