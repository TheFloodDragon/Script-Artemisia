plugins {
    application
}

dependencies {
    shadowModule("module-core")
    shadowModule("module-runtime")
    shadowModule("module-util")
    shadowModule("module-compiler")
    shadowGson()
}

application {
    mainClass.set("${group}.application.MScript")
}

tasks {
    build { dependsOn(shadowJar) }
}