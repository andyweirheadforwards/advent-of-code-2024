import Day23.LanParty
import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    @Test
    fun `It should find networks of three computers`() {
        val expected = """
            co,de,ta
            co,ka,ta
            de,ka,ta
            qp,td,wh
            tb,vc,wq
            tc,td,wh
            td,wh,yn
        """.trimIndent()

        val lanParty = LanParty(testInput)

        assertEquals(expected, lanParty.findNetworksOfThreeNodes().joinToString("\n") { it.joinToString(",") })
    }

    @Test
    fun `It should solve part one`() {
        val expected = 1173
        val input = readInput("Day23")
        val lanParty = LanParty(input)

        assertEquals(expected, lanParty.solvePartOne())
    }

    @Test
    fun `It should find networks of interconnected computers`() {
        val expected = listOf("co", "de", "ka", "ta")
        val lanParty = LanParty(testInput)
        val interconnectedComputers = lanParty.findNetworksOfInterconnectedNodes()

        assertEquals(expected, interconnectedComputers.first())
    }

    @Test
    fun `It should find password`() {
        val expected = "co,de,ka,ta"
        val lanParty = LanParty(testInput)
        val password = lanParty.findPassword()

        assertEquals(expected, password)
    }

    @Test
    fun `It should solve part two`() {
        val expected = "cm,de,ez,gv,hg,iy,or,pw,qu,rs,sn,uc,wq"
        val input = readInput("Day23")
        val lanParty = LanParty(input)
        val solution = lanParty.solvePartTwo()

        assertEquals(expected, solution)
    }

    companion object {
        val testInput = """
            kh-tc
            qp-kh
            de-cg
            ka-co
            yn-aq
            qp-ub
            cg-tb
            vc-aq
            tb-ka
            wh-tc
            yn-cg
            kh-ub
            ta-co
            de-co
            tc-td
            tb-wq
            wh-td
            ta-ka
            td-qp
            aq-cg
            wq-ub
            ub-vc
            de-ta
            wq-aq
            wq-vc
            wh-yn
            ka-de
            kh-ta
            co-tc
            wh-qp
            tb-vc
            td-yn
        """.trimIndent()
    }
}
