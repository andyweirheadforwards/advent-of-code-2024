import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Day04Test {

  @ParameterizedTest(name = "It should find {2} instances of WORD facing {1}")
  @MethodSource("getSmallGridString")
  fun `It should find WORD in small grid`(grid: GridString, direction: Direction, expected: Int) {
    assertEquals(expected, grid.searchForWord(WORD, direction))
  }

  @Test
  fun `It should find all instances of WORD in small grid`() {
    val grid = smallGridString
    assertEquals(4, grid.searchForWord(WORD))
  }

  @ParameterizedTest(name = "It should find {2} instances of WORD facing {1}")
  @MethodSource("getLargeGridString")
  fun `It should find WORD in large grid`(grid: GridString, direction: Direction, expected: Int) {
    assertEquals(expected, grid.searchForWord(WORD, direction))
  }

  @Test
  fun `It should find all instances of WORD in large grid`() {
    val grid = largeGridString
    assertEquals(18, grid.searchForWord(WORD))
  }

  @Test
  fun `It should reverse grid lines horizontally`() {
    val grid =
        """
            AB
            DC
        """
            .trimIndent()

    val expected =
        """
            BA
            CD
        """
            .trimIndent()

    assertEquals(expected, grid.reverseHorizontal())
  }

  @Test
  fun `It should reverse grid lines vertically`() {
    val grid =
        """
            AB
            DC
        """
            .trimIndent()

    val expected =
        """
            DC
            AB
        """
            .trimIndent()

    assertEquals(expected, grid.reverseVertical())
  }

  @Test
  fun `It should rotate grid CW`() {
    val grid =
        """
            ABC
            FED
        """
            .trimIndent()
    val expected =
        """
            FA
            EB
            DC
        """
            .trimIndent()

    assertEquals(expected, grid.rotatedCW())
  }

  @Test
  fun `It should rotate grid CCW`() {
    val grid =
        """
            FA
            EB
            DC
        """
            .trimIndent()
    val expected =
        """
            ABC
            FED
        """
            .trimIndent()

    assertEquals(expected, grid.rotatedCCW())
  }

  @Test
  fun `It should slope grid to the right`() {
    val grid =
        """
            ABC
            DEF
            GHI
        """
            .trimIndent()
    val expected =
        """
            ABC..
            .DEF.
            ..GHI
        """
            .trimIndent()

    assertEquals(expected, grid.slopeRight())
  }

  @Test
  fun `It should slope grid to the left`() {
    val grid =
        """
            ABC
            DEF
            GHI
"""
            .trimIndent()
    val expected =
        """
            ..ABC
            .DEF.
            GHI..
"""
            .trimIndent()

    assertEquals(expected, grid.slopeLeft())
  }

  @ParameterizedTest(name = "{0} rotated {1} times should match X_MAS")
  @MethodSource("getXMas")
  fun `It should match X_MAS rotated`(grid: GridString, rotated: Int) {
    assertTrue(grid.matchesXMas())
  }

  @Test
  fun `It should not match invalid X_MAS`() {
    val invalidGridString: GridString =
        """
            M S
             X 
            M S
        """
            .trimIndent()

    assertFalse(invalidGridString.matchesXMas())
  }

  @Test
  fun `It should find all instances of X_MAS in grid`() {
    val grid: GridString =
        """
            .M.S......
            ..A..MSMS.
            .M.S.MAA..
            ..A.ASMSM.
            .M.S.M....
            ..........
            S.S.S.S.S.
            .A.A.A.A..
            M.M.M.M.M.
            ..........
        """
            .trimIndent()
    val expected = 9

    val number = grid.searchForXMas()

    assertEquals(expected, number)
  }

  companion object {
    private val smallGridString =
        """
            ..X...
            .SAMX.
            .A..A.
            XMAS.S
            .X....
        """
            .trimIndent()

    @JvmStatic
    fun getSmallGridString() =
        listOf(
                Arguments.of(smallGridString, Direction.NORTH, 1),
                Arguments.of(smallGridString, Direction.NORTHEAST, 0),
                Arguments.of(smallGridString, Direction.EAST, 1),
                Arguments.of(smallGridString, Direction.SOUTHEAST, 1),
                Arguments.of(smallGridString, Direction.SOUTH, 0),
                Arguments.of(smallGridString, Direction.SOUTHWEST, 0),
                Arguments.of(smallGridString, Direction.WEST, 1),
                Arguments.of(smallGridString, Direction.NORTHWEST, 0),
            )
            .asIterable()

    private val largeGridString =
        """
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
        """
            .trimIndent()

    @JvmStatic
    fun getLargeGridString() =
        listOf(
                Arguments.of(largeGridString, Direction.NORTH, 2),
                Arguments.of(largeGridString, Direction.NORTHEAST, 4),
                Arguments.of(largeGridString, Direction.EAST, 3),
                Arguments.of(largeGridString, Direction.SOUTHEAST, 1),
                Arguments.of(largeGridString, Direction.SOUTH, 1),
                Arguments.of(largeGridString, Direction.SOUTHWEST, 1),
                Arguments.of(largeGridString, Direction.WEST, 2),
                Arguments.of(largeGridString, Direction.NORTHWEST, 4),
            )
            .asIterable()

    @JvmStatic
    fun getXMas() =
        (0..3)
            .map { rotated ->
              var xmas = X_MAS.replace(" ", "Z")
              repeat(rotated) { xmas = xmas.rotatedCW() }
              Arguments.of(xmas, rotated)
            }
            .asIterable()
  }
}
