import java.math.BigInteger

fun main() {


    fun part1(input: List<String>): BigInteger {
        val cosmos = parseCosmos(input)
        return cosmos.totalDistance().toAbsoluteDistance(2)
    }


    fun part2(input: List<String>): BigInteger {
        val cosmos = parseCosmos(input)
        return cosmos.totalDistance().toAbsoluteDistance(1000000)
    }


    val testInput = readInput("Day11_test")
    check(part1(testInput) == BigInteger.valueOf(374))
//    check(part2(testInput) == BigInteger.valueOf(8410))

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

enum class TileType(private val symbol: Char) {
    GALAXY('#'), EMPTY_SPACE('.');

    companion object {
        fun fromSymbol(symbol: Char): TileType = TileType.entries.find { it.symbol == symbol }!!
    }
}


fun parseCosmos(input: List<String>): Cosmos {
    val tiles = input.map { it.map(TileType.Companion::fromSymbol) }
    return Cosmos(tiles)
}

class Distance(val normalSpace: Int, val emptySpace: Int) {
    operator fun plus(d: Distance): Distance = Distance(
        normalSpace + d.normalSpace,
        emptySpace + d.emptySpace,
    )

    fun toAbsoluteDistance(emptySpaceSize: Int): BigInteger =
        emptySpace.toBigInteger().times(emptySpaceSize.toBigInteger()).plus(normalSpace.toBigInteger())
}

class Cosmos(val grid: List<List<TileType>>) {
    val width = grid[0].size
    val height = grid.size
    val emptyColumnCounts = (0..<width).map { isColumnEmpty(it) }.countTrueValues()
    val emptyRowsCount = (0..<height).map { isRowEmpty(it) }.countTrueValues()

    private fun isColumnEmpty(index: Int) =
        grid.all { it[index] == TileType.EMPTY_SPACE }

    private fun isRowEmpty(index: Int) =
        grid[index].all { it == TileType.EMPTY_SPACE }

    private fun distance(row1: Int, column1: Int, row2: Int, column2: Int): Distance {
        val columns = Math.abs(column2 - column1)
        val rows = Math.abs(row2 - row1)
        val emptyColumns = Math.abs(emptyColumnCounts[column2] - emptyColumnCounts[column1])
        val emptyRows = Math.abs(emptyRowsCount[row2] - emptyRowsCount[row1])
        return Distance(rows + columns - emptyRows - emptyColumns, emptyRows + emptyColumns)
    }

    fun totalDistance(): Distance {
        val galaxies = grid.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, type -> Pair(rowIndex, colIndex).takeIf { isGalaxy(rowIndex, colIndex) } }
        }

        var totalDistance = Distance(0, 0)
        galaxies.forEachIndexed { index1, galaxy1 ->
            for (index2 in index1 + 1..<galaxies.size) {
                totalDistance += distance(galaxies[index1].first, galaxies[index1].second, galaxies[index2].first, galaxies[index2].second)
            }
        }
        return totalDistance
    }

    private fun isGalaxy(row1: Int, column1: Int) = grid[row1][column1] == TileType.GALAXY
}

private fun List<Boolean>.countTrueValues(): List<Int> {
    var count = 0
    return map { count += if (it) 1 else 0; count }
}
