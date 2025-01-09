package day13

import utils.PROFILE_REPEAT
import utils.readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("aoc/day13")

            val costOfAllPrizesOne = input.costOfAllPrizesOne()
            println(
                "What is the fewest tokens you would have to spend to win all possible prizes? $costOfAllPrizesOne",
            )

            val costOfAllPrizesTwo = input.costOfAllPrizesTwo()
            println(
                "What is the fewest tokens you would have to spend to win all possible prizes? $costOfAllPrizesTwo",
            )
        }
    }.let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
