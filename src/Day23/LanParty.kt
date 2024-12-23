package Day23

private const val POSSIBLE_HISTORIAN = 't'

class LanParty(val nodes: List<ComputerNode>) {
    companion object {
        operator fun invoke(input: String): LanParty {
            val nodes = mutableMapOf<NodeName, MutableSet<NodeName>>()

            input.trim().lines().map { it.split("-") }
                .forEach { (one, two) ->
                    nodes.computeIfAbsent(one) { mutableSetOf() }.add(two)
                    nodes.computeIfAbsent(two) { mutableSetOf() }.add(one)
                }
            return LanParty(nodes.map { ComputerNode(it.key, it.value.sorted()) }.sortedBy { it.name })
        }
    }

    private val possibleHistorianNodes = nodes.filter { it.name.startsWith(POSSIBLE_HISTORIAN) }

    fun solvePartOne() = findNetworksOfThreeNodes().count()
    fun solvePartTwo() = findPassword()

    fun findNetworksOfThreeNodes(): List<List<String>> {
        val networks = mutableSetOf<Triple<NodeName, NodeName, NodeName>>()

        possibleHistorianNodes.forEach { node ->
            node.neighbours.forEach { neighbour ->
                nodes.filter { it.neighbours.contains(node.name) && it.neighbours.contains(neighbour) }.forEach {
                    val (one, two, three) = listOf(node.name, neighbour, it.name).sorted()
                    networks.add(Triple(one, two, three))
                }
            }
        }

        return networks.map { it.toList() }
            .sortedBy { it.joinToString("") }
    }

    fun findNetworksOfInterconnectedNodes(): List<List<String>> {
        val lanParties: List<MutableList<NodeName>> = nodes.map { mutableListOf(it.name) }

        nodes.forEach { node ->
            lanParties.forEach { lanParty ->
                if (lanParty.intersect(node.neighbours).size == lanParty.size) lanParty.add(node.name)
            }
        }

        val result = lanParties.map { it.sorted() }.toSet()
            .sortedBy { it.joinToString("") }
            .sortedByDescending { it.size }

        return result
    }

    fun findPassword() = findNetworksOfInterconnectedNodes()
        .first()
        .joinToString(",")
}
