package com.example.votekt.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.votekt.domain.votings.VoteType

@Entity(tableName = "proposals")
data class ProposalEntity(
    @PrimaryKey(autoGenerate = false)
    val uuid: String,
    val creatorAddress: String,
    val deployTransactionHash: String?,
    val remoteId: Long? = null,
    val createdAt: Long,
    val expiresAt: Long? = null,
    val title: String,
    val description: String,
    val votesFor: Int = 0,
    val votesAgainst: Int = 0,
    val selfVote: VoteType? = null,
    val selfVoteTransactionHash: String? = null,
)
