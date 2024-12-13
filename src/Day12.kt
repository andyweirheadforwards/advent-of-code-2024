import java.awt.Point
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day12")

          val garden = Garden(input)
          garden.walk()
          val totalPrice = garden.totalPrice
          println("What is the total price of fencing all regions on your map?     $totalPrice")

          val discountTotalPrice = garden.discountTotalPrice
          println(
              "What is the new total price of fencing all regions on your map? $discountTotalPrice")
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

  val sides: Int
    get() {
      val points = plots.map { it.point }

      val startX = points.minOf { it.x }
      val endX = points.maxOf { it.x }
      val startY = points.minOf { it.y }
      val endY = points.maxOf { it.y }

      // Tops
      val tops =
          (startY..endY)
              .map { y ->
                (startX..endX)
                    .map { x ->
                      val point = Point(x, y)
                      when {
                        points.firstOrNull { it.toString() == point.toString() } == null -> 'O'
                        points.firstOrNull {
                          it.toString() == Point(point.x, point.y - 1).toString()
                        } != null -> 'O'
                        else -> 'X'
                      }
                    }
                    .joinToString("")
                    .split("O+".toRegex())
                    .filter { !it.isBlank() }
                    .size
              }
              .sumOf { it }

      // Bottoms
      val bottoms =
          (startY..endY)
              .map { y ->
                (startX..endX)
                    .map { x ->
                      val point = Point(x, y)
                      when {
                        !points.contains(point) -> 'O'
                        points.contains(Point(point.x, point.y + 1)) -> 'O'
                        else -> 'X'
                      }
                    }
                    .joinToString("")
                    .split("O+".toRegex())
                    .filter { !it.isBlank() }
                    .size
              }
              .sumOf { it }

      // Lefts
      val lefts =
          (startX..endX)
              .map { x ->
                (startY..endY)
                    .map { y ->
                      val point = Point(x, y)
                      when {
                        !points.contains(point) -> 'O'
                        points.contains(Point(point.x - 1, point.y)) -> 'O'
                        else -> 'X'
                      }
                    }
                    .joinToString("")
                    .split("O+".toRegex())
                    .filter { !it.isBlank() }
                    .size
              }
              .sumOf { it }

      // Rights
      val rights =
          (startX..endX)
              .map { x ->
                (startY..endY)
                    .map { y ->
                      val point = Point(x, y)
                      when {
                        !points.contains(point) -> 'O'
                        points.contains(Point(point.x + 1, point.y)) -> 'O'
                        else -> 'X'
                      }
                    }
                    .joinToString("")
                    .split("O+".toRegex())
                    .filter { !it.isBlank() }
                    .size
              }
              .sumOf { it }

      return tops + bottoms + lefts + rights
    }

  val price: Int
    get() = area * perimeter

  val discountPrice: Int
    get() = area * sides
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

  val discountTotalPrice: Int
    get() = regions.sumOf { it.discountPrice }

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
