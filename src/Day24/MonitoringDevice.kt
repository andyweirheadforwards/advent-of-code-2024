package Day24

class MonitoringDevice(private val wires: Map<String, Wire>, private val gates: List<LogicGate>) {
    companion object {
        operator fun invoke(input: String): MonitoringDevice = MonitoringDevice(input.getWires(), input.getLogicGates())
    }

    val xValue: String = wires.filter { it.key.startsWith('x') }
        .values.sortedByDescending { it.name }
        .map { it.value }.joinToString("")
    val yValue: String = wires.filter { it.key.startsWith('y') }
        .values.sortedByDescending { it.name }
        .map { it.value }.joinToString("")

    val zValue: String = (xValue.toLong(2) + yValue.toLong(2)).toString(2).padStart(xValue.length + 1, '0')

    fun getOutputBinary(): String {
        val queue = gates.toMutableList()
        val wiresCopy = wires.toMutableMap()

        fun gateValue(gate: LogicGate): Int {
            val (one, two) = gate.input
            val wireOne = wiresCopy[one]!!
            val wireTwo = wiresCopy[two]!!
            return when (gate.type) {
                GateType.AND -> wireOne and wireTwo
                GateType.OR -> wireOne or wireTwo
                GateType.XOR -> wireOne xor wireTwo
            }
        }

        while (queue.isNotEmpty()) {
            val next =
                queue.first { gate -> wiresCopy.filter { gate.input.toList().contains(it.value.name) }.size == 2 }
            wiresCopy.getOrPut(next.out) { Wire(next.out) }.value = gateValue(next)
            queue.remove(next)
        }

        return wiresCopy.filter { it.key.startsWith('z') }.values
            .sortedByDescending { it.name }
            .joinToString("") { "${it.value}" }
            .padStart(xValue.length + 1, '0')
    }

    fun getOutputDecimal() = getOutputBinary().toLong(2)

    override fun toString(): String =
        wires.values.joinToString("\n") { "$it" } + "\n\n" + gates.joinToString("\n") { "$it" }
}
