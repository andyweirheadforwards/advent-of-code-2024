package day21

import utils.PROFILE_REPEAT
import utils.readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("day21")

            val puzzle = Puzzle()
            val solutionOne = puzzle.solveOne(input)

            println("What is the sum of the complexities of the five codes on your list? $solutionOne")

            val solutionTwo = puzzle.solveTwo(input)
            println("What is the sum of the complexities of the five codes on your list? $solutionTwo")
        }
    }.let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
