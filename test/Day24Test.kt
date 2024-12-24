import Day24.MonitoringDevice
import Day24.findSolutionTwo
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Day24Test {

    @Test
    fun `It should initialise`() {
        val expected = smallTestInput
        val monitoringDevice = MonitoringDevice(expected)

        assertEquals(expected, monitoringDevice.toString())
    }

    @Test
    fun `It should get binary value - small`() {
        val expected = "0100"
        val monitoringDevice = MonitoringDevice(smallTestInput)

        assertEquals(expected, monitoringDevice.getOutputBinary())
    }

    @Test
    fun `It should get binary value -  big`() {
        val expected = "0011111101000"
        val monitoringDevice = MonitoringDevice(testInput)

        assertEquals(expected, monitoringDevice.getOutputBinary())
    }

    @Test
    fun `It should get decimal value - small`() {
        val expected = 4L
        val monitoringDevice = MonitoringDevice(smallTestInput)

        assertEquals(expected, monitoringDevice.getOutputDecimal())
    }

    @Test
    fun `It should get decimal value - big`() {
        val expected = 2024L
        val monitoringDevice = MonitoringDevice(testInput)

        assertEquals(expected, monitoringDevice.getOutputDecimal())
    }

    @Test
    fun `It should solve part one`() {
        val expected = 51410244478064L
        val input = readInput("Day24")
        val monitoringDevice = MonitoringDevice(input)

        assertEquals(expected, monitoringDevice.getOutputDecimal())
    }

    @Test
    fun `It should calculate zValue`() {
        val expected = "11000"
        val input = """
            x00: 1
            x01: 1
            x02: 0
            x03: 1
            y00: 1
            y01: 0
            y02: 1
            y03: 1
            
            x00 AND y00 -> z00
        """.trimIndent()

        val monitoringDevice = MonitoringDevice(input)

        assertEquals(expected, monitoringDevice.zValue)
    }

    @ParameterizedTest(name = "It should be x {1} - y {2}")
    @MethodSource("getXyValues")
    fun `It should get starting values`(input: String, x: String, y: String, z: String) {
        val monitoringDevice = MonitoringDevice(input)

        assertEquals(x, monitoringDevice.xValue, "x")
        assertEquals(y, monitoringDevice.yValue, "y")
        assertEquals(z, monitoringDevice.zValue, "z")
    }

    @ParameterizedTest(name = "It should be {3} for x: {1} y: {2}")
    @MethodSource("getInvalidXyValues")
    fun `It find solution two`(input: String, x: String, y: String, expected: String) {
        val result = runBlocking { findSolutionTwo(input, 2) }

        assertEquals(expected, result)
    }

    companion object {
        val smallTestInput = """
            x00: 1
            x01: 1
            x02: 1
            y00: 0
            y01: 1
            y02: 0

            x00 AND y00 -> z00
            x01 XOR y01 -> z01
            x02 OR y02 -> z02
        """.trimIndent()

        val testInput = """
            x00: 1
            x01: 0
            x02: 1
            x03: 1
            x04: 0
            y00: 1
            y01: 1
            y02: 1
            y03: 1
            y04: 1

            ntg XOR fgs -> mjb
            y02 OR x01 -> tnw
            kwq OR kpj -> z05
            x00 OR x03 -> fst
            tgd XOR rvg -> z01
            vdt OR tnw -> bfw
            bfw AND frj -> z10
            ffh OR nrd -> bqk
            y00 AND y03 -> djm
            y03 OR y00 -> psh
            bqk OR frj -> z08
            tnw OR fst -> frj
            gnj AND tgd -> z11
            bfw XOR mjb -> z00
            x03 OR x00 -> vdt
            gnj AND wpb -> z02
            x04 AND y00 -> kjc
            djm OR pbm -> qhw
            nrd AND vdt -> hwm
            kjc AND fst -> rvg
            y04 OR y02 -> fgs
            y01 AND x02 -> pbm
            ntg OR kjc -> kwq
            psh XOR fgs -> tgd
            qhw XOR tgd -> z09
            pbm OR djm -> kpj
            x03 XOR y03 -> ffh
            x00 XOR y04 -> ntg
            bfw OR bqk -> z06
            nrd XOR fgs -> wpb
            frj XOR qhw -> z04
            bqk OR frj -> z07
            y03 OR x01 -> nrd
            hwm AND bqk -> z03
            tgd XOR rvg -> z12
            tnw OR pbm -> gnj
        """.trimIndent()

        val invalidGatesInputSmall = """
            x00: 0
            x01: 1
            x02: 0
            x03: 0
            y00: 1
            y01: 0
            y02: 0
            y03: 0

            x00 OR y00 -> z01
            x01 OR y01 -> z00
            x02 OR y02 -> z03
            x03 OR y03 -> z02

        """.trimIndent()

        val invalidGatesInput = """
            x00: 0
            x01: 1
            x02: 0
            x03: 1
            x04: 0
            x05: 1
            y00: 0
            y01: 0
            y02: 1
            y03: 1
            y04: 0
            y05: 1
            
            x00 AND y00 -> z05
            x01 AND y01 -> z02
            x02 AND y02 -> z01
            x03 AND y03 -> z03
            x04 AND y04 -> z04
            x05 AND y05 -> z00
        """.trimIndent()

        @JvmStatic
        fun getXyValues() = listOf(
            Arguments.of(smallTestInput, "111", "010", "1001"), // small input
            Arguments.of(testInput, "01101", "11111", "101100"), // test input
            Arguments.of(invalidGatesInput, "101010", "101100", "1010110"), // invalid gates input
            Arguments.of(
                readInput("Day24"),
                "110001011010001110001010000101001110001111001".reversed(),
                "101100110111101001010111011011010001111101011".reversed(),
                "1011101011111111011111001111000010010001110000"
            ), // input
        ).iterator()

        @JvmStatic
        fun getInvalidXyValues() = listOf(
            Arguments.of(invalidGatesInputSmall, "10", "01", "z00,z01,z02,z03"), // invalid gates input
            Arguments.of(invalidGatesInput, "101010", "101100", "z00,z01,z02,z05"), // invalid gates input
//            Arguments.of(
//                readInput("Day24"),
//                "110001011010001110001010000101001110001111001".reversed(),
//                "101100110111101001010111011011010001111101011".reversed(),
//                "1011101011111111011111001111000010010001110000"
//            ), // input
        ).iterator()
    }
}
