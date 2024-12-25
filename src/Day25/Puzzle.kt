package Day25

import Grid
import grid
import string

class Puzzle(val locks: List<Lock>, val keys: List<Key>) {
    companion object {
        operator fun invoke(input: String): Puzzle {
            val keys = mutableListOf<Key>()
            val locks = mutableListOf<Lock>()

            input.grid.windowed(7, 8, false).forEach {
                when {
                    it.isLock -> locks.add(Lock(it.string))
                    it.isKey -> keys.add(Key(it.string))
                    else -> error("Invalid input\n\n${it.string}")
                }
            }

            return Puzzle(locks, keys)
        }
    }

    fun solveOne() = locks.flatMap { lock -> keys.map { key -> key fitsIn lock } }.count { it == true }
}

val Grid.isLock: Boolean get() = first().joinToString("") == "#####" && size == 7
val Grid.isKey: Boolean get() = first().joinToString("") == "....." && size == 7
