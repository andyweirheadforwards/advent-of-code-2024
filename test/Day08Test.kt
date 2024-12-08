import java.awt.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Day08Test {

  @ParameterizedTest(name = "it should list all frequencies {1}")
  @MethodSource("getGrids")
  fun `It should list all frequencies in the grid`(string: GridString, expected: CharArray) {
    assertEquals(expected.joinToString(""), AntennaMap(string).frequencies.joinToString(""))
  }

  @ParameterizedTest(name = "it should list all antenna locations {2}")
  @MethodSource("getGrids")
  fun `It should list all antenna positions grouped by frequency`(
      string: GridString,
      frequencies: Frequencies,
      expected: AntennaLocations
  ) {
    assertEquals(expected, AntennaMap(string).antennaLocations)
  }

  @Test
  fun `It should list antenna positions in test input`() {
    val grid = AntennaMap(testInput)
    val antennaLocations = grid.antennaLocations
    val count = antennaLocations.map { it.value.size }.sumOf { it }

    assertEquals(7, count)
  }

  @ParameterizedTest(name = "{0} should have vector {1} between")
  @MethodSource("getPointDifferences")
  fun `It should calculate vector between two antennas`(
      antennas: Pair<Point, Point>,
      expected: Point
  ) {
    assertEquals(expected, antennas.first.diff(antennas.second))
  }

  @ParameterizedTest(name = "{0} should have vector {1} between")
  @MethodSource("getAntinodeExamples")
  fun `It should list all antinodes - Part One`(input: String, expected: String) {
    val grid = AntennaMap(input)
    val antinodes = grid.antinodesOne

    antinodes.forEach { if (grid.symbolAt(it) == '.') grid.setSymbolAtPoint(it, '#') }

    assertEquals(expected, grid.toString())
  }

  @Test
  fun `It should list all antinodes - Part Two`() {
    val expected = testOutputTwo
    val grid = AntennaMap(testInput)
    val antinodes = grid.antinodesTwo

    antinodes.forEach { if (grid.symbolAt(it) == '.') grid.setSymbolAtPoint(it, '#') }

    assertEquals(expected, grid.toString())
  }

  @Test
  fun `It should count unique antinodes - Part One`() {
    val expected = 14
    val grid = AntennaMap(testInput)

    assertEquals(expected, grid.antinodesOne.size)
  }

  @Test
  fun `It should count unique antinodes - Part Two`() {
    val expected = 34
    val grid = AntennaMap(testInput)

    assertEquals(expected, grid.antinodesTwo.size)
  }

  companion object {

    val testInput =
        """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............        
        """
            .trimIndent()

    val testOutputOne =
        """
        ......#....#
        ...#....0...
        ....#0....#.
        ..#....0....
        ....0....#..
        .#....A.....
        ...#........
        #......#....
        ........A...
        .........A..
        ..........#.
        ..........#.
        """
            .trimIndent()

    val testOutputTwo =
        """
        ##....#....#
        .#.#....0...
        ..#.#0....#.
        ..##...0....
        ....0....#..
        .#...#A....#
        ...#..#.....
        #....#.#....
        ..#.....A...
        ....#....A..
        .#........#.
        ...#......##
        """
            .trimIndent()

    @JvmStatic
    fun getGrids() =
        listOf(
                Arguments.of(
                    """
                    A..
                    .B.
                    ..C
                    """
                        .trimIndent(),
                    "ABC".toCharArray(),
                    mapOf<Freq, List<Point>>(
                        'A' to listOf(Point(0, 0)),
                        'B' to listOf(Point(1, 1)),
                        'C' to listOf(Point(2, 2)),
                    )),
                Arguments.of(
                    """
                    ..d
                    .e.
                    f..
                    """
                        .trimIndent(),
                    "def".toCharArray(),
                    mapOf<Char, List<Point>>(
                        'd' to listOf(Point(2, 0)),
                        'e' to listOf(Point(1, 1)),
                        'f' to listOf(Point(0, 2)),
                    )),
                Arguments.of(
                    """
                    1.2
                    .3.
                    4.5
                    """
                        .trimIndent(),
                    "12345".toCharArray(),
                    mapOf<Char, List<Point>>(
                        '1' to listOf(Point(0, 0)),
                        '2' to listOf(Point(2, 0)),
                        '3' to listOf(Point(1, 1)),
                        '4' to listOf(Point(0, 2)),
                        '5' to listOf(Point(2, 2)),
                    )),
            )
            .iterator()

    @JvmStatic
    fun getPointDifferences() =
        listOf(
                Arguments.of(Pair(Point(0, 0), Point(1, 2)), Point(1, 2)),
                Arguments.of(Pair(Point(1, 2), Point(0, 0)), Point(-1, -2)),
                Arguments.of(Pair(Point(5, 5), Point(6, 3)), Point(1, -2)),
                Arguments.of(Pair(Point(5, 5), Point(4, 7)), Point(-1, 2)),
            )
            .iterator()

    @JvmStatic
    fun getAntinodeExamples() =
        listOf(
                Arguments.of(
                    """
                    ..........
                    ..........
                    ..........
                    ....a.....
                    ..........
                    .....a....
                    ..........
                    ..........
                    ..........
                    ..........
                    """
                        .trimIndent(),
                    """
                    ..........
                    ...#......
                    ..........
                    ....a.....
                    ..........
                    .....a....
                    ..........
                    ......#...
                    ..........
                    ..........
                    """
                        .trimIndent()),
                Arguments.of(
                    """
                    ..........
                    ..........
                    ..........
                    ....a.....
                    ........a.
                    .....a....
                    ..........
                    ..........
                    ..........
                    ..........
                    """
                        .trimIndent(),
                    """
                    ..........
                    ...#......
                    #.........
                    ....a.....
                    ........a.
                    .....a....
                    ..#.......
                    ......#...
                    ..........
                    ..........
                    """
                        .trimIndent()),
                Arguments.of(
                    """
                    ..........
                    ..........
                    ..........
                    ....a.....
                    ........a.
                    .....a....
                    ..........
                    ......A...
                    ..........
                    ..........
                    """
                        .trimIndent(),
                    """
                    ..........
                    ...#......
                    #.........
                    ....a.....
                    ........a.
                    .....a....
                    ..#.......
                    ......A...
                    ..........
                    ..........
                    """
                        .trimIndent()),
                Arguments.of(testInput, testOutputOne),
            )
            .iterator()
  }
}
