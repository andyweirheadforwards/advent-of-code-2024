import GuardDirection.entries
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.awt.Point
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input: PatrolMap = readInput("Day06").map

          val locationCount = input.patrol().first.guardLocationCount
          println(
              "How many distinct locations will the guard visit before leaving the mapped area? $locationCount")

          val loopCount = input.countLoopObstructionLocations()
          println(
              "How many different locations could you choose for this obstruction?              $loopCount")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias PatrolMapString = GridString

typealias PatrolMap = Grid

const val GUARD_LOCATION = 'X'
const val OBSTRUCTION = '#'
const val NEW_OBSTRUCTION = 'O'

enum class GuardDirection(var symbol: Char) {
  NORTH('^'),
  EAST('>'),
  SOUTH('v'),
  WEST('<');

  companion object {

    val symbols = entries.map { it.symbol }

    fun fromSymbol(symbol: Char): GuardDirection =
        GuardDirection.entries.first { it.symbol == symbol }
  }
}

data class Guard(val point: Point, val direction: GuardDirection) {
  val nextPoint: Point
    get() =
        when (direction) {
          GuardDirection.NORTH -> Point(point.x, point.y - 1)
          GuardDirection.SOUTH -> Point(point.x, point.y + 1)
          GuardDirection.WEST -> Point(point.x - 1, point.y)
          GuardDirection.EAST -> Point(point.x + 1, point.y)
        }

  fun turnRight(): Guard = copy(direction = direction.turnRight)

  fun moveForwards(): Guard = copy(point = nextPoint)
}

val GuardDirection.turnRight: GuardDirection
  get() =
      when {
        ordinal == GuardDirection.entries.size - 1 -> GuardDirection.entries[0]
        else -> GuardDirection.entries[ordinal + 1]
      }

val PatrolMapString.map: PatrolMap
  get() = trim().lines().map { it.toCharArray() }.toMutableList()

val PatrolMapString.guardLocationCount: Int
  get() = count { it == GUARD_LOCATION }

val PatrolMap.guardLocationCount: Int
  get() = this.string.guardLocationCount

fun PatrolMap.copy(): PatrolMap = map { it.copyOf() }.toMutableList()

fun PatrolMap.getGuard(): Guard {
  indices.forEach { row ->
    this[row].forEachIndexed { col, symbol ->
      if (symbol in GuardDirection.symbols)
          return Guard(Point(col, row), GuardDirection.fromSymbol(symbol))
    }
  }
  throw IllegalStateException("Guard not found in the PatrolMap!")
}

fun PatrolMap.isObstruction(point: Point) =
    getSymbolAt(point) in listOf(OBSTRUCTION, NEW_OBSTRUCTION)

fun PatrolMap.isExit(point: Point) =
    when {
      point.x < 0 || point.y < 0 -> true
      point.x > first().lastIndex -> true // col
      point.y > lastIndex -> true // row
      else -> false
    }

class StuckInLoopException(val map: PatrolMap, val guard: Guard) :
    Exception("Stuck in a loop at location ${guard.point} facing ${guard.direction}")

fun PatrolMap.patrol(): Pair<PatrolMap, List<Point>> {
  var route: PatrolMap = copy()
  var guard = getGuard()

  var path = mutableListOf<Point>()
  var history: MutableSet<Guard> = mutableSetOf()

  while (!isExit(guard.point)) {
    if (!history.add(guard)) throw StuckInLoopException(route, guard)

    route.setSymbolAt(guard.point)
    path.add(guard.point)

    guard =
        when {
          isExit(guard.nextPoint) -> return Pair(route, path)
          isObstruction(guard.nextPoint) -> guard.turnRight()
          else -> guard.moveForwards()
        }
  }

  return Pair(route, path)
}

fun PatrolMap.countLoopObstructionLocations(): Int = runBlocking {
  val (_, history) = patrol()
  val startPoint = history.first()
  val locations = history.toSet() - startPoint

  val result =
      locations.map { location ->
        async {
          val map = copy()
          try {
            map.setSymbolAt(Point(location.x, location.y), NEW_OBSTRUCTION).patrol()
            false
          } catch (`_`: StuckInLoopException) {
            true
          }
        }
      }

  result.awaitAll().filter { it == true }.size
}
