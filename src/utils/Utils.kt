package utils

import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readText

const val PROFILE_REPEAT = 1

typealias GridString = String

typealias Grid = List<CharArray>

fun readInput(name: String) =
    Path("data/$name.txt")
        .readText()
        .trim()
        .lines()
        .joinToString("\n")

val CharArray.string: String
    get() = joinToString("")

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
    WEST,
    ;

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
    NORTHWEST,
}

fun <T> dijkstra(
    start: T,
    end: T,
    findNeighbours: (T) -> List<T>,
): List<T>? = Dijkstra(start, end, false, findNeighbours).findPaths().firstOrNull()

fun <T> dijkstraAll(
    start: T,
    end: T,
    findNeighbours: (T) -> List<T>,
): List<List<T>> = Dijkstra(start, end, true, findNeighbours).findPaths()

fun Point.toCoordinate(): Coordinate = Coordinate(x, y)

data class Coordinate(
    val x: Int,
    val y: Int,
) {
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
