package Day13

import java.awt.Point

fun Point.toLongPoint(): LongPoint = LongPoint(x.toLong(), y.toLong())

operator fun Point.times(other: Int): Point = Point(x * other, y * other)
