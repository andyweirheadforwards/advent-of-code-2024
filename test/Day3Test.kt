import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

private const val INPUT = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
private const val ENABLED_INPUT = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

class Day3Test {

    @Test
    fun `It should return a MulList`() {
        val expected = listOf(
            "mul(2,4)",
            "mul(5,5)",
            "mul(11,8)",
            "mul(8,5)",
        )

        assertEquals(expected, INPUT.toMulList())
    }

    @Test
    fun `It should calculate the sum of the line`() {
        val expected = 161

        assertEquals(expected, INPUT.toMulList().calculate())
    }

    @Test
    fun `It should return an enabled MulList`() {
        val expected = listOf(
            "mul(2,4)",
            "mul(8,5)",
        )

        assertEquals(expected, ENABLED_INPUT.enabledMemory.toMulList())
    }

    @Test
    fun `It should calculate the enabled sum of the line`() {
        val expected = 48

        assertEquals(expected, ENABLED_INPUT.enabledMemory.toMulList().calculate())
    }
}