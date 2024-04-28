package com.example.votekt.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.votekt.domain.votings.ProposalDuration

@Entity(tableName = "proposals")
data class ProposalEntity(
    @PrimaryKey(autoGenerate = false)
    val uuid: String,
    val isDraft: Boolean,
    val creatorAddress: String,
    val deployTransactionHash: String?,
    val number: Int? = null,
    val createdAt: Long,
    val expiresAt: Long? = null,
    val durationInHours: Int? = null,
    val title: String,
    val description: String,
    val votesFor: Int = 0,
    val votesAgainst: Int = 0,
    val selfVote: Boolean? = null,
    val selfVoteTransactionHash: String? = null,
)
