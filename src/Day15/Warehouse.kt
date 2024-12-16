package Day15

import Direction
import Grid
import copy
import getSymbolAt
import move
import setSymbolAt
import string
import java.awt.Point

open class Warehouse
internal constructor(val grid: Grid, val directions: List<Direction>, robot: Point) {

  companion object {

    const val ROBOT = '@'
    const val WALL = '#'
    const val BOX = 'O'
    const val SPACE = '.'

    fun extractGrid(input: String) =
        input.trim().lines().takeWhile { it.isNotBlank() }.map { it.toCharArray() }

    fun extractDirections(input: String) =
        input
            .trim()
            .lines()
            .takeLastWhile { it.isNotBlank() }
            .joinToString("")
            .map {
              when (it) {
                '^' -> Direction.NORTH
                'v' -> Direction.SOUTH
                '<' -> Direction.WEST
                '>' -> Direction.EAST
                else -> error("Invalid symbol $it")
              }
            }

    fun extractRobot(grid: Grid): Point {
      val y = grid.indexOfFirst { it.contains(ROBOT) }
      val x = grid[y].indexOf(ROBOT)

      return Point(x, y)
    }

    operator fun invoke(input: String): Warehouse {

      val grid = extractGrid(input)
      val directions = extractDirections(input)
      val robot = extractRobot(grid)

      grid.setSymbolAt(robot, SPACE)

      return Warehouse(grid, directions, robot)
    }
  }

  var robot: Point = robot
    private set

  open fun getCurrentBoxCount() = grid.joinToString("") { it.joinToString("") }.count { it == BOX }

  open val gpsCoordinates: List<Int>
    get() =
        grid
            .flatMapIndexed { y, line ->
              line
                  .mapIndexed { x, symbol -> if (symbol == BOX) Point(x, y) else null }
                  .filterNotNull()
            }
            .map { gpsCoordinate(it) }

  fun gpsCoordinate(point: Point): Int = 100 * point.y + point.x

  val gpsSum: Int
    get() = gpsCoordinates.sum()

  fun move() = move(directions.size)

  fun move(number: Int) =
      directions.take(number).forEach { direction ->
        val symbol = grid.getSymbolAt(robot, direction)
        when {
          isWall(symbol) -> {}
          isSpace(symbol) -> robot.move(direction)
          isBox(symbol) -> {
            val box = Point(robot)
            box.move(direction)
            if (pushBox(box, direction)) {
              robot.move(direction)
              grid.setSymbolAt(robot, SPACE)
            }
          }
          else -> error("Invalid direction $direction")
        }
      }

  open fun pushBox(box: Point, direction: Direction): Boolean {
    val from = Point(box)
    val to = Point(from)
    to.move(direction)

    val symbol = grid.getSymbolAt(to)
    return when {
      isWall(symbol) -> false
      isSpace(symbol) -> {
        grid.setSymbolAt(to, grid.getSymbolAt(from))
        true
      }
      isBox(symbol) -> {
        val canPush = pushBox(to, direction)
        if (canPush) grid.setSymbolAt(to, grid.getSymbolAt(from))
        canPush
      }
      else -> error("Invalid box move $direction")
    }
  }

  internal open fun isBox(symbol: Char): Boolean = symbol.isSymbol(BOX)

  internal fun isSpace(symbol: Char): Boolean = symbol.isSymbol(SPACE)

  internal fun isWall(symbol: Char): Boolean = symbol.isSymbol(WALL)

  fun Char.isSymbol(other: Any) =
      when (other) {
        is Char -> this == other
        is String -> other.contains(this)
        else -> error("Invalid type $other")
      }

  override fun toString(): String = grid.copy().setSymbolAt(robot, ROBOT).string
}
