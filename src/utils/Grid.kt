package utils

import GUARD_LOCATION
import lastIndexX
import lastIndexY
import java.awt.Point

val GridString.grid: Grid
    get() = trim().lines().map { it.toCharArray() }

val Grid.string: GridString
    get() = joinToString("\n") { it.joinToString("") }

fun Grid.getSymbolAt(
    point: Point,
    direction: Direction? = null,
): Char =
    when (direction) {
        null -> this[point.y][point.x]
        Direction.NORTH -> getSymbolAt(Point(point.x, point.y - 1))
        Direction.SOUTH -> getSymbolAt(Point(point.x, point.y + 1))
        Direction.EAST -> getSymbolAt(Point(point.x + 1, point.y))
        Direction.WEST -> getSymbolAt(Point(point.x - 1, point.y))
    }

fun Grid.getSymbolAt(point: Coordinate): Char = getSymbolAt(Point(point.x, point.y))

fun Grid.getSymbolAt(
    point: Coordinate,
    direction: Direction,
): Char = getSymbolAt(Point(point.x, point.y), direction)

fun Grid.setSymbolAt(
    point: Point,
    symbol: Char = GUARD_LOCATION,
): Grid {
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
    ).filter { it.x >= 0 && it.x <= lastIndexX && it.y >= 0 && it.y <= lastIndexY }

fun Grid.findFirst(symbol: Char): Point? =
    withIndex().firstNotNullOfOrNull { (y, row) ->
        row.indexOfFirst { it == symbol }.takeIf { it != -1 }?.let { x -> Point(x, y) }
    }

fun Grid.findAll(symbol: Char): List<Point> =
    flatMapIndexed { y, row ->
        row.toList().mapIndexedNotNull { x, cell -> if (cell == symbol) Point(x, y) else null }
    }

fun Grid.isValidPoint(point: Point): Boolean =
    when {
        point.x < 0 || point.x > this.first().lastIndex -> false
        point.y < 0 || point.y > this.lastIndex -> false
        else -> true
    }
