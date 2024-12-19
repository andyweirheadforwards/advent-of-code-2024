import Day19.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test

class Day19Test {

    @Test
    fun `It should get a list of towels`() {
        val expected = setOf("r", "wr", "b", "g", "bwu", "rb", "gb", "br")
        val towels = testInput.towels

        assertEquals(expected, towels)
    }

    @Test
    fun `It should get a list of designs`() {
        val expected =
            listOf(
                "brwrr",
                "bggr",
                "gbbr",
                "rrbgbr",
                "ubwu",
                "bwurrg",
                "brgr",
                "bbrgwb",
            )
        val designs = testInput.designs

        assertEquals(expected, designs)
    }

    @ParameterizedTest(name = "Design {0} can be reconstructed: {1}")
    @MethodSource("getDesigns")
    fun `It should determine if a design can be reconstructed`(design: Design, expected: Boolean) {
        val result = design.canReconstruct(testInput.towels)

        assertEquals(expected, result)
    }

    @Test
    fun `It should have correct count of designs`() {
        val input = readInput("Day19")

        val towels = input.towels
        val designs = input.designs

        val count = designs.count { it.canReconstruct(towels) }

        assertEquals(333, count)
    }

    @ParameterizedTest(name = "Design {0} should have {2} ways to be reconstructed")
    @MethodSource("getDesigns")
    fun `It should determine the number of ways a design can be reconstructed`(
        design: Design,
        canReconstruct: Boolean,
        expected: Long
    ) {
        val result = design.canReconstructWays(testInput.towels)

        assertEquals(expected, result)
    }

    @Test
    fun `It should have a sum of 16`() {
        val input = testInput
        val towels = input.towels
        val designs = input.designs

        val count = designs.sumOf { it.canReconstructWays(towels) }

        assertEquals(16L, count)
    }

    companion object {
        val testInput =
            """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
        """
                .trimIndent()

        @JvmStatic
        fun getDesigns() =
            listOf(
                Arguments.of("brwrr", true, 2L),
                Arguments.of("bggr", true, 1L),
                Arguments.of("gbbr", true, 4L),
                Arguments.of("rrbgbr", true, 6L),
                Arguments.of("ubwu", false, 0L),
                Arguments.of("bwurrg", true, 1L),
                Arguments.of("brgr", true, 2L),
                Arguments.of("bbrgwb", false, 0L),
            )
                .iterator()
    }
}
