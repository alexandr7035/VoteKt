package com.example.votekt.ui.feature.account.create.model

import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.domain.account.MnemonicWordConfirm
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ConfirmPhraseState(
    val isLoading: Boolean = false,
    val phrase: List<MnemonicWord> = emptyList(),
    val confirmData: List<MnemonicWordConfirm> = emptyList(),
    val confirmationSelection: Map<Int, MnemonicWord> = emptyMap(),
    val confirmationError: String? = null,
    val navigationEvent: StateEventWithContent<ConfirmPhraseNavigationEvent> = consumed()
)
