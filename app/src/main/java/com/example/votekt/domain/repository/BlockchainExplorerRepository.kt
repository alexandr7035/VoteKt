package com.example.votekt.domain.repository

import com.example.votekt.domain.model.explorer.ExploreType

interface BlockchainExplorerRepository {
    fun getExplorerUrl(
        payload: String,
        exploreType: ExploreType,
    ): String?
}
