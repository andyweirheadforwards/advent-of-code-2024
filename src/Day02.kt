import java.io.File

fun main() {
    val input: ReportListString = readInput("Day02")

    val reports = input.toReportList()

    println("How many reports are safe?     ${reports.totalSafeReports()}")
    println("How many reports are now safe? ${reports.totalSafeReportsWithDampener()}")
}

typealias ReportListString = String
typealias Report = String
typealias ReportList = List<Report>

enum class ReportStatus {
    SAFE,
    UNSAFE
}

typealias ReportWithStatus = Pair<Report, ReportStatus>
typealias ReportStatusList = List<ReportWithStatus>

fun ReportListString.toReportList(): List<String> = this.lines()

fun ReportWithStatus.isSafe() = this.second == ReportStatus.SAFE

fun Report.status(): Pair<String, ReportStatus> = this.getReportWithStatus()

fun Report.statusWithDampener(): Pair<String, ReportStatus> {
    val status = this.getReportWithStatus()
    if (status.isSafe()) return status

    val levels = this.split(" ").map {
        it.toInt()
    }

    for (index in levels.indices) {
        val dampedStatus = levels.toMutableList().apply {
            this.removeAt(index)
        }.joinToString(" ").getReportWithStatus()
        if (dampedStatus.second == ReportStatus.SAFE) return dampedStatus
    }

    return status
}

fun ReportStatusList.countSafeReports() = this.filter {
    it.isSafe()
}.size

fun ReportList.totalSafeReports() = this.map {
    it.status()
}.countSafeReports()

fun ReportList.totalSafeReportsWithDampener() = this.map {
    it.statusWithDampener()
}.countSafeReports()

fun Report.getReportWithStatus(): ReportWithStatus {
    val levels = this.split(" ").map {
        it.toInt()
    }.listIterator()

    var isIncreasing: Boolean? = null
    var previous = levels.next()

    while (levels.hasNext()) {
        val current = levels.next()
        val hasProblem = !isValidStep(isIncreasing, current, previous)

        if (hasProblem) return Pair(this, ReportStatus.UNSAFE)
        isIncreasing = current > previous
        previous = current
    }
    return Pair(this, ReportStatus.SAFE)
}

private fun isValidStep(isIncreasing: Boolean?, current: Int, previous: Int): Boolean = when {
    isIncreasing == null && current in previous + 1..previous + 3 -> true
    isIncreasing == null && current in previous - 3..previous - 1 -> true
    isIncreasing == true && current in previous + 1..previous + 3 -> true
    isIncreasing == false && current in previous - 3..previous - 1 -> true
    else -> false
}









