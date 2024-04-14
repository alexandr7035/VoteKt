package com.example.votekt.domain.account

interface MnemonicRepository {
    fun generateMnemonic(wordCount: Int): List<MnemonicWord>

    fun verifyMnemonic(words: List<String>)

    fun getTestMnemonic(): List<MnemonicWord>

    fun getRandomMnemonicWords(mnemonic: List<MnemonicWord>): List<MnemonicWordConfirm>

    fun confirmPhrase(
        mnemonic: List<MnemonicWord>,
        proposedWordsToConfirm: List<MnemonicWordConfirm>,
        confirmationData: Map<Int, MnemonicWord>
    )
}