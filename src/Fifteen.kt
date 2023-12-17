fun main() {
    forLinesIn("day15/input.txt") {
        val seqs = it.first().split(",").toList()
        val codes = seqs.map { hash(it) }
        println("Part 1: ${codes.sum()}")

        val boxes = mutableMapOf<Int, LinkedHashMap<String, Int>>()
        for (i in seqs.indices) {
            val seq = seqs[i]
            val split = seq.split("-", "=")
            val label = split[0]
            val code = hash(label)
            if (! boxes.containsKey(code)) {
                boxes[code] = LinkedHashMap()
            }
            if (seq.contains("-")) {
                boxes[code]!!.remove(label)
            }
            else {
                val fl = split[1].trim().toInt()
                boxes[code]!![label] = fl
            }
            //println("$boxes")
        }
        var focusPower = 0
        for (i in 0..255) {
            val lenses = boxes[i]?.entries?.toList() ?: listOf()
            for (lensi in lenses.indices) {
                val lensPwr = (1 + i) * (1 + lensi) * lenses[lensi].value
                focusPower += lensPwr
            }
        }

        println("Part 2: ${focusPower}")
    }
}

fun hash(seqs: String): Int {
    var cur = 0
    for (c in seqs) {
        cur += c.code
        cur *= 17
        cur %= 256
    }
    return cur
}
