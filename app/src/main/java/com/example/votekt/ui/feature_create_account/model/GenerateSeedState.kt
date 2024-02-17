package com.example.votekt.ui.feature_create_account.model

import com.example.votekt.data.account.mnemonic.Word
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class GenerateSeedState(
    val words: List<Word> = emptyList(),
    val navigationEvent: StateEventWithContent<GeneratePhraseNavigationEvent> = consumed(),
)
