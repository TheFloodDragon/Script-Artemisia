plugins {
    application
}

dependencies {
    implementation("org.fusesource.jansi:jansi:2.1.1")
    shadowModule("module-core")
    shadowModule("module-runtime")
    shadowModule("module-util")
    shadowModule("module-compiler")
    shadowModule("module-api")
    shadowGson()
}

application {
    mainClass.set("${group}.application.MScript")
}

tasks {
    build { dependsOn(shadowJar) }
}