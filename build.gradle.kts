import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version kotlinVersion
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
        // 测试
        test { useJUnitPlatform() }
        // 编码设置
        withType<JavaCompile> { options.encoding = "UTF-8" }
    }

    dependencies {
        // Kotlin标准库
        //compileOnly(kotlin("stdlib"))
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