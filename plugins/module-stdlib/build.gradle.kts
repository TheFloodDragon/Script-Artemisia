dependencies {
    compileModule("module-api")
}
tasks {
    build { dependsOn(shadowJar) }
}