package by.alexandr7035.votekt.domain.security

import by.alexandr7035.votekt.domain.security.model.BiometricsAvailability

class CheckIfBiometricsAvailableUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): BiometricsAvailability {
        return appLockRepository.checkBiometricsAvailable()
    }
}
