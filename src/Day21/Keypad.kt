package Day21

import Coordinate
import Grid
import diff
import dijkstra
import dijkstraAll
import getNeighbours
import getSymbolAt
import grid

const val NUMERIC_KEYPAD =
    """
789
456
123
#0A
"""

const val DIRECTIONAL_KEYPAD =
    """
#^A
<v>
"""

const val KEYPAD_START = 'A'
const val BLANK_KEY = '#'

private val keyPressLookup =
    mapOf<Pair<Char, Char>, String>(
        // A
        Pair('A', 'A') to "A",
        Pair('A', '^') to "<A",
        Pair('A', '<') to "v<<A",
        Pair('A', 'v') to "<vA",
        Pair('A', '>') to "vA",
        // ^
        Pair('^', 'A') to ">A",
        Pair('^', '^') to "A",
        Pair('^', '<') to "v<A",
        Pair('^', 'v') to "vA",
        Pair('^', '>') to "v>A",
        // <
        Pair('<', 'A') to ">>^A",
        Pair('<', '^') to ">^A",
        Pair('<', '<') to "A",
        Pair('<', 'v') to ">A",
        Pair('<', '>') to ">>A",
        // v
        Pair('v', 'A') to "^>A",
        Pair('v', '^') to "^A",
        Pair('v', '<') to "<A",
        Pair('v', 'v') to "A",
        Pair('v', '>') to ">A",
        // >
        Pair('>', 'A') to "^A",
        Pair('>', '^') to "<^A",
        Pair('>', '<') to "<<A",
        Pair('>', 'v') to "<A",
        Pair('>', '>') to "A",
    )

class Keypad(private val grid: Grid) {

    companion object {
        operator fun invoke(input: String): Keypad = Keypad(input.trimIndent().grid)
    }

    private val keyLocations: Map<Char, Coordinate> = buildKeyLocations()

    private val isNumericKeypad = grid.size == 4

    fun findRoute(input: String, depth: Int = 1): String {
        return findRouteRecursive(input, depth)
    }

    private fun findRouteRecursive(route: String, depth: Int): String {
        if (depth == 0) return route

        val newRoute = findRouteForCode(route)
        return findRouteRecursive(newRoute, depth - 1)
    }

    private fun findRouteForCode(code: String): String {
        val iterator = code.iterator()
        var route = ""
        var from = KEYPAD_START
        while (iterator.hasNext()) {
            val to = iterator.next()
            route +=
                if (true || isNumericKeypad) {
                    findDirections(keyLocations[from]!!, keyLocations[to]!!) + KEYPAD_START
                } else {
                    keyPressLookup[Pair(from, to)]!!
                }
            from = to
        }
        return route
    }

    private fun buildKeyLocations(): Map<Char, Coordinate> {
        val locations = mutableMapOf<Char, Coordinate>()
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                val point = Coordinate(x, y)
                val symbol = grid.getSymbolAt(point)
                if (symbol != BLANK_KEY) {
                    locations[symbol] = point
                }
            }
        }
        return locations.toMap()
    }

    private fun findDirections(from: Coordinate, to: Coordinate): String {
        if (from == to) return ""

        val path = dijkstra(from, to) { current ->
            grid.getNeighbours(current).filter { grid.getSymbolAt(it) != BLANK_KEY }
        } ?: error("No route found")

        val directions = path.zipWithNext().map { (current, next) ->
            when {
                next.x > current.x -> '>'
                next.x < current.x -> '<'
                next.y > current.y -> 'v'
                next.y < current.y -> '^'
                else -> error("Invalid move")
            }
        }.joinToString("")

        val leftRightMoves = directions.filter { it == '<' || it == '>' }
        val upDownMoves = directions.filter { it == '^' || it == 'v' }

        return when {
            from.x == 1 && to.x == 0 && grid.getSymbolAt(from, Direction.WEST) == BLANK_KEY -> upDownMoves + leftRightMoves
            from.x < to.x -> leftRightMoves + upDownMoves
            else -> upDownMoves + leftRightMoves
        }
    }
}

private data class Move(val x: Int, val y: Int, val symbol: Char)
