import java.io.File

fun fetchDataForDay(day:Int): String = File("./data/day-$day.txt").readText().trim()
