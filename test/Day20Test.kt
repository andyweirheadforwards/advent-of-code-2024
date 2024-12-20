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
    fun `It should find a shortcut - One`() {
        val expected = 7
        val input = """
            #######
            #S###E#
            #..#..#
            ##...##
            #######
        """.trimIndent()
        val race = Race(input)
        val shortcuts = race.findShortcuts(2)

        assertEquals(1, shortcuts.count())
        assertEquals(expected, shortcuts.first())
    }

    @Test
    fun `It should find a shortcut - Two`() {
        val expected = 3
        val input = """
            #####
            #S#E#
            #...#
            #####
        """.trimIndent()
        val race = Race(input)
        val shortcuts = race.findShortcuts(2)

        assertEquals(1, shortcuts.count())
        assertEquals(expected, shortcuts.first())
    }

    @Test
    fun `It should find a shortcut - Three`() {
        val expected = 8
        val input = """
            ########
            #S####E#
            #..##..#
            ##....##
            ########
        """.trimIndent()
        val race = Race(input)
        val shortcuts = race.findShortcuts(3)

        assertEquals(1, shortcuts.count())
        assertEquals(expected, shortcuts.first())
    }

    @ParameterizedTest(name = "There are {1} cheats that save {0} picoseconds.")
    @MethodSource("getCheatsOne")
    fun `It should find the shortcuts - one`(time: Int, expected: Int) {
        val race = Race(testInput)
        val result = race.solveOne()
        assertEquals(expected, result.count { it == time })
    }

    @Test
    fun `It should solve part one`() {
        val expected = 1409
        val input = readInput("Day20")
        val race = Race(input)
        val solutionOne = race.solveOne()

        assertEquals(expected, solutionOne.count { it >= 100 })
    }

    @ParameterizedTest(name = "There are {1} cheats that save {0} picoseconds.")
    @MethodSource("getCheatsTwo")
    fun `It should find the shortcuts - two`(time: Int, expected: Int) {
        val race = Race(testInput)
        val result = race.solveTwo()
        assertEquals(expected, result.count { it == time })
    }

    @Test
    fun `It should solve part two`() {
        val expected = 1012821
        val input = readInput("Day20")
        val race = Race(input)
        val solutionOne = race.solveTwo()

        assertEquals(expected, solutionOne.count { it >= 100 })
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
        fun getCheatsOne() = listOf(
            // time, count
            Arguments.of(2, 14),
            Arguments.of(4, 14),
            Arguments.of(6, 2),
            Arguments.of(8, 4),
            Arguments.of(10, 2),
            Arguments.of(12, 3),
            Arguments.of(20, 1),
            Arguments.of(36, 1),
            Arguments.of(38, 1),
            Arguments.of(40, 1),
            Arguments.of(64, 1),
        ).iterator()

        @JvmStatic
        fun getCheatsTwo() = listOf(
            // time, count
            Arguments.of(50, 32),
            Arguments.of(52, 31),
            Arguments.of(54, 29),
            Arguments.of(56, 39),
            Arguments.of(58, 25),
            Arguments.of(60, 23),
            Arguments.of(62, 20),
            Arguments.of(64, 19),
            Arguments.of(66, 12),
            Arguments.of(68, 14),
            Arguments.of(70, 12),
            Arguments.of(72, 22),
            Arguments.of(74, 4),
            Arguments.of(76, 3),
        ).iterator()
    }
}
