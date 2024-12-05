import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*

class Day02Test {
    var example: ReportListString = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent()

    companion object {
        @JvmStatic
        fun getReports() = listOf(
            Arguments.of("7 6 4 2 1", "SAFE"),
            Arguments.of("1 2 7 8 9", "UNSAFE"),
            Arguments.of("9 7 6 2 1", "UNSAFE"),
            Arguments.of("1 3 2 4 5", "UNSAFE"),
            Arguments.of("8 6 4 4 1", "UNSAFE"),
            Arguments.of("1 3 6 7 9", "SAFE"),
        ).asIterable()

        @JvmStatic
        fun getDamperReports() = listOf(
            Arguments.of("7 6 4 2 1", "SAFE"),
            Arguments.of("1 2 7 8 9", "UNSAFE"),
            Arguments.of("9 7 6 2 1", "UNSAFE"),
            Arguments.of("1 3 2 4 5", "SAFE"),
            Arguments.of("8 6 4 4 1", "SAFE"),
            Arguments.of("1 3 6 7 9", "SAFE"),
        ).asIterable()
    }

    @Test
    fun `It should create a report list`() {
        val reportList = example.toReportList()

        val expected: ReportList = listOf(
            "7 6 4 2 1",
            "1 2 7 8 9",
            "9 7 6 2 1",
            "1 3 2 4 5",
            "8 6 4 4 1",
            "1 3 6 7 9",
        )

        assertEquals(expected, reportList)
    }

    @ParameterizedTest(name = "report \"{0}\" should have status {1}")
    @MethodSource("getReports")
    fun `It should get report with status`(report: Report, status: String) {
        assertEquals(ReportStatus.valueOf(status), report.status().second)
    }

    @ParameterizedTest(name = "report \"{0}\" should have status {1}")
    @MethodSource("getDamperReports")
    fun `It should get report with dampener status`(report: Report, status: String) {
        assertEquals(ReportStatus.valueOf(status), report.statusWithDampener().second)
    }

    @Test
    fun `It should count safe reports`() {
        val reportList = example.toReportList()
        val expected = 2

        assertEquals(expected, reportList.totalSafeReports())
    }

    @Test
    fun `It should count safe reports with dampener`() {
        val reportList = example.toReportList()
        val expected = 4

        assertEquals(expected, reportList.totalSafeReportsWithDampener())
    }
}