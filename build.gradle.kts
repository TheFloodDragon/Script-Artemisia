import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.huaweicloud.com/repository/maven/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.9")
}

application {
    mainClass.set("${group}.MScript")
}

tasks {
    test {
        useJUnitPlatform()
    }
    build { dependsOn(shadowJar) }
    withType<ShadowJar> {
        relocate("kotlin.", "kotlin1921.")
    }
    // 编码设置
    withType<JavaCompile> { options.encoding = "UTF-8" }
    // Kotlin Jvm 设置
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }
}

// Java 版本设置
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// 发布
publishing {
    publications {
        register<MavenPublication>("publishToMavenLocal") {
            from(components["java"])
        }
    }
}