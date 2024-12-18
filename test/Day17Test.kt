import Day17.Computer
import Day17.REGISTER_A
import Day17.REGISTER_B
import Day17.REGISTER_C
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day17Test {

  @Test
  fun `It should parse testInput`() {
    val computer = Computer(testInput)

    assertEquals(testInput, computer.toString())
  }

  @Test
  fun advTest() {
    val input =
        """
            Register A: 8
            Register B: 0
            Register C: 0

            Program: 0,2
        """
            .trimIndent()

    val computer = Computer(input)
    computer.run()
    assertEquals(2, computer.getRegisterValue(REGISTER_A))
  }

  @Test
  fun bxlTest() {
    val input =
        """
            Register A: 0
            Register B: 3
            Register C: 0

            Program: 1,4
        """
            .trimIndent()

    val computer = Computer(input)
    computer.run()
    assertEquals(7, computer.getRegisterValue(REGISTER_B))
  }

  @Test
  fun bstTest() {
    val input =
        """
            Register A: 0
            Register B: 0
            Register C: 14

            Program: 2,6
        """
            .trimIndent()

    val computer = Computer(input)
    computer.run()
    assertEquals(6, computer.getRegisterValue(REGISTER_B))
  }

  @Test
  fun jnzTest() {
    val input =
        """
            Register A: 0
            Register B: 1
            Register C: 2

            Program: 3,3
        """
            .trimIndent()

    val computer = Computer(input)
    val result = computer.output
    assertEquals(0, computer.getRegisterValue(REGISTER_A))
    assertEquals(1, computer.getRegisterValue(REGISTER_B))
    assertEquals(2, computer.getRegisterValue(REGISTER_C))
    assertEquals("", result)
  }

  @Test
  fun bxcTest() {
    val input =
        """
            Register A: 0
            Register B: 3
            Register C: 5

            Program: 4,0
        """
            .trimIndent()

    val computer = Computer(input)
    computer.run()
    assertEquals(6, computer.getRegisterValue(REGISTER_B))
  }

  @Test
  fun `If register C contains 9, the program 2,6 would set register B to 1`() {
    val input =
        """
            Register A: 0
            Register B: 3
            Register C: 4

            Program: 4,6
        """
            .trimIndent()

    val computer = Computer(input)
    computer.run()
    assertEquals(7, computer.getRegisterValue(REGISTER_B))
  }

  @Test
  fun outTest() {
    val input =
        """
            Register A: 0
            Register B: 0
            Register C: 0

            Program: 5,3
        """
            .trimIndent()

    val computer = Computer(input)
    val result = computer.output
    assertEquals("3", result)
  }

  @Test
  fun bdvTest() {
    val input =
        """
            Register A: 8
            Register B: 0
            Register C: 0

            Program: 6,2
        """
            .trimIndent()

    val computer = Computer(input)
    computer.run()
    assertEquals(2, computer.getRegisterValue(REGISTER_B))
  }

  @Test
  fun cdvTest() {
    val input =
        """
            Register A: 8
            Register B: 0
            Register C: 0

            Program: 7,2
        """
            .trimIndent()

    val computer = Computer(input)
    computer.run()
    assertEquals(2, computer.getRegisterValue(REGISTER_C))
  }

  @Test
  fun `If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2`() {
    val input =
        """
            Register A: 10
            Register B: 0
            Register C: 9

            Program: 5,0,5,1,5,4
        """
            .trimIndent()

    val computer = Computer(input)
    val output = computer.output
    assertEquals("0,1,2", output)
  }

  @Test
  fun `If register B contains 29, the program 1,7 would set register B to 26`() {
    val input =
        """
            Register A: 0
            Register B: 29
            Register C: 0

            Program: 1,7
        """
            .trimIndent()

    val computer = Computer(input)
    computer.run()
    assertEquals(26, computer.getRegisterValue(REGISTER_B))
  }

  @Test
  fun `If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354`() {
    val input =
        """
            Register A: 0
            Register B: 2024
            Register C: 43690

            Program: 4,0
        """
            .trimIndent()

    val computer = Computer(input)
    computer.run()
    assertEquals(44354, computer.getRegisterValue(REGISTER_B))
  }

  @Test
  fun `If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A`() {
    val input =
        """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
        """
            .trimIndent()

    val computer = Computer(input)
    val output = computer.output
    assertEquals("4,2,5,6,7,7,7,7,3,1,0", output)
    assertEquals(0, computer.getRegisterValue(REGISTER_A))
  }

  @Test
  fun `It should output the correct result for testInput`() {
    val computer = Computer(testInput)
    val output = computer.output
    assertEquals("4,6,3,5,6,3,5,2,1,0", output)
  }

  @Test
  fun `It should search for the value of A that results in outputting the input program`() {
    val input =
        """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,3,5,4,3,0
        """
            .trimIndent()

    val computer = Computer(input)
    val solution = computer.searchForA()
    assertEquals(117440L, solution)
  }

  companion object {
    val testInput =
        """
            Register A: 729
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
        """
            .trimIndent()
  }
}
