import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {

  @Test
  fun `It should find all regions`() {
    val expected = listOf<PlantType>('A', 'B', 'C', 'D', 'E')
    val input =
        """
            AAAA
            BBCD
            BBCC
            EEEC
        """
            .trimIndent()

    val garden = Garden(input)
    garden.walk()

    assertEquals(expected, garden.regions.map { it.type })
  }

  @ParameterizedTest(name = "It should generate type: {1}, area: {2}, perimeter: {3}, price: {4}")
  @MethodSource("getRegions")
  fun `It should generate regions`(
      index: Int,
      type: PlantType,
      area: Int,
      perimeter: Int,
      price: Int
  ) {
    val garden = Garden(testInput)
    garden.walk()

    val region = garden.regions[index]

    assertEquals(type, region.type)
    assertEquals(area, region.area)
    assertEquals(perimeter, region.perimeter)
    assertEquals(price, region.price)
  }

  @ParameterizedTest(name = "It should generate type: {1}, sides: {2}, price: {3}")
  @MethodSource("getBulkDiscountRegions")
  fun `It should generate regions with discount`(
      index: Int,
      type: PlantType,
      sides: Int,
      discountPrice: Int
  ) {
    val garden = Garden(testInput)
    garden.walk()

    val region = garden.regions[index]

    assertEquals(type, region.type)
    assertEquals(sides, region.sides)
    assertEquals(discountPrice, region.discountPrice)
  }

  @Test
  fun `It should get total price`() {
    val expected = 1930
    val garden = Garden(testInput)
    garden.walk()

    assertEquals(expected, garden.totalPrice)
  }

  @Test
  fun `It should calculate a regions sides`() {
    val expected = 8
    val input =
        """
            .....
            .AA..
            ..AA.
            .....
        """
            .trimIndent()

    val garden = Garden(input)
    garden.walk()
    val region = garden.regions.first { it.type == 'A' }

    assertEquals(expected, region.sides)
  }

  @Test
  fun `It should get numbers of sides`() {
    val input =
        """
            AAAA
            BBCD
            BBCC
            EEEC
        """
            .trimIndent()

    val garden = Garden(input)
    garden.walk()

    assertEquals(4, garden.regions.first { it.type == 'A' }.sides)
    assertEquals(4, garden.regions.first { it.type == 'B' }.sides)
    assertEquals(8, garden.regions.first { it.type == 'C' }.sides)
    assertEquals(4, garden.regions.first { it.type == 'D' }.sides)
    assertEquals(4, garden.regions.first { it.type == 'E' }.sides)
  }

  @ParameterizedTest(name = "It should be {1}")
  @MethodSource("getBulkDiscountExamples")
  fun `It should get bulk discount total price`(input: GridString, expected: Int) {
    val garden = Garden(input)
    garden.walk()

    assertEquals(expected, garden.discountTotalPrice)
  }

  companion object {
    val testInput =
        """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
        """
            .trimIndent()

    @JvmStatic
    fun getRegions() =
        listOf(
                // region: type, area, perimeter, price
                Arguments.of(0, 'R', 12, 18, 216),
                Arguments.of(1, 'I', 4, 8, 32),
                Arguments.of(2, 'C', 14, 28, 392),
                Arguments.of(3, 'F', 10, 18, 180),
                Arguments.of(4, 'V', 13, 20, 260),
                Arguments.of(5, 'J', 11, 20, 220),
                Arguments.of(6, 'C', 1, 4, 4),
                Arguments.of(7, 'E', 13, 18, 234),
                Arguments.of(8, 'I', 14, 22, 308),
                Arguments.of(9, 'M', 5, 12, 60),
                Arguments.of(10, 'S', 3, 8, 24),
            )
            .iterator()

    @JvmStatic
    fun getBulkDiscountRegions() =
        listOf(
                // region: type, area, perimeter, price
                Arguments.of(0, 'R', 10, 120),
                Arguments.of(1, 'I', 4, 16),
                Arguments.of(2, 'C', 22, 308),
                Arguments.of(3, 'F', 12, 120),
                Arguments.of(4, 'V', 10, 130),
                Arguments.of(5, 'J', 12, 132),
                Arguments.of(6, 'C', 4, 4),
                Arguments.of(7, 'E', 8, 104),
                Arguments.of(8, 'I', 16, 224),
                Arguments.of(9, 'M', 6, 30),
                Arguments.of(10, 'S', 6, 18),
            )
            .iterator()

    @JvmStatic
    fun getBulkDiscountExamples() =
        listOf(
                Arguments.of(
                    """
                        AAAA
                        BBCD
                        BBCC
                        EEEC
                    """
                        .trimIndent(),
                    80),
                Arguments.of(
                    """
                        OOOOO
                        OXOXO
                        OOOOO
                        OXOXO
                        OOOOO
                    """
                        .trimIndent(),
                    436),
                Arguments.of(
                    """
                        EEEEE
                        EXXXX
                        EEEEE
                        EXXXX
                        EEEEE
                    """
                        .trimIndent(),
                    236),
                Arguments.of(
                    """
                        AAAAAA
                        AAABBA
                        AAABBA
                        ABBAAA
                        ABBAAA
                        AAAAAA
                    """
                        .trimIndent(),
                    (4 * 4) + (4 * 4) + ((36 - 8) * 12)),
                Arguments.of(testInput, 1206))
            .iterator()
  }
}
