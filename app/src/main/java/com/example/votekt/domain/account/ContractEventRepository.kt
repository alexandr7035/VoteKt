package com.example.votekt.domain.account

interface ContractEventRepository {
    suspend fun subscribe()
}
