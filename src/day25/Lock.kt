package day25

import utils.grid

data class Lock(
    val pins: List<Int>,
) {
    companion object {
        operator fun invoke(input: String): Lock {
            val pins = mutableListOf(0, 0, 0, 0, 0)
            val grid = input.grid

            grid.drop(1).forEach { row ->
                row.forEachIndexed { index, it ->
                    if (it == '#') pins[index]++
                }
            }
            return Lock(pins)
        }
    }

    val size = pins.sum()

    override fun toString(): String = pins.joinToString(",")
}
