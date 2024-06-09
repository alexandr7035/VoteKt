package by.alexandr7035.votekt.domain.repository

import by.alexandr7035.votekt.domain.model.explorer.ExploreType

interface BlockchainExplorerRepository {
    fun getExplorerUrl(
        payload: String,
        exploreType: ExploreType,
    ): String?
}
