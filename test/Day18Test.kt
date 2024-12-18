import Day18.Byte
import Day18.Ram
import java.awt.Point
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
  @Test
  fun `It should initialise grid`() {
    val expected = testGrid
    val ram = Ram(testInput, 6)

    assertEquals(expected, ram.toString(12))
  }

  @Test
  fun `It should solve the test data`() {
    val ram = Ram(testInput, 6)
    val (solution) = ram.solve(12)

    assertEquals(22, solution)
  }

  @Test
  fun `It should be unsolvable`() {
    val ram = Ram(testInput, 6)
    val (solution) = ram.solve()

    assertEquals(Int.MAX_VALUE, solution)
  }

  @Test
  fun `It should get last byte`() {
    val ram = Ram(testInput, 6)
    val byte: Byte = ram.solveLastByte()

    assertEquals(Point(6, 1).toString(), byte.toString())
  }

  companion object {
    val testInput =
        """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0
        """
            .trimIndent()

    val testGrid =
        """
            ...#...
            ..#..#.
            ....#..
            ...#..#
            ..#..#.
            .#..#..
            #.#....
        """
            .trimIndent()
  }
}
