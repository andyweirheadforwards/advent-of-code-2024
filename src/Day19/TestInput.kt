package Day19

typealias TestInput = String

typealias Towel = String

typealias Towels = Set<Towel>

typealias Designs = List<Design>

val TestInput.towels: Towels
  get() = trim().lines().first().split(",").map { it.trim() }.toSet()
val TestInput.designs: Designs
  get() = trim().lines().takeLastWhile { it.isNotBlank() }.map { it.trim() }
