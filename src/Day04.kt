fun main() {

    fun String.toNumberSet() = split(Regex(" +")).map(String::toInt).toSet()

    fun String.parseLineNumbers(): Pair<Set<Int>, Set<Int>> =
        substringAfter(":").split("|").map { it.trim().toNumberSet() }.let { Pair(it[0], it[1]) }

    fun String.commonNumberCount() = parseLineNumbers().let { (winningNumbers, numbers) ->
        winningNumbers.intersect(numbers).size
    }

    fun cardPoints(line: String) = line.commonNumberCount().let {
        if (it == 0) 0 else 1 shl (it - 1)
    }

    fun part1(input: List<String>): Int = input.map(::cardPoints).sum()


    fun part2(input: List<String>): Int {
        val commonNumberCounts: List<Int> = input.map(String::commonNumberCount)
        val cardCounts = IntArray(input.size) { 1 }
        cardCounts.forEachIndexed { index, currentCardCount ->
            for (i in 1..commonNumberCounts[index]) {
                cardCounts[index + i] += currentCardCount
            }
        }
        return cardCounts.sum()
    }


    // test if implementation meets criteria from the description", "like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

