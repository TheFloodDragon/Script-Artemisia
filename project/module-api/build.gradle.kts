dependencies {
    implementation(project(mapOf("path" to ":project:module-util")))
    compileModule("module-runtime")
}