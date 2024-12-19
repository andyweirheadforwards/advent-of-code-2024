package Day19

typealias Design = String

fun Design.canReconstructWays(towels: Towels): Long {
  val memo = mutableMapOf<String, Long>()

  fun backtrack(remaining: String): Long {
    if (remaining.isEmpty()) return 1L
    if (remaining in memo) return memo[remaining]!!

    var count = 0L
    for (towel in towels) {
      if (remaining.startsWith(towel)) {
        count += backtrack(remaining.removePrefix(towel))
      }
    }

    memo[remaining] = count
    return count
  }

  return backtrack(this)
}

fun Design.canReconstruct(towels: Towels): Boolean = canReconstructWays(towels) > 0
