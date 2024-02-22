package com.example.votekt.data.helpers

import cash.z.ecc.android.bip39.Mnemonics
import com.example.votekt.BuildConfig
import com.example.votekt.domain.account.MnemonicWord

interface MnemonicHelper {
    fun generateMnemonic(): List<MnemonicWord>
}

class MnemonicHelperImpl : MnemonicHelper {
    override fun generateMnemonic(): List<MnemonicWord> {
        val mnemonicCode = Mnemonics.MnemonicCode(Mnemonics.WordCount.COUNT_24)
        return mnemonicCode.words.mapIndexed { index, it ->
            MnemonicWord(
                index = index,
                value = it.toString(),
            )
        }
    }
}

class MnemonicHelperDebugImpl : MnemonicHelper {
    override fun generateMnemonic(): List<MnemonicWord> {
        return BuildConfig.TEST_MNEMONIC.split(" ")
            .mapIndexed { index, word ->
                MnemonicWord(index, word)
            }
    }
}
