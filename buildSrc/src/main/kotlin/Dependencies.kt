import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.project

fun PluginAware.applyPlugins() {
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")
}

fun Project.buildDirClean() {
    @Suppress("DEPRECATION") gradle.buildFinished { buildDir.deleteRecursively() }
}

/**
 * Gson
 */
fun DependencyHandler.compileGson() {
    add(ACTION_COMPILE, "com.google.code.gson:gson:$gsonVersion")
}

fun DependencyHandler.shadowGson() {
    add(ACTION_SHADOW, "com.google.code.gson:gson:$gsonVersion")
}

/**
 * 依赖项目
 * @param name 项目名称
 */
fun DependencyHandler.compileModule(name: String, configuration: String? = null) {
    add(ACTION_COMPILE, project(":project:$name", configuration))
}

fun DependencyHandler.installModule(name: String, configuration: String? = null) {
    add(ACTION_INSTALL, project(":project:$name", configuration))
}

fun DependencyHandler.shadowModule(name: String, configuration: String? = null) {
    add(ACTION_SHADOW, project(":project:$name", configuration))
}

/**
 * 依赖所有项目
 */
fun DependencyHandler.compileAll() {
    project(":project").dependencyProject.childProjects.forEach { add(ACTION_COMPILE, it.value) }
}

private const val ACTION_COMPILE = "compileOnly"
private const val ACTION_INSTALL = "api"
private const val ACTION_SHADOW = "implementation"