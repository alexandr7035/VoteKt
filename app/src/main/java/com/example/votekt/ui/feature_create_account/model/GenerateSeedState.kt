package com.example.votekt.ui.feature_create_account.model

import com.example.votekt.domain.account.MnemonicWord
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class GenerateSeedState(
    val words: List<MnemonicWord> = emptyList(),
    val navigationEvent: StateEventWithContent<GeneratePhraseNavigationEvent> = consumed(),
)
