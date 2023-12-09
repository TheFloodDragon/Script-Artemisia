plugins {
    id("kotlin")
}

group = "net.mscript.compiler"
version = "0.0.1-DEV"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":project:module-ast"))
    compileOnly(project(":project:module-runtime"))
    compileOnly(project(":project:module-utils"))
}

tasks.test {
    useJUnitPlatform()
}