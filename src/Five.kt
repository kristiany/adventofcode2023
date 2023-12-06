fun main() {
    forLinesIn("day05/input.txt") {
        val lines = it.iterator()
        val seeds = lines.next().split(": ")[1].split(" ").map { it.toLong() }.toList()
        lines.next() // Skipping empty line
        lines.next() // Skipping title
        val maps = mutableListOf<List<SeedMap>>()
        val seedToSoil = seedMap(lines)
        maps.add(seedToSoil)
        lines.next() // Skipping empty line
        val soilToFert = seedMap(lines)
        maps.add(soilToFert)
        lines.next() // Skipping empty line
        val fertToWater = seedMap(lines)
        maps.add(fertToWater)
        lines.next() // Skipping empty line
        val waterToLight = seedMap(lines)
        maps.add(waterToLight)
        lines.next() // Skipping empty line
        val lightToTemp = seedMap(lines)
        maps.add(lightToTemp)
        lines.next() // Skipping empty line
        val tempToHumidity = seedMap(lines)
        maps.add(tempToHumidity)
        lines.next() // Skipping empty line
        val humidityToLocation = seedMap(lines)
        maps.add(humidityToLocation)

        /*println("Seeds $seeds")
        println("seedToSoil $seedToSoil")
        println("soilToFert $soilToFert")
        println("fertToWater $fertToWater")
        println("waterToLight $waterToLight")
        println("lightToTemp $lightToTemp")
        println("tempToHumidity $tempToHumidity")
        println("humidityToLocation $humidityToLocation")
         */

        var lowest = Long.MAX_VALUE
        for (seed in seeds) {
            var s = seed
            //println("\nSeed $s")
            for (seedMaps in maps) {
                for (map in seedMaps) {
                    val mapped = map.applyTo(s)
                    if (mapped != null) {
                        s = mapped
                        break
                    }
                }
                //print(", $s")
            }
            if (s < lowest) {
                lowest = s
            }
        }
        println("Part1: $lowest")

        var lowest2 = Long.MAX_VALUE
        for (seedRange in seeds.chunked(2)) {
            val seedStart = seedRange.get(0)
            var len = seedRange.get(1)
            val seedEnd = seedRange.get(0) + len - 1
            //println("Seed map $i")
            var start = seedStart
            var end = seedEnd
            var step = 1000
            do {
                val s1 = applyMapping(start, maps)
                val s2 = applyMapping(start + step, maps)
                // A local low perhaps, let's search it
                if (s1 > s2) {
                    start = start - step
                    step = 1
                } else {
                    step = 1000
                }
                //println("$start - $s1")
                start = start + step
                if (s1 < lowest2) {
                    lowest2 = s1
                }
            } while (start < end)
        }
        println("Part2: $lowest2")
    }
}

private fun applyMapping(seed: Long, maps: MutableList<List<SeedMap>>): Long {
    var s = seed
    var i = 0
    for (rangeMaps in maps) {
        //println("Seed map $i")
        for (map in rangeMaps) {
            val mapped = map.applyTo(s)
            //println("Seed $seed, mapped ${mapped ?: s}")
            if (mapped != null) {
                s = mapped
                break
            }
        }
        i++
    }
    return s
}

private fun seedMap(lines: Iterator<String>): List<SeedMap> {
    val result = mutableListOf<SeedMap>()
    while (lines.hasNext()) {
        val row = lines.next()
        if (row.isEmpty()) {
            break;
        }
        val s = row.split(" ").map { it.toLong() }.toList()
        result.add(SeedMap(s[0], s[1]..<s[1] + s[2])
        )
    }
    return result
}

data class SeedMap(val dest: Long, val src: LongRange) {
    fun applyTo(srcIn: Long): Long? {
        if (src.contains(srcIn)) {
            return dest + srcIn - src.first
        }
        return null
    }
}