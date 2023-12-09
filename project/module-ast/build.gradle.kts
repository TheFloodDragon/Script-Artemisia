plugins {
    id("kotlin")
}

group = "net.mscript.ast"
version = "0.0.1-DEV"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":project:module-utils"))
}

tasks.test {
    useJUnitPlatform()
}