import kotlin.math.max
import kotlin.math.min

fun main() {
    forLinesIn("day14/input.txt") {
        val rolling = mutableSetOf<Rock>()
        val static = mutableSetOf<Rock>()
        it.forEachIndexed {y, row ->
            for (x in row.indices) {
                if (row[x] == 'O') {
                    rolling.add(Rock(x, y))
                } else if (row[x] == '#') {
                    static.add(Rock(x, y))
                }
            }
        }
        //println("Rolling $rolling")
        //println("Static $static")

        val maxSouth = max(rolling.maxOf { it.y }, static.maxOf { it.y }) + 1
        val maxEast = max(rolling.maxOf { it.x }, static.maxOf { it.x }) + 1
        val rolled = rollNorth(rolling, static)
        println("Part 1: ${rolled.map { maxSouth - it.y }.sum()}")

        var rocks = rolling
        val tail = mutableListOf<Int>()
        var repeat: Pair<Int, List<Int>>? = null
        for (cycle in 1..200) {
            rocks = rollNorth(rocks, static)
            //println("   north $rocks")
            rocks = rollWest(rocks, static)
            //println("   west $rocks")
            rocks = rollSouth(rocks, static, maxSouth)
            //println("   south $rocks")
            rocks = rollEast(rocks, static, maxEast)
            //println("   east $rocks")
            val load = rocks.map { maxSouth - it.y }.sum()
            println("$cycle: $load")
            tail.add(load)
            // Trying to find a cycle pattern
            if (tail.size > 10) {
                repeat = findRepeat(tail)
                if (repeat != null) {
                    break
                }
            }
        }
        val rest = (1000000000 - repeat!!.first - 1) % repeat.second.size
        println("Part 2: ${repeat.second[rest]}")
    }
}

private fun findRepeat(tail: MutableList<Int>): Pair<Int, List<Int>>? {
    for (t in (0..<tail.size).reversed()) {
        val found = tail.subList(0, t).lastIndexOf(tail[t])
        //println("t: ${tail[t]} found $found")
        if (found > 0) {
            val p = tail.subList(found, tail.size)
            if (p.size > 5 && found - p.size > 0 && p == tail.subList(found - p.size, found)) {
                println("repeat found at $found\npattern $p\n$tail}")
                return found - p.size to p
            }
        }
    }
    return null
}

private fun rollNorth(
    rolling: Set<Rock>,
    static: Set<Rock>
): MutableSet<Rock> {
    val rolled = mutableSetOf<Rock>()
    for (r in rolling.sortedBy { it.y }) {
        //println("rock $r")
        val colRolled = rolled.filter { r.x == it.x && r.y > it.y }.toSet()
        val colStatic = static.filter { r.x == it.x && r.y > it.y }.toSet()
        //println("col rolled $colRolled, col static $colStatic")
        val rolledMaxY = if (colRolled.isEmpty()) -1 else colRolled.maxOf { it.y }
        val staticMaxY = if (colStatic.isEmpty()) -1 else colStatic.maxOf { it.y }
        //println("max of $rolledMaxY - $staticMaxY = ${max(rolledMaxY, staticMaxY) + 1}")
        rolled.add(Rock(r.x, max(rolledMaxY, staticMaxY) + 1))
    }
    return rolled
}

private fun rollSouth(
    rolling: Set<Rock>,
    static: Set<Rock>,
    height: Int
): MutableSet<Rock> {
    val rolled = mutableSetOf<Rock>()
    for (r in rolling.sortedBy { it.y }.reversed()) {
        //println("rock $r")
        val colRolled = rolled.filter { r.x == it.x && r.y < it.y }.toSet()
        val colStatic = static.filter { r.x == it.x && r.y < it.y }.toSet()
        val rolledMinY = if (colRolled.isEmpty()) height else colRolled.minOf { it.y }
        val staticMinY = if (colStatic.isEmpty()) height else colStatic.minOf { it.y }
        rolled.add(Rock(r.x, min(rolledMinY, staticMinY) - 1))
    }
    return rolled
}

private fun rollEast(
    rolling: Set<Rock>,
    static: Set<Rock>,
    width: Int
): MutableSet<Rock> {
    val rolled = mutableSetOf<Rock>()
    for (r in rolling.sortedBy { it.x }.reversed()) {
        //println("rock $r")
        val colRolled = rolled.filter { r.y == it.y && r.x < it.x }.toSet()
        val colStatic = static.filter { r.y == it.y && r.x < it.x }.toSet()
        val rolledMinX = if (colRolled.isEmpty()) width else colRolled.minOf { it.x }
        val staticMinX = if (colStatic.isEmpty()) width else colStatic.minOf { it.x }
        rolled.add(Rock(min(rolledMinX, staticMinX) - 1, r.y))
    }
    return rolled
}

private fun rollWest(
    rolling: Set<Rock>,
    static: Set<Rock>
): MutableSet<Rock> {
    val rolled = mutableSetOf<Rock>()
    for (r in rolling.sortedBy { it.x }) {
        //println("rock $r")
        val colRolled = rolled.filter { r.y == it.y && r.x > it.x }.toSet()
        val colStatic = static.filter { r.y == it.y && r.x > it.x }.toSet()
        val rolledMaxX = if (colRolled.isEmpty()) -1 else colRolled.maxOf { it.x }
        val staticMaxX = if (colStatic.isEmpty()) -1 else colStatic.maxOf { it.x }
        rolled.add(Rock(max(rolledMaxX, staticMaxX) + 1, r.y))
    }
    return rolled
}

data class Rock(val x: Int, val y: Int)
