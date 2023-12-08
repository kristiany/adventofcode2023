
fun main() {
    forLinesIn("day08/input.txt") {
        val input = it.toList()
        val lrs = input[0]
        val map = input.subList(2, input.size).map {
            val eq = it.split(" = ")
            val from = eq[0]
            val pair = eq[1].split(", ")
            val left = pair[0].substring(1)
            val right = pair[1].substring(0, 3)
            from to Pair(left, right)
        }.toMap()

        println(lrs)
        println("$map")

        var cur = "AAA"
        var ins = 0
        var steps = 0
        while (cur != "ZZZ") {
            val way = lrs.get(ins)
            ins = (ins + 1) % lrs.length
            cur = if (way == 'L') {
                map.get(cur)!!.first
            } else {
                map.get(cur)!!.second
            }
            steps++
        }
        println("Part 1: $steps")

        var currents = map.keys.filter { it.endsWith("A") }.toMutableList()
        ins = 0
        var steps2 = 0L
        var stepsToZ = mutableListOf<Long>()
        currents.forEach { stepsToZ.add(0) }
        while (! currents.all { it.endsWith("Z") } && stepsToZ.any { it == 0L }) {
            val way = lrs[ins]
            ins = (ins + 1) % lrs.length
            currents = if (way == 'L') {
                currents.map { map[it]!!.first }.toMutableList()
            } else {
                currents.map { map[it]!!.second }.toMutableList()
            }
            steps2++
            //println("$steps2 Currents $currents")
            for (i in currents.indices) {
                if (stepsToZ[i] == 0L && currents[i].endsWith("Z")) {
                    stepsToZ[i] = steps2
                }
            }
            //println("Steps to Z: $stepsToZ")
        }
        println("Steps to Z: $stepsToZ")
        // Shouldn't this be possible to calculate without iteration?
        // Perhaps: https://en.wikipedia.org/wiki/Least_common_multiple
        val largest = stepsToZ.max()
        var nr = largest
        while (! stepsToZ.map { nr % it }.all { it == 0L }) {
            nr += largest
        }

        println("Part 2: $nr")
    }
}
