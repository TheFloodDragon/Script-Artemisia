import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    id("com.github.johnrengelman.shadow") version shadowJarVersion apply false
}

subprojects {

    applyPlugins()

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.huaweicloud.com/repository/maven/")
        maven("https://jitpack.io")
    }

    tasks {
        // 编码设置
        withType<JavaCompile> { options.encoding = "UTF-8" }
        // Kotlin Jvm 设置
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-Xjvm-default=all")
            }
        }
        // ShadowJar 基本设置
        withType<ShadowJar> {
            // Options
            archiveAppendix.set("")
            archiveClassifier.set("")
            archiveVersion.set(rootVersion)
            destinationDirectory.set(file("$rootDir/outs"))
            // Kotlin
            relocate("kotlin.", "kotlin${kotlinVersion.escapedVersion}.") { exclude("kotlin.Metadata") }
            relocate("kotlinx.", "kotlinx${kotlinVersion.escapedVersion}.")
        }
    }

    dependencies {
        // Kotlin标准库
        compileOnly(kotlin("stdlib"))
    }

    // Java 版本设置
    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // 基本信息设置
    group = rootGroup
    version = rootVersion

    // 发布
    publishing {
        publications {
            register<MavenPublication>("maven") { from(components["java"]) }
        }
    }

}

buildDirClean()