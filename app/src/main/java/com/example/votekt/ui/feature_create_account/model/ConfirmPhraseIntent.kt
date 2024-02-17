package com.example.votekt.ui.feature_create_account.model

import com.example.votekt.data.account.mnemonic.Word

sealed class ConfirmPhraseIntent {
    data class LoadData(val phrase: List<Word>): ConfirmPhraseIntent()
}
