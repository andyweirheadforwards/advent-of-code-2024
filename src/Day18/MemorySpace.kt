package Day18

import java.awt.Point

data class MemorySpace(
    val byte: Point,
    var score: Int = Int.MAX_VALUE,
    var previous: MemorySpace? = null,
    var path: MemoryPath = listOf()
) {
  val x = byte.x
  val y = byte.y
}
