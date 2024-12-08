import java.awt.Point
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day08")

          val countOne = AntennaMap(input).antinodesOne.size
          println(
              "How many unique locations within the bounds of the map contain an antinode? $countOne")

          val countTwo = AntennaMap(input).antinodesTwo.size
          println(
              "How many unique locations within the bounds of the map contain an antinode? $countTwo")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias Freq = Char

typealias Frequencies = CharArray

typealias AntennaLocations = Map<Freq, List<Point>>

class AntennaMap(val grid: Grid) {

  companion object {
    operator fun invoke(string: GridString) = AntennaMap(string.grid)

    val frequencyRegex = "(\\w)".toRegex()
  }

  val frequencies: Frequencies by lazy {
    frequencyRegex.findAll(grid.string).map { it.value.first() }.toSet().toCharArray()
  }

  val antennaLocations: AntennaLocations by lazy {
    frequencies.associateWith { freq ->
      grid
          .mapIndexed { y, line ->
            line.joinToString("").mapIndexedNotNull { x, symbol ->
              if (symbol == freq) Point(x, y) else null
            }
          }
          .flatten()
    }
  }

  val antinodesOne: Set<Point>
    get() = computeAntinodes { one, two ->
      val diff = two.diff(one)
      listOf(one + diff)
    }

  val antinodesTwo: Set<Point>
    get() = computeAntinodes { location, other ->
      val diff = other.diff(location)
      generateSequence(location) { p -> p + diff } // Expansion logic
          .takeWhile(grid::isValidPoint)
          .toList()
    }

  fun symbolAt(point: Point): Char = grid.getSymbolAt(point)

  fun setSymbolAtPoint(point: Point, symbol: Char) = grid.setSymbolAt(point, symbol)

  override fun toString(): String = grid.string

  private fun computeAntinodes(compute: (Point, Point) -> List<Point>): Set<Point> =
      antennaLocations.values
          .flatMap { locations ->
            locations.flatMap { location ->
              locations.filterNot { location == it }.flatMap { compute(location, it) }
            }
          }
          .filter(grid::isValidPoint)
          .toSet()
}
