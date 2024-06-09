package by.alexandr7035.votekt.data.repository

import by.alexandr7035.votekt.BuildConfig
import by.alexandr7035.votekt.domain.model.explorer.EVMNetwork
import by.alexandr7035.votekt.domain.model.explorer.ExploreType
import by.alexandr7035.votekt.domain.repository.BlockchainExplorerRepository

class BlockchainExplorerRepositoryImpl : BlockchainExplorerRepository {
    override fun getExplorerUrl(
        payload: String,
        exploreType: ExploreType
    ): String? {
        val networkUrl = findNetworkUrl(BuildConfig.CHAIN_ID) ?: return null
        return buildExplorerUrl(networkUrl, payload, exploreType)
    }

    private fun buildExplorerUrl(
        baseUrl: String,
        value: String,
        exploreType: ExploreType
    ): String? {
        val route = EXPLORER_ROUTES[exploreType]
        return "$baseUrl/$route/$value"
    }

    @Suppress("SameParameterValue")
    private fun findNetworkUrl(chainId: Int): String? {
        val network = EVMNetwork.values().find { it.chainId == chainId }
        return NETWORK_URLS[network]
    }

    companion object {
        private val NETWORK_URLS = mapOf(
            EVMNetwork.SEPOLIA to "https://sepolia.etherscan.io"
        )

        private val EXPLORER_ROUTES = mapOf(
            ExploreType.ADDRESS to "address",
            ExploreType.BLOCK to "block",
            ExploreType.TRANSACTION to "tx",
        )
    }
}
