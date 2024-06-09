package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.votekt.BuildConfig
import com.example.votekt.domain.account.MnemonicWord

class MnemonicPreviewProvider : PreviewParameterProvider<List<MnemonicWord>> {
    override val values: Sequence<List<MnemonicWord>>
        get() = sequenceOf(
            BuildConfig.TEST_MNEMONIC.split(" ").mapIndexed { index, it ->
                MnemonicWord(index, it)
            }
        )
}
