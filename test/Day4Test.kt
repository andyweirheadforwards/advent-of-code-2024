import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Day4Test {

    @ParameterizedTest(name="It should find {2} instances of XMAS facing {1}")
    @MethodSource("getSmallGrid")
    fun `It should find XMAS in small grid`(grid: Grid, direction: Direction, expected: Int) {
        assertEquals(expected, grid.searchFor(WORD, direction))
    }

    @Test
    fun `It should find all instances of XMAS in small grid`() {
        val grid = smallGrid
        assertEquals(4, grid.searchFor(WORD))
    }

    @ParameterizedTest(name="It should find {2} instances of XMAS facing {1}")
    @MethodSource("getLargeGrid")
    fun `It should find XMAS in large grid`(grid: Grid, direction: Direction, expected: Int) {
        assertEquals(expected, grid.searchFor(WORD, direction))
    }

    @Test
    fun `It should find all instances of XMAS in large grid`() {
        val grid = largeGrid
        assertEquals(18, grid.searchFor(WORD))
    }

    @Test
    fun `It should reverse grid lines horizontally`() {
        val grid = """
            AB
            DC
        """.trimIndent()

        val expected = """
            BA
            CD
        """.trimIndent()

        assertEquals(expected, grid.reverseHorizontal())
    }

    @Test
    fun `It should reverse grid lines vertically`() {
        val grid = """
            AB
            DC
        """.trimIndent()

        val expected = """
            DC
            AB
        """.trimIndent()

        assertEquals(expected, grid.reverseVertical())
    }

    @Test
    fun `It should rotate grid CW`() {
        val grid = """
            ABC
            FED
        """.trimIndent()
        val expected = """
            FA
            EB
            DC
        """.trimIndent()

        assertEquals(expected, grid.rotatedCW())
    }

    @Test
    fun `It should rotate grid CCW`() {
        val grid = """
            ABC
            FED
        """.trimIndent()
        val expected = """
            CD
            BE
            AF
        """.trimIndent()

        assertEquals(expected, grid.rotatedCCW())
    }

    @Test
    fun`It should slope grid to the right`(){
        val grid = """
            ABC
            DEF
            GHI
        """.trimIndent()
        val expected = """
            ABC..
            .DEF.
            ..GHI
        """.trimIndent()

        assertEquals(expected, grid.slopeRight())
    }

    @Test
    fun`It should slope grid to the left`(){
        val grid = """
            ABC
            DEF
            GHI
""".trimIndent()
        val expected = """
            ..ABC
            .DEF.
            GHI..
""".trimIndent()

        assertEquals(expected, grid.slopeLeft())
    }

    companion object {
        private val smallGrid = """
            ..X...
            .SAMX.
            .A..A.
            XMAS.S
            .X....
        """.trimIndent()

        @JvmStatic
        fun getSmallGrid() = listOf(
            Arguments.of(smallGrid, Direction.NORTH, 1),
            Arguments.of(smallGrid, Direction.NORTHEAST, 0),
            Arguments.of(smallGrid, Direction.EAST, 1),
            Arguments.of(smallGrid, Direction.SOUTHEAST, 1),
            Arguments.of(smallGrid, Direction.SOUTH, 0),
            Arguments.of(smallGrid, Direction.SOUTHWEST, 0),
            Arguments.of(smallGrid, Direction.WEST, 1),
            Arguments.of(smallGrid, Direction.NORTHWEST, 0),
        ).asIterable()

        private val largeGrid = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent()

        @JvmStatic
        fun getLargeGrid() = listOf(
            Arguments.of(largeGrid, Direction.NORTH, 2),
            Arguments.of(largeGrid, Direction.NORTHEAST, 4),
            Arguments.of(largeGrid, Direction.EAST, 3),
            Arguments.of(largeGrid, Direction.SOUTHEAST, 1),
            Arguments.of(largeGrid, Direction.SOUTH, 1),
            Arguments.of(largeGrid, Direction.SOUTHWEST, 1),
            Arguments.of(largeGrid, Direction.WEST, 2),
            Arguments.of(largeGrid, Direction.NORTHWEST, 4),
        ).asIterable()
    }

}