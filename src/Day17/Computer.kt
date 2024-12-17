package Day17

import kotlin.math.pow

const val REGISTER_A = 'A'

const val REGISTER_B = 'B'

const val REGISTER_C = 'C'

typealias Operand = Char

typealias Opcode = Char

class Computer
private constructor(
    registerA: Int,
    registerB: Int,
    registerC: Int,
    private val program: CharArray,
) {
  companion object {
    operator fun invoke(input: String): Computer {

      val registerA = "Register A: (\\d+)".toRegex().find(input)!!.groupValues[1].toInt()
      val registerB = "Register B: (\\d+)".toRegex().find(input)!!.groupValues[1].toInt()
      val registerC = "Register C: (\\d+)".toRegex().find(input)!!.groupValues[1].toInt()

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

  private var instructionPointer = 0

  fun run(): String {
    var output = charArrayOf()

    while (instructionPointer < program.lastIndex) {
      val opcode = opcodes[program[instructionPointer]]
      val operand = program[instructionPointer + 1]

      when (opcode) {
        // 0
        "adv" ->
            registers[REGISTER_A] =
                registers[REGISTER_A]!!.div(2.0.pow((operand.combo % 8).toDouble()).toInt())
        // 1
        "bxl" -> registers[REGISTER_B] = registers[REGISTER_B]!!.xor(operand.literal)
        // 2
        "bst" -> registers[REGISTER_B] = operand.combo % 8
        // 3
        "jnz" ->
            if (registers[REGISTER_A] != 0) {
              instructionPointer = operand.literal
              continue
            }
        // 4
        "bxc" -> registers[REGISTER_B] = registers[REGISTER_B]!!.xor(registers[REGISTER_C]!!)
        // 5
        "out" -> output += (operand.combo % 8).digitToChar()
        // 6
        "bdv" ->
            registers[REGISTER_B] =
                registers[REGISTER_A]!!.div(2.0.pow((operand.combo % 8).toDouble()).toInt())
        // 7
        "cdv" ->
            registers[REGISTER_C] =
                registers[REGISTER_A]!!.div(2.0.pow((operand.combo % 8).toDouble()).toInt())
        else -> error("Invalid opcode $opcode")
      }
      instructionPointer += 2
    }
    return output.joinToString(",") { "$it" }
  }

  fun getRegisterValue(register: Char): Int = registers[register] ?: 0

  override fun toString(): String =
      """
        Register A: ${registers[REGISTER_A]}
        Register B: ${registers[REGISTER_B]}
        Register C: ${registers[REGISTER_C]}

        Program: ${program.joinToString(",")}
      """
          .trimIndent()

  private val registers =
      mutableMapOf<Char, Int>(
          REGISTER_A to registerA,
          REGISTER_B to registerB,
          REGISTER_C to registerC,
      )

  private val opcodes =
      mapOf<Char, String>(
          '0' to "adv",
          '1' to "bxl",
          '2' to "bst",
          '3' to "jnz",
          '4' to "bxc",
          '5' to "out",
          '6' to "bdv",
          '7' to "cdv",
      )

  private val Operand.literal
    get() = "$this".toInt(8)

  private val Operand.combo: Int
    get() =
        when (this) {
          in '0'..'3' -> "${this.literal}".toInt(8)
          '4' -> registers[REGISTER_A]!!
          '5' -> registers[REGISTER_B]!!
          '6' -> registers[REGISTER_C]!!
          '7' -> error("Invalid operand - reserved")
          else -> error("Invalid operand")
        }
}

val Pair<Char, Char>.opcode
  get() = first
val Pair<Char, Char>.operand
  get() = second
