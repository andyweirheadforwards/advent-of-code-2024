import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Day07Test {

  @ParameterizedTest(name = "it should create calibration {0}")
  @MethodSource("getCalibrationList")
  fun `It should create Calibration from string`(expected: Calibration) {
    val string = "${expected.target}: ${expected.values.joinToString(" ")}"

    assertEquals(expected, string.toCalibration())
  }

  @Test
  fun `It should create a CalibrationList from string`() {
    val expected = calibrationList
    val string = expected.joinToString("\n") { "${it.target}: ${it.values.joinToString(" ")}" }

    assertEquals(expected, string.toCalibrationList())
  }

  @ParameterizedTest(name = "it should convert {0}")
  @MethodSource("getCalibrationList")
  fun `It should convert Calibration to String`(calibration: Calibration, string: String) {
    assertEquals(string, calibration.toString())
  }

  @ParameterizedTest(name = "{1} is valid {2}")
  @MethodSource("getCalibrationList")
  fun `It should be a valid calibration`(
      calibration: Calibration,
      string: String,
      isValid: Boolean
  ) {
    assertEquals(isValid, calibration.isValidOne)
  }

  @Test
  fun `It should calculate the sum of calibrations plus, times`() {
    val expected = 3749L

    assertEquals(expected, calibrationList.filter { it.isValidOne }.calibrationTotal)
  }

  @Test
  fun `It should calculate the sum of calibrations plus, times, concat`() {
    val expected = 11387L

    assertEquals(expected, calibrationList.filter { it.isValidTwo }.calibrationTotal)
  }

  companion object {
    val calibrationListString =
        """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """
            .trimIndent()

    val calibrationList =
        listOf<Calibration>(
            Calibration(190, listOf(10, 19)),
            Calibration(3267, listOf(81, 40, 27)),
            Calibration(83, listOf(17, 5)),
            Calibration(156, listOf(15, 6)),
            Calibration(7290, listOf(6, 8, 6, 15)),
            Calibration(161011, listOf(16, 10, 13)),
            Calibration(192, listOf(17, 8, 14)),
            Calibration(21037, listOf(9, 7, 18, 13)),
            Calibration(292, listOf(11, 6, 16, 20)),
        )

    val validCalibrations =
        listOf<Calibration>(
            Calibration(190, listOf(10, 19)),
            Calibration(3267, listOf(81, 40, 27)),
            Calibration(292, listOf(11, 6, 16, 20)),
        )

    @JvmStatic
    fun getCalibrationList() =
        calibrationListString
            .lines()
            .mapIndexed { index, string ->
              Arguments.of(
                  calibrationList[index],
                  string,
                  validCalibrations.contains(calibrationList[index]))
            }
            .asIterable()
  }
}
