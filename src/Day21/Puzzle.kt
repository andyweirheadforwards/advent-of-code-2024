package Day21

class Puzzle {
    private val keypad = KeypadTwo()

    fun findRoute(input: String, depth: Int = 2) =
        keypad.findNumRoutes(input).flatMap { keypad.findDirRoutes(it, depth) }

    fun findComplexity(keyCode: String, depth: Int = 2): Int {
        val length = findRoute(keyCode, depth).minOf { it.length }
        val digits = "0?(\\d+)A".toRegex().find(keyCode)!!.destructured.component1().toInt()
        return length * digits
    }

    fun solveOne(input: String) = input.trim().lines().sumOf { findComplexity(it) }
    fun solveTwo(input: String) = input.trim().lines().sumOf { findComplexity(it, 25) }
}