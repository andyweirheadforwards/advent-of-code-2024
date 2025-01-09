package Day23

import Utils.PROFILE_REPEAT
import Utils.readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("Day23")
            val lanParty = LanParty(input)
            val solutionOne = lanParty.solvePartOne()
            println("How many contain at least one computer with a name that starts with t? $solutionOne")

            val solutionTwo = lanParty.solvePartTwo()
            println("What is the password to get into the LAN party?                        $solutionTwo")
        }
    }
        .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}