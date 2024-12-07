import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Day05Test {

  private val rules: RuleList =
      listOf(
          Rule(47, 53),
          Rule(97, 13),
          Rule(97, 61),
          Rule(97, 47),
          Rule(75, 29),
          Rule(61, 13),
          Rule(75, 53),
          Rule(29, 13),
          Rule(97, 29),
          Rule(53, 29),
          Rule(61, 53),
          Rule(97, 53),
          Rule(61, 29),
          Rule(47, 13),
          Rule(75, 47),
          Rule(97, 75),
          Rule(47, 61),
          Rule(75, 61),
          Rule(47, 29),
          Rule(75, 13),
          Rule(53, 13),
      )

  @Test
  fun `It should convert RuleListString to RuleList`() {
    val input: RuleListString = rules.joinToString("\n") { "${it.first}|${it.second}" }
    val expected: RuleList = rules

    assertEquals(expected, input.toRuleList())
  }

  @ParameterizedTest(name = "{0} is correct? {1}")
  @MethodSource("getExampleUpdates")
  fun `It should match correct updates`(update: ManualUpdate, isCorrect: Boolean) {
    assertEquals(isCorrect, update.isCorrect(rules))
  }

  @Test
  fun `It should filter correct updates`() {
    val input: ManualUpdatesString =
        """
            5, 47, 61, 53, 29
            97, 61, 53, 29, 13
            75, 29, 13
            75, 97, 47, 61, 53
            61, 13, 29
            97, 13, 75, 29, 47
        """
            .trimIndent()

    val expected =
        """
            5, 47, 61, 53, 29
            97, 61, 53, 29, 13
            75, 29, 13
        """
            .trimIndent()
            .toManualUpdatesList()

    assertEquals(expected, input.toManualUpdatesList().filterCorrectUpdates(rules))
  }

  @Test
  fun `It should add the middle pages`() {
    val input: ManualUpdatesString =
        """
            75,47,61,53,29
            97,61,53,29,13
            75,29,13
        """
            .trimIndent()

    val expected = 143

    val sumOfMiddlePages = input.toManualUpdatesList().sumOfMiddlePages()

    assertEquals(expected, sumOfMiddlePages)
  }

  @Test
  fun `It should fix incorrect updates`() {
    val input =
        """
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
        """
            .trimIndent()

    val expected =
        """
            97,75,47,61,53
            61,29,13
            97,75,47,29,13
        """
            .trimIndent()
            .toManualUpdatesList()

    assertEquals(
        expected, input.toManualUpdatesList().filterIncorrectUpdates(rules).map { it.fix(rules) })
  }

  companion object {

    @JvmStatic
    fun getExampleUpdates() =
        listOf(
                Arguments.of(listOf(5, 47, 61, 53, 29), true),
                Arguments.of(listOf(97, 61, 53, 29, 13), true),
                Arguments.of(listOf(75, 29, 13), true),
                Arguments.of(listOf(75, 97, 47, 61, 53), false),
                Arguments.of(listOf(61, 13, 29), false),
                Arguments.of(listOf(97, 13, 75, 29, 47), false),
            )
            .asIterable()
  }
}
