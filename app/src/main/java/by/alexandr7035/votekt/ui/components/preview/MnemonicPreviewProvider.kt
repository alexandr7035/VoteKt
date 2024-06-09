package by.alexandr7035.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import by.alexandr7035.votekt.BuildConfig
import by.alexandr7035.votekt.domain.account.MnemonicWord

class MnemonicPreviewProvider : PreviewParameterProvider<List<MnemonicWord>> {
    override val values: Sequence<List<MnemonicWord>>
        get() = sequenceOf(
            BuildConfig.TEST_MNEMONIC.split(" ").mapIndexed { index, it ->
                MnemonicWord(index, it)
            }
        )
}
