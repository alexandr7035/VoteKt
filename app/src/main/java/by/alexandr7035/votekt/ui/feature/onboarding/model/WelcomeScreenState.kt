package by.alexandr7035.votekt.ui.feature.onboarding.model

import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class WelcomeScreenState(
    val navigationEvent: StateEventWithContent<WelcomeScreenNavigationEvent> = consumed()
)
