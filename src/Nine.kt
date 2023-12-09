
fun main() {
    forLinesIn("day09/input.txt") {
        val input = it.map { it.split(" ").map { it.toLong() } }.toList()
        val result = input.map {
            val lastInSeries = mutableListOf<Long>()
            val firstInSeries = mutableListOf<Long>()
            var serie = it.toList()
            //println("$serie")
            firstInSeries.add(0, serie.first())
            lastInSeries.add(0, serie.last())
            while (! serie.all { it == 0L }) {
                val nextSerie = mutableListOf<Long>()
                for (i in 1..<serie.size) {
                    nextSerie.add(serie[i] - serie[i - 1])
                }
                serie = nextSerie
                firstInSeries.add(0, serie.first())
                lastInSeries.add(0, serie.last())
                //println("$serie")
            }
            //println("first $firstInSeries")
            var previous = 0L
            for (f in firstInSeries) {
                previous = f - previous
            }
            //println("previous $previous")
            //println("last $lastInSeries")
            (lastInSeries.sum() to previous)
        }
        println("Part 1: ${result.map { it.first }.sum()}")
        println("Part 2: ${result.map { it.second }.sum()}")
    }
}