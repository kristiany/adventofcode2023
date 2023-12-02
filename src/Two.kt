import java.lang.Integer.max

fun main() {
    forLinesIn("day02/input.txt") {
        val games = it.map { line ->
            val first = line.split(": ")
            val id = first.get(0).split(" ").get(1).toInt()
            val turns = first[1].split("; ")
                .map { turn ->
                    turn.split(", ")
                        .map { part ->
                            part.split(" ")
                        }.map { t -> ( t[1] to t[0].toInt() ) }
                        .toMap()
                }
                .map { Turn(
                    it.getOrDefault("red", 0),
                    it.getOrDefault("green", 0),
                    it.getOrDefault("blue", 0))
                }
                .toList()
            Game(id, turns)
        }
        .toList()

        println(games)

        val result1 = games.filter { g ->
            g.turns.all { t -> t.blue <= 14 && t.green <= 13 && t.red <= 12  }
        }.sumOf { it.id }
        println("Part 1: $result1")

        val result2 = games.map {g ->
            val fewest = g.turns.reduce {acc, t ->
                Turn(max(acc.red, t.red), max(acc.green, t.green), max(acc.blue, t.blue))
            }
            fewest.red * fewest.green * fewest.blue
        }.sum()
        println("Part 2: $result2")
    }
}

data class Turn(val red: Int, val green: Int, val blue: Int)
data class Game(val id: Int, val turns: List<Turn>)