package com.example.votekt.data.account.mnemonic

import cash.z.ecc.android.bip39.Mnemonics
import com.example.votekt.BuildConfig

interface MnemonicHelper {
    fun generateMnemonic(): List<Word>
}

class MnemonicHelperImpl : MnemonicHelper {
    override fun generateMnemonic(): List<Word> {
        val mnemonicCode = Mnemonics.MnemonicCode(Mnemonics.WordCount.COUNT_24)
        return mnemonicCode.words.mapIndexed { index, it ->
            Word(
                index = index,
                value = it.toString(),
            )
        }
    }
}

class MnemonicHelperDebugImpl : MnemonicHelper {
    override fun generateMnemonic(): List<Word> {
        return BuildConfig.TEST_MNEMONIC.split(" ")
            .mapIndexed { index, word ->
                Word(index, word)
            }
    }
}
