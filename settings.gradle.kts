rootProject.name = "Artemisia"

apply("plugins")
apply("platform")
apply("project")

fun apply(name: String) {
    File(rootDir, name).listFiles()?.filter { it.isDirectory }?.forEach {
        include("$name:${it.name}")
    }
}