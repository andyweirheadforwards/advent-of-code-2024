import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import kotlin.test.assertFailsWith

class Day06Test {

  val testInput =
      """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..G.....
        ........#.
        #.........
        ......#...
    """
          .trimIndent()

  val testOutput =
      """
        ....#.....
        ....XXXXX#
        ....X...X.
        ..#.X...X.
        ..XXXXX#X.
        ..X.X.X.X.
        .#XXXXXXX.
        .XXXXXXX#.
        #XXXXXXX..
        ......#X..
    """
          .trimIndent()

  @ParameterizedTest(name = "{0} should be {1} after turning right")
  @MethodSource("getDirections")
  fun `It should change direction by turning right`(
      direction: GuardDirection,
      expected: GuardDirection
  ) {

    assertEquals(expected, direction.turnRight)
  }

  @Test
  fun `It should fetch symbol at position`() {
    val map: PatrolMap =
        """
            ...
            .X.
            ...
        """
            .trimIndent()
            .map

    assertEquals('.', map.getSymbolAt(Point(0, 0)))
    assertEquals('X', map.getSymbolAt(Point(1, 1)))
  }

  @Test
  fun `It should detect obstacles`() {
    val map: PatrolMap =
        """
            ...
            .#.
            ...
        """
            .trimIndent()
            .map

    assertFalse(map.isObstruction(Point(0, 0)))
    assertTrue(map.isObstruction(Point(1, 1)))
  }

  @Test
  fun `It should detect exit`() {
    val map: PatrolMap =
        """
            ...
            .#.
            ...
        """
            .trimIndent()
            .map

    assertTrue(map.isExit(Point(-1, -1)))
    assertFalse(map.isExit(Point(1, 1)))
    assertTrue(map.isExit(Point(3, 3)))
  }

  @ParameterizedTest(name = "{0} should be {1} after turning right")
  @MethodSource("getNextLocations")
  fun `It should get the next guard position based on direction`(
      direction: GuardDirection,
      expected: Point
  ) {
    val guard = Guard(Point(1, 1), direction)

    assertEquals(expected, guard.nextPoint)
  }

  @Test
  fun `It should mark the map`() {
    val map: PatrolMap =
        """
            ...
            ...
            ...
        """
            .trimIndent()
            .map

    val expected =
        """
            ...
            .X.
            ...
        """
            .trimIndent()

    val guardLocation = Point(1, 1)

    assertEquals(expected, map.setSymbolAt(guardLocation).string)
  }

  @Test
  fun `It should patrol the map`() {
    val map = testInput.replace('G', GuardDirection.NORTH.symbol).map

    val expected =
        """
            ....#.....
            ....XXXXX#
            ....X...X.
            ..#.X...X.
            ..XXXXX#X.
            ..X.X.X.X.
            .#XXXXXXX.
            .XXXXXXX#.
            #XXXXXXX..
            ......#X..
        """
            .trimIndent()

    assertEquals(expected, map.patrol().first.string)
  }

  @Test
  fun `It should count distinct guard locations from string`() {
    val mapString: PatrolMapString = testOutput

    val expected = 41

    assertEquals(expected, mapString.guardLocationCount)
  }

  @Test
  fun `It should count distinct guard locations from map`() {
    val map: PatrolMap = testOutput.map

    val expected = 41

    assertEquals(expected, map.guardLocationCount)
  }

  @Test
  fun `It should throw StuckInLoop exception`() {
    val map: PatrolMap =
        """
            ....#.....
            ....+---+#
            ....|...|.
            ..#.|...|.
            ....|..#|.
            ....|...|.
            .#.O^---+.
            ........#.
            #.........
            ......#...
        """
            .trimIndent()
            .map

    val expected =
        """
            ....#.....
            ....XXXXX#
            ....X...X.
            ..#.X...X.
            ....X..#X.
            ....X...X.
            .#.OXXXXX.
            ........#.
            #.........
            ......#...
        """
            .trimIndent()

    val exception = assertFailsWith<StuckInLoopException> { map.patrol() }

    assertEquals(expected, exception.map.string)
  }

  @Test
  fun `It should count loop obstruction positions`() {
    val map: PatrolMap = testInput.replace('G', GuardDirection.NORTH.symbol).map

    val expected = 6

    assertEquals(expected, map.countLoopObstructionLocations())
  }

  companion object {
    @JvmStatic
    fun getDirections() =
        listOf(
                // current direction, direction after turning right
                Arguments.of(GuardDirection.NORTH, GuardDirection.EAST),
                Arguments.of(GuardDirection.EAST, GuardDirection.SOUTH),
                Arguments.of(GuardDirection.SOUTH, GuardDirection.WEST),
                Arguments.of(GuardDirection.WEST, GuardDirection.NORTH),
            )
            .asIterable()

    @JvmStatic
    fun getNextLocations() =
        listOf(
                // current direction, next position (starting from 1,1)
                Arguments.of(GuardDirection.NORTH, Point(1, 0)),
                Arguments.of(GuardDirection.EAST, Point(2, 1)),
                Arguments.of(GuardDirection.SOUTH, Point(1, 2)),
                Arguments.of(GuardDirection.WEST, Point(0, 1)),
            )
            .asIterable()
  }
}
