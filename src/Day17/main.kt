package Day17

import PROFILE_REPEAT
import readInput
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day17")
          val computer = Computer(input)
          val output = computer.run()
          println(
              "What do you get if you use commas to join the values it output into a single string? $output")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
