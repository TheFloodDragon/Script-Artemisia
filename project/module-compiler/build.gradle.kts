plugins {
    id("kotlin")
}

group = "net.mscript.compiler"
version = "0.0.1-DEV"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":project:module-ast"))
    implementation(project(":project:module-runtime"))
    implementation(project(":project:module-utils"))
}

tasks.test {
    useJUnitPlatform()
}