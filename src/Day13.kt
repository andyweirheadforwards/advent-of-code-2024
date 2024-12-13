import java.awt.Point
import kotlin.time.measureTime

const val BUTTON_COST_A = 3
const val BUTTON_COST_B = 1

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day13")

          val costOfAllPrizesOne = input.costOfAllPrizesOne
          println(
              "What is the fewest tokens you would have to spend to win all possible prizes? $costOfAllPrizesOne")

          val costOfAllPrizesTwo = input.costOfAllPrizesTwo
          println(
              "What is the fewest tokens you would have to spend to win all possible prizes? $costOfAllPrizesTwo")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias RawMachineString = String

typealias RawMachineLines = List<String>

fun RawMachineString.toRawMachineLinesList(): List<RawMachineLines> =
    lines().chunked(4).map { it.take(3) }

val RawMachineString.costOfAllPrizesOne: Int
  get() = toRawMachineLinesList().mapNotNull { it.toClawMachineOne().cost }.sumOf { it }

val RawMachineString.costOfAllPrizesTwo: Int
  get() = toRawMachineLinesList().mapNotNull { it.toClawMachineTwo().cost }.sumOf { it }

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
        it.component1().toInt() to it.component2().toInt()
      } ?: error("Invalid prize format")

  return ClawMachine(
      Button(aString, BUTTON_COST_A),
      Button(bString, BUTTON_COST_B),
      LongPoint("1%013d".format(x).toLong(), "1%013d".format(y).toLong()))
}

fun Point.toLongPoint(): LongPoint = LongPoint(x.toLong(), y.toLong())

data class ClawMachine(val buttonA: Button, val buttonB: Button, val prizeLocation: LongPoint) {
  val cost: Int?
    get() {
      var totalCost: Int? = null

      var aPresses = 0
      var bPresses = 0

      var buttonPresses: Button = Button(0, 0, 0)

      while (buttonPresses <= prizeLocation - buttonA.point) {
        buttonPresses += buttonA
        aPresses++
      }

      if (buttonPresses == prizeLocation) totalCost = buttonPresses.cost

      (aPresses downTo 0).forEach {
        while (buttonPresses <= prizeLocation - buttonB.point) {
          buttonPresses += buttonB
          bPresses++
        }
        if (buttonPresses == prizeLocation) totalCost = buttonPresses.cost
        buttonPresses -= buttonA
        aPresses--
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
}

data class Button(val point: LongPoint, val cost: Int) {
  companion object {
    val BUTTON_REGEX = "Button [AB]: X\\+(\\d+), Y\\+(\\d+)".toRegex()

    operator fun invoke(point: Point, cost: Int): Button = Button(point.toLongPoint(), cost)

    operator fun invoke(x: Long, y: Long, cost: Int): Button = Button(LongPoint(x, y), cost)

    operator fun invoke(string: String, cost: Int): Button {

      val (x, y) = BUTTON_REGEX.find(string)!!.groups.toList().drop(1).map { it!!.value.toLong() }

      return Button(x, y, cost)
    }
  }

  operator fun plus(other: Button): Button = Button(point + other.point, cost + other.cost)

  operator fun minus(other: Button): Button = Button(point - other.point, cost - other.cost)

  operator fun compareTo(other: LongPoint): Int =
      maxOf(point.x.compareTo(other.x), point.y.compareTo(other.y))

  override fun equals(other: Any?): Boolean =
      when (other) {
        is LongPoint -> x == other.x && y == other.y
        else -> super.equals(other)
      }

  val x: Long
    get() = point.x

  val y: Long
    get() = point.y

  override fun toString(): String = "x:$x, y:$y, cost:$cost"
}

data class LongPoint(val x: Long, val y: Long) {
  operator fun plus(other: LongPoint): LongPoint = LongPoint(x + other.x, y + other.y)

  operator fun minus(other: LongPoint): LongPoint = LongPoint(x - other.x, y - other.y)
}
