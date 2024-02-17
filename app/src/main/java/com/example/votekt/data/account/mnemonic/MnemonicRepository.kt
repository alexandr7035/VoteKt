package com.example.votekt.data.account.mnemonic

interface MnemonicRepository {
    fun generateMnemonic(): List<Word>

    fun getRandomMnemonicWords(mnemonic: List<Word>): List<WordToConfirm>
}