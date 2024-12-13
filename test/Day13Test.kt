import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import kotlin.test.Test
import kotlin.test.assertEquals

class Day13Test {

  @Test
  fun `It should extract input blocks`() {
    val expected =
        listOf(
            """
                Button A: X+94, Y+34
                Button B: X+22, Y+67
                Prize: X=8400, Y=5400
            """
                .trimIndent()
                .lines(),
            """
                Button A: X+26, Y+66
                Button B: X+67, Y+21
                Prize: X=12748, Y=12176
            """
                .trimIndent()
                .lines(),
            """
                Button A: X+17, Y+86
                Button B: X+84, Y+37
                Prize: X=7870, Y=6450
            """
                .trimIndent()
                .lines(),
            """
                Button A: X+69, Y+23
                Button B: X+27, Y+71
                Prize: X=18641, Y=10279
            """
                .trimIndent()
                .lines(),
        )

    val result = testInput.toRawMachineLinesList()

    assertEquals(expected, result)
  }

    @Test
    fun `It should extract machine data one`() {
        val expected =
            ClawMachine(Button(94, 34, BUTTON_COST_A), Button(22, 67, BUTTON_COST_B), LongPoint(8400, 5400))
        val input =
            """
                Button A: X+94, Y+34
                Button B: X+22, Y+67
                Prize: X=8400, Y=5400
            """
                .trimIndent()
                .lines()

        assertEquals(expected.toString(), input.toClawMachineOne().toString())
    }

    @Test
    fun `It should extract machine data two`() {
        val expected =
            ClawMachine(Button(94, 34, BUTTON_COST_A), Button(22, 67, BUTTON_COST_B), LongPoint(10000000008400, 10000000005400))
        val input =
            """
                Button A: X+94, Y+34
                Button B: X+22, Y+67
                Prize: X=8400, Y=5400
            """
                .trimIndent()
                .lines()

        assertEquals(expected.toString(), input.toClawMachineTwo().toString())
    }

  @ParameterizedTest(name = "button {0} <= point {1} = {2}")
  @MethodSource("getButtonComparators")
  fun `It should calculate Button less than or equal to Point`(
      button: Button,
      point: Point,
      expected: Boolean
  ) {
    assertEquals(expected, button <= point.toLongPoint())
  }

  @Test
  fun `It should do button addition`() {
    val expected = Button(Point(2, 3), 4)
    val buttonOne = Button(Point(1, 1), 1)
    val buttonTwo = Button(Point(1, 2), 3)

    assertEquals(expected.toString(), (buttonOne + buttonTwo).toString())
  }

  @Test
  fun `It should do button subtraction`() {
    val expected = Button(Point(1, 1), 1)
    val buttonOne = Button(Point(2, 3), 4)
    val buttonTwo = Button(Point(1, 2), 3)

    assertEquals(expected.toString(), (buttonOne - buttonTwo).toString())
  }

  @Test
  fun `It should cost 280 tokens`() {
    val expected = 280
    val input =
        """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400   
        """
            .trimIndent()
            .lines()

    assertEquals(expected, input.toClawMachineOne().cost)
  }

  @ParameterizedTest(name = "index {0} should cost {1}")
  @MethodSource("getMachinePrizes")
  fun `It should calculate machine prize cost`(index: Int, expected: Int?) {
    val input = testInput.toRawMachineLinesList()[index].toClawMachineOne()

    assertEquals(expected, input.cost)
  }

  @Test
  fun `It should calculate cost for all prizes`() {
    val expected = 480
    val input = testInput.costOfAllPrizesOne

    assertEquals(expected, input)
  }

  companion object {
    val testInput =
        """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
        """
            .trimIndent()

    @JvmStatic
    fun getButtonComparators() =
        listOf(
                Arguments.of(Button(0, 0, BUTTON_COST_A), Point(1, 1), true),
                Arguments.of(Button(0, 1, BUTTON_COST_A), Point(1, 1), true),
                Arguments.of(Button(1, 0, BUTTON_COST_A), Point(1, 1), true),
                Arguments.of(Button(1, 1, BUTTON_COST_A), Point(1, 1), true),
                Arguments.of(Button(1, 2, BUTTON_COST_A), Point(1, 1), false),
                Arguments.of(Button(2, 1, BUTTON_COST_A), Point(1, 1), false),
                Arguments.of(Button(2, 2, BUTTON_COST_A), Point(1, 1), false),
            )
            .iterator()

    @JvmStatic
    fun getMachinePrizes() =
        listOf(
                // index, prizeCost
                Arguments.of(0, 280),
                Arguments.of(1, null),
                Arguments.of(2, 200),
                Arguments.of(3, null),
            )
            .iterator()
  }
}
