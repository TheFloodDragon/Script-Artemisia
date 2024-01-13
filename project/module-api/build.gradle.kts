dependencies {
    implementation(project(mapOf("path" to ":project:module-util")))
    implementation(project(mapOf("path" to ":project:module-core")))
    implementation(project(mapOf("path" to ":project:module-core")))
    shadowGson()
    compileOnly("org.fusesource.jansi:jansi:2.1.1")
    compileModule("module-runtime")
}