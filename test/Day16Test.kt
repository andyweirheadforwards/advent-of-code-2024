import Day16.Maze
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16Test {

  @Test
  fun `test maze 1`() {
    val mazeString =
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

    val maze = Maze(mazeString)
    val result = maze.solve()

    assertEquals(7036, result, "Total score of the path should be 7036")
  }

  @Test
  fun `test maze 2`() {
    val mazeString =
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

    val maze = Maze(mazeString)
    val result = maze.solve()

    // Assertions
    assertEquals(11048, result, "Total score of the path should be 11048")
  }
}
