rootProject.name = "Artemisia"

applyAll("plugin")
applyAll("platform")
applyAll("project")

fun applyAll(name: String) {
    File(rootDir, name).listFiles()?.filter { it.isDirectory }?.forEach {
        include("$name:${it.name}")
    }
}