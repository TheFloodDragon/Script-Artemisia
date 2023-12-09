plugins {
    id("kotlin")
}

group = "net.mscript.utils"
version = "0.0.1-DEV"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.huaweicloud.com/repository/maven/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.9")
}

tasks.test {
    useJUnitPlatform()
}