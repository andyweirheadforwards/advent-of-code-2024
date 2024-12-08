const val WORD = "XMAS"
const val X_MAS = "M S\n A \nM S"

fun main() {
  val input: GridString = readInput("Day04")

  println("How many times does XMAS appear?     ${input.searchForWord(WORD)}")
  println("How many times does an X-MAS appear? ${input.searchForXMas()}")
}

enum class Direction {
  NORTH,
  NORTHEAST,
  EAST,
  SOUTHEAST,
  SOUTH,
  SOUTHWEST,
  WEST,
  NORTHWEST
}

fun GridString.searchForWord(word: String, direction: Direction? = null): Int {

  if (direction == null) return Direction.entries.sumOf { searchForWord(word, it) }

  return when (direction) {
        Direction.NORTH -> this.rotatedCW()
        Direction.NORTHEAST -> this.slopeRight().rotatedCW()
        Direction.EAST -> this
        Direction.SOUTHEAST -> this.slopeLeft().rotatedCCW()
        Direction.SOUTH -> this.rotatedCCW()
        Direction.SOUTHWEST -> this.slopeRight().rotatedCCW()
        Direction.WEST -> reverseHorizontal()
        Direction.NORTHWEST -> this.slopeLeft().rotatedCW()
      }
      .let { "($WORD)".toRegex().findAll(it) }
      .count()
}

fun GridString.searchForXMas(): Int {
  val grid: List<String> = lines()
  var count = 0

  grid.forEachIndexed { rowIndex, row ->
    if (rowIndex < 1 || rowIndex > grid.lastIndex - 1) return@forEachIndexed
    row.forEachIndexed { colIndex, col ->
      if (colIndex < 1 || colIndex > row.lastIndex - 1) return@forEachIndexed
      if (col != 'A') return@forEachIndexed

      val searchGridString =
          grid.subList(rowIndex - 1, rowIndex + 2).joinToString("\n") { row ->
            row.substring(colIndex - 1, colIndex + 2)
          }

      if (searchGridString.matchesXMas()) count++
    }
  }

  return count
}

fun GridString.reverseHorizontal() = lines().joinToString("\n") { it.reversed() }

fun GridString.reverseVertical(): GridString = lines().reversed().joinToString("\n")

fun GridString.rotatedCW(): GridString {
  val rotated = mutableListOf<MutableList<Char>>()
  val lines = trim().lines().map { it.trim() }

  repeat(lines.first().length) { rotated.add(mutableListOf<Char>()) }

  lines.reversed().forEach { row -> row.forEachIndexed { index, col -> rotated[index].add(col) } }

  return rotated.joinToString("\n") { it.joinToString("") }
}

fun GridString.rotatedCCW(): GridString = rotatedCW().reverseHorizontal().reverseVertical()

fun GridString.slopeRight(): GridString =
    trim()
        .lines()
        .mapIndexed { index, line ->
          "${".".repeat(index)}${line.trim()}${".".repeat(line.lastIndex - index)}"
        }
        .joinToString("\n")

fun GridString.slopeLeft(): GridString = reverseHorizontal().slopeRight().reverseHorizontal()

fun GridString.matchesXMas(): Boolean {

  fun doesMatch(grid: GridString): Boolean {
    if (!"M.S".toRegex().matches(grid.lines()[0])) return false
    if (!".A.".toRegex().matches(grid.lines()[1])) return false
    if (!"M.S".toRegex().matches(grid.lines()[2])) return false

    return true
  }

  (0..3).forEach {
    var grid = this
    repeat(it) { grid = grid.rotatedCW() }
    if (doesMatch(grid)) return true
  }

  return false
}
