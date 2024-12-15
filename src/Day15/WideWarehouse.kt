package Day15

import Direction
import Grid
import getSymbolAt
import move
import setSymbolAt
import java.awt.Point

class WideWarehouse(grid: Grid, directions: List<Direction>, robot: Point) :
    Warehouse(grid, directions, robot) {

  companion object {

    const val WIDE_BOX = "[]"

    val gridMap =
        mapOf<Char, String>(
            '#' to "##",
            'O' to "[]",
            '.' to "..",
            '@' to "@.",
        )

    operator fun invoke(input: String): WideWarehouse {

      val grid: Grid =
          extractGrid(input).map { it.joinToString("") { gridMap[it].toString() }.toCharArray() }
      val directions = extractDirections(input)
      val robot = extractRobot(grid)

      grid.setSymbolAt(robot, SPACE)

      return WideWarehouse(grid, directions, robot)
    }
  }

  override val gpsCoordinates: List<Int>
    get() =
        grid
            .flatMapIndexed { y, line ->
              line
                  .mapIndexed { x, symbol -> if (symbol == WIDE_BOX.first()) Point(x, y) else null }
                  .filterNotNull()
            }
            .map { gpsCoordinate(it) }

  override fun getCurrentBoxCount() =
      grid.joinToString("") { it.joinToString("") }.count { WIDE_BOX.contains(it) }

  override fun isBox(symbol: Char): Boolean = WIDE_BOX.contains(symbol)

  override fun pushBox(box: Point, direction: Direction): Boolean {
    if (listOf(Direction.EAST, Direction.WEST).contains(direction))
        return super.pushBox(box, direction)

    return pushRow(getWideBox(box), direction)
  }

  fun pushRow(row: Set<Point>, direction: Direction): Boolean {
    if (!isValidMove(row, direction)) return false

    val next = rowBoxes(nextRow(row, direction))
    if (next.isNotEmpty()) pushRow(next, direction)

    row.forEach {
      val from = Point(it)
      val to = Point(from)
      to.move(direction)

      grid.setSymbolAt(to, grid.getSymbolAt(from))
      grid.setSymbolAt(from, SPACE)
    }
    return true
  }

  fun isValidMove(row: Set<Point>, direction: Direction): Boolean {
    val next = nextRow(row, direction)

    return next.all {
      val symbol = grid.getSymbolAt(it)
      when {
        isWall(symbol) -> false
        isSpace(symbol) -> true
        else -> isValidMove(getWideBox(it), direction)
      }
    }
  }

  fun rowBoxes(row: Set<Point>) = row.filter { isBox(grid.getSymbolAt(it)) }.toSet()

  fun nextRow(row: Set<Point>, direction: Direction): Set<Point> =
      row.flatMap {
            val next = Point(it)
            next.move(direction)

            val symbol = grid.getSymbolAt(next)
            when {
              isBox(symbol) -> getWideBox(next)
              else -> listOf(next)
            }
          }
          .toSet()

  fun getWideBox(box: Point): Set<Point> =
      when (grid.getSymbolAt(box)) {
        ']' -> listOf(Point(box.x - 1, box.y), Point(box))
        '[' -> listOf(Point(box), Point(box.x + 1, box.y))
        else -> error("Invalid box")
      }.toSet()
}
