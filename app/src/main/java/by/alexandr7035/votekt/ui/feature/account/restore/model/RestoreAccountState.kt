package by.alexandr7035.votekt.ui.feature.account.restore.model

import by.alexandr7035.votekt.ui.core.resources.UiText
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class RestoreAccountState(
    val wordInput: String = "",
    val error: UiText? = null,
    val navigationEvent: StateEventWithContent<RestoreAccountNavigationEvent> = consumed()
)
