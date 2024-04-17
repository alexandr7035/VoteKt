package com.example.votekt.ui.feature_restore_account.model

import com.example.votekt.ui.core.resources.UiText
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class RestoreAccountState(
    val wordInput: String = "",
    val error: UiText? = null,
    val navigationEvent: StateEventWithContent<RestoreAccountNavigationEvent> = consumed()
)
