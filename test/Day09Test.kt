import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {

  @ParameterizedTest(name = "It should expand {0} to {1}")
  @MethodSource("getExamples")
  fun `It should expand a disk map to blocks`(diskMapString: String, expected: String) {
    val blocks = diskMapString.toDiskBlocks()

    assertEquals(expected, blocks.joinToString(""))
  }

  @ParameterizedTest(name = "It should compact {1} to {2}")
  @MethodSource("getExamples")
  fun `It should compact disk blocks`(diskMapString: String, start: String, expected: String) {
    val diskBlocks: DiskBlocks = diskMapString.toDiskBlocks()
    assertEquals(expected, diskBlocks.compact().joinToString(""))
  }

  @ParameterizedTest(name = "It calculate checksum {3}")
  @MethodSource("getExamples")
  fun `It should compact disk blocks`(
      diskMapString: String,
      start: String,
      end: String,
      expected: Int
  ) {
    val diskBlocks: DiskBlocks = diskMapString.toDiskBlocks().compact()
    assertEquals(expected.toLong(), diskBlocks.checksum)
  }

  @Test
  fun `It should de-frag disk blocks`() {
    val diskBlocks: DiskBlocks = "2333133121414131402".toDiskBlocks()
    //             "00...111...2...333.44.5555.6666.777.888899"
    val expected = "00992111777.44.333....5555.6666.....8888.."

    assertEquals(expected, diskBlocks.defragmentDiskBlocks().joinToString(""))
  }

  companion object {

    @JvmStatic
    fun getExamples() =
        listOf(
                Arguments.of(
                    "12345", // disk map
                    "0..111....22222", // start blocks
                    "022111222......", // end blocks
                    60 // checksum
                    ),
                Arguments.of(
                    "2333133121414131402", // disk map
                    "00...111...2...333.44.5555.6666.777.888899", // start blocks
                    "0099811188827773336446555566..............", // end blocks
                    1928 // checksum
                    ),
                Arguments.of(
                    "233313312141413140256", // disk map
                    "00...111...2...333.44.5555.6666.777.888899.....101010101010", // start blocks
                    "0010101011110101029983338448555586666777...................", // end blocks
                    3383 // checksum
                    ),
            )
            .iterator()
  }
}
