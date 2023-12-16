import java.lang.RuntimeException

fun main() {
    forLinesIn("day12/input.txt") {
        val input = it.map {
            val spl = it.split(" ")
            spl[0] to spl[1].split(",").map { it.toInt() }.toList()
        }.toList()
        //println("$input")
        println("Part 1: ${input.map { check(transf(it), it.second, mutableMapOf()) }.sum()}")

        println("Part 2: ${input.mapIndexed {i, p -> 
            //println("$p ")
            val exdInput = transf(p)
            val groups = p.second
            //println("Index $i of ${input.size - 1}")
            val res = check("${exdInput}U${exdInput}U${exdInput}U${exdInput}U${exdInput}", 
                listOf(groups, groups, groups, groups, groups).flatMap { it }.toList(),
                mutableMapOf()
            )
            //println(" -> $res")
            res
        }.sum()}")
    }
}

// Just trasforming for easier use with regexp
private fun transf(line: Pair<String, List<Int>>): String {
    val data = line.first.map {
        when (it) {
            '?' -> 'U'
            '.' -> '1'
            '#' -> '0'
            else -> throw RuntimeException("WAT")
        }
    }.joinToString("")
    return data
}

fun check(input: String, groups: List<Int>, cache: MutableMap<Key, Long>): Long {
    val cacheKey = Key(input, groups)
    if (cache.containsKey(cacheKey)) {
        return cache[cacheKey]!!
    }
    if (groups.isEmpty() && ! input.contains("0")) {
        return 1
    }
    if (input.isEmpty() || groups.isEmpty()) {
        return 0
    }
    val nextGroup = groups.first()
    if (input.length < nextGroup || input.length < groups.sum() + groups.size - 1) {
        return 0
    }
    val next = Regex("([0U]){$nextGroup,$nextGroup}(1|U|$)")
    var result = 0L
    val match = next.matchesAt(input, 0)
    if (match) {
        if (input.length > nextGroup) {
            result += check(input.substring(nextGroup + 1), groups.drop(1), cache)
        } else {
            result += check(input.substring(nextGroup), groups.drop(1), cache)
            return result
        }
    }
    if (input.startsWith("1") || input.startsWith("U")) {
        result += check(input.substring(1), groups, cache)
    }
    cache[cacheKey] = result
    return result
}

data class Key(val str: String, val groups: List<Int>)