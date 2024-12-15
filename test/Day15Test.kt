import Day15.Warehouse
import Day15.Warehouse.Companion.ROBOT
import Day15.Warehouse.Companion.SPACE
import Day15.WideWarehouse
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Point
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {
  @Test
  fun `It should read input`() {
    val expectedGrid =
        """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########
        """
            .trimIndent()
            .grid
    val expectedDirections =
        listOf(
            Direction.NORTH,
            Direction.NORTH,
            Direction.SOUTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.WEST,
            Direction.EAST,
            Direction.EAST)
    val expectedRobot = Point(2, 2)

    val input = expectedGrid.string + "\n\n^^vv<<>>"

    val warehouse = Warehouse(input)

    assertEquals(expectedGrid.string, warehouse.toString())
    assertEquals(expectedGrid.string.replace(ROBOT, SPACE), warehouse.grid.string)
    assertEquals(expectedDirections.joinToString(""), warehouse.directions.joinToString(""))
    assertEquals(expectedRobot, warehouse.robot)
  }

  @ParameterizedTest(name = "It should move {0} {1}")
  @MethodSource("getMoves")
  fun `It should move robot`(index: Int, direction: Char, expected: String) {
    val warehouse = Warehouse(testInput)
    warehouse.move(index)

    assertEquals(expected.replace(ROBOT, SPACE), warehouse.grid.string)
    assertEquals(expected, warehouse.toString())
  }

  @Test
  fun `It should move small input`() {
    val expected =
        """
            ########
            #....OO#
            ##.....#
            #.....O#
            #.#O@..#
            #...O..#
            #...O..#
            ########
        """
            .trimIndent()

    val warehouse = Warehouse(testInput)
    warehouse.move()

    assertEquals(expected.replace(ROBOT, SPACE), warehouse.grid.string)
    assertEquals(expected, warehouse.toString())
  }

  @Test
  fun `It should move big input`() {
    val expected =
        """
          ##########
          #.O.O.OOO#
          #........#
          #OO......#
          #OO@.....#
          #O#.....O#
          #O.....OO#
          #O.....OO#
          #OO....OO#
          ##########
        """
            .trimIndent()

    val warehouse = Warehouse(bigTestInput)
    warehouse.move()

    assertEquals(expected.replace(ROBOT, SPACE), warehouse.grid.string)
    assertEquals(expected, warehouse.toString())
  }

  @Test
  fun `It should calculate GPS coordinate`() {
    val expected = 104
    val input =
        """
            #######
            #...O..
            #.....@
            
            <>
        """
            .trimIndent()

    val warehouse = Warehouse(input)

    assertEquals(listOf(expected), warehouse.gpsCoordinates)
  }

  @Test
  fun `It should sum all coordinates - small`() {
    val expected = 2028
    val warehouse = Warehouse(testInput)
    warehouse.move()

    assertEquals(expected, warehouse.gpsSum)
  }

  @Test
  fun `It should sum all coordinates - big`() {
    val expected = 10092
    val warehouse = Warehouse(bigTestInput)
    warehouse.move()

    assertEquals(expected, warehouse.gpsSum)
  }

  @Test
  fun `It should draw wide warehouse`() {
    val expectedGrid =
        """
            ##############
            ##......##..##
            ##..........##
            ##....[][]@.##
            ##....[]....##
            ##..........##
            ##############
        """
            .trimIndent()
            .grid
    val expectedRobot = Point(10, 3)

    val warehouse = WideWarehouse(wwTestInput)

    assertEquals(expectedGrid.string, warehouse.toString())
    assertEquals(expectedGrid.string.replace(ROBOT, SPACE), warehouse.grid.string)
    assertEquals(expectedRobot, warehouse.robot)
  }

  @ParameterizedTest(name = "It should move {0} {1}")
  @MethodSource("getWwMoves")
  fun `It should move wide warehouse robot`(index: Int, direction: Char, expected: String) {
    val warehouse = WideWarehouse(wwTestInput)
    warehouse.move(index)

    assertEquals(expected.replace(ROBOT, SPACE), warehouse.grid.string)
    assertEquals(expected, warehouse.toString())
  }

  @Test
  fun `It should calculate GPS coordinate - wide`() {
    val expected = 105
    val input =
        """
                #######
                #..O@..
                #......
                
                <
            """
            .trimIndent()

    val warehouse = WideWarehouse(input)
    warehouse.move()

    assertEquals(listOf(expected), warehouse.gpsCoordinates)
  }

  @Test
  fun `It should sum all coordinates - wide`() {
    val expected = 9021
    val warehouse = WideWarehouse(bigTestInput)
    warehouse.move()

    assertEquals(expected, warehouse.gpsSum)
  }

  companion object {
    val testInput =
        """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            <^^>>>vv<v>>v<<
        """
            .trimIndent()

    val bigTestInput =
        """
          ##########
          #..O..O.O#
          #......O.#
          #.OO..O.O#
          #..O@..O.#
          #O#..O...#
          #O..O..O.#
          #.OO.O.OO#
          #....O...#
          ##########

          <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
          vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
          ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
          <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
          ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
          ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
          >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
          <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
          ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
          v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
      """
            .trimIndent()

    val wwTestInput =
        """
          #######
          #...#.#
          #.....#
          #..OO@#
          #..O..#
          #.....#
          #######

          <vv<<^^<<^^
        """
            .trimIndent()

    @JvmStatic
    fun getMoves() =
        listOf(
                Arguments.of(
                    0,
                    ' ',
                    """
                        ########
                        #..O.O.#
                        ##@.O..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #......#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    1,
                    '<',
                    """
                        ########
                        #..O.O.#
                        ##@.O..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #......#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    2,
                    '^',
                    """
                        ########
                        #.@O.O.#
                        ##..O..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #......#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    3,
                    '^',
                    """
                        ########
                        #.@O.O.#
                        ##..O..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #......#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    4,
                    '>',
                    """
                        ########
                        #..@OO.#
                        ##..O..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #......#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    5,
                    '>',
                    """
                        ########
                        #...@OO#
                        ##..O..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #......#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    6,
                    '>',
                    """
                        ########
                        #...@OO#
                        ##..O..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #......#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    7,
                    'v',
                    """
                        ########
                        #....OO#
                        ##..@..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #...O..#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    8,
                    'v',
                    """
                        ########
                        #....OO#
                        ##..@..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #...O..#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    9,
                    '<',
                    """
                        ########
                        #....OO#
                        ##.@...#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #...O..#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    10,
                    'v',
                    """
                        ########
                        #....OO#
                        ##.....#
                        #..@O..#
                        #.#.O..#
                        #...O..#
                        #...O..#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    11,
                    '>',
                    """
                        ########
                        #....OO#
                        ##.....#
                        #...@O.#
                        #.#.O..#
                        #...O..#
                        #...O..#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    12,
                    '>',
                    """
                        ########
                        #....OO#
                        ##.....#
                        #....@O#
                        #.#.O..#
                        #...O..#
                        #...O..#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    13,
                    'v',
                    """
                        ########
                        #....OO#
                        ##.....#
                        #.....O#
                        #.#.O@.#
                        #...O..#
                        #...O..#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    14,
                    '<',
                    """
                        ########
                        #....OO#
                        ##.....#
                        #.....O#
                        #.#O@..#
                        #...O..#
                        #...O..#
                        ########
                    """
                        .trimIndent()),
                Arguments.of(
                    15,
                    '<',
                    """
                        ########
                        #....OO#
                        ##.....#
                        #.....O#
                        #.#O@..#
                        #...O..#
                        #...O..#
                        ########
                    """
                        .trimIndent()),
            )
            .iterator()

    @JvmStatic
    fun getWwMoves() =
        listOf(
                Arguments.of(
                    0,
                    ' ',
                    """
                        ##############
                        ##......##..##
                        ##..........##
                        ##....[][]@.##
                        ##....[]....##
                        ##..........##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    1,
                    '<',
                    """
                        ##############
                        ##......##..##
                        ##..........##
                        ##...[][]@..##
                        ##....[]....##
                        ##..........##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    2,
                    'v',
                    """
                        ##############
                        ##......##..##
                        ##..........##
                        ##...[][]...##
                        ##....[].@..##
                        ##..........##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    3,
                    'v',
                    """
                        ##############
                        ##......##..##
                        ##..........##
                        ##...[][]...##
                        ##....[]....##
                        ##.......@..##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    4,
                    '<',
                    """
                        ##############
                        ##......##..##
                        ##..........##
                        ##...[][]...##
                        ##....[]....##
                        ##......@...##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    5,
                    '<',
                    """
                        ##############
                        ##......##..##
                        ##..........##
                        ##...[][]...##
                        ##....[]....##
                        ##.....@....##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    6,
                    '^',
                    """
                        ##############
                        ##......##..##
                        ##...[][]...##
                        ##....[]....##
                        ##.....@....##
                        ##..........##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    7,
                    '^',
                    """
                        ##############
                        ##......##..##
                        ##...[][]...##
                        ##....[]....##
                        ##.....@....##
                        ##..........##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    8,
                    '<',
                    """
                        ##############
                        ##......##..##
                        ##...[][]...##
                        ##....[]....##
                        ##....@.....##
                        ##..........##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    9,
                    '<',
                    """
                        ##############
                        ##......##..##
                        ##...[][]...##
                        ##....[]....##
                        ##...@......##
                        ##..........##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    10,
                    '^',
                    """
                        ##############
                        ##......##..##
                        ##...[][]...##
                        ##...@[]....##
                        ##..........##
                        ##..........##
                        ##############
                    """
                        .trimIndent()),
                Arguments.of(
                    11,
                    '^',
                    """
                        ##############
                        ##...[].##..##
                        ##...@.[]...##
                        ##....[]....##
                        ##..........##
                        ##..........##
                        ##############
                    """
                        .trimIndent()),
            )
            .iterator()
  }
}
