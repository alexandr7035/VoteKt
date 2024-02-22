package com.example.votekt.domain.account

interface MnemonicRepository {
    fun generateMnemonic(): List<MnemonicWord>

    fun getRandomMnemonicWords(mnemonic: List<MnemonicWord>): List<MnemonicWordConfirm>

    fun confirmPhrase(
        mnemonic: List<MnemonicWord>,
        proposedWordsToConfirm: List<MnemonicWordConfirm>,
        confirmationData: Map<Int, MnemonicWord>
    )
}