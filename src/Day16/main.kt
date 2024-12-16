package Day16

import PROFILE_REPEAT
import kotlin.time.measureTime
import readInput

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day16")
          val maze = Maze(input)
          val score = maze.solve()
          println("What is the lowest score a Reindeer could possibly get? $score")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
