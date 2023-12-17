import java.lang.RuntimeException
import kotlin.math.ceil

fun main() {
    forLinesIn("day13/input.txt") {
        val patterns = mutableListOf<Pattern>()
        var builder = mutableListOf<String>()
        it.toList().forEach {
            if (it.isBlank()) {
                patterns.add(Pattern(builder.toList()))
                builder = mutableListOf()
            } else {
                builder.add(it)
            }
        }
        patterns.add(Pattern(builder.toList()))
        builder = mutableListOf()
        //println("$patterns")
        val mirrorPoints = patterns.map { mirrorPoints(it) }.toList()
        println("Part 1: ${mirrorPoints.map { it.points() }.sum()}")

        println("Part 2: ${patterns.mapIndexed { i, p -> mirrorPoints2(p, mirrorPoints[i]) } // Ugly hack
            .map { it.points() }.sum()}")
    }
}

fun mirrorPoints(p: Pattern): Result {
    //println("Pattern $p")
    val colHashes = colHashes(p)
    val rowHashes = rowHashes(p)
    return findMirrorPoint(colHashes, rowHashes, null)!!
}

fun mirrorPoints2(p: Pattern, mirrorPoint: Result): Result {
    //println("Pattern $p")
    val colHashes = colHashes(p)
    val rowHashes = rowHashes(p)
    for (y in p.data.indices) {
        for (x in p.data[0].indices) {
            val c = if (p.data[y][x] == '.') '#' else '.'
            //println("Replace $x, $y with $c: ${p.data}")
            val row = p.data[y].substring(0, x) + c + p.data[y].substring(x + 1)
            val updatedColHashes = colHashes.toMutableList()
            updatedColHashes[x] = p.data.indices.map {
                if (it == y) c else p.data[it][x]
            }.hashCode()
            val updatedRowHashes = rowHashes.toMutableList()
            updatedRowHashes[y] = row.hashCode()
            val point = findMirrorPoint(updatedColHashes, updatedRowHashes, mirrorPoint)
            if (point != null) {
                //println("Got point $point")
                return point
            }
        }
    }
    throw RuntimeException("No no no...nope")
}

private fun findMirrorPoint(
    colHashes: List<Int>,
    rowHashes: List<Int>,
    disregard: Result?
): Result? {
    val colbwd = searchColBwd(colHashes)
    if (colbwd != null && colbwd.notEqCol(disregard)) {
        return colbwd
    }
    val colfwd = searchColFwd(colHashes)
    if (colfwd != null && colfwd.notEqCol(disregard)) {
        return colfwd
    }
    val rowbwd = searchRowBwd(rowHashes)
    if (rowbwd != null && rowbwd.notEqRow(disregard)) {
        return rowbwd
    }
    val rowfwd = searchRowFwd(rowHashes)
    if (rowfwd != null && rowfwd.notEqRow(disregard)) {
        return rowfwd
    }
    return null
}

fun colHashes(p: Pattern): List<Int> {
    return p.data[0].indices.map { col ->
        p.data.indices.map { p.data[it][col] }.hashCode()
    }.toList()
}

fun rowHashes(p: Pattern): List<Int> {
    return p.data.map { it.hashCode() }.toList()
}

fun searchColBwd(hashes: List<Int>): Result? {
    for (i in (ceil(hashes.size / 2.0).toInt()..<hashes.size).reversed()) {
        val right = hashes.subList(i, hashes.size)
        val left = hashes.subList(0, i)
        //println("cols bwd left $left, right $right")
        if (startsWith(left.reversed(), right)) {
            return Result(i, null)
        }
    }
    return null
}

fun searchColFwd(hashes: List<Int>): Result? {
    for (i in 1..(hashes.size / 2)) {
        val right = hashes.subList(i, hashes.size)
        val left = hashes.subList(0, i)
        //println("cols fwd left $left, right $right")
        if (startsWith(right, left.reversed())) {
            return Result(i, null)
        }
    }
    return null
}

fun searchRowBwd(hashes: List<Int>): Result? {
    for (i in (ceil(hashes.size / 2.0).toInt()..<hashes.size).reversed()) {
        val bot = hashes.subList(i, hashes.size)
        val top = hashes.subList(0, i)
        //println("rows bwd top $top, bot $bot")
        if (startsWith(top.reversed(), bot)) {
            return Result(null, i)
        }
    }
    return null
}

fun searchRowFwd(hashes: List<Int>): Result? {
    for (i in 1..(hashes.size / 2)) {
        val bot = hashes.subList(i, hashes.size)
        val top = hashes.subList(0, i)
        //println("rows fwd top $top, bot $bot")
        if (startsWith(bot, top.reversed())) {
            return Result(null, i)
        }
    }
    return null
}
fun startsWith(list: List<Int>, start: List<Int>): Boolean {
    return list.subList(0, start.size) == start
}

data class Pattern(val data: List<String>)

data class Result(val col: Int?, val row: Int?) {
    fun points(): Int {
        return col ?: (row!! * 100)
    }

    fun notEqCol(o: Result?): Boolean {
        if (o == null || o.row != null) {
            return true
        }
        return col != null && o.col != null && col != o.col
    }

    fun notEqRow(o: Result?): Boolean {
        if (o == null || o.col != null) {
            return true
        }
        return row != null && o.row != null && row != o.row
    }
}