package by.alexandr7035.votekt.domain.usecase.demo

import by.alexandr7035.votekt.domain.repository.DemoModeRepository

class IsDemoModeEnabledUseCase(
    private val demoModeRepository: DemoModeRepository
) {
    fun invoke(): Boolean {
        return demoModeRepository.isDemoModeEnabled()
    }
}
