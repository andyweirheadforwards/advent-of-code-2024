package Day16

import Direction
import java.awt.Point

typealias MazeNodePair = Pair<Point, Direction>

data class MazeNode(
    val point: Point,
    val direction: Direction,
    var score: Int = Int.MAX_VALUE,
    var previous: MazeNode? = null
) : Comparable<MazeNode> {

  override fun compareTo(other: MazeNode): Int = score.compareTo(other.score)

  fun toPair(): MazeNodePair = Pair(Point(point), direction)
}
