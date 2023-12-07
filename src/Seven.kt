import java.util.Comparator

fun main() {
    forLinesIn("day07/input.txt") {
        val hands = it.map {
            val parts = it.split(" ")
            val cards = parts[0].map { toNr(it) }.toList()
            val bid = parts[1].toInt()
            Hand(cards, bid)
        }.toList()
        .sortedWith(HandComparator)

        val result = calcResult(hands)
        println("Part 1: $result")

        val jokerHands = hands.sortedWith(JokerComparator)
        val result2 = calcResult(jokerHands)
        println("Part 2: $result2")
    }
}

private fun calcResult(hands: List<Hand>): Int {
    var result = 0
    var rank = hands.size
    for (h in hands) {
        result += rank * h.bid
        println("R $rank: $h -> ${h.jokerScore()}")
        rank -= 1
    }
    return result
}

fun toNr(c: Char): Int {
    return when (c) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 11
        'T' -> 10
        else -> c.toString().toInt()
    }
}

data class Hand(val cards: List<Int>, val bid: Int) {
    fun score(): Int {
        val freqMap = mutableMapOf<Int, Int>()
        cards.forEach { freqMap[it] = freqMap.getOrDefault(it, 0) + 1 }
        val freq = freqMap.values.toSet()
        // Five of a kind
        //println("$freq, $freqMap")
        if (freq.contains(5)) {
            return 100
        }
        // Four of a kind
        if (freq.contains(4)) {
            return 90
        }
        // Full house
        if (freq.contains(2) && freq.contains(3)) {
            return 80
        }
        // Three of a kind
        if (freq.contains(3)) {
            return 70
        }
        // Two pairs
        if (freqMap.filterValues { it == 2 }.count() == 2) {
            return 60
        }
        // Two of a kind
        if (freq.contains(2)) {
            return 50
        }
        return 1
    }

    fun jokerScore(): Int {
        val freqMap = mutableMapOf<Int, Int>()
        cards.forEach { freqMap[it] = freqMap.getOrDefault(it, 0) + 1 }
        val freq = freqMap.filterKeys { it != 11 }.values.toSet()
        val jokers = cards.count { it == 11 }
        // Five of a kind
        //println("$freq, $freqMap")
        if (freq.contains(5) || freq.contains(5 - jokers) || jokers == 5) {
            return 100
        }
        // Four of a kind
        if (freq.contains(4) || freq.contains(4 - jokers) || jokers == 4) {
            return 90
        }
        // Full house
        if (freq.contains(2) && freq.contains(3)
            || jokers == 1 && (freq.contains(1) && freq.contains(3) || freqMap.filterValues { it == 2 }.count() == 2)
            || jokers == 2 && (freq.contains(1) && freq.contains(2) || freq.contains(3))
            || jokers == 3 && (freq.contains(2) || freqMap.filterValues { it == 1 }.count() == 2)) {
            return 80
        }
        // Three of a kind
        if (freq.contains(3) || freq.contains(3 - jokers) || jokers == 3) {
            return 70
        }
        // Two pairs
        // Ugly check I know
        if (freqMap.filterValues { it == 2 }.count() == 2
            || freqMap.filterValues { it == 1 }.count() == 2 && jokers == 2
            || freqMap.filterValues { it == 2 }.count() == 1 && freqMap.filterValues { it == 1 }.count() == 1 && jokers == 1) {
            return 60
        }
        // Two of a kind
        if (freq.contains(2) || freq.contains(2 - jokers) || jokers == 2) {
            return 50
        }
        return 1
    }
}

class HandComparator {
    companion object : Comparator<Hand> {
        override fun compare(a: Hand, b: Hand): Int {
            val res = b.score().compareTo(a.score())
            if (res == 0) {
                // Battle out on card order
                for (i in 0..5) {
                    val cmp = b.cards[i].compareTo(a.cards[i])
                    if (cmp != 0) {
                        return cmp
                    }
                }
            }
            return res
        }
    }
}

class JokerComparator {
    companion object : Comparator<Hand> {
        override fun compare(a: Hand, b: Hand): Int {
            val res = b.jokerScore().compareTo(a.jokerScore())
            if (res == 0) {
                // Battle out on card order
                for (i in 0..<5) {
                    val acard = if (a.cards[i] == 11) 1 else a.cards[i]
                    val bcard = if (b.cards[i] == 11) 1 else b.cards[i]
                    val cmp = bcard.compareTo(acard)
                    //println("cmp eq ${b.cards} $bcard - ${a.cards} $acard = $cmp")
                    if (cmp != 0) {
                        return cmp
                    }
                }
                return 0
            }
            return res
        }
    }
}
