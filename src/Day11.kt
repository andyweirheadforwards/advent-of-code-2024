import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day11").stones

          val blink25 = input.countStonesAfterBlinks(25) // 193607
          println("How many stones will you have after blinking 25 times?             $blink25")

          val blink75 = input.countStonesAfterBlinks(75)
          println("How many stones would you have after blinking a total of 75 times? $blink75")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias Stone = String

typealias StoneString = String

typealias Stones = List<Stone>

private const val ODD_STONE_DIGIT_MULTIPLIER = 2024

val Stone.isEven: Boolean
  get() = length % 2 == 0

val StoneString.stones: Stones
  get() = split(" ")

val Stones.stoneString: String
  get() = joinToString(" ")

fun Stones.blink(blinks: Int = 1): Stones {
  var stones = this
  repeat(blinks) {
    stones =
        stones.flatMap {
          when {
            it == "0" -> listOf("1")
            it.isEven ->
                listOf(
                    it.substring(0, it.length / 2),
                    it.substring(it.length / 2, it.length).toLong().toString())

            else -> listOf(it.toLong().times(ODD_STONE_DIGIT_MULTIPLIER).toString())
          }
        }
  }
  return stones
}

typealias StonesMap = MutableMap<Stone, Long>

val Stones.stonesMap: StonesMap
  get() = mutableMapOf<Stone, Long>().apply { addStones(this@stonesMap) }

fun StonesMap.addStones(stones: Stones): StonesMap {
  stones.forEach { this.put(it, this.getOrDefault(it, 0L) + 1) }
  return this
}

fun StonesMap.blink(blinks: Int): StonesMap {
  var stonesMap = this
  repeat(blinks) {
    val updatedMap: StonesMap = mutableMapOf()
    stonesMap.forEach { stone ->
      listOf(stone.key).blink().forEach {
        updatedMap.put(it, updatedMap.getOrDefault(it, 0L) + stone.value)
      }
    }
    stonesMap = updatedMap
  }

  return stonesMap
}

fun Stones.countStonesAfterBlinks(blinks: Int): Long = stonesMap.blink(blinks).values.sumOf { it }
