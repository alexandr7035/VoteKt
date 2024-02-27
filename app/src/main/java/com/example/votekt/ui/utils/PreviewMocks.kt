package com.example.votekt.ui.utils

import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.domain.account.MnemonicWordConfirm

fun MnemonicWordConfirm.Companion.mock() = MnemonicWordConfirm(
    correctWord = MnemonicWord(0, "Lorem"), incorrectWords = listOf(
        MnemonicWord(5, "Wrong1"),
        MnemonicWord(10, "Wrong2"),
    )
)
