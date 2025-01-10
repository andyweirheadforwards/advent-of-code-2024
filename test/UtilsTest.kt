import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import utils.Grid
import utils.findAll
import utils.findFirst
import utils.grid
import utils.readInput
import utils.setSymbolAt
import utils.string
import java.awt.Point

class UtilsTest {
    @Test
    fun readInput_readsFileContentCorrectly() {
        val content = readInput("sample")
        assertEquals("expected content", content)
    }

    @Test
    fun gridString_toGrid_convertsCorrectly() {
        val gridString =
            """
            1.2
            .3.
            4.5
            """.trimIndent()
        val expectedGrid: Grid =
            listOf<CharArray>(
                charArrayOf('1', '.', '2'),
                charArrayOf('.', '3', '.'),
                charArrayOf('4', '.', '5'),
            )
        assertEquals(expectedGrid.map { it.string }, gridString.grid.map { it.string })
    }

    fun List<CharArray>.getSymbolAt(point: Point): Char = this[point.y][point.x]

    @Test
    fun grid_getSymbolAt_returnsCorrectSymbol() {
        val grid =
            """
            1.2
            .3.
            4.5
            """.trimIndent().grid
        assertEquals('1', grid.getSymbolAt(Point(0, 0)))
        assertEquals('2', grid.getSymbolAt(Point(2, 0)))
        assertEquals('3', grid.getSymbolAt(Point(1, 1)))
        assertEquals('4', grid.getSymbolAt(Point(0, 2)))
        assertEquals('5', grid.getSymbolAt(Point(2, 2)))
    }

    @Test
    fun grid_setSymbolAt_setsSymbolCorrectly() {
        val grid =
            listOf(
                charArrayOf('#', '#', '#'),
                charArrayOf('.', '#', '.'),
                charArrayOf('#', '#', '#'),
            )
        val updatedGrid = grid.setSymbolAt(Point(1, 1), 'X')
        assertEquals('X', updatedGrid[1][1])
    }

    @Test
    fun grid_findFirst_findsFirstOccurrence() {
        val grid =
            """
            123
            455
            555
            """.trimIndent().grid
        assertEquals(Point(1, 1), grid.findFirst('5'))
        assertNull(grid.findFirst('X'))
    }

    @Test
    fun grid_findAll_findsAllOccurrences() {
        val grid =
            listOf(
                charArrayOf('#', '#', '#'),
                charArrayOf('.', '#', '.'),
                charArrayOf('#', '#', '#'),
            )
        val expectedPoints =
            listOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(1, 1), Point(0, 2), Point(1, 2), Point(2, 2))
        assertEquals(expectedPoints, grid.findAll('#'))
    }
}
