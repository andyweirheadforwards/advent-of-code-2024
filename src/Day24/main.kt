package Day24

import PROFILE_REPEAT
import kotlinx.coroutines.*
import readInput
import kotlin.time.measureTime

fun main() {
    measureTime {
        repeat(PROFILE_REPEAT) {
            val input = readInput("Day24")

            val monitoringDevice = MonitoringDevice(input)
            val solutionOne = monitoringDevice.getOutputDecimal()

            println("What decimal number does it output on the wires starting with z? $solutionOne")

            val solutionTwo = runBlocking { findSolutionTwo(input) }
            println(solutionTwo)
        }
    }
        .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

val dispatcher = newFixedThreadPoolContext(4, "IO") // Limit to 4 threads

suspend fun findSolutionTwo(input: String, pairCount: Int = 4): String? = withContext(dispatcher) {
    val wires = input.getWires()
    val gates = input.getLogicGates()
    val combinations = generateCombinations(gates, pairCount)

    val deferredResults: List<Deferred<String?>> = combinations.map { combination ->
        async {
            val gatesCopy = gates.toMutableSet()
            combination.forEach { pair ->
                val (one, two) = pair
                val (swapOne, swapTwo) = swapGateOutputs(pair)
                gatesCopy.remove(one)
                gatesCopy.remove(two)
                gatesCopy.add(swapOne)
                gatesCopy.add(swapTwo)
            }

            val monitoringDevice = MonitoringDevice(wires, gatesCopy.toList())

            val output = monitoringDevice.getOutputBinary()
            val zValue = monitoringDevice.zValue
            if (output == zValue) {
                return@async combination.flatMap { it.toList() }.map { it.out }.sorted().joinToString(",")
            }

            null
        }
    }.toList()

    deferredResults.awaitAll().firstOrNull { it != null }
}

fun generateCombinations(gates: List<LogicGate>, pairCount: Int): Sequence<Set<Pair<LogicGate, LogicGate>>> = sequence {
    val n = gates.size
    val indices = IntArray(pairCount * 2) { it }

    while (indices[0] < n - pairCount * 2 + 1) {
        val combination = mutableSetOf<Pair<LogicGate, LogicGate>>()
        for (i in 0 until pairCount) {
            val pair = listOf(gates[indices[i * 2]], gates[indices[i * 2 + 1]]).sortedBy { it.out }
            combination.add(pair[0] to pair[1])
        }
        yield(combination)

        var t = pairCount * 2 - 1
        while (t != 0 && indices[t] == n - pairCount * 2 + t) {
            t--
        }
        indices[t]++
        for (i in t + 1 until pairCount * 2) {
            indices[i] = indices[i - 1] + 1
        }
    }
}

fun <T> Set<T>.combinations(k: Int): Sequence<List<T>> = sequence {
    if (k == 0) yield(emptyList())
    else if (this@combinations.isNotEmpty()) {
        val head = this@combinations.first()
        val tail = this@combinations.drop(1).toSet()
        yieldAll(tail.combinations(k - 1).map { listOf(head) + it })
        yieldAll(tail.combinations(k))
    }
}

fun swapGateOutputs(gatePair: Pair<LogicGate, LogicGate>): Pair<LogicGate, LogicGate> {
    val (gate1, gate2) = gatePair
    return gate1.copy(out = gate2.out) to gate2.copy(out = gate1.out)
}

fun String.getWires(): Map<String, Wire> = this.trim().lines().takeWhile { it.isNotBlank() }.associate { line ->
    val (name, value) = line.split(":").map { it.trim() }
    name to Wire(name, value.toInt())
} // Use toMap() instead of associate for better performance

fun String.getLogicGates(): List<LogicGate> = this.trim().lines().takeLastWhile { it.isNotBlank() }.map { line ->

    val (_, one, type, two, out) = "([a-z\\d]{3}) (AND|OR|XOR) ([a-z\\d]{3}) -> ([a-z\\d]{3})".toRegex()
        .find(line)!!.groups.map { it!!.value }
    LogicGate(one to two, out, GateType.valueOf(type))
}