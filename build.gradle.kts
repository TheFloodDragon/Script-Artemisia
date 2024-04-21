import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version kotlinVersion
}

subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.huaweicloud.com/repository/maven/")
        maven("https://jitpack.io")
    }
        applyPlugins()
        tasks {
            // 测试
            test { useJUnitPlatform() }
            // 编码设置
            withType<JavaCompile> { options.encoding = "UTF-8" }
        }

        dependencies {
            // Kotlin标准库
            compileOnly(kotlin("stdlib"))
        }

        // Java 版本设置
        java {
            withJavadocJar()
            withSourcesJar()
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlin {
            compilerOptions {
                jvmTarget = JvmTarget.JVM_1_8
                freeCompilerArgs = listOf("-Xjvm-default=all")
            }
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
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}