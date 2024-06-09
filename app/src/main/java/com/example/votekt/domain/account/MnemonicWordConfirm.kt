package com.example.votekt.domain.account

data class MnemonicWordConfirm(
    val correctWord: MnemonicWord,
    private val incorrectWords: List<MnemonicWord>
) {

    fun rightWordIndex() = correctWord.index

    fun shuffledWords(): List<MnemonicWord> {
        return mutableListOf<MnemonicWord>().apply {
            add(correctWord)
            addAll(incorrectWords)
        }.shuffled()
    }
}
