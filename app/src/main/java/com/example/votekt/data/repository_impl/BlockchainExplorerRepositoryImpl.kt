package com.example.votekt.data.repository_impl

import com.example.votekt.BuildConfig
import com.example.votekt.domain.model.blockchain_explorer.EVMNetwork
import com.example.votekt.domain.model.blockchain_explorer.ExploreType
import com.example.votekt.domain.repository.BlockchainExplorerRepository

class BlockchainExplorerRepositoryImpl: BlockchainExplorerRepository {
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
        return "${baseUrl}/${route}/${value}"
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
