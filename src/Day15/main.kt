package Day15

import PROFILE_REPEAT
import readInput
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day15")
          val warehouse = Warehouse(input)
          warehouse.move()
          println("What is the sum of all boxes' GPS coordinates?       ${warehouse.gpsSum}")

          val wideWarehouse = WideWarehouse(input)
          wideWarehouse.move()
          println("What is the sum of all boxes' final GPS coordinates? ${wideWarehouse.gpsSum}")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
