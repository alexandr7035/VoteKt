package com.example.votekt.ui.feature_create_account.model

import com.example.votekt.domain.account.MnemonicWord

sealed class ConfirmPhraseIntent {
    data class LoadData(val phrase: List<MnemonicWord>): ConfirmPhraseIntent()

    data class SelectWordToConfirm(
        val index: Int,
        val word: MnemonicWord
    ): ConfirmPhraseIntent()

    object ConfirmSeed: ConfirmPhraseIntent()
}
