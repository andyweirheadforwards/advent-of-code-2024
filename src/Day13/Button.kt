package Day13

import minus
import plus
import java.awt.Point

data class Button(val point: Point, val cost: Int) {
  companion object {
    val BUTTON_REGEX = "Button [AB]: X\\+(\\d+), Y\\+(\\d+)".toRegex()

    operator fun invoke(x: Int, y: Int, cost: Int): Button = Button(Point(x, y), cost)

    operator fun invoke(string: String, cost: Int): Button {

      val (x, y) = BUTTON_REGEX.find(string)!!.groups.toList().drop(1).map { it!!.value.toInt() }

      return Button(x, y, cost)
    }
  }

  operator fun plus(other: Button): Button = Button(point + other.point, cost + other.cost)

  operator fun minus(other: Button): Button = Button(point - other.point, cost - other.cost)

  operator fun times(other: Int): Button = Button(point * other, cost * other)

  operator fun div(other: LongPoint): Long = minOf(x / other.x, y / other.y)

  operator fun compareTo(other: LongPoint): Int =
      maxOf(point.x.compareTo(other.x), point.y.compareTo(other.y))

  override fun equals(other: Any?): Boolean =
      when (other) {
        is Point -> x == other.x && y == other.y
        else -> super.equals(other)
      }

  val x: Int
    get() = point.x

  val y: Int
    get() = point.y

  override fun toString(): String = "x:$x, y:$y, cost:$cost"
}
