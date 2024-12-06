import java.util.Collections

fun main() {
    val input = readInput("Day05")

    val rules: RuleList = input.trim().lines().takeWhile {
        it.isNotBlank()
    }.joinToString("\n").toRuleList()

    val updates: ManualUpdatesList = input.trim().lines().takeLastWhile {
        it.isNotBlank()
    }.joinToString("\n").toManualUpdatesList()

    val sumOfMiddlePages: Int = updates.filterCorrectUpdates(rules).sumOfMiddlePages()
    val sumOfCorrectedMiddlePages: Int = updates.filterIncorrectUpdates(rules).map {
        it.fix(rules)
    }.sumOfMiddlePages()

    println("What do you get if you add up the middle page number from those correctly-ordered updates?         $sumOfMiddlePages")
    println("What do you get if you add up the middle page numbers after correctly ordering just those updates? $sumOfCorrectedMiddlePages")
}

typealias ManualUpdatesString = String
typealias ManualUpdatesList = List<ManualUpdate>
typealias ManualUpdate = List<Int>

typealias RuleListString = String
typealias RuleList = List<Rule>
typealias Rule = Pair<Int, Int>

fun ManualUpdatesString.toManualUpdatesList() = trim().lines().map {
    it.split(',').map {
        it.trim().toInt()
    }
}

fun ManualUpdatesList.filterCorrectUpdates(rules: RuleList): ManualUpdatesList = filter {
    it.isCorrect(rules)
}

fun ManualUpdatesList.filterIncorrectUpdates(rules: RuleList): ManualUpdatesList = filter {
    !it.isCorrect(rules)
}

fun ManualUpdatesList.sumOfMiddlePages(): Int = map {
    it[(it.size / 2)].toInt()
}.sumOf {
    it
}

fun ManualUpdate.isCorrect(rules: RuleList): Boolean {
    rules.forEach {
        if (checkRule(it) == false) return false
    }
    return true
}

fun ManualUpdate.fix(rules: RuleList): ManualUpdate {
    var fixedUpdate = this
    do {
        rules.forEach {
            fixedUpdate = fixedUpdate.fixRule(it)
        }
    } while (!fixedUpdate.isCorrect(rules))
    return fixedUpdate
}

fun ManualUpdate.checkRule(rule: Rule): Boolean {
    val indexOfFirst = indexOf(rule.first)
    val indexOfSecond = indexOf(rule.second)

    return when {
        indexOfFirst == -1 -> true
        indexOfSecond == -1 -> true
        indexOfFirst < indexOfSecond -> true
        else -> false
    }
}

fun ManualUpdate.fixRule(rule: Rule): ManualUpdate {

    if (checkRule(rule)) return this

    val fixedUpdate = toMutableList()
    val indexOfFirst = indexOf(rule.first)
    val indexOfSecond = indexOf(rule.second)

    Collections.swap(fixedUpdate, indexOfFirst, indexOfSecond)

    return fixedUpdate.toList()
}

fun RuleListString.toRuleList(): RuleList = trim().lines().map {
    it.split('|').map {
        it.toInt()
    }
}.map {
    Rule(it.first(), it.last())
}

