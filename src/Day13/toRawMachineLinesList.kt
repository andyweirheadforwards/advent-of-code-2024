package Day13

typealias RawMachineString = String

fun RawMachineString.toRawMachineLinesList(): List<RawMachineLines> =
    lines().windowed(3, 4, partialWindows = false)

fun RawMachineString.costOfAllPrizesOne(): Long =
    toRawMachineLinesList().asSequence().mapNotNull { it.toClawMachineOne().cost }.sum()

fun RawMachineString.costOfAllPrizesTwo(): Long =
    toRawMachineLinesList().sumOf { it.toClawMachineTwo().cost ?: 0L }
