package com.example.votekt.domain.security

import com.example.votekt.domain.security.model.BiometricsAvailability

class CheckIfBiometricsAvailableUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): BiometricsAvailability {
        return appLockRepository.checkBiometricsAvailable()
    }
}