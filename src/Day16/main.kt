package Day16

import PROFILE_REPEAT
import readInput
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day16")
          val maze = Maze(input)
          val (score, paths) = maze.solve()
          println("What is the lowest score a Reindeer could possibly get? $score")

          var tiles = countTilesOnBestPath(paths)

          println(
              "How many tiles are part of at least one of the best paths through the maze? $tiles")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

fun countTilesOnBestPath(paths: List<MazePath>): Int = paths.flatten().toSet().size
