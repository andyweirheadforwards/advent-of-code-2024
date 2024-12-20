package Day20

import PROFILE_REPEAT
import readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("Day20")

            val race = Race(input)
            val solutionOne = race.solveOne().count { it >= 100 }

            println("How many cheats would save you at least 100 picoseconds? $solutionOne")
        }
    }
        .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}