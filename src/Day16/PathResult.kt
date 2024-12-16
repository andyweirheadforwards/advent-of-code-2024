package Day16

data class PathResult(private val path: List<MazeNode>) {

  val nodes: Int
    get() = path.size

  val score: Int
    get() = path.last().score

  val steps: Int
    get() = path.size - 1 - turns

  val turns: Int
    get() {
      var turns = 0
      for (i in 1 until path.size) {
        if (path[i].direction != path[i - 1].direction) {
          turns++
        }
      }
      return turns
    }
}
