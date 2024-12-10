import java.awt.Point
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day10")

          val totalTrailScore = input.grid.totalTrailScore
          println(
              "What is the sum of the scores of all trailheads on your topographic map? $totalTrailScore")

          val totalTrailRating = input.grid.totalTrailRating
          println(
              "What is the sum of the ratings of all trailheads?                        $totalTrailRating")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias TrailMap = Grid

val Grid.lastIndexX: Int
  get() = first().lastIndex
val Grid.lastIndexY: Int
  get() = lastIndex

data class TrailPosition(val point: Point, val height: Int)

fun TrailMap.findTrailHeads(): List<Point> = flatMapIndexed { y, row ->
  row.mapIndexed { x, height -> if (height == '0') Point(x, y) else null }.filterNotNull()
}

fun TrailMap.getNeighbours(position: TrailPosition): List<Point> =
    listOf(
            Point(position.point.x, position.point.y - 1),
            Point(position.point.x - 1, position.point.y),
            Point(position.point.x + 1, position.point.y),
            Point(position.point.x, position.point.y + 1),
        )
        .filter { it.x >= 0 && it.x <= lastIndexX && it.y >= 0 && it.y <= lastIndexY }

fun TrailMap.getNextTrailPoints(position: TrailPosition): List<Point> =
    getNeighbours(position).filter {
      val nextHeight = "${position.height + 1}".toCharArray().first()
      getSymbolAt(it) == nextHeight
    }

fun TrailMap.calculateTrailScore(trailHead: Point): Int {

  val trails = walkTrail(TrailPosition(trailHead, 0)).toSet()

  return trails.count { getSymbolAt(it) == '9' }
}

fun TrailMap.calculateTrailRating(trailHead: Point): Int {
  val trails = walkTrail(TrailPosition(trailHead, 0))

  return trails.count { getSymbolAt(it) == '9' }
}

fun TrailMap.walkTrail(position: TrailPosition): List<Point> {
  val nextTrailPoints = getNextTrailPoints(position)

  return if (position.height < 9)
      nextTrailPoints.flatMap { walkTrail(TrailPosition(it, position.height + 1)) }
  else listOf(position.point)
}

val TrailMap.totalTrailScore: Int
  get() = findTrailHeads().sumOf(::calculateTrailScore)

val TrailMap.totalTrailRating: Int
  get() = findTrailHeads().sumOf(::calculateTrailRating)
