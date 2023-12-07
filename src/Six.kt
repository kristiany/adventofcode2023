
fun main() {
    // Input part 1
    val times = listOf(53, 91, 67, 68)
    val distances = listOf(250, 1330, 1081, 1025)

    // Test part 1
    //val times = listOf(7, 15, 30)
    //val distances = listOf(9, 40, 200)

    // Test part 2
    //val times = listOf(71530)
    //val distances = listOf(940200)

    var score = 1
    for (i in times.indices) {
        val time = times[i]
        val record = distances[i]

        var lower = 0
        do {
            lower += 1
            val adist = (time - lower) * /*speed*/ lower
        } while (adist <= record)
        //println("$i lower $lower")

        var upper = lower
        do {
            upper += 1
            val adist = (time - upper) * /*speed*/ upper
        } while (adist > record)
        upper -= 1
        //println("$i upper $upper")

        score *= (upper - lower + 1)
    }
    println("Part1: $score")

    // Input part 2
    val time = 53916768
    val record = 250133010811025

    // solving this one with basic math
    // 250133010811025 = (53916768 - x) * x
    // 250133010811025 = 53916768 * x - x^2
    // x^2 - 53916768x + 250133010811025 = 0
    // x = 26958384 +- sqrt(726754467891456 - 250133010811025)
    // x = 26958384 +- 21831661,80 ~ 21831662
    // x_1 = 48790045,8, x_2 = 5126722,2
    // x_1 - x_2 = 43663323,6
    // 👆🏼These are the turning points for the record distance
    // Validation:
    // Lower bound
    // (53916768 - 5126722) * 5126722 = 250133002209212
    // (53916768 - 5126723) * 5126723 = 250133045872535
    // Upper bound
    // (53916768 - 48790045) * 48790045 = 250133045872535
    // (53916768 - 48790046) * 48790046 = 250133002209212

    // 48790045 - 5126723 + 1 = 43663323

    println("Part2: 43663323")
}