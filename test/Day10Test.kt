import java.awt.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Day10Test {

  @Test
  fun `It should get trail heads`() {
    val expected: List<Point> =
        listOf(
            Point(0, 0),
            Point(2, 2),
        )

    val input =
        """
            012
            343
            210
        """
            .trimIndent()
            .grid

    assertEquals(expected.joinToString("\n"), input.findTrailHeads().joinToString("\n"))
  }

  @ParameterizedTest(name = "It should have score of {1}")
  @MethodSource("getTrails")
  fun `It should get trail score`(input: String, expected: Int) {
    val trail = input.grid
    val trailHead = trail.findTrailHeads().first()
    assertEquals(expected, trail.calculateTrailScore(trailHead))
  }

  @Test
  fun `It should get trail scores`() {
    val expected = listOf(5, 6, 5, 3, 1, 3, 5, 3, 5)
    val trail = testInput.grid

    val scores = trail.findTrailHeads().map { trail.calculateTrailScore(it) }

    assertEquals(expected, scores)
  }

  @Test
  fun `It should get trail ratings`() {
    val expected = listOf(20, 24, 10, 4, 1, 4, 5, 8, 5)
    val trail = testInput.grid

    val ratings = trail.findTrailHeads().map { trail.calculateTrailRating(it) }
  }

  @Test
  fun `It should get total score`() {
    val expected = 36
    val trail = testInput.grid

    val totalTrailScore = trail.totalTrailScore

    assertEquals(expected, totalTrailScore)
  }

  @Test
  fun `It should get total rating`() {
    val expected = 81
    val trail = testInput.grid

    val totalTrailRating = trail.totalTrailRating

    assertEquals(expected, totalTrailRating)
  }

  @Test
  fun `It should get neighbours`() {
    val expected =
        listOf<Point>(
            Point(1, 0),
            Point(0, 1),
            Point(2, 1),
            Point(1, 2),
        )

    val input: TrailMap =
        """
            123
            456
            789
        """
            .trimIndent()
            .grid

    val start = TrailPosition(Point(1, 1), 5)

    assertEquals(expected.joinToString("\n"), input.getNeighbours(start).joinToString("\n"))
  }

  @Test
  fun `It should get next positions`() {
    val expected =
        listOf<Point>(
            Point(2, 1),
        )

    val input: TrailMap =
        """
            123
            456
            789
        """
            .trimIndent()
            .grid

    val start = TrailPosition(Point(1, 1), 5)

    assertEquals(expected.joinToString("\n"), input.getNextTrailPoints(start).joinToString("\n"))
  }

  companion object {
    val testInput =
        """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
        """
            .trimIndent()

    @JvmStatic
    fun getTrails() =
        listOf(
                Arguments.of(
                    """
                            ...0...
                            ...1...
                            ...2...
                            6543456
                            7.....7
                            8.....8
                            9.....9
                        """
                        .trimIndent(),
                    2),
                Arguments.of(
                    """
                            ..90..9
                            ...1.98
                            ...2..7
                            6543456
                            765.987
                            876....
                            987....
                        """
                        .trimIndent(),
                    4),
                Arguments.of(
                    """
                            10..9..
                            2...8..
                            3...7..
                            4567654
                            ...8..3
                            ...9..2
                            ......1
                        """
                        .trimIndent(),
                    1),
                Arguments.of(
                    """
                            1...9..
                            2...8..
                            3...7..
                            4567654
                            ...8..3
                            ...9..2
                            .....01
                        """
                        .trimIndent(),
                    2),
            )
            .iterator()
  }
}
