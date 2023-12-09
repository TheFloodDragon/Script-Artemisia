plugins {
    application
}

dependencies {
    shadowModule("module-ast")
    shadowModule("module-runtime")
    shadowModule("module-utils")
    shadowModule("module-compiler")
    shadowGson()
}

application {
    mainClass.set("${group}.MScript")
}

tasks {
    build { dependsOn(shadowJar) }
}