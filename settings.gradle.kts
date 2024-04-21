rootProject.name = "Artemisia-main"

applyAll("plugin")
applyAll("platform")
applyAll("project")

pluginManagement{
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.huaweicloud.com/repository/maven/")
        maven("https://jitpack.io")
    }
}

fun applyAll(name: String) {
    File(rootDir, name).listFiles()?.filter { it.isDirectory }?.forEach {
        include("$name:${it.name}")
    }
}
