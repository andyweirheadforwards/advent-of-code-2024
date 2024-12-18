package Day13

import PROFILE_REPEAT
import readInput
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day13")

          val costOfAllPrizesOne = input.costOfAllPrizesOne()
          println(
              "What is the fewest tokens you would have to spend to win all possible prizes? $costOfAllPrizesOne")

          val costOfAllPrizesTwo = input.costOfAllPrizesTwo()
          println(
              "What is the fewest tokens you would have to spend to win all possible prizes? $costOfAllPrizesTwo")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
