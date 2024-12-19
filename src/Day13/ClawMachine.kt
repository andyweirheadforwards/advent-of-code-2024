package Day13

data class ClawMachine(val buttonA: Button, val buttonB: Button, val prizeLocation: LongPoint) {

  val cost: Long?
    get() {
      val one: Long? = calculatePresses(buttonA, buttonB).let {
        if (it == null) return null
        val (aPresses, bPresses) = it
        aPresses * buttonA.cost + bPresses * buttonB.cost
      }
      val two: Long? = calculatePresses(buttonB, buttonA).let {
        if (it == null) return null
        val (bPresses, aPresses) = it
        aPresses * buttonA.cost + bPresses * buttonB.cost
      }

      return listOfNotNull(one, two).minOrNull()
    }

  fun calculatePresses(buttonA: Button, buttonB: Button): Pair<Long, Long>? {
    val aX = buttonA.x
    val aY = buttonA.y
    val bX = buttonB.x
    val bY = buttonB.y
    val cX = prizeLocation.x
    val cY = prizeLocation.y

    // Check if the lines are parallel
    if (aX * bY == aY * bX) return null

    // Calculate the intersection point
    val t = (cX * bY - cY * bX) / (aX * bY - aY * bX)
    val intersectionX = aX * t

    // Calculate the number of A presses
    val aPresses = t.toLong()

    // Calculate the number of B presses
    val bPresses = ((cX - intersectionX) / bX).toLong()

    // Check if aX * A + bX * B = cX and aY * A + bY * B = cY
    if (aX * aPresses + bX * bPresses != cX) return null
    if (aY * aPresses + bY * bPresses != cY) return null
    if (aPresses <= 0 && bPresses < 0) return null

    return Pair(aPresses, bPresses)
  }

  override fun toString(): String =
      """
        Day13.Button A: X+${buttonA.x}, Y+${buttonA.y}
        Day13.Button B: X+${buttonB.x}, Y+${buttonB.y}
        Prize: X=${prizeLocation.x}, Y=${prizeLocation.y}
      """
          .trimIndent()

  private fun gcd(a: Int, b: Int): Int {
    return if (b == 0) a else gcd(b, a % b)
  }

  private fun extendedGcd(a: Long, b: Long): Triple<Long, Long, Long> {
    // Base case
    if (b == 0L) return Triple(a, 1L, 0L) // gcd(a, 0) = a, x = 1, y = 0

    // Recursive case
    val (gcd, x1, y1) = extendedGcd(b, a % b)
    val x = y1
    val y = x1 - (a / b) * y1
    return Triple(gcd, x, y)
  }
}
