import java.util.*
import kotlin.time.measureTime

fun main() {
  measureTime {
        repeat(PROFILE_REPEAT) {
          val input = readInput("Day09")

          val checksumOne = input.toDiskBlocks().compact().checksum
          println("What is the resulting filesystem checksum? $checksumOne")

          val checksumTwo = input.toDiskBlocks().defragmentDiskBlocks().checksum
          println("What is the resulting filesystem checksum? $checksumTwo")
        }
      }
      .let { println("\nAverage time taken: ${it / PROFILE_REPEAT}") }
}

typealias DiskMap = String

typealias DiskBlocks = MutableList<String>

private const val FREE_BLOCK = "."

fun DiskMap.toDiskBlocks(): DiskBlocks =
    toList()
        .chunked(2)
        .flatMapIndexed() { fileId, chunk ->
          val blocks = chunk.first().digitToInt()
          val spaces = chunk.getOrNull(1)?.digitToInt() ?: 0
          List(blocks) { "$fileId" } + List(spaces) { FREE_BLOCK }
        }
        .toMutableList()

fun DiskBlocks.compact(): DiskBlocks {

  var nextFreeBlock = indexOfFirst { it == FREE_BLOCK }
  var nextBlock = indexOfLast { it != FREE_BLOCK }

  while (nextFreeBlock < nextBlock) {
    Collections.swap(this, nextBlock, nextFreeBlock)

    nextFreeBlock = indexOfFirst { it == FREE_BLOCK }
    nextBlock = indexOfLast { it != FREE_BLOCK }
  }
  return this
}

fun DiskBlocks.defragmentDiskBlocks(): DiskBlocks {

  val fileIds: List<String> = toSet().filter { it != FREE_BLOCK }.reversed()

  fileIds.forEach { fileId ->
    var file = getFile(fileId)
    var freeSpace = getFreeSpace(file.size)

    if (freeSpace != null && freeSpace.startIndex < file.startIndex) moveFile(file, freeSpace)
  }

  return this
}

fun DiskBlocks.getFile(fileId: String): File {
  val lastIndex = lastIndexOf(fileId)
  require(lastIndex != -1) { "File $fileId not found" }

  val startIndex = (lastIndex downTo 0).firstOrNull { this[it] != fileId } ?: -1
  val size = lastIndex - startIndex
  return File(fileId, startIndex + 1, size)
}

fun DiskBlocks.getFreeSpace(targetSize: Int): FreeSpace? {
  var lastIndex = -1
  var size = 0
  val iterator = iterator().withIndex()
  while (iterator.hasNext() && size < targetSize) {
    val next = iterator.next()
    if (next.value == FREE_BLOCK) {
      lastIndex = next.index
      size++
    } else {
      lastIndex = -1
      size = 0
    }
  }
  if (lastIndex == -1) return null

  val index = lastIndex - size + 1
  return if (size >= targetSize) FreeSpace(index, size) else null
}

fun DiskBlocks.moveFile(file: File, space: FreeSpace) {
  fillRange(file.startIndex..file.startIndex + file.size - 1, FREE_BLOCK)
  fillRange(space.startIndex..space.startIndex + file.size - 1, file.id)
}

fun DiskBlocks.fillRange(range: IntRange, value: String) {
  range.forEach { this[it] = value }
}

val DiskBlocks.checksum: Long
  get() =
      mapIndexed { index, blockId -> if (blockId != FREE_BLOCK) index * blockId.toLong() else 0 }
          .sumOf { it }

data class FreeSpace(val startIndex: Int, val size: Int)

data class File(val id: String, val startIndex: Int, val size: Int)
