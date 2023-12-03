fun main() {
    forLinesIn("day03/input.txt") {
        val lines = it.toList()
        var prevLine: String? = null
        var partNumbers = 0
        val gearHalves = hashMapOf<Pair<Int, Int>, Int>()
        var gearSum = 0
        for (line in lines.withIndex()) {
            val nextLine = lines.getOrNull(line.index + 1)
            var number = mutableListOf<IndexedValue<Char>>()
            for (c in line.value.withIndex()) {
                if (c.value.isDigit()) {
                    number.add(c)
                }
                else if (number.size > 0) {
                    if (anyAdjacentSymbol(number, prevLine, line.value, nextLine)) {
                        val newNumber = number.map { it.value }.joinToString(separator = "").toInt()
                        partNumbers += newNumber

                        val adjGear = anyAdjacentGear(number, prevLine, line.value, line.index, nextLine)
                        if (adjGear != null) {
                            println("gear $adjGear, nr $newNumber")
                            val existingHalve = gearHalves.get(adjGear)
                            if (existingHalve == null) {
                                gearHalves.put(adjGear, newNumber)
                            }
                            else {
                                println("Pair found ${(existingHalve * newNumber)}")
                                gearSum += (existingHalve * newNumber)
                            }
                        }
                    }
                    number = mutableListOf()
                }
            }
            // Special case, end of row
            if (number.size > 0 && anyAdjacentSymbol(number, prevLine, line.value, nextLine)) {
                val newNumber = number.map { it.value }.joinToString(separator = "").toInt()
                partNumbers += newNumber

                val adjGear = anyAdjacentGear(number, prevLine, line.value, line.index, nextLine)
                if (adjGear != null) {
                    println("gear $adjGear, nr $newNumber")
                    val existingHalve = gearHalves.get(adjGear)
                    if (existingHalve == null) {
                        gearHalves.put(adjGear, newNumber)
                    }
                    else {
                        println("Pair found ${(existingHalve * newNumber)}")
                        gearSum += (existingHalve * newNumber)
                    }
                }
            }
            prevLine = line.value
        }
        println("Part1: $partNumbers")
        println("Part2: $gearSum")

    }
}

val symbolPattern = Regex("[^\\d.]")

fun anyAdjacentSymbol(
    number: MutableList<IndexedValue<Char>>,
    prevLine: String?,
    line: String,
    nextLine: String?
): Boolean {

    // checking left and right, cannot be a digit
    if (line.getOrElse(number.first().index - 1) { '.' } != '.'
        || line.getOrElse(number.last().index + 1) { '.' } != '.') {
        return true
    }
    val startIndex = Math.max(number.first().index - 1, 0)
    val endIndex = number.last().index + 2
    if (prevLine?.substring(startIndex, Math.min(endIndex, prevLine.length))
        ?.contains(symbolPattern) == true) {
        return true
    }
    if (nextLine?.substring(startIndex, Math.min(endIndex, nextLine.length))
        ?.contains(symbolPattern) == true) {
        return true
    }

    return false
}

fun anyAdjacentGear(
    number: MutableList<IndexedValue<Char>>,
    prevLine: String?,
    line: String,
    lineIndex: Int,
    nextLine: String?
): Pair<Int, Int>? {

    // checking left and right, cannot be a digit
    if (line.getOrElse(number.first().index - 1) { '.' } == '*') {
        return number.first().index - 1 to lineIndex
    }
    if (line.getOrElse(number.last().index + 1) { '.' } == '*') {
        return number.last().index + 1 to lineIndex
    }
    val startIndex = Math.max(number.first().index - 1, 0)
    val endIndex = number.last().index + 2
    if (prevLine != null) {
        val gearIndex = prevLine.substring(0, Math.min(endIndex, prevLine.length)).indexOf("*", startIndex)
        if (gearIndex >= 0) {
            return gearIndex to lineIndex - 1
        }
    }
    if (nextLine != null) {
        val nextGearIndex = nextLine.substring(0, Math.min(endIndex, nextLine.length)).indexOf("*", startIndex)
        if (nextGearIndex >= 0) {
            return nextGearIndex to lineIndex + 1
        }
    }
    return null
}
