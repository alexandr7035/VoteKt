package by.alexandr7035.votekt.domain.repository

import by.alexandr7035.votekt.domain.model.demo.DemoProposal

interface DemoModeRepository {
    fun isDemoModeEnabled(): Boolean
    fun getRandomDemoProposal(): DemoProposal
}
