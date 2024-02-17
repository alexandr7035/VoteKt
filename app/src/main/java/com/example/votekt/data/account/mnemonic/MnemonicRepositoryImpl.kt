package com.example.votekt.data.account.mnemonic

import java.security.SecureRandom
import java.util.Random

class MnemonicRepositoryImpl(
    private val mnemonicHelper: MnemonicHelper
) : MnemonicRepository {
    private val secureRandom = SecureRandom()

    override fun generateMnemonic(): List<Word> {
        return mnemonicHelper.generateMnemonic()
    }

    override fun getRandomMnemonicWords(mnemonic: List<Word>): List<WordToConfirm> {
        return generateConfirmationWords(mnemonic)
    }

    private fun generateConfirmationWords(mnemonic: List<Word>): List<WordToConfirm> {
        val confirmationWords = mutableListOf<WordToConfirm>()

        val correctIndexes = getCorrectWordIndexes(mnemonic.size)

        correctIndexes.forEach {
            val correctWord = mnemonic[it]
            val wrongWords = getIncorrectIndexesForWord(mnemonic, correctWord).map { incorrectIndex ->
                mnemonic[incorrectIndex]
            }

            confirmationWords.add(WordToConfirm(correctWord, wrongWords))
        }

        return confirmationWords.sortedBy { it.correctWord.index }
    }

    private fun getIncorrectIndexesForWord(mnemonic: List<Word>, correctWord: Word): List<Int> {
        val incorrectWords = mutableListOf<Int>()

        while (incorrectWords.size < INCORRECT_WORDS_COUNT) {
            val randomIndex = secureRandom.nextInt(mnemonic.size)

            if (randomIndex != correctWord.index && randomIndex !in incorrectWords) {
                incorrectWords.add(randomIndex)
            }
        }

        return incorrectWords
    }

    private fun getCorrectWordIndexes(maxIndex: Int): List<Int> {
        val randomIndexes = mutableListOf<Int>()
        val random = Random()

        while (randomIndexes.size < CONFIRMATION_WORDS_COUNT) {
            val randomIndex = random.nextInt(maxIndex)
            if (!randomIndexes.contains(randomIndex)) {
                randomIndexes.add(randomIndex)
            }
        }

        return randomIndexes
    }

    companion object {
        private const val CONFIRMATION_WORDS_COUNT = 3
        private const val INCORRECT_WORDS_COUNT = 2
    }
}