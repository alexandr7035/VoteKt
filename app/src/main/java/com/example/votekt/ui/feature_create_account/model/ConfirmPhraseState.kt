package com.example.votekt.ui.feature_create_account.model

import com.example.votekt.data.account.mnemonic.Word
import com.example.votekt.data.account.mnemonic.WordToConfirm
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ConfirmPhraseState(
    val isLoading: Boolean = false,
    val phrase: List<Word> = emptyList(),
    val confirmData: List<WordToConfirm> = emptyList(),
    val confirmationSelection: Map<Int, Word> = emptyMap(),
    val confirmationError: String? = null,
    val navigationEvent: StateEventWithContent<ConfirmPhraseNavigationEvent> = consumed()
)