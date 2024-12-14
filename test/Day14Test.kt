import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {

  @Test
  fun `It should create a list of robots from string`() {
    val expected =
        listOf(
            Robot(Point(0, 4), Point(3, -3), TEST_TILES),
            Robot(Point(6, 3), Point(-1, -3), TEST_TILES),
        )
    val input = expected.joinToString("\n") { it.toString() }

    assertEquals(expected, input.toRobotList(TEST_TILES))
  }

  @ParameterizedTest(name = "It should be at {0} after {1} seconds")
  @MethodSource("getRobotMoves")
  fun `It should teleport`(seconds: Int, expected: Point) {
    var robot = Robot("p=2,4 v=2,-3", TEST_TILES)
    robot = robot.move(seconds)

    assertEquals(expected, robot.position)
  }

  @Test
  fun `It should plot robots`() {
    val expected =
        """
            ......*..*.
            ...........
            *..........
            .**........
            .....*.....
            ...**......
            .*....*....
        """
            .trimIndent()
    val restroom = Restroom(TEST_TILES, testInput)
    restroom.move(100)

    assertEquals(expected, restroom.toString())
  }

  @Test
  fun `It should count robots in quadrants`() {
    val expected = listOf(1, 3, 4, 1)
    val restroom = Restroom(TEST_TILES, testInput)

    restroom.move(100)

    assertEquals(expected, restroom.countRobots())
  }

  @Test
  fun `It should calculate safety factor`() {
    val expected = 12
    val restroom = Restroom(TEST_TILES, testInput)
    restroom.move(100)

    assertEquals(expected, restroom.safetyFactor)
  }

  companion object {
    val TEST_TILES = Pair(11, 7)
    val testInput =
        """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
        """
            .trimIndent()

    @JvmStatic
    fun getRobotMoves() =
        listOf(
                Arguments.of(0, Point(2, 4)),
                Arguments.of(1, Point(4, 1)),
                Arguments.of(2, Point(6, 5)),
                Arguments.of(3, Point(8, 2)),
                Arguments.of(4, Point(10, 6)),
                Arguments.of(5, Point(1, 3)),
            )
            .iterator()
  }
}
