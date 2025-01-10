package day19

import utils.PROFILE_REPEAT
import utils.readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("aoc/day19")

            val towels = input.towels
            val designs = input.designs

            val countOne = designs.count { it.canReconstruct(towels) }

            println(
                "How many designs are possible?                                                         $countOne",
            )

            val countTwo = designs.sumOf { it.canReconstructWays(towels) }
            println(
                "What do you get if you add up the number of different ways you could make each design? $countTwo",
            )
        }
    }.let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
