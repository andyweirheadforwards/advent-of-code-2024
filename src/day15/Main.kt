package day15

import utils.PROFILE_REPEAT
import utils.readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("aoc/day15")
            val warehouse = Warehouse(input)
            warehouse.move()
            println("What is the sum of all boxes' GPS Utils.coordinates?       ${warehouse.gpsSum}")

            val wideWarehouse = WideWarehouse(input)
            wideWarehouse.move()
            println("What is the sum of all boxes' final GPS Utils.coordinates? ${wideWarehouse.gpsSum}")
        }
    }.let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}
