import Day22.SecretGenerator
import Day22.calculateSumOfSecrets
import Day22.findHighestScoringSequence
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {
  @Test
  fun `It should mix a value`() {
    val expected = 37L
    val secretNumber = 42
    val value = 15L

    val generator = SecretGenerator(secretNumber)

    assertEquals(expected, generator.mix(value))
  }

  @Test
  fun `It should prune the secret number`() {
    val expected = 16113920L
    val secretNumber = 100000000

    val generator = SecretGenerator(secretNumber)

    assertEquals(expected, generator.prune())
  }

  @ParameterizedTest(name = "It should be {1} after {0} evolutions")
  @MethodSource("getOneTwoThree")
  fun `It should evolve 123`(count: Int, expected: Int) {
    val generator = SecretGenerator(123)

    assertEquals(expected.toLong(), generator.evolve(count))
  }

  @ParameterizedTest(name = "It should evolve {0} to {1}")
  @MethodSource("getEvolvedTwoThousandTimes")
  fun `It should evolve secret number 2000 times`(number: Int, expected: Int) {
    val generator = SecretGenerator(number)

    assertEquals(expected.toLong(), generator.evolve(2000))
  }

  @Test
  fun `It should calculate the sum of secrets`() {
    val expected = 37327623L
    val secrets =
        listOf(
            1,
            10,
            100,
            2024,
        )

    val solution = calculateSumOfSecrets(secrets, 2000)

    assertEquals(expected, solution)
  }

  @ParameterizedTest(name = "It should be {0} for secret {1}")
  @MethodSource("getOnesDigits")
  fun `It should get ones digit`(secret: Int, expected: Int) {
    val generator = SecretGenerator(secret)

    assertEquals(expected, generator.price())
  }

  @Test
  fun `It should solve part one`() {
    val expected = 17724064040
    val input = readInput("Day22").lines().map { it.toInt() }
    val solution = calculateSumOfSecrets(input, 2000)

    assertEquals(expected, solution)
  }

  @Test
  fun `It should generate a sequence`() {
    val expected =
        """     123: 3    
15887950: 0 (-3)
16495136: 6 (6)
  527345: 5 (-1)
  704524: 4 (-1)
 1553684: 4 (0)
12683156: 6 (2)
11100544: 4 (-2)
12249484: 4 (0)
 7753432: 2 (-2)"""

    val generator = SecretGenerator(123)

    assertEquals(expected, generator.priceSequenceString(10))
  }

  @Test
  fun `It should get score for secret number repeating sequence`() {
    val expectedSequence = listOf(-1, -1, 0, 2)
    val expectedScore = 6
    val secretNumber = 123
    val generator = SecretGenerator(secretNumber)

    val (sequence, score) =
        generator.getSequenceScoreList(10).entries.first { it.key == expectedSequence }

    assertEquals(expectedScore, score)
    assertEquals(expectedSequence, sequence)
  }

  @Test
  fun `It should get score for secret number list repeating sequence`() {
    val expectedSequence = listOf(-2, 1, -1, 3)
    val expectedScore = 23
    val secretGeneratorList = listOf(1, 2, 3, 2024).map { SecretGenerator(it) }
    val result = findHighestScoringSequence(secretGeneratorList, 2000)
    val (sequence, score) = result.entries.first { it.key == expectedSequence }

    assertEquals(expectedScore, score)
    assertEquals(expectedSequence, sequence)
  }

  @Test
  fun `It should solve part two`() {
    val expected = 1998
    val input = readInput("Day22")
    val secretGeneratorList = input.lines().map { it.trim().toInt() }.map { SecretGenerator(it) }
    val solution = findHighestScoringSequence(secretGeneratorList, 2001).maxOf { it.value }

    assertEquals(expected, solution)
  }

  companion object {
    @JvmStatic
    fun getOneTwoThree() =
        listOf(
                Arguments.of(1, 15887950),
                Arguments.of(2, 16495136),
                Arguments.of(3, 527345),
                Arguments.of(4, 704524),
                Arguments.of(5, 1553684),
                Arguments.of(6, 12683156),
                Arguments.of(7, 11100544),
                Arguments.of(8, 12249484),
                Arguments.of(9, 7753432),
                Arguments.of(10, 5908254),
            )
            .iterator()

    @JvmStatic
    fun getEvolvedTwoThousandTimes() =
        listOf(
                Arguments.of(1, 8685429),
                Arguments.of(10, 4700978),
                Arguments.of(100, 15273692),
                Arguments.of(2024, 8667524),
            )
            .iterator()

    @JvmStatic
    fun getOnesDigits() =
        listOf(
                Arguments.of(123, 3),
                Arguments.of(15887950, 0),
                Arguments.of(16495136, 6),
            )
            .iterator()
  }
}
