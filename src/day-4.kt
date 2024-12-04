const val WORD = "XMAS"

fun main() {
    val input: Grid = fetchDataForDay(4)

    val wordCount = input.searchFor(WORD)

    println("How many times does XMAS appear? $wordCount")
}


enum class Direction {
    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
}

val thing = Direction.entries.toList()

typealias Grid = String

fun Grid.searchFor(word: String, direction: Direction? = null): Int {

    if (direction == null) return Direction.entries.sumOf { searchFor(word, it) }

    return when (direction) {
        Direction.NORTH -> this.rotatedCW()
        Direction.NORTHEAST -> this.slopeRight().rotatedCW()
        Direction.EAST -> this
        Direction.SOUTHEAST -> this.slopeLeft().rotatedCCW()
        Direction.SOUTH -> this.rotatedCCW()
        Direction.SOUTHWEST -> this.slopeRight().rotatedCCW()
        Direction.WEST -> reverseHorizontal()
        Direction.NORTHWEST -> this.slopeLeft().rotatedCW()
    }.let { "(XMAS)".toRegex().findAll(it) }.count()
}

fun Grid.reverseHorizontal() = this.lines().joinToString("\n") { it.reversed() }

fun Grid.reverseVertical(): Grid = this.lines().reversed().joinToString("\n")

fun Grid.rotatedCW(): Grid {
    val rotated = mutableListOf<MutableList<Char>>()
    val lines = this.trim().lines().map { it.trim() }

    repeat(lines.first().length) { rotated.add(mutableListOf<Char>()) }

    lines.reversed().forEach { row ->
        row.forEachIndexed { index, col -> rotated[index].add(col) }
    }

    return rotated.joinToString("\n") { it.joinToString("") }
}

fun Grid.rotatedCCW(): Grid = this.rotatedCW().reverseHorizontal().reverseVertical()

fun Grid.slopeRight(): Grid = this.trim().lines()
    .mapIndexed { index, line -> "${".".repeat(index)}${line.trim()}${".".repeat(line.lastIndex - index)}" }
    .joinToString("\n")

fun Grid.slopeLeft(): Grid = this.reverseHorizontal().slopeRight().reverseHorizontal()