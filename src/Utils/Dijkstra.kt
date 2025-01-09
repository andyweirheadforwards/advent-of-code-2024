package Utils

import java.util.PriorityQueue

class Dijkstra<T>(
    private val start: T,
    private val end: T,
    private val returnAll: Boolean,
    private val findNeighbours: (T) -> List<T>,
    private val calculateScoreForNeighbour: (T, T) -> Int = { _, _ -> 1 }
) {
    private val distances = mutableMapOf<T, Int>().withDefault { Int.MAX_VALUE }
    private val previousNodes = mutableMapOf<T, MutableList<T>>()
    private val priorityQueue = PriorityQueue(compareBy<Pair<T, Int>> { it.second })

    init {
        distances[start] = 0
        priorityQueue.add(start to 0)
    }

    private fun generateSinglePath(): List<List<T>> {
        val path = mutableListOf<T>()
        var node: T? = end
        while (node != null) {
            path.add(0, node)
            node = previousNodes[node]?.firstOrNull()
        }
        return listOf(path)
    }

    private fun generateAllPaths(): List<List<T>> {
        val paths = mutableListOf<List<T>>()
        val stack = mutableListOf(listOf(end))
        while (stack.isNotEmpty()) {
            val path = stack.removeAt(stack.lastIndex)
            val lastNode = path.first()
            if (lastNode == start) {
                paths.add(path)
            } else {
                previousNodes[lastNode]?.forEach { prev ->
                    stack.add(listOf(prev) + path)
                }
            }
        }
        return paths
    }

    private fun updateNeighbors(current: T, currentDistance: Int) {
        for (neighbor in findNeighbours(current)) {
            val newDistance = currentDistance + calculateScoreForNeighbour(current, neighbor)
            if (newDistance < distances.getValue(neighbor)) {
                distances[neighbor] = newDistance
                previousNodes[neighbor] = mutableListOf(current)
                priorityQueue.add(neighbor to newDistance)
            } else if (newDistance == distances.getValue(neighbor)) {
                previousNodes[neighbor]?.add(current)
            }
        }
    }

    fun findPaths(): List<List<T>> {
        while (priorityQueue.isNotEmpty()) {
            val (current, currentDistance) = priorityQueue.poll()

            if (current == end) {
                return if (!returnAll) {
                    generateSinglePath()
                } else {
                    generateAllPaths()
                }
            }

            updateNeighbors(current, currentDistance)
        }
        return emptyList()
    }
}