import Day16.Maze
import Day16.countTilesOnBestPath
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import kotlin.test.assertEquals

class Day16Test {

  @ParameterizedTest(name = "Test maze {index} should have a score of {1}")
  @MethodSource("getTestInput")
  fun `It should calculate a maze score`(input: String, expected: Int, tiles: Int) {
    val maze = Maze(input)
    val (score) = maze.solve()

    assertEquals(expected, score, "Total score of the path should be 7036")
  }

  @Test
  fun `It should find the points along the shortest path`() {

    val maze = Maze(mazeWithOnePath)
    val (score, paths) = maze.solve()

    val expectedScore = 5010
    val expectedPath =
        listOf(
            Point(1, 1), // Start
            Point(1, 2),
            Point(1, 3),
            Point(2, 3),
            Point(3, 3),
            Point(3, 2),
            Point(3, 1),
            Point(4, 1),
            Point(5, 1),
            Point(5, 2),
            Point(5, 3), // Finish
        )
    val expectedTiles = 11

    assertEquals(expectedScore, score)
    assertEquals(expectedPath.joinToString("\n"), paths.first().joinToString("\n"))
    assertEquals(expectedTiles, countTilesOnBestPath(paths))
  }

  @Test
  fun `It should find all shortest paths`() {

    val maze = Maze(mazeWithTwoPaths)
    val (_, paths) = maze.solve()

    val expectedTiles = 16

    assertEquals(2, paths.size)
    assertEquals(expectedTiles, countTilesOnBestPath(paths))
  }

  @ParameterizedTest(name = "Test maze {index} should have {2} tiles")
  @MethodSource("getTestInput")
  fun `It should find the number of tiles on the best path`(
      input: String,
      score: Int,
      expected: Int
  ) {
    val maze = Maze(input)
    val (_, paths) = maze.solve()
    assertEquals(expected, countTilesOnBestPath(paths))
  }

  @Test
  fun `It should match string - one`() {
    val expected =
        """
            ###############
            #.......#....O#
            #.#.###.#.###O#
            #.....#.#...#O#
            #.###.#####.#O#
            #.#.#.......#O#
            #.#.#####.###O#
            #..OOOOOOOOO#O#
            ###O#O#####O#O#
            #OOO#O....#O#O#
            #O#O#O###.#O#O#
            #OOOOO#...#O#O#
            #O###.#.#.#O#O#
            #O..#.....#OOO#
            ###############
        """
            .trimIndent()

    val input = testInput1
    assertEquals(expected, Maze(input).toString())
    assertEquals(3, Maze(input).solve().second.size)
  }

  @Test
  fun `It should match string - two`() {
    val expected =
        """
            #################
            #...#...#...#..O#
            #.#.#.#.#.#.#.#O#
            #.#.#.#...#...#O#
            #.#.#.#.###.#.#O#
            #OOO#.#.#.....#O#
            #O#O#.#.#.#####O#
            #O#O..#.#.#OOOOO#
            #O#O#####.#O###O#
            #O#O#..OOOOO#OOO#
            #O#O###O#####O###
            #O#O#OOO#..OOO#.#
            #O#O#O#####O###.#
            #O#O#OOOOOOO..#.#
            #O#O#O#########.#
            #O#OOO..........#
            #################
        """
            .trimIndent()

    val input = testInput2
    assertEquals(expected, Maze(input).toString())
    assertEquals(2, Maze(input).solve().second.size)
  }

  @Test
  fun `It should match string - three`() {
    val expected =
        """
                ########
                ###OOOO#
                ###O#O##
                #OOO#O##
                #O#O#O##
                #OOOOO##
                #O######
                ########
            """
            .trimIndent()

    val input =
        """
                ########
                ###...E#
                ###.#.##
                #...#.##
                #.#.#.##
                #.....##
                #S######
                ########
            """
            .trimIndent()

    assertEquals(expected, Maze(input).toString())
  }

  companion object {
    val testInput1 =
        """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
        """
            .trimIndent()
    val testInput2 =
        """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
        """
            .trimIndent()

    val mazeWithOnePath =
        """
            #######
            #S#   #
            # # # #
            #   #E#
            #######
        """
            .trimIndent()

    val mazeWithTwoPaths =
        """
            #######
            ###   #
            #   # #
            #S###E#
            # #   #
            #   ###
            #######
        """
            .trimIndent()

    @JvmStatic
    fun getTestInput() =
        listOf(Arguments.of(testInput1, 7036, 45), Arguments.of(testInput2, 11048, 64)).iterator()
  }
}
