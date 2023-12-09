rootProject.name = "MScript"

apply("platform")
apply("project")

fun apply(name: String) {
    File(rootDir, name).listFiles()?.filter { it.isDirectory }?.forEach {
        println(":$name:${it.name}")
        include("$name:${it.name}")
        findProject(":$name:${it.name}")?.name = it.name
    }
}

//rootProject.children.forEach { project ->
//    project.buildFileName = "${project.name}.gradle.kts"
//    project.children.forEach { p ->
//        p.buildFileName = "${p.name}.gradle.kts"
//    }
//}




