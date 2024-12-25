package Day25

import grid

data class Key(val bit: List<Int>) {
    companion object {
        operator fun invoke(input: String): Key {
            val bit = mutableListOf<Int>(0, 0, 0, 0, 0)

            val grid = input.grid

            grid.take(6).forEach { row ->
                row.forEachIndexed { index, it ->
                    if (it == '#') bit[index]++
                }
            }

            return Key(bit)
        }
    }

    val size = bit.sum()

    override fun toString(): String = bit.joinToString(",")
}

infix fun Key.fitsIn(lock: Lock): Boolean {
    if (this.size + lock.size > 25) return false
    this.bit.forEachIndexed { index, it ->
        val pin = lock.pins[index]
        if (it + pin > 5) return false
    }
    return true
}
