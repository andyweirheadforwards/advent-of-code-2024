package Day17

import kotlin.math.pow

const val REGISTER_A = 'A'

const val REGISTER_B = 'B'

const val REGISTER_C = 'C'

typealias Operand = Int

class Computer
private constructor(
    val initialA: Long,
    val initialB: Long,
    val initialC: Long,
    private val program: CharArray,
) {

  companion object {
    operator fun invoke(input: String): Computer {

      val registerA = "Register A: (\\d+)".toRegex().find(input)!!.groupValues[1].toLong()
      val registerB = "Register B: (\\d+)".toRegex().find(input)!!.groupValues[1].toLong()
      val registerC = "Register C: (\\d+)".toRegex().find(input)!!.groupValues[1].toLong()

      val program =
          "Program: (\\d,?)+"
              .toRegex()
              .find(input)!!
              .groupValues[0]
              .split(": ")[1]
              .split(",")
              .map { it.first() }
              .toCharArray()

      return Computer(registerA, registerB, registerC, program)
    }
  }

  private val registers: MutableMap<Char, Long> =
      mutableMapOf(
          REGISTER_A to initialA,
          REGISTER_B to initialB,
          REGISTER_C to initialC,
      )

  private var instructionPointer = 0

  val output: String
    get() = run().joinToString(",")

  fun searchForA(): Long = backtrack()

  fun getRegisterValue(register: Char): Long = registers[register] ?: 0L

  override fun toString(): String =
      """
        Register A: ${registers[REGISTER_A]}
        Register B: ${registers[REGISTER_B]}
        Register C: ${registers[REGISTER_C]}

        Program: ${program.joinToString(",")}
      """
          .trimIndent()

  fun run(): CharArray {
    var output = charArrayOf()

    while (instructionPointer < program.lastIndex) {
      val opcode = Opcode.entries[program[instructionPointer].digitToInt()]
      val operand = program[instructionPointer + 1].digitToInt()

      when (opcode) {
        Opcode.ADV -> advOp(operand)
        Opcode.BXL -> bxlOp(operand)
        Opcode.BST -> bstOp(operand)
        Opcode.JNZ -> if (jnzOp(operand)) continue
        Opcode.BXC -> bxcOp(operand)
        Opcode.OUT -> output += outOp(operand)
        Opcode.BDV -> bdvOp(operand)
        Opcode.CDV -> cdvOp(operand)
        else -> error("Invalid opcode $opcode")
      }
      instructionPointer += 2
    }
    return output
  }

  private fun backtrack(currA: Long = 0L, index: Int = program.lastIndex): Long {
    for (a in 0..Long.MAX_VALUE) {
      reset()

      val prevA = (currA * 8) + a
      registers[REGISTER_A] = prevA

      val output = run()
      val target = program.drop(index).toCharArray()

      if (output.contentEquals(target)) {
        return if (index > 0) backtrack(prevA, index - 1) else prevA
      }
    }
    return 0L
  }

  private fun advOp(operand: Operand) {
    registers[REGISTER_A] =
        registers[REGISTER_A]!!.div(2.toDouble().pow((operand.combo % 8).toDouble())).toLong()
  }

  private fun bxlOp(operand: Operand) {
    registers[REGISTER_B] = registers[REGISTER_B]!!.xor(operand.literal.toLong())
  }

  private fun bstOp(operand: Operand) {
    registers[REGISTER_B] = operand.combo % 8
  }

  private fun jnzOp(operand: Operand): Boolean {
    val isJnz = registers[REGISTER_A] != 0L
    if (isJnz) instructionPointer = operand.literal
    return isJnz
  }

  private fun bxcOp(operand: Operand) {
    registers[REGISTER_B] = registers[REGISTER_B]!!.xor(registers[REGISTER_C]!!)
  }

  private fun outOp(operand: Operand): Char = (operand.combo % 8).toInt().digitToChar()

  private fun bdvOp(operand: Operand) {
    registers[REGISTER_B] =
        registers[REGISTER_A]!!.div(2.0.pow((operand.combo % 8).toDouble()).toInt())
  }

  private fun cdvOp(operand: Operand) {
    registers[REGISTER_C] =
        registers[REGISTER_A]!!.div(2.0.pow((operand.combo % 8).toDouble()).toInt())
  }

  private fun reset() {
    registers[REGISTER_A] = initialA
    registers[REGISTER_B] = initialB
    registers[REGISTER_C] = initialC
    instructionPointer = 0
  }

  private val Operand.literal
    get() = this

  private val Operand.combo: Long
    get() =
        when (this) {
          in 0..3 -> literal.toLong()
          4 -> registers[REGISTER_A]!!
          5 -> registers[REGISTER_B]!!
          6 -> registers[REGISTER_C]!!
          7 -> error("Invalid operand - reserved")
          else -> error("Invalid operand")
        }
}
