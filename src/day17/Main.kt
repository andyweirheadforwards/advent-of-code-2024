package day17

import utils.PROFILE_REPEAT
import utils.readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("day17")
            val computer = Computer(input)
            val output = computer.output
            println(
                "What do you get if you use commas to join the values it output into a single Utils.string?                         $output",
            )

            val solution = computer.searchForA()
            println(
                "What is the lowest positive initial value for register A that causes the program to output a copy of itself? $solution",
            )
        }
    }.let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
