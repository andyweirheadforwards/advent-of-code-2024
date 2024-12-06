import kotlin.text.Regex.Companion.escape

fun main() {
    val input: PatrolMap = readInput("Day06").map

    val positionCount = input.patrol().first.guardLocationCount
    println("How many distinct positions will the guard visit before leaving the mapped area? $positionCount")

    val loopCount = input.countLoopObstructionPositions()
    println("How many different positions could you choose for this obstruction?              $loopCount")
}

typealias PatrolMapString = String
typealias PatrolMap = List<String>
typealias MutablePatrolMap = MutableList<String>

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
        val regex = "[${escape(symbols.joinToString(""))}]".toRegex()

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

val PatrolMapString.map: PatrolMap get() = trim().lines()
val PatrolMapString.guardLocationCount: Int get() = count { it == GUARD_LOCATION }

fun PatrolMap.toMutablePatrolMap(): MutablePatrolMap = toMutableList()
fun MutablePatrolMap.toPatrolMap(): PatrolMap = toMutableList()

val PatrolMap.string: PatrolMapString get() = joinToString("\n")
val PatrolMap.guardLocationCount: Int get() = this.string.guardLocationCount

fun PatrolMap.getGuard(): Guard {
    val row = this.indexOfFirst { it.contains(GuardDirection.regex) }
    val col = this[row].indexOfFirst { it in GuardDirection.symbols }

    val symbol = GuardDirection.regex.find(string)?.value.orEmpty().first().toChar()

    return Guard(Position(col, row), GuardDirection.fromSymbol(symbol))
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
    val map = this.toMutablePatrolMap()
    var row = map[position.second].toMutableList()

    row[position.first] = symbol

    map[position.second] = row.joinToString("")

    return map.toPatrolMap()
}

class StuckInLoopException(val map: PatrolMap) : Exception("Stuck in a loop!")

fun PatrolMap.patrol(): Pair<PatrolMap, List<Position>> {
    var guard = getGuard()
    var route: PatrolMap = this
    var history: MutableList<Guard> = mutableListOf()

    while (!isExit(guard.position)) {
        route = route.markPosition(guard.position)
        history.add(guard)

        val nextPosition = guard.nextPosition

        guard = when {
            isExit(nextPosition) -> return Pair(route, history.map { it.position })
            isObstruction(nextPosition) -> guard.turnRight()
            else -> guard.moveForwards()
        }

        if (history.contains(guard)) throw StuckInLoopException(route)
    }

    return Pair(route, history.map { it.position })
}

fun PatrolMap.countLoopObstructionPositions(): Int {
    var loopCount = 0
    val history = patrol().second
    val startPosition = history.first()
    val locations = history.distinct().filter { it != startPosition }

    locations.forEach { location ->
        try {
            val (col, row) = location
            markPosition(Position(col, row), NEW_OBSTRUCTION).patrol()
        } catch (_: StuckInLoopException) {
            loopCount++
        }
    }
    return loopCount
}
