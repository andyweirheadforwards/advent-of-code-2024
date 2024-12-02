import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*

class Day1Test {

    val exampleIdString = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent()

    val exampleIdPairList = listOf(
        IdPair(1, 3),
        IdPair(2, 3),
        IdPair(3, 3),
        IdPair(3, 4),
        IdPair(3, 5),
        IdPair(4, 9),
    )

    companion object {
        @JvmStatic
        fun getPairs() = listOf(
            Arguments.of(IdPair(1, 3), 2),
            Arguments.of(IdPair(2, 3), 1),
            Arguments.of(IdPair(3, 3), 0),
            Arguments.of(IdPair(3, 4), 1),
            Arguments.of(IdPair(3, 5), 2),
            Arguments.of(IdPair(4, 9), 5),
        ).asIterable()
    }

    @ParameterizedTest(name = "pair {0} should have distance apart {1}")
    @MethodSource("getPairs")
    fun `It should calculate distance between values in pair`(idPair: IdPair, expected: Int) {
        assertEquals(expected, idPair.distanceApart())
    }

    @Test
    fun `It should calculate total distance between two lists`() {
        val idPairList = exampleIdPairList
        val expected = 11

        assertEquals(expected, idPairList.totalDistance())
    }

    @Test
    fun `It should crate an IdPairList`() {

        val a = listOf(3, 4, 2, 1, 3, 3)
        val b = listOf(4, 3, 5, 3, 9, 3)

        val expected: IdPairList = exampleIdPairList

        assertEquals(expected, createIdPairList(a, b))
    }

    @Test
    fun `It should create IdPairList from string`() {
        val input: IdPairListString = exampleIdString
        val expected: IdPairList = exampleIdPairList

        assertEquals(expected, input.toIdPairList())
    }

    @Test
    fun `It should calculate similarity score`() {
        val idPairList: IdPairList = exampleIdPairList

        assertEquals(31, idPairList.similarityScore())
    }
}