package Day13

data class ClawMachine(val buttonA: Button, val buttonB: Button, val prizeLocation: LongPoint) {
  val cost: Long?
    get() {

      // Not solvable for y
      if (prizeLocation.y % extendedGcd(buttonA.y.toLong(), buttonB.y.toLong()).first != 0L)
          return null

      val aX = buttonA.x
      val aY = buttonA.y
      val bX = buttonB.x
      val bY = buttonB.y
      val cX = prizeLocation.x
      val cY = prizeLocation.y

      val (gX, x0, y0) = extendedGcd(aX.toLong(), bX.toLong())
      val gY = extendedGcd(aY.toLong(), bY.toLong()).first

      // If gcd(a, b) does not divide c, there's no solution
      if (cX % gX != 0L || cY % gY != 0L) return null

      var totalCost: Long? = null

      // Scale the solution to the original equation
      val x0Scaled = x0 * (cX / gX)
      val y0Scaled = y0 * (cX / gX)

      // Calculate the range for t
      val bXdivGx = bX / gX
      val aXdivGx = aX / gX

      val tMin = (-x0Scaled).floorDiv(bXdivGx)
      val tMax = y0Scaled.floorDiv(aXdivGx)

      // Iterate over the possible t values to find solutions
      for (t in tMin..tMax) {
        val pressesA = x0Scaled + bXdivGx * t
        val pressesB = y0Scaled - aXdivGx * t
        if (pressesA > 0 && pressesB > 0 && cY == aY * pressesA + bY * pressesB) {
          val cost = buttonA.cost * pressesA + buttonB.cost * pressesB
          totalCost = minOf(totalCost ?: cost, cost)
        }
      }

      return totalCost
    }

  override fun toString(): String =
      """
        Button A: X+${buttonA.x}, Y+${buttonA.y}
        Button B: X+${buttonB.x}, Y+${buttonB.y}
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
