import kotlinx.coroutines.*
import java.awt.Point
import kotlin.time.measureTime

const val BUTTON_COST_A = 3
const val BUTTON_COST_B = 1

fun main(): Unit = runBlocking {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day13")

          val costOfAllPrizesOne = input.costOfAllPrizesOne()
          println(
              "What is the fewest tokens you would have to spend to win all possible prizes? $costOfAllPrizesOne")

          val costOfAllPrizesTwo = input.costOfAllPrizesTwo()
          println(
              "What is the fewest tokens you would have to spend to win all possible prizes? $costOfAllPrizesTwo")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias RawMachineString = String

typealias RawMachineLines = List<String>

fun RawMachineString.toRawMachineLinesList(): List<RawMachineLines> =
    lines().windowed(3, 4, partialWindows = false)

fun RawMachineString.costOfAllPrizesOne(): Long =
    toRawMachineLinesList().asSequence().mapNotNull { it.toClawMachineOne().cost }.sum()

suspend fun RawMachineString.costOfAllPrizesTwo(): Long =
    withContext(Dispatchers.Default) {
      toRawMachineLinesList().map { async { it.toClawMachineTwo().cost ?: 0L } }.awaitAll().sum()
    }

val RawMachineLines.PRIZE_REGEX: Regex
  get() = "Prize: X=(\\d+), Y=(\\d+)".toRegex()

fun RawMachineLines.toClawMachineOne(): ClawMachine {
  val (aString, bString, prizeString) = this

  val (x, y) =
      PRIZE_REGEX.find(prizeString)?.destructured?.let {
        it.component1().toLong() to it.component2().toLong()
      } ?: error("Invalid prize format")

  return ClawMachine(
      Button(aString, BUTTON_COST_A), Button(bString, BUTTON_COST_B), LongPoint(x, y))
}

fun RawMachineLines.toClawMachineTwo(): ClawMachine {
  val (aString, bString, prizeString) = this
  val (x, y) =
      PRIZE_REGEX.find(prizeString)?.destructured?.let {
        it.component1().toLong() to it.component2().toLong()
      } ?: error("Invalid prize format")

  return ClawMachine(
      Button(aString, BUTTON_COST_A),
      Button(bString, BUTTON_COST_B),
      LongPoint("1%013d".format(x).toLong(), "1%013d".format(y).toLong()))
}

fun Point.toLongPoint(): LongPoint = LongPoint(x.toLong(), y.toLong())

operator fun Point.times(other: Int): Point = Point(x * other, y * other)

data class ClawMachine(val buttonA: Button, val buttonB: Button, val prizeLocation: LongPoint) {
  val cost: Long?
    get() {

      // Not solvable for y
      if (prizeLocation.y % extendedGcd(buttonA.y.toLong(), buttonB.y.toLong()).first != 0L)
          return null

      val aX = buttonA.x
      val aY = buttonA.y
      val bX = buttonB.x
      val bY = buttonB.y
      val cX = prizeLocation.x
      val cY = prizeLocation.y

      val (gX, x0, y0) = extendedGcd(aX.toLong(), bX.toLong())
      val gY = extendedGcd(aY.toLong(), bY.toLong()).first

      // If gcd(a, b) does not divide c, there's no solution
      if (cX % gX != 0L || cY % gY != 0L) return null

      var totalCost: Long? = null

      // Scale the solution to the original equation
      val x0Scaled = x0 * (cX / gX)
      val y0Scaled = y0 * (cX / gX)

      // Calculate the range for t
      val bXdivGx = bX / gX
      val aXdivGx = aX / gX

      val tMin = (-x0Scaled).floorDiv(bXdivGx)
      val tMax = y0Scaled.floorDiv(aXdivGx)

      // Iterate over the possible t values to find solutions
      for (t in tMin..tMax) {
        val pressesA = x0Scaled + bXdivGx * t
        val pressesB = y0Scaled - aXdivGx * t
        if (pressesA > 0 && pressesB > 0 && cY == aY * pressesA + bY * pressesB) {
          val cost = buttonA.cost * pressesA + buttonB.cost * pressesB
          totalCost = minOf(totalCost ?: cost, cost)
        }
      }

      return totalCost
    }

  override fun toString(): String =
      """
        Button A: X+${buttonA.x}, Y+${buttonA.y}
        Button B: X+${buttonB.x}, Y+${buttonB.y}
        Prize: X=${prizeLocation.x}, Y=${prizeLocation.y}
      """
          .trimIndent()

  private fun gcd(a: Int, b: Int): Int {
    return if (b == 0) a else gcd(b, a % b)
  }

  private fun extendedGcd(a: Long, b: Long): Triple<Long, Long, Long> {
    // Base case
    if (b == 0L) return Triple(a, 1L, 0L) // gcd(a, 0) = a, x = 1, y = 0

    // Recursive case
    val (gcd, x1, y1) = extendedGcd(b, a % b)
    val x = y1
    val y = x1 - (a / b) * y1
    return Triple(gcd, x, y)
  }
}

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
