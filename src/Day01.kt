import kotlin.io.path.useLines

fun part1(input: Sequence<String>): Int {
    return input
        .sumOf { line ->
            val firstDigit = line.first { it.isDigit() }
            val lastDigit = line.last { it.isDigit() }
            firstDigit.digitToInt() * 10 + lastDigit.digitToInt()
        }
}

class SpelledDigitTracker(
    private val word: String,
    val value: Int,
) {
    private var currentIndex: Int = 0

    val isComplete: Boolean
        get() = currentIndex > word.lastIndex

    private val nextChar: Char
        get() = word[currentIndex]

    fun track(char: Char) {
        when (char) {
            nextChar -> currentIndex++
            word.first() -> currentIndex = 1
            else -> currentIndex = 0
        }
    }
}

private val spelledDigits = listOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)

private fun getSpelledDigitTrackers(useReversedWords: Boolean) = spelledDigits
    .map {
        val word = if (useReversedWords) it.first.reversed() else it.first
        SpelledDigitTracker(word, it.second)
    }

fun part2(input: Sequence<String>): Int {
    return input
        .sumOf { line ->
            val firstDigit = getFirstDigitIncludingSpelled(line)
            val lastDigit = getFirstDigitIncludingSpelled(line.reversed(), true)
            firstDigit * 10 + lastDigit
        }
}

private fun getFirstDigitIncludingSpelled(line: String, isReversed: Boolean = false): Int {
    val digitTrackers = getSpelledDigitTrackers(useReversedWords = isReversed)
    line.forEach { char ->
        if (char.isDigit()) return char.digitToInt()
        digitTrackers.firstOrNull {
            it.track(char)
            it.isComplete
        }?.let { return it.value }
    }
    throw IllegalArgumentException("The line doesn't contain digits")
}

fun main() {
    val input = getInput("day_1")
    input.useLines { part1(it) }.println()
    input.useLines { part2(it) }.println()
}
