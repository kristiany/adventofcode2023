import kotlin.math.abs
import kotlin.math.max

fun main() {
    forLinesIn("day11/input.txt") {
        val input = it.toList()
        val glx = mutableListOf<Pos>()
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c == '#') {
                    glx.add(Pos(x, y, 0, 0))
                }
            }
        }
        val emptyRows = input.indices.minus(glx.map { it.y }.toSet())
        val emptyCols = input[0].indices.minus(glx.map { it.x }.toSet())
        val expanded = glx.map { gl ->
            val yx = emptyRows.filter { it < gl.y }.count()
            val xx = emptyCols.filter { it < gl.x }.count()
            Pos(gl.x, gl.y, xx, yx)
        }.toList()
        val dists = distances(expanded, 1)
        println("Part 1: $dists")

        val dists2 = distances(expanded, 1000000)
        println("Part 2: $dists2")
    }
}

private fun distances(expanded: List<Pos>, expandFactor: Long): Long {
    var dists = 0L
    val xpf = max(expandFactor - 1L, 1L)
    val combos = expanded.flatMapIndexed { i, a ->
        expanded.subList(i + 1, expanded.size).map { Pair(a, it) }
    }.toSet()
    //println("${combos.size}")
    for (p in combos) {
        val xd = abs((p.second.x + p.second.xf * xpf) - (p.first.x + p.first.xf * xpf))
        val yd = abs((p.second.y + p.second.yf * xpf) - (p.first.y + p.first.yf * xpf))
        dists += xd + yd
        //println("Glx $p dist ${xd + yd}")
    }
    return dists
}

data class Pos(val x: Int, val y: Int, val xf: Int, val yf: Int)