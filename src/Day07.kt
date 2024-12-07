import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day07")

          val validCalibrationTotalOne = input.toCalibrationList().validCalibrationTotalOne
          println("What is their total calibration result? $validCalibrationTotalOne")

          val validCalibrationTotalTwo = input.toCalibrationList().validCalibrationTotalTwo
          println("What is their total calibration result? $validCalibrationTotalTwo")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias CalibrationList = List<Calibration>

fun String.toCalibration(): Calibration =
    Calibration(
        takeWhile { it != ':' }.toLong(),
        takeLastWhile { it != ':' }.trim().split(" ").map { it.toInt() })

fun String.toCalibrationList(): CalibrationList = trim().lines().map { it.toCalibration() }

val CalibrationList.calibrationTotal: Long
  get() = sumOf { it.target }

val CalibrationList.validCalibrationTotalOne: Long
  get() = filter { it.isValidOne }.calibrationTotal

val CalibrationList.validCalibrationTotalTwo: Long
  get() = filter { it.isValidTwo }.calibrationTotal

data class Calibration(val target: Long, val values: List<Int>) {
  override fun toString(): String = "$target: ${values.joinToString(" ")}"

  val isValidOne: Boolean
    get() = calculateNextOne()

  val isValidTwo: Boolean
    get() = calculateNextTwo()

  fun calculateNextOne(acc: Long = 0L, index: Int = 0): Boolean =
      calculateNextOp(acc, index, Long::plus, this::calculateNextOne) ||
          calculateNextOp(acc, index, Long::times, this::calculateNextOne)

  fun calculateNextTwo(acc: Long = 0L, index: Int = 0): Boolean =
      calculateNextOp(acc, index, Long::plus) ||
          calculateNextOp(acc, index, Long::times) ||
          calculateNextOp(acc, index, { a, v -> "$a$v".toLong() })

  fun calculateNextOp(
      acc: Long,
      index: Int,
      op: (Long, Int) -> Long,
      calculate: (acc: Long, index: Int) -> Boolean = this::calculateNextTwo
  ): Boolean {
    val nextAcc = op(acc, values[index])
    val nextIndex = index + 1

    return when {
      nextAcc > target -> false
      nextIndex < values.size -> calculate(nextAcc, nextIndex)
      else -> nextAcc == target
    }
  }
}
