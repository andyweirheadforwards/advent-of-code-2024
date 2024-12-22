package Day21

import Coordinate
import Direction
import Grid
import com.github.benmanes.caffeine.cache.Caffeine
import dijkstraAll
import getNeighbours
import getSymbolAt
import grid
import kotlinx.coroutines.*
import move
import java.util.*
import java.util.Queue
import java.util.concurrent.TimeUnit

typealias KeyLocations = Map<Char, Coordinate>

class KeypadTwo {

    private val numKeypad: Grid = NUMERIC_KEYPAD.trimIndent().grid
    private val dirKeypad: Grid = DIRECTIONAL_KEYPAD.trimIndent().grid

    private val numKeyLocations: KeyLocations = buildKeyLocations(numKeypad)
    private val dirKeyLocations: KeyLocations = buildKeyLocations(dirKeypad)

    private val pathCache = Caffeine.newBuilder().maximumSize(1000).expireAfterAccess(10, TimeUnit.MINUTES)
        .build<Pair<Char, Char>, List<List<Coordinate>>>()

    private val routeCache = Caffeine.newBuilder().maximumSize(1000).expireAfterAccess(10, TimeUnit.MINUTES)
        .build<Pair<Char, Char>, List<String>>()

    private val combinationCache = Caffeine.newBuilder().maximumSize(1000).expireAfterAccess(10, TimeUnit.MINUTES)
        .build<List<List<String>>, Sequence<String>>()

    fun findNumRoutes(input: String) = runBlocking { findAllShortestRoutes(input, numKeypad, numKeyLocations) }
    fun findDirRoutes(input: String, depth: Int = 1) =
        runBlocking { findAllShortestRoutes(input, dirKeypad, dirKeyLocations, depth) }

    fun findNumKeys(input: String): String = findKeys(input, numKeypad, numKeyLocations)
    fun findDirKeys(input: String): String = findKeys(input, dirKeypad, dirKeyLocations)

    private fun findKeys(input: String, keypad: Grid, keyLocations: KeyLocations): String {
        var keys = ""
        var position = keyLocations[KEYPAD_START]!!
        val iterator = input.iterator()
        while (iterator.hasNext()) {
            val move = iterator.next()
            when (move) {
                KEYPAD_START -> keys += keypad.getSymbolAt(position)
                '^' -> position = position.move(Direction.NORTH)
                '>' -> position = position.move(Direction.EAST)
                'v' -> position = position.move(Direction.SOUTH)
                '<' -> position = position.move(Direction.WEST)
                else -> error("Invalid key $move")
            }
        }
        return keys
    }

    private fun buildKeyLocations(grid: Grid): Map<Char, Coordinate> {
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

    private suspend fun findAllShortestRoutes(
        input: String,
        grid: Grid,
        keyLocations: Map<Char, Coordinate>,
        depth: Int = 1,
    ): Sequence<String> = coroutineScope {
        if (input.isEmpty()) return@coroutineScope emptySequence()
        if (depth > 25) error("Depth too large, may cause OutOfMemoryError")

        val routes = mutableListOf<List<String>>()
        var from = KEYPAD_START

        input.forEach { char ->
            val to = char
            if (to == from) {
                routes.add(listOf("A"))
            } else {
                val paths = pathCache.get(from to to) {
                    dijkstraAll(keyLocations[from]!!, keyLocations[to]!!) { current ->
                        grid.getNeighbours(current).filter { grid.getSymbolAt(it) != BLANK_KEY }
                    }
                }

                if (paths.isEmpty()) error("No route found")

                val directionsList = routeCache.get(from to to) {
                    paths.map { path ->
                        path.zipWithNext().map { (current, next) ->
                            when {
                                next.x > current.x -> '>'
                                next.x < current.x -> '<'
                                next.y > current.y -> 'v'
                                next.y < current.y -> '^'
                                else -> 'A'
                            }
                        }.joinToString("") + KEYPAD_START
                    }
                }

                routes.add(directionsList)
            }
            from = to
        }

        val queue: Queue<Pair<Sequence<String>, Int>> = LinkedList()
        queue.add(getAllCombinations(routes) to 1)

        while (queue.isNotEmpty()) {
            val (currentResult, currentDepth) = queue.poll()
            if (currentDepth < depth) {
                val deferredResults: List<Deferred<Sequence<String>>> = currentResult.toList().map { route ->
                    async { findAllShortestRoutes(route, grid, keyLocations, 1) }
                }
                queue.addAll(deferredResults.awaitAll().map { it to (currentDepth + 1) })
            } else {
                return@coroutineScope currentResult
            }
        }

        emptySequence()
    }

    private fun getAllCombinations(routes: List<List<String>>): Sequence<String> {
        return combinationCache.get(routes) {
            sequence {
                val indices = IntArray(routes.size)
                while (true) {
                    yield(routes.indices.joinToString("") { routes[it][indices[it]] })
                    var i = routes.size - 1
                    while (i >= 0 && ++indices[i] == routes[i].size) {
                        indices[i] = 0
                        i--
                    }
                    if (i < 0) break
                }
            }
        }
    }
}