fun main() {
    val input: PatrolMap = readInput("Day06").map

    val positionCount = input.patrol().first.guardLocationCount
    println("How many distinct positions will the guard visit before leaving the mapped area? $positionCount")

    val loopCount = input.countLoopObstructionPositions()
    println("How many different positions could you choose for this obstruction?              $loopCount")
}

typealias PatrolMapString = String
typealias PatrolMap = MutableList<CharArray>

typealias Row = Int
typealias Col = Int
typealias Position = Pair<Col, Row> // 0,0 is top left

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

        fun fromSymbol(symbol: Char): GuardDirection = GuardDirection.entries.first { it.symbol == symbol }
    }
}

data class Guard(val position: Position, val direction: GuardDirection) {
    val nextPosition: Position
        get() = when (direction) {
            GuardDirection.NORTH -> Position(position.first, position.second - 1)
            GuardDirection.SOUTH -> Position(position.first, position.second + 1)
            GuardDirection.WEST -> Position(position.first - 1, position.second)
            GuardDirection.EAST -> Position(position.first + 1, position.second)
        }

    fun turnRight(): Guard = copy(direction = direction.turnRight)
    fun moveForwards(): Guard = copy(position = nextPosition)
}

val GuardDirection.turnRight: GuardDirection
    get() = when {
        ordinal == GuardDirection.entries.size - 1 -> GuardDirection.entries[0]
        else -> GuardDirection.entries[ordinal + 1]
    }

val PatrolMapString.map: PatrolMap get() = trim().lines().map { it.toCharArray() }.toMutableList()
val PatrolMapString.guardLocationCount: Int get() = count { it == GUARD_LOCATION }

val PatrolMap.string: PatrolMapString get() = joinToString("\n") { it.joinToString("") }
val PatrolMap.guardLocationCount: Int get() = this.string.guardLocationCount

fun PatrolMap.copy(): PatrolMap = map { it.copyOf() }.toMutableList()

fun PatrolMap.getGuard(): Guard {
    indices.forEach { row ->
        this[row].forEachIndexed { col, symbol ->
            if (symbol in GuardDirection.symbols) return Guard(Position(col, row), GuardDirection.fromSymbol(symbol))
        }
    }
    throw IllegalStateException("Guard not found in the PatrolMap!")
}

fun PatrolMap.symbolAt(position: Position) = this[position.second][position.first]

fun PatrolMap.isObstruction(position: Position) = symbolAt(position) in listOf(OBSTRUCTION, NEW_OBSTRUCTION)

fun PatrolMap.isExit(position: Position) = when {
    position.first < 0 || position.second < 0 -> true
    position.first > first().lastIndex -> true // col
    position.second > lastIndex -> true        // row
    else -> false
}

fun PatrolMap.markPosition(position: Position, symbol: Char = GUARD_LOCATION): PatrolMap {
    this[position.second][position.first] = symbol

    return this
}

class StuckInLoopException(val map: PatrolMap, val guard: Guard) :
    Exception("Stuck in a loop at position ${guard.position} facing ${guard.direction}")

fun PatrolMap.patrol(): Pair<PatrolMap, List<Position>> {
    var route: PatrolMap = copy()
    var guard = getGuard()

    var path = mutableListOf<Position>()
    var history: MutableSet<Guard> = mutableSetOf()

    while (!isExit(guard.position)) {
        if (!history.add(guard)) throw StuckInLoopException(route, guard)

        route.markPosition(guard.position)
        path.add(guard.position)

        guard = when {
            isExit(guard.nextPosition) -> return Pair(route, path)
            isObstruction(guard.nextPosition) -> guard.turnRight()
            else -> guard.moveForwards()
        }
    }

    return Pair(route, path)
}

fun PatrolMap.countLoopObstructionPositions(): Int {
    var loopCount = 0
    val (_, history) = patrol()
    val startPosition = history.first()
    val locations = history.toSet().filter { it !== startPosition }

    locations.forEach { location ->
        val map = copy()
        val (col, row) = location
        try {
            map.markPosition(Position(col, row), NEW_OBSTRUCTION).patrol()
        } catch (_: StuckInLoopException) {
            loopCount++
        }
    }
    return loopCount
}
