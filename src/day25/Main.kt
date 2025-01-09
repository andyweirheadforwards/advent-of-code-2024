package day25

import utils.PROFILE_REPEAT
import utils.readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("day25")

            val puzzle = Puzzle(input)
            val solutionOne = puzzle.solveOne()

            println("How many unique lock/key pairs fit together without overlapping in any column? $solutionOne")
        }
    }.let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
