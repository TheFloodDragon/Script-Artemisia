import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}

group = "net.mugwort.dev"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        name = "Huawei Maven"
        setUrl("https://repo.huaweicloud.com/repository/maven/")
    }
    maven {
        name = "spigotmc-repo"
        setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        setUrl("https://maven.google.com")
    }
    google()
    maven { setUrl("https://jitpack.io") }
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.7")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.named("build") {
    dependsOn("shadowJar")
}

tasks.withType<ShadowJar>{
    archiveFileName.set(tasks.named<Jar>("jar").get().archiveFileName.get())
    relocate("kotlin","kotlin190")
}

application {
    mainClass.set("${group}.MScript")
}