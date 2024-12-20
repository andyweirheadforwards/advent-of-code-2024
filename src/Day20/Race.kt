package Day20

import Direction
import Grid
import findFirst
import getNeighbours
import getSymbolAt
import grid
import isValidPoint
import move
import string
import java.awt.Point

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
        var curr = start
        val path = mutableListOf(curr)
        while (curr != finish) {
            val prev = if (path.size < 2) Point(-1, -1) else path[path.lastIndex - 1]
            val next = track.getNeighbours(curr).first { it != prev && track.getSymbolAt(it) != RACE_WALL }
            path.add(next)
            curr = next
        }
        path
    }

    private val pathIndexMap by lazy { path.withIndex().associate { it.value to it.index } }

    fun solveOne() = findShortcuts().map { path.size - it.size }

    fun findShortcuts(): List<List<Point>> {
        return path.flatMapIndexed { index, point ->
            potentialShortcuts(point).filter { shortcutPoint ->
                pathIndexMap[shortcutPoint]?.let { it > index } == true
            }
                .flatMap { shortcutPoint ->
                    val middleX = point.x + ((shortcutPoint.x - point.x) / 2)
                    val middleY = point.y + ((shortcutPoint.y - point.y) / 2)
                    val beginning = path.subList(0, index + 1)
                    val middle = listOf(Point(middleX, middleY))
                    val end = path.subList(path.indexOf(shortcutPoint), path.size)
                    listOf(beginning + middle + end)
                }
        }
    }


    override fun toString(): String = track.string

    private fun potentialShortcuts(point: Point): List<Point> = Direction.entries.mapNotNull { direction ->
        val shortcut = Point(point)
        shortcut.move(direction)
        shortcut.move(direction)
        if (track.isValidPoint(shortcut)) shortcut else null
    }
}