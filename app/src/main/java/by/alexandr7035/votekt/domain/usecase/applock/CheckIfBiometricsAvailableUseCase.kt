package by.alexandr7035.votekt.domain.usecase.applock

import by.alexandr7035.votekt.domain.model.security.BiometricsAvailability
import by.alexandr7035.votekt.domain.repository.AppLockRepository

class CheckIfBiometricsAvailableUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): BiometricsAvailability {
        return appLockRepository.checkBiometricsAvailable()
    }
}
