import Day25.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day25Test {
    @Test
    fun `It should create a lock`() {
        val expected = "0,5,3,4,3"
        val input = """
            #####
            .####
            .####
            .####
            .#.#.
            .#...
            .....
        """.trimIndent()

        val lock = Lock(input)

        assertEquals(expected, lock.toString())
    }

    @Test
    fun `It should create a key`() {
        val expected = "5,0,2,1,3"
        val input = """
            .....
            #....
            #....
            #...#
            #.#.#
            #.###
            #####
        """.trimIndent()

        val key = Key(input)

        assertEquals(expected, key.toString())
    }


    @Test
    fun `It should be a key`() {
        val input = """
            .....
            #....
            #....
            #...#
            #.#.#
            #.###
            #####
        """.trimIndent().grid

        assertTrue(input.isKey)
    }

    @Test
    fun `It should not be a key`() {
        val input = """
            #####
            .####
            .####
            .####
            .#.#.
            .#...
            .....
        """.trimIndent().grid

        assertFalse(input.isKey)
    }

    @ParameterizedTest(name = "It key: {1} fits lock: {0} - {2}")
    @MethodSource("getKeyLockPairs")
    fun `It should fit lock`(lock: Lock, key: Key, expected: Boolean) {
        assertEquals(expected, key fitsIn lock)
    }

    @Test
    fun `It should solve the puzzle with test input`() {
        val expected = 3
        val puzzle = Puzzle(testInput)

        assertEquals(expected, puzzle.solveOne())
    }

    companion object {
        val testInput = """
            #####
            .####
            .####
            .####
            .#.#.
            .#...
            .....

            #####
            ##.##
            .#.##
            ...##
            ...#.
            ...#.
            .....

            .....
            #....
            #....
            #...#
            #.#.#
            #.###
            #####

            .....
            .....
            #.#..
            ###..
            ###.#
            ###.#
            #####

            .....
            .....
            .....
            #....
            #.#..
            #.#.#
            #####
        """.trimIndent()

        @JvmStatic
        fun getKeyLockPairs() = listOf(
            Arguments.of(
                Lock(
                    """
                        #####
                        .####
                        .####
                        .###.
                        .#.#.
                        .#...
                        .....
                    """.trimIndent()
                ),
                Key(
                    """
                        .....
                        #....
                        #....
                        #...#
                        #.#.#
                        #.###
                        #####
                    """.trimIndent()
                ),
                true
            ),
            Arguments.of(
                Lock(
                    """
                        #####
                        .####
                        .####
                        .###.
                        .#.#.
                        .#...
                        .....
                    """.trimIndent()
                ),
                Key(
                    """
                        .....
                        .....
                        #....
                        #...#
                        #.#.#
                        #.###
                        #####
                    """.trimIndent()
                ),
                true
            ),
            Arguments.of(
                Lock(
                    """
                        #####
                        .####
                        .####
                        .####
                        .#.#.
                        .#...
                        .....
                    """.trimIndent()
                ),
                Key(
                    """
                        .....
                        #....
                        #....
                        #...#
                        #.#.#
                        #.###
                        #####
                    """.trimIndent()
                ),
                false
            ),
            Arguments.of(
                Lock(
                    """
                        #####
                        .####
                        .###.
                        .###.
                        .#.#.
                        .#...
                        .....
                    """.trimIndent()
                ),
                Key(
                    """
                        .....
                        #....
                        #....
                        #.#.#
                        #.#.#
                        #.###
                        #####
                    """.trimIndent()
                ),
                false
            ),
        ).iterator()
    }
}
