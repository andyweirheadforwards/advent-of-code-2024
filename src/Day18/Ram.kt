package Day18

import Day16.DEAD_END_SCORE
import Direction
import Grid
import move
import string
import java.awt.Point
import java.util.*

typealias Byte = Point

typealias CorruptedBytes = Set<Byte>

typealias MemoryPath = List<Byte>

typealias MemorySolution = Pair<Int, List<MemoryPath>>

class Ram(private val corruptedBytes: CorruptedBytes, private val size: Int) {

  private val start = MemorySpace(Point(0, 0))
  private val finish = Point(size, size)

  private fun grid(time: Int): Grid {
    val timeBytes = corruptedBytes.take(time)
    return (0..size).map { y ->
      (0..size).map { x -> if (timeBytes.contains(Point(x, y))) '#' else '.' }.toCharArray()
    }
  }

  companion object {
    operator fun invoke(input: String, size: Int = 70): Ram {
      val bytes =
          input.lines().map { it.split(",") }.map { (x, y) -> Byte(x.toInt(), y.toInt()) }.toSet()
      return Ram(bytes, size)
    }
  }

  fun solve(time: Int = corruptedBytes.size): MemorySolution = dijkstra(time)

  fun solveLastByte(): Byte {
    var low = 0
    var high = corruptedBytes.size
    var result: Byte? = null

    while (low <= high) {
      val mid = (low + high) / 2
      val (steps) = solve(mid)
      if (steps == Int.MAX_VALUE) {
        result = corruptedBytes.elementAt(mid - 1)
        high = mid - 1
      } else {
        low = mid + 1
      }
    }

    return result ?: error("Failed to solve last byte")
  }

  fun toString(time: Int = corruptedBytes.size): String = grid(time).string

  private fun dijkstra(time: Int): MemorySolution {
    val openSet = PriorityQueue<MemorySpace>(compareBy { it.score })
    val visited = HashSet<Byte>()
    val paths = mutableListOf<MemoryPath>()

    val timeBytes = corruptedBytes.take(time).toSet()

    start.score = 0
    openSet.add(start)

    var lowestScore = DEAD_END_SCORE

    while (openSet.isNotEmpty()) {
      val current = openSet.poll()

      if (current.byte == finish) {
        if (current.score < lowestScore) {
          lowestScore = current.score
          paths.clear()
        }
        if (current.score == lowestScore) {
          paths.add(current.path)
        }
        continue
      }

      if (current.byte in visited) continue
      visited.add(current.byte)

      for (neighbor in getNeighbors(current, timeBytes)) {
        if (timeBytes.contains(neighbor.byte)) continue

        val score = calculateScoreForNeighbor(neighbor, timeBytes)
        val tentativeScore = current.score + score

        if (tentativeScore <= neighbor.score) {
          neighbor.score = tentativeScore
          neighbor.previous = current
          openSet.add(neighbor)
        }
      }
    }

    return Pair(lowestScore, paths)
  }

  private fun getNeighbors(
      memorySpace: MemorySpace,
      bytes: CorruptedBytes = corruptedBytes
  ): Sequence<MemorySpace> {
    val byte = memorySpace.byte
    return Direction.entries
        .asSequence()
        .map { Point(byte).apply { move(it) } }
        .filter { it.x in 0..size && it.y in 0..size }
        .filterNot { it == memorySpace.previous?.byte }
        .filterNot { bytes.contains(it) }
        .map(::MemorySpace)
  }

  private fun calculateScoreForNeighbor(
      neighbor: MemorySpace,
      bytes: CorruptedBytes = corruptedBytes
  ): Int =
      when {
        bytes.contains(neighbor.byte) -> DEAD_END_SCORE
        else -> 1
      }

  private fun reconstructPath(memorySpace: MemorySpace): MemoryPath {
    val path = mutableListOf<Byte>()
    var current: MemorySpace? = memorySpace
    while (current != null) {
      path.add(current.byte)
      current = current.previous
    }
    return path.reversed()
  }
}
