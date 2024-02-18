package com.example.votekt.data.account.mnemonic

data class WordToConfirm(
    val correctWord: Word,
    private val incorrectWords: List<Word>
) {

    fun rightWordIndex() = correctWord.index

    fun shuffledWords(): List<Word> {
        return mutableListOf<Word>().apply {
            add(correctWord)
            addAll(incorrectWords)
        }.shuffled()
    }

    companion object {}
}