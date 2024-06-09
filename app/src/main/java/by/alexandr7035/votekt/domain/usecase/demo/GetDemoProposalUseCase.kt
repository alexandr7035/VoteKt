package by.alexandr7035.votekt.domain.usecase.demo

import by.alexandr7035.votekt.domain.model.demo.DemoProposal
import by.alexandr7035.votekt.domain.repository.DemoModeRepository

class GetDemoProposalUseCase(
    private val demoModeRepository: DemoModeRepository
) {
    fun invoke(): DemoProposal {
        return demoModeRepository.getRandomDemoProposal()
    }
}
