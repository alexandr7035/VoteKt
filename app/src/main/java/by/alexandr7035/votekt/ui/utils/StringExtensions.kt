package by.alexandr7035.votekt.ui.utils

@Suppress("MagicNumber")
fun String.prettifyAddress(): String {
    val prefixLength = 5
    val suffixLength = 4
    val ellipsis = "..."
    val length = this.length

    return if (length > prefixLength + suffixLength) {
        val prefix = this.substring(0, prefixLength)
        val suffix = this.substring(length - suffixLength, length)
        "$prefix$ellipsis$suffix"
    } else {
        this
    }
}