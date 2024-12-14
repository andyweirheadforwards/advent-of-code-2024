import java.awt.Point
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day14")
          var tiles = Pair(101, 103)

          val restroomOne = Restroom(tiles, input)
          restroomOne.move(100)
          val safetyFactor = restroomOne.safetyFactor

          println(
              "What will the safety factor be after exactly 100 seconds have elapsed? $safetyFactor")

          val restroomTwo = Restroom(tiles, input)
          var seconds = 0
          while (true) {
            while (!restroomTwo.hasRowOfRobots) {
              restroomTwo.move(1)
              seconds++
            }
            println(restroomTwo.toString())
            println("Restroom after $seconds seconds.")
            readln()
            restroomTwo.move(1)
            seconds++
          }
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias RobotListString = String

typealias RobotList = List<Robot>

fun RobotListString.toRobotList(tiles: Pair<Int, Int>): RobotList =
    trim().lines().map { Robot(it, tiles) }

typealias Tiles = Pair<Int, Int> // cols, rows

data class Restroom(val tiles: Tiles, var robots: RobotList) {
  companion object {
    operator fun invoke(tiles: Tiles, string: RobotListString): Restroom {
      return Restroom(tiles, string.toRobotList(tiles))
    }
  }

  val safetyFactor: Int
    get() = countRobots().reduce { acc, quadrant -> acc * quadrant }

  val hasRowOfRobots: Boolean
    get() {
      val robotPositions = robots.map { it.position }

      // Group robots by rows and columns
      val rows = robotPositions.groupBy { it.y }
      val columns = robotPositions.groupBy { it.x }

      // Check for consecutive robots in rows or columns
      return (rows.values + columns.values).any { positions ->
        hasConsecutive(positions.map { it.x })
      }
    }

  private val grid = "${".".repeat(tiles.first)}\n".repeat(tiles.second).grid

  fun move(seconds: Int) {
    robots = robots.map { it.move(seconds) }
  }

  fun countRobots(): List<Int> {
    val (cols, rows) = tiles
    val quadrantCols = cols / 2
    val quadrantRows = rows / 2

    val quadrants =
        listOf(
            mutableListOf<Point>(),
            mutableListOf<Point>(),
            mutableListOf<Point>(),
            mutableListOf<Point>())

    robots
        .map { it.position }
        .forEach {
          when {
            it.x < quadrantCols && it.y < quadrantRows -> quadrants[0].add(it)
            it.x > quadrantCols && it.y < quadrantRows -> quadrants[1].add(it)
            it.x < quadrantCols && it.y > quadrantRows -> quadrants[2].add(it)
            it.x > quadrantCols && it.y > quadrantRows -> quadrants[3].add(it)
          }
        }

    return quadrants.map { it.size }
  }

  override fun toString(): String =
      grid
          .copy()
          .also {
            robots.map { it.position }.toSet().forEach { position -> it.setSymbolAt(position, '*') }
          }
          .string

  fun hasConsecutive(positions: List<Int>, length: Int = 10): Boolean =
      positions
          .sorted()
          .windowed(length - 1) // Sliding window of size `length`
          .any { window -> window.zipWithNext().all { (a, b) -> b == a + 1 } }
}

data class Robot(val position: Point, val velicity: Point, val tiles: Pair<Int, Int>) {
  companion object {
    val POSITION_REGEX = "p=(\\d+),(\\d+)".toRegex()
    val VELOCITY_REGEX = "v=(-?\\d+),(-?\\d+)".toRegex()

    operator fun invoke(pX: Int, pY: Int, vX: Int, vY: Int, tiles: Pair<Int, Int>): Robot =
        Robot(Point(pX, pY), Point(vX, vY), tiles)

    operator fun invoke(string: String, tiles: Pair<Int, Int>): Robot {
      val (pX, pY) = POSITION_REGEX.find(string)!!.destructured.toList().map { it.toInt() }
      val (vX, vY) = VELOCITY_REGEX.find(string)!!.destructured.toList().map { it.toInt() }
      return Robot(pX, pY, vX, vY, tiles)
    }
  }

  fun move(seconds: Int): Robot {
    val (cols, rows) = tiles

    var newX = (position.x + (velicity.x * seconds)) % cols
    var newY = (position.y + (velicity.y * seconds)) % rows

    if (newX < 0) newX = cols + newX
    if (newY < 0) newY = rows + newY

    return copy(position = Point(newX, newY))
  }

  override fun toString(): String = "p=${position.x},${position.y} v=${velicity.x},${velicity.y}"
}
