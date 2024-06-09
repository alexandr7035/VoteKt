package com.example.votekt.domain.usecase.explorer

import com.example.votekt.domain.model.explorer.ExploreType
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
