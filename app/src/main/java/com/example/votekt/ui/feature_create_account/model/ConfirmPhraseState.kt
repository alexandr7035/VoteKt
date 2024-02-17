package com.example.votekt.ui.feature_create_account.model

import com.example.votekt.data.account.mnemonic.WordToConfirm

data class ConfirmPhraseState(
    val isLoading: Boolean = false,
    val confirmData: List<WordToConfirm> = emptyList()
)