package Day13

import java.awt.Point

data class LongPoint(val x: Long, val y: Long) {
  operator fun plus(other: LongPoint): LongPoint = LongPoint(x + other.x, y + other.y)

  operator fun plus(other: Point): LongPoint = this.plus(other.toLongPoint())

  operator fun minus(other: LongPoint): LongPoint = LongPoint(x - other.x, y - other.y)

  operator fun minus(other: Point): LongPoint = this.minus(other.toLongPoint())

  operator fun times(other: Long): LongPoint = LongPoint(x * other, y * other)

  operator fun div(other: LongPoint): Long = minOf(x / other.x, y / other.y)

  operator fun div(other: Point): Long = minOf(x / other.x.toLong(), y / other.y.toLong())

  override fun equals(other: Any?): Boolean =
      when (other) {
        is Point -> x == other.x.toLong() && y == other.y.toLong()
        is LongPoint -> x == other.x && y == other.y
        else -> super.equals(other)
      }
}
