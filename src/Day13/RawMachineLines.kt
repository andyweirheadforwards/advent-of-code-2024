package Day13

const val BUTTON_COST_A = 3
const val BUTTON_COST_B = 1

typealias RawMachineLines = List<String>

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
