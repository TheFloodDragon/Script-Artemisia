rootProject.name = "MScript"

apply("platform")
apply("project")
apply("scripts")

fun apply(name: String) {
    File(rootDir, name).listFiles()?.filter { it.isDirectory }?.forEach {
        include("$name:${it.name}")
    }
}

//rootProject.children.forEach { project ->
//    project.buildFileName = "${project.name}.gradle.kts"
//    project.children.forEach { p ->
//        p.buildFileName = "${p.name}.gradle.kts"
//    }
//}