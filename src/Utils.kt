import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readText

const val PROFILE_REPEAT = 1

typealias GridString = String

typealias Grid = List<CharArray>

fun readInput(name: String) = Path("data/$name.txt").readText().trim().lines().joinToString("\n")

val CharArray.string: String get() = joinToString("")

val GridString.grid: Grid
  get() = trim().lines().map { it.toCharArray() }

val Grid.string: GridString
  get() = joinToString("\n") { it.joinToString("") }

fun Grid.setSymbolAt(point: Point, symbol: Char = GUARD_LOCATION): Grid {
  this[point.y][point.x] = symbol
  return this
}

fun Grid.getSymbolAt(point: Point): Char = this[point.y][point.x]

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
