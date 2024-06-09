package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.domain.account.MnemonicWordConfirm

class WordsToConfirmPreviewProvider : PreviewParameterProvider<MnemonicWordConfirm> {
    override val values: Sequence<MnemonicWordConfirm>
        get() = sequenceOf(
            MnemonicWordConfirm(
                correctWord = MnemonicWord(0, "Lorem"),
                incorrectWords = listOf(
                    MnemonicWord(5, "Wrong1"),
                    MnemonicWord(10, "Wrong2"),
                )
            )
        )
}
