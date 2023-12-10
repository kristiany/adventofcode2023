import java.lang.IllegalArgumentException

fun main() {
    forLinesIn("day10/input.txt") {
        var s = 0 to 0
        val map = it.mapIndexed { y, line ->
            val x = line.indexOf("S")
            if (x >= 0) {
                s = x to y
            }
            line }.toList()
        //println("S $s, $map")
        val paths = paths(map, s)
        var steps = 1
        var cur1 = paths.get(0)
        var cur2 = paths.get(1)
        val loop = mutableSetOf(s)
        loop.add(cur1)
        loop.add(cur2)
        var lastVisited1 = s
        var lastVisited2 = s
        while (cur1 != cur2) {
            //println("Step $steps, cur1 $cur1, cur2 $cur2")
            val prev1 = cur1
            val prev2 = cur2
            cur1 = next(map[cur1.second][cur1.first], lastVisited1, cur1)
            cur2 = next(map[cur2.second][cur2.first], lastVisited2, cur2)
            steps += 1
            loop.add(cur1)
            loop.add(cur2)
            lastVisited1 = prev1
            lastVisited2 = prev2
        }

        println("Part 1: $steps")

        // Ray edge counting https://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon
        val inside = mutableSetOf<Pair<Int, Int>>()
        for (y in map.indices) {
            var crossedLoopEdges = 0
            var parallellEdge: Char? = null
            for (x in map[y].indices) {
                if (loop.contains(x to y)) {
                    if (map[y][x] == '-') {
                        continue
                    }
                    if (horizontalPipeStart.contains(map[y][x])) {
                       parallellEdge = map[y][x]
                    }
                    // Don't count parallel edges that are actually vertical twice
                    if (horizontalPipeEnd.contains(map[y][x])) {
                       if (parallellEdge == 'L' && map[y][x] == '7'
                           || parallellEdge == 'F' && map[y][x] == 'J') {
                           parallellEdge = null
                           continue
                       }
                    }
                    crossedLoopEdges += 1
                }
                else if (crossedLoopEdges % 2 == 1) {
                    inside.add(x to y)
                }
            }
        }
        //println("loop: $loop")
        println("Inside: $inside")
        //println("Part 2: ${map.size * map[0].length - loop.size - outer.size}")
        println("Part 2: ${inside.size}")
    }
}

val horizontalPipeStart = mutableSetOf('F', 'L', 'S')
val horizontalPipeEnd = mutableSetOf('J', '7', 'S')

val upwards = setOf('|', 'F', '7')
val downwards = setOf('|', 'L', 'J')
val leftwards = setOf('-', 'L', 'F')
val rightwards = setOf('-', '7', 'J')

private fun next(
    cur: Char,
    prev: Pair<Int, Int>,
    pos: Pair<Int, Int>
): Pair<Int, Int> {
    if (pos.second - prev.second > 0) {
        // Coming down
        return when (cur) {
            'L' -> pos.first + 1 to pos.second
            '|' -> pos.first to pos.second + 1
            'J' -> pos.first - 1 to pos.second
            else -> throw IllegalArgumentException("Oh noe")
        }
    }
    if (pos.second - prev.second < 0) {
        // Going up
        return when (cur) {
            'F' -> pos.first + 1 to pos.second
            '|' -> pos.first to pos.second - 1
            '7' -> pos.first - 1 to pos.second
            else -> throw IllegalArgumentException("Oh noe")
        }
    }
    if (pos.first - prev.first > 0) {
        // Going right
        return when (cur) {
            '7' -> pos.first to pos.second + 1
            '-' -> pos.first + 1 to pos.second
            'J' -> pos.first to pos.second - 1
            else -> throw IllegalArgumentException("Oh noe")
        }
    }
    if (pos.first - prev.first < 0) {
        // Going left
        return when (cur) {
            'L' -> pos.first to pos.second - 1
            '-' -> pos.first - 1 to pos.second
            'F' -> pos.first to pos.second + 1
            else -> throw IllegalArgumentException("Oh noe")
        }
    }
    throw IllegalArgumentException("Oh noe")
}

private fun paths(
    map: List<String>,
    pos: Pair<Int, Int>,
): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    if (canGoUp(pos, map)) {
        result.add(pos.first to pos.second - 1)
    }
    if (canGoDown(pos, map)) {
        result.add(pos.first to pos.second + 1)
    }
    if (canGoLeft(pos, map)) {
        result.add(pos.first - 1 to pos.second)
    }
    if (canGoRight(pos, map)) {
        result.add(pos.first + 1 to pos.second)
    }
    return result
}

private fun canGoRight(pos: Pair<Int, Int>, map: List<String>) =
    pos.first < map.get(0).length - 1 && rightwards.contains(map[pos.second][pos.first + 1])

private fun canGoLeft(pos: Pair<Int, Int>, map: List<String>) =
    pos.first > 0 && leftwards.contains(map[pos.second][pos.first - 1])

private fun canGoUp(pos: Pair<Int, Int>, map: List<String>) =
    pos.second > 0 && upwards.contains(map[pos.second - 1][pos.first])

private fun canGoDown(pos: Pair<Int, Int>, map: List<String>) =
    pos.second < map.size - 1 && downwards.contains(map[pos.second + 1][pos.first])
