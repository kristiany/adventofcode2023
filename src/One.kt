fun main() {
    forLinesIn("day01/input.txt") {
        val input = it.toList();
        val result = input.sumOf { (it.find { it.isDigit() }.toString() + it.findLast { it.isDigit() }).toInt() }
        println("Part 1: $result")

        val result2 = input.sumOf { (first(it) + last(it)).toInt() }
        println("Part 2: $result2")
    }
}

val digits = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
    "1" to "1",
    "2" to "2",
    "3" to "3",
    "4" to "4",
    "5" to "5",
    "6" to "6",
    "7" to "7",
    "8" to "8",
    "9" to "9")

fun first(input: String): String? {
    return input.findAnyOf(digits.keys)?.let { digits.get(it.second) }
}

fun last(input: String): String? {
    return input.findLastAnyOf(digits.keys)?.let { digits.get(it.second) }
}
