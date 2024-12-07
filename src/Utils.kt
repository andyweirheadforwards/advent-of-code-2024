import kotlin.io.path.Path
import kotlin.io.path.readText

val PROFILE_REPEAT = 1

fun readInput(name: String) = Path("data/$name.txt").readText().trim().lines().joinToString("\n")
