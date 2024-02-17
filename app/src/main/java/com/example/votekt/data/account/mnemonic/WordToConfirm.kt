package com.example.votekt.data.account.mnemonic

data class WordToConfirm(
    val correctWord: Word,
    private val incorrectWords: List<Word>
) {
    fun randomWords(): List<Word> {
        return mutableListOf<Word>().apply {
            add(correctWord)
            addAll(incorrectWords)
        }.shuffled()
    }
}