import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class IdPairTest {
    @ParameterizedTest
    @MethodSource("getPairs")
    fun `It should calculate distance between values in pair`(idPair: IdPair, expected: Int) {
        assertEquals(expected, idPair.distanceApart())
    }

    @ParameterizedTest
    @MethodSource("getIdPairList")
    fun `It should calculate total distance between two lists`(idPairList: IdPairList, expected: Int) {
        assertEquals(expected, idPairList.totalDistance())
    }

    @Test
    fun `It should crate an IdPairList`() {

        val a = listOf(3, 4, 2, 1, 3, 3,)
        val b = listOf(4, 3, 5, 3, 9, 3,)

        val expected: IdPairList = listOf(
            IdPair(1, 3),
            IdPair(2, 3),
            IdPair(3, 3),
            IdPair(3, 4),
            IdPair(3, 5),
            IdPair(4, 9),
        )

        assertEquals(expected, createIdPairList(a, b))
    }

    @Test
    fun `It should create IdPairList from string`() {
        val input: IdPairListString = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent()

        val expected: IdPairList = listOf(
            IdPair(1, 3),
            IdPair(2, 3),
            IdPair(3, 3),
            IdPair(3, 4),
            IdPair(3, 5),
            IdPair(4, 9),
        )

        assertEquals(expected, input.toIdPairList())
    }

    @Test
    fun`It should calculate similarity score`() {
        val idPairList: IdPairList = listOf(
            IdPair(1, 3),
            IdPair(2, 3),
            IdPair(3, 3),
            IdPair(3, 4),
            IdPair(3, 5),
            IdPair(4, 9),
        )

        assertEquals(31, idPairList.similarityScore())
    }

    companion object {
        @JvmStatic
        fun getPairs() = listOf(
            Arguments.of(
                IdPair(1, 3),
                2
            ), // The smallest number in the left list is 1, and the smallest number in the right list is 3. The distance between them is 2.
            Arguments.of(
                IdPair(2, 3),
                1
            ), // The second-smallest number in the left list is 2, and the second-smallest number in the right list is another 3. The distance between them is 1.
            Arguments.of(
                IdPair(3, 3),
                0
            ), // The third-smallest number in both lists is 3, so the distance between them is 0.
            Arguments.of(IdPair(3, 4), 1), // The next numbers to pair up are 3 and 4, a distance of 1.
            Arguments.of(IdPair(3, 5), 2), // The fifth-smallest numbers in each list are 3 and 5, a distance of 2.
            Arguments.of(
                IdPair(4, 9),
                5
            ), // Finally, the largest number in the left list is 4, while the largest number in the right list is 9; these are a distance 5 apart.
        ).asIterable()

        @JvmStatic
        fun getIdPairList() = listOf(
            Arguments.of(
                listOf(
                    IdPair(1, 3),
                    IdPair(2, 3),
                    IdPair(3, 3),
                    IdPair(3, 4),
                    IdPair(3, 5),
                    IdPair(4, 9),
                ),
                11
            )
        ).asIterable()
    }


}