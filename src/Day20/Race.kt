package Day20

import Grid
import findFirst
import getNeighbours
import getSymbolAt
import grid
import string
import java.awt.Point
import kotlin.math.abs

private const val RACE_START = 'S'
private const val RACE_FINISH = 'E'
private const val RACE_WALL = '#'

class Race(private val track: Grid) {

    companion object {
        operator fun invoke(input: String): Race = Race(input.grid)
    }

    val start: Point = track.findFirst(RACE_START)!!
    val finish: Point = track.findFirst(RACE_FINISH)!!

    val path: List<Point> by lazy {
        val path = mutableListOf(start)
        var curr = start
        while (curr != finish) {
            val prev = if (path.size < 2) Point(-1, -1) else path[path.lastIndex - 1]
            val next = track.getNeighbours(curr).first { it != prev && track.getSymbolAt(it) != RACE_WALL }
            path.add(next)
            curr = next
        }
        path
    }

    fun solveOne() = findShortcuts(2).map { path.size - it }

    fun solveTwo() = findShortcuts(20).map { path.size - it }

    fun findShortcuts(time: Int): List<Int> = path.indices.flatMap { scStartIndex ->
        (scStartIndex + 2 until path.size).mapNotNull { scFinishIndex ->
            val dist = manhattanDistance(path[scStartIndex], path[scFinishIndex])
            if (dist in 2..time) {
                val shortcut = scStartIndex + dist + path.size - scFinishIndex
                if (shortcut < path.size) shortcut else null
            } else null
        }
    }

    override fun toString(): String = track.string

    private fun manhattanDistance(p1: Point, p2: Point): Int {
        return abs(p1.x - p2.x) + abs(p2.y - p1.y)
    }
}