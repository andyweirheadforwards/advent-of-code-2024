import Utils.dijkstra
import Utils.dijkstraAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DijkstraTest {

    @Test
    fun findShortestPath() {
        val findNeighbours: (Int) -> List<Int> = { node ->
            when (node) {
                1 -> listOf(2, 3)
                2 -> listOf(4)
                3 -> listOf(4)
                4 -> listOf()
                else -> listOf()
            }
        }
        val result = dijkstra(1, 4, findNeighbours)
        assertEquals(listOf(1, 2, 4), result)
    }

    @Test
    fun findAllPaths() {
        val findNeighbours: (Int) -> List<Int> = { node ->
            when (node) {
                1 -> listOf(2, 3)
                2 -> listOf(4)
                3 -> listOf(4)
                4 -> listOf()
                else -> listOf()
            }
        }
        val result = dijkstraAll(1, 4, findNeighbours)
        assertTrue(result.contains(listOf(1, 2, 4)))
        assertTrue(result.contains(listOf(1, 3, 4)))
    }

    @Test
    fun noPathExists() {
        val findNeighbours: (Int) -> List<Int> = { node ->
            when (node) {
                1 -> listOf(2)
                2 -> listOf()
                else -> listOf()
            }
        }
        val result = dijkstra(1, 3, findNeighbours)
        assertNull(result)
    }

    @Test
    fun startEqualsEnd() {
        val findNeighbours: (Int) -> List<Int> = { node -> listOf() }
        val result = dijkstra(1, 1, findNeighbours)
        assertEquals(listOf(1), result)
    }

    @Test
    fun singleNodeGraph() {
        val findNeighbours: (Int) -> List<Int> = { node -> listOf() }
        val result = dijkstra(1, 1, findNeighbours)
        assertEquals(listOf(1), result)
    }
}