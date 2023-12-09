plugins {
    id("kotlin")
}

group = "net.mscript.object"
version = "0.0.1-DEV"

repositories {
    mavenCentral()
}

dependencies {

}

tasks.test {
    useJUnitPlatform()
}