import kotlinx.coroutines.runBlocking
import java.awt.Point
import kotlin.time.measureTime

fun main(): Unit = runBlocking {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day12")

          val garden = Garden(input)
          garden.walk()
          println(
              "What is the total price of fencing all regions on your map? ${garden.totalPrice}")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias PlantType = Char

data class Plot(val point: Point, val type: PlantType, val neighbours: Int) {
  override fun toString(): String = "${point.x},${point.y}"
}

data class Region(val type: PlantType, val plots: List<Plot>) {

  val area: Int = plots.size

  val perimeter: Int = plots.map { 4 - it.neighbours }.sumOf { it }

  val price: Int
    get() = area * perimeter
}

class Garden(val grid: Grid) {
  companion object {
    operator fun invoke(string: String): Garden = Garden(string.grid)
  }

  private val visitedPoints: MutableSet<String> = mutableSetOf()

  val mutableRegions: MutableList<Region> = mutableListOf()
  val regions: List<Region>
    get() = mutableRegions.toList()

  val totalPrice: Int
    get() = regions.sumOf { it.price }

  fun getRegionPoints(point: Point, type: PlantType): List<Point> {
    if (!visitedPoints.add(point.toString())) return listOf()

    return listOf(point) +
        grid
            .getNeighbours(point)
            .filter { grid.getSymbolAt(it) == type }
            .filter { !visitedPoints.contains(it.toString()) }
            .flatMap { getRegionPoints(it, type) }
  }

  fun getNeighboursCount(point: Point, type: PlantType) =
      grid.getNeighbours(point).filter { grid.getSymbolAt(it) == type }.size

  fun walk() {
    grid.forEachIndexed { y, row ->
      row.forEachIndexed { x, type ->
        val point = Point(x, y)
        val type = grid.getSymbolAt(point)
        getRegionPoints(point, type)
            .map { Plot(it, type, getNeighboursCount(it, type)) }
            .let { if (it.isNotEmpty()) mutableRegions.add(Region(type, it)) }
      }
    }
  }
}
