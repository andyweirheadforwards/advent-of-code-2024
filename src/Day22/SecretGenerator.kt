package Day22

import kotlin.math.floor

typealias SecretNumberPriceChange = Pair<SecretNumber, PriceChange>

typealias PriceChangeSequence = Sequence<SecretNumberPriceChange>

typealias PriceChangeList = List<PriceChange>

typealias SequenceScore = Int

typealias SecretNumber = Long

typealias PriceChange = Int

typealias Price = Int

val SecretNumber.price: Price
  get() = this.onesDigit

class SecretGenerator(private var secretNumber: SecretNumber) {
  companion object {
    operator fun invoke(number: Int): SecretGenerator = SecretGenerator(number.toLong())
  }

  fun mix(value: Long): SecretNumber {
    secretNumber = secretNumber.xor(value)
    return secretNumber
  }

  fun prune(): SecretNumber {
    secretNumber = secretNumber % 16777216
    return secretNumber
  }

  fun price(): PriceChange = secretNumber.onesDigit

  fun evolve(count: Int): SecretNumber {
    repeat(count) {
      // Calculate the result of multiplying the secret number by 64.
      val one = secretNumber * 64
      // Then, mix this result into the secret number.
      mix(one)
      // Finally, prune the secret number.
      prune()
      // Calculate the result of dividing the secret number by 32.
      // Round the result down to the nearest integer.
      var two = floor(secretNumber.toDouble() / 32).toLong()
      // Then, mix this result into the secret number.
      mix(two)
      // Finally, prune the secret number.
      prune()
      // Calculate the result of multiplying the secret number by 2048.
      val three = secretNumber * 2048
      // Then, mix this result into the secret number.
      mix(three)
      // Finally, prune the secret number.
      prune()
    }
    return secretNumber
  }

  fun priceSequence(): PriceChangeSequence {
    val seed: SecretNumberPriceChange = Pair(secretNumber, 0)
    return generateSequence(seed) {
      evolve(1)
      val (prevSecret: SecretNumber) = it
      val priceChange = secretNumber.price - prevSecret.price
      Pair(secretNumber, priceChange)
    }
  }

  fun getSequenceScoreList(limit: Int = 2000): Map<PriceChangeList, SequenceScore> {
    val sequenceScores = mutableMapOf<PriceChangeList, SequenceScore>()
    val seenSequences = mutableSetOf<PriceChangeList>()
    var count = 0

    priceSequence()
        .windowed(SEQUENCE_LENGTH, 1, partialWindows = false)
        .takeWhile { count < limit }
        .forEach {
          val sequence = it.map { it.second }
          if (seenSequences.add(sequence)) {
            val (lastSecretNumber) = it.last()
            sequenceScores[sequence] = lastSecretNumber.price
            seenSequences.add(sequence)
          }
          count++
        }

    return sequenceScores
  }

  fun priceSequenceString(count: Int): String {
    return priceSequence()
        .take(count)
        .mapIndexed { index, secretNumberChange ->
          val (secretNumber, change) = secretNumberChange
          "${
                secretNumber.toString().padStart(8, ' ')
            }: ${secretNumber.price} ${if (index > 0) "($change)" else "   "}"
        }
        .joinToString("\n")
  }
}
