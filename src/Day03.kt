import java.io.File

private const val DISABLED = "don't()"
private const val ENABLED = "do()"

fun main() {
    val input: MemoryListString = readInput("Day03")

    val memory = input.memory
    println("What do you get if you add up all of the results of the multiplications?              ${memory.toMulList().calculate()}")
    println("What do you get if you add up all of the results of just the enabled multiplications? ${memory.enabledMemory.toMulList().calculate()}")
}

typealias MemoryListString = String
typealias Memory = String
typealias MulList = List<String>

val MemoryListString.memory: Memory get() = lines().joinToString(" ~ ")

fun MemoryListString.toMulList(): MulList = "mul\\(\\d{1,3},\\d{1,3}\\)".toRegex()
    .findAll(this)
    .map { it.value }.toList()

val Memory.enabledMemory: Memory
    get() {
        var isEnabled = true
        return buildList {
            var remainingData = this@enabledMemory
            while (remainingData.isNotEmpty()) {
                val marker = if (isEnabled) DISABLED else ENABLED
                val index = remainingData.indexOf(marker)
                val found = index != -1

                val segment = when {
                    !found && isEnabled -> remainingData
                    isEnabled -> remainingData.substring(0, index)
                    else -> ""
                }

                add(segment)

                remainingData = remainingData.takeIf { found }?.substring(index + marker.length) ?: ""
                isEnabled = found && !isEnabled
            }
        }.filter { it.isNotBlank() }.joinToString(" ~ ")
    }

fun MulList.calculate(): Int = sumOf { it.extractIntegerPair().let { it.first * it.second } }

fun String.extractIntegerPair(): Pair<Int, Int> {
    val groups = "(\\d{1,3}),(\\d{1,3})".toRegex().find(this)?.groups
    val first = groups?.get(1)?.value?.toInt() ?: 0
    val second = groups?.get(2)?.value?.toInt() ?: 0

    return first to second
}
