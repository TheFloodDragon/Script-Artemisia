plugins {
    id("kotlin")
}

group = "net.mscript.runtime"
version = "0.0.1-DEV"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":project:module-utils"))
}

tasks.test {
    useJUnitPlatform()
}