package com.example.votekt.domain.usecase.blockchain_explorer

import com.example.votekt.domain.model.blockchain_explorer.ExploreType
import com.example.votekt.domain.repository.BlockchainExplorerRepository

class GetBlockchainExplorerUrlUseCase(
    private val explorerRepository: BlockchainExplorerRepository
) {
    fun invoke(
        exploreType: ExploreType,
        value: String,
    ): String? {
        return explorerRepository.getExplorerUrl(value, exploreType)
    }
}
