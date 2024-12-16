package Day16

import Direction
import Grid
import GridString
import findFirst
import getSymbolAt
import grid
import isValidPoint
import move
import java.awt.Point
import java.util.*

const val MAZE_MOVE_SCORE = 1
const val MAZE_TURN_SCORE = 1000
const val DEAD_END_SCORE = Int.MAX_VALUE

const val MAZE_START = 'S'
const val MAZE_WALL = '#'
const val MAZE_FINISH = 'E'

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

  fun solve(): Int = dijkstra()

  private fun dijkstra(): Int {
    val openSet = PriorityQueue<MazeNode>(compareBy { it.score })
    val visited = mutableSetOf<MazeNodePair>()

    start.score = 0
    openSet.add(start)

    while (openSet.isNotEmpty()) {
      val current = openSet.poll()

      if (current.point == finish) return current.score

      if (Pair(current.point, current.direction) in visited) continue
      visited.add(Pair(current.point, current.direction)) // Add to visited

      for (neighbor in getNeighbors(current)) {
        if (grid.getSymbolAt(neighbor.point) == MAZE_WALL) continue

        val score = calculateScoreForNeighbor(current, neighbor)
        val tentativeScore = current.score + score

        if (tentativeScore < neighbor.score) {
          neighbor.score = tentativeScore
          neighbor.previous = current
          openSet.add(neighbor)
        }
      }
    }

    return DEAD_END_SCORE // No path found
  }

  private fun getNeighbors(node: MazeNode): Sequence<MazeNode> {
    val point = node.point
    val direction = node.direction

    val forwardPoint = getNextPoint(point, direction)
    val isForwardBlocked =
        !grid.isValidPoint(forwardPoint) || grid.getSymbolAt(forwardPoint) == MAZE_WALL
    val isLeftBlocked = grid.getSymbolAt(point, direction.turnCcw()) == MAZE_WALL
    val isRightBlocked = grid.getSymbolAt(point, direction.turnCw()) == MAZE_WALL

    // If all directions are blocked, return no neighbors (dead-end)
    if (isForwardBlocked && isLeftBlocked && isRightBlocked) {
      node.score = DEAD_END_SCORE
      return emptySequence() // Using sequence to avoid creating an unnecessary list
    }

    return sequence {
      if (!isForwardBlocked) yield(move(node))
      if (!isLeftBlocked) yield(turnCcw(node))
      if (!isRightBlocked) yield(turnCw(node))
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
