package by.alexandr7035.votekt.ui.feature.account.create.model

import by.alexandr7035.votekt.domain.account.MnemonicWord
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class GenerateSeedState(
    val words: List<MnemonicWord> = emptyList(),
    val navigationEvent: StateEventWithContent<GeneratePhraseNavigationEvent> = consumed(),
)
