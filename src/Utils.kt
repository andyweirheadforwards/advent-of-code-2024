import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readText

const val PROFILE_REPEAT = 1

typealias GridString = String

typealias Grid = List<CharArray>

fun readInput(name: String) = Path("data/$name.txt").readText().trim().lines().joinToString("\n")

val CharArray.string: String
  get() = joinToString("")

val GridString.grid: Grid
  get() = trim().lines().map { it.toCharArray() }

val Grid.string: GridString
  get() = joinToString("\n") { it.joinToString("") }

fun Grid.getSymbolAt(point: Point, direction: Direction? = null): Char =
    when (direction) {
      null -> this[point.y][point.x]
      Direction.NORTH -> getSymbolAt(Point(point.x, point.y - 1))
      Direction.SOUTH -> getSymbolAt(Point(point.x, point.y + 1))
      Direction.EAST -> getSymbolAt(Point(point.x + 1, point.y))
      Direction.WEST -> getSymbolAt(Point(point.x - 1, point.y))
      else -> error("Invalid direction $direction")
    }

fun Grid.setSymbolAt(point: Point, symbol: Char = GUARD_LOCATION): Grid {
  this[point.y][point.x] = symbol
  return this
}

fun Grid.getNeighbours(point: Point): List<Point> =
    listOf(
            Point(point.x, point.y - 1),
            Point(point.x - 1, point.y),
            Point(point.x + 1, point.y),
            Point(point.x, point.y + 1),
        )
        .filter { it.x >= 0 && it.x <= lastIndexX && it.y >= 0 && it.y <= lastIndexY }

fun Grid.isValidPoint(point: Point) =
    when {
      point.x < 0 || point.x > this.first().lastIndex -> false
      point.y < 0 || point.y > this.lastIndex -> false
      else -> true
    }

operator fun Point.plus(point: Point): Point {
  val newPoint = this.clone() as Point
  newPoint.translate(point.x, point.y)
  return newPoint
}

operator fun Point.minus(point: Point): Point {
  val newPoint = this.clone() as Point
  newPoint.translate(-point.x, -point.y)
  return newPoint
}

fun Point.diff(point: Point): Point = point - this

fun Point.move(direction: Direction): Unit =
    when (direction) {
      Direction.NORTH -> move(x, y - 1)
      Direction.SOUTH -> move(x, y + 1)
      Direction.EAST -> move(x + 1, y)
      Direction.WEST -> move(x - 1, y)
      else -> error("Invalid direction $direction")
    }

fun clearScreen() {
  System.out.print("\\033[H\\033[2J")
  System.out.flush()
}
