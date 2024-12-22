import Day21.KeypadTwo
import Day21.Puzzle
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day21Test {

    @Test
    fun `It should return key presses for numeric keypad`() {
        val expected = "029A"
        val keypad = KeypadTwo()
        val routes = keypad.findNumRoutes(expected)

        routes.forEach {
            assertEquals(expected, keypad.findNumKeys(it))
        }
    }

    @Test
    fun `It should return key presses for directional keypad`() {
        val expected = "v<<A>>^A<A>AvA<^AA>A<vA A A >^A"
        val input = "029A"
        val keypad = KeypadTwo()

        val keypadRoutes = keypad.findNumRoutes(input)
        keypadRoutes.forEach {
            assertEquals(input.length, it.count { it == 'A' })
        }

        val routes = keypadRoutes.flatMap { keypad.findDirRoutes(it) }
        routes.forEach {
            assertEquals(keypadRoutes.minOf { it.length }, it.count { it == 'A' })
        }
    }

    @Test
    fun `It should map output to input - num`() {
        val expected = "029A"
        val input = listOf("<A^A>^^AvvvA", "<A^A^>^AvvvA", "<A^A^^>AvvvA")

        val keypad = KeypadTwo()
        input.forEach {
            val keyPresses = keypad.findNumKeys(it)
            assertEquals(keyPresses, expected)
        }
    }


    @Test
    fun `It should map output to input - dir`() {
        val expected = "<A^A>^^AvvvA"
        val input = "v<<A>>^A<A>AvA<^AA>A<vAAA>^A"

        val keyPresses = KeypadTwo().findDirKeys(input)
        assertEquals(keyPresses, expected)
    }

    @Test
    fun `It should map output to input - dir - num`() {
        val expected = "029A"
        val input = "v<<A>>^A<A>AvA<^AA>A<vAAA>^A"
        val keypad = KeypadTwo()
        val keyPresses = keypad.findNumKeys(keypad.findDirKeys(input))

        assertEquals(keyPresses, expected)
    }

    @Test
    fun `It should map output to input - dir - dir - num`() {
        val expected = "029A"
        val input = "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A"
        val keypad = KeypadTwo()
        val keyPresses = keypad.findNumKeys(keypad.findDirKeys(keypad.findDirKeys(input)))

        assertEquals(keyPresses, expected)
    }

    @Test
    fun `It should return button presses for input on directional keypad - one`() {
        val expected = "v<<A>>^A<A>AvA<^AA>A<vAAA>^A"
        val input = "029A"
        val keypad = KeypadTwo()
        val routes = keypad.findNumRoutes(input).flatMap { keypad.findDirRoutes(it) }

        assertEquals(expected.length, routes.minOf { it.length })
        assertTrue(routes.contains(expected))
    }

    @Test
    fun `It should return button presses for input on directional keypad - two`() {
        val expected = "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A"
        val input = "029A"
        val keypad = KeypadTwo()
        val routes =
            keypad.findNumRoutes(input).flatMap { keypad.findDirRoutes(it, 2) }

        assertEquals(expected.length, routes.minOf { it.length })
    }


    @Test
    fun `It should solve the puzzle - one`() {
        val expected = "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A"
        val input = "029A"
        val puzzle = Puzzle()
        val minLength = puzzle.findRoute(input).minOf { it.length }

        assertEquals(expected.length, minLength)
    }

    @ParameterizedTest(name = "It should find directions to key code {0}")
    @MethodSource("getKeyCodes")
    fun `It should find directions to key codes`(keyCode: String, expected: String) {
        val puzzle = Puzzle()
        val minLength = puzzle.findRoute(keyCode).minOf { it.length }

        assertEquals(expected.length, minLength)
    }

    @ParameterizedTest(name = "It should find complexity to key code {0}")
    @MethodSource("getKeyCodes")
    fun `It should find solutions to key codes`(keyCode: String, directions: String, expected: Int) {
        val puzzle = Puzzle()
        assertEquals(directions.length * expected, puzzle.findComplexity(keyCode))
    }

    @Test
    fun `It should solve part one`() {
        val expected = 215374
        val puzzle = Puzzle()
        val input = readInput("Day21")

        assertEquals(expected, puzzle.solveOne(input))
    }

    companion object {
        @JvmStatic
        fun getKeyCodes() = listOf(
            Arguments.of("029A", "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A", 29),
            Arguments.of("980A", "<v<A>>^AAAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A", 980),
            Arguments.of("179A", "<v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A", 179),
            Arguments.of("456A", "<v<A>>^AA<vA<A>>^AAvAA<^A>A<vA>^A<A>A<vA>^A<A>A<v<A>A>^AAvA<^A>A", 456),
            Arguments.of("379A", "<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A", 379),
        ).iterator()
    }
}
