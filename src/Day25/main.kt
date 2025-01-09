package Day25

import Utils.PROFILE_REPEAT
import Utils.readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("Day25")

            val puzzle = Puzzle(input)
            val solutionOne = puzzle.solveOne()

            println("How many unique lock/key pairs fit together without overlapping in any column? $solutionOne")
        }
    }
        .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}