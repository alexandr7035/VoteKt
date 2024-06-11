package by.alexandr7035.votekt.domain.model.account

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
