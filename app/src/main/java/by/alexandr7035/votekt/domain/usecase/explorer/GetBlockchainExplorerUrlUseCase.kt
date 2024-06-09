package by.alexandr7035.votekt.domain.usecase.explorer

import by.alexandr7035.votekt.domain.model.explorer.ExploreType
import by.alexandr7035.votekt.domain.repository.BlockchainExplorerRepository

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
