import java.awt.Point
import java.util.*
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
    }
fun Grid.getSymbolAt(point: Coordinate): Char = getSymbolAt(Point(point.x, point.y))
fun Grid.getSymbolAt(point: Coordinate, direction: Direction): Char = getSymbolAt(Point(point.x, point.y), direction)

fun Grid.setSymbolAt(point: Point, symbol: Char = GUARD_LOCATION): Grid {
  this[point.y][point.x] = symbol
  return this
}

fun Grid.getNeighbours(point: Coordinate): List<Coordinate> = getNeighbours(point.toPoint()).map { it.toCoordinate() }
fun Grid.getNeighbours(point: Point): List<Point> =
    listOf(
            Point(point.x, point.y - 1),
            Point(point.x - 1, point.y),
            Point(point.x + 1, point.y),
            Point(point.x, point.y + 1),
        )
        .filter { it.x >= 0 && it.x <= lastIndexX && it.y >= 0 && it.y <= lastIndexY }

fun Grid.findFirst(symbol: Char): Point? =
    withIndex().firstNotNullOfOrNull { (y, row) ->
      row.indexOfFirst { it == symbol }.takeIf { it != -1 }?.let { x -> Point(x, y) }
    }

fun Grid.findAll(symbol: Char): List<Point> = flatMapIndexed { y, row ->
  row.toList().mapIndexedNotNull { x, cell -> if (cell == symbol) Point(x, y) else null }
}

fun Grid.isValidPoint(point: Point): Boolean =
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
    }

val Point.coordinates: String
  get() = "$x,$y"

enum class Direction {
  NORTH,
  EAST,
  SOUTH,
  WEST;

  fun turnCw(): Direction =
      when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
      }

  fun turnCcw(): Direction =
      when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
      }
}

enum class OrdinalDirection {
  NORTH,
  NORTHEAST,
  EAST,
  SOUTHEAST,
  SOUTH,
  SOUTHWEST,
  WEST,
  NORTHWEST
}

//fun <T> dijkstra(
//    start: T,
//    end: T,
//    findNeighbours: (T) -> List<T>
//): List<T>? {
//    val distances = mutableMapOf<T, Int>().withDefault { Int.MAX_VALUE }
//    val previousNodes = mutableMapOf<T, T?>()
//    val priorityQueue = PriorityQueue(compareBy<Pair<T, Int>> { it.second })
//
//    distances[start] = 0
//    priorityQueue.add(start to 0)
//
//    while (priorityQueue.isNotEmpty()) {
//        val (current, currentDistance) = priorityQueue.poll()
//
//        if (current == end) {
//            val path = mutableListOf<T>()
//            var node: T? = end
//            while (node != null) {
//                path.add(0, node)
//                node = previousNodes[node]
//            }
//            return path
//        }
//
//        for (neighbor in findNeighbours(current)) {
//            val newDistance = currentDistance + 1
//            if (newDistance < distances.getValue(neighbor)) {
//                distances[neighbor] = newDistance
//                previousNodes[neighbor] = current
//                priorityQueue.add(neighbor to newDistance)
//            }
//        }
//    }
//    return null
//}

fun <T> dijkstra(
    start: T,
    end: T,
    findNeighbours: (T) -> List<T>,
): List<T>? = dijkstra(start, end, false, findNeighbours).firstOrNull()

fun <T> dijkstraAll(
    start: T,
    end: T,
    findNeighbours: (T) -> List<T>,
): List<List<T>> = dijkstra(start, end, true, findNeighbours)

fun <T> dijkstra(
    start: T,
    end: T,
    returnAll: Boolean,
    findNeighbours: (T) -> List<T>,
): List<List<T>> {
    val distances = mutableMapOf<T, Int>().withDefault { Int.MAX_VALUE }
    val previousNodes = mutableMapOf<T, MutableList<T>>()
    val priorityQueue = PriorityQueue(compareBy<Pair<T, Int>> { it.second })

    distances[start] = 0
    priorityQueue.add(start to 0)

    while (priorityQueue.isNotEmpty()) {
        val (current, currentDistance) = priorityQueue.poll()

        if (current == end) {
            if (!returnAll) {
                val path = mutableListOf<T>()
                var node: T? = end
                while (node != null) {
                    path.add(0, node)
                    node = previousNodes[node]?.firstOrNull()
                }
                return listOf(path)
            } else {
                val paths = mutableListOf<List<T>>()
                val stack = mutableListOf(listOf(end))
                while (stack.isNotEmpty()) {
                    val path = stack.removeAt(stack.lastIndex)
                    val lastNode = path.first()
                    if (lastNode == start) {
                        paths.add(path)
                    } else {
                        previousNodes[lastNode]?.forEach { prev ->
                            stack.add(listOf(prev) + path)
                        }
                    }
                }
                return paths
            }
        }

        for (neighbor in findNeighbours(current)) {
            val newDistance = currentDistance + 1
            if (newDistance < distances.getValue(neighbor)) {
                distances[neighbor] = newDistance
                previousNodes[neighbor] = mutableListOf(current)
                priorityQueue.add(neighbor to newDistance)
            } else if (newDistance == distances.getValue(neighbor)) {
                previousNodes[neighbor]?.add(current)
            }
        }
    }
    return emptyList()
}

fun Point.toCoordinate(): Coordinate = Coordinate(x, y)

data class Coordinate(val x: Int, val y: Int) {
    companion object {
        operator fun invoke(point: Coordinate): Coordinate = Coordinate(point.x, point.y)
        operator fun invoke(point: Point): Coordinate = Coordinate(point.x, point.y)
    }

    fun toPoint() = Point(x, y)
}

fun Coordinate.diff(other: Coordinate): Coordinate = this.toPoint().diff(other.toPoint()).toCoordinate()
fun Coordinate.move(direction: Direction): Coordinate {
    val newPoint = toPoint()
    newPoint.move(direction)
    return newPoint.toCoordinate()
}