package Day18

import Utils.PROFILE_REPEAT
import Utils.coordinates
import Utils.readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("Day18")

            val ram = Ram(input)
            val (steps) = ram.solve(1024)
            println(
                "What is the minimum number of steps needed to reach the exit?                                                           $steps",
            )

            val byte = ram.solveLastByte()
            println(
                "What are the Utils.coordinates of the first byte that will prevent the exit from being reachable from your starting position? ${byte.coordinates}",
            )
        }
    }.let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
