import kotlin.io.path.Path
import kotlin.io.path.readText

fun readInput(name: String) = Path("data/$name.txt").readText().trim().lines().joinToString("\n")

