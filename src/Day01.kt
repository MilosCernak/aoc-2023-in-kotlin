fun main() {

    fun calibrationValuePart1(str: String) = 10 * str.first(Char::isDigit).digitToInt() + str.last(Char::isDigit).digitToInt()

    fun part1(input: List<String>): Int = input.map(::calibrationValuePart1).sum()

    val textToNumberMap = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    ) + (0..9).associateBy(Int::toString)

    val numberRegexDefinition = textToNumberMap.keys.joinToString(separator = "|")
    val firstDigitRegex = Regex("(${numberRegexDefinition})")
    val lastDigitRegex = Regex(".*(${numberRegexDefinition})")

    fun getDigit(str: String, regex: Regex): Int {
        val digitText = regex.find(str)!!.groupValues[1]
        return textToNumberMap[digitText]!!
    }

    fun calibrationValuePart2(str: String) = 10 * getDigit(str, firstDigitRegex) + getDigit(str, lastDigitRegex)

    fun part2(input: List<String>): Int = input.map(::calibrationValuePart2).sum()

    // test if implementation meets criteria from the description", "like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInputPart2 = readInput("Day01_test_part2")
    check(part2(testInputPart2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

