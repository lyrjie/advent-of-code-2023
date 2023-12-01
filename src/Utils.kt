import kotlin.io.path.Path

fun getInput(name: String) = Path("src/inputs/$name.txt")

fun Any?.println() = println(this)
