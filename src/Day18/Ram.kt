package Day18

import Direction
import Grid
import dijkstra
import move
import string
import java.awt.Point

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

  fun solve(time: Int = corruptedBytes.size): MemorySolution {
    val timeBytes = corruptedBytes.take(time).toSet()
    val path = dijkstra(start.byte, finish) { getNeighbors(it, timeBytes) }
    val score = path?.lastIndex ?: Int.MAX_VALUE
    return Pair(score, listOf(path ?: emptyList()))
  }

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

  private fun getNeighbors(
    byte: Byte,
    bytes: CorruptedBytes = corruptedBytes,
    previous: Byte? = null
  ): List<Byte> {
    return Direction.entries
      .map { Point(byte).apply { move(it) } }
      .filter { it.x in 0..size && it.y in 0..size }
      .filterNot { it == previous }
      .filterNot { bytes.contains(it) }
  }
}
