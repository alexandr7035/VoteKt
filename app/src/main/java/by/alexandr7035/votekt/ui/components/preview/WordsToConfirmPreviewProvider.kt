package by.alexandr7035.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.domain.model.account.MnemonicWordConfirm

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
