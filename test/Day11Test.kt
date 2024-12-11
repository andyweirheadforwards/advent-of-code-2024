import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test

class Day11Test {

  @ParameterizedTest(name = "convert a {0} to {1}")
  @MethodSource("getRuleScenarios")
  fun `It should stones`(stones: Stones, expected: Stones) {
    assertEquals(expected, stones.blink())
  }

  @Test
  fun `It should transform stones`() {
    val expected = listOf("1", "2024", "1", "0", "9", "9", "2021976")
    val stones = listOf("0", "1", "10", "99", "999")

    assertEquals(expected, stones.blink())
  }

    @ParameterizedTest(name = "{0} blinks should transform to {1}")
    @MethodSource("getSixBlinks")
    fun `It should handle multiple blinks`(blinks: Int, expected: StoneString) {
        val stones = "125 17".stones

        assertEquals(expected, stones.blink(blinks).stoneString)
    }

    @ParameterizedTest(name = "{0} blinks should transform to {1}")
    @MethodSource("getSixBlinks")
    fun `It should count blinks`(blinks: Int, expectedString: StoneString) = runBlocking {
        val stones = "125 17".stones
        val expected = expectedString.stones.count().toLong()
        assertEquals(expected, stones.stonesMap.blink(blinks).values.sumOf { it })
    }

  companion object {
    @JvmStatic
    fun getRuleScenarios() =
        listOf(
                Arguments.of(listOf("0"), listOf("1")),
                Arguments.of(listOf("1"), listOf("2024")),
                Arguments.of(listOf("10"), listOf("1", "0")),
                Arguments.of(listOf("1000"), listOf("10", "0")),
                Arguments.of(listOf("99"), listOf("9", "9")),
                Arguments.of(listOf("999"), listOf("2021976")),
            )
            .iterator()

    @JvmStatic
    fun getSixBlinks() =
        listOf(
                Arguments.of(1, "253000 1 7"),
                Arguments.of(2, "253 0 2024 14168"),
                Arguments.of(3, "512072 1 20 24 28676032"),
                Arguments.of(4, "512 72 2024 2 0 2 4 2867 6032"),
                Arguments.of(5, "1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32"),
                Arguments.of(6, "2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2"),
            )
            .iterator()
  }
}
