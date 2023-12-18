fun main() {
    forLinesIn("day16/input.txt") {
        val map = it.map { it }.toList()
        val startingBeam = Beam(P(-1, 0), 1)
        val energized = run(map, startingBeam)

        println("Part 1: ${energized.map { it.p }.toSet().size}")

        var maxEnergize = P(0, 0) to 0
        for (y in map.indices) {
            var e = run(map, Beam(P(-1, y), 1)).map { it.p }.toSet().size
            if (e > maxEnergize.second) {
                maxEnergize = P(-1, y) to e
            }
            e = run(map, Beam(P(map[0].length, y), 3)).map { it.p }.toSet().size
            if (e > maxEnergize.second) {
                maxEnergize = P(map[0].length, y) to e
            }
        }
        for (x in map[0].indices) {
            var e = run(map, Beam(P(x, -1), 2)).map { it.p }.toSet().size
            if (e > maxEnergize.second) {
                maxEnergize = P(x, -1) to e
            }
            e = run(map, Beam(P(x, map.size), 0)).map { it.p }.toSet().size
            if (e > maxEnergize.second) {
                maxEnergize = P(x, map.size) to e
            }
        }
        println("Part 2: $maxEnergize")
    }
}

private fun run(map: List<String>, startingBeam: Beam): Set<Beam> {
    val energized = mutableSetOf<Beam>()
    val beams = ArrayDeque<Beam>()
    beams.add(startingBeam)
    while (beams.isNotEmpty()) {
        val beam = beams.removeFirst()
        // If we've been here already with the exact same dir, we can stop
        if (energized.contains(beam)) {
            continue
        }
        energized.add(beam)
        //println("E ${beam}")
        val nextP = nextPos(beam)
        // Outside of map, it's lost do nothing
        if (nextP.x < 0 || nextP.x >= map[0].length || nextP.y < 0 || nextP.y >= map.size) {
            continue
        }
        val mapped = map[nextP.y][nextP.x]
        //println("   nextP $nextP -> $mapped")
        if (mapped == '.') {
            beams.add(Beam(nextP, beam.dir))
            //println("   . passing by")
        } else if (mapped == '|') {
            if (beam.dir == 0 || beam.dir == 2) {
                beams.add(Beam(nextP, beam.dir))
                //println("   | passing by")
            } else {
                beams.add(Beam(nextP, 0))
                beams.add(Beam(nextP, 2))
                //println("   | splitting up and down")
            }
        } else if (mapped == '-') {
            if (beam.dir == 1 || beam.dir == 3) {
                beams.add(Beam(nextP, beam.dir))
                //println("   - passing by")
            } else {
                beams.add(Beam(nextP, 1))
                beams.add(Beam(nextP, 3))
                //println("   - splitting left and right")
            }
        } else if (mapped == '/') {
            //println("   / mirroring")
            when (beam.dir) {
                0 -> beams.add(Beam(nextP, 1))
                1 -> beams.add(Beam(nextP, 0))
                2 -> beams.add(Beam(nextP, 3))
                3 -> beams.add(Beam(nextP, 2))
                else -> throw RuntimeException("What dir?")
            }
        } else if (mapped == '\\') {
            //println("   \\ mirroring")
            when (beam.dir) {
                0 -> beams.add(Beam(nextP, 3))
                1 -> beams.add(Beam(nextP, 2))
                2 -> beams.add(Beam(nextP, 1))
                3 -> beams.add(Beam(nextP, 0))
                else -> throw RuntimeException("What dir?")
            }
        }
        //println("   queue ${beams}")
    }
    return energized - startingBeam
}

private fun nextPos(beam: Beam): P {
    val nextP = when (beam.dir) {
        0 -> P(beam.p.x, beam.p.y - 1)
        1 -> P(beam.p.x + 1, beam.p.y)
        2 -> P(beam.p.x, beam.p.y + 1)
        3 -> P(beam.p.x - 1, beam.p.y)
        else -> throw RuntimeException("Oh noes")
    }
    return nextP
}

data class P(val x: Int, val y: Int)
/*
    dir:
        0 - up
        1 - right
        2 - down
        3 - left
 */
data class Beam(val p: P, val dir: Int)
