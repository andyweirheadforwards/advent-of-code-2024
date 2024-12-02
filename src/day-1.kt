import java.io.File
import kotlin.math.abs

fun main() {
    val input: IdPairListString = File("./data/day-1.txt").readText().trim()

    val idPairList = input.toIdPairList()

    println("Total distance between: ${idPairList.totalDistance()}")
    println("Similarity score:       ${idPairList.similarityScore()}")
}

typealias IdPair = Pair<Int, Int>
typealias IdPairList = List<IdPair>
typealias IdPairListString = String

fun IdPair.distanceApart() = abs(this.first - this.second)

fun createIdPairList(a: List<Int>, b: List<Int>): IdPairList = a.sorted().zip(b.sorted())

fun IdPairList.totalDistance() = this.sumOf { it.distanceApart() }

fun IdPairList.similarityScore() = this.sumOf { it.first * this.count { pair -> pair.second == it.first } }

fun IdPairListString.toIdPairList(): IdPairList {
    val a = mutableListOf<Int>()
    val b = mutableListOf<Int>()

    this.split("\n").forEach { line ->
        val values = line.trim().split("\\s".toRegex())
        a.add(values.first().toInt())
        b.add(values.last().toInt())
    }

    return createIdPairList(a, b)
}