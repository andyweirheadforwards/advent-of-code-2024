package Day16

import Direction
import Grid
import GridString
import findFirst
import getSymbolAt
import grid
import isValidPoint
import move
import string
import java.awt.Point
import java.util.*

const val MAZE_MOVE_SCORE = 1
const val MAZE_TURN_SCORE = 1000
const val DEAD_END_SCORE = Int.MAX_VALUE

const val MAZE_START = 'S'
const val MAZE_WALL = '#'
const val MAZE_FINISH = 'E'

typealias MazePath = List<Point>

typealias MazeSolution = Pair<Int, List<MazePath>>

class Maze(private val grid: Grid) {
  companion object {
    operator fun invoke(gridString: GridString): Maze = Maze(gridString.grid)
  }

  val start: MazeNode =
      MazeNode(
          grid.findFirst(MAZE_START) ?: throw IllegalArgumentException("Start not found"),
          Direction.EAST)
  val finish: Point =
      grid.findFirst(MAZE_FINISH) ?: throw IllegalArgumentException("Finish not found")

  fun solve(): MazeSolution = dijkstra()

  override fun toString(): String {
    val (_, paths) = solve()
    val pathPoints = paths.flatten().toSet()
    val gridCopy = grid.map { it.copyOf() }

    for (point in pathPoints) {
      gridCopy[point.y][point.x] = 'O'
    }

    return gridCopy.string
  }

  private fun dijkstra(): MazeSolution {
    val openSet = PriorityQueue<MazeNode>(compareBy { it.score })
    val visited = mutableMapOf<MazeNodePair, Int>()
    val paths = mutableListOf<MazePath>()

    start.score = 0
    start.path = listOf(start.point)
    openSet.add(start)

    var lowestScore = DEAD_END_SCORE

    while (openSet.isNotEmpty()) {
      val current = openSet.poll()

      if (current.point == finish) {
        if (current.score < lowestScore) {
          lowestScore = current.score
          paths.clear()
        }
        if (current.score == lowestScore) {
          paths.add(current.path)
        }
        continue
      }

      val currentPair = current.toPair()
      if (currentPair in visited && visited[currentPair]!! < current.score) continue
      visited[currentPair] = current.score

      for (neighbor in getNeighbors(current)) {
        if (grid.getSymbolAt(neighbor.point) == MAZE_WALL) continue

        val score = calculateScoreForNeighbor(current, neighbor)
        val tentativeScore = current.score + score

        if (tentativeScore <= neighbor.score) {
          neighbor.score = tentativeScore
          neighbor.previous = current
          if (neighbor.point != current.point) {
            neighbor.path = current.path + neighbor.point
          } else {
            neighbor.path = current.path
          }
          openSet.add(neighbor)
        }
      }
    }

    return Pair(lowestScore, paths)
  }

  private fun getNeighbors(node: MazeNode): Sequence<MazeNode> {
    val point = node.point
    val direction = node.direction

    val forwardPoint = getNextPoint(point, direction)
    val isForwardBlocked =
        !grid.isValidPoint(forwardPoint) || grid.getSymbolAt(forwardPoint) == MAZE_WALL

    return sequence {
      if (!isForwardBlocked) yield(move(node))
      yield(turnCcw(node))
      yield(turnCw(node))
    }
  }

  private fun calculateScoreForNeighbor(current: MazeNode, neighbor: MazeNode): Int =
      when {
        grid.getSymbolAt(neighbor.point) == MAZE_WALL -> DEAD_END_SCORE
        neighbor.direction == current.direction -> MAZE_MOVE_SCORE
        else -> MAZE_TURN_SCORE
      }

  private fun getNextPoint(point: Point, direction: Direction): Point =
      Point(point).apply { move(direction) }

  private fun move(node: MazeNode): MazeNode =
      MazeNode(getNextPoint(node.point, node.direction), node.direction)

  private fun turnCcw(node: MazeNode): MazeNode = MazeNode(node.point, node.direction.turnCcw())

  private fun turnCw(node: MazeNode): MazeNode = MazeNode(node.point, node.direction.turnCw())
}
