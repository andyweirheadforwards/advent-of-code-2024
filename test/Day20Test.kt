import Day20.Race
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Test {

    @Test
    fun `It should load the input`() {
        val expected = smallTestInput
        val race = Race(expected)
        assertEquals(expected, race.toString())
    }

    @Test
    fun `It should find the start and finish`() {
        val expectedStart = Point(1, 1)
        val expectedFinish = Point(5, 1)

        val race = Race(smallTestInput)

        assertEquals(expectedStart, race.start)
        assertEquals(expectedFinish, race.finish)
    }

    @Test
    fun `It should find the race path`() {
        val expected = listOf(
            Point(1, 1),
            Point(1, 2),
            Point(2, 2),
            Point(2, 3),
            Point(3, 3),
            Point(4, 3),
            Point(4, 2),
            Point(5, 2),
            Point(5, 1),
        )
        val race = Race(smallTestInput)

        assertEquals(expected, race.path)
    }

    @Test
    fun `It should find a shortcut`() {
        val expected = listOf(
            Point(1, 1),
            Point(1, 2),
            Point(2, 2),
            Point(3, 2),
            Point(4, 2),
            Point(5, 2),
            Point(5, 1),
        )
        val race = Race(smallTestInput)
        val shortcuts = race.findShortcuts()

        assertEquals(expected, shortcuts.first())
    }

    @ParameterizedTest(name = "There are {1} cheats that save {0} picoseconds.")
    @MethodSource("getCheats")
    fun `It should find the shortcuts`(time: Int, expected: Int) {
        val race = Race(testInput)
        assertEquals(expected, race.solveOne().count { it == time })
    }

    @Test
    fun `It should solve part one`() {
        val expected = 1409
        val input = readInput("Day20")
        val race = Race(input)
        val solutionOne = race.solveOne().count { it >= 100 }

        assertEquals(expected, solutionOne)
    }

    companion object {
        val smallTestInput = """
            #######
            #S###E#
            #..#..#
            ##...##
            #######
        """.trimIndent()

        val testInput = """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
        """.trimIndent()

        @JvmStatic
        fun getCheats() = listOf(
            // time, count
            // There are 14 cheats that save 2 picoseconds.
            Arguments.of(2, 14),
            // There are 14 cheats that save 4 picoseconds.
            Arguments.of(4, 14),
            // There are 2 cheats that save 6 picoseconds.
            Arguments.of(6, 2),
            // There are 4 cheats that save 8 picoseconds.
            Arguments.of(8, 4),
            // There are 2 cheats that save 10 picoseconds.
            Arguments.of(10, 2),
            // There are 3 cheats that save 12 picoseconds.
            Arguments.of(12, 3),
            // There is one cheat that saves 20 picoseconds.
            Arguments.of(20, 1),
            // There is one cheat that saves 36 picoseconds.
            Arguments.of(36, 1),
            // There is one cheat that saves 38 picoseconds.
            Arguments.of(38, 1),
            // There is one cheat that saves 40 picoseconds.
            Arguments.of(40, 1),
            // There is one cheat that saves 64 picoseconds.
            Arguments.of(64, 1),
        ).iterator()
    }
}
