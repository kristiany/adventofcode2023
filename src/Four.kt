import kotlin.math.min

fun main() {
    forLinesIn("day04/input.txt") {
        val numbers = it.map { line ->
            val numbers = line.split(Regex(":\\s+")).get(1).split(Regex("\\s+\\|\\s+"))
            val winning = numbers.get(0).split(Regex("\\s+"))
                .map { it.toInt() }
                .toSet()
            val playing = numbers.get(1).split(Regex("\\s+"))
                .map { it.toInt() }
                .toSet()

            winning to playing
        }
        .toList()

        val total = numbers.map { nrs ->
            val intsec = nrs.first.intersect(nrs.second)
            calcPoints(intsec.size)
        }.sum()

        println("Part 1: $total")

        val cards = mutableMapOf<Int, Int>()
        numbers.indices.forEach { cards[it] = 1 }
        for (i in numbers.indices) {
            val current = numbers.get(i)
            val wins = current.first.intersect(current.second).size
            if (wins > 0) {
                for (iOffset in 1..min(wins, numbers.size - 1)) {
                    cards[i + iOffset] = cards[i + iOffset]!!.plus(cards[i]!!)
                }
            }
            //println("i $i, $cards")
        }
        println("Part2: ${cards.values.sum()}")
    }
}

private fun calcPoints(size: Int): Int {
    var points = 0
    if (size > 0) {
        points = 1
        for (i in 2..size) {
            points *= 2
        }
        //println("Intsec ${size} points $points")
    }
    return points
}
